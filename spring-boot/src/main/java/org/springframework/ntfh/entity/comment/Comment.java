package org.springframework.ntfh.entity.comment;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import org.hibernate.envers.Audited;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.user.User;
import lombok.Getter;
import lombok.Setter;

/**
 * Currently a placeholder. creating the entity was necessary to associate it with the game entity. It's yet to be
 * modeled and completed
 * 
 * @author andrsdt
 */
@Getter
@Setter
@Entity
@Audited
// TODO table()...?
public class Comment extends BaseEntity {

    @NotEmpty(message = "A comment must not be empty")
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
