package org.springframework.ntfh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication()
public class NtfhApplication {

	public static void main(String[] args) {
		SpringApplication.run(NtfhApplication.class, args);
		log.info("NTFH Application started");
	}

}
