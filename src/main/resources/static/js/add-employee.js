document.getElementById('add-employee-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const authHeader = localStorage.getItem('auth');
    if (!authHeader) {
        window.location.href = '/login.html';
        return;
    }

    const employeeData = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        role: document.getElementById('role').value,
        baseSalary: parseFloat(document.getElementById('baseSalary').value)
    };

    fetch('/api/employees', {
        method: 'POST',
        headers: {
            'Authorization': authHeader,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(employeeData)
    })
    .then(response => {
        if (response.ok) {
            alert('Employee added successfully!');
            window.location.href = '/employees.html';
        } else {
            // You can get more specific error messages from the backend if you have them
            alert('Failed to add employee. Please check the data and try again.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred while adding the employee.');
    });
});
