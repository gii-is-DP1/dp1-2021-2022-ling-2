package org.springframework.ntfh.entity.playablecard.abilitycard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.stereotype.Service;

@Service
public class AbilityCardService {

    @Autowired
    private AbilityCardRepository abilityCardRepository;

    public Integer count() {
        return (int) abilityCardRepository.count();
    }

    public Iterable<AbilityCard> findAll() {
        return abilityCardRepository.findAll();
    }

    public Iterable<AbilityCard> findByCharacterTypeEnum(CharacterTypeEnum characterTypeEnum) {
        return abilityCardRepository.findByCharacterTypeEnum(characterTypeEnum);
    }

    public AbilityCard findByAbilityCardTypeEnum(AbilityCardTypeEnum abilityCardTypeEnum) {
        return abilityCardRepository.findByAbilityCardTypeEnum(abilityCardTypeEnum);
    }

}
