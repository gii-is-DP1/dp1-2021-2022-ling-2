package org.springframework.samples.ntfh.user.unregistered;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jstockwell
 */
@Getter
@Setter
@Entity
@Table(name = "unregisteredUsers")
public class UnregisteredUser extends BaseEntity {

    @NotBlank
    private String username;
    @NotBlank
    private LocalTime creationTime;
}