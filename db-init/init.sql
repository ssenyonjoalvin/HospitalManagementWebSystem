This script will run when the database container is first created.
-- It inserts a single administrator user into the 'user' table.

-- The table name is 'user' by default unless you specified a @Table(name="...") annotation.
-- The column names match the field names in your User.java entity.

INSERT INTO user (
    fullName,
    phoneNumber,
    email,
    dateOfBirth,
    address,
    nextOfKin,
    password,
    role,
    gender,
    deleted
)
VALUES (
    'Timothy Micheal',                      -- fullName
    '0700111222',                           -- phoneNumber
    'admin@hospital.com',                   -- email (this will be the login username)
    '1990-01-15',                           -- dateOfBirth (YYYY-MM-DD format)
    '123 Admin Avenue, Capital City',       -- address
    'Jane Doe',                             -- nextOfKin
    'adminpass',                            -- password (this is plain text. Your app must hash it for comparison)
    'ADMINISTRATOR',                                -- role (must match the exact string value of your Rolename enum)
    'MALE',                                 -- gender (must match the exact string value of your Gender enum)
    false                                   -- deleted (boolean value: false, 0, or 'false' usually works)
);

-- Add a nullable reason column to appointments table for cancellation reasons
ALTER TABLE appointments ADD COLUMN reason VARCHAR(255);
