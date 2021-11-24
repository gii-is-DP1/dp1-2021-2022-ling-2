package org.springframework.samples.ntfh.enemy.ingame;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.enemy.Warlord;
import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "warlords_ingame")
public class WarlordIngame extends BaseEntity{
    @ManyToOne(optional = false)
    @JoinColumn(name = "wardlord_id")
    private Warlord warlord;

    @NotNull
    private Integer currentEndurance;

    // TODO check if this is overriding IngameEntity's location
    @NotNull
    @Enumerated(EnumType.STRING)
    private EnemyLocation warlordLocation;
}
