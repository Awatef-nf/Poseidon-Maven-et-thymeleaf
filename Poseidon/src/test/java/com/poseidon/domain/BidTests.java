package com.poseidon.domain;

import com.poseidon.repositories.BidListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class BidTests {

    @Autowired
    private BidListRepository bidListRepository;

    @Test
    public void shouldSaveBidList() {
        BidList bid = new BidList("Account Test", "Type Test", 10d);

        BidList savedBid = bidListRepository.save(bid);

        assertNotNull(savedBid.getBidListId());
        assertEquals(10d, savedBid.getBidQuantity(), 0.001);
    }

    @Test
    public void shouldUpdateBidList() {
        BidList bid = new BidList("Account Test", "Type Test", 10d);
        bid = bidListRepository.save(bid);

        bid.setBidQuantity(20d);
        BidList updatedBid = bidListRepository.save(bid);

        assertEquals(20d, updatedBid.getBidQuantity(), 0.001);
    }

    @Test
    public void shouldFindAllBidLists() {
        BidList bid = new BidList("Account Test", "Type Test", 10d);
        bidListRepository.save(bid);

        List<BidList> listResult = bidListRepository.findAll();

        assertTrue(listResult.size() > 0);
    }
    @Test
    public void shouldDeleteBidList() {
        BidList bid = new BidList("Account Test", "Type Test", 10d);
        bid = bidListRepository.save(bid);

        Integer id = bid.getBidListId();
        bidListRepository.delete(bid);

        Optional<BidList> result = bidListRepository.findById(id);

        assertFalse(result.isPresent());
    }
}