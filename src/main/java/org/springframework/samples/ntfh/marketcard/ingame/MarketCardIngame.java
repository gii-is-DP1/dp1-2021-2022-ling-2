package org.springframework.samples.ntfh.marketcard.ingame;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.marketcard.MarketCard;
import org.springframework.samples.ntfh.model.IngameEntity;

public class MarketCardIngame extends IngameEntity {
    @ManyToOne(optional = false)
    private MarketCard marketCard;

    @NotNull
    private MarketCardLocation marketCardLocation;
}
