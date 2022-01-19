package org.springframework.ntfh.entity.achievement;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    public Integer achievementCount() {
        return (int) achievementRepository.count();
    }

    public Iterable<Achievement> findAll() {
        return achievementRepository.findAll();
    }

    public Optional<Achievement> findAchievementById(Integer id) {
        return achievementRepository.findById(id);
    }

    @Transactional
    public Achievement updateAchievement(Achievement achievement, String token)
            throws NonMatchingTokenException, IllegalArgumentException {

        // If we are sending a petition with a non-existing id, we throw an exception.
        // We always have to send an id because we are always editing existing
        // achievements.

        if (!achievementRepository.existsById(achievement.getId())) {
            throw new IllegalArgumentException("Achievement not found in the system");
        }

        Optional<Achievement> sameNameOptional = achievementRepository.findOptionalByName(achievement.getName());
        if (sameNameOptional.isPresent() &&
                !(sameNameOptional.get().getId().equals(achievement.getId()))) {
            throw new IllegalArgumentException("There is already an achievement with the same name");
        }

        if (Boolean.FALSE.equals(TokenUtils.tokenHasAnyAuthorities(token, "admin"))) {
            throw new NonMatchingTokenException("Only admins can edit achievements");
        }

        Optional<Achievement> achievementFromRepo = achievementRepository.findById(achievement.getId());
        if(achievementFromRepo.isPresent()) achievement.setType(achievementFromRepo.get().getType());
        log.info("Admin with token " + token + " has updated achievement with ID: " + achievement.getId());
        return achievementRepository.save(achievement);
    }
}
