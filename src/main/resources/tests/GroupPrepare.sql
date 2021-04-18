TRUNCATE TABLE groups CASCADE ;

INSERT INTO groups(group_id, group_name, creator_id, creation_timestamp)
    VALUES(555, 'GroupNameSQLInsert', 555, '2012-04-23T18:25:43.511Z');