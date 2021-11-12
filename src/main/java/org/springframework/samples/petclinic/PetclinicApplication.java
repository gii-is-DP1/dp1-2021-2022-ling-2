package org.springframework.samples.petclinic;

import org.springframework.boot.SpringApplication;

// There must be a single @SpringBootApplication annotation, which is being set in NtfhApplication.java
// @SpringBootApplication()
public class PetclinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetclinicApplication.class, args);
	}

}
