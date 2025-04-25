DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
    );

INSERT INTO users(username, password, email) VALUES
    ('Peter','pass123', 'peter@ubu.es' ),
    ('Celio','celio123','celio23@alu.ubu.es'),
    ('Fernando', 'torres2009','Torresprime2009@ubu.es'),
    ('Rodolfo', 'rodolfo123','chiquilicuatre@ubu.es');