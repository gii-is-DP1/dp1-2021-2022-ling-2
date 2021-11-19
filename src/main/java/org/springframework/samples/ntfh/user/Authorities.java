package org.springframework.samples.ntfh.user;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

// This entity is meant to set a difference between users and admins
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