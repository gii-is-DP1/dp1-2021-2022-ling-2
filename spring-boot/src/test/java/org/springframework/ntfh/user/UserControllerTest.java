package org.springframework.ntfh.user;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserController;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @see https://stackabuse.com/guide-to-unit-testing-spring-boot-rest-apis/
 * @see https://www.tutorialspoint.com/spring_boot/spring_boot_rest_controller_unit_test.htm
 * @see https://dimitr.im/testing-your-rest-controllers-and-clients-with-spring
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserService userService;

	@MockBean
	CharacterService characterService;

	@MockBean
	DataSource dataSource;

	@BeforeEach
	void setup() {
		User user1 = new User();
		user1.setUsername("user1");
		user1.setPassword("user1");
		user1.setEmail("user1@mail.com");
		user1.setEnabled(true);

		User user2 = new User();
		user2.setUsername("user2");
		user2.setPassword("user2");
		user2.setEmail("user2@mail.com");
		user2.setEnabled(true);

		User user3 = new User();
		user3.setUsername("user3");
		user3.setPassword("user3");
		user3.setEmail("user3@mail.com");
		user3.setEnabled(true);

		User user4 = new User();
		user4.setUsername("user4");
		user4.setPassword("user4");
		user4.setEmail("user4");
		user4.setEnabled(true);

		Pageable page = PageRequest.of(1, 2);
		when(userService.findPage(page)).thenReturn(List.of(user3, user4));
		when(userService.findUser("user1")).thenReturn(user1);
		when(userService.loginUser(any(User.class))).thenReturn(TokenUtils.USER_TOKEN);

	}

	@Test
	@WithMockUser(username = "admin", password = "admin", roles = "admin")
	void findPage_success() throws Exception {
		// Mock the userService.findPage() method
		mockMvc.perform(get("/users").param("page", "1").param("size", "2").header("authorization",
				"Bearer " + TokenUtils.ADMIN_TOKEN)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].username", is("user3"))).andExpect(jsonPath("$[1].username", is("user4")));
	}

	@Test
	@WithMockUser("user")
	void findByUsername_success() throws Exception {
		// Mock the userService.findByUsername() method

		final String USERNAME = "user1";

		mockMvc.perform(get("/users/" + USERNAME)).andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("user1"))).andExpect(jsonPath("$.email", is("user1@mail.com")));
	}

	@Test
	@WithMockUser(username = "admin", password = "admin", roles = "admin")
	void update_by_admin_success() throws Exception {
		final String PUT_JSON = "{\"username\":\"admin\",\"email\":\"newMailAdmin@mail.com\",\"password\":\"admin\"}";
		mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TokenUtils.ADMIN_TOKEN).content(PUT_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser("testUser")
	void register_success() throws Exception {
		final String POST_JSON =
				"{\"username\":\"testUser\",\"email\":\"testUser@mail.com\",\"password\":\"testUser\"}";
		mockMvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON).content(POST_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	void login_success() throws Exception {
		final String POST_JSON = "{\"username\":\"user1\",\"password\":\"user1\"}";
		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN).content(POST_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.authorization", is(TokenUtils.USER_TOKEN)));
	}

	@Test
	@WithMockUser("user1")
	void testSetCharacter_Success() throws Exception {
		final String PUT_JSON =
				"{\"username\":\"user1\",\"email\":\"user1@mail.com\",\"enabled\":true,\"character\":{\"id\":5,\"baseHealth\":3,\"characterTypeEnum\":\"WARRIOR\",\"characterGenderEnum\":\"MALE\",\"proficiencies\":[{\"id\":2,\"proficiencyTypeEnum\":\"MELEE\",\"secondaryDebuff\":0}]},\"authorities\":[{\"id\":15,\"authority\":\"user\"}]}";
		mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
				.header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN).content(PUT_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser("user1")
	void testToggleBanUser_Success() throws Exception {
		final String PUT_JSON =
				"{\"username\":\"user1\",\"email\":\"user1@mail.com\",\"password\":\"user1\",\"enabled\":false}";
		mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
				.header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN).content(PUT_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", password = "admin", roles = "admin")
	void testDeleteUser() throws Exception {
		User user5 = new User();
		user5.setUsername("user5");
		user5.setPassword("user5");
		user5.setEmail("user5");
		user5.setEnabled(true);

		String DeletedUsername = "user5";
		mockMvc.perform(delete("/users/" + DeletedUsername).header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN))
				.andExpect(status().isOk());
	}

}
