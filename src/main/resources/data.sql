INSERT INTO roles (id, name, slug) VALUES (1, 'Administrator', 'ADMIN')
ON DUPLICATE KEY UPDATE name = VALUES(name), slug = VALUES(slug);
INSERT INTO roles (id, name, slug) VALUES (2, 'Passenger', 'PASSENGER')
ON DUPLICATE KEY UPDATE name = VALUES(name), slug = VALUES(slug);
INSERT INTO roles (id, name, slug) VALUES (3, 'Staff', 'STAFF')
ON DUPLICATE KEY UPDATE name = VALUES(name), slug = VALUES(slug);

INSERT INTO permissions (id, name, slug) VALUES (1, 'Admin Access', 'ADMIN_ACCESS')
ON DUPLICATE KEY UPDATE name = VALUES(name), slug = VALUES(slug);
INSERT INTO permissions (id, name, slug) VALUES (2, 'Passenger Access', 'PASSENGER_ACCESS')
ON DUPLICATE KEY UPDATE name = VALUES(name), slug = VALUES(slug);
INSERT INTO permissions (id, name, slug) VALUES (3, 'Staff Access', 'STAFF_ACCESS')
ON DUPLICATE KEY UPDATE name = VALUES(name), slug = VALUES(slug);

INSERT IGNORE INTO role_permission (role_id, permission_id) VALUES (1, 1);
INSERT IGNORE INTO role_permission (role_id, permission_id) VALUES (2, 2);
INSERT IGNORE INTO role_permission (role_id, permission_id) VALUES (3, 3);

INSERT IGNORE INTO users (id, user_name, email, phone, address, password_hash, role_id) VALUES (1, 'admin system', 'admin@gmail.com', '0987654321', 'VN', '$2a$10$jivQED6nsdZyJfBAtzD.seiFE3BnIVb/Numhn8crX4yJHrDF1I4Qa', 1);
INSERT IGNORE INTO users (id, user_name, email, phone, address, password_hash, role_id) VALUES (99, 'staff', 'staff@gmail.com', '0987654321', 'VN', '$2a$10$jivQED6nsdZyJfBAtzD.seiFE3BnIVb/Numhn8crX4yJHrDF1I4Qa', 3);

-- Seed data for locations table (20 locations)
INSERT IGNORE INTO locations (id, name, slug) VALUES (1, 'Hà Nội', 'HN');
INSERT IGNORE INTO locations (id, name, slug) VALUES (2, 'Hồ Chí Minh', 'HCM');
INSERT IGNORE INTO locations (id, name, slug) VALUES (3, 'Đà Nẵng', 'DN');
INSERT IGNORE INTO locations (id, name, slug) VALUES (4, 'Hải Phòng', 'HP');
INSERT IGNORE INTO locations (id, name, slug) VALUES (5, 'Cần Thơ', 'CT');
INSERT IGNORE INTO locations (id, name, slug) VALUES (6, 'Nha Trang', 'NT');
INSERT IGNORE INTO locations (id, name, slug) VALUES (7, 'Huế', 'HUE');
INSERT IGNORE INTO locations (id, name, slug) VALUES (8, 'Vinh', 'VINH');
INSERT IGNORE INTO locations (id, name, slug) VALUES (9, 'Đà Lạt', 'DL');
INSERT IGNORE INTO locations (id, name, slug) VALUES (10, 'Buôn Ma Thuột', 'BMT');
INSERT IGNORE INTO locations (id, name, slug) VALUES (11, 'Quy Nhơn', 'QN');
INSERT IGNORE INTO locations (id, name, slug) VALUES (12, 'Thanh Hóa', 'TH');
INSERT IGNORE INTO locations (id, name, slug) VALUES (13, 'Nam Định', 'ND');
INSERT IGNORE INTO locations (id, name, slug) VALUES (14, 'Vũng Tàu', 'VT');
INSERT IGNORE INTO locations (id, name, slug) VALUES (15, 'Đồng Hới', 'DH');
INSERT IGNORE INTO locations (id, name, slug) VALUES (16, 'Pleiku', 'PLEIKU');
INSERT IGNORE INTO locations (id, name, slug) VALUES (17, 'Long Xuyên', 'LX');
INSERT IGNORE INTO locations (id, name, slug) VALUES (18, 'Mỹ Tho', 'MT');
INSERT IGNORE INTO locations (id, name, slug) VALUES (19, 'Phan Thiết', 'PT');
INSERT IGNORE INTO locations (id, name, slug) VALUES (20, 'Cà Mau', 'CM');

-- Seed data for routes table (20 routes)
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (1, 1, 2, 1700.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (2, 1, 3, 764.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (3, 1, 4, 103.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (4, 2, 3, 964.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (5, 2, 5, 169.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (6, 2, 14, 95.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (7, 3, 7, 103.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (8, 3, 11, 235.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (9, 4, 1, 103.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (10, 5, 2, 169.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (11, 6, 9, 220.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (12, 6, 19, 250.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (13, 7, 8, 210.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (14, 8, 12, 145.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (15, 9, 10, 190.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (16, 10, 16, 180.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (17, 11, 6, 195.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (18, 13, 1, 90.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (19, 17, 5, 40.0);
INSERT IGNORE INTO routes (id, from_id, to_id, distance) VALUES (20, 18, 2, 70.0);
