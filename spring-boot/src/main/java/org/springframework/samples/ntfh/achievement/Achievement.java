package org.springframework.samples.ntfh.achievement;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "achievements")
public class Achievement extends NamedEntity {

    // private Date date; // TODO: Date of? shouldn't be used.
    // private Predicate<Object> achievementCondition;
    // This can't be stored as a predicate here because it can't be stored in a db.

    @NotNull
    private String description;
}
