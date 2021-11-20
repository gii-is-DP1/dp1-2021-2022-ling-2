package org.springframework.samples.ntfh.game;

import java.security.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Transient;
import org.springframework.samples.ntfh.comments.Comment;
import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pabrobcam
 * @author andrsdt
 */
@Getter
@Setter
@Entity
@Table(name = "games")
public class Game extends BaseEntity {

    // Set by the server
    private Timestamp startTime;

    // Set by the server
    private Timestamp finishTime;

    @NotNull
    private Boolean spectatorsAllowed;

    @NotNull
    private Integer maxPlayers;

    // TODO hacer la asociacion tambien desde parte de comments? bidireccional?
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    @JsonIgnore
    private Set<Comment> comments;

    @Transient
    private Boolean isStarted;

    @Transient
    private Boolean isFinished;

    @Transient
    private Long duration;

    /**
     * @author andrsdt
     */
    public Boolean isStarted() {
        return startTime != null;
    }

    /**
     * @author andrsdt
     */
    public Boolean isFinished() {
        return finishTime != null;
    }

    /**
     * Derived. Returns the duration of the game in seconds
     * 
     * @author andrsdt
     * @return Long duration of the time in seconds
     */
    public Long getDuration() {
        Long timeInMilliseconds = finishTime.getTimestamp().getTime() - startTime.getTimestamp().getTime();
        return timeInMilliseconds / 1000;
    }
}
