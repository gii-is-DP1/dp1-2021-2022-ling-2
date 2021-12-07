package org.springframework.ntfh.character;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.dao.DataAccessException;

@JsonComponent
public class CharacterDeserializer extends JsonDeserializer<Character> {

    @Autowired
    private CharacterService characterService;

    /**
     * @param deserializationContext containing either the user id or the username
     */
    @Override
    public Character deserialize(final JsonParser jp, final DeserializationContext ctxt)
            throws IOException, DataAccessException {

        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        DeserializationConfig config = ctxt.getConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(Character.class);
        JsonDeserializer<Object> defaultDeserializer = BeanDeserializerFactory.instance.buildBeanDeserializer(ctxt,
                type, config.introspect(type));

        try {
            if (defaultDeserializer instanceof ResolvableDeserializer) {
                ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
            }

            JsonParser treeParser = oc.treeAsTokens(node);
            config.initialize(treeParser);

            if (treeParser.getCurrentToken() == null) {
                treeParser.nextToken();
            }

            // Try to use the default deserializer
            return (Character) defaultDeserializer.deserialize(treeParser, ctxt);
        } catch (Exception e) {
            // If the default deserializer fails, try to deserialize it manually:
            // If the id is present, deserialize by it
            if (node.getNodeType() == JsonNodeType.NUMBER) {
                Integer characterId = node.intValue();
                return characterService.findCharacterById(characterId).get();
            } else {
                throw new IllegalArgumentException("Cannot deserialize character");
            }
        }
    }
}
