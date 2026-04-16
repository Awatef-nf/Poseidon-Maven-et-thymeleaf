package com.poseidon.service;

import com.poseidon.domain.BidList;
import com.poseidon.repositories.BidListRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService {
    @Autowired
    private BidListRepository bidListRepository;


    public List<BidList> findAll() {
        return bidListRepository.findAll();
    }
    public BidList findById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bid Id: " + id));
    }

    public void addBidlist(@Valid BidList bidList) {
        bidListRepository.save(bidList);
    }

    public void deleteById(Integer id) {
        bidListRepository.deleteById(id);
    }


}
