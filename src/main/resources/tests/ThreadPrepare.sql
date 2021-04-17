TRUNCATE TABLE threads CASCADE ;
TRUNCATE TABLE groups CASCADE ;

INSERT INTO groups(group_id, group_name, creator_id, creation_timestamp)
    VALUES(555, 'GroupNameSQLInsert', 555, '2012-04-23T18:25:43.511Z');

INSERT INTO threads(thread_id, category_id, creation_timestamp, creator_id,
                    last_reply_author_id, last_reply_timestamp, title, group_id)
    VALUES(444, 1, '2012-04-23T18:25:43.511Z', 1, 1, '2012-04-23T18:25:43.511Z', 'ThreadNameSQLInsert', 555);