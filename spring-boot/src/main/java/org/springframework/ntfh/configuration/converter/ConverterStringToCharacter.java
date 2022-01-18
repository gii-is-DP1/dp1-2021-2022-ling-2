package org.springframework.ntfh.configuration.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.stereotype.Component;

@Component
public class ConverterStringToCharacter implements Converter<Integer, Character>{

    @Autowired
    private CharacterService characterService;

    @Override
    public Character convert(Integer characterId) {
        return characterService.findById(characterId);

    }    
}
