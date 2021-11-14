package org.springframework.samples.ntfh.web;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

	@GetMapping({ "/", "/welcome" })
	public String welcome(Map<String, Object> model) {
		model.put("title", "LIng-2");
		model.put("group", "Students");

		return "welcome";
	}
}
