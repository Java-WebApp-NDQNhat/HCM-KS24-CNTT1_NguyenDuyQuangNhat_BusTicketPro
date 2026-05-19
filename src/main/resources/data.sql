INSERT INTO roles (id, name, slug) VALUES (1, 'Administrator', 'ADMIN')
ON DUPLICATE KEY UPDATE name = VALUES(name), slug = VALUES(slug);
INSERT INTO roles (id, name, slug) VALUES (2, 'Passenger', 'PASSENGER')
ON DUPLICATE KEY UPDATE name = VALUES(name), slug = VALUES(slug);

INSERT INTO permissions (id, name, slug) VALUES (1, 'Admin Access', 'ADMIN_ACCESS')
ON DUPLICATE KEY UPDATE name = VALUES(name), slug = VALUES(slug);
INSERT INTO permissions (id, name, slug) VALUES (2, 'Passenger Access', 'PASSENGER_ACCESS')
ON DUPLICATE KEY UPDATE name = VALUES(name), slug = VALUES(slug);

INSERT IGNORE INTO role_permission (role_id, permission_id) VALUES (1, 1);
INSERT IGNORE INTO role_permission (role_id, permission_id) VALUES (2, 2);

INSERT IGNORE INTO users (id, user_name, email, phone, address, password_hash, role_id) VALUES (1, 'admin system', 'admin@gmail.com', '0987654321', 'VN', '$2a$10$jivQED6nsdZyJfBAtzD.seiFE3BnIVb/Numhn8crX4yJHrDF1I4Qa', 1);
