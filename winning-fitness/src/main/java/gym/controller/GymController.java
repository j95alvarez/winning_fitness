package gym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gym.controller.model.GymData;
import gym.service.GymService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/gym")
@Slf4j
public class GymController {
	@Autowired
	private GymService gymService;
	
	/*
	 * Create Gym
	 */
	@PostMapping("/gym")
	@ResponseStatus(code = HttpStatus.CREATED)
	public GymData createGym(@RequestBody GymData gymData) {
		log.info("Creating Gym ID= " + gymData);
		
		return gymService.saveGym(gymData);
	}
	
	@PutMapping("gym/{gymId}")
	public GymData updateGym(@PathVariable Long gymId, @RequestBody GymData gymData) {
		gymData.setGymId(gymId);
		log.info("Updating Gym ID= " + gymId);
		
		return gymService.saveGym(gymData);
	}
}
