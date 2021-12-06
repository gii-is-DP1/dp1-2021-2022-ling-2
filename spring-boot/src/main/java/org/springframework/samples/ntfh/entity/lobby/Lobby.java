package org.springframework.samples.ntfh.entity.lobby;

import java.beans.Transient;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.samples.ntfh.entity.game.Game;
import org.springframework.samples.ntfh.entity.model.BaseEntity;
import org.springframework.samples.ntfh.entity.user.User;

import lombok.Getter;
import lombok.Setter;

/**
 * This table represents the lobby that is created by the user waiting for
 * people to join to start a game
 * 
 * @author andrsdt
 */
@Getter
@Setter
@Entity
@Table(name = "lobbies")
public class Lobby extends BaseEntity {

    @NotEmpty(message = "The lobby name must not be empty")
    private String name;

    // TODO replace with game. Initially to null, eventually to the game that has
    // been created from that lobby
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game")
    @JsonIgnoreProperties(value = { "players", "leader" })
    private Game game;

    @NotNull
    private Boolean hasScenes;

    @NotNull
    private Boolean spectatorsAllowed;

    @NotNull
    private Integer maxPlayers;

    // TODO if I remove CascadeType.REMOVE then the deleteLobby() method in
    // LobbyController throws an exception. If I add CascadeType.REMOVE then the
    // deleteLobby() method in LobbyController works fine but it deletes the users
    // from the database. Find the solution for this
    // I want that, whe I delete a lobby, the user rows that were referencing that
    // lobby change their value to NULL in the lobby_id column
    @OneToMany(mappedBy = "lobby")
    private Set<User> users; // Managed by the server

    @ManyToOne()
    @JoinColumn(name = "leader", referencedColumnName = "username")
    private User leader; // leader of the game. Currently being chosen by the host

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "host", referencedColumnName = "username")
    private User host;// The host column in the game will only have the host's
    // username
    // private String host; // TODO why is the former not working? Shouldnt "host"
    // be a FK to user table?
    // See:
    // https://stackoverflow.com/questions/54523524/json-parse-error-cannot-construct-instance-of-custom-object-while-dealing-wit

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
    public boolean getHasStarted() {
        return this.game != null;
    }
}
