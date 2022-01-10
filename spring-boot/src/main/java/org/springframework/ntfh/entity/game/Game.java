package org.springframework.ntfh.entity.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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

    @NotNull // Set by the server to Time.now()
    private Long startTime; // unix timestamp

    @NotNull // Set from Lobby
    private Boolean hasScenes;

    // Set from Lobby by creating Players instances from users
    @OneToMany
    @JsonIgnoreProperties({ "game", "lobby" })
    private List<Player> players;

    @OneToOne
    @JsonIgnore
    private Player leader;

    @OneToMany(mappedBy = "game")
    @NotAudited
    @JsonIgnore
    private List<Turn> turns = new ArrayList<>();

    // TODO should these be not audited?
    @OneToMany
    @NotAudited
    private List<EnemyIngame> enemiesInPile = new ArrayList<>();

    @OneToMany
    @NotAudited
    private List<EnemyIngame> enemiesFighting = new ArrayList<>();

    @OneToMany
    @NotAudited
    private List<MarketCardIngame> marketCardsInPile = new ArrayList<>();

    @OneToMany
    @NotAudited
    private List<MarketCardIngame> marketCardsForSale = new ArrayList<>();

    /**
     * 
     * @author andrsdt
     * @return A list of the players in the game sorted by their turn order
     */
    @Transient
    @JsonIgnore
    public List<Player> getAlivePlayersInTurnOrder() {
        return players.stream()
                .filter(p -> !p.isDead())
                .sorted((p1, p2) -> p1.getTurnOrder() - p2.getTurnOrder())
                .collect(java.util.stream.Collectors.toList());
    }

    @Transient
    @JsonSerialize
    @JsonDeserialize
    @JsonIgnoreProperties({ "game" })
    public Turn getCurrentTurn() {
        return turns.isEmpty() ? null : turns.get(turns.size() - 1);
    }
}
