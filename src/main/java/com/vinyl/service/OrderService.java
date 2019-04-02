package com.vinyl.service;

import com.vinyl.model.Purchase;
import com.vinyl.model.Status;
import com.vinyl.modelDTO.StatusDTO;
import com.vinyl.repository.PurchaseRepository;
import com.vinyl.service.exception.BadRequestException;
import com.vinyl.service.exception.NotFoundException;
import com.vinyl.service.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderService {

    @Autowired
    private ValidatorFactory validatorFactory;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public void changeOrderStatus(StatusDTO newStatus, Integer orderId, String tokenHash) {
        validatorFactory.getAdminValidator().validate(tokenHash);

        Status status;
        try {
            status = Status.valueOf(newStatus.getStatus());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BadRequestException("The order status is invalid!");
        }

        Purchase p = purchaseRepository.findById(orderId).orElseThrow(() -> new NotFoundException("The order is not found!"));
        if (!status.equals(p.getStatus())) {
            p.setStatus(status);
            purchaseRepository.save(p);
        }
    }
}