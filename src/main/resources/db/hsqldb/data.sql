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
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (27, 'SHAMAN', 1, 1);
INSERT INTO horde_enemies(id, type, gold, extra_glory) VALUES (26, 'SHAMAN', 2, 0);

INSERT INTO warlords(id, type) VALUES (1, 'GURDRUG');
INSERT INTO warlords(id, type) VALUES (2, 'ROGHKILLER');
INSERT INTO warlords(id, type) VALUES (3, 'SHRIEKKNIFER');

INSERT INTO scenes(id, name) VALUES (1, 'Mercado de Lotharion');
INSERT INTO scenes(id, name) VALUES (2, 'Campo de Batalla');
INSERT INTO scenes(id, name) VALUES (3, 'Lágrimas de Aradiel');
INSERT INTO scenes(id, name) VALUES (4, 'Lodazal de Kalern');
INSERT INTO scenes(id, name) VALUES (5, 'Montañas de Ur');
INSERT INTO scenes(id, name) VALUES (6, 'Pantano Umbrío');
INSERT INTO scenes(id, name) VALUES (7, 'Planicie de Skaarg');
INSERT INTO scenes(id, name) VALUES (8, 'Portal de Ulthar');
INSERT INTO scenes(id, name) VALUES (9, 'Puerto de Eqûe');
INSERT INTO scenes(id, name) VALUES (10, 'Ruinas de Brunmar');
INSERT INTO scenes(id, name) VALUES (11, 'Yacimientos de Jade');
INSERT INTO scenes(id, name) VALUES (12, 'Yermo de Cémenmar');
