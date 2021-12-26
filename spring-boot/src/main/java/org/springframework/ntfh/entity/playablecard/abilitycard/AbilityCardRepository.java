package org.springframework.ntfh.entity.playablecard.abilitycard;

import org.springframework.data.repository.CrudRepository;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;

public interface AbilityCardRepository extends CrudRepository<AbilityCard, Integer> {
    Iterable<AbilityCard> findByCharacterTypeEnum(CharacterTypeEnum characterTypeEnum);
}
