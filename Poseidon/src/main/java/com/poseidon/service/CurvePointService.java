package com.poseidon.service;

import com.poseidon.domain.CurvePoint;
import com.poseidon.repositories.CurvePointRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;

    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    public void addCurvePoint(@Valid CurvePoint curvePoint) {
        curvePointRepository.save(curvePoint);
    }

    public CurvePoint findById(Integer id) {
        return curvePointRepository.getReferenceById(id);
//                .orElseThrow(() -> new IllegalArgumentException("Invalid curve Id: "+id));
    }

    public void deleteById(Integer id) {
        curvePointRepository.deleteById(id);
    }


}
