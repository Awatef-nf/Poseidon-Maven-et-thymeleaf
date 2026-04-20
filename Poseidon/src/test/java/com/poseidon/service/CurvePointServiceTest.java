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

    @Test
    void findByIdTest(){
        CurvePoint curv = new CurvePoint(1,12.2,22.5);
        curv.setId(1);

        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curv));

        CurvePoint result = curvePointService.findById(1);
        assertEquals(1, result.getId());
    }

    @Test
    void FindByIDNotFoundTest() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            curvePointService.findById(1);
        });
    }

    @Test
    void addCurvePointTest(){
        CurvePoint curve = new CurvePoint(1,12.34,23.1);
        curvePointService.addCurvePoint(curve);
        verify(curvePointRepository,times(1)).save(curve);

    }

   @Test
    void deleteByIdTest(){
        curvePointService.deleteById(1);
        verify(curvePointRepository, times(1)).deleteById(1);
   }


}