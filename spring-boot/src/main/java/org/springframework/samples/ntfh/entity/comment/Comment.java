package org.springframework.samples.ntfh.entity.comment;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.samples.ntfh.entity.game.Game;
import org.springframework.samples.ntfh.entity.model.BaseEntity;
import org.springframework.samples.ntfh.entity.user.User;

import lombok.Getter;
import lombok.Setter;

/**
 * Currently a placeholder. creating the entity was necessary to associate it
 * with the game entity. It's yet to be modeled and completed
 * 
 * @author andrsdt
 */
@Getter
@Setter
@Entity
public class Comment extends BaseEntity {

    private String text;

    // Many comments can be made in a game
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    // Many comments can be made by one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
