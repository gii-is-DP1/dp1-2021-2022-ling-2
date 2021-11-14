package org.springframework.samples.ntfh.round;

import javax.persistence.Entity;

import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Round extends BaseEntity{
    
    private Integer GameId;
    private Integer RoundNumber;
}
