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
import org.springframework.samples.ntfh.character.Character;
import org.springframework.samples.ntfh.character.CharacterService;

@JsonComponent
public class UserDeserializer extends JsonDeserializer<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;

    /**
     * @param deserializationContext containing either the user id or the username
     */
    @Override
    public User deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
            throws IOException, DataAccessException {
        // TODO exhaustive testing for considering edge cases
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
            if (!optionalUserFromUsername.isPresent()) {
                // If the user is not found either by id or by username, we must be creating a
                // new user:
                User user = new User();
                user.setUsername(node.get("username").asText());
                user.setEmail(node.get("email").asText());
                user.setPassword(node.get("password").asText());
                return user;
            }

            // TODO consider using the Prototype pattern for creating a user clone
            User userFromDb = optionalUserFromUsername.get();
            User user = new User();
            user.setAuthorities(userFromDb.getAuthorities());
            user.setEnabled(node.get("enabled") == null ? userFromDb.isEnabled() : node.get("enabled").asBoolean());
            user.setPassword(node.get("password") == null ? userFromDb.getPassword() : node.get("password").asText());
            user.setUsername(node.get("username") == null ? userFromDb.getUsername() : node.get("username").asText());
            user.setEmail(node.get("email") == null ? userFromDb.getEmail() : node.get("email").asText());

            // Set character property
            if (node.get("character") != null) {
                Integer characterId = null;
                if (node.get("character").getNodeType().equals(JsonNodeType.NUMBER)) {
                    characterId = node.get("character").asInt();
                } else {
                    characterId = node.get("character").get("id").asInt();
                }
                Optional<Character> characterFromIdOptional = characterService.findCharacterById(characterId);
                if (characterFromIdOptional.isPresent()) {
                    Character characterFromId = characterFromIdOptional.get();
                    user.setCharacter(characterFromId);
                }
            }
            // TODO possible point of failure. Currently, if we pass these values as
            // parameters in JSON they won't be considered and their value will be set to
            // the current value on the database. This is due to the effort in parsing
            // custom classes. In case we need this at some point, we would implement
            // parsers that creates objects from the JSON and then sets the values on the
            // object.

            user.setGame(userFromDb.getGame());
            user.setLobby(userFromDb.getLobby());

            return user;
        } catch (

        Exception e) {
            throw new IOException(e);
        }
    }

}
