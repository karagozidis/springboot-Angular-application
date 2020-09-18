package crm.cloudApp.backend.services.data.modules.market;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.action.NotificationDTO;
import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.mappers.data.modules.market.BaseInfoTradeMapper;
import crm.cloudApp.backend.mappers.data.modules.market.TradeMapper;
import crm.cloudApp.backend.models.data.modules.market.MatchedOrder;
import crm.cloudApp.backend.repositories.data.modules.market.MatchedOrderRepository;
import crm.cloudApp.backend.services.action.NotificationService;
import crm.cloudApp.backend.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@Transactional
public class MatchedOrderService {

    private final MatchedOrderRepository matchedOrderRepository;
    private final TradeMapper tradeMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;
    private final BaseInfoTradeMapper baseInfoTradeMapper;
    private final NotificationService notificationService;

    public MatchedOrderService(MatchedOrderRepository matchedOrderRepository,
                               TradeMapper tradeMapper, SimpMessagingTemplate simpMessagingTemplate,
                               UserService userService, BaseInfoTradeMapper baseInfoTradeMapper,
                               NotificationService notificationService) {
        this.matchedOrderRepository = matchedOrderRepository;
        this.tradeMapper = tradeMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
        this.baseInfoTradeMapper = baseInfoTradeMapper;
        this.notificationService = notificationService;
    }

    @Transactional
    public List<TradeDTO> addMatches(List<MatchedOrder> matchedOrders) {
        List<MatchedOrder> createdMatchedOrders = new ArrayList<>();
        List<Long> matchedUsersIds = new ArrayList<>();

        for (MatchedOrder matchedOrder : matchedOrders) {
            MatchedOrder createdMatchedOrder = matchedOrderRepository.save(matchedOrder);

            this.createMatchingNotifications(createdMatchedOrder);

            if (!matchedUsersIds.contains(createdMatchedOrder.getSingleOrder1().getUser().getId())) {
                matchedUsersIds.add(createdMatchedOrder.getSingleOrder1().getUser().getId());
            }
            if (!matchedUsersIds.contains(createdMatchedOrder.getSingleOrder2().getUser().getId())) {
                matchedUsersIds.add(createdMatchedOrder.getSingleOrder2().getUser().getId());
            }

            this.notifyUsersForMatchingUsingWebSockets(matchedUsersIds);

            createdMatchedOrders.add(createdMatchedOrder);
        }

        Long loggedInUserId = 0L;
        if (userService.getLoggedInUser() != null) {
            loggedInUserId = userService.getLoggedInUser().getId();
        }

        return tradeMapper
                .mapForUserCloningOwnOrders(createdMatchedOrders, loggedInUserId);

    }

    private void createMatchingNotifications(MatchedOrder createdMatchedOrder) {

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setStatus(AppConstants.Types.NotificationStatus.pending);
        notificationDTO.setDescription(
                "<h4><b>Congratulations, you just made a new trade with details</b></h4> " +
                        " Product : " + createdMatchedOrder.getSingleOrder1().getProduct().getName() +
                        " <br> Order id : " + createdMatchedOrder.getSingleOrder1().getId().toString() +
                        " <br> Bid time : " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(
                        ZoneId.of("UTC")).format(createdMatchedOrder.getSingleOrder1().getBidTime()) + " UTC" +
                        " <br> Order direction : " + createdMatchedOrder.getSingleOrder1().getOrderDirection()
                        .toString() +
                        " <br> Order type : " + createdMatchedOrder.getSingleOrder1().getOrderType().toString()
                        +
                        " <br> Order details : " + Optional
                        .ofNullable(createdMatchedOrder.getSingleOrder1().getMetadata()).orElse("") +
                        " <br> <b>Quantity : " + createdMatchedOrder.getQuantity() + "</b>" +
                        " <br> <b>Price : " + createdMatchedOrder.getPrice() + "</b>"
        );
        notificationDTO.setName(
                "New Trade! " + createdMatchedOrder.getSingleOrder1().getProduct().getName() +
                        "/" + createdMatchedOrder.getSingleOrder1().getId().toString() +
                        " Qty: " + createdMatchedOrder.getQuantity().toString() +
                        " Price: " + createdMatchedOrder.getPrice().toString()
        );
        notificationDTO.setSenderId(createdMatchedOrder.getSingleOrder1().getUser().getId());
        notificationDTO.setReceiverId(createdMatchedOrder.getSingleOrder1().getUser().getId());
        notificationDTO.setTimeSent(Instant.now());
        notificationDTO.setType(AppConstants.Types.NotificationType.marketTradeCreation);
        this.notificationService.create(notificationDTO);

        notificationDTO = new NotificationDTO();
        notificationDTO.setStatus(AppConstants.Types.NotificationStatus.pending);
        notificationDTO.setDescription(
                "<h4><b>Congratulations, you just made a new trade with details</b></h4> " +
                        "  Product : " + createdMatchedOrder.getSingleOrder2().getProduct().getName() +
                        " <br> Order id : " + createdMatchedOrder.getSingleOrder2().getId().toString() +
                        " <br> Bid time : " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.of("UTC")).format(createdMatchedOrder.getSingleOrder2().getBidTime())
                        + " UTC" +
                        " <br> Order direction : " + createdMatchedOrder.getSingleOrder2().getOrderDirection()
                        .toString() +
                        " <br> Order type : " + createdMatchedOrder.getSingleOrder2().getOrderType().toString()
                        +
                        " <br> Order details : " + Optional
                        .ofNullable(createdMatchedOrder.getSingleOrder2().getMetadata()).orElse("") +
                        " <br> <b>Quantity : " + createdMatchedOrder.getQuantity() + "</b>" +
                        " <br> <b>Price : " + createdMatchedOrder.getPrice() + "</b>"
        );

        notificationDTO.setName(
                "New Trade! " + createdMatchedOrder.getSingleOrder2().getProduct().getName() +
                        " - " + createdMatchedOrder.getSingleOrder2().getId().toString() +
                        " Qty: " + createdMatchedOrder.getQuantity().toString() +
                        " Price: " + createdMatchedOrder.getPrice().toString()
        );
        notificationDTO.setSenderId(createdMatchedOrder.getSingleOrder2().getUser().getId());
        notificationDTO.setReceiverId(createdMatchedOrder.getSingleOrder2().getUser().getId());
        notificationDTO.setTimeSent(Instant.now());
        notificationDTO.setType(AppConstants.Types.NotificationType.marketTradeCreation);
        this.notificationService.create(notificationDTO);
    }

    private void notifyUsersForMatchingUsingWebSockets(List<Long> usersIds) {
        for (Long id : usersIds) {
            this.simpMessagingTemplate
                    .convertAndSend("/topic/market/matching/" + id + "", "new-matching");
        }
    }


    public List<TradeDTO> getNewTrades(String marketCode) {

        List<MatchedOrder> newMatchedOrders = matchedOrderRepository
                .findNewByUser(userService.getLoggedInUser().getId(), marketCode);

        List<Long> ids = newMatchedOrders.stream().map(MatchedOrder::getId)
                .collect(Collectors.toList());
        if (!ids.isEmpty()) {
            matchedOrderRepository.setStatusToViewed(ids);
        }

        return tradeMapper
                .mapForUserCloningOwnOrders(newMatchedOrders, userService.getLoggedInUser().getId());
    }

    public List<TradeDTO> getTradesOnPeriod(
            Instant createdFrom,
            Instant createdTo,
            String marketCode) {
        List<MatchedOrder> matchedOrder = matchedOrderRepository
                .getTradesOnPeriod(createdFrom, createdTo, marketCode);
        return tradeMapper.mapForUserCloningOwnOrders(matchedOrder,userService.getLoggedInUser().getId());
    }

    public List<TradeDTO> findByProductId(Long productId) {
        List<MatchedOrder> matchedOrders = this.matchedOrderRepository.findByProduct(productId);
        return tradeMapper.map(matchedOrders);
    }

    public List<TradeDTO> findCurrentUsersByProductId(Long productId) {
        List<MatchedOrder> matchedOrders = this.matchedOrderRepository.findByProductAndUser(productId,
                this.userService.getLoggedInUser().getId());
        return tradeMapper.mapAndFilterForUser(matchedOrders, userService.getLoggedInUser().getId());
    }

}
