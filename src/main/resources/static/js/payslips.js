document.addEventListener('DOMContentLoaded', function() {
    const authHeader = localStorage.getItem('auth');
    const tableBody = document.getElementById('payslip-table-body');

    if (!authHeader) {
        window.location.href = '/login.html';
        return;
    }

    fetch('/api/payrolls/my', {
        headers: {
            'Authorization': authHeader,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            window.location.href = '/login.html';
        }
    })
    .then(payslips => {
        if (payslips.length === 0) {
            const row = tableBody.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 6;
            cell.textContent = 'No payslip records found.';
            cell.style.textAlign = 'center';
        } else {
            payslips.forEach(ps => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = `${ps.payPeriodStartDate} to ${ps.payPeriodEndDate}`;
                row.insertCell().textContent = ps.baseSalary.toFixed(2);
                row.insertCell().textContent = ps.overtimePay.toFixed(2);
                row.insertCell().textContent = ps.deductions.toFixed(2);
                row.insertCell().textContent = ps.netPay.toFixed(2);
                row.insertCell().textContent = new Date(ps.calculationDate).toLocaleString();
            });
        }
    })
    .catch(error => {
        console.error('Error fetching payslips:', error);
        const row = tableBody.insertRow();
        const cell = row.insertCell();
        cell.colSpan = 6;
        cell.textContent = 'Error loading data.';
        cell.style.textAlign = 'center';
    });
});
