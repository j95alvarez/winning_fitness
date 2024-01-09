package gym.controller.model;

import java.util.HashSet;
import java.util.Set;

import gym.entity.FitnessCoach;
import gym.entity.Gym;
import gym.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GymData {
private Long gymId;
	
	private String gymName;
	private String gymAddress;
	private String gymCity;
	private String gymZipcode;
	private String gymPhone;

	private Set<GymMember> members = new HashSet<>();
	private Set<GymFitnessCoach> fitnessCoaches = new HashSet<>();
	
	@Data
	@NoArgsConstructor
	public static class GymMember {
		private Long memberId;
		
		private String memberFirstName;
		private String memberLastName;
		private String memberEmail;
		private String memberPhone;
		
		public GymMember(Member member) {
			this.memberId = member.getMemberId();
			this.memberFirstName = member.getMemberFirstName();
			this.memberLastName = member.getMemberLastName();
			this.memberEmail = member.getMemberEmail();
			this.memberPhone = member.getMemberPhone();
		}
		
	}
	
	@Data
	@NoArgsConstructor
	public static class GymFitnessCoach {
		private Long fitnessCoachId;
		
		private String fitnessCoachFirstName;
		private String fitnessCoachLastName;
		private String fitnessCoachEmail;
		private String fitnessCoachPhone;
		private String fitnessCoachSpecialty;
		
		public GymFitnessCoach(FitnessCoach fitnessCoach) {
			this.fitnessCoachId = fitnessCoach.getFitnessCoachId();
			this.fitnessCoachFirstName = fitnessCoach.getFitnessCoachFirstName();
			this.fitnessCoachLastName = fitnessCoach.getFitnessCoachLastName();
			this.fitnessCoachEmail = fitnessCoach.getFitnessCoachemail();
			this.fitnessCoachPhone = fitnessCoach.getFitnessCoachPhone();
			this.fitnessCoachSpecialty = fitnessCoach.getFitnessCoachSpecialty();
		}
	}
	
	public GymData (Gym gym) {
		this.gymId = gym.getGymId();
		this.gymName = gym.getGymName();
		this.gymAddress = gym.getGymAddress();
		this.gymCity = gym.getGymCity();
		this.gymZipcode = gym.getGymZipcode();
		this.gymPhone = gym.getGymPhone();
		
		for (Member member : gym.getMembers()) {
			this.members.add(new GymMember(member));
		}
		
		for (FitnessCoach fitnessCoach : gym.getFitnessCoaches()) {
			this.fitnessCoaches.add(new GymFitnessCoach(fitnessCoach));
		}
	}
}
