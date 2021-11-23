package org.springframework.samples.ntfh.user;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.dao.DataAccessException;

@JsonComponent
public class UserDeserializer extends JsonDeserializer<User> {

    @Autowired
    private UserService userService;

    /**
     * @param deserializationContext containing either the user id or the username
     */
    @Override
    public User deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
            throws IOException, DataAccessException {
        try {
            // Given a JSON string, parse it into a User object
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            String username = null;
            if (node.getNodeType().equals(JsonNodeType.STRING)) {
                // If we are trying to parse a String (username) to User:
                username = node.asText();
            } else {
                // If we are trying to parse an incomplete User object:
                username = node.get("username").asText();
            }
            Optional<User> optionalUserFromUsername = userService.findUser(username);
            if (!optionalUserFromUsername.isPresent())
                // If the user is not found either by id or by username:
                throw new DataAccessException("User not found") {
                };

            return optionalUserFromUsername.get();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
