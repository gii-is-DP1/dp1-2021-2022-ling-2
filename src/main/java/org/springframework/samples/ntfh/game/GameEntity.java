package org.springframework.samples.ntfh.game;

import java.security.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.Transient;
import org.springframework.samples.ntfh.comments.Comment;
import org.springframework.samples.ntfh.model.NamedEntity;
import org.springframework.samples.ntfh.scene.SceneType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GameEntity extends NamedEntity {

    private Timestamp startTime;
    private Timestamp finishTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private Set<Comment> comments;
    // Lista de usuarios en el juego [Mirar!!!!!!!]
    // @OneToMany(cascade = CascadeType.ALL,mappedBy = "game")
    // private Set<User> players;

    private Boolean spectatorsAllowed;

    private Boolean scenariosAllowed;

    // TODO tiene que ser null si !scenariosallowed
    // Esto hace referencia a una tabla donde estan todos los escenarios
    private SceneType currentScene;

    @Transient // Indica que es un getter, no queremos que se guarde en la tabla
    public Long getDuration() {
        return finishTime.getTimestamp().getTime() - startTime.getTimestamp().getTime();
    }

    @Transient
    public boolean isFinished() {
        return finishTime != null;
    }
}
