package gym.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import gym.entity.Gym;

public interface GymDao extends JpaRepository<Gym, Long>{

}
