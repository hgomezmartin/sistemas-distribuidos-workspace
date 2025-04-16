DROP TABLE IF EXISTS books; --por el ejmplo
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
    );

INSERT INTO users(username, password, email) VALUES
    ('Peter','Peter123456', 'peter@ubu.es' ),
    ('Celio','Celio123456','celio23@alu.ubu.es'),
    ('Fernando', 'FernandoTorres2009','Torresprime2009@ubu.es'),
    ('Rodolfo', 'RodolfoEurovision123456','chiquilicuatre@ubu.es');