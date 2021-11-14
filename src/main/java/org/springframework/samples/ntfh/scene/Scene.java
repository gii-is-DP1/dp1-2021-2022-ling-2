package org.springframework.samples.ntfh.scene;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.enumerates.SceneType;
import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;

@Getter
@Entity
@Table(name = "scenes")
public class Scene extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    private SceneType type;
}
