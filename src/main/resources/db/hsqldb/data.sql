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

INSERT INTO warlords(id, endurance, type, image) VALUES (1, 8, 'GURDRUG', '/cards/enemies/warlords/warlords__0000s_0000_Thunberg.png');
INSERT INTO warlords(id, endurance, type, image) VALUES (2, 9, 'ROGHKILLER', '/cards/enemies/warlords/warlords__0000s_0000_Thunberg.png');
INSERT INTO warlords(id, endurance, type, image) VALUES (3, 10, 'SHRIEKKNIFER', '/cards/enemies/warlords/warlords__0000s_0000_Shrek.png');


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
