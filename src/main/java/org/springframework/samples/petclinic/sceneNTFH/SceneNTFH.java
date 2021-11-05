package org.springframework.samples.petclinic.sceneNTFH;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "scenes")
public class SceneNTFH extends NamedEntity {

    private String modifier;
}
