package com.poseidon.service;

import com.poseidon.domain.BidList;
import com.poseidon.repositories.BidListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListService;

    @Test
    void findAllTest() {
        BidList bid1 = new BidList("Account1", "Type1", 10d);
        BidList bid2 = new BidList("Account2", "Type2", 20d);

        when(bidListRepository.findAll()).thenReturn(Arrays.asList(bid1, bid2));

        assertEquals(2, bidListService.findAll().size());
    }

    @Test
    void findByIdTest() {
        BidList bid = new BidList("Account", "Type", 10d);
        bid.setBidListId(1);

        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid));

        BidList result = bidListService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getBidListId());
    }

    @Test
    void findByIdNotFoundTest() {
        when(bidListRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            bidListService.findById(1);
        });
    }

    @Test
    void addBidListTest() {
        BidList bid = new BidList("Account", "Type", 10d);

        bidListService.addBidlist(bid);

        verify(bidListRepository, times(1)).save(bid);
    }

    @Test
    void deleteByIdTest() {
        bidListService.deleteById(1);

        verify(bidListRepository, times(1)).deleteById(1);
    }
}