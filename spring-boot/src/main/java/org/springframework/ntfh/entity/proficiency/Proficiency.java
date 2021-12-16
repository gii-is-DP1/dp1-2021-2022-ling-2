package org.springframework.ntfh.entity.proficiency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.ntfh.entity.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "proficiencies")
public class Proficiency extends BaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProficiencyTypeEnum proficiencyTypeEnum;

    @NotNull
    private Integer secondaryDebuff; // 0 or -1

}