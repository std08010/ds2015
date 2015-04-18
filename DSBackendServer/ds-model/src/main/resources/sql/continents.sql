DELETE FROM continent;

DROP INDEX IF EXISTS continent_name_idx;

ALTER SEQUENCE continent_id_seq RESTART WITH 1;
UPDATE continent SET id = DEFAULT;

CREATE INDEX continent_name_idx ON continent (name ASC);

INSERT INTO continent(id, active, code, comment, descr, inserted_at, updated_at, version_number, name) VALUES (nextval('continent_id_seq'), true, NULL, NULL, NULL, '2015-02-07 14:40:06.214', '2015-02-07 14:40:06.214', 0, 'Europe');
INSERT INTO continent(id, active, code, comment, descr, inserted_at, updated_at, version_number, name) VALUES (nextval('continent_id_seq'), true, NULL, NULL, NULL, '2015-02-07 14:40:06.214', '2015-02-07 14:40:06.214', 0, 'Africa');
INSERT INTO continent(id, active, code, comment, descr, inserted_at, updated_at, version_number, name) VALUES (nextval('continent_id_seq'), true, NULL, NULL, NULL, '2015-02-07 14:40:06.214', '2015-02-07 14:40:06.214', 0, 'North America');
INSERT INTO continent(id, active, code, comment, descr, inserted_at, updated_at, version_number, name) VALUES (nextval('continent_id_seq'), true, NULL, NULL, NULL, '2015-02-07 14:40:06.214', '2015-02-07 14:40:06.214', 0, 'South America');
INSERT INTO continent(id, active, code, comment, descr, inserted_at, updated_at, version_number, name) VALUES (nextval('continent_id_seq'), true, NULL, NULL, NULL, '2015-02-07 14:40:06.214', '2015-02-07 14:40:06.214', 0, 'Asia');
INSERT INTO continent(id, active, code, comment, descr, inserted_at, updated_at, version_number, name) VALUES (nextval('continent_id_seq'), true, NULL, NULL, NULL, '2015-02-07 14:40:06.214', '2015-02-07 14:40:06.214', 0, 'Oceania');
INSERT INTO continent(id, active, code, comment, descr, inserted_at, updated_at, version_number, name) VALUES (nextval('continent_id_seq'), true, NULL, NULL, NULL, '2015-02-07 14:40:06.214', '2015-02-07 14:40:06.214', 0, 'Antarctica');
