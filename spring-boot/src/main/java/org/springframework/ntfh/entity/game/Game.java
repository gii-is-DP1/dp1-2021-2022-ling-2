package org.springframework.ntfh.entity.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.Transient;
import org.springframework.ntfh.entity.comment.Comment;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pabrobcam
 * @author andrsdt
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "games")
public class Game extends BaseEntity {

    @NotNull
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date finishTime;

    @NotNull(message = "The scenes must be either on or off")
    private Boolean hasScenes;

    @NotNull(message = "The spectators must be either allowed or not allowed")
    private Boolean spectatorsAllowed;

    @NotNull(message = "The number of max players can not be null")
    private Integer maxPlayers;

    // Set from Lobby by creating Players instances from users
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game", orphanRemoval = true)
    @JsonIgnoreProperties({"game", "lobby"})
    private List<Player> players = new ArrayList<>();

    @OneToOne
    private Player leader;

    @OneToOne
    private Player winner;

    @OneToMany(mappedBy = "game")
    @NotAudited
    @JsonIgnore
    private List<Turn> turns = new ArrayList<>();

    // ! DEFINE GAME STATE ATTRIBUTE

    // TODO should these four be NotAudited? If they are audited,
    // can they help with the statistics stuff? (Play X card Y times...)
    @OneToMany
    private List<EnemyIngame> enemiesInPile = new ArrayList<>();

    @OneToMany
    private List<EnemyIngame> enemiesFighting = new ArrayList<>();

    @OneToMany
    private List<MarketCardIngame> marketCardsInPile = new ArrayList<>();

    @OneToMany
    private List<MarketCardIngame> marketCardsForSale = new ArrayList<>();

    // orphanRemoval: The "comment" rows will be deleted when the game is deleted
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    /**
     * 
     * @author andrsdt
     * @return A list of the players in the game sorted by their turn order
     */
    @Transient
    @JsonIgnore
    public List<Player> getAlivePlayersInTurnOrder() {
        return players.stream().filter(p -> !p.isDead()).sorted((p1, p2) -> p1.getTurnOrder() - p2.getTurnOrder())
                .collect(java.util.stream.Collectors.toList());
    }

    @Transient
    @JsonSerialize
    @JsonDeserialize
    @JsonIgnoreProperties({"game"})
    public Turn getCurrentTurn() {
        return turns.isEmpty() ? null : turns.get(turns.size() - 1);
    }

    /**
     * Derived. Returns the duration of the game in seconds
     * 
     * @author andrsdt
     * @return Long duration of the time in seconds
     */
    // TODO send to frontend already parsed
    // @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @Transient
    public Long getDuration() {
        return (finishTime == null) ? null : finishTime.getTime() - startTime.getTime();
    }

    @Transient
    public boolean getHasStarted() {
        return startTime != null;
    }

    @Transient
    public Boolean getHasFinished() {
        return finishTime != null;
    }
}
