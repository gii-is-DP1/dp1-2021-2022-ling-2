package org.springframework.samples.ntfh.round;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    @Column(name = "game_id")
    @OneToOne(optional = false)
    private Game game;

    private Integer roundNumber;

    @Column(name = "scene_id")
    @ManyToOne(optional = true)
    private Scene currentScene;
}
