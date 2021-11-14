package org.springframework.samples.ntfh.enemy;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Enemy extends NamedEntity {
    @NotNull
    private Integer endurance;

    @NotNull
    private String image;
}
