document.addEventListener('DOMContentLoaded', function() {
    const authHeader = localStorage.getItem('auth');
    const tableBody = document.getElementById('employee-table-body');
    const addEmployeeBtn = document.getElementById('add-employee-btn');

    if (!authHeader) {
        window.location.href = '/login.html';
        return;
    }

    addEmployeeBtn.addEventListener('click', () => {
        window.location.href = '/add-employee.html';
    });

    function fetchEmployees() {
        fetch('/api/employees', {
            headers: { 'Authorization': authHeader }
        })
        .then(response => {
            if (response.ok) return response.json();
            throw new Error('Failed to fetch employees.');
        })
        .then(employees => {
            tableBody.innerHTML = ''; // Clear existing table data
            employees.forEach(emp => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = emp.id;
                row.insertCell().textContent = emp.firstName;
                row.insertCell().textContent = emp.lastName;
                row.insertCell().textContent = emp.email;
                row.insertCell().textContent = emp.role;

                const actionsCell = row.insertCell();
                const editBtn = document.createElement('button');
                editBtn.textContent = 'Edit';
                editBtn.className = 'action-btn edit-btn';
                editBtn.onclick = () => {
                    window.location.href = `/edit-employee.html?id=${emp.id}`;
                };
                
                const deleteBtn = document.createElement('button');
                deleteBtn.textContent = 'Delete';
                deleteBtn.className = 'action-btn delete-btn';
                deleteBtn.onclick = () => deleteEmployee(emp.id);

                actionsCell.appendChild(editBtn);
                actionsCell.appendChild(deleteBtn);
            });
        })
        .catch(error => console.error('Error:', error));
    }

    function deleteEmployee(id) {
        if (!confirm(`Are you sure you want to delete employee with ID: ${id}?`)) {
            return;
        }

        fetch(`/api/employees/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': authHeader }
        })
        .then(response => {
            if (response.ok) {
                alert('Employee deleted successfully.');
                fetchEmployees(); // Refresh the table
            } else {
                alert('Failed to delete employee.');
            }
        })
        .catch(error => console.error('Error:', error));
    }

    fetchEmployees();
});
