CREATE TABLE Companies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    industry VARCHAR(100),
    HRContact VARCHAR(100)
);

CREATE TABLE Students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    Dept VARCHAR(100),
    GPA DECIMAL(3, 2)
);

CREATE TABLE Placements (
    id SERIAL PRIMARY KEY,
    student_id INT REFERENCES Students(id),
    company_id INT REFERENCES Companies(id),
    position VARCHAR(100),
    salary DECIMAL(10, 2),
    placement_date DATE
);

CREATE TABLE Drives (
    id SERIAL PRIMARY KEY,
    company_id INT REFERENCES Companies(id),
    Start_date DATE,
    End_date DATE,
    MinGpa DECIMAL(3, 2),
    Description TEXT,
    Available_Seats INT
);

CREATE TABLE Applications (
    id SERIAL PRIMARY KEY,
    student_id INT REFERENCES Students(id),
    drive_id INT REFERENCES Drives(id),
    application_date DATE,
    status VARCHAR(50)
);