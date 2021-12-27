package org.springframework.ntfh.entity.turn;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.scene.Scene;

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
    @JsonIgnoreProperties({ "glory", "kills", "gold", "wounds", "user", "characterType" })
    private Player player;

    @ManyToOne(optional = true)
    @JoinColumn(name = "scene_id") // TODO needed?
    private Scene currentScene;

    @Transient
    TurnState state;
    @Transient
    TurnState EnemyState;
    @Transient
    TurnState MarketState;
    @Transient
    TurnState PlayerState;
    @Transient
    TurnState RefreshState;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TurnStageEnum stage;
}
