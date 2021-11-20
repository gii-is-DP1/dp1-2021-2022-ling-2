package org.springframework.samples.ntfh.scene;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;

@Getter
@Entity
@Table(name = "scenes")
public class Scene extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    private SceneTypeEnum sceneTypeEnum;

    @Transient
    private String backImage;

    @Transient
    private String frontImage;

    public String getFrontImage() {
        return null;
    }

    public String getBackImage() {
        return null;
    }
}
