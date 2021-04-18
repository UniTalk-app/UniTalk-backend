TRUNCATE TABLE categories CASCADE ;
TRUNCATE TABLE groups CASCADE ;

INSERT INTO groups(group_id, group_name, creator_id, creation_timestamp)
    VALUES(555, 'GroupNameSQLInsert', 555, '2012-04-23T18:25:43.511Z');

INSERT INTO categories(id, name,group_id, creation_timestamp)
    VALUES(44,'Cat1', 555,'2012-04-23T18:25:43.511Z');

INSERT INTO categories(id, name,group_id, creation_timestamp)
    VALUES(55,'Cat2', 555,'2013-04-23T18:25:43.511Z');