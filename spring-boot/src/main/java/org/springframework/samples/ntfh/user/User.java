package org.springframework.samples.ntfh.user;

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

import org.hibernate.validator.constraints.Length;
import org.springframework.samples.ntfh.character.Character;
import org.springframework.samples.ntfh.game.Game;
import org.springframework.samples.ntfh.lobby.Lobby;
import org.springframework.samples.ntfh.user.authorities.Authorities;

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
	@NotBlank
	@Length(min = 4, max = 20, message = "The username must be 4-20 characters long")
	private String username;

	@NotBlank
	@Length(min = 4, message = "Password must be at least 4 characters long")
	private String password;

	@NotNull
	@Email(message = "Please provide a valid Email")
	private String email;

	@NotNull
	@Column(columnDefinition = "boolean default true")
	private boolean enabled; // If a user gets banned, he/she will get disabled

	// TODO the cascade type is yet to be determined
	// @OneToOne(mappedBy = "host")
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
	private Character character; // Character that the user has currently
	// selected. Will be set during a lobby,
	// and will stay the same during the entire game he/she is playing.

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnore
	private Set<Authorities> authorities;
}
