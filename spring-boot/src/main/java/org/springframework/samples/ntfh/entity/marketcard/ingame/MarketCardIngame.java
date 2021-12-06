package org.springframework.samples.ntfh.entity.marketcard.ingame;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.samples.ntfh.entity.game.Game;
import org.springframework.samples.ntfh.entity.marketcard.MarketCard;
import org.springframework.samples.ntfh.entity.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author andrsdt
 */
@Entity
@Getter
@Setter
@Table(name = "market_cards_ingame")
public class MarketCardIngame extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "market_card_id")
    private MarketCard marketCard;

    @ManyToOne
    @JsonIgnore
    private Game game;

    // TODO check if this is overriding IngameEntity's location
    @NotNull
    @Enumerated(EnumType.STRING)
    private MarketCardLocation location;
}
