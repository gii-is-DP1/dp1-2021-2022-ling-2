package org.springframework.ntfh.achievement;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.ntfh.entity.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "achievements") // TODO redundant?
public class Achievement extends NamedEntity {

    @NotEmpty
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonIgnore // This is for internal handling
    private AchievementType type;
}
