document.getElementById('login-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const username = this.username.value;
    const password = this.password.value;
    const errorMessage = document.getElementById('error-message');

    // Create the Basic Auth header
    const authHeader = 'Basic ' + btoa(username + ':' + password);

    // We try to fetch a protected route that any authenticated user can access.
    // If it succeeds, the credentials are valid.
    fetch('/api/user/me', {
        headers: {
            'Authorization': authHeader,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            // Store the auth header for future requests
            localStorage.setItem('auth', authHeader);
            // Redirect to the home page
            window.location.href = '/home.html';
        } else if (response.status === 401) {
            errorMessage.textContent = 'Invalid username or password.';
        } else {
            errorMessage.textContent = 'An error occurred. Please try again.';
        }
    })
    .catch(error => {
        console.error('Login error:', error);
        errorMessage.textContent = 'Could not connect to the server.';
    });
});
