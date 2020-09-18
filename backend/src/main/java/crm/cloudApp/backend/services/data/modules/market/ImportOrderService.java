package crm.cloudApp.backend.services.data.modules.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.data.modules.market.ImportOrderDTO;
import crm.cloudApp.backend.mappers.data.modules.market.ImportOrderMapper;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.models.data.modules.market.ImportOrder;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.repositories.data.modules.market.ImportOrderRepository;
import crm.cloudApp.backend.services.users.UserService;
import crm.cloudApp.backend.utils.files.OpenCsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Validated
@Transactional
public class ImportOrderService {

    private final UserService userService;
    private final ImportOrderRepository importOrderRepository;
    private final ImportOrderMapper importOrderMapper;

    public ImportOrderService(UserService userService,
                              ImportOrderRepository importOrderRepository,
                              ImportOrderMapper importOrderMapper) {
        this.userService = userService;
        this.importOrderRepository = importOrderRepository;
        this.importOrderMapper = importOrderMapper;
    }

    public List<ImportOrder> parseCsvAndGenerateImportOrders(MessageInfo messageinfo,
                                                             String marketCode) throws IOException {

        List<ImportOrder> importOrders = new ArrayList<>();
        List<String[]> csvData = OpenCsvUtil.csvInList(messageinfo.getMessage().getEncryptedMessage());

        User user = userService.getLoggedInUser();
        Boolean firstRow = true;
        int csvLineNumber = 1;

        for (String[] csvLine : csvData) {

            if (firstRow) {
                firstRow = false;
                continue;
            }

            ImportOrder importOrder = this.parseRow(csvLine, csvLineNumber);
            importOrder.setUserId(user.getId());
            importOrder.setStatus(AppConstants.Types.ImportOrderStatus.NOT_EXECUTED);
            importOrder.setMessageInfoId(messageinfo.getId());
            importOrder.setMessageInfoName(messageinfo.getName());
            importOrder.setMarketCode(marketCode);
            importOrder.setCreatedBy(userService.getLoggedInUserName());
            importOrder.setCreatedOn(Instant.now());
            importOrder.setModifiedBy(userService.getLoggedInUserName());
            importOrder.setModifiedOn(Instant.now());
            importOrders.add(importOrder);

            csvLineNumber++;
        }

        return importOrders;
    }


    private ImportOrder parseRow(String[] csvLine, int csvLineNumber) {

        ImportOrder importOrder = new ImportOrder();
        String errorMessage = "Error at line ";

        if (!csvLine[0].matches("[0-9]+")) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    errorMessage + csvLineNumber + ". Id column should be an Integer value");
        }
        importOrder.setOrder_ID(Long.parseLong(csvLine[0]));

        if (!(csvLine[1].toLowerCase().equals("place") || csvLine[1].toLowerCase().equals("modify"))
        ) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    errorMessage + csvLineNumber
                            + ". Action column should contain 'Place' or 'Modify'.");
        }
        importOrder.setAction(csvLine[1]);

        if (csvLine[2].toLowerCase().isBlank() || csvLine[2].toLowerCase().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    errorMessage + csvLineNumber + ". Product name must not be empty.");
        }
        importOrder.setProduct_name(csvLine[2]);

        if (!(
                csvLine[3].toLowerCase().equals("limit") ||
                        csvLine[3].toLowerCase().equals("immediate_or_cancel") ||
                        csvLine[3].toLowerCase().equals("fill_or_kill") ||
                        csvLine[3].toLowerCase().equals("all_or_none") ||
                        csvLine[3].toLowerCase().equals("iceberg") ||
                        csvLine[3].toLowerCase().equals("basket")


        )) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    errorMessage + csvLineNumber
                            + ". Order type must be on the list [limit, immediate_or_cancel, fill_or_kill, all_or_none, iceberg, basket]");
        }
        importOrder.setOrderType(
                AppConstants.Types.SingleOrderType.valueOf(
                        csvLine[3].toUpperCase()
                )
        );

        importOrder.setQuantity(Double.parseDouble(csvLine[4]));

        AppConstants.Types.OrderDirection orderDirection;
        if (csvLine[7].toLowerCase().equals("sale")) {
            orderDirection = AppConstants.Types.OrderDirection.SELL;
        } else {
            orderDirection = AppConstants.Types.OrderDirection.BUY;
        }

        if (orderDirection == AppConstants.Types.OrderDirection.BUY) {
            importOrder.setPrice(Double.parseDouble(csvLine[5]));
        } else {
            importOrder.setPrice(Double.parseDouble(csvLine[6]));
        }

        if (importOrder.getPrice() <= 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    errorMessage + csvLineNumber + ". Price must be > 0.");
        }

        if (!(orderDirection.toString().toLowerCase().equals("buy") || orderDirection.toString()
                .toLowerCase().equals("sell"))) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error ar line " + csvLineNumber + ". Order Direction must be Buy or Sell.");
        }
        importOrder.setOrderDirection(orderDirection);

        try {

//            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
//                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
//                    .toFormatter()
//                    .withZone(ZoneOffset.UTC);
//            Instant deliveryTime = dateTimeFormatter.parse(csvLine[8], Instant::from);
            OffsetDateTime deliveryTime = OffsetDateTime.parse(csvLine[8]);
            importOrder.setTimeStamp(deliveryTime.toInstant());

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error ar line " + csvLineNumber + ". Timestamp could not be read.");
        }

        try {
            if (csvLine[3].toLowerCase().equals("iceberg") ||
                    csvLine[3].toLowerCase().equals("basket")) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> metadataMap = mapper.readValue(csvLine[9], Map.class);
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Metadata error at line " + csvLineNumber + ".");
        }

        importOrder.setOrder_meta(csvLine[9]);
        importOrder.setBasket_ID(csvLine[10]);
        importOrder.setBasket_Type(csvLine[11]);
        importOrder.setBasket_meta(csvLine[12]);

        return importOrder;
    }

    @Transactional
    public void create(List<ImportOrder> importOrders) {
        this.importOrderRepository.saveAll(importOrders);
    }

    @Transactional
    public void connectMarketEntityToImportOrder(ImportOrder importOrder, Long marketGroupId) {
        this.importOrderRepository.updateSingleOrderData(importOrder.getId(), marketGroupId);

        this.importOrderRepository.updateSingleOrderActiveGroupId(
                importOrder.getMessageInfoId(),
                importOrder.getOrder_ID(),
                marketGroupId
        );
    }

    @Transactional
    public void connectMarketBasketToImportOrder(ImportOrder importOrder, Long basketId) {
        //  this.importOrderRepository.updateSingleOrderData(importOrder.getId(), basketId);

        this.importOrderRepository.updateBasketOrderActiveGroupId(
                importOrder.getMessageInfoId(),
                importOrder.getBasket_ID(),
                basketId
        );
    }

    @Transactional
    public void connectMarketBasketToImportOrderList(List<ImportOrder> importOrders, Long basketId) {
        for (ImportOrder importOrder : importOrders) {
            this.importOrderRepository.updateSingleOrderData(importOrder.getId(), basketId);
        }
    }


    public List<ImportOrderDTO> getAll(String marketCode) {
        User user = userService.getLoggedInUser();
        List<ImportOrder> importOrders = this.importOrderRepository.findByMarketCodeIsLikeAndUserIdIsLike(marketCode, user.getId());
        return this.importOrderMapper.map(importOrders);
    }

    public Page<ImportOrderDTO> getPage(String marketCode, Pageable pageable) {
        User user = userService.getLoggedInUser();
        Page<ImportOrder> importOrders = this.importOrderRepository
                .findByMarketCodeIsLikeAndUserIdIsLike(marketCode, user.getId(), pageable);
        return this.importOrderMapper.map(importOrders);
    }

    public List<ImportOrder> getOrdersByTimestamp(Instant instant) {

        ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);

        Instant timestampFrom = zonedDateTime.truncatedTo(ChronoUnit.SECONDS).toInstant();
        Instant timestampTo = zonedDateTime.truncatedTo(ChronoUnit.SECONDS).plusMinutes(1).toInstant();

        return importOrderRepository.findOrdersByTimestampOnPeriod(timestampFrom, timestampTo);
    }

    public List<List<ImportOrder>> getBasketsByTimestamp(Instant instant) {

        ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);

        Instant timestampFrom = zonedDateTime.truncatedTo(ChronoUnit.SECONDS).toInstant();
        Instant timestampTo = zonedDateTime.truncatedTo(ChronoUnit.SECONDS).plusMinutes(1).toInstant();

        List<ImportOrder> importOrders = importOrderRepository.findBasketsByTimestampOnPeriod(timestampFrom, timestampTo);

        List<List<ImportOrder>> importOrderGroups = new ArrayList<>();
        List<ImportOrder> importOrderGroup = new ArrayList<>();
        String orderId = "~";
        for (ImportOrder importOrder : importOrders) {

            if (importOrder.getBasket_ID().equals("")) continue;

            if (orderId.equals("~")) {
                orderId = importOrder.getOrder_ID().toString();
            }

            importOrderGroup.add(importOrder);

            if (!orderId.equals(importOrder.getOrder_ID().toString())) {
                importOrderGroups.add(importOrderGroup);
                importOrderGroup = new ArrayList<>();
            }

            orderId = importOrder.getOrder_ID().toString();
        }

        if (importOrderGroup.size() > 0) {
            importOrderGroups.add(importOrderGroup);
        }

        return importOrderGroups;
    }

    public void deleteAll(String marketCode) {
        User user = userService.getLoggedInUser();
        importOrderRepository.deleteByUserAndMarketCode(user.getId(), marketCode);
    }

    public void deleteGroupById(Long id) {

        User user = userService.getLoggedInUser();
        Optional<ImportOrder> optionalImportOrder = importOrderRepository.findById(id);

        if (optionalImportOrder.isPresent()) {
            ImportOrder importOrder = optionalImportOrder.get();

            if (!user.getId().equals(importOrder.getUserId())) {
                return;
            }
            importOrderRepository.deleteGroup(importOrder.getMessageInfoId(), importOrder.getOrder_ID());
        }

    }

    public void deleteMessageInfoId(Long id) {
        User user = userService.getLoggedInUser();
        importOrderRepository.deleteByMessageInfoId(id, user.getId());
    }
}
