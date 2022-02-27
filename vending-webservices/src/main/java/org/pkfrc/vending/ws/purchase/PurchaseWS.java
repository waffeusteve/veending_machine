package org.pkfrc.vending.ws.purchase;

import org.pkfrc.core.dto.base.BaseRecordDTO;
import org.pkfrc.core.services.base.IBaseService;
import org.pkfrc.core.ws.base.BaseWS;
import org.pkfrc.projurise.repo.purchase.PurchaseRepository;
import org.pkfrc.vending.dto.purchase.BuyResponse;
import org.pkfrc.vending.dto.purchase.PurchaseDTO;
import org.pkfrc.vending.entities.purchase.Purchase;
import org.pkfrc.vending.services.purchase.BuyRequest;
import org.pkfrc.vending.services.purchase.IPurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/purchase")
public class PurchaseWS extends BaseWS<Purchase, Long> {

    Logger logger = LoggerFactory.getLogger(PurchaseDTO.class);

    @Autowired
    IPurchaseService service;
    
    @Autowired
	PurchaseRepository repository;

    @Override
    public Logger getLOGGER() {
        return logger;
    }

    @Override
    public IBaseService<Purchase, Long> getService() {
        return service;
    }

    @GetMapping(value = "/buy")
    public ResponseEntity<BaseRecordDTO<BuyResponse>> deposit(
    		@RequestBody BuyRequest request) {
        try {
            BaseRecordDTO<BuyResponse> result = evaluateServiceData(service.doBuy(getConnectedUser(), request), BuyResponse.class);
            return new ResponseEntity<BaseRecordDTO<BuyResponse>>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<BaseRecordDTO<BuyResponse>>(evaluateException(e), STATUS_OK);
        }
    }
    
}