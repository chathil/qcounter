--Add initial roles
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

--Add initial users
INSERT INTO users (id, created_at, updated_at, email, ip_address, is_active, location, name, password, profile_completion)
VALUES (1, '2020-07-26 01:02:03', '2020-07-26 01:02:03','chathil98@gmail.com', '192.168.1.99', true, 'Indonesia', 'Abdul Chathil', '$2a$10$IiSrZE6rocnJz6lNVSuLJOjPEM5X4ACumcdWvJP1JhvWKEQdNyv62', 10);
INSERT INTO users (id, created_at, updated_at, email, ip_address, is_active, location, name, password, profile_completion)
VALUES (2, '2020-07-26 01:02:03', '2020-07-26 01:02:03','yusuf@gmail.com', '192.168.1.99', true, 'Indonesia', 'Yusuf Raditya', '$2a$10$IiSrZE6rocnJz6lNVSuLJOjPEM5X4ACumcdWvJP1JhvWKEQdNyv62', 10);
INSERT INTO users (id, created_at, updated_at, email, ip_address, is_active, location, name, password, profile_completion)
VALUES (3, '2020-07-26 01:02:03', '2020-07-26 01:02:03','ihza@gmail.com', '192.168.1.99', true, 'Indonesia', 'Ihza Ahmad', '$2a$10$IiSrZE6rocnJz6lNVSuLJOjPEM5X4ACumcdWvJP1JhvWKEQdNyv62', 10);
INSERT INTO users (id, created_at, updated_at, email, ip_address, is_active, location, name, password, profile_completion)
VALUES (4, '2020-07-26 01:02:03', '2020-07-26 01:02:03','mark@gmail.com', '192.168.1.99', true, 'Indonesia', 'Mark My Name', '$2a$10$IiSrZE6rocnJz6lNVSuLJOjPEM5X4ACumcdWvJP1JhvWKEQdNyv62', 10);
INSERT INTO users (id, created_at, updated_at, email, ip_address, is_active, location, name, password, profile_completion)
VALUES (5, '2020-07-26 01:02:03', '2020-07-26 01:02:03','shawn@gmail.com', '192.168.1.99', true, 'Indonesia', 'Shawn Sanders', '$2a$10$IiSrZE6rocnJz6lNVSuLJOjPEM5X4ACumcdWvJP1JhvWKEQdNyv62', 10);
INSERT INTO users (id, created_at, updated_at, email, ip_address, is_active, location, name, password, profile_completion)
VALUES (6, '2020-07-26 01:02:03', '2020-07-26 01:02:03','lakeesha@gmail.com', '192.168.1.99', true, 'Indonesia', 'Lakeesha from the Hood', '$2a$10$IiSrZE6rocnJz6lNVSuLJOjPEM5X4ACumcdWvJP1JhvWKEQdNyv62', 10);
INSERT INTO users (id, created_at, updated_at, email, ip_address, is_active, location, name, password, profile_completion)
VALUES (7, '2020-07-26 01:02:03', '2020-07-26 01:02:03','sterling@gmail.com', '192.168.1.99', true, 'Indonesia', 'Jack Sterling', '$2a$10$IiSrZE6rocnJz6lNVSuLJOjPEM5X4ACumcdWvJP1JhvWKEQdNyv62', 10);
INSERT INTO users (id, created_at, updated_at, email, ip_address, is_active, location, name, password, profile_completion)
VALUES (8, '2020-07-26 01:02:03', '2020-07-26 01:02:03','gabrielle@gmail.com', '192.168.1.99', true, 'Indonesia', 'Gabrielle', '$2a$10$IiSrZE6rocnJz6lNVSuLJOjPEM5X4ACumcdWvJP1JhvWKEQdNyv62', 10);
INSERT INTO users (id, created_at, updated_at, email, ip_address, is_active, location, name, password, profile_completion)
VALUES (9, '2020-07-26 01:02:03', '2020-07-26 01:02:03','sheena@gmail.com', '192.168.1.99', true, 'Indonesia', 'Sheena', '$2a$10$IiSrZE6rocnJz6lNVSuLJOjPEM5X4ACumcdWvJP1JhvWKEQdNyv62', 10);
INSERT INTO users (id, created_at, updated_at, email, ip_address, is_active, location, name, password, profile_completion)
VALUES (10, '2020-07-26 01:02:03', '2020-07-26 01:02:03','chef@gmail.com', '192.168.1.99', true, 'Indonesia', 'Chef Jerome', '$2a$10$IiSrZE6rocnJz6lNVSuLJOjPEM5X4ACumcdWvJP1JhvWKEQdNyv62', 10);

--Add initial user_devices
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (1, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ1', true, 1);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (2, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ2', true, 1);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (3, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ3', true, 1);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (4, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ41', true, 2);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (5, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ42', true, 2);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (6, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ51', true, 3);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (7, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ52', true, 3);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (8, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ6', true, 4);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (9, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ7', true, 5);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (10, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ8', true, 6);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (11, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ9', true, 7);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (12, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ10', true, 8);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (13, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ11', true, 9);
INSERT INTO user_devices(id, device_token, is_refresh_active, user_id) VALUES (14, 'eyJzdWIiOiIxNSIsImlhdCI6MTU5NTc3NDEzMSwiZXhwIjoxNTk1ODA2NTMxfQ12', true, 10);

--Add initial refresh_tokens
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (1, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 0, 'e765a49f-cdab-4db0-bf7e-e8faea9f74421', 1);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (2, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 1, 'e765a49f-cdab-4db0-bf7e-e8faea9f74422', 2);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (3, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 0, 'e765a49f-cdab-4db0-bf7e-e8faea9f74423', 3);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (4, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 1, 'e765a49f-cdab-4db0-bf7e-e8faea9f74424', 4);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (5, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 0, 'e765a49f-cdab-4db0-bf7e-e8faea9f74425', 5);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (6, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 1, 'e765a49f-cdab-4db0-bf7e-e8faea9f74426', 6);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (7, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 0, 'e765a49f-cdab-4db0-bf7e-e8faea9f74427', 7);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (8, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 1, 'e765a49f-cdab-4db0-bf7e-e8faea9f74428', 8);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (9, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 0, 'e765a49f-cdab-4db0-bf7e-e8faea9f74429', 9);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (10, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 1, 'e765a49f-cdab-4db0-bf7e-e8faea9f744211', 10);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (11, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 0, 'e765a49f-cdab-4db0-bf7e-e8faea9f744212', 11);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (12, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 1, 'e765a49f-cdab-4db0-bf7e-e8faea9f744213', 12);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (13, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 0, 'e765a49f-cdab-4db0-bf7e-e8faea9f744214', 13);
INSERT INTO refresh_tokens(id, created_at, updated_at, expiry_date, refresh_count, token, user_device_id)
VALUES (14, '2020-07-26 01:02:03', '2020-07-26 01:02:03', '2021-01-26 01:02:03', 1, 'e765a49f-cdab-4db0-bf7e-e8faea9f744215', 14);

--Add initial queue_stats
INSERT INTO queue_stats(id, current_in_queue, current_queue, state) VALUES (1, 10, 1, 0);
INSERT INTO queue_stats(id, current_in_queue, current_queue, state) VALUES (2, 10, 2, 0);
INSERT INTO queue_stats(id, current_in_queue, current_queue, state) VALUES (3, 10, 1, 0);
INSERT INTO queue_stats(id, current_in_queue, current_queue, state) VALUES (4, 10, 5, 0);

--Add initial queues
INSERT INTO queues(id, created_at, updated_at, client_generated_id, contact, description, increment_by, is_closed_queue, location, max_capacity, name, valid_until, owner_user_id, queue_stats_id)
VALUES (1, '2020-07-26 01:02:03', '2020-07-26 01:02:03', 'VlTudOiQ1K7l5kg6xLDyA38JLdPbl_2loOfUwA20cZykYgr4_6qAlxKaGdIuFZw8TKhuyIsE4D41PjOlwUo13g1', '085306440054', 'A quite long queue description to illustrate how good it will look in front end, but not too long', 1, false, 'Indonesia', 15, 'Lorem Queue Chathil', '2020-12-26 01:02:03', 1, 1);
INSERT INTO queues(id, created_at, updated_at, client_generated_id, contact, description, increment_by, is_closed_queue, location, max_capacity, name, valid_until, owner_user_id, queue_stats_id)
VALUES (2, '2020-07-26 01:02:03', '2020-07-26 01:02:03', 'VlTudOiQ1K7l5kg6xLDyA38JLdPbl_2loOfUwA20cZykYgr4_6qAlxKaGdIuFZw8TKhuyIsE4D41PjOlwUo13g2', '085306440055', 'A quite long queue description to illustrate how good it will look in front end, but not too long', 1, true, 'Indonesia', 10, 'Lorem Queue Chathil', '2020-12-26 01:02:03', 1, 2);
INSERT INTO queues(id, created_at, updated_at, client_generated_id, contact, description, increment_by, is_closed_queue, location, max_capacity, name, valid_until, owner_user_id, queue_stats_id)
VALUES (3, '2020-07-26 01:02:03', '2020-07-26 01:02:03', 'VlTudOiQ1K7l5kg6xLDyA38JLdPbl_2loOfUwA20cZykYgr4_6qAlxKaGdIuFZw8TKhuyIsE4D41PjOlwUo13g3', '085306440056', 'A quite long queue description to illustrate how good it will look in front end, but not too long', 1, false, 'Indonesia', 12, 'Lorem Queue Yusuf', '2020-12-26 01:02:03', 2, 3);
INSERT INTO queues(id, created_at, updated_at, client_generated_id, contact, description, increment_by, is_closed_queue, location, max_capacity, name, valid_until, owner_user_id, queue_stats_id)
VALUES (4, '2020-07-26 01:02:03', '2020-07-26 01:02:03', 'VlTudOiQ1K7l5kg6xLDyA38JLdPbl_2loOfUwA20cZykYgr4_6qAlxKaGdIuFZw8TKhuyIsE4D41PjOlwUo13g4', '085306440057', 'A quite long queue description to illustrate how good it will look in front end, but not too long', 1, false, 'Indonesia', 13, 'Lorem Queue Ihza', '2020-12-26 01:02:03', 3, 4);

--Add initial in_queues
INSERT INTO in_queues(id, blocked_at, called_at, contact, exited_at, joined_at, name, queue_num, queue_id, user_id)
VALUES (1, null, null, '085206440054', null, '2020-07-26 02:02:03', 'Abdul Chathil', 1, 3, 1);
INSERT INTO in_queues(id, blocked_at, called_at, contact, exited_at, joined_at, name, queue_num, queue_id, user_id)
VALUES (2, null, null, '085206440054', null, '2020-07-26 02:02:03', 'Ihza Ahmad', 2, 3, 3);
INSERT INTO in_queues(id, blocked_at, called_at, contact, exited_at, joined_at, name, queue_num, queue_id, user_id)
VALUES (3, null, null, '085206440054', null, '2020-07-26 02:02:03', 'Mark My Name', 3, 3, 4);
INSERT INTO in_queues(id, blocked_at, called_at, contact, exited_at, joined_at, name, queue_num, queue_id, user_id)
VALUES (4, null, null, '085206440054', null, '2020-07-26 02:02:03', 'Shawn Sanders', 4, 3, 5);
INSERT INTO in_queues(id, blocked_at, called_at, contact, exited_at, joined_at, name, queue_num, queue_id, user_id)
VALUES (5, null, null, '085206440054', null, '2020-07-26 02:02:03', 'Lakeesha from the Hood', 5, 3, 6);
INSERT INTO in_queues(id, blocked_at, called_at, contact, exited_at, joined_at, name, queue_num, queue_id, user_id)
VALUES (6, null, null, '085206440054', null, '2020-07-26 02:02:03', 'Jack Sterling', 6, 3, 7);
INSERT INTO in_queues(id, blocked_at, called_at, contact, exited_at, joined_at, name, queue_num, queue_id, user_id)
VALUES (7, null, null, '085206440054', null, '2020-07-26 02:02:03', 'Gabrielle', 7, 3, 8);
INSERT INTO in_queues(id, blocked_at, called_at, contact, exited_at, joined_at, name, queue_num, queue_id, user_id)
VALUES (8, null, null, '085206440054', null, '2020-07-26 02:02:03', 'Sheena', 8, 3, 9);
INSERT INTO in_queues(id, blocked_at, called_at, contact, exited_at, joined_at, name, queue_num, queue_id, user_id)
VALUES (9, null, null, '085206440054', null, '2020-07-26 02:02:03', 'Chef Jerr', 9, 3, 10);

--Add initial user_authority
INSERT INTO user_authority(user_id, role_id) VALUES (1, 1);
INSERT INTO user_authority(user_id, role_id) VALUES (1, 2);
INSERT INTO user_authority(user_id, role_id) VALUES (2, 1);
INSERT INTO user_authority(user_id, role_id) VALUES (3, 1);
INSERT INTO user_authority(user_id, role_id) VALUES (4, 1);
INSERT INTO user_authority(user_id, role_id) VALUES (5, 1);
INSERT INTO user_authority(user_id, role_id) VALUES (6, 1);
INSERT INTO user_authority(user_id, role_id) VALUES (7, 1);
INSERT INTO user_authority(user_id, role_id) VALUES (8, 1);
INSERT INTO user_authority(user_id, role_id) VALUES (9, 1);
INSERT INTO user_authority(user_id, role_id) VALUES (10, 1);





