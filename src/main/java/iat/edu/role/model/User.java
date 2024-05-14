package iat.edu.role.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name="users")
public class User {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;
	
	@NotBlank(message = "name is required")
	@Column(name = "user_name")
	private String userName;
	
	@Size(min = 4,
			message = "The length of password must be at least 6 characters")
	@NotBlank(message = "password is required")
	@Column(name = "password")
	private String password;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@Column (name = "photo", nullable = true, length = 64)
	private String photo;
	
	@Transient
	private MultipartFile file;
	
	@Transient
	public String getPhotosImagePath() {
		if (photo == null || userId == null) return null;
		return "/user-photos/" + userId + "/" + photo;
	}
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn (name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> roles = new HashSet<> ();
	public List<String> getRoles() {
		List<String> retList = new ArrayList<> ();
		roles.forEach(role -> retList.add(role.getName()));
		return retList;
	}
}
