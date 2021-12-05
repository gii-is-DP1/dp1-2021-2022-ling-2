package org.springframework.samples.ntfh.model;

import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.game.Game;
import org.springframework.samples.ntfh.interfaces.Location;

import lombok.Getter;
import lombok.Setter;

/**
 * This class will be the base class for all entities that will be physically in
 * the game. This means that their row in the corresponding table has to include
 * the game that the card is in and the location in the table where the card is.
 * 
 * @author andrsdt
 */
@MappedSuperclass
@Getter
@Setter
public class IngameEntity extends BaseEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @NotNull
    private Location location;
}
