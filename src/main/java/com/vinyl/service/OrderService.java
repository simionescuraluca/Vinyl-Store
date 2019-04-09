package com.vinyl.service;

import com.vinyl.modelDTO.StatusDTO;

public interface OrderService {

    void changeOrderStatus(StatusDTO newStatus, Integer orderId, String tokenHash);
}
