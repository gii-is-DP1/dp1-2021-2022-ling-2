package org.springframework.ntfh.entity.game;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.Transient;
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
    @JsonFormat(pattern = "dd/MM/yyyy | HH:mm:ss")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd/MM/yyyy | HH:mm:ss")
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

    @OneToOne(cascade = CascadeType.ALL)
    private Player leader;

    @OneToOne(cascade = CascadeType.ALL)
    private Player winner;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotAudited
    @JsonIgnore
    private List<Turn> turns = new ArrayList<>();

    // TODO should these four be NotAudited? If they are audited,
    // can they help with the statistics stuff? (Play X card Y times...)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EnemyIngame> enemiesInPile = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EnemyIngame> enemiesFighting = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MarketCardIngame> marketCardsInPile = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MarketCardIngame> marketCardsForSale = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private GameStateType stateType;

    /**
     * 
     * @author andrsdt
     * @return A list of the players in the game sorted by their turn order
     */
    @Transient
    @JsonIgnore
    public List<Player> getAlivePlayersInTurnOrder() {
        return players.stream().filter(p -> !p.isDead()).sorted((p1, p2) -> p1.getTurnOrder() - p2.getTurnOrder())
                .collect(Collectors.toList());
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
    @Transient
    @JsonFormat(pattern = "HH:mm:ss")
    public Timestamp getDuration() {
        return (finishTime == null) ? null : new Timestamp(finishTime.getTime() - startTime.getTime());
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
