CREATE TABLE `mock_entity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `unique_property` varchar(42) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `removed_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_uuid___mock_entity` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;