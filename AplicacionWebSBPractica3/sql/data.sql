-- USUARIOS (contraseñas cifradas con BCrypt)
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL
    );


-- MERCHANDISE
DROP TABLE IF EXISTS product;

CREATE TABLE IF NOT EXISTS product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(255),
    price DECIMAL(10,2),
    image_url VARCHAR(255)
    );

INSERT INTO product(name, description, price, image_url) VALUES
    ('Camiseta Bandito', 'T-shirt oficial', 25.00, '/images/bandito.jpg'),
    ('Sudadera TØP',     'Hoodie logo',   45.00, '/images/hoodie.jpg');



-- TICKETS
DROP TABLE IF EXISTS ticket;

CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(50),
    date DATE,
    price DECIMAL(10,2)
    );

INSERT INTO ticket(city, date, price) VALUES
    ('Madrid',    '2025-10-01',  49.00),
    ('Barcelona', '2025-10-02',  49.00);