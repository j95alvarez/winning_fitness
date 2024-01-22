package gym.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gym.controller.model.GymData;
import gym.controller.model.GymData.GymFitnessCoach;
import gym.controller.model.GymData.GymMember;
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
	
	/*
	 * Update Gym
	 */
	@PutMapping("/gym/{gymId}")
	public GymData updateGym(@PathVariable Long gymId, @RequestBody GymData gymData) {
		gymData.setGymId(gymId);
		log.info("Updating Gym ID= " + gymId);
		
		return gymService.saveGym(gymData);
	}
	
	/*
	 * Add FitnessCoach
	 */
	@PostMapping("/gym/{gymId}/fitness_coach")
	@ResponseStatus(code = HttpStatus.CREATED)
	public GymFitnessCoach addFitnessCoach(@PathVariable Long gymId, @RequestBody GymFitnessCoach gymFitnessCoach) {
		log.info("Adding Fitness Coach ID= " + gymFitnessCoach.getFitnessCoachId() + ", to Gym ID= " + gymId);
		
		return gymService.saveFitnessCoach(gymId, gymFitnessCoach);
	}
	
	/*
	 * Add Member
	 */
	@PostMapping("/gym/{gymId}/member")
	@ResponseStatus(code = HttpStatus.CREATED)
	public GymMember addMember(@PathVariable Long gymId, @RequestBody GymMember gymMember) {
		log.info("Adding Member ID= " + gymMember.getMemberId() + " to Gym ID= " + gymId);
		
		return gymService.saveMember(gymId, gymMember);
	}
	
	/*
	 * Retrieve all Gyms
	 */
	@GetMapping("/gym")
	public List<GymData> retrieveAllGyms() {
		log.info("Retrieving all gyms");
		
		return gymService.retrieveAllGyms();
	}
	
	/*
	 * Retrieve Gym by ID
	 */
	@GetMapping("/gym/{gymId}")
	public GymData getGymById(@PathVariable Long gymId) {
		log.info("Retrieving Gym ID= " + gymId);
		
		return gymService.getGymById(gymId);
	}
	
	/*
	 * Delete Gym by ID
	 */
	@DeleteMapping("/gym/{gymId}")
	public Map<String, String> deleteGymById(@PathVariable Long gymId) {
		log.info("Deleting Gym ID= " + gymId);
		
		gymService.deleteGymById(gymId);
		
		return Map.of("message", "Success in deleting Gym ID= " + gymId);
	}
	
	@PatchMapping("/{gymId}/{fitnessCoachID}")
	public GymFitnessCoach bookACoach(@PathVariable Long gymId, @PathVariable Long fitnessCoachID) {
		// This will be used to set the booked status of the FitnessCoach
		boolean booked = true;

		log.info("Booking first available in Gym ID= " + gymId);
		
		return gymService.bookACoach(gymId, fitnessCoachID, booked);
	}
	
	@PatchMapping("/{gymId}")
	public GymFitnessCoach bookARandomCoach(@PathVariable Long gymId) {
		boolean booked = true;
		log.info("Bookung a random Fitness Coach in Gym ID= " + gymId);
		
		return gymService.bookARandomCoach(gymId, booked);
	}
	
	
	/*
	 * Retrieve all Gyms
	 */
	@GetMapping("/gym/{gymId}/")
	public List<GymFitnessCoach> retrieveAllCoachesByGymId(@PathVariable Long gymId) {
		log.info("Retrieving all Fitness Coaches in Gym ID= " + gymId);
		
		return gymService.retrieveAllFitnessCoachesByGymId(gymId);
	}
}
