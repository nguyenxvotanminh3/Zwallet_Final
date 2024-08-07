document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM fully loaded and parsed');
  
    const storedToken = localStorage.getItem('token');
    const storedUsername = localStorage.getItem('username');

  
    if (storedToken && storedUsername) {
        const requestUrl = `http://localhost:8765/api/v1/${storedUsername}`;
        const requestHeaders = new Headers({
            'Authorization': `Bearer ${storedToken}`
        });

        // Fetch user data including fullname
        fetch(requestUrl, { headers: requestHeaders })
            .then(response => response.json())
            .then(data => {
                const totalAmount = parseInt(data.totalAmount, 10); // Convert to integer
                const formattedTotalAmount = totalAmount.toLocaleString('vi-VN'); // Format with thousands separator
                document.getElementById('totalAmount').textContent = formattedTotalAmount;
                
                // Display user full name
                document.getElementById('userFullName').textContent = data.fullName; // Make sure to have an element with id 'userFullName'

                renderTransactionHistory(data.transactionHistoryResponses);
            })
            .catch(error => console.error('Error fetching user data:', error));
    } else {
        console.error('Token or username not found in localStorage');
        window.location.href = '/Zwallet/pages/logout.html';
    }

    // Adjust total amount
    const form = document.getElementById('totalAmountAdjust');
    if (form) {
        form.addEventListener('submit', async (event) => {
            event.preventDefault();
           
            
            const currentAmountInput = document.getElementById('currentAmountInput').value;
            
            // get the value of the input and pass this as json to save in the transaction history 
            const amountInput =  currentAmountInput
            const useForInput = "Add money to wallet"
            const now = new Date().toISOString();
            const userWasting = new UserWastingDTO(useForInput, amountInput, now);
            const userJson = JSON.stringify(userWasting);
            //------------------------------------------------------------------------------

            const username = localStorage.getItem('username'); 

            if (username) {
                try {
                    const response = await fetch(`http://localhost:8765/api/v1/user/update-total-amount/${username}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'text/plain',
                            'Authorization': `Bearer ${storedToken}`
                        },
                        body: currentAmountInput
                    });

                    if (response.ok) {

                        
                        const responseText = await response.text(); 
                        console.log('Response Text:', responseText);

                        const formattedAmount = parseInt(currentAmountInput, 10).toLocaleString('vi-VN');
                        document.getElementById('totalAmount').textContent = formattedAmount;
                        // Save the updated total amount to the database
                        try{
                       const response =  await fetch(`http://localhost:8765/api/v2/trans/create/${username}`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': `Bearer ${storedToken}`
                            },
                            body: userJson
                        });
                        if (response.ok) {
                            console.log('Response Text:', responseText);
                        }else{
                            throw new Error(`HTTP error! Status: ${response.status}`);
                        }
                        
                         }catch (error) {
                        console.error('Error creating transaction:', error);
                    }
                        refreshTransactionHistory();
                    } else {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                } catch (error) {
                    console.error('Error updating total amount:', error);
                }
            } else {
                console.error('Username not found in localStorage');
                window.location.href = '/Zwallet/pages/logout.html';
            }
        });
    }

    // Define DTO class
    class UserWastingDTO {
        constructor(purpose, amountUsed, localDateTime, moneyLeft) {
            this.purpose = purpose;
            this.amountUsed = amountUsed;
            this.localDateTime = localDateTime;
            this.moneyLeft = moneyLeft;
        }
    }

    const formTransaction = document.getElementById('useFor');

    if (formTransaction) {
        formTransaction.addEventListener('submit', async (event) => {
            event.preventDefault();

            const useForInput = document.getElementById('useForInput').value;
            const amountInput = document.getElementById('amountInput').value;
            const now = new Date();
            console.log('Now:', now);
            // Convert to GMT+7 (Vietnam time)
            const gmtPlus7Time = new Date(now.getTime() + 7 * 60 * 60 * 1000);
            // Format the date and time as ISO string
            const formattedTime = gmtPlus7Time.toISOString();
                // Fetch the updated total amount after creating a transaction
            const totalAmount = parseInt(document.getElementById('totalAmount').textContent.replace(/\./g, ''), 10);
            const amountUsed = parseInt(amountInput, 10);
            const updatedTotalAmount = totalAmount - amountUsed;
            // Update the total amount on the page
            document.getElementById('totalAmount').textContent = updatedTotalAmount.toLocaleString('vi-VN');
            const userWasting = new UserWastingDTO(useForInput, amountInput, formattedTime, updatedTotalAmount);
            const userJson = JSON.stringify(userWasting);
        
            const username = localStorage.getItem('username'); 

            try {
                const response = await fetch(`http://localhost:8765/api/v2/trans/create/${username}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${storedToken}`
                    },
                    body: userJson
                });

                if (response.ok) {
                    const responseText = await response.text(); 
                    console.log('Response Text:', responseText);

                   

                    // Save the updated total amount to the database
                    await fetch(`http://localhost:8765/api/v1/user/update-total-amount/${username}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'text/plain',
                            'Authorization': `Bearer ${storedToken}`
                        },
                        body: updatedTotalAmount.toString()
                    });

                    await refreshTransactionHistory();
                } else {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
            } catch (error) {
                console.error('Error creating transaction:', error);
            }
        });
    }

    async function refreshTransactionHistory() {
        const username = localStorage.getItem('username'); 
        const requestUrl = `http://localhost:8765/api/v1/${username}`;
        const requestHeaders = new Headers({
            'Authorization': `Bearer ${storedToken}`
        });

        try {
            const response = await fetch(requestUrl, { headers: requestHeaders });
            if (response.ok) {
                const data = await response.json();
                const formattedTotalAmount = parseInt(data.totalAmount, 10).toLocaleString('vi-VN');
                document.getElementById('totalAmount').textContent = formattedTotalAmount;
                renderTransactionHistory(data.transactionHistoryResponses);
            } else {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
        } catch (error) {
            console.error('Error fetching transaction history:', error);
        }
    }

    function renderTransactionHistory(transactions) {
        const transactionHistoryBody = document.getElementById('transactionHistoryBody');
        transactionHistoryBody.innerHTML = '';

        transactions.forEach(transaction => {
            const row = document.createElement('tr');

            const amountCell = document.createElement('td');
            amountCell.textContent = parseInt(transaction.amountUsed, 10).toLocaleString('vi-VN');
            row.appendChild(amountCell);

            const purposeCell = document.createElement('td');
            purposeCell.textContent = transaction.purpose;
            row.appendChild(purposeCell);

            const dateTimeCell = document.createElement('td');
            dateTimeCell.textContent = new Date(transaction.localDateTime).toLocaleString();
            row.appendChild(dateTimeCell);

            const moneyLeftCell = document.createElement('td');
            moneyLeftCell.textContent = parseInt(transaction.moneyLeft, 10).toLocaleString('vi-VN'); // Hiển thị số tiền còn lại
            row.appendChild(moneyLeftCell);

            transactionHistoryBody.appendChild(row);
        });
    }
 
  
});
 //logout function 
 function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    window.location.href = '/Zwallet/pages/logout.html';
}

// set an get token from local storage
