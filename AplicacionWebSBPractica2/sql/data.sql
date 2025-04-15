CREATE TABLE IF NOT EXISTS books (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL
    );

-- Insert sample data into the books table
INSERT INTO books (title, author) VALUES
                                      ('To Kill a Mockingbird', 'Harper Lee'),
                                      ('1984', 'George Orwell'),
                                      ('The Great Gatsby', 'F. Scott Fitzgerald'),
                                      ('Pride and Prejudice', 'Jane Austen'),
                                      ('The Catcher in the Rye', 'J.D. Salinger');