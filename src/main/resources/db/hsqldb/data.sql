-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password, email) VALUES ('admin','admin', 'admin@mail.com');
INSERT INTO authorities(id,username,authority) VALUES (1,'admin','admin');

-- -- One owner user, named owner1 with passwor 0wn3r
-- INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
-- INSERT INTO authorities(id,username,authority) VALUES (2,'owner1','owner');

-- INSERT INTO users(username,password,enabled) VALUES ('anddurter','anddurter',TRUE);
-- INSERT INTO authorities(id,username,authority) VALUES (4,'anddurter','owner');

-- INSERT INTO users(username,password,enabled) VALUES ('pabsanval1','idk',TRUE);
-- INSERT INTO authorities(id,username,authority) VALUES (5,'pabsanval1','owner');

-- INSERT INTO users(username,password,enabled) VALUES ('jaistomen','idk2',TRUE);
-- INSERT INTO authorities(id,username,authority) VALUES (6,'jaistomen','owner');

-- INSERT INTO users(username,password,enabled) VALUES ('pabrobcam','pabrobcam',TRUE);
-- INSERT INTO authorities(id,username,authority) VALUES (7,'pabrobcam','owner');

INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (1, 'WARRIOR', 0, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (2, 'WARRIOR', 0, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (3, 'WARRIOR', 1, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (4, 'WARRIOR', 1, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (5, 'WARRIOR', 1, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (6, 'WARRIOR', 2, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (7, 'WARRIOR', 2, 1);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (8, 'WARRIOR', 2, 1);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (9, 'SLINGER', 0, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (10, 'SLINGER', 0, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (11, 'SLINGER', 0, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (12, 'SLINGER', 1, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (13, 'SLINGER', 1, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (14, 'BERSERKER', 0, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (15, 'BERSERKER', 1, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (16, 'BERSERKER', 1, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (17, 'REGEN', 0, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (18, 'REGEN', 0, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (19, 'REGEN', 0, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (20, 'REGEN', 1, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (21, 'REGEN', 1, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (22, 'MAGE', 0, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (23, 'MAGE', 2, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (24, 'MAGE', 2, 0);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (25, 'MAGE', 2, 1);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (26, 'SHAMAN', 1, 1);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (27, 'SHAMAN', 2, 0);

INSERT INTO warlords(id, type) VALUES (1, 'GURDRUG');
INSERT INTO warlords(id, type) VALUES (2, 'ROGHKILLER');
INSERT INTO warlords(id, type) VALUES (3, 'SHRIEKKNIFER');

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

--INSERT INTO playable_cards(id, card_type, location) VALUES (1, 'ABILITY_CARD','PILE');

INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (1, 'MARKET', 3, 'DAGA_ELFICA', 'Ranger, Rogue, Warrior, Wizard');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (2, 'MARKET', 3, 'DAGA_ELFICA', 'Ranger, Rogue, Warrior, Wizard');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (3, 'MARKET', 8, 'POCION_CURATIVA', 'Ranger, Rogue, Warrior');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (4, 'MARKET', 8, 'POCION_CURATIVA', 'Ranger, Rogue, Warrior, Wizard');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (5, 'MARKET', 8, 'POCION_CURATIVA', 'Ranger, Rogue, Warrior, Wizard');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (6, 'MARKET', 4, 'PIEDRA_DE_AMOLAR', 'Ranger, Rogue, Warrior, Wizard');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (7, 'MARKET', 5, 'VIAL_DE_CONJURACION', 'Ranger, Rogue, Warrior, Wizard');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (8, 'MARKET', 5, 'VIAL_DE_CONJURACION', 'Ranger, Rogue, Warrior, Wizard');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (9, 'MARKET', 3, 'ELIXIR_DE_LA_CONCENTRACION', 'Ranger, Rogue, Warrior, Wizard');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (10, 'MARKET', 3, 'ELIXIR_DE_LA_CONCENTRACION', 'Ranger, Rogue, Warrior, Wizard');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (11, 'MARKET', 3, 'CAPA_ELFICA', 'Ranger, Wizard');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (12, 'MARKET', 4, 'ARMADURA_DE_PLACAS', 'Warrior');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (13, 'MARKET', 5, 'ALABARDA_ORCA', 'Warrior');
INSERT INTO market_cards(id, type, price, type, usableBy) VALUES (14, 'MARKET', 5, 'ARCO_COMPUESTO', 'Ranger');


