-- Schema

DROP TABLE IF EXISTS products;

CREATE TABLE products (
  id         SERIAL PRIMARY KEY,
  name       VARCHAR(30),  
  UNIQUE (name)
);

CREATE INDEX name_product IN products (name)

CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1000 INCREMENT BY 42;

-- Initial data

INSERT INTO products VALUES (10, 'Apple');
