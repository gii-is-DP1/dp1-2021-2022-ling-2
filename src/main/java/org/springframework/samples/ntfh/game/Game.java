package org.springframework.samples.ntfh.game;

import java.security.Timestamp;
import java.util.List;

import javax.persistence.Entity;

import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Game extends BaseEntity {

    // Note: we should consider dd/MM/yyyy HH:mm:ss format since a game can start
    // today and end tomorrow
    // @DateTimeFormat(pattern = "HH/mm/ss")
    private Timestamp startTime;

    // Note: we should consider dd/MM/yyyy HH:mm:ss format since a game can start
    // today and end tomorrow
    // @DateTimeFormat(pattern = "HH/mm/ss")
    private Timestamp finishTime;

    private List<String> comments; // The list of comments would be another table. To be implemented yet.

    private Boolean spectatorsAllowed;

    /**
     * Derived. Returns the duration of the game in seconds
     * 
     * @author andrsdt
     * @return Long duration of the time in seconds
     */
    public Long getDuration() {
        return finishTime.getTimestamp().getTime() - startTime.getTimestamp().getTime();

    }
}
