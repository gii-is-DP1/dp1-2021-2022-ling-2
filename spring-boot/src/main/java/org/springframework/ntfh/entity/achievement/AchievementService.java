package org.springframework.ntfh.entity.achievement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private ApplicationContext applicationContext;

    public Integer achievementCount() {
        return (int) achievementRepository.count();
    }

    public Iterable<Achievement> findAll() {
        return achievementRepository.findAll();
    }

    public Achievement findById(Integer id) throws DataAccessException {
        Optional<Achievement> achievement = achievementRepository.findById(id);
        if (!achievement.isPresent())
            throw new DataAccessException("Achievement with id " + id + " was not found") {};
        return achievement.get();
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
        if (sameNameOptional.isPresent() && !(sameNameOptional.get().getId().equals(achievement.getId()))) {
            throw new IllegalArgumentException("There is already an achievement with the same name");
        }

        if (Boolean.FALSE.equals(TokenUtils.tokenHasAnyAuthorities(token, "admin"))) {
            throw new NonMatchingTokenException("Only admins can edit achievements");
        }

        Optional<Achievement> achievementFromRepo = achievementRepository.findById(achievement.getId());
        if (achievementFromRepo.isPresent())
            achievement.setType(achievementFromRepo.get().getType());
        log.info("Admin with token " + token + " has updated achievement with ID: " + achievement.getId());
        return achievementRepository.save(achievement);
    }

    // Find all the achievements earned by a user
    public Iterable<Achievement> findByUser(User user) {
        List<Achievement> achievements = new ArrayList<>();
        this.findAll().forEach(achievement -> {
            if (userHasAchievement(user, achievement))
                achievements.add(achievement);
        });
        return achievements;
    }

    private Boolean userHasAchievement(User user, Achievement achievement) {
        String className = CaseUtils.toCamelCase(achievement.getType().toString(), true, new char[] {'_'});
        String completeClassName =
                String.format("org.springframework.ntfh.entity.achievement.concreteachievement.%s", className);

        try {
            // Get the class from its name
            Class<?> clazz = Class.forName(completeClassName);
            // Instantiate an object of the class
            Object achievementChecker = clazz.getDeclaredConstructor().newInstance();
            // Autowire the new object's dependencies (services used inside)
            AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
            factory.autowireBean(achievementChecker);
            factory.initializeBean(achievementChecker, className);
            Method method = clazz.getDeclaredMethod("check", User.class, Integer.class);
            // Execute the method with the parameters
            Object res = method.invoke(achievementChecker, user, achievement.getCondition());
            return (Boolean) res;

        } catch (Exception e) {
            throw new IllegalArgumentException("Condition for achievement " + className + " is not implemented");
        }
    }

    public void delete(Achievement achievement) {
        achievementRepository.delete(achievement);
    }

    public List<AchievementType> findAllTypes() {
        return Arrays.asList(AchievementType.values());
    }

    public void createAchievement(Achievement achievement) {
        achievementRepository.save(achievement);
    }
}
