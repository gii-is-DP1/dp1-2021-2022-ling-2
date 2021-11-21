package org.springframework.samples.ntfh.achievement;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void save(Achievement achievement) {
        achievementRepository.save(achievement);
    }

    public void delete(Integer achievementId) {
        achievementRepository.deleteById(achievementId);
    }

}
