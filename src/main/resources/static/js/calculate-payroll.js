document.addEventListener('DOMContentLoaded', function() {
    const authHeader = localStorage.getItem('auth');
    const calculateBtn = document.getElementById('calculate-btn');
    const monthInput = document.getElementById('month-input');
    const resultsBody = document.getElementById('payroll-results-body');

    if (!authHeader) {
        window.location.href = '/login.html';
        return;
    }

    // Set default value for month input to the current month
    const now = new Date();
    monthInput.value = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;

    calculateBtn.addEventListener('click', function() {
        const monthValue = monthInput.value;
        if (!monthValue) {
            alert('Please select a month.');
            return;
        }

        resultsBody.innerHTML = '<tr><td colspan="5" style="text-align:center;">Calculating...</td></tr>';

        fetch(`/api/payrolls/calculate?month=${monthValue}`, {
            method: 'POST',
            headers: { 'Authorization': authHeader }
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Calculation failed.');
        })
        .then(payrolls => {
            resultsBody.innerHTML = ''; // Clear "Calculating..." message
            if (payrolls.length === 0) {
                resultsBody.innerHTML = '<tr><td colspan="5" style="text-align:center;">No payroll data calculated for this month.</td></tr>';
            } else {
                payrolls.forEach(p => {
                    const row = resultsBody.insertRow();
                    row.insertCell().textContent = `${p.employee.firstName} ${p.employee.lastName}`;
                    row.insertCell().textContent = `${p.payPeriodStartDate} to ${p.payPeriodEndDate}`;
                    row.insertCell().textContent = p.baseSalary.toFixed(2);
                    row.insertCell().textContent = p.overtimePay.toFixed(2);
                    row.insertCell().textContent = p.netPay.toFixed(2);
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            resultsBody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:red;">An error occurred.</td></tr>';
        });
    });
});
