package org.springframework.ntfh.entity.spectator;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.unregistered.UnregisteredUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "spectators")
public class Spectator extends BaseEntity {
    @NotNull
    @Column(columnDefinition = "integer default 0")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date entryTime;

    @OneToOne(cascade = CascadeType.ALL) // TODO not oneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private User user; // User who is this spectator

    @OneToOne(cascade = CascadeType.ALL) // TODO not oneToOne
    @JoinColumn(name = "unregistered_user_id", referencedColumnName = "username")
    private UnregisteredUser unregisteredUser; // UnregisteredUser who is this spectator

    @NotNull
    @OneToOne(cascade = CascadeType.ALL) // TODO not oneToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game; // Game this spectator watches

}
