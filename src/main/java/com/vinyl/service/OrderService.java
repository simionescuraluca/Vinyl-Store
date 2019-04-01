package com.vinyl.service;

import com.vinyl.model.Purchase;
import com.vinyl.modelDTO.StatusDTO;
import com.vinyl.repository.PurchaseRepository;
import com.vinyl.repository.TokenRepository;
import com.vinyl.service.exception.BadRequestException;
import com.vinyl.service.exception.NotFoundException;
import com.vinyl.service.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.empty;

@Service("orderService")
public class OrderService {

    @Autowired
    private ValidatorFactory validatorFactory;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public void changeOrderStatus(StatusDTO newStatus, Integer orderId, String tokenHash){

        validateAdminToken(tokenHash);

        if(!newStatus.getStatus().equals("SENT")){
            throw new BadRequestException("The order status is invalid!");
        }

        Optional order = purchaseRepository.findById(orderId);

        if(order == empty()){
            throw new NotFoundException("The order is not found!");
        }

        Purchase newOrder = purchaseRepository.findById(orderId).get();
        newOrder.setStatus(newStatus.getStatus());
        purchaseRepository.save(newOrder);
    }

    public void validateAdminToken(String tokenHash) {
        validatorFactory.getTokenValidator().validate(tokenHash);
        validatorFactory.getAdminValidator().validate(tokenRepository.findByHash(tokenHash));
    }
}