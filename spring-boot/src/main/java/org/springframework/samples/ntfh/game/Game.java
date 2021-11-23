package org.springframework.samples.ntfh.game;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Transient;
import org.springframework.samples.ntfh.comments.Comment;
import org.springframework.samples.ntfh.model.BaseEntity;
import org.springframework.samples.ntfh.player.Player;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pabrobcam
 * @author andrsdt
 */
@Getter
@Setter
@Entity
@Table(name = "games")
public class Game extends BaseEntity {

    @NotNull // Set by the server to Time.now()
    private Long startTime; // unix timestamp

    @NotNull // Set from Lobby
    private Boolean hasScenes;

    // Set from Lobby by creating Players instances from users
    @OneToMany // TODO cascade? If we set CascadeType.ALL then deleting the game will delete
               // the players. It shouldn't be like that.
    private Set<Player> players;

    @OneToOne
    private Player leader;

    // TODO a good idea would be to have a GameHistory entity where finished games
    // are stored. This way, this class will only be used to refer to the game a
    // User is currently playing in, and the GameHistory will be an immutable table
    // whose rows are created with the final results of a game (including players
    // who were playing on it, finish time and many others that will only matter
    // once the game has finished)

}
