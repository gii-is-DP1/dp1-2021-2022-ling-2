package org.springframework.ntfh.achievement;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {
    @Autowired
    private AchievementRepository achievementRepository;

    @Transactional
    public Integer achievementCount() {
        return (int) achievementRepository.count();
    }

    @Transactional
    public Iterable<Achievement> findAll() {
        return achievementRepository.findAll();
    }

    @Transactional
    public Optional<Achievement> findAchievementById(Integer id) {
        return achievementRepository.findById(id);
    }

    public Achievement updateAchievement(Achievement achievement, String token) throws DataAccessException,
            DataIntegrityViolationException, NonMatchingTokenException, IllegalArgumentException {

        // If we are sending a petition with a non-existing id, we throw an exception.
        // We always have to send an id because we are always editing existing
        // achievements.
        Optional<Achievement> sameIdOptional = achievementRepository.findById(achievement.getId());
        if (!sameIdOptional.isPresent()) {
            throw new IllegalArgumentException("Achievement not found in the system.");
        }

        Optional<Achievement> sameNameOptional = achievementRepository.findOptionalByName(achievement.getName());
        if (sameNameOptional.isPresent() && !(sameNameOptional.get().getId().equals(achievement.getId()))) {
            throw new IllegalArgumentException("There is already an achievement with the same name.");
        }

        if (achievement.getName().isEmpty()) {
            throw new IllegalArgumentException("The name cannot be empty.");
        }

        if (achievement.getDescription().isEmpty()) {
            throw new IllegalArgumentException("The description cannot be empty.");
        }

        if (!TokenUtils.tokenHasAnyAuthorities(token, "admin")) {
            throw new NonMatchingTokenException("Only admins can edit achievements.");
        }

        achievement.setType(sameIdOptional.get().getType());
        return achievementRepository.save(achievement);
    }

}
