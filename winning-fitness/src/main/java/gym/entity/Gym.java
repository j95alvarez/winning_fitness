package gym.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Entity
@Data
public class Gym {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long gymId;
	
	private String gymName;
	private String gymAddress;
	private String gymCity;
	private String gymZipcode;
	private String gymPhone;
	
	
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "gym_member", 
		joinColumns = @JoinColumn(name = "gym_id"), 
		inverseJoinColumns = @JoinColumn(name = "member_id"))
	private Set<Member> members = new HashSet<>();
	
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<FitnessCoach> fitnessCoaches = new HashSet<>();

}
