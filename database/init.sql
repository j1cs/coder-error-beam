-- Switch to the new database
\c main;

-- Create a table to store the model user's information
CREATE TABLE person (
                             id SERIAL PRIMARY KEY,
                             first_name VARCHAR(255) NOT NULL,
                             last_name VARCHAR(255) NOT NULL
);

-- Insert a model user with a specific name and last name
INSERT INTO person (first_name, last_name)
VALUES ('john', 'doe');
