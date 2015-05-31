CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `yago_id` varchar(45) DEFAULT NULL,
  `lng` float DEFAULT NULL,
  `lat` float DEFAULT NULL,
  `image` varchar(45) DEFAULT NULL,
  `route_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `INDEX_ORDER` (`route_order`)
) ENGINE=InnoDB AUTO_INCREMENT=491 DEFAULT CHARSET=latin1;

CREATE TABLE `fact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country_id` int(11) DEFAULT NULL,
  `data` varchar(255) DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  `rank` float DEFAULT NULL,
  `yago_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_COUNTRY_ID_idx` (`country_id`),
  KEY `FK_QUESTION_TYPE_idx` (`type_id`),
  KEY `INDEX_RANK` (`rank`),
  CONSTRAINT `FK_COUNTRY_ID` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_QUESTION_TYPE` FOREIGN KEY (`type_id`) REFERENCES `fact_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=493182 DEFAULT CHARSET=latin1;

CREATE TABLE `fact_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `rank` float DEFAULT NULL,
  `is_property_of_country` bit(1) DEFAULT NULL,
  `question_wording` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=390 DEFAULT CHARSET=latin1;

CREATE TABLE `highscore` (
  `id` int(11) NOT NULL,
  `name` varchar(150) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `link` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `entity1` varchar(150) DEFAULT NULL,
  `entity2` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15024336 DEFAULT CHARSET=latin1;
