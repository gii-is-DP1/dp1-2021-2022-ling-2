package org.springframework.ntfh.entity.statistic.metaStatistic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import javax.persistence.CascadeType;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.user.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Entity
@Table(name="meta_statistics")
public class MetaStatistic extends BaseEntity {
    
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user")
    private User user;

    @NotNull
    @Column(name="duration")
    private Integer duration;

    @Column(name="victory")
    private Boolean victory;

    @NotNull
    @Column(name="character")
    private CharacterTypeEnum character;

    @Column(name="died")
    private Boolean died;

    @Column(name="kills")
    @PositiveOrZero
    private Integer killCount;

    @PositiveOrZero
    @Column(name="glory")
    private Integer gloryEarned;
}
