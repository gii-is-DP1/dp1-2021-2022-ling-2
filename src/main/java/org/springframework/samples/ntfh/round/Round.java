package org.springframework.samples.ntfh.round;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.samples.ntfh.game.Game;
import org.springframework.samples.ntfh.model.BaseEntity;
import org.springframework.samples.ntfh.scene.Scene;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Round extends BaseEntity {

    @OneToOne(optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    private Integer roundNumber;

    @ManyToOne(optional = true)
    @JoinColumn(name = "scene_id")
    private Scene currentScene;
}
