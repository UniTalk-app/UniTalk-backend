TRUNCATE TABLE users CASCADE;

insert into users (id, email, first_name, last_name, password, username)
values (1, 'email@email', 'first', 'last', '$2a$10$xPnL.Pddsox2oZGnGzKESOpg1EhOvBd6Fc0XfnyMW4bHY24zsFmi2', 'rafal');