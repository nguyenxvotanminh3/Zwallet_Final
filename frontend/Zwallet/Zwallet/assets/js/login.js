// Interceptor for Fetch API
const originalFetch = window.fetch;
window.fetch = async (...args) => {
    const token = localStorage.getItem('token');
    if (token) {
        args[1] = args[1] || {};
        args[1].headers = { ...args[1].headers, 'Authorization': `Bearer ${token}` };
    }
    return await originalFetch(...args);
};

// Login Form Submission
document.getElementById('loginForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('http://localhost:8765/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ userName: username, password: password })
        });

        if (!response.ok) {
            const errorData = await response.json();
            const errorMessage = errorData.message || 'An error occurred during login';
            alert(errorMessage);
            return; // Stop execution on login failure
        }

        const data = await response.json();

        if (data.token) {
            localStorage.setItem('token', data.token);
            console.log('Login successful:', data);
            localStorage.setItem('username', username); // Store username
            
            // Redirect to dashboard with token as query parameter
            window.location.href = '/Zwallet/pages/dashboard.html?token=' + encodeURIComponent(data.token);
          
        } else {
            console.log('An error occurred during login. Token not received.');
            
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred during login. Please try again later.');
    }
});
//Alert for login
document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Dummy check for username and password
    if (username !== 'correctUsername' || password !== 'correctPassword') {
        stopExecution(1000);
        document.getElementById('error-message').textContent = 'Tên đăng nhập hoặc mật khẩu sai';
    } else {
        // Redirect to another page or handle successful login
        document.getElementById('error-message').textContent = '';
        window.location.href = 'dashboard.html'; // Example redirection after successful login
    }
});


