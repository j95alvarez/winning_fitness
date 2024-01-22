package gym.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gym.controller.model.GymData;
import gym.controller.model.GymData.GymFitnessCoach;
import gym.controller.model.GymData.GymMember;
import gym.dao.FitnessCoachDao;
import gym.dao.GymDao;
import gym.dao.MemberDao;
import gym.entity.FitnessCoach;
import gym.entity.Gym;
import gym.entity.Member;

@Service
public class GymService {
	@Autowired
	private GymDao gymDao;
	
	@Autowired
	private FitnessCoachDao fitnessCoachDao;
	
	@Autowired
	private MemberDao memberDao;
	

	/*
	 * Gym Services
	 */
	public GymData saveGym(GymData gymData) {
		// TODO Auto-generated method stub
		Gym gym = findOrCreateGym(gymData.getGymId());
		// Copies the fields of GymData to Gym
		copyGymFields(gym, gymData);
		
		return new GymData(gymDao.save(gym));
	}

	private void copyGymFields(Gym gym, GymData gymData) {
		// Copies the fields of GymData to Gym
		gym.setGymName(gymData.getGymName());
		gym.setGymAddress(gymData.getGymAddress());
		gym.setGymCity(gymData.getGymCity());
		gym.setGymZipcode(gymData.getGymZipcode());
		gym.setGymPhone(gymData.getGymPhone());
	}

	private Gym findOrCreateGym(Long gymId) {
		if (gymId == null)
			return new Gym();
		else
			return gymDao.findById(gymId).orElseThrow(
					() -> new NoSuchElementException("Gym ID= " + gymId + " was not found!"));
	}

	/*
	 * FitnessCoach Services
	 */
	@Transactional(readOnly = false)
	public GymFitnessCoach saveFitnessCoach(Long gymId, GymFitnessCoach gymFitnessCoach) {
		Gym gym = findOrCreateGym(gymId);
		Long fitnessCoachId = gymFitnessCoach.getFitnessCoachId();
		FitnessCoach fitnessCoach = findOrCreateFitnessCoach(gymId, fitnessCoachId);
		
		// Copies fields of GymFitnessCoach to FitnessCoach
		copyFitnessCoachFields(fitnessCoach, gymFitnessCoach);
		fitnessCoach.setGym(gym);
		
		gym.getFitnessCoaches().add(fitnessCoach);
		
		return new GymFitnessCoach(fitnessCoachDao.save(fitnessCoach));
	}

	private void copyFitnessCoachFields(FitnessCoach fitnessCoach, GymFitnessCoach gymFitnessCoach) {
		// Copies the fields of GymFitnessCoach to FitnessCoach
		fitnessCoach.setFitnessCoachFirstName(gymFitnessCoach.getFitnessCoachFirstName());
		fitnessCoach.setFitnessCoachLastName(gymFitnessCoach.getFitnessCoachLastName());
		fitnessCoach.setFitnessCoachemail(gymFitnessCoach.getFitnessCoachEmail());
		fitnessCoach.setFitnessCoachPhone(gymFitnessCoach.getFitnessCoachPhone());
		fitnessCoach.setFitnessCoachSpecialty(gymFitnessCoach.getFitnessCoachSpecialty());
		fitnessCoach.setFitnessCoachId(gymFitnessCoach.getFitnessCoachId());
		fitnessCoach.setBooked(gymFitnessCoach.isBooked());
	}

	private FitnessCoach findOrCreateFitnessCoach(Long gymId, Long fitnessCoachId) {
		if (fitnessCoachId == null)
			return new FitnessCoach();
		else
			return findFitnessCoachById(gymId, fitnessCoachId);
	}

	private FitnessCoach findFitnessCoachById(Long gymId, Long fitnessCoachId) {
		FitnessCoach fitnessCoach = fitnessCoachDao.findById(fitnessCoachId).orElseThrow(
				() -> new NoSuchElementException("FitnessCoach ID= " + fitnessCoachId + " was not found!"));
		
		// Searches through the gym and checks to see if the Fitness Coach belongs to the gym
		if (fitnessCoach.getGym().getGymId() != gymId)
			throw new IllegalArgumentException("Employee ID= " + fitnessCoachId + " is not in Gym ID= " + gymId);
		
		return fitnessCoach;
	}

	/*
	 * Member Services
	 */
	@Transactional(readOnly = false)
	public GymMember saveMember(Long gymId, GymMember gymMember) {
		Gym gym = findOrCreateGym(gymId);
		Long memberId = gymMember.getMemberId();
		Member member = findOrCreateMember(gymId, memberId);
		
		// Copies the fields of GymMember into Member
		copyGymMemberFields(member, gymMember);
		
		// Adds the Gym to the list of Gyms the member belongs too
		member.getGyms().add(gym);
		// Adds the member to the Gym
		gym.getMembers().add(member);
		
		return new GymMember(memberDao.save(member));
	}
	
	private void copyGymMemberFields(Member member, GymMember gymMember) {
		// Copies the fields of GymMember to Member
		member.setMemberFirstName(gymMember.getMemberFirstName());
		member.setMemberLastName(gymMember.getMemberLastName());
		member.setMemberEmail(gymMember.getMemberEmail());
		member.setMemberPhone(gymMember.getMemberPhone());
		member.setMemberId(gymMember.getMemberId());
	}

	private Member findOrCreateMember(Long gymId, Long memberId) {
		if (memberId == null) 
			return new Member();
		else
			return findMemberById(gymId, memberId);
	}

	private Member findMemberById(Long gymId, Long memberId) {
		Member member = memberDao.findById(memberId).orElseThrow(
				() -> new NoSuchElementException("Member ID= " + memberId + " was not found!"));
		
		// Searches through the gym and checks to see if the member belongs to the gym
		for (Gym gym : member.getGyms())
			if (gym.getGymId().equals(gymId))
				return member;
		
		throw new IllegalArgumentException("Member ID= " + memberId + " is not in Gym ID= " + gymId);
	}
	
	/*
	 * Retrieve all gyms
	 */
	@Transactional(readOnly = true)
	public List<GymData> retrieveAllGyms() {
		// TODO Auto-generated method stub
		List<Gym> gyms = gymDao.findAll();
		List<GymData> result = new LinkedList<>();
		
		// Generates a list of all the gyms to retrieve
		for (Gym gym : gyms) {
			GymData gymData = new GymData(gym);
			
			gymData.getMembers().clear();
			gymData.getFitnessCoaches().clear();
			
			result.add(gymData);
		}
		
		return result;
	}

	/*
	 * Get Gym By ID
	 */
	@Transactional(readOnly = true)
	public GymData getGymById(Long gymId) {
		// TODO Auto-generated method stub
		GymData gymData = new GymData();
		
		Gym gym = findOrCreateGym(gymId);
		
		// If the gym is not null, it will copy the fields
		if (gym != null) {
			gymData.setGymId(gym.getGymId());
			gymData.setGymName(gym.getGymName());
			gymData.setGymAddress(gym.getGymAddress());
			gymData.setGymCity(gym.getGymCity());
			gymData.setGymZipcode(gym.getGymZipcode());
			gymData.setGymPhone(gym.getGymPhone());
		}
		
		return gymData;
	}

	/*
	 * Delete Gym by ID
	 */
	@Transactional(readOnly = false)
	public void deleteGymById(Long gymId) {
		// TODO Auto-generated method stub
		Gym gym = findOrCreateGym(gymId);
		
		// Deletes the Gym table
		gymDao.delete(gym);
	}

	/*
	 * Book a Coach by ID
	 */
	@Transactional(readOnly = false)
	public GymFitnessCoach bookACoach(Long gymId, Long fitnessCoachID, boolean booked) {
		// TODO Auto-generated method stub
		// Finds the Fitness Coach that the user wants to book
		FitnessCoach fitnessCoach = findOrCreateFitnessCoach(gymId, fitnessCoachID);
		
		// Sets status of the Fitness Coach to booked
		fitnessCoach.setBooked(booked);
		
		return new GymFitnessCoach(fitnessCoachDao.save(fitnessCoach));
	}

	@Transactional(readOnly = false)
	public GymFitnessCoach bookARandomCoach(Long gymId, boolean booked) {
		// TODO Auto-generated method stub

		Random ran = new Random();

		Gym gym = findOrCreateGym(gymId);
		
		// Retrieve the list of FitnessCoaches in the Gym, initialize the counter to 0,
		// and chose a random index to iterate through the set in order to retrieve the
		// Fitness Coach we are booking at Random
		Set<FitnessCoach> fitnessCoaches = gym.getFitnessCoaches();
		int count = 0;
		int random_fitnessCoach_index = ran.nextInt(fitnessCoaches.size());
		
		// Initialize the Coach that will be booked
		FitnessCoach coachToBook = fitnessCoaches.iterator().next();
		
		for (FitnessCoach fitnessCoach : fitnessCoaches) {
			// Checks the if the counter has reached the random index generated
			// and checks to see if that Coach is booked
			if ((count == random_fitnessCoach_index) && (coachToBook.isBooked())) {
				// Random Coach is set to the coachToBook variable
				coachToBook = fitnessCoach;
			}
			else {
				count++;
			}
		}
		
		// Sets the status of the coach that was chosen randomly to true
		coachToBook.setBooked(booked);
		
		return new GymFitnessCoach(fitnessCoachDao.save(coachToBook));
	}

	public List<GymFitnessCoach> retrieveAllFitnessCoachesByGymId(Long gymId) {
		Gym gym = findOrCreateGym(gymId);
		Set<FitnessCoach> fitnessCoaches = gym.getFitnessCoaches();
		List<GymFitnessCoach> result = new LinkedList<>();
		
		for (FitnessCoach fitnessCoach : fitnessCoaches) {
			GymFitnessCoach gymFitnessCoach = new GymFitnessCoach(fitnessCoach);
			
			result.add(gymFitnessCoach);
			
		}
		return result;
	}
	
	/*
	 * 	 * Retrieve all gyms
	 
	@Transactional(readOnly = true)
	public List<GymData> retrieveAllGyms() {
		// TODO Auto-generated method stub
		List<Gym> gyms = gymDao.findAll();
		List<GymData> result = new LinkedList<>();
		
		// Generates a list of all the gyms to retrieve
		for (Gym gym : gyms) {
			GymData gymData = new GymData(gym);
			
			gymData.getMembers().clear();
			gymData.getFitnessCoaches().clear();
			
			result.add(gymData);
		}
		
		return result;
	}
	 */
	
}
