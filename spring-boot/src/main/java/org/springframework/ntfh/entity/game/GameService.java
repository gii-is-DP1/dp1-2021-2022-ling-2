package org.springframework.ntfh.entity.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.enemy.hordeenemy.ingame.HordeEnemyIngame;
import org.springframework.ntfh.entity.enemy.hordeenemy.ingame.HordeEnemyIngameService;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.player.PlayerService;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private TurnService turnService;

    @Autowired
    private AbilityCardIngameService abilityCardIngameService;

    @Autowired
    private HordeEnemyIngameService hordeEnemyIngameService;

    @Transactional
    public Integer gameCount() {
        return (int) gameRepository.count();
    }

    public Iterable<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game findGameById(int id) throws DataAccessException {
        Optional<Game> game = gameRepository.findById(id);
        if (!game.isPresent())
            throw new DataAccessException("Game with id " + id + " was not found") {
            };
        return game.get();
    }

    public List<Player> findPlayersByGameId(int id) throws DataAccessException {
        return gameRepository.getPlayersByGameId(id);
    }

    public Turn getCurrentTurnByGameId(Integer gameId) {
        return gameRepository.getCurrentTurnByGameId(gameId);
    }

    @Transactional
    public Game createFromLobby(@Valid Lobby lobby) {
        if (lobby.getUsers().size() < 2) {
            throw new IllegalArgumentException("A game must have at least 2 players");
        }
        Game game = new Game();
        game.setStartTime(System.currentTimeMillis());
        game.setHasScenes(lobby.getHasScenes());

        Set<User> users = lobby.getUsers();

        Integer i = 1;
        List<Player> players = new ArrayList<>();
        for (User user : users) {
            // The turnOrder 0 is reserved for the host
            Boolean isHost = lobby.getHost().getUsername().equals(user.getUsername());
            Integer turnOrder = isHost ? 0 : i;
            if (!isHost)
                i++;
            Player createdPlayer = playerService.createFromUser(user, lobby, turnOrder);
            players.add(createdPlayer);
            // TODO temporary solution. Set the lobby host as the leader. In the real game
            // it is chosen via a "minigame" with cards
            if (isHost)
                game.setLeader(createdPlayer);
        }

        game.setPlayers(players);
        Game savedGame = gameRepository.save(game);
        // Now, we instantiate the entities that will be used in the game

        turnService.initializeFromGame(game);

        // Once the game is in the database, we update the lobby with a FK to it
        lobby.setGame(game);
        lobbyService.save(lobby);
        return savedGame;
    }

    @Transactional
    public Game save(@Valid Game game) {
        // Return the game created after saving it
        return gameRepository.save(game);
    }

    public void delete(Game game) {
        gameRepository.delete(game);
    }

    @Transactional
    public void playCard(Integer abilityCardIngameId, Integer enemyId) {
        AbilityCardIngame abilityCardIngame = abilityCardIngameService.findById(abilityCardIngameId);
        AbilityCardTypeEnum abilityCardTypeEnum = abilityCardIngame.getAbilityCard().getAbilityCardTypeEnum();
        Player playerFrom = abilityCardIngame.getPlayer();
        String characterType = abilityCardIngame.getAbilityCard().getCharacterTypeEnum().toString().toLowerCase();

        // Convert the enum to the appropiate PascalCase class name (DAGA_ELFICA ->
        // DagaElfica)
        String className = CaseUtils.toCamelCase(abilityCardTypeEnum.toString(), true,
                new char[] { '_' });
        String completeClassName = String.format("org.springframework.ntfh.cardlogic.abilitycard.%s.%s",
                characterType,
                className);

        try {
            // Get the class from its name
            Class<?> clazz = Class.forName(completeClassName);
            // Instantiate an object of the class
            Object cardCommand = clazz.getDeclaredConstructor().newInstance();
            // Autowire the new object's dependencies (services used inside)
            AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
            factory.autowireBean(cardCommand);
            factory.initializeBean(cardCommand, className);
            if (enemyId == null) {
                // Handle self playable card (does not target a specific enemy)
                // Get a reference to the method "execute", that receives 2 parameters
                Method method = clazz.getDeclaredMethod("execute", Player.class);
                // Execute the method with the parameters
                method.invoke(cardCommand, playerFrom);
            } else {
                // Handle card that targets an enemy
                HordeEnemyIngame targetedEnemy = hordeEnemyIngameService.findById(abilityCardIngameId);
                Method method = clazz.getDeclaredMethod("execute", Player.class, HordeEnemyIngame.class);
                method.invoke(cardCommand, playerFrom, targetedEnemy);

            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("Ability card type " + className +
                    " is not implemented");
        }
        // After playing any card, make sure to move the card to the discard pile
        Player player = abilityCardIngame.getPlayer();
        player.getHand().remove(abilityCardIngame);
        player.getDiscardPile().add(abilityCardIngame);

        // Check if the card is exiliable and if so, remove it from the discard pile too

    }
}
