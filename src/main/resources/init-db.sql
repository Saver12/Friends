CREATE TABLE `users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '',
  `email` varchar(50) DEFAULT NULL,
  `country` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `photoId` int(11) unsigned NOT NULL,
  `surname` varchar(20) NOT NULL DEFAULT '',
  `job` varchar(100) NOT NULL DEFAULT '',
  `education` varchar(100) NOT NULL DEFAULT '',
  `relationship` varchar(50) NOT NULL DEFAULT '',
  `birthdate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) DEFAULT CHARSET=utf8;

CREATE TABLE `photos` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userId` int(11) unsigned NOT NULL,
  `name` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) DEFAULT CHARSET=utf8;

CREATE TABLE `audios` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userId` int(11) unsigned NOT NULL,
  `name` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) DEFAULT CHARSET=utf8;

CREATE TABLE `friends` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `firstFriend` int(11) unsigned NOT NULL,
  `secondFriend` int(11) unsigned NOT NULL,
  `request` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`firstFriend`) REFERENCES `users` (`id`),
  FOREIGN KEY (`secondFriend`) REFERENCES `users` (`id`)
) DEFAULT CHARSET=utf8;

CREATE TABLE `messages` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `message` varchar(500) NOT NULL DEFAULT '',
  `firstUserId` int(11) unsigned NOT NULL,
  `secondUserId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`firstUserId`) REFERENCES `users` (`id`),
  FOREIGN KEY (`secondUserId`) REFERENCES `users` (`id`)
) DEFAULT CHARSET=utf8;