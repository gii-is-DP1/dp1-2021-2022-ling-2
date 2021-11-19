package org.springframework.samples.ntfh.character;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.Getter;

@Getter
@Entity
@Table(name = "characters")
public class Character {
    @Enumerated(EnumType.STRING)
    private CharacterTypeEnum characterTypeEnum;

    @Enumerated(EnumType.STRING)
    private CharacterGenderEnum characterGenderEnum;

}
