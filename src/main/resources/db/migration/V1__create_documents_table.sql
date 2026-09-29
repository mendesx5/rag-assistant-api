CREATE TABLE documents (
    uuid UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    upload_date TIMESTAMP NOT NULL,
    owner VARCHAR(255),
    file_path VARCHAR(255) NOT NULL
);