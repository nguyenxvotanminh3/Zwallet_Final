document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('convertButton').addEventListener('click', function () {
        const fromCurrency = document.getElementById('fromCurrency').value;
        const toCurrency = document.getElementById('toCurrency').value;
        const amount = parseFloat(document.getElementById('amount').value);

        // Kiểm tra nếu amount không hợp lệ
        if (isNaN(amount) || amount <= 0) {
            alert('Please enter a valid amount.');
            return;
        }

        fetch(`http://localhost:8765/currency-conversion-feign/from/${fromCurrency}/to/${toCurrency}/quantity/${amount}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Conversion rate:', data);
            const convertedAmount = data.totalCalculatedAmount;
            document.getElementById('convertedAmount').innerText = convertedAmount.toFixed(2);
        })
        .catch(error => {
            console.error('Error fetching conversion rate:', error);
            alert('An error occurred while converting the currency. Please try again later.');
        });
    });

    document.getElementById('swapButton').addEventListener('click', function () {
        const fromCurrency = document.getElementById('fromCurrency');
        const toCurrency = document.getElementById('toCurrency');
        const temp = fromCurrency.value;
        fromCurrency.value = toCurrency.value;
        toCurrency.value = temp;
    });
});

function logout() {
    localStorage.removeItem('token'); // Delete token from localStorage
    localStorage.removeItem('username'); // Delete token from localStorage
    window.location.href = '/Zwallet/pages/logout.html'; // Redirect to logout.html
}
