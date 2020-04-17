CREATE DATABASE IF NOT EXISTS konyvtar;

USE konyvtar;

CREATE TABLE `tag` (
  `tag_ID` int NOT NULL AUTO_INCREMENT,
  `nev` varchar(45) NOT NULL,
  `cim` varchar(45) NOT NULL,
  `telefonszam` varchar(11) DEFAULT NULL,
  `szemelyi` varchar(8) NOT NULL,
  `statusz` varchar(7) NOT NULL DEFAULT 'aktiv',
  PRIMARY KEY (`tag_ID`),
  UNIQUE KEY `szemelyi_UNIQUE` (`szemelyi`),
  UNIQUE KEY `telefonszam_UNIQUE` (`telefonszam`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `keszlet` (
  `keszlet_ID` int NOT NULL AUTO_INCREMENT,
  `szerzo` varchar(45) DEFAULT NULL,
  `cim` varchar(45) NOT NULL,
  `kategoria` varchar(20) NOT NULL,
  `darabszam` int NOT NULL,
  PRIMARY KEY (`keszlet_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `kolcsonzes` (
  `kolcsonID` int NOT NULL AUTO_INCREMENT,
  `tagID` int NOT NULL,
  `keszletID` int NOT NULL,
  `kivetel_datum` date NOT NULL,
  `hatarido` date DEFAULT NULL,
  `vissza_datum` date DEFAULT NULL,
  PRIMARY KEY (`kolcsonID`),
  KEY `tagID_idx` (`tagID`),
  KEY `keszletIdFk_idx` (`keszletID`),
  CONSTRAINT `keszletIdFk` FOREIGN KEY (`keszletID`) REFERENCES `keszlet` (`keszlet_ID`),
  CONSTRAINT `tagIdFk` FOREIGN KEY (`tagID`) REFERENCES `tag` (`tag_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO konyvtar.tag VALUES(1, 'Toth Akos', '3515 Miskolc, Egyetemvaros E/2', '06205559991', '056189TA', 'aktiv');
INSERT INTO konyvtar.tag VALUES(2, 'Kiss Zoltan', '1134 Budapest, Arpad u 12', '06703771096', '782936JF', 'aktiv');
INSERT INTO konyvtar.tag VALUES(3, 'Molnar Janos', '2800 Tatabanya, Vezer u 4', '06303244091', '749270MA', 'aktiv');
INSERT INTO konyvtar.tag VALUES(4, 'Nagy Attila', '3390 Fuzesabony, Tarkanyi u 40', '06308771239', '997134SJ', 'aktiv');
INSERT INTO konyvtar.tag VALUES(5, 'Csapo Imre', '4031 Debrecen, Jakab u 10', '06207654321', '772801KM', 'aktiv');
INSERT INTO konyvtar.tag VALUES(6, 'Horváth Balázs', '4031 Debrecen, Kossuth u 40', '06207746120', '722910UJ', 'aktiv');
INSERT INTO konyvtar.tag VALUES(7, 'Kovács Zsolt', '3322 Hevesaranyos Erzsébet Tér 52', '06308144766', '888111HJ', 'aktiv');
INSERT INTO konyvtar.tag VALUES(8, 'Kiss Imre', '9339 Öntésmajor, Wesselényi u 85', '06701752340', '836293JU', 'aktiv');
INSERT INTO konyvtar.tag VALUES(9, 'Szabó László', '4162 Hosszúhát, Tompa u 44', '06309284922', '638455AB', 'aktiv');
INSERT INTO konyvtar.tag VALUES(10, 'Juhász Lilla', '8742 Nagyhorváti, Kálmán Imre u 78', '06709482492', '173229NH', 'aktiv');
INSERT INTO konyvtar.tag VALUES(11, 'Tóth András', '9019 Gyirmót, Nyár utca 88', '06207348294', '732935MU', 'aktiv');
INSERT INTO konyvtar.tag VALUES(12, 'Boholy Anna', '8834 Bajcsa, Kálmán Imre u 19', '06309826478', '378826IU', 'aktiv');
INSERT INTO konyvtar.tag VALUES(13, 'Pásztor Béla', '5461 Homok, Veres Pálné utca 16', '06308759283', '172652JA', 'aktiv');
INSERT INTO konyvtar.tag VALUES(14, 'Varga Alex', '1034 Budapest, Király utca 40', '06509164782', '146558BA', 'aktiv');
INSERT INTO konyvtar.tag VALUES(15, 'Fülöp Ilona', '3515 Miskolc, Egyetemvaros E/4', '06205551111', '345092AT', 'aktiv');


INSERT INTO konyvtar.keszlet VALUES(1, 'Pierce Brown', 'A sotetseg kora', 'konyv', 10);
INSERT INTO konyvtar.keszlet VALUES(2, 'JR Ward', 'A kivalasztott', 'konyv', 6);
INSERT INTO konyvtar.keszlet VALUES(3, 'Christopher Nolan', 'The Dark Knight', 'film', 3);
INSERT INTO konyvtar.keszlet VALUES(4, 'Stephen King', 'Alomdoktor', 'konyv', 4);
INSERT INTO konyvtar.keszlet(keszlet_ID, cim, kategoria, darabszam) VALUES(5, 'Forbes', 'folyoirat', 1);
INSERT INTO konyvtar.keszlet(keszlet_ID, cim, kategoria, darabszam) VALUES(6, 'MS Office 2016', 'szoftver', 3);
INSERT INTO konyvtar.keszlet VALUES(7, 'Andrzej Sapkowski', 'Viharido', 'konyv', 4);
INSERT INTO konyvtar.keszlet VALUES(8, 'Andy Weir', 'The Martian', 'konyv', 12);
INSERT INTO konyvtar.keszlet(keszlet_ID, cim, kategoria, darabszam) VALUES(9, 'PTC Creo', 'szoftver', 10);
INSERT INTO konyvtar.keszlet(keszlet_ID, cim, kategoria, darabszam) VALUES(10, 'GameStar', 'folyoirat', 3);
INSERT INTO konyvtar.keszlet VALUES(11, 'Edith Eva Eger', 'A dontes', 'konyv', 7);
INSERT INTO konyvtar.keszlet VALUES(12, 'JK Rowling', 'Harry Potter es a Tuz Serlege', 'konyv', 21);
INSERT INTO konyvtar.keszlet VALUES(13, 'Harry Potter es a Tuz Serlege', 'The Winds of Winter', 'konyv', 7);


INSERT INTO konyvtar.kolcsonzes VALUES(1, 4, 2, '2020-01-03', '2020-02-03', '2020-02-09');
INSERT INTO konyvtar.kolcsonzes VALUES(2, 10, 7, '2020-02-14', '2020-03-14', '2020-04-01');
INSERT INTO konyvtar.kolcsonzes VALUES(3, 7, 1, '2020-04-09', '2020-05-09', '2020-04-14');
INSERT INTO konyvtar.kolcsonzes VALUES(4, 6, 9, '2020-01-30', '2020-02-28', '2020-03-08');
INSERT INTO konyvtar.kolcsonzes(kolcsonID, tagID, keszletID, kivetel_datum, hatarido) VALUES(5, 14, 8, '2020-04-12', '2020-05-12');
INSERT INTO konyvtar.kolcsonzes(kolcsonID, tagID, keszletID, kivetel_datum, hatarido) VALUES(6, 1, 12, '2020-04-11', '2020-05-11');
INSERT INTO konyvtar.kolcsonzes(kolcsonID, tagID, keszletID, kivetel_datum, hatarido) VALUES(7, 4, 4, '2020-03-27', '2020-04-27');