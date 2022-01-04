package org.springframework.ntfh.entity.turn;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // TODO is this bidirectionality needed? I think yes
    @ManyToOne
    @JoinColumn(name = "game_id") // TODO needed?
    @JsonIgnore
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
}
