package org.springframework.samples.ntfh.enemy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;

@Getter
@MappedSuperclass
public class EnemyEntity extends BaseEntity {
    // TODO abstract? It should not be implemented by the user directly so...
    @Column(name = "endurance")
    @NotEmpty
    private Integer endurance;
}
