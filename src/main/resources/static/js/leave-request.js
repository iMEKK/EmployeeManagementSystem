document.getElementById('leave-request-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const authHeader = localStorage.getItem('auth');
    if (!authHeader) {
        window.location.href = '/login.html';
        return;
    }

    const leaveData = {
        startDate: document.getElementById('startDate').value,
        endDate: document.getElementById('endDate').value,
        reason: document.getElementById('reason').value
    };

    fetch('/api/leaverequests', {
        method: 'POST',
        headers: {
            'Authorization': authHeader,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(leaveData)
    })
    .then(response => {
        if (response.ok) {
            alert('Leave request submitted successfully!');
            window.location.href = '/home.html';
        } else {
            alert('Failed to submit leave request. Please check the dates and try again.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred while submitting the request.');
    });
});
