package org.springframework.ntfh.entity.scene;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.ntfh.entity.model.BaseEntity;

import lombok.Getter;

@Getter
@Entity
@Table(name = "scenes")
public class Scene extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    private SceneTypeEnum sceneTypeEnum;
}
