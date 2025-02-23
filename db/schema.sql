CREATE TABLE person (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  pin VARCHAR(11) NOT NULL UNIQUE,
  sex VARCHAR(10) CHECK (sex IN ('MALE', 'FEMALE', 'OTHER'))
);

CREATE TABLE person_phone-numbers (
  id SERIAL PRIMARY KEY,
  person_id INTEGER NOT NULL,
  phone_number VARCHAR(20) NOT NULL,
  UNIQUE (person_id, phone_number),
  FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE
);

CREATE TABLE person_email_addresses (
  id SERIAL PRIMARY KEY,
  person_id INTEGER NOT NULL,
  email_address VARCHAR(50) NOT NULL,
  UNIQUE (person_id, email_address)phone_number),
  FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE
);