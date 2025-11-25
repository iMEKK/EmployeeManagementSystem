document.addEventListener('DOMContentLoaded', function() {
    const authHeader = localStorage.getItem('auth');
    const tableBody = document.getElementById('timesheet-table-body');

    if (!authHeader) {
        window.location.href = '/login.html';
        return;
    }

    fetch('/api/timesheets', {
        headers: {
            'Authorization': authHeader,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            // Handle errors, e.g., redirect to login if unauthorized
            window.location.href = '/login.html';
        }
    })
    .then(timesheets => {
        if (timesheets.length === 0) {
            const row = tableBody.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 4;
            cell.textContent = 'No timesheet records found.';
            cell.style.textAlign = 'center';
        } else {
            timesheets.forEach(ts => {
                const row = tableBody.insertRow();

                const checkIn = new Date(ts.checkInTime);
                const checkOut = ts.checkOutTime ? new Date(ts.checkOutTime) : null;

                row.insertCell().textContent = checkIn.toLocaleDateString();
                row.insertCell().textContent = checkIn.toLocaleTimeString();
                row.insertCell().textContent = checkOut ? checkOut.toLocaleTimeString() : 'Not checked out';

                let duration = 'N/A';
                if (checkOut) {
                    const diffMs = checkOut - checkIn;
                    const diffHours = (diffMs / (1000 * 60 * 60)).toFixed(2);
                    duration = diffHours;
                }
                row.insertCell().textContent = duration;
            });
        }
    })
    .catch(error => {
        console.error('Error fetching timesheets:', error);
        const row = tableBody.insertRow();
        const cell = row.insertCell();
        cell.colSpan = 4;
        cell.textContent = 'Error loading data.';
        cell.style.textAlign = 'center';
    });
});
