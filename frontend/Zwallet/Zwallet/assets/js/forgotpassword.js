//Regist DTO
class UserRegistrationDto {
    constructor(userName, password, email) {
      this.userName = userName;
      this.password = password;
      this.email = email;
    }
  }

  document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    let valid = true;
    document.getElementById('usernameError').textContent = '';
    document.getElementById('passwordError').textContent = '';
    document.getElementById('confirmPasswordError').textContent = '';

    // check password
    if (password.length < 10 || /[^\w]/.test(password)) {
      document.getElementById('passwordError').textContent = 'Password must be at least 10 characters long and contain no special characters.';
      valid = false;
    }
    
    // Confirm password validation
    if (password !== confirmPassword) {
      document.getElementById('confirmPasswordError').textContent = 'Passwords do not match.';
      valid = false;
    }
    
    if (valid) {
      // Check username availability
      fetch('http://localhost:8123/api/v1/check-user', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userName: username })
      })
      .then(response => {
        console.log(response); // Log the response to check its content
        return response.json();
      })
      .then(data => {
        if (data.available) {
          // Create UserRegistrationDto object from form data
          const user = new UserRegistrationDto(
            document.getElementById('fullName').value,
            document.getElementById('username').value,
            "{noop}" + document.getElementById('password').value,
            document.getElementById('email').value
          );

          // Convert user object to JSON format
          const userJson = JSON.stringify(user);

          // Send POST request to API with user data
          fetch('http://localhost:8123/api/v1/user/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: userJson
          })
          .then(response => response.json())
          .then(data => {
          
            console.log('Registration successful:', data);
           
          })
          .catch(error => {
            
            console.error('Registration failed:', error);
        
          });
        } else {
          document.getElementById('usernameError').textContent = 'Username is already taken, user another one.';
        }
      })
      .catch(error => {
        console.error('Error checking username availability:', error);
        document.getElementById('usernameError').textContent = 'Error checking username availability.';
      });
    }
  });