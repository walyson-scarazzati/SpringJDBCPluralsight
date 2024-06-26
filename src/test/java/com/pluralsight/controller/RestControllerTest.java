package com.pluralsight.controller;

import java.util.List;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import com.pluralsight.model.Ride;
import com.pluralsight.util.ServiceError;

public class RestControllerTest {
	
	@Test(timeout=3000)
	public void testCreateRide() {
		RestTemplate restTemplate = new RestTemplate();
		
		Ride ride = new Ride();
		ride.setName("Bobsled Trail");
		ride.setDuration(33);

		ride = restTemplate.postForObject("http://localhost:9090/ride_tracker/ride", ride, Ride.class);
		System.out.println("Ride" + ride);
	}

	@Test(timeout=3000)
	public void testGetRides() {
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<List<Ride>> ridesResponse = restTemplate.exchange(
				"http://localhost:9090/ride_tracker/rides", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Ride>>() {
				});
		List<Ride> rides = ridesResponse.getBody();

		for (Ride ride : rides) {
			System.out.println("Ride name: " + ride.getName());
		}
	}
	
	@Test(timeout=3000)
	public void testGetRide() {
		RestTemplate restTemplate = new RestTemplate();
		
		Ride ride = restTemplate.getForObject("http://localhost:9090/ride_tracker/ride/1", Ride.class);

		System.out.println("Ride name: " + ride.getName());
	}
	
	@Test(timeout=3000)
	public void testUpdateRide() {
		RestTemplate restTemplate = new RestTemplate();
		
		Ride ride = restTemplate.getForObject("http://localhost:9090/ride_tracker/ride/1", Ride.class);
		
		ride.setDuration(ride.getDuration() + 1 );
		
		restTemplate.put("http://localhost:9090/ride_tracker/ride", ride);
		
		System.out.println("Ride name: " + ride.getName());
	}
	
	
	@Test(timeout=3000)
	public void testBatchUpdateRides() {
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.getForObject("http://localhost:9090/ride_tracker/batch", Object.class);
		
	}
	
	@Test(timeout=3000)
	public void testDelete() {
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.delete("http://localhost:9090/ride_tracker/delete/16");
		
	}
	
	@Test(timeout=3000)
	public void testException() {
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.getForObject("http://localhost:9090/ride_tracker/test", Ride.class);
		
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ServiceError> handle(RuntimeException ex){
		ServiceError error = new ServiceError(HttpStatus.OK.value(), ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.OK);
	}
	
}
