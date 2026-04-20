package com.poseidon.service;

import com.poseidon.domain.Trade;
import com.poseidon.repositories.TradeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;


    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    public void addTrade(@Valid Trade trade) {
        tradeRepository.save(trade);
    }

    public Trade findById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trade Id: " + id));

    }

    public void deleteById(Integer id) {
        tradeRepository.deleteById(id);
    }
}
