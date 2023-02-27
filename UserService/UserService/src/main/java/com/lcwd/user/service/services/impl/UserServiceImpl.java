package com.lcwd.user.service.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.external.services.RatingService;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HotelService hotelService;

	@Autowired
	RatingService ratingService;

	// private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public User saveUser(User user) {
		// generate unique userid
		String randomUserId = UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		// implement RATING SERVICE CALL: USING REST TEMPLATE
		return userRepository.findAll();
	}

	// get single user
	@Override
	public User getUser(String userId) {

		User user = userRepository.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User with given id is not found on server !! : " + userId));

		List<Rating> ratings = ratingService.getRatingsByUserId(user.getUserId());

		// Rating[] ratingsOfUser =
		// restTemplate.getForObject("http://localhost:8083/ratings/users/" +
		// user.getUserId(),Rating[].class);

		List<Rating> ratingList = ratings.stream().map(rating -> {

			// http://localhost:8082/hotels/1cbaf36d-0b28-4173-b5ea-f1cb0bc0a791
			// ResponseEntity<Hotel> forEntity =
			// restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(),
			// Hotel.class);
			Hotel hotel = hotelService.getHotel(rating.getHotelId());

			// set the hotel to rating
			rating.setHotel(hotel);
			return rating;
		}).collect(Collectors.toList());

		user.setRatings(ratingList);

		return user;
	}

	@Override
	public User getUserOnly(String userId) {
		return userRepository.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User with given id is not found on server !! : " + userId));
	}
}
