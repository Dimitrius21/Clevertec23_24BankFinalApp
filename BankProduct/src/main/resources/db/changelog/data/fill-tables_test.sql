--liquibase formatted sql
--changeset group1:fill_tables contextFilter:test

INSERT INTO news (time, title, text, username) VALUES ('2023-05-29 00:00', 'New BMW', 'Presented new BMW 5', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment11', 'Jon', 1),
   	(localtimestamp + interval '10 second', 'Comment12', 'Sema', 1),
   	(localtimestamp + interval '20 second', 'Comment13', 'Jon', 1),
   	(localtimestamp + interval '30 second', 'Comment14', 'Alice', 1),
   	(localtimestamp + interval '40 second', 'Comment15', 'Alice', 1),
   	(localtimestamp + interval '50 second', 'Comment16', 'Serge', 1),
   	(localtimestamp + interval '60 second', 'Comment17', 'Ivan', 1),
   	(localtimestamp + interval '70 second', 'Comment18', 'Jon', 1),
   	(localtimestamp + interval '80 second', 'Comment19', 'Maks', 1),
    (localtimestamp + interval '90 second', 'Comment110', 'Anton', 1),
    ('2023-05-30 00:00', 'Comment120', 'Maria', 1);

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Sport. Football. Italy', 'Napoli won Serie A', 'David');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment21', 'Jon', 2),
   	(localtimestamp + interval '10 second', 'Comment22', 'Sema', 2),
   	(localtimestamp + interval '20 second', 'Comment23', 'Jon', 2),
   	(localtimestamp + interval '30 second', 'Comment24', 'Alice', 2),
   	(localtimestamp + interval '40 second', 'Comment25', 'Alice', 2),
   	(localtimestamp + interval '50 second', 'Comment26', 'Serge', 2),
   	(localtimestamp + interval '60 second', 'Comment27', 'Ivan', 2),
   	(localtimestamp + interval '70 second', 'Comment28', 'Jon', 2),
   	(localtimestamp + interval '80 second', 'Comment29', 'Maks', 2),
    (localtimestamp + interval '90 second', 'Comment210', 'Anton', 2);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Upgraded Toyota Yaris', 'An upgraded Toyota Yaris is presented in Europe', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment31', 'Jon', 3),
   	(localtimestamp + interval '10 second', 'Comment32', 'Sema', 3),
   	(localtimestamp + interval '20 second', 'Comment33', 'Jon', 3),
   	(localtimestamp + interval '30 second', 'Comment34', 'Alice', 3),
   	(localtimestamp + interval '40 second', 'Comment35', 'Alice', 3),
   	(localtimestamp + interval '50 second', 'Comment36', 'Serge', 3),
   	(localtimestamp + interval '60 second', 'Comment37', 'Ivan', 3),
   	(localtimestamp + interval '70 second', 'Comment38', 'Jon', 3),
   	(localtimestamp + interval '80 second', 'Comment39', 'Maks', 3),
    (localtimestamp + interval '90 second', 'Comment310', 'Anton', 3);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'The Cheapest tickets', 'Ryanair is going to cancel ticket by 9.99', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment41', 'Jon', 4),
   	(localtimestamp + interval '10 second', 'Comment42', 'Sema', 4),
   	(localtimestamp + interval '20 second', 'Comment43', 'Jon', 4),
   	(localtimestamp + interval '30 second', 'Comment44', 'Alice', 4),
   	(localtimestamp + interval '40 second', 'Comment45', 'Alice', 4),
   	(localtimestamp + interval '50 second', 'Comment46', 'Serge', 4),
   	(localtimestamp + interval '60 second', 'Comment47', 'Ivan', 4),
   	(localtimestamp + interval '70 second', 'Comment48', 'Jon', 4),
   	(localtimestamp + interval '80 second', 'Comment49', 'Maks', 4),
    (localtimestamp + interval '90 second', 'Comment410', 'Anton', 4);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event5', 'News5', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment51', 'Jon', 5),
   	(localtimestamp + interval '10 second', 'Comment52', 'Sema', 5),
   	(localtimestamp + interval '20 second', 'Comment53', 'Jon', 5),
   	(localtimestamp + interval '30 second', 'Comment54', 'Alice', 5),
   	(localtimestamp + interval '40 second', 'Comment55', 'Alice', 5),
   	(localtimestamp + interval '50 second', 'Comment56', 'Serge', 5),
   	(localtimestamp + interval '60 second', 'Comment57', 'Ivan', 5),
   	(localtimestamp + interval '70 second', 'Comment58', 'Jon', 5),
   	(localtimestamp + interval '80 second', 'Comment59', 'Maks', 5),
    (localtimestamp + interval '90 second', 'Comment510', 'Anton', 5);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event6', 'News6', 'David');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment61', 'Jon', 6),
   	(localtimestamp + interval '10 second', 'Comment62', 'Sema', 6),
   	(localtimestamp + interval '20 second', 'Comment63', 'Jon', 6),
   	(localtimestamp + interval '30 second', 'Comment64', 'Alice', 6),
   	(localtimestamp + interval '40 second', 'Comment65', 'Alice', 6),
   	(localtimestamp + interval '50 second', 'Comment66', 'Serge', 6),
   	(localtimestamp + interval '60 second', 'Comment67', 'Ivan', 6),
   	(localtimestamp + interval '70 second', 'Comment68', 'Jon', 6),
   	(localtimestamp + interval '80 second', 'Comment69', 'Maks', 6),
    (localtimestamp + interval '90 second', 'Comment610', 'Anton', 6);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event7', 'News7', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment71', 'Jon', 7),
   	(localtimestamp + interval '10 second', 'Comment72', 'Sema', 7),
   	(localtimestamp + interval '20 second', 'Comment73', 'Jon', 7),
   	(localtimestamp + interval '30 second', 'Comment74', 'Alice', 7),
   	(localtimestamp + interval '40 second', 'Comment75', 'Alice', 7),
   	(localtimestamp + interval '50 second', 'Comment76', 'Serge', 7),
   	(localtimestamp + interval '60 second', 'Comment77', 'Ivan', 7),
   	(localtimestamp + interval '70 second', 'Comment78', 'Jon', 7),
   	(localtimestamp + interval '80 second', 'Comment79', 'Maks', 7),
    (localtimestamp + interval '90 second', 'Comment710', 'Anton', 7);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event8', 'News8', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment81', 'Jon', 8),
   	(localtimestamp + interval '10 second', 'Comment82', 'Sema', 8),
   	(localtimestamp + interval '20 second', 'Comment83', 'Jon', 8),
   	(localtimestamp + interval '30 second', 'Comment84', 'Alice', 8),
   	(localtimestamp + interval '40 second', 'Comment85', 'Alice', 8),
   	(localtimestamp + interval '50 second', 'Comment86', 'Serge', 8),
   	(localtimestamp + interval '60 second', 'Comment87', 'Ivan', 8),
   	(localtimestamp + interval '70 second', 'Comment88', 'Jon', 8),
   	(localtimestamp + interval '80 second', 'Comment89', 'Maks', 8),
    (localtimestamp + interval '90 second', 'Comment810', 'Anton', 8);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event9', 'News9', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment91', 'Jon', 9),
   	(localtimestamp + interval '10 second', 'Comment92', 'Sema', 9),
   	(localtimestamp + interval '20 second', 'Comment93', 'Jon', 9),
   	(localtimestamp + interval '30 second', 'Comment94', 'Alice', 9),
   	(localtimestamp + interval '40 second', 'Comment95', 'Alice', 9),
   	(localtimestamp + interval '50 second', 'Comment96', 'Serge', 9),
   	(localtimestamp + interval '60 second', 'Comment97', 'Ivan', 9),
   	(localtimestamp + interval '70 second', 'Comment98', 'Jon', 9),
   	(localtimestamp + interval '80 second', 'Comment99', 'Maks', 9),
    (localtimestamp + interval '90 second', 'Comment910', 'Anton', 9),
    (localtimestamp + interval '100 second', 'Comment911', 'Elen', 9);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event10', 'News10', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment101', 'Jon', 10),
   	(localtimestamp + interval '10 second', 'Comment102', 'Sema', 10),
   	(localtimestamp + interval '20 second', 'Comment103', 'Jon', 10),
   	(localtimestamp + interval '30 second', 'Comment104', 'Alice', 10),
   	(localtimestamp + interval '40 second', 'Comment105', 'Alice', 10),
   	(localtimestamp + interval '50 second', 'Comment106', 'Serge', 10),
   	(localtimestamp + interval '60 second', 'Comment107', 'Ivan', 10),
   	(localtimestamp + interval '70 second', 'Comment108', 'Jon', 10),
   	(localtimestamp + interval '80 second', 'Comment109', 'Maks', 10),
    (localtimestamp + interval '90 second', 'Comment1010', 'Anton', 10);