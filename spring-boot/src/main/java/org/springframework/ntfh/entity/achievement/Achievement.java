package org.springframework.ntfh.entity.achievement;

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
@Table(name = "achievements")
public class Achievement extends NamedEntity {

    @NotEmpty(message = "The description must not be empty")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private AchievementType type;

    private Integer condition; // Win X times, lose X times, etc.
}
