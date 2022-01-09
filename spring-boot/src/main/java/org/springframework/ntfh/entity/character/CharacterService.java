package org.springframework.ntfh.entity.character;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public Optional<Character> findCharacterById(Integer id) {
        return characterRepository.findById(id);
    }
}
