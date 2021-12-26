package org.springframework.ntfh.entity.playablecard.abilitycard;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.stereotype.Service;

@Service
public class AbilityCardService {

    @Autowired
    private AbilityCardRepository abilityCardRepository;

    @Transactional
    public Integer count() {
        return (int) abilityCardRepository.count();
    }

    @Transactional
    public Iterable<AbilityCard> findAll() {
        return abilityCardRepository.findAll();
    }

    @Transactional
    public Iterable<AbilityCard> findByCharacterTypeEnum(CharacterTypeEnum characterTypeEnum) {
        return abilityCardRepository.findByCharacterTypeEnum(characterTypeEnum);
    }

}
