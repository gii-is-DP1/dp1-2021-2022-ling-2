package org.springframework.samples.ntfh.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;

@Getter
@MappedSuperclass
public class EnemyEntity extends BaseEntity {
    // TODO abstract? It should not be implemented by the user directly so...
    // Or is it enough by not annotating it as @Entity?
    @Column(name = "endurance")
    @NotEmpty
    private Integer endurance;
}
