package com.poseidon.service;

import com.poseidon.domain.Rating;
import com.poseidon.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {
    @Mock
    private RatingRepository ratingRepository;
    @InjectMocks
    private RatingService ratingService;

    @Test
    void findAllTest(){
        Rating rate1 = new Rating("moody","sand","ficth",12);
        Rating rate2 = new Rating("moodyS","sandP","ficthR",121);

        when(ratingRepository.findAll()).thenReturn(Arrays.asList(rate1, rate2));

        assertEquals(2,ratingService.findAll().size());

    }

    @Test
    void findByIdTest(){
        Rating rate = new Rating("moody","sand","ficth",12);
        rate.setId(1);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rate));

        Rating result = ratingService.findById(1);
        assertEquals(1, result.getId());
    }

    @Test
    void FindByIDNotFoundTest() {
        when(ratingRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            ratingService.findById(1);
        });
    }

    @Test
    void addRateTest(){
        Rating rate = new Rating("moody","sand","ficth",12);
        ratingService.addRating(rate);
        verify(ratingRepository,times(1)).save(rate);

    }

    @Test
    void deleteByIdTest(){
        ratingService.deleteById(1);
        verify(ratingRepository, times(1)).deleteById(1);
    }


}