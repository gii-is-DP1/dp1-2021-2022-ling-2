package org.springframework.ntfh.entity.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.hibernate.validator.constraints.Length;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.user.authorities.Authorities;

import lombok.Getter;
import lombok.Setter;

/**
 * @author andrsdt
 */
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
	@Id
	@NotBlank(message = "Username is required")
	@Length(min = 4, max = 20, message = "Your username must be 4-20 characters long")
	private String username;

	@Length(min = 4, message = "Your password must be at least 4 characters long")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@Column(unique = true)
	@NotBlank(message = "The email must not be empty")
	@Email(message = "Please provide a valid email")
	private String email;

	@NotNull
	@Column(columnDefinition = "boolean default true")
	private Boolean enabled; // If a user gets banned, he/she will get disabled

	// TODO the cascade type is yet to be determined
	// @OneToOne(mappedBy = "host")
	// TODO is this even used? Set to null on new creations
	@ManyToOne
	@JoinColumn(name = "game")
	@JsonIgnore
	private Game game; // game where the user is currently in

	@ManyToOne
	@JoinColumn(name = "lobby")
	@JsonIgnoreProperties({ "users", "host", "leader" })
	private Lobby lobby; // lobby where the user is currently in

	@ManyToOne // TODO set appropiate cascade type
	@JoinColumn(name = "character")
	private Character character;
	// Character that the user has currently selected. Will be set during a lobby,
	// and will stay the same during the entire game he/she is playing. Useful later
	// for social interactions (your friends can see the character you are playing
	// in your current game)

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnoreProperties({ "user" })
	private Set<Authorities> authorities;
}
