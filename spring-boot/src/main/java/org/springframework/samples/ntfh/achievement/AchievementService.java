package org.springframework.samples.ntfh.achievement;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.samples.ntfh.util.TokenUtils;
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

    @Transactional
    public Achievement updateAchievement(Achievement achievement, String token) throws DataAccessException,
            DataIntegrityViolationException, NonMatchingTokenException, IllegalArgumentException {
        
        if (achievement.getName().isEmpty()) {
            throw new IllegalArgumentException("The name cannot be empty.");
        }

        if (achievement.getDescription().isEmpty()) {
            throw new IllegalArgumentException("The description cannot be empty.");
        }

        if(!TokenUtils.tokenHasAnyAuthorities(token, "admin")) {
            throw new NonMatchingTokenException("Only admins can edit achievements.");
        }

        return achievementRepository.save(achievement);
    }

}
