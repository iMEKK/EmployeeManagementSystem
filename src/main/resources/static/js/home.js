document.addEventListener('DOMContentLoaded', function() {
    const authHeader = localStorage.getItem('auth');
    const welcomeMessage = document.getElementById('welcome-message');
    const userRole = document.getElementById('user-role');
    const navLinks = document.querySelector('.nav-links');
    const logoutButton = document.getElementById('logout-button');

    if (!authHeader) {
        window.location.href = '/login.html';
        return;
    }

    fetch('/api/user/me', {
        headers: {
            'Authorization': authHeader,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            localStorage.removeItem('auth');
            window.location.href = '/login.html';
            throw new Error('Unauthorized');
        }
    })
    .then(user => {
        if (welcomeMessage) {
            welcomeMessage.textContent = `Welcome, ${user.firstName} ${user.lastName}!`;
        }
        if (userRole) {
            userRole.textContent = `Your role: ${user.role}`;
        }
        
        // Populate navigation links based on user role
        let links = '';
        if (user.role === 'ROLE_ADMIN') {
            links = `
                <a href="/employees.html">Manage Employees</a>
                <a href="/approve-leave.html">Approve Leave</a>
                <a href="/calculate-payroll.html">Calculate Payroll</a>
            `;
        } else if (user.role === 'ROLE_EMPLOYEE') {
            links = `
                <a href="/timesheets.html">My Timesheets</a>
                <a href="/leave-request.html">Submit Leave</a>
                <a href="/payslips.html">My Payslips</a>
            `;
        }
        if (navLinks) {
            navLinks.innerHTML = links;
        }
    })
    .catch(error => {
        if (error.message !== 'Unauthorized') {
            console.error('Error fetching user data:', error);
            if (welcomeMessage) {
                welcomeMessage.textContent = 'Failed to load user data.';
            }
        }
    });

    if (logoutButton) {
        logoutButton.addEventListener('click', () => {
            localStorage.removeItem('auth');
            window.location.href = '/login.html';
        });
    }
});
