package org.springframework.samples.ntfh.user.admin;

import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.samples.ntfh.model.BaseEntity;
import org.springframework.samples.ntfh.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admins")
public class Admin extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private User user; // User who is this admin

}
