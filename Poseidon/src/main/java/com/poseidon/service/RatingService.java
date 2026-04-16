package com.poseidon.service;

import com.poseidon.domain.Rating;
import com.poseidon.repositories.RatingRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    public Object findAll() {
        return ratingRepository.findAll();
    }

    public void addRating(@Valid Rating rating) {
        ratingRepository.save(rating);
    }

    public Rating findById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rat Id: " + id));
    }

    public void deleteById(Integer id) {
        ratingRepository.deleteById(id);
    }
}

