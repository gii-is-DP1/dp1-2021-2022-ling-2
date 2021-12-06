package org.springframework.ntfh.character;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    @Transactional
    public Optional<Character> findCharacterById(Integer id) {
        return characterRepository.findById(id);
    }
}
