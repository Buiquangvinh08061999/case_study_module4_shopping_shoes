package com.cg.service.OrderStatus;

import com.cg.model.OrderStatus;
import com.cg.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderStatusServiceImpl implements IOrderStatusService{

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Override
    public List<OrderStatus> findAll() {
        return orderStatusRepository.findAll();
    }

    @Override
    public Optional<OrderStatus> findById(Long id) {
        return orderStatusRepository.findById(id);
    }

    @Override
    public OrderStatus getById(Long id) {
        return null;
    }

    @Override
    public OrderStatus save(OrderStatus orderStatus) {
        return orderStatusRepository.save(orderStatus);
    }

    @Override
    public void remove(Long id) {
        orderStatusRepository.deleteById(id);
    }
}
