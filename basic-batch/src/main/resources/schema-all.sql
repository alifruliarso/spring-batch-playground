DROP TABLE customer IF EXISTS;

CREATE TABLE customer  (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);
INSERT INTO customer(first_name, last_name) VALUES('John', 'Doe');