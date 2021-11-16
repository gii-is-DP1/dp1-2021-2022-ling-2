package org.springframework.samples.ntfh.user.authorities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.samples.ntfh.model.BaseEntity;
import org.springframework.samples.ntfh.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Authorities extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "username")
    User user;

    @Size(min = 3, max = 50)
    String authority;
}
