package com.poseidon;

import com.poseidon.domain.Rating;
import com.poseidon.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RatingTests {

	@Autowired
	private RatingRepository ratingRepository;


	@Test
	void saveRatingTest() {
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);

		Rating saved = ratingRepository.save(rating);

		assertNotNull(saved.getId());
		assertEquals(10, saved.getOrderNumber());
	}

	@Test
	void updateRatingTest() {

		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		rating = ratingRepository.save(rating);

		Integer id = rating.getId();

		rating.setOrderNumber(20);
		ratingRepository.save(rating);

		Rating updated = ratingRepository.findById(id).orElseThrow();

		assertEquals(20, updated.getOrderNumber());
	}

	@Test
	void findAllRatingsTest() {
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		ratingRepository.save(rating);

		List<Rating> listResult = ratingRepository.findAll();

		assertTrue(listResult.size() > 0);
	}

	@Test
	void deleteRatingTest() {
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		rating = ratingRepository.save(rating);

		Integer id = rating.getId();
		ratingRepository.delete(rating);

		Optional<Rating> deleted = ratingRepository.findById(id);

		assertFalse(deleted.isPresent());
	}
}