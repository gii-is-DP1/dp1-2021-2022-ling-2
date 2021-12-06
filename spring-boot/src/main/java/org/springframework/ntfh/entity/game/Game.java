package org.springframework.ntfh.entity.game;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.player.Player;

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
    @JsonIgnoreProperties({ "game", "lobby" })
    private List<Player> players;

    @OneToOne
    @JsonIgnoreProperties({ "game" })
    private Player leader;
}
