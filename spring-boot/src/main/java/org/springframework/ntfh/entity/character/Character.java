package org.springframework.ntfh.entity.character;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.proficiency.Proficiency;

import lombok.Getter;

@Getter
@Entity
@Audited
@Table(name = "characters")
public class Character extends BaseEntity {

    @NotNull
    private Integer baseHealth;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CharacterTypeEnum characterTypeEnum;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CharacterGenderEnum characterGenderEnum;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @NotAudited
    @JoinTable(name = "characters_proficiencies", joinColumns = @JoinColumn(name = "character_id"), inverseJoinColumns = @JoinColumn(name = "proficiency_id"))
    private Set<Proficiency> proficiencies;
}
