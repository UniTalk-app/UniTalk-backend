TRUNCATE TABLE groups CASCADE ;
TRUNCATE TABLE categories CASCADE ;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE threads CASCADE ;

-- USER

INSERT INTO users (id, email, first_name, last_name, password, username)
VALUES (10, 'email@email', 'first', 'last', '$2a$10$xPnL.Pddsox2oZGnGzKESOpg1EhOvBd6Fc0XfnyMW4bHY24zsFmi2', 'testuser');

-- GROUP

INSERT INTO groups(group_id, group_name, creator_id, creation_timestamp)
VALUES(10, 'GroupTitleTest', 10, '2012-04-23T18:25:43.511Z');

INSERT INTO groups(group_id, group_name, creator_id, creation_timestamp)
VALUES(11, 'GroupTitleTestTwo', 11, '2012-04-23T18:25:43.511Z');

-- CATEGORY

INSERT INTO categories(category_id, name,group_id, creation_timestamp)
VALUES(10,'CategoryTitleTest', 10,'2012-04-23T18:25:43.511Z');

INSERT INTO categories(category_id, name,group_id, creation_timestamp)
VALUES(11,'CategoryTitleTest', 11,'2013-04-23T18:25:43.511Z');

-- THREAD

INSERT INTO threads(thread_id, category_id, creation_timestamp, creator_id,
                    last_reply_author_id, last_reply_timestamp, title, group_id)
VALUES(10, 10, '2012-04-23T18:25:43.511Z', 10, 10, '2012-04-23T18:25:43.511Z', 'ThreadTitleTest', 10);

