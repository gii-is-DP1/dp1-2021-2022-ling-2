package org.springframework.samples.ntfh.model;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.game.Game;
import org.springframework.samples.ntfh.interfaces.Location;

/**
 * This class will be the base class for all entities that will be physically in
 * the game. This means that their row in the corresponding table has to include
 * the game that the card is in and the location in the table where the card is.
 * 
 * @author andrsdt
 */
@MappedSuperclass
public class IngameEntity extends BaseEntity {

    @NotNull
    @Column(name = "game_id")
    @ManyToOne
    private Game game;

    @NotNull
    private Location location;
}
