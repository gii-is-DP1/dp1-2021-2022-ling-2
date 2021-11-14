package org.springframework.samples.ntfh.scene;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.ntfh.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "scenes")
public class Scene extends NamedEntity {
    private String modifier;
}
