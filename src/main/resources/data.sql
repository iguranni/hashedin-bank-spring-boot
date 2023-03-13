INSERT INTO `hashedin-bank`.role(`role_id`, `role_nme`) VALUES (1, 'ROLE_USER');

INSERT INTO `hashedin-bank`.role(`role_id`, `role_nme`) VALUES (2, 'ROLE_SUPER_ADMIN');

INSERT INTO `hashedin-bank`.role(`role_id`, `role_nme`) VALUES (3, 'ROLE_PROGRAM_ADMIN');

INSERT INTO `hashedin-bank`.role(`role_id`, `role_nme`) VALUES (4, 'ROLE_ADMIN');

INSERT INTO `hashedin-bank`.`user`(`user_id`,`created_dt`,`created_by`,`updated_dt`,`updated_by`,`email`,`first_nme`,`is_active_flg`,`is_approved_flg`,`last_nme`,`password`,`phone`,`remarks`,`company_id`)
VALUES (1,CURRENT_TIMESTAMP,'SYSTEM',CURRENT_TIMESTAMP,'SYSTEM','root@hashedin.com','Root',1,1,'User','$2a$12$2pTyY7H3pKAewsiviBma4emrjqHkxG7LRRn7XJuInQpGN5/LflQpi','1234567891','default user',null);

INSERT INTO `hashedin-bank`.`expense_category`(`expense_category_id`,`expense_category_nme`,`expense_cd`,`created_dt`,`created_by`,`updated_dt`,`updated_by`)
VALUES (1,'Well Being Subsidy','WELLBEING561',CURRENT_TIMESTAMP,'SYSTEM',CURRENT_TIMESTAMP,'SYSTEM');
--WHERE NOT EXISTS
--(SELECT `expense_cd` FROM  `hashedin-bank`.`expense_category` WHERE `expense_cd`= 'WELLBEING561');

INSERT INTO `hashedin-bank`.`expense_category`(`expense_category_id`,`expense_category_nme`,`expense_cd`,`created_dt`,`created_by`,`updated_dt`,`updated_by`)
VALUES (2,'Medical Expenses','MED615',CURRENT_TIMESTAMP,'SYSTEM',CURRENT_TIMESTAMP,'SYSTEM');
--WHERE NOT EXISTS
--(SELECT `expense_cd` FROM  `hashedin-bank`.`expense_category` WHERE `expense_cd`= 'MED615');

INSERT INTO `hashedin-bank`.`expense_category`(`expense_category_id`,`expense_category_nme`,`expense_cd`,`created_dt`,`created_by`,`updated_dt`,`updated_by`)
VALUES (3,'Commute Expense','COMM156',CURRENT_TIMESTAMP,'SYSTEM',CURRENT_TIMESTAMP,'SYSTEM');
--WHERE NOT EXISTS
--(SELECT `expense_cd` FROM  `hashedin-bank`.`expense_category` WHERE `expense_cd`= 'COMM156');
