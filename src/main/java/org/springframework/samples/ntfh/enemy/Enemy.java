package org.springframework.samples.ntfh.enemy;

import org.springframework.samples.ntfh.interfaces.EnemyType;
import org.springframework.samples.ntfh.interfaces.PhysicalCard;

public interface Enemy extends PhysicalCard {

    public Integer getEndurance();

    public EnemyType getType();
}
