package org.springframework.ntfh.entity.character;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public Character findById(Integer id) throws DataAccessException {
        Optional<Character> character = characterRepository.findById(id);
        if (!character.isPresent())
            throw new DataAccessException("Character with id " + id + " was not found") {};
        return character.get();
    }

}
