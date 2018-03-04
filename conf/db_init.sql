DROP TABLE posts;
DROP TABLE users;

CREATE TABLE users (
  user_id       BIGSERIAL PRIMARY KEY,
  name          VARCHAR(255) NOT NULL,
  creation_date TIMESTAMP    NOT NULL,
  note          VARCHAR(255) NOT NULL
);

CREATE TABLE posts (
  post_id       SERIAL  PRIMARY KEY,
  content       VARCHAR(1000) NOT NULL,
  creation_date TIMESTAMP     NOT NULL,
  author        BIGINT  NOT NULL REFERENCES users (user_id)
);