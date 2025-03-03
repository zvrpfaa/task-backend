const BASE_URL = 'http://localhost:8080/api/v1/persons/';

// Helper: Convert comma-separated string to array
function parseCSV(input) {
    return input.split(',')
        .map(item => item.trim())
        .filter(item => item !== '');
}

// Create Person
document.getElementById('createPersonForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const name = document.getElementById('name').value;
    const surname = document.getElementById('surname').value;
    const pin = document.getElementById('pin').value;
    const sex = document.getElementById('sex').value.toUpperCase();
    const emailAddresses = parseCSV(document.getElementById('emailAddresses').value);
    const phoneNumbers = parseCSV(document.getElementById('phoneNumbers').value);

    const person = { name, surname, pin, sex, emailAddresses, phoneNumbers };
    try {
        const response = await fetch(BASE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(person)
        });
        if (response.ok) {
            const result = await response.json();
            console.log('Person created:\n' + JSON.stringify(result, null, 2));
        } else {
            const error = await response.text();
            alert('Error creating person: ' + error);
        }
    } catch (error) {
        console.error('Error creating person:', error);
    }
});

document.addEventListener('DOMContentLoaded', () => {
    // Initially load all persons
    loadPeople();

    // You can also set up event listeners if needed (for example, on keyup events for instant filtering)
});

/**
 * Fetches people from the backend.
 * Optionally accepts a filters object with keys: name, surname, sex.
 */
async function loadPeople(filters = {}) {
    try {
        let url = BASE_URL;
        // Build query parameters from filters if provided
        const params = new URLSearchParams();
        if (filters.name) {
            params.append('name', filters.name);
        }
        if (filters.surname) {
            params.append('surname', filters.surname);
        }
        if (filters.sex) {
            params.append('sex', filters.sex);
        }
        if (Array.from(params).length > 0) {
            url += '?' + params.toString();
        }
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('Error fetching persons: ' + response.statusText);
        }
        const people = await response.json();
        populateTable(people);
    } catch (error) {
        console.error('Error loading people:', error);
    }
}

/**
 * Populates the table with data.
 * Expects each person object to have: name, surname, sex, emailaddresses (array), phonenumbers (array)
 */
function populateTable(people) {
    const tableBody = document.getElementById('peopleTableBody');
    tableBody.innerHTML = '';

    people.forEach(person => {
        const row = document.createElement('tr');

        // Name Column
        const nameTd = document.createElement('td');
        nameTd.textContent = person.name || '';
        row.appendChild(nameTd);

        // Surname Column
        const surnameTd = document.createElement('td');
        surnameTd.textContent = person.surname || '';
        row.appendChild(surnameTd);

        // Sex Column
        const sexTd = document.createElement('td');
        sexTd.textContent = person.sex || '';
        row.appendChild(sexTd);

        // Email Addresses Column (assuming it's an array)
        const emailTd = document.createElement('td');
        emailTd.textContent = person.emailAddresses ? person.emailAddresses.join(', ') : '';
        row.appendChild(emailTd);

        // Phone Numbers Column (assuming it's an array)
        const phoneTd = document.createElement('td');
        phoneTd.textContent = person.phoneNumbers ? person.phoneNumbers.join(', ') : '';
        row.appendChild(phoneTd);

        tableBody.appendChild(row);
    });
}

/**
 * Reads filter values from the input elements and reloads the table data.
 */
function applyFilters() {
    const name = document.getElementById('nameFilter').value.trim();
    const surname = document.getElementById('surnameFilter').value.trim();
    const sex = document.getElementById('sexFilter').value;

    const filters = { name, surname, sex };
    loadPeople(filters);
}

// Get All Persons (with optional filters)
document.getElementById('filterForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const name = document.getElementById('filterName').value;
    const surname = document.getElementById('filterSurname').value;
    const sex = document.getElementById('filterSex').value;

    const params = new URLSearchParams();
    if (name) params.append('name', name);
    if (surname) params.append('surname', surname);
    if (sex) params.append('sex', sex);

    try {
        const response = await fetch(BASE_URL + '?' + params.toString());
        if (response.ok) {
            const persons = await response.json();
            const listDiv = document.getElementById('personsList');
            listDiv.innerHTML = '';
            persons.forEach(person => {
                const p = document.createElement('p');
                p.textContent = JSON.stringify(person);
                listDiv.appendChild(p);
            });
        } else {
            const error = await response.text();
            alert('Error fetching persons: ' + error);
        }
    } catch (error) {
        console.error('Error fetching persons:', error);
    }
});

// Add Additional Phone Numbers
document.getElementById('addPhoneForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const personId = document.getElementById('personIdPhone').value;
    const phoneNumbers = parseCSV(document.getElementById('phoneNumbersAdd').value);

    try {
        const response = await fetch(`${BASE_URL}${personId}/phone-numbers`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({phoneNumbers: phoneNumbers})
        });
        if (response.ok) {
            const result = await response.json();
            alert('Phone numbers added:\n' + JSON.stringify(result, null, 2));
        } else {
            const error = await response.text();
            alert('Error adding phone numbers: ' + error);
        }
    } catch (error) {
        console.error('Error adding phone numbers:', error);
    }
});

// Add Additional Email Addresses
document.getElementById('addEmailForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const personId = document.getElementById('personIdEmail').value;
    const emailAddresses = parseCSV(document.getElementById('emailAddressesAdd').value);

    try {
        const response = await fetch(`${BASE_URL}${personId}/addresses`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({emailAddresses: emailAddresses})
        });
        if (response.ok) {
            const result = await response.json();
            alert('Email addresses added:\n' + JSON.stringify(result, null, 2));
        } else {
            const error = await response.text();
            alert('Error adding email addresses: ' + error);
        }
    } catch (error) {
        console.error('Error adding email addresses:', error);
    }
});
