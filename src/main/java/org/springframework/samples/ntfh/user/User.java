package org.springframework.samples.ntfh.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
	@Id
	@NotBlank
	@Length(min = 4, max = 20)
	private String username;

	@NotBlank
	@Length(min = 1)
	private String password;

	@NotNull
	@Email(message = "Please provide a valid Email")
	private String email;

	@NotNull
	@Column(columnDefinition = "boolean default false")
	private boolean isBanned;

	@NotNull
	@Column(columnDefinition = "boolean default true")
	private boolean enabled; // Legacy from Petclinic, currently needed for CRUD of users. To be removed

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnore
	private Set<Authorities> authorities;
}
