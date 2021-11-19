-- One admin user, named admin with password admin and authority admin
INSERT INTO users(username,password, email) VALUES ('admin','admin', 'admin@mail.com');
INSERT INTO authorities(id,username,authority) VALUES (1,'admin','admin');

INSERT INTO users(username,password,enabled) VALUES ('andres','andres',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'andres','user');

-- INSERT INTO users(username,password,enabled) VALUES ('pabsanval1','idk',TRUE);
-- INSERT INTO authorities(id,username,authority) VALUES (5,'pabsanval1','owner');

-- INSERT INTO users(username,password,enabled) VALUES ('jaistomen','idk2',TRUE);
-- INSERT INTO authorities(id,username,authority) VALUES (6,'jaistomen','owner');

-- INSERT INTO users(username,password,enabled) VALUES ('pabrobcam','pabrobcam',TRUE);
-- INSERT INTO authorities(id,username,authority) VALUES (7,'pabrobcam','owner');

INSERT INTO horde_enemy_types(horde_enemy_type, horde_enemy_modifier_type, endurance) VALUES ('WARRIOR', null, 4)
INSERT INTO horde_enemy_types(horde_enemy_type, horde_enemy_modifier_type, endurance) VALUES ('SLINGER', null, 2)
INSERT INTO horde_enemy_types(horde_enemy_type, horde_enemy_modifier_type, endurance) VALUES ('BERSERKER', null, 6)
INSERT INTO horde_enemy_types(horde_enemy_type, horde_enemy_modifier_type, endurance) VALUES ('REGEN','HEALING_CAPABILITIES', 3)
INSERT INTO horde_enemy_types(horde_enemy_type, horde_enemy_modifier_type, endurance) VALUES ('MAGE','MAGIC_ATTACKER', 5)
INSERT INTO horde_enemy_types(horde_enemy_type, horde_enemy_modifier_type, endurance) VALUES ('SHAMAN','MAGIC_ATTACKER', 3)

INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (1, 'WARRIOR', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (2, 'WARRIOR', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (3, 'WARRIOR', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (4, 'WARRIOR', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (5, 'WARRIOR', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (6, 'WARRIOR', 2, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (7, 'WARRIOR', 2, 1);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (8, 'WARRIOR', 2, 1);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (9, 'SLINGER', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (10, 'SLINGER', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (11, 'SLINGER', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (12, 'SLINGER', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (13, 'SLINGER', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (14, 'BERSERKER', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (15, 'BERSERKER', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (16, 'BERSERKER', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (17, 'REGEN', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (18, 'REGEN', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (19, 'REGEN', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (20, 'REGEN', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (21, 'REGEN', 1, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (22, 'MAGE', 0, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (23, 'MAGE', 2, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (24, 'MAGE', 2, 0);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (25, 'MAGE', 2, 1);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (26, 'SHAMAN', 1, 1);
INSERT INTO horde_enemies(id, horde_enemy_type, gold, extra_glory) VALUES (27, 'SHAMAN', 2, 0);

-- Ejemplo de entidad hordeEnemyIngame:
-- INSERT INTO horde_enemies_ingame(id, game_id, horde_enemy_id, current_endurance, horde_enemy_location) VALUES (1, 1, 1, defaultEndurance, "HORDE_PILE")

INSERT INTO warlords(id, warlord_type_enum, endurance) VALUES (1, 'GURDRUG', 8);
INSERT INTO warlords(id, warlord_type_enum, endurance) VALUES (2, 'ROGHKILLER', 9);
INSERT INTO warlords(id, warlord_type_enum, endurance) VALUES (3, 'SHRIEKKNIFER', 10);

-- Ejemplo de entidad warlord ingame:
-- INSERT INTO warlords_ingame(id, game_id, warlord_id, current_endurance, warlord_location) VALUES (1, 1, 1, 8, "HORDE_PILE");

INSERT INTO scenes(id, type) VALUES (1, 'MERCADO_DE_LOTHARION'); 
INSERT INTO scenes(id, type) VALUES (2, 'CAMPO_DE_BATALLA');
INSERT INTO scenes(id, type) VALUES (3, 'LAGRIMAS_DE_ARADIEL');
INSERT INTO scenes(id, type) VALUES (4, 'LODAZAL_DE_KALERN');
INSERT INTO scenes(id, type) VALUES (5, 'MONTANAS_DE_UR');
INSERT INTO scenes(id, type) VALUES (6, 'PANTANO_UMBRIO');
INSERT INTO scenes(id, type) VALUES (7, 'PLANICIE_DE_SKAARG');
INSERT INTO scenes(id, type) VALUES (8, 'PORTAL_DE_ULTHAR');
INSERT INTO scenes(id, type) VALUES (9, 'PUERTO_DE_EQUE');
INSERT INTO scenes(id, type) VALUES (10, 'RUINAS_DE_BRUNMAR');
INSERT INTO scenes(id, type) VALUES (11, 'YACIMIENTOS_DE_JADE');
INSERT INTO scenes(id, type) VALUES (12, 'YERMO_DE_CEMENMAR');

--INSERT INTO playable_cards_ingame(id, game_id, ability_card_id, location) VALUES (1, 1, 1, 'HORDE_PILE');

-- MARKET no es una propiedad de las cartas. Es una propiedad de la instancia de la carta, cuando se encuentre ya dentro de un juego.
INSERT INTO market_cards(id, price, name, usable_by) VALUES (1, 3, 'DAGA_ELFICA', 'RANGER, ROGUE, WARRIOR, WIZARD');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (2, 3, 'DAGA_ELFICA', 'RANGER, ROGUE, WARRIOR, WIZARD');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (3, 8, 'POCION_CURATIVA', 'RANGER, ROGUE, WARRIOR');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (4, 8, 'POCION_CURATIVA', 'RANGER, ROGUE, WARRIOR, WIZARD');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (5, 8, 'POCION_CURATIVA', 'RANGER, ROGUE, WARRIOR, WIZARD');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (6, 4, 'PIEDRA_DE_AMOLAR', 'RANGER, ROGUE, WARRIOR, WIZARD');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (7, 5, 'VIAL_DE_CONJURACION', 'RANGER, ROGUE, WARRIOR, WIZARD');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (8, 5, 'VIAL_DE_CONJURACION', 'RANGER, ROGUE, WARRIOR, WIZARD');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (9, 3, 'ELIXIR_DE_LA_CONCENTRACION', 'RANGER, ROGUE, WARRIOR, WIZARD');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (10, 3, 'ELIXIR_DE_LA_CONCENTRACION', 'RANGER, ROGUE, WARRIOR, WIZARD');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (11, 3, 'CAPA_ELFICA', 'RANGER, WIZARD');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (12, 4, 'ARMADURA_DE_PLACAS', 'WARRIOR');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (13, 5, 'ALABARDA_ORCA', 'WARRIOR');
INSERT INTO market_cards(id, price, name, usable_by) VALUES (14, 5, 'ARCO_COMPUESTO', 'RANGER');

-- Eso será una tabla diferente que trackee el uso de la carta dentro de la partida (si la tiene alguien, su posición...
-- INSERT INTO market_cards_ingame(id, game_id, market_card_id, market_card_location) VALUES (1, 1, "ROGUE_HAND")
