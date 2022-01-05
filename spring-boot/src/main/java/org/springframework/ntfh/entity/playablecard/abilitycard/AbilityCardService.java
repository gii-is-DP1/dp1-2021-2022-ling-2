package org.springframework.ntfh.entity.playablecard.abilitycard;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.stereotype.Service;

@Service
public class AbilityCardService {

    @Autowired
    private AbilityCardRepository abilityCardRepository;

    public Integer count() {
        return (int) abilityCardRepository.count();
    }

    public AbilityCard findById(int id) throws DataAccessException {
        Optional<AbilityCard> abilityCard = abilityCardRepository.findById(id);
        if(!abilityCard.isPresent()) throw new DataAccessException("AbilityCard with id " + id + " was not found") {
        };
    return abilityCard.get();
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
