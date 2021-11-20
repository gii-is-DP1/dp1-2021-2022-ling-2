package org.springframework.samples.ntfh.enemy.ingame;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.enemy.Warlord;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "warlords_ingame")
public class WarlordIngame {
    @ManyToOne(optional = false)
    @JoinColumn(name = "wardlord_id")
    private Warlord warlord;

    @NotNull
    private Integer currentEndurance;

    // TODO check if this is overriding IngameEntity's location
    @NotNull
    private EnemyLocation warlordLocation;
}
