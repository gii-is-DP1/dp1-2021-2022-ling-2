package org.springframework.samples.ntfh.user.unregistered;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jstockwell
 */
@Getter
@Setter
@Entity
@Table(name = "unregisteredUsers")
public class UnregisteredUser {

    @Id
    @NotBlank
    private String username;

    /**
     * Let this timestamp be a simple token for authentication.
     */
    @NotNull
    @JsonProperty("token") // the JSON will call this field "token"
    private Long creationTime;
}