CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

insert into artist(id, name, idx) values(uuid_generate_v4(), 'Adele', 0);
insert into artist(id, name, idx) values(uuid_generate_v4(), 'Beyonce', 1);
insert into artist(id, name, idx) values(uuid_generate_v4(), 'Coldplay', 2);
insert into artist(id, name, idx) values(uuid_generate_v4(), 'Drake', 3);
insert into artist(id, name, idx) values(uuid_generate_v4(), 'Ed Sheeran', 4);
