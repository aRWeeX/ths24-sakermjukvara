--
-- File generated with SQLiteStudio v3.4.17 on fre maj 16 14:50:42 2025
--
-- Text encoding used: System
--
BEGIN TRANSACTION;

-- Table: authors
CREATE TABLE IF NOT EXISTS authors (
    author_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_year INTEGER,
    nationality VARCHAR(100)
);
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (1, 'Astrid', 'Lindgren', 1907, 'Swedish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (2, 'Stieg', 'Larsson', 1954, 'Swedish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (3, 'Camilla', 'L�ckberg', 1974, 'Swedish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (4, 'Henning', 'Mankell', 1948, 'Swedish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (5, 'Selma', 'Lagerl�f', 1858, 'Swedish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (6, 'August', 'Strindberg', 1849, 'Swedish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (7, 'P�r', 'Lagerkvist', 1891, 'Swedish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (8, 'Vilhelm', 'Moberg', 1898, 'Swedish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (9, 'Hjalmar', 'S�derberg', 1869, 'Swedish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (10, 'Tove', 'Jansson', 1914, 'Finnish-Swedish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (11, 'Agatha', 'Christie', 1890, 'British');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (12, 'Arthur Conan', 'Doyle', 1859, 'British');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (13, 'J.K.', 'Rowling', 1965, 'British');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (14, 'George', 'Orwell', 1903, 'British');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (15, 'Jane', 'Austen', 1775, 'British');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (16, 'Charles', 'Dickens', 1812, 'British');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (17, 'Virginia', 'Woolf', 1882, 'British');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (18, 'Oscar', 'Wilde', 1854, 'Irish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (19, 'James', 'Joyce', 1882, 'Irish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (20, 'Bram', 'Stoker', 1847, 'Irish');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (21, 'Leo', 'Tolstoy', 1828, 'Russian');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (22, 'Fyodor', 'Dostoevsky', 1821, 'Russian');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (23, 'Anton', 'Chekhov', 1860, 'Russian');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (24, 'Vladimir', 'Nabokov', 1899, 'Russian-American');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (25, 'Ernest', 'Hemingway', 1899, 'American');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (26, 'Mark', 'Twain', 1835, 'American');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (27, 'F. Scott', 'Fitzgerald', 1896, 'American');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (28, 'Harper', 'Lee', 1926, 'American');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (29, 'John', 'Steinbeck', 1902, 'American');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (30, 'Kurt', 'Vonnegut', 1922, 'American');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (31, 'Gabriel Garc�a', 'M�rquez', 1927, 'Colombian');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (32, 'Jorge Luis', 'Borges', 1899, 'Argentine');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (33, 'Paulo', 'Coelho', 1947, 'Brazilian');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (34, 'Isabel', 'Allende', 1942, 'Chilean');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (35, 'Milan', 'Kundera', 1929, 'Czech');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (36, 'Franz', 'Kafka', 1883, 'Czech');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (37, 'Hermann', 'Hesse', 1877, 'German');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (38, 'Thomas', 'Mann', 1875, 'German');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (39, 'Umberto', 'Eco', 1932, 'Italian');
INSERT INTO authors (author_id, first_name, last_name, birth_year, nationality) VALUES (40, 'Haruki', 'Murakami', 1949, 'Japanese');

-- Table: books
CREATE TABLE IF NOT EXISTS books (
    book_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    publication_year INTEGER,
    available_copies INTEGER DEFAULT 1,
    total_copies INTEGER DEFAULT 1,
    author_id INTEGER,
    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE SET NULL ON UPDATE CASCADE
);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (1, 'Pippi Longstocking', 1945, 2, 3, 1);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (2, 'The Girl with the Dragon Tattoo', 2005, 1, 2, 2);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (3, 'The Ice Princess', 2003, 2, 2, 3);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (4, 'Faceless Killers', 1991, 1, 2, 4);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (5, 'The Wonderful Adventures of Nils', 1906, 3, 3, 5);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (6, 'The Red Room', 1879, 2, 2, 6);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (7, 'Barabbas', 1950, 1, 1, 7);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (8, 'The Emigrants', 1949, 2, 2, 8);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (9, 'Doctor Glas', 1905, 1, 1, 9);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (10, 'The Moomins and the Great Flood', 1945, 2, 2, 10);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (11, 'Murder on the Orient Express', 1934, 1, 2, 11);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (12, 'The Adventure of Sherlock Holmes', 1892, 2, 3, 12);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (13, 'Harry Potter and the Philosopher''s Stone', 1997, 0, 2, 13);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (14, '1984', 1949, 1, 2, 14);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (15, 'Pride and Prejudice', 1813, 2, 2, 15);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (16, 'Great Expectations', 1861, 1, 2, 16);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (17, 'To the Lighthouse', 1927, 1, 1, 17);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (18, 'The Picture of Dorian Gray', 1890, 1, 1, 18);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (19, 'Ulysses', 1922, 1, 1, 19);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (20, 'Dracula', 1897, 1, 2, 20);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (21, 'War and Peace', 1869, 1, 1, 21);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (22, 'Crime and Punishment', 1866, 1, 2, 22);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (23, 'The Cherry Orchard', 1904, 1, 1, 23);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (24, 'Lolita', 1955, 1, 1, 24);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (25, 'The Old Man and the Sea', 1952, 1, 2, 25);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (26, 'Adventures of Huckleberry Finn', 1884, 2, 2, 26);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (27, 'The Great Gatsby', 1925, 0, 2, 27);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (28, 'To Kill a Mockingbird', 1960, 1, 2, 28);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (29, 'The Grapes of Wrath', 1939, 1, 1, 29);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (30, 'Slaughterhouse-Five', 1969, 1, 1, 30);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (31, 'One Hundred Years of Solitude', 1967, 1, 1, 31);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (32, 'Labyrinths', 1962, 1, 1, 32);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (33, 'The Alchemist', 1988, 2, 2, 33);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (34, 'The House of the Spirits', 1982, 1, 1, 34);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (35, 'The Unbearable Lightness of Being', 1984, 1, 1, 35);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (36, 'The Metamorphosis', 1915, 1, 2, 36);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (37, 'Steppenwolf', 1927, 1, 1, 37);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (38, 'The Magic Mountain', 1924, 1, 1, 38);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (39, 'The Name of the Rose', 1980, 1, 1, 39);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (40, 'Norwegian Wood', 1987, 1, 2, 40);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (41, 'The Girl Who Played with Fire', 2006, 1, 1, 2);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (42, 'The Preacher', 2004, 1, 1, 3);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (43, 'The White Lioness', 1993, 1, 1, 4);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (44, 'G�sta Berling''s Saga', 1891, 1, 1, 5);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (45, 'Miss Julie', 1888, 1, 1, 6);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (46, 'The Dwarf', 1944, 1, 1, 7);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (47, 'The Last Letter', 1959, 1, 1, 8);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (48, 'Gertrud', 1906, 1, 1, 9);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (49, 'Finn Family Moomintroll', 1948, 1, 1, 10);
INSERT INTO books (book_id, title, publication_year, available_copies, total_copies, author_id) VALUES (50, 'And Then There Were None', 1939, 1, 1, 11);

-- Table: loans
CREATE TABLE IF NOT EXISTS loans (
    loan_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    borrowed_date DATETIME NOT NULL,
    due_date DATETIME NOT NULL,
    returned_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE RESTRICT ON UPDATE CASCADE
);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (1, 1, 1, '2024-01-15 10:00:00', '2024-01-29 10:00:00', '2024-01-25 14:30:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (2, 2, 5, '2024-01-20 11:00:00', '2024-02-03 11:00:00', '2024-02-01 09:15:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (3, 3, 8, '2024-01-25 09:30:00', '2024-02-08 09:30:00', '2024-02-05 16:45:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (4, 4, 12, '2024-02-01 14:00:00', '2024-02-15 14:00:00', '2024-02-12 11:20:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (5, 5, 15, '2024-02-05 13:30:00', '2024-02-19 13:30:00', '2024-02-18 10:00:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (6, 6, 20, '2024-02-10 08:45:00', '2024-02-24 08:45:00', '2024-02-22 15:30:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (7, 7, 25, '2024-02-15 16:15:00', '2024-03-01 16:15:00', '2024-02-28 12:45:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (8, 8, 30, '2024-02-20 10:30:00', '2024-03-06 10:30:00', '2024-03-04 14:20:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (9, 9, 35, '2024-02-25 12:00:00', '2024-03-11 12:00:00', '2024-03-09 09:30:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (10, 10, 40, '2024-03-01 15:45:00', '2024-03-15 15:45:00', '2024-03-13 11:15:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (11, 1, 2, '2024-03-10 09:00:00', '2024-03-24 09:00:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (12, 2, 7, '2024-03-12 14:30:00', '2024-03-26 14:30:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (13, 3, 11, '2024-03-15 11:15:00', '2024-03-29 11:15:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (14, 4, 16, '2024-03-18 13:45:00', '2024-04-01 13:45:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (15, 5, 21, '2024-03-20 10:30:00', '2024-04-03 10:30:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (16, 6, 26, '2024-03-22 15:00:00', '2024-04-05 15:00:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (17, 7, 31, '2024-03-24 12:30:00', '2024-04-07 12:30:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (18, 8, 36, '2024-03-26 09:45:00', '2024-04-09 09:45:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (19, 9, 41, '2024-03-28 14:00:00', '2024-04-11 14:00:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (20, 10, 45, '2024-03-30 11:30:00', '2024-04-13 11:30:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (21, 1, 3, '2024-02-05 08:30:00', '2024-02-19 08:30:00', '2024-02-17 13:00:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (22, 2, 6, '2024-02-08 10:15:00', '2024-02-22 10:15:00', '2024-02-20 15:30:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (23, 3, 9, '2024-02-12 13:45:00', '2024-02-26 13:45:00', '2024-02-24 09:45:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (24, 4, 14, '2024-02-15 09:00:00', '2024-03-01 09:00:00', '2024-02-28 14:15:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (25, 5, 17, '2024-02-18 14:30:00', '2024-03-04 14:30:00', '2024-03-02 11:00:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (26, 6, 22, '2024-02-20 11:45:00', '2024-03-06 11:45:00', '2024-03-04 16:30:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (27, 7, 24, '2024-02-22 16:00:00', '2024-03-08 16:00:00', '2024-03-06 12:15:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (28, 8, 28, '2024-02-25 12:15:00', '2024-03-11 12:15:00', '2024-03-09 10:45:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (29, 9, 33, '2024-02-28 10:00:00', '2024-03-14 10:00:00', '2024-03-12 15:20:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (30, 10, 38, '2024-03-02 15:30:00', '2024-03-16 15:30:00', '2024-03-14 09:30:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (31, 1, 13, '2024-03-31 10:00:00', '2024-04-14 10:00:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (32, 2, 27, '2024-03-31 11:30:00', '2024-04-14 11:30:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (33, 3, 4, '2024-04-01 09:15:00', '2024-04-15 09:15:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (34, 4, 18, '2024-04-01 14:45:00', '2024-04-15 14:45:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (35, 5, 19, '2024-04-02 12:00:00', '2024-04-16 12:00:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (36, 6, 23, '2024-04-02 16:30:00', '2024-04-16 16:30:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (37, 7, 29, '2024-04-03 11:00:00', '2024-04-17 11:00:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (38, 8, 32, '2024-04-03 15:15:00', '2024-04-17 15:15:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (39, 9, 34, '2024-04-04 13:30:00', '2024-04-18 13:30:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (40, 10, 37, '2024-04-04 10:45:00', '2024-04-18 10:45:00', NULL);
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (41, 1, 39, '2024-01-10 14:00:00', '2024-01-24 14:00:00', '2024-01-22 11:30:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (42, 2, 42, '2024-01-12 09:30:00', '2024-01-26 09:30:00', '2024-01-24 16:15:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (43, 3, 43, '2024-01-15 11:45:00', '2024-01-29 11:45:00', '2024-01-27 13:00:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (44, 4, 44, '2024-01-18 15:30:00', '2024-02-01 15:30:00', '2024-01-30 10:45:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (45, 5, 46, '2024-01-20 12:15:00', '2024-02-03 12:15:00', '2024-02-01 14:30:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (46, 6, 47, '2024-01-22 10:00:00', '2024-02-05 10:00:00', '2024-02-03 16:00:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (47, 7, 48, '2024-01-25 13:45:00', '2024-02-08 13:45:00', '2024-02-06 11:15:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (48, 8, 49, '2024-01-28 11:30:00', '2024-02-11 11:30:00', '2024-02-09 15:45:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (49, 9, 50, '2024-01-30 16:15:00', '2024-02-13 16:15:00', '2024-02-11 12:30:00');
INSERT INTO loans (loan_id, user_id, book_id, borrowed_date, due_date, returned_date) VALUES (50, 10, 10, '2024-02-02 14:00:00', '2024-02-16 14:00:00', '2024-02-14 10:00:00');

-- Table: users
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO users (user_id, first_name, last_name, email, password, registration_date) VALUES (1, 'Anna', 'Andersson', 'anna.andersson@email.com', 'password123', '2024-01-15 10:30:00');
INSERT INTO users (user_id, first_name, last_name, email, password, registration_date) VALUES (2, 'Erik', 'Eriksson', 'erik.eriksson@email.com', 'password456', '2024-01-20 14:15:00');
INSERT INTO users (user_id, first_name, last_name, email, password, registration_date) VALUES (3, 'Maria', 'Karlsson', 'maria.karlsson@email.com', 'password789', '2024-02-01 09:45:00');
INSERT INTO users (user_id, first_name, last_name, email, password, registration_date) VALUES (4, 'Johan', 'Johansson', 'johan.johansson@email.com', 'secure123', '2024-02-10 16:20:00');
INSERT INTO users (user_id, first_name, last_name, email, password, registration_date) VALUES (5, 'Emma', 'Svensson', 'emma.svensson@email.com', 'mypass456', '2024-02-15 11:00:00');
INSERT INTO users (user_id, first_name, last_name, email, password, registration_date) VALUES (6, 'Oscar', 'Nilsson', 'oscar.nilsson@email.com', 'secret789', '2024-03-01 08:30:00');
INSERT INTO users (user_id, first_name, last_name, email, password, registration_date) VALUES (7, 'Sofia', 'Larsson', 'sofia.larsson@email.com', 'pass1234', '2024-03-05 13:45:00');
INSERT INTO users (user_id, first_name, last_name, email, password, registration_date) VALUES (8, 'Lucas', 'Olsson', 'lucas.olsson@email.com', 'mykey567', '2024-03-10 15:30:00');
INSERT INTO users (user_id, first_name, last_name, email, password, registration_date) VALUES (9, 'Astrid', 'Persson', 'astrid.persson@email.com', 'pwd890', '2024-03-15 12:15:00');
INSERT INTO users (user_id, first_name, last_name, email, password, registration_date) VALUES (10, 'William', 'Gustafsson', 'william.gustafsson@email.com', 'login123', '2024-03-20 17:00:00');

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
