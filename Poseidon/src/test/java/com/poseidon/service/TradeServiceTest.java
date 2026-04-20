package com.poseidon.service;

import com.poseidon.domain.Trade;
import com.poseidon.repositories.TradeRepository;
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
class TradeServiceTest {
    @Mock
    private TradeRepository tradeRepository;
    @InjectMocks
    private TradeService tradeService;

    @Test
    void findAllTest(){
        Trade trade1 = new Trade("abcd","type",12.23);
        Trade trade2 = new Trade("abcd","type",12.21);

        when(tradeRepository.findAll()).thenReturn(Arrays.asList(trade1,trade2));
       assertEquals(2,tradeService.findAll().size());

    }

    @Test
    void findByIdTest(){
        Trade trade = new Trade("abcd","type",12.23);
        trade.setTradeId(1);

        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        Trade result = tradeService.findById(1);
        assertEquals(1, result.getTradeId());
    }

    @Test
    void FindByIDNotFoundTest() {
        when(tradeRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            tradeService.findById(1);
        });
    }

    @Test
    void addTradeTest(){
        Trade trade = new Trade("abcd","type",12.23);
        tradeService.addTrade(trade);
        verify(tradeRepository,times(1)).save(trade);

    }

    @Test
    void deleteByIdTest(){
        tradeService.deleteById(1);
        verify(tradeRepository, times(1)).deleteById(1);
    }


}