package org.springframework.ntfh.entity.enemy.warlord.ingame;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.ntfh.entity.enemy.EnemyLocation;
import org.springframework.ntfh.entity.enemy.warlord.Warlord;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author andrsdt
 * @author alegestor
 */
@Entity
@Getter
@Setter
@Table(name = "warlords_ingame")
public class WarlordIngame extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "wardlord_id")
    private Warlord warlord;

    @NotNull
    private Integer currentEndurance;

    // TODO check if this is overriding IngameEntity's location
    @NotNull
    @Enumerated(EnumType.STRING)
    private EnemyLocation location;

    @ManyToOne
    @JsonIgnore
    private Game game;
}
