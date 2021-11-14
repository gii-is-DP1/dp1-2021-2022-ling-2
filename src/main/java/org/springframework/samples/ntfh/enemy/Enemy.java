package org.springframework.samples.ntfh.enemy;

import org.springframework.samples.ntfh.enumerates.EnemyType;

public interface Enemy {

    public Integer getEndurance();

    public String getFrontImage();

    public String getBackImage();

    public EnemyType getType();
}
