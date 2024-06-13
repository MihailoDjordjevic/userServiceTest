function dodajPodatke() {
    var ime = document.getElementById('ime').value;
    var prezime = document.getElementById('prezime').value;

    if (ime !== '' && prezime !== '') {
        var data = {
            id: 0,
            name: ime,
            surname: prezime
        };

        // Making an HTTP POST request
        fetch('http://localhost:8080/api/student', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(responseData => {
                // Handle the response if needed
                console.log('Server response:', responseData);
                alert('Podaci su uspesno poslati!');
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Doslo je do greske prilikom slanja podataka.');
            });
    } else {
        alert('Molimo vas da popunite oba polja.');
    }
}

function loginUser() {
    // Specify the URL of your Spring backend
    const url = 'http://localhost:8080/api/user/login';

    var ime = document.getElementById('ime').value;
    var prezime = document.getElementById('prezime').value;

    // Create an object with the data to be sent in the request body
    const data = {
        username: ime,
        password: prezime
    };

    // Make the POST request using the fetch API
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            // You may need to include additional headers such as authorization tokens
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            // Handle the response data here
            console.log('Response:', data);
        })
        .catch(error => {
            // Handle errors here
            console.error('Error:', error);
        });
}

// Example usage
const username = 'yourUsername';
const password = 'yourPassword';

loginUser(username, password);

function getTerms() {
    // Specify the URL of your Spring backend for the GET request
    const url = 'http://localhost:8080/api/user/terms';

    // Make the GET request using the fetch API
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            // You may need to include additional headers such as authorization tokens
        }
    })
        .then(response => response.json())
        .then(data => {
            // Handle the response data here
            console.log('Terms:', data);
        })
        .catch(error => {
            // Handle errors here
            console.error('Error:', error);
        });
}

// Example usage
getTerms();


