package com.poseidon.service;

import com.poseidon.domain.CurvePoint;
import com.poseidon.repositories.CurvePointRepository;
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
class CurvePointServiceTest {
    @Mock
    private CurvePointRepository curvePointRepository;
    @InjectMocks
    CurvePointService curvePointService;

    @Test
    void findAllTest(){
        CurvePoint curv1 = new CurvePoint(1,12.2,23.2);
        CurvePoint curv2 = new CurvePoint(2,20.2,23.2);

        when(curvePointRepository.findAll()).thenReturn(Arrays.asList(curv1, curv2));

        assertEquals(2,curvePointService.findAll().size());

    }


}

//
//    @Test
//    void findByIdTest() {
//        BidList bid = new BidList("Account", "Type", 10d);
//        bid.setBidListId(1);
//
//        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid));
//
//        BidList result = bidListService.findById(1);
//
//        assertNotNull(result);
//        assertEquals(1, result.getBidListId());
//    }
//
//    @Test
//    void findByIdNotFoundTest() {
//        when(bidListRepository.findById(1)).thenReturn(Optional.empty());
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            bidListService.findById(1);
//        });
//    }
//
//    @Test
//    void addBidListTest() {
//        BidList bid = new BidList("Account", "Type", 10d);
//
//        bidListService.addBidlist(bid);
//
//        verify(bidListRepository, times(1)).save(bid);
//    }
//
//    @Test
//    void deleteByIdTest() {
//        bidListService.deleteById(1);
//
//        verify(bidListRepository, times(1)).deleteById(1);
//    }
