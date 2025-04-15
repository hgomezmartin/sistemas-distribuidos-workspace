CREATE TABLE IF NOT EXISTS books (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL
    );

-- Insert sample data into the books table
INSERT INTO books (title, author) VALUES
                                      ('No me lo preguntes','adivinalo');