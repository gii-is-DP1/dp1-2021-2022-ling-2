INSERT INTO users(username,password, email) VALUES ('admin','$2a$10$bicbzJTFskk8.sHWJauxCu2RzDIqXk/zCxQDZ5ByLQw0m0lQ6l2Pa', 'admin@mail.com');
INSERT INTO authorities(id,username,authority) VALUES (1,'admin','admin');
INSERT INTO authorities(id,username,authority) VALUES (2,'admin','user');

INSERT INTO users(username,password,email) VALUES ('pablo','$2a$10$k.p0O7lwMeTbmeKbODEqbO8l6VDv38JgLI66UHydll9QhIK4ewlgq', 'pablo@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(4, 'pablo','admin');
INSERT INTO authorities(id,username,authority) VALUES(5, 'pablo','user');

INSERT INTO users(username,password,email) VALUES ('andres','$2a$10$MnbY762s/bvKrB249IRRferPU3lg70XfzPLxHBwK9Sg8EXA.OSMGO','andres@mail.com');
INSERT INTO authorities(id,username,authority) VALUES (3,'andres','user');

INSERT INTO users(username,password,email) VALUES ('stockie','$2a$10$xKDzRwhX90OQZ4EeOUOtMeTc8Sfzj4Dacsbfqh8HSeLzBT5zWgX6W', 'stockie@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(6, 'stockie','user');

INSERT INTO users(username,password,email) VALUES ('alejandro','$2a$10$wto4YGQm44mFCrxN.GoAW.0ll2zZiA.1dy6fMk06AwPy8tqKsw.ra', 'alejandro@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(7, 'alejandro','user');

INSERT INTO users(username,password,email) VALUES ('merlin','$2a$10$nY6NBJd0GmBPNLvi/JqiguR6.3UDaXINdnIP2Z3PpOAIATvW6ahj.', 'merlin@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(8, 'merlin','user');

INSERT INTO users(username,password,email) VALUES ('legolas','$2a$10$M5ZhWHiGOKrB5gpLAnPo3OTEj9SFOmxmBDyj.FuwtCH9ZdQlNNtWG', 'legolas@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(9, 'legolas','user');

INSERT INTO users(username,password,email) VALUES ('gandalf','$2a$10$mtBX7rLI1QALxQcnsUc1/OapMiBnjMmDTsRwxiXqJXpQmXZuOYR5q', 'gandalf@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(10, 'gandalf','user');

INSERT INTO users(username,password,email) VALUES ('frodo','$2a$10$PalqK8iUM3awRALpPgs1j.AnUo2uKdriZcOAXQopLRIUQVsySrDVa', 'frodo@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(11, 'frodo','user');

INSERT INTO users(username,password,email) VALUES ('dalinar','$2a$10$UkzgrcjhQFvIzpSAiev6YuGzKzKFLCk9YOD8GD/vGerFzMjRyl7De', 'dalinar@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(12, 'dalinar','user');

INSERT INTO users(username,password,email) VALUES ('aragorn','$2a$10$yDGVc5VBqkHDkEIu7xHYquXsiMNAa4iY3ioE1s5HPT2x5P2N1ftvS', 'aragorn@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(13, 'aragorn','user');

INSERT INTO users(username,password,email) VALUES ('ezio','$2a$10$wlrC8OwlLYJYg3wM.5gxmuiPhvHaK/sh2sDbHnMYehSzvnRUzauXm', 'ezio@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(14, 'ezio','user');

INSERT INTO users(username,password,email) VALUES ('user1','$2a$10$/oWmIwbGzUR5guYoznFzDefC.MMYFTwZ3bmLIiWd4akO8cac/eTX2', 'user1@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(15, 'user1','user');

INSERT INTO users(username,password,email) VALUES ('user2','$2a$10$pLlYabqhasLIEdk16qUXqeNNNnK3tszIc2ud1QQ6IshsN.WoOOQHu', 'user2@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(16, 'user2','user');

INSERT INTO users(username,password,email) VALUES ('user3','$2a$10$/Equ7DkGQXhbXtz5bO6uruA/VStGtLxhGE8aLd3Xi28mOkVqaj70y', 'user3@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(17, 'user3','user');

INSERT INTO users(username,password,email) VALUES ('user4','$2a$10$.AxtcWQeT3zpXHv4j0892uKnU5kkJTpRhFijkaJujP2bb22KcR/qC', 'user4@mail.com');
INSERT INTO authorities(id,username,authority) VALUES(18, 'user4','user');

INSERT INTO achievements(name, description, type) VALUES ('A new hand touches the beacon', 'Listen. Hear me and obey. A foul darkness has seeped into my temple. A darkness that you will destroy. Return my beacon to Mount Kilkreath. And I will make you the instrument of my cleansing light', 'CREATE_ACCOUNT');
INSERT INTO achievements(name, description, type) VALUES ('Newcomer', 'Play your first game', 'PLAY_1_GAME');
INSERT INTO achievements(name, description, type) VALUES ('Avid player', 'Win 5 games of No Time for Heroes', 'WIN_5_GAMES');

INSERT INTO proficiencies(id, proficiency_type_enum, secondary_debuff) VALUES (1, 'DEXTERITY', 0);
INSERT INTO proficiencies(id, proficiency_type_enum, secondary_debuff) VALUES (2, 'MELEE', 0);
INSERT INTO proficiencies(id, proficiency_type_enum, secondary_debuff) VALUES (3, 'MELEE', -1);
INSERT INTO proficiencies(id, proficiency_type_enum, secondary_debuff) VALUES (4, 'RANGED', 0);
INSERT INTO proficiencies(id, proficiency_type_enum, secondary_debuff) VALUES (5, 'RANGED', -1);
INSERT INTO proficiencies(id, proficiency_type_enum, secondary_debuff) VALUES (6, 'SPELL', 0);

INSERT INTO characters(id, character_type_enum, character_gender_enum) VALUES (1, 'RANGER','MALE');
INSERT INTO characters(id, character_type_enum, character_gender_enum) VALUES (2, 'RANGER','FEMALE');
INSERT INTO characters(id, character_type_enum, character_gender_enum) VALUES (3, 'ROGUE','MALE');
INSERT INTO characters(id, character_type_enum, character_gender_enum) VALUES (4, 'ROGUE','FEMALE');
INSERT INTO characters(id, character_type_enum, character_gender_enum) VALUES (5, 'WARRIOR','MALE');
INSERT INTO characters(id, character_type_enum, character_gender_enum) VALUES (6, 'WARRIOR','FEMALE');
INSERT INTO characters(id, character_type_enum, character_gender_enum) VALUES (7, 'WIZARD','MALE');
INSERT INTO characters(id, character_type_enum, character_gender_enum) VALUES (8, 'WIZARD','FEMALE');

-- Character RANGER has proficiencies melee-1, ranged
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (1, 3);
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (1, 4);
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (2, 3);
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (2, 4);

-- Character ROGUE has proficiencies dexterity, ranged-1
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (3, 1);
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (3, 5);
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (4, 1);
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (4, 5);

-- Character WIZARD has proficiency spell
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (5, 6);
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (6, 6);

-- Character WARRIOR has proficiency melee
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (7, 2);
INSERT INTO characters_proficiencies(character_id, proficiency_id) VALUES (8, 2);


INSERT INTO horde_enemy_types(horde_enemy_type_enum, horde_enemy_modifier_type_enum, endurance) VALUES ('WARRIOR', null, 4);
INSERT INTO horde_enemy_types(horde_enemy_type_enum, horde_enemy_modifier_type_enum, endurance) VALUES ('SLINGER', null, 2);
INSERT INTO horde_enemy_types(horde_enemy_type_enum, horde_enemy_modifier_type_enum, endurance) VALUES ('BERSERKER', null, 6);
INSERT INTO horde_enemy_types(horde_enemy_type_enum, horde_enemy_modifier_type_enum, endurance) VALUES ('REGEN','HEALING_CAPABILITIES', 3);
INSERT INTO horde_enemy_types(horde_enemy_type_enum, horde_enemy_modifier_type_enum, endurance) VALUES ('MAGE','MAGIC_ATTACKER', 5);
INSERT INTO horde_enemy_types(horde_enemy_type_enum, horde_enemy_modifier_type_enum, endurance) VALUES ('SHAMAN','MAGIC_ATTACKER', 3);

INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (1, 'WARRIOR', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (2, 'WARRIOR', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (3, 'WARRIOR', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (4, 'WARRIOR', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (5, 'WARRIOR', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (6, 'WARRIOR', 2, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (7, 'WARRIOR', 2, 1);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (8, 'WARRIOR', 2, 1);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (9, 'SLINGER', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (10, 'SLINGER', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (11, 'SLINGER', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (12, 'SLINGER', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (13, 'SLINGER', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (14, 'BERSERKER', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (15, 'BERSERKER', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (16, 'BERSERKER', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (17, 'REGEN', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (18, 'REGEN', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (19, 'REGEN', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (20, 'REGEN', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (21, 'REGEN', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (22, 'MAGE', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (23, 'MAGE', 2, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (24, 'MAGE', 2, 0);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (25, 'MAGE', 2, 1);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (26, 'SHAMAN', 1, 1);
INSERT INTO horde_enemies(id, horde_enemy_type_enum, gold, extra_glory) VALUES (27, 'SHAMAN', 2, 0);

-- Ejemplo de entidad hordeEnemyIngame:
-- INSERT INTO horde_enemies_ingame(id, game_id, horde_enemy_id, current_endurance, horde_enemy_location) VALUES (1, 1, 1, defaultEndurance, 'HORDE_PILE')

INSERT INTO warlords(id, warlord_type_enum, endurance) VALUES (1, 'GURDRUG', 8);
INSERT INTO warlords(id, warlord_type_enum, endurance) VALUES (2, 'ROGHKILLER', 9);
INSERT INTO warlords(id, warlord_type_enum, endurance) VALUES (3, 'SHRIEKKNIFER', 10);

-- Ejemplo de entidad warlord ingame:
-- INSERT INTO warlords_ingame(id, game_id, warlord_id, current_endurance, warlord_location) VALUES (1, 1, 1, 8, 'HORDE_PILE');

INSERT INTO scenes(id, scene_type_enum) VALUES (1, 'MERCADO_DE_LOTHARION'); 
INSERT INTO scenes(id, scene_type_enum) VALUES (2, 'CAMPO_DE_BATALLA');
INSERT INTO scenes(id, scene_type_enum) VALUES (3, 'LAGRIMAS_DE_ARADIEL');
INSERT INTO scenes(id, scene_type_enum) VALUES (4, 'LODAZAL_DE_KALERN');
INSERT INTO scenes(id, scene_type_enum) VALUES (5, 'MONTANAS_DE_UR');
INSERT INTO scenes(id, scene_type_enum) VALUES (6, 'PANTANO_UMBRIO');
INSERT INTO scenes(id, scene_type_enum) VALUES (7, 'PLANICIE_DE_SKAARG');
INSERT INTO scenes(id, scene_type_enum) VALUES (8, 'PORTAL_DE_ULTHAR');
INSERT INTO scenes(id, scene_type_enum) VALUES (9, 'PUERTO_DE_EQUE');
INSERT INTO scenes(id, scene_type_enum) VALUES (10, 'RUINAS_DE_BRUNMAR');
INSERT INTO scenes(id, scene_type_enum) VALUES (11, 'YACIMIENTOS_DE_JADE');
INSERT INTO scenes(id, scene_type_enum) VALUES (12, 'YERMO_DE_CEMENMAR');

--INSERT INTO playable_cards_ingame(id, game_id, ability_card_id, location) VALUES (1, 1, 1, 'HORDE_PILE');

-- MARKET no es una propiedad de las cartas. Es una propiedad de la instancia de la carta, cuando se encuentre ya dentro de un juego.
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (1, 3, 'DAGA_ELFICA');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (2, 3, 'DAGA_ELFICA');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (3, 8, 'POCION_CURATIVA');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (4, 8, 'POCION_CURATIVA');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (5, 8, 'POCION_CURATIVA');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (6, 4, 'PIEDRA_DE_AMOLAR');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (7, 5, 'VIAL_DE_CONJURACION');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (8, 5, 'VIAL_DE_CONJURACION');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (9, 3, 'ELIXIR_DE_CONCENTRACION');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (10, 3, 'ELIXIR_DE_CONCENTRACION');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (11, 3, 'CAPA_ELFICA');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (12, 4, 'ARMADURA_DE_PLACAS');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (13, 5, 'ALABARDA_ORCA');
INSERT INTO market_cards(id, price, market_card_type_enum) VALUES (14, 5, 'ARCO_COMPUESTO');

-- PIEDRA DE AMOLAR can be used by characters with dexterity, melee and ranged
INSERT INTO marketcards_proficiencies(market_card_id, proficiency_type_enum) VALUES (6, 'DEXTERITY');
INSERT INTO marketcards_proficiencies(market_card_id, proficiency_type_enum) VALUES (6, 'MELEE');
INSERT INTO marketcards_proficiencies(market_card_id, proficiency_type_enum) VALUES (6, 'RANGED');

-- CAPA ELFICA can be used by characters with ranged, spell
INSERT INTO marketcards_proficiencies(market_card_id, proficiency_type_enum) VALUES (11, 'RANGED');
INSERT INTO marketcards_proficiencies(market_card_id, proficiency_type_enum) VALUES (11, 'SPELL');

-- ARMADURA DE PLACAS can be used by characters with melee
INSERT INTO marketcards_proficiencies(market_card_id, proficiency_type_enum) VALUES (12, 'MELEE');

-- ALABARDA ORCA can be used by characters with melee
INSERT INTO marketcards_proficiencies(market_card_id, proficiency_type_enum) VALUES (13, 'MELEE');

-- ARCO COMPUESTO can be used by characters with ranged
INSERT INTO marketcards_proficiencies(market_card_id, proficiency_type_enum) VALUES (14, 'RANGED');

INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (1, 'COMPANERO_LOBO', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (2, 'DISPARO_CERTERO', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (3, 'DISPARO_CERTERO', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (4, 'DISPARO_RAPIDO', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (5, 'DISPARO_RAPIDO', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (6, 'DISPARO_RAPIDO', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (7, 'DISPARO_RAPIDO', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (8, 'DISPARO_RAPIDO', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (9, 'DISPARO_RAPIDO', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (10, 'EN_LA_DIANA', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (11, 'LLUVIA_DE_FLECHAS', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (12, 'LLUVIA_DE_FLECHAS', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (13, 'RECOGER_FLECHAS', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (14, 'RECOGER_FLECHAS', 'RANGER');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (15, 'SUPERVIVENCIA', 'RANGER');

INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (16, 'ATAQUE_BRUTAL', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (17, 'ATAQUE_BRUTAL', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (18, 'CARGA_CON_ESCUDO', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (19, 'DOBLE_ESPADAZO', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (20, 'DOBLE_ESPADAZO', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (21, 'ESCUDO', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (22, 'ESCUDO', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (23, 'ESPADAZO', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (24, 'ESPADAZO', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (25, 'ESPADAZO', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (26, 'ESPADAZO', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (27, 'PASO_ATRAS', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (28, 'PASO_ATRAS', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (29, 'TODO_O_NADA', 'WARRIOR');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (30, 'VOZ_DE_ALIENTO', 'WARRIOR');

INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (31, 'AURA_PROTECTORA', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (32, 'BOLA_DE_FUEGO', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (33, 'DISPARO_GELIDO', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (34, 'DISPARO_GELIDO', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (35, 'FLECHA_CORROSIVA', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (36, 'GOLPE_DE_BASTON', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (37, 'GOLPE_DE_BASTON', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (38, 'GOLPE_DE_BASTON', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (39, 'GOLPE_DE_BASTON', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (40, 'ORBE_CURATIVO', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (41, 'PROYECTIL_IGNEO', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (42, 'PROYECTIL_IGNEO', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (43, 'PROYECTIL_IGNEO', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (44, 'RECONSTITUCION', 'WIZARD');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (45, 'TORRENTE_DE_LUZ', 'WIZARD');

INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (46, 'AL_CORAZON', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (47, 'AL_CORAZON', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (48, 'ATAQUE_FURTIVO', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (49, 'ATAQUE_FURTIVO', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (50, 'ATAQUE_FURTIVO', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (51, 'BALLESTA_PRECISA', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (52, 'BALLESTA_PRECISA', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (53, 'BALLESTA_PRECISA', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (54, 'EN_LAS_SOMBRAS', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (55, 'EN_LAS_SOMBRAS', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (56, 'ENGANAR', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (57, 'ROBAR_BOLSILLOS', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (58, 'SAQUEO', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (59, 'SAQUEO', 'ROGUE');
INSERT INTO ability_cards(id, ability_card_type_enum, character_type_enum) VALUES (60, 'TRAMPA', 'ROGUE');


-- CREATE A GAME FROM A LOBBY WITH 2 PLAYERS
-- Create the initial lobby
INSERT INTO lobbies(id, name, game, has_scenes, spectators_allowed, max_players, host, leader) VALUES (1, 'andres with pablo', null, true, true, 2, 'andres', 'andres');
-- add the users to the lobby
UPDATE users SET lobby = 1 WHERE username = 'pablo';
UPDATE users SET lobby = 1 WHERE username = 'andres';
-- create the players instances and then a game
INSERT INTO players(id, glory, gold, kills, wounds, character_id, turn_order, user_id) VALUES (1, 0, 0, 0, 0, 8, 0, 'pablo');
INSERT INTO players(id, glory, gold, kills, wounds, character_id, turn_order, user_id) VALUES (2, 0, 0, 0, 0, 1, 1, 'andres');
INSERT INTO games(id, has_scenes, start_time, leader_id) VALUES (1, true, 1637867168863, 1);
-- Once the game is created, the lobby references the game
UPDATE lobbies SET game = 1 WHERE id = 1;
-- Add the players to the game
INSERT INTO games_players(game_id, players_id) VALUES (1, 1);
INSERT INTO games_players(game_id, players_id) VALUES (1, 2);
-- Also, set the game attribute in each user to reference the game they are in now
UPDATE users SET game = 1 WHERE username = 'pablo';
UPDATE users SET game = 1 WHERE username = 'andres';

-- CREATE A GAME FROM A LOBBY WITH 4 PLAYERS
-- Create the initial lobby
INSERT INTO lobbies (id, name, game, has_scenes, spectators_allowed, max_players, host) VALUES (2,'Lord of the rings', null, false, true, 4, 'gandalf');
-- add the users to the lobby
UPDATE users SET lobby = 2 WHERE username = 'gandalf';
UPDATE users SET lobby = 2 WHERE username = 'frodo';
UPDATE users SET lobby = 2 WHERE username = 'legolas';
UPDATE users SET lobby = 2 WHERE username = 'aragorn';
-- create the players instances and then a game
INSERT INTO players(id, glory, gold, kills, wounds, character_id, turn_order, user_id) VALUES (3, 0, 0, 0, 0, 1, 1, 'frodo');
INSERT INTO players(id, glory, gold, kills, wounds, character_id, turn_order, user_id) VALUES (4, 0 ,0, 0, 0 ,3, 0, 'gandalf');
INSERT INTO players(id, glory, gold, kills, wounds, character_id, turn_order, user_id) VALUES (5, 0, 0, 0, 0, 5, 2, 'legolas');
INSERT INTO players(id, glory, gold, kills, wounds, character_id, turn_order, user_id) VALUES (6, 0 ,0, 0, 0 ,7, 3, 'aragorn');
INSERT INTO games(id, has_scenes, start_time, leader_id) VALUES (2, true, 1637881111234, 3);
-- Once the game is created, the lobby references the game
UPDATE lobbies SET game = 2 WHERE id = 2;
-- Add the players to the game
INSERT INTO games_players(game_id, players_id) VALUES (2, 3);
INSERT INTO games_players(game_id, players_id) VALUES (2, 4);
INSERT INTO games_players(game_id, players_id) VALUES (2, 5);
INSERT INTO games_players(game_id, players_id) VALUES (2, 6);
-- Also, set the game attribute in each user to reference the game they are in now
UPDATE users SET game = 2 WHERE username = 'gandalf';
UPDATE users SET game = 2 WHERE username = 'frodo';
UPDATE users SET game = 2 WHERE username = 'legolas';
UPDATE users SET game = 2 WHERE username = 'aragorn';


-- CREATE A GAME HISTORY FROM A LOBBY WITH 3 PLAYERS
-- CREATE A GAME FROM A LOBBY WITH 4 PLAYERS
-- Create the initial lobby
INSERT INTO lobbies (id, name, game, has_scenes, spectators_allowed, max_players, host) VALUES (3,'LING 2', null, true, false, 3, 'stockie');
-- add the users to the lobby
UPDATE users SET lobby = 3 WHERE username = 'stockie';
UPDATE users SET lobby = 3 WHERE username = 'alejandro';
-- create the players instances and then a game
INSERT INTO players(id, glory, gold, kills, wounds, character_id, turn_order, user_id) VALUES (7, 0, 0, 0, 0, 2, 1, 'stockie');
INSERT INTO players(id, glory, gold, kills, wounds, character_id, turn_order, user_id) VALUES (8, 0 ,0, 0, 0 ,4, 0, 'alejandro');
INSERT INTO games(id, has_scenes, start_time, leader_id) VALUES (3, true, 1637881111234, 8);
-- Once the game is created, the lobby references the game
UPDATE lobbies SET game = 3 WHERE id = 3;
-- Add the players to the game
INSERT INTO games_players(game_id, players_id) VALUES (3, 7);
INSERT INTO games_players(game_id, players_id) VALUES (3, 8);
-- Also, set the game attribute in each user to reference the game they are in now
UPDATE users SET game = 3 WHERE username = 'stockie';
UPDATE users SET game = 3 WHERE username = 'alejandro';

INSERT INTO games_history(id, game_id, winner_id, finish_time) VALUES (1, 3, 7, 1637882596427);
-- Set that the users are not in a game anymore
UPDATE users set game = null WHERE username = 'stockie';
UPDATE users set game = null WHERE username = 'alejandro';


-- UnregisteredUser for testing
INSERT INTO unregistered_users(username, creation_time) VALUES ('user0023', 1637882596427);
