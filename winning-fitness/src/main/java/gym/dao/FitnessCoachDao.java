package gym.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gym.entity.FitnessCoach;

public interface FitnessCoachDao extends JpaRepository<FitnessCoach, Long>{

}
