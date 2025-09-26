BEGIN;

DROP TABLE IF EXISTS loan_books;
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS authors;

-- Table: authors
CREATE TABLE IF NOT EXISTS authors (
    author_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_year INTEGER,
    nationality VARCHAR(100)
);

-- Table: books
CREATE TABLE IF NOT EXISTS books (
    book_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    publication_year INTEGER,
    available_copies INTEGER DEFAULT 1,
    total_copies INTEGER DEFAULT 1,
    author_id BIGINT,

    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Table: users
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    registration_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    failed_login_attempts INT NOT NULL DEFAULT 0,
    lock_until TIMESTAMP
);

-- Table: loans
CREATE TABLE IF NOT EXISTS loans (
    loan_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    borrowed_date DATETIME NOT NULL,
    due_date DATETIME NOT NULL,
    returned_date DATETIME,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Table: loan_books
CREATE TABLE IF NOT EXISTS loan_books (
    loan_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    PRIMARY KEY (loan_id, book_id),

    FOREIGN KEY (loan_id) REFERENCES loans(loan_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
);

COMMIT;
