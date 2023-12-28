package gym.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;
	
	private String memberFirstName;
	private String membeLastName;
	private String memberEmail;
	private String memberPhone;
	
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@ManyToMany(mappedBy = "members", cascade = CascadeType.PERSIST)
	private Set<Gym> gyms = new HashSet<>();
}
