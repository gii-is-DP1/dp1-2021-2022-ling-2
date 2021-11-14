package org.springframework.samples.ntfh.enemy;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Warlord extends Enemy {
    @NotNull
    private WarlordType type;
}
