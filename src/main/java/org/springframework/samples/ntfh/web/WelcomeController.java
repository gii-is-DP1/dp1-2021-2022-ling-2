package org.springframework.samples.ntfh.web;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.springframework.samples.ntfh.model.Person;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

	@GetMapping({ "/", "/welcome" })
	public String welcome(Map<String, Object> model) {

		List<Person> people = new ArrayList<Person>();
		Person jaime = new Person();
		jaime.setFirstName("Jaime");
		jaime.setLastName("Stockwell Mendoza");
		people.add(jaime);

		Person pabloS = new Person();
		pabloS.setFirstName("Pablo Aurelio");
		pabloS.setLastName("Sanchez Valenzuela");
		people.add(pabloS);

		Person andres = new Person();
		andres.setFirstName("Andres");
		andres.setLastName("Duran Terrero");
		people.add(andres);

		Person pabloR = new Person();
		pabloR.setFirstName("Pablo Jose");
		pabloR.setLastName("Robledo Campa");
		people.add(pabloR);

		Person alex = new Person();
		alex.setFirstName("Alejandro Manuel");
		alex.setLastName("Gestoso Torres");
		people.add(alex);

		Person pabloN = new Person();
		pabloN.setFirstName("Pablo");
		pabloN.setLastName("Nuñez Moreno");
		people.add(pabloN);

		model.put("people", people);
		model.put("title", "LIng-2");
		model.put("group", "Students");

		return "welcome";
	}
}
