package org.springframework.ntfh.entity.achievement;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
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

    public Integer count() {
        return (int) achievementRepository.count();
    }

    public List<Achievement> findAll() {
        return achievementRepository.findAll();
    }

    public Iterable<Achievement> findPageable(Pageable pageable) {
        return achievementRepository.findPage(pageable).getContent();
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

        Optional<Achievement> achievementInDBOptional = achievementRepository.findById(achievement.getId());
        if (!achievementInDBOptional.isPresent()) {
            throw new IllegalArgumentException("Achievement not found in the system");
        }

        Achievement achievementInDB = achievementInDBOptional.get();
        if (!achievement.getVersion().equals(achievementInDB.getVersion())) {
            throw new OptimisticLockingFailureException(
                    "This achievement is being modified by someone else. Please, try again later") {};
        }

        if (Boolean.FALSE.equals(TokenUtils.tokenHasAnyAuthorities(token, "admin"))) {
            throw new NonMatchingTokenException("Only admins can edit achievements");
        }

        log.info("Admin with token " + token + " has updated achievement with ID: " + achievement.getId());
        return achievementRepository.save(achievement);
    }

    // Find all the achievements earned by a user
    public List<Achievement> findByUser(User user, Pageable pageable) {
        // This has to be done this way instead of using a custom query because having an achievement or not is not
        // stored in the database, but computed dynamically.
        return findAll().stream().filter(a -> userHasAchievement(user, a)).skip(pageable.getOffset())
                .limit(pageable.getPageSize()).collect(Collectors.toList());
    }

    // Count the number of achievements earned by a user
    public Integer countByUser(User user) {
        return (int) findAll().stream().filter(a -> userHasAchievement(user, a)).count();
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
            log.error("Error while checking if user has achievement", e);
            return false;
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
