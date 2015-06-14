CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `yago_id` varchar(45) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `label` varchar(45) DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `updated` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `IDX_deleted` (`deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=latin1;

CREATE TABLE `country_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `route_order` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `poster_image` varchar(128) DEFAULT NULL,
  `route_name` varchar(45) DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `updated` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `route_order_UNIQUE` (`route_order`),
  KEY `FK_COUNTRY_COUNTRY_ORDER_idx` (`country_id`),
  KEY `IDX_DELETED` (`deleted`),
  CONSTRAINT `FK_COUNTRY_COUNTRY_ORDER` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=latin1;

CREATE TABLE `fact_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `rank` float DEFAULT NULL,
  `is_literal` bit(1) DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `updated` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `IDX_DELETED` (`deleted`),
  KEY `IDX_IS_LITERAL` (`is_literal`)
) ENGINE=InnoDB AUTO_INCREMENT=817 DEFAULT CHARSET=latin1;

CREATE TABLE `fact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `yago_id` varchar(100) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `data` varchar(255) DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  `rank` float DEFAULT '0',
  `deleted` bit(1) DEFAULT b'0',
  `updated` bit(1) DEFAULT b'0',
  `label` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_COUNTRY_ID_idx` (`country_id`),
  KEY `FK_QUESTION_TYPE_idx` (`type_id`),
  KEY `IDX_RANK` (`rank`),
  KEY `IDX_DELETED` (`deleted`),
  CONSTRAINT `FK_COUNTRY_ID` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_QUESTION_TYPE` FOREIGN KEY (`type_id`) REFERENCES `fact_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=930246 DEFAULT CHARSET=latin1;



CREATE TABLE `fact_type_question_wording` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fact_id` int(11) DEFAULT NULL,
  `question_wording` varchar(255) DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `updated` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `fact_id_UNIQUE` (`fact_id`),
  KEY `FK_FACT_TYPE_WORDING_FACT_TYPE_idx` (`fact_id`),
  KEY `IDX_DELETED` (`deleted`),
  CONSTRAINT `FK_FACT_TYPE_WORDING_FACT_TYPE` FOREIGN KEY (`fact_id`) REFERENCES `fact_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `updated` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `IDX_DELETED` (`deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

CREATE TABLE `user_fact_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `fact_id` int(11) DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `updated` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_USER_FACT_HISTORY_USER_idx` (`user_id`),
  KEY `FK_USER_FACT_HISTORY_FACT_idx` (`fact_id`),
  KEY `IDX_DELETED` (`deleted`),
  CONSTRAINT `FK_USER_FACT_HISTORY_FACT` FOREIGN KEY (`fact_id`) REFERENCES `fact` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_USER_FACT_HISTORY_USER` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=latin1;


CREATE TABLE `highscore` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `updated` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_HIGHSCORE_USER_ID_idx` (`user_id`),
  KEY `IDX_DELETED` (`deleted`),
  CONSTRAINT `FK_HIGHSCORE_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
