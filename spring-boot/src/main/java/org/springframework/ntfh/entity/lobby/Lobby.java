package org.springframework.ntfh.entity.lobby;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.Transient;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.NamedEntity;
import org.springframework.ntfh.entity.user.User;

import lombok.Getter;
import lombok.Setter;

/**
 * This table represents the lobby that is created by the user waiting for people to join to start a game
 * 
 * @author andrsdt
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "lobbies")
public class Lobby extends NamedEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game")
    @JsonIgnoreProperties(value = {"players", "leader", "currentTurn"})
    private Game game;

    @NotNull(message = "The scenes must be either on or off")
    private Boolean hasScenes;

    @NotNull(message = "The spectators must be either allowed or not allowed")
    private Boolean spectatorsAllowed;

    @NotNull(message = "The number of max players can not be null")
    private Integer maxPlayers;

    // TODO if I remove CascadeType.REMOVE then the deleteLobby() method in
    // LobbyController throws an exception. If I add CascadeType.REMOVE then the
    // deleteLobby() method in LobbyController works fine but it deletes the users
    // from the database. Find the solution for this
    // I want that, whe I delete a lobby, the user rows that were referencing that
    // lobby change their value to NULL in the lobby_id column
    @OneToMany
    private Set<User> users; // Managed by the server

    @ManyToOne()
    @JoinColumn(name = "leader", referencedColumnName = "username")
    private User leader; // leader of the game. Currently being chosen by the host

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "host", referencedColumnName = "username")
    private User host;

    /**
     * Add a user to the set of users in the lobby
     * 
     * @author andrsdt
     * @param User user object to add
     */
    public boolean addUser(User user) {
        return this.users.add(user);
    }

    /**
     * Remove a user from the set of users in the lobby
     * 
     * @author andrsdt
     * @param User user object to remove
     */
    public boolean removeUser(User user) {
        return this.users.remove(user);
    }

    @Transient
    @JsonIgnore
    public boolean getHasStarted() {
        return this.game != null;
    }
}
