DELETE FROM product_options;
DELETE FROM wish;
DELETE FROM product;
DELETE FROM member;

INSERT INTO member (email, password)
VALUES ('user@example.com', 'password1234');


INSERT INTO product (name, price, image_url)
VALUES ('아메리카노 1', 4000, 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=800&q=80'),
       ('카페라떼 1', 4500, 'https://images.unsplash.com/photo-1511920170033-f8396924c348?auto=format&fit=crop&w=800&q=80'),
       ('바닐라 라떼 1', 5000, 'https://images.unsplash.com/photo-1572442388855-4341a444d3b8?auto=format&fit=crop&w=800&q=80'),
       ('카라멜 마끼아또 1', 5500, 'https://images.unsplash.com/photo-1542990253-a781447c1651?auto=format&fit=crop&w=800&q=80'),
       ('에스프레소 1', 3500, 'https://images.unsplash.com/photo-1501339847703-ac7b5788204b?auto=format&fit=crop&w=800&q=80'),
       ('콜드브루 1', 5000, 'https://images.unsplash.com/photo-1517701550927-2b8b9b59aa4b?auto=format&fit=crop&w=800&q=80'),
       ('아메리카노 2', 4000, 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=800&q=80'),
       ('카페라떼 2', 4500, 'https://images.unsplash.com/photo-1511920170033-f8396924c348?auto=format&fit=crop&w=800&q=80'),
       ('바닐라 라떼 2', 5000, 'https://images.unsplash.com/photo-1572442388855-4341a444d3b8?auto=format&fit=crop&w=800&q=80'),
       ('카라멜 마끼아또 2', 5500, 'https://images.unsplash.com/photo-1542990253-a781447c1651?auto=format&fit=crop&w=800&q=80'),
       ('에스프레소 2', 3500, 'https://images.unsplash.com/photo-1501339847703-ac7b5788204b?auto=format&fit=crop&w=800&q=80'),
       ('콜드브루 2', 5000, 'https://images.unsplash.com/photo-1517701550927-2b8b9b59aa4b?auto=format&fit=crop&w=800&q=80'),
       ('아메리카노 3', 4000, 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=800&q=80'),
       ('카페라떼 3', 4500, 'https://images.unsplash.com/photo-1511920170033-f8396924c348?auto=format&fit=crop&w=800&q=80'),
       ('바닐라 라떼 3', 5000, 'https://images.unsplash.com/photo-1572442388855-4341a444d3b8?auto=format&fit=crop&w=800&q=80'),
       ('카라멜 마끼아또 3', 5500, 'https://images.unsplash.com/photo-1542990253-a781447c1651?auto=format&fit=crop&w=800&q=80'),
       ('에스프레소 3', 3500, 'https://images.unsplash.com/photo-1501339847703-ac7b5788204b?auto=format&fit=crop&w=800&q=80'),
       ('콜드브루 3', 5000, 'https://images.unsplash.com/photo-1517701550927-2b8b9b59aa4b?auto=format&fit=crop&w=800&q=80'),
       ('아메리카노 4', 4000, 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=800&q=80'),
       ('카페라떼 4', 4500, 'https://images.unsplash.com/photo-1511920170033-f8396924c348?auto=format&fit=crop&w=800&q=80'),
       ('바닐라 라떼 4', 5000, 'https://images.unsplash.com/photo-1572442388855-4341a444d3b8?auto=format&fit=crop&w=800&q=80'),
       ('카라멜 마끼아또 4', 5500, 'https://images.unsplash.com/photo-1542990253-a781447c1651?auto=format&fit=crop&w=800&q=80'),
       ('에스프레소 4', 3500, 'https://images.unsplash.com/photo-1501339847703-ac7b5788204b?auto=format&fit=crop&w=800&q=80'),
       ('콜드브루 4', 5000, 'https://images.unsplash.com/photo-1517701550927-2b8b9b59aa4b?auto=format&fit=crop&w=800&q=80'),
       ('아메리카노 5', 4000, 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=800&q=80'),
       ('카페라떼 5', 4500, 'https://images.unsplash.com/photo-1511920170033-f8396924c348?auto=format&fit=crop&w=800&q=80'),
       ('바닐라 라떼 5', 5000, 'https://images.unsplash.com/photo-1572442388855-4341a444d3b8?auto=format&fit=crop&w=800&q=80'),
       ('카라멜 마끼아또 5', 5500, 'https://images.unsplash.com/photo-1542990253-a781447c1651?auto=format&fit=crop&w=800&q=80'),
       ('에스프레소 5', 3500, 'https://images.unsplash.com/photo-1501339847703-ac7b5788204b?auto=format&fit=crop&w=800&q=80'),
       ('콜드브루 5', 5000, 'https://images.unsplash.com/photo-1517701550927-2b8b9b59aa4b?auto=format&fit=crop&w=800&q=80');

INSERT INTO product_options (product_id, name, quantity)
VALUES (1, '기본', 100),
       (2, '기본', 100),
       (3, '기본', 100),
       (4, '기본', 100),
       (5, '기본', 100),
       (6, '기본', 100),
       (7, '기본', 100),
       (8, '기본', 100),
       (9, '기본', 100),
       (10, '기본', 100),
       (11, '기본', 100),
       (12, '기본', 100),
       (13, '기본', 100),
       (14, '기본', 100),
       (15, '기본', 100),
       (16, '기본', 100),
       (17, '기본', 100),
       (18, '기본', 100),
       (19, '기본', 100),
       (20, '기본', 100),
       (21, '기본', 100),
       (22, '기본', 100),
       (23, '기본', 100),
       (24, '기본', 100),
       (25, '기본', 100),
       (26, '기본', 100),
       (27, '기본', 100),
       (28, '기본', 100),
       (29, '기본', 100),
       (30, '기본', 100);

INSERT INTO product_options (product_id, name, quantity)
VALUES (1, '샷 추가', 50),
       (2, '두유로 변경', 50),
       (3, '시럽 추가', 50);

INSERT INTO wish (member_id, product_id)
VALUES (1, 1),
       (1, 2);
