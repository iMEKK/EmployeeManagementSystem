document.addEventListener('DOMContentLoaded', function() {
    const authHeader = localStorage.getItem('auth');
    const form = document.getElementById('edit-employee-form');
    
    // Get employee ID from URL query parameter
    const urlParams = new URLSearchParams(window.location.search);
    const employeeId = urlParams.get('id');

    if (!authHeader || !employeeId) {
        window.location.href = '/login.html';
        return;
    }

    // Fetch existing employee data and populate the form
    fetch(`/api/employees/${employeeId}`, {
        headers: { 'Authorization': authHeader }
    })
    .then(response => response.json())
    .then(employee => {
        document.getElementById('firstName').value = employee.firstName;
        document.getElementById('lastName').value = employee.lastName;
        document.getElementById('email').value = employee.email;
        document.getElementById('username').value = employee.username;
        document.getElementById('role').value = employee.role;
        document.getElementById('baseSalary').value = employee.baseSalary;
    });

    // Handle form submission
    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const updatedData = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            email: document.getElementById('email').value,
            // username is not sent as it's read-only
            role: document.getElementById('role').value,
            baseSalary: parseFloat(document.getElementById('baseSalary').value)
        };

        fetch(`/api/employees/${employeeId}`, {
            method: 'PUT',
            headers: {
                'Authorization': authHeader,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedData)
        })
        .then(response => {
            if (response.ok) {
                alert('Employee updated successfully!');
                window.location.href = '/employees.html';
            } else {
                alert('Failed to update employee.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while updating the employee.');
        });
    });
});
