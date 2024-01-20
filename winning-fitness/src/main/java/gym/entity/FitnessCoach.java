package gym.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class FitnessCoach {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fitnessCoachId;
	
	private String fitnessCoachFirstName;
	private String fitnessCoachLastName;
	private String fitnessCoachemail;
	private String fitnessCoachPhone;
	private String fitnessCoachSpecialty;
	private boolean isBooked;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "gym_id")
	private Gym gym;
	
}
