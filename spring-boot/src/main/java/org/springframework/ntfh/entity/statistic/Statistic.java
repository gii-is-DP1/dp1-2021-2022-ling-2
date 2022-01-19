package org.springframework.ntfh.entity.statistic;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.user.User;

import ch.qos.logback.core.util.Duration;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Statistic extends BaseEntity {
    
    // @Column(name="matches")
    // private Integer matches;
    
    // @Column(name="wins")
    // private Integer wins;

    @Column(name="deaths")
    private Integer deaths;

    @Column(name="playedCharacters")
    private Map<Character,Integer> playedCharacters;

    @Column(name="maxDuration")
    private Duration maxDuration;

    @NotNull
    @OneToOne
    @JoinColumn(name="user")
    private User user;



}
