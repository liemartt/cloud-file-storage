drop table if exists users;
CREATE TABLE users
(
    id       INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE INDEX username_index ON users (username);

ALTER TABLE users
    ADD CONSTRAINT unique_username UNIQUE (username);
