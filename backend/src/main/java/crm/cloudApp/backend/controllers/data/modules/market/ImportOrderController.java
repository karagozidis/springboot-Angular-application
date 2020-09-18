package crm.cloudApp.backend.controllers.data.modules.market;

import crm.cloudApp.backend.config.data.modules.market.MarketInitialization;
import crm.cloudApp.backend.dto.data.modules.market.ImportOrderDTO;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.models.data.modules.market.ImportOrder;
import crm.cloudApp.backend.services.data.modules.file_upload.FileUploadService;
import crm.cloudApp.backend.services.data.modules.market.ImportOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/market/import/orders")
public class ImportOrderController {

  private final FileUploadService fileUploadService;
  private final ImportOrderService importOrderService;

  public ImportOrderController(FileUploadService fileUploadService,
      ImportOrderService importOrderService) {
    this.fileUploadService = fileUploadService;
    this.importOrderService = importOrderService;
  }



  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void uploadOrders(
      @RequestParam("file") MultipartFile multipartFile,
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode
  ) throws IOException {

    if (!MarketInitialization.initialized) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          MarketInitialization.MARKET_INITIALIZATION_MESSAGE);
    }

    MessageInfo messageinfo = this.fileUploadService.uploadFiles(multipartFile, "order_upload");
    try {
      List<ImportOrder> importOrders = this.importOrderService
          .parseCsvAndGenerateImportOrders(messageinfo, marketCode);
      this.importOrderService.create(importOrders);
    } catch (ResponseStatusException ex) {
      log.error(ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error(ex.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Error parsing the file!");
    }
  }


  @GetMapping
  public List<ImportOrderDTO> getAll(
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {
    return this.importOrderService.getAll(marketCode);
  }

  @GetMapping(path = "/page")
  public Page<ImportOrderDTO> getPage(
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode,
      Pageable pageable) {
    return this.importOrderService.getPage(marketCode, pageable);
  }


  @DeleteMapping
  public void deleteAll(
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode
  ) {

    if (!MarketInitialization.initialized) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          MarketInitialization.MARKET_INITIALIZATION_MESSAGE);
    }
    this.importOrderService.deleteAll(marketCode);
  }

  @DeleteMapping(path = "/group-by-id")
  public void deleteGroupById(
      @RequestParam(name = "id", defaultValue = "CY", required = false) Long id
  ) {
    if (!MarketInitialization.initialized) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          MarketInitialization.MARKET_INITIALIZATION_MESSAGE);
    }

    this.importOrderService.deleteGroupById(id);
  }

  @DeleteMapping(path = "/by-message-info-id")
  public void deleteByMessageInfoId(
          @RequestParam(name = "message-info-id", defaultValue = "CY", required = false) Long id
  ) {
    if (!MarketInitialization.initialized) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
              MarketInitialization.MARKET_INITIALIZATION_MESSAGE);
    }
    this.importOrderService.deleteMessageInfoId(id);
  }
}

