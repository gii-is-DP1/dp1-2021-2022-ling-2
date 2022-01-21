package org.springframework.ntfh.entity.user.authorities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
@Table(name = "authorities")
public class Authorities extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Size(min = 3, max = 50)
    String authority;
}
