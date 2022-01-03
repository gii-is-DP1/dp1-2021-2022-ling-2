package org.springframework.ntfh.user;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserRepository;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author alegestor
 */

public class UserControllerTestWtutorialspoint extends AbstractTestWtutorialspoint {
    
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void testGetAll() throws Exception{
        String uri = "/users/";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        User[] users = super.mapFromJson(content, User[].class);
        assertEquals(10, users.length);
    }

    @Test
    void testGetUser() throws Exception {
        String uri = "/users/alejandro";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(true, content.contains("alejandro"));
    }

    @Disabled
    @Test
    void testUpdateUser() throws Exception {
        String uri = "/users/alejandro";
        User alejandro2 = new User();
        alejandro2.setUsername("alejandro");
        alejandro2.setPassword("alejandro2");
        alejandro2.setEmail("alejandro@mail.com");
        String input = super.mapToJson(alejandro2);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(true, content.contains("alejandro2"));
    }

}
