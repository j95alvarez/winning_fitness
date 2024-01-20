package gym.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
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
		copyGymFields(gym, gymData);
		
		return new GymData(gymDao.save(gym));
	}

	private void copyGymFields(Gym gym, GymData gymData) {
		// TODO Auto-generated method stub
		gym.setGymName(gymData.getGymName());
		gym.setGymAddress(gymData.getGymAddress());
		gym.setGymCity(gymData.getGymCity());
		gym.setGymZipcode(gymData.getGymZipcode());
		gym.setGymPhone(gymData.getGymPhone());
	}

	private Gym findOrCreateGym(Long gymId) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		Gym gym = findOrCreateGym(gymId);
		Long fitnessCoachId = gymFitnessCoach.getFitnessCoachId();
		FitnessCoach fitnessCoach = findOrCreateFitnessCoach(gymId, fitnessCoachId);
		
		copyFitnessCoachFields(fitnessCoach, gymFitnessCoach);
		fitnessCoach.setGym(gym);
		
		gym.getFitnessCoaches().add(fitnessCoach);
		
		return new GymFitnessCoach(fitnessCoachDao.save(fitnessCoach));
	}

	private void copyFitnessCoachFields(FitnessCoach fitnessCoach, GymFitnessCoach gymFitnessCoach) {
		// TODO Auto-generated method stub
		fitnessCoach.setFitnessCoachFirstName(gymFitnessCoach.getFitnessCoachFirstName());
		fitnessCoach.setFitnessCoachLastName(gymFitnessCoach.getFitnessCoachLastName());
		fitnessCoach.setFitnessCoachemail(gymFitnessCoach.getFitnessCoachEmail());
		fitnessCoach.setFitnessCoachPhone(gymFitnessCoach.getFitnessCoachPhone());
		fitnessCoach.setFitnessCoachSpecialty(gymFitnessCoach.getFitnessCoachSpecialty());
		fitnessCoach.setFitnessCoachId(gymFitnessCoach.getFitnessCoachId());
		fitnessCoach.setBooked(gymFitnessCoach.isBooked());
	}

	private FitnessCoach findOrCreateFitnessCoach(Long gymId, Long fitnessCoachId) {
		// TODO Auto-generated method stub
		if (fitnessCoachId == null)
			return new FitnessCoach();
		else
			return findFitnessCoachById(gymId, fitnessCoachId);
	}

	private FitnessCoach findFitnessCoachById(Long gymId, Long fitnessCoachId) {
		// TODO Auto-generated method stub
		FitnessCoach fitnessCoach = fitnessCoachDao.findById(fitnessCoachId).orElseThrow(
				() -> new NoSuchElementException("FitnessCoach ID= " + fitnessCoachId + " was not found!"));
		
		if (fitnessCoach.getGym().getGymId() != gymId)
			throw new IllegalArgumentException("Employee ID= " + fitnessCoachId + " is not in Gym ID= " + gymId);
		
		return fitnessCoach;
		/*
		if (fitnessCoach.getGym().getGymId().equals(gymId))
			return fitnessCoach;
		else
			throw new IllegalArgumentException("Employee ID= " + fitnessCoachId + " is not in Gym ID= " + gymId);
		*/
	}

	/*
	 * Member Services
	 */
	@Transactional(readOnly = false)
	public GymMember saveMember(Long gymId, GymMember gymMember) {
		// TODO Auto-generated method stub
		Gym gym = findOrCreateGym(gymId);
		Long memberId = gymMember.getMemberId();
		Member member = findOrCreateMember(gymId, memberId);
		
		copyGymMemberFields(member, gymMember);
		
		member.getGyms().add(gym);
		gym.getMembers().add(member);
		
		return new GymMember(memberDao.save(member));
	}
	
	private void copyGymMemberFields(Member member, GymMember gymMember) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		Member member = memberDao.findById(memberId).orElseThrow(
				() -> new NoSuchElementException("Member ID= " + memberId + " was not found!"));
		
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
		
		gymDao.delete(gym);
	}

	/*
	 * Book a Coach by ID
	 */
	@Transactional(readOnly = false)
	public GymFitnessCoach bookACoach(Long gymId, Long fitnessCoachID, boolean booked) {
		// TODO Auto-generated method stub
		// Finds the Gym in which the user wants to book the next available FitnessCoach
		FitnessCoach fitnessCoach = findOrCreateFitnessCoach(gymId, fitnessCoachID);
		
		fitnessCoach.setBooked(booked);
		
		return new GymFitnessCoach(fitnessCoachDao.save(fitnessCoach));
		/*
		// Iterate through the set until a coach is not booked
		for (FitnessCoach fitnessCoach : gym.getFitnessCoaches()) {
			
			if (!fitnessCoach.isBooked()) {
				// Set the status of the FitnessCoach to true
				fitnessCoach.setBooked(booked);
				return new GymFitnessCoach(fitnessCoachDao.save(fitnessCoach));
			}
		}
		return null;
		*/
	}
	
	
}
