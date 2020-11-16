create TABLE `user_journey`
(
`id` int(11) NOT NULL AUTO_INCREMENT,
`date` time with timezone NOT NULL,
`transport` enum('BUS','TUBE') NOT NULL,
`from_station` enum('HOLBORN','EARLS_COURT','WIMBLEDON','HAMMERSMITH') NOT NULL,
`to_station` enum('HOLBORN','EARLS_COURT','WIMBLEDON','HAMMERSMITH') NOT NULL,
`card_id` int(11) NOT NULL,
PRIMARY KEY (`id`),
KEY `fk_card_id_idx` (`card_id`),
CONTRAINT `fk_card_id` FOREIGN KEY (`card_id`) REFERENCES `cards`(`id`)
);