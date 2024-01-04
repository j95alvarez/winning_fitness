package gym.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gym.controller.model.GymData;
import gym.dao.GymDao;
import gym.entity.Gym;

@Service
public class GymService {
	@Autowired
	private GymDao gymDao;

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
	
	
}
