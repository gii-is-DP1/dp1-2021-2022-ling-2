package org.springframework.ntfh.entity.playablecard.marketcard.ingame;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCard;

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
    @JoinColumn(name = "market_card_id") // TODO redundant?
    private MarketCard marketCard;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MarketCardLocation location;
}
