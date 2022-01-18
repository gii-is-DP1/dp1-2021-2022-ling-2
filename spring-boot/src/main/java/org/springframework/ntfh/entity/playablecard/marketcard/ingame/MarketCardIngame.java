package org.springframework.ntfh.entity.playablecard.marketcard.ingame;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
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
@Audited
@Table(name = "market_cards_ingame")
public class MarketCardIngame extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "market_card_id") // TODO redundant?
    @NotAudited
    private MarketCard marketCard;
}
