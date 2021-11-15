package org.springframework.samples.ntfh.player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.type.CharacterType;
import org.springframework.samples.ntfh.character.CharacterVersion;
import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "players")
public class Player extends BaseEntity {
    @NotNull
	@Column(columnDefinition = "integer default 0")
    private Integer glory;

    @NotNull
	@Column(columnDefinition = "integer default 0")
    private Integer kills;

    @NotNull
	@Column(columnDefinition = "integer default 0")
    private Integer gold;

    @NotNull
	@Column(columnDefinition = "integer default 0")
    private Integer wounds;

    @NotNull
    private Integer currentTurnOrder;

    @NotNull
	@Column(columnDefinition = "boolean default false")
    private Boolean isHost;

    @NotNull
    private CharacterVersion characterVersion;

    @NotNull
    private CharacterType characterType;
}
