package org.springframework.samples.ntfh.enemy;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.enumerates.WarlordType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "warlords")
public class Warlord extends Enemy {

    @NotNull
    @Enumerated(EnumType.STRING)
    private WarlordType type;
}
