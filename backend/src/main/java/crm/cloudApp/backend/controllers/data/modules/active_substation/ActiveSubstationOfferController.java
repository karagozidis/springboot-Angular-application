package crm.cloudApp.backend.controllers.data.modules.active_substation;

import crm.cloudApp.backend.dto.data.modules.active_substation.ActiveSubstationMetadataDTO;
import crm.cloudApp.backend.services.data.modules.active_substation.ActiveSubstationOfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/active-substation-offer")
public class ActiveSubstationOfferController {

    private final ActiveSubstationOfferService activeSubstationOfferService;

    public ActiveSubstationOfferController(ActiveSubstationOfferService activeSubstationOfferService) {
        this.activeSubstationOfferService = activeSubstationOfferService;
    }

    @GetMapping(value = "/offers")
    public List<ActiveSubstationMetadataDTO> getOffersMetadata(@RequestParam("user-group") Long userGroup) throws IOException {
        return this.activeSubstationOfferService.getOffersMetadata(userGroup);
    }

    @GetMapping(value = "/offer-by-id")
    public ActiveSubstationMetadataDTO getOfferMetadataById(
                                                         @RequestParam("id") String uuid ) throws IOException {
        return this.activeSubstationOfferService.getOfferMetadata(uuid);
    }


    @GetMapping(value = "/offer-today")
    public ActiveSubstationMetadataDTO getOfferMetadataToday(
            @RequestParam("user-group") Long userGroup ) throws IOException {
        return this.activeSubstationOfferService.getOffersMetadataToday(userGroup);
    }


    @PostMapping(path = "/update-status")
    public void updateStatus(@RequestParam("id") String id, @RequestParam("status") String status) throws IOException {
        this.activeSubstationOfferService.updateStatus(id,status);
    }


}
