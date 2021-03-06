package org.springframework.ntfh.entity.user;

import java.util.Set;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.hibernate.validator.constraints.Length;
import org.springframework.ntfh.entity.user.authorities.Authorities;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.model.BaseEntity;

import org.hibernate.envers.Audited;

import lombok.Getter;
import lombok.Setter;

/**
 * @author andrsdt
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "users")
public class User extends BaseEntity {
    @Column(unique = true)
    @NotBlank(message = "Username is required")
    @Length(min = 4, max = 20, message = "Your username must be 4-20 characters long")
    private String username;

    @Length(min = 4, message = "Your password must be at least 4 characters long")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Column(unique = true)
    @NotBlank(message = "The email must not be empty")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotNull
    @Column(columnDefinition = "boolean default true")
    private Boolean enabled; // If a user gets banned, he/she will get disabled

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Player> players = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnoreProperties({"user"})
    private Set<Authorities> authorities;

    @Transient
    @JsonIgnore
    public Boolean hasAnyAuthorities(String commaSeparatedAuthorities) {
        List<String> parameterAuthorities =
                Stream.of(commaSeparatedAuthorities.split(",")).map(String::trim).collect(Collectors.toList());
        Stream<String> userAuthorities = this.getAuthorities().stream().map(Authorities::getAuthority);
        return userAuthorities.anyMatch(parameterAuthorities::contains); // At least contains 1 of them
    }

    @Transient
    public Player getPlayer() { // Current player the user is handling, or null if not in a game
        if (getPlayers().isEmpty())
            return null;
        Player lastPlayerInList = getPlayers().get(getPlayers().size() - 1);
        return Boolean.TRUE.equals(lastPlayerInList.getGame().getHasFinished()) ? null : lastPlayerInList;
    }
}
