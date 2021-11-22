package org.springframework.samples.ntfh.lobby;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.model.BaseEntity;
import org.springframework.samples.ntfh.user.User;

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

    @NotNull
    private String name;

    @NotNull // default to false. Mustn't be passed in the JSON body, only set by the server
    private Boolean hasStarted;

    @NotNull
    private Boolean hasScenes;

    @NotNull
    private Boolean spectatorsAllowed;

    @NotNull
    private Integer maxPlayers;

    @OneToMany(cascade = { CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE }, mappedBy = "lobby")
    // TODO if I remove CascadeType.REMOVE then the deleteLobby() method in
    // LobbyController throws an exception. If I add CascadeType.REMOVE then the
    // deleteLobby() method in LobbyController works fine but it deletes the users
    // from the database. Find the solution for this
    private Set<User> users; // Managed by the server

    // @NotNull
    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "host", referencedColumnName = "username")
    // private User host;// The host column in the game will only have the host's
    // username
    private String host; // TODO why is the former not working? Shouldnt "host" be a FK to user table?
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
}
