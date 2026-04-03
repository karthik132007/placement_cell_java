CREATE TABLE student (
    rollnum VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER,
    major VARCHAR(100) NOT NULL,
    gpa DECIMAL(4, 2) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    resume_path VARCHAR(255)
);

CREATE TABLE company (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    location VARCHAR(200),
    industry VARCHAR(100),
    hrcontact VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE drive (
    D_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    companyId INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    availableSeats INT NOT NULL,
    lpa DECIMAL(10, 2) NOT NULL,
    mingpa DECIMAL(4, 2) NOT NULL,
    rounds_list VARCHAR(255) DEFAULT 'HR Round',
    FOREIGN KEY (companyId) REFERENCES company (id)
);

CREATE TABLE applications (
    A_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    driveId INTEGER NOT NULL,
    s_id VARCHAR(50) NOT NULL,
    applicationDate DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (driveId) REFERENCES drive (D_id),
    FOREIGN KEY (s_id) REFERENCES student (rollnum),
    CHECK (
        status IN (
            'Applied',
            'Shortlisted',
            'Rejected',
            'Accepted'
        )
    )
);

CREATE TABLE placements (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    student_roll VARCHAR(50) NOT NULL,
    company_id INTEGER NOT NULL,
    drive_id INTEGER NOT NULL,
    salary DECIMAL(10, 2) NOT NULL,
    placement_date DATE NOT NULL,
    FOREIGN KEY (student_roll) REFERENCES student (rollnum),
    FOREIGN KEY (company_id) REFERENCES company (id),
    FOREIGN KEY (drive_id) REFERENCES drive (D_id)
);

CREATE TABLE interviews (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    application_id INTEGER NOT NULL,
    interview_date DATETIME NOT NULL,
    interview_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'Scheduled',
    notes TEXT,
    FOREIGN KEY (application_id) REFERENCES applications (A_id)
);

CREATE TABLE notifications (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    recipient_type ENUM('student', 'admin') NOT NULL,
    recipient_id VARCHAR(50),
    message TEXT NOT NULL,
    sent_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE
);