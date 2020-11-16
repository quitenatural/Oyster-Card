create TABLE `users`
(
`id` int(11) NOT NULL AUTO_INCREMENT,
`first_name` VARCHAR(255) NOT NULL,
`last_name` VARCHAR(255) NOT NULL,
`card_id` int(11) NOT NULL,
PRIMARY KEY (`id`),
UNIQUE KEY `idx_unq_name` (`first_name`, `last_name`),
KEY `fk_card_id_idx` (`card_id`),
CONTRAINT `fk_card_id` FOREIGN KEY (`card_id`) REFERENCES `cards`(`id`)
);