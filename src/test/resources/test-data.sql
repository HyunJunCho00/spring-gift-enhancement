DELETE FROM wish;
DELETE FROM products;
DELETE FROM member;

INSERT INTO member (id, email, password) VALUES (1, 'userA@example.com', 'hashed_password');
INSERT INTO member (id, email, password) VALUES (2, 'userB@example.com', 'hashed_password');
INSERT INTO products (id, name, price, image_url) VALUES (101, '상품A', 1000, 'urlA');
INSERT INTO products (id, name, price, image_url) VALUES (102, '상품B', 2000, 'urlB');
INSERT INTO wish (id, member_id, product_id) VALUES (1001, 1, 101);