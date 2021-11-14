package org.springframework.samples.ntfh.enemy;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Enemy extends BaseEntity {
    @NotNull
    private Integer endurance;

    @NotNull
    private String image;
}
