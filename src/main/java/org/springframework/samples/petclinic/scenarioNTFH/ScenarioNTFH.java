package org.springframework.samples.petclinic.scenarioNTFH;

import javax.persistence.Entity;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Data;

@Data
@Entity
public class ScenarioNTFH extends NamedEntity{
    
    private String description;

}
