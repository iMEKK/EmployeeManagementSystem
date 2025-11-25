document.addEventListener('DOMContentLoaded', function() {
    const authHeader = localStorage.getItem('auth');
    const tableBody = document.getElementById('leave-request-table-body');

    if (!authHeader) {
        window.location.href = '/login.html';
        return;
    }

    function fetchLeaveRequests() {
        fetch('/api/leaverequests', {
            headers: { 'Authorization': authHeader }
        })
        .then(response => response.json())
        .then(requests => {
            tableBody.innerHTML = ''; // Clear table
            requests.forEach(req => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = `${req.employee.firstName} ${req.employee.lastName}`;
                row.insertCell().textContent = req.startDate;
                row.insertCell().textContent = req.endDate;
                row.insertCell().textContent = req.reason;
                
                const statusCell = row.insertCell();
                statusCell.textContent = req.status;
                statusCell.className = `status-${req.status}`;

                const actionsCell = row.insertCell();
                if (req.status === 'PENDING') {
                    const approveBtn = document.createElement('button');
                    approveBtn.textContent = 'Approve';
                    approveBtn.className = 'action-btn approve-btn';
                    approveBtn.onclick = () => updateLeaveStatus(req.id, 'approve');

                    const rejectBtn = document.createElement('button');
                    rejectBtn.textContent = 'Reject';
                    rejectBtn.className = 'action-btn reject-btn';
                    rejectBtn.onclick = () => updateLeaveStatus(req.id, 'reject');

                    actionsCell.appendChild(approveBtn);
                    actionsCell.appendChild(rejectBtn);
                }
            });
        });
    }

    function updateLeaveStatus(id, action) {
        fetch(`/api/leaverequests/${id}/${action}`, {
            method: 'PUT',
            headers: { 'Authorization': authHeader }
        })
        .then(response => {
            if (response.ok) {
                alert(`Request ${action}d successfully!`);
                fetchLeaveRequests(); // Refresh the list
            } else {
                alert(`Failed to ${action} the request.`);
            }
        });
    }

    fetchLeaveRequests();
});
