INSERT INTO users (id, name, email) VALUES (2, 'Username2', 'user2@shareit.ru');

INSERT INTO users (id, name, email) VALUES (3, 'Username3', 'user3@shareit.ru');

INSERT INTO users (id, name, email) VALUES (4, 'Username4', 'user4@shareit.ru');

INSERT INTO requests (id, description, created, requestor_id) VALUES (2, 'Request description 2', '2025-03-01 21:22:23', 3);

INSERT INTO requests (id, description, created, requestor_id) VALUES (3, 'Request description 3', '2025-03-03 00:00:00', 4);

INSERT INTO requests (id, description, created, requestor_id) VALUES (4, 'Request description 4', '2025-03-04 00:00:00', 4);

INSERT INTO items (id, name, description, is_available, owner_id, request_id) VALUES (2, 'Item2', 'Description2', 'true', 2, 2);

INSERT INTO items (id, name, description, is_available, owner_id, request_id) VALUES (3, 'Item3', 'Описание', 'false', 2, null);

INSERT INTO items (id, name, description, is_available, owner_id, request_id) VALUES (4, 'Item4', 'Description4', 'true', 2, null);

INSERT INTO bookings (id, start_date_time, end_date_time, item_id, booker_id, status) VALUES (2, '2025-03-28 00:00:01', '2025-04-01 00:00:01', 2, 2, 'WAITING');

INSERT INTO bookings (id, start_date_time, end_date_time, item_id, booker_id, status) VALUES (3, '2025-01-01 00:00:01', '2025-02-01 00:00:01', 2, 3, 'CANCELED');

INSERT INTO bookings (id, start_date_time, end_date_time, item_id, booker_id, status) VALUES (4, '2025-03-05 00:00:01', '2025-03-12 00:00:01', 3, 2, 'APPROVED');

INSERT INTO comments (id, text, item_id, author_id, created) VALUES (2, 'comment2', 2, 3, '2025-03-10 00:00:01');

INSERT INTO comments (id, text, item_id, author_id, created) VALUES (3, 'comment3', 3, 3, '2025-03-11 00:00:01');

INSERT INTO comments (id, text, item_id, author_id, created) VALUES (4, 'comment4', 3, 2, '2025-03-12 00:00:01');