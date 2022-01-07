package org.springframework.ntfh.entity.audit;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import lombok.Setter;

@Entity
@Setter
@RevisionEntity(UserRevisionListener.class)
public class UserRevEntity extends DefaultRevisionEntity {
    @Column(name = "user")
    private String username;
}
