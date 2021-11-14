package org.springframework.samples.ntfh.card;

import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Integer> {
    
}
