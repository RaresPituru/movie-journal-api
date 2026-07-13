CREATE TABLE IF NOT EXISTS Users (
id INT NOT NULL,
username varchar(100) NOT NULL UNIQUE,
password varchar(250) NOT NULL,
role varchar(50) NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Movie (
id INT NOT NULL,
title varchar(250) NOT NULL,
rating INT NOT NULL,
review varchar(250) NOT NULL,
release_year INT NOT NULL,
user_id INT NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES Users(id)
);
