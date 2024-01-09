package gym.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gym.entity.Member;

public interface MemberDao extends JpaRepository<Member, Long> {

}
