package org.springframework.samples.petclinic.userNTFH;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.user.AuthoritiesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserNTFHController {
    
    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "usersNTFH/createOrUpdateOwnerForm";

    private final UserNTFHService userNTFHService;

    @Autowired
	public UserNTFHController(UserNTFHService userNTFHService, AuthoritiesService authoritiesService) {
		this.userNTFHService = userNTFHService;
	}

    @InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

    @GetMapping(value = "/usersNTFH/new")
	public String initCreationForm(Map<String, Object> model) {
		UserNTFH userNTFH = new UserNTFH();
		model.put("userNTFH", userNTFH);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

    @PostMapping(value = "/usersNTFH/new")
	public String processCreationForm(@Valid UserNTFH userNTFH, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		else {
			//creating owner, user and authorities
			this.userNTFHService.saveUserNTFH(userNTFH);;
			
			return "redirect:/usersNTFH/" + userNTFH.getUsername();
		}
	}

    @GetMapping(value = "/usersNTFH/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("userNTFH", new UserNTFH());
		return "usersNTFH/findUsersNTFH";
	}

    @GetMapping(value = "/usersNTFH")
	public String processFindForm(UserNTFH userNTFH, BindingResult result, Map<String, Object> model) {

		// allow parameterless GET request for /usersNTFH to return all records
		if (userNTFH.getUsername() == null) {
			userNTFH.setUsername(""); // empty string signifies broadest possible search
		}

		// find usersNTFH by username
		Collection<UserNTFH> results = this.userNTFHService.findUserNTFHByLastName(userNTFH.getUsername());
		if (results.isEmpty()) {
			// no usersNTFH found
			result.rejectValue("username", "notFound", "not found");
			return "usersNTFH/findUsersNTFH";
		}
		else if (results.size() == 1) {
			// 1 userNTFH found
			userNTFH = results.iterator().next();
			return "redirect:/usersNTFH/" + userNTFH.getId();
		}
		else {
			// multiple usersNTFH found
			model.put("selections", results);
			return "usersNTFH/usersNTFHList";
		}
	}

    @GetMapping(value = "/usersNTFH/{userNTFHUsername}/edit")
	public String initUpdateUserNTFHForm(@PathVariable("userNTFHUsername") String userNTFHUsername, Model model) {
		UserNTFH userNTFH = this.userNTFHService.findUserNTFHByUsername(userNTFHUsername).stream().collect(Collectors.toList()).get(0);
		model.addAttribute(userNTFH);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

    @PostMapping(value = "/usersNTFH/{userNTFHUsername}/edit")
	public String processUpdateUserNTFHForm(@Valid UserNTFH userNTFH, BindingResult result,
			@PathVariable("userNTFHUsername") String userNTFHUsername) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		else {
            userNTFH.setUsername(userNTFHUsername);
            this.userNTFHService.saveUserNTFH(userNTFH);
			return "redirect:/usersNTFH/{userNTFHUsername}";
		}
	}



    @GetMapping("/usersNTFH/{userNTFHUsername}")
	public ModelAndView showUserNTFH(@PathVariable("userNTFHUsername") String userNTFHUsername) {
		ModelAndView mav = new ModelAndView("usersNTFH/userNTFHDetails");
		mav.addObject(this.userNTFHService.findUserNTFHByUsername(userNTFHUsername));
		return mav;
	}

}
