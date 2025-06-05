-- USUARIOS (contraseñas cifradas con BCrypt)
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL
    );

INSERT INTO users(id, username, password, email, role) VALUES
    (1, 'admin', '$2a$10$kR6lCj2wx2Q3S9d2taHBjO7uTUcTLONYJtub34f5evMHzGwtB/5Ni', 'admin@top.com', 'ROLE_ADMIN');


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
    ('Sudadera Bandito', 'Official Hoodie', 55.99, '/images/bandito.png'),
    ('Sudadera TOP',     'Official Hoodie logo',   50.99, '/images/TOP.png'),
    ('Sudadera Trench',     'Official Hoodie tench',   59.99, '/images/trench.png'),
    ('Burryface CD',     'Official Burryface CD',   10.99, '/images/blurrycd.jpg'),
    ('Trench CD',     'Official Trench CD',   12.99, '/images/trenchcd.jpg'),
    ('Scaled And Icy CD',     'Official SAI CD',   12.99, '/images/saicd.webp');




-- TICKETS
DROP TABLE IF EXISTS ticket;

CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(50),
    date DATE,
    price DECIMAL(10,2)
    );

INSERT INTO ticket(city, date, price) VALUES
    ('Madrid - Movistar Arena',    '2025-10-01',  49.00),
    ('Barcelona - Palau Sant Jordi', '2025-10-02',  49.00),
    ('Munich - Olympiahalle', '2025-10-05',  49.00),
    ('Milan - Forum di Milano', '2025-10-07',  49.00),
    ('Amsterdam - Palau Sant Jordi', '2025-10-10',  49.00),
    ('Koln - Lanxess Arena', '2025-10-12',  49.00),
    ('Paris - Accor Arena', '2025-10-13',  49.00),
    ('Prague - O2 Arena', '2025-10-16',  49.00),
    ('Vienna - Wiener Stadthalle', '2025-10-17',  49.00),
    ('Zurich - Hallenstadion', '2025-10-19',  49.00);
