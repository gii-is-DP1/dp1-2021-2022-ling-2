package org.springframework.ntfh.entity.turn;

import java.beans.Transient;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.scene.Scene;
import org.springframework.ntfh.entity.turn.concretestates.EnemyState;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.ntfh.entity.turn.concretestates.RefreshState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Turn extends BaseEntity {

    // TODO is this bidirectionality needed? I don't think so
    @OneToOne(optional = false)
    @JoinColumn(name = "game_id") // TODO needed?
    @JsonIgnoreProperties({ "startTime" })
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_id") // TODO needed?
    @JsonIgnoreProperties({ "glory", "kills", "gold", "wounds", "characterType" })
    private Player player;

    @ManyToOne(optional = true)
    @JoinColumn(name = "scene_id") // TODO needed?
    private Scene currentScene;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TurnStateType stateType;

    @Transient
    public TurnState getState() {
        if (stateType == TurnStateType.PLAYER_STATE) {
            return new PlayerState();
        } else if (stateType == TurnStateType.MARKET_STATE) {
            return new MarketState();
        } else if (stateType == TurnStateType.ENEMY_STATE) {
            return new EnemyState();
        } else if (stateType == TurnStateType.REFRESH_STATE) {
            return new RefreshState();
        } else {
            return null;
        }
    }
}
