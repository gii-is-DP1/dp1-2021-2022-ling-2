package org.springframework.samples.petclinic.sceneNTFH;

import javax.persistence.Entity;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SceneNTFH extends NamedEntity {

    private String name;
    private String modifier;
}
