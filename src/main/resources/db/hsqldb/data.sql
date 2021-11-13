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

INSERT INTO scenes(id, name) VALUES (1, 'Mercado de Lotharion');
