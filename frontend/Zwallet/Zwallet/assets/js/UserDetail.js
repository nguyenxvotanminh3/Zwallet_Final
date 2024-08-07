class UserRegistrationDto {
  constructor(fullName, userName, password,company, emailAddress,address,city,country,postalCode,aboutMe,quotes,tag ) {
    this.fullName = fullName;
    this.userName = userName;
    this.password = password;
    this.company = company;
    this.emailAddress = emailAddress;
    this.city = city;
    this.address = address;
    this.country = country;
    this.postalCode = postalCode;
    this.aboutMe = aboutMe; 
    this.quotes = quotes; 
    this.tag = tag; 
  }
}
document.addEventListener('DOMContentLoaded', () => {
  console.log('DOM fully loaded and parsed');
  // check the token and username in localStorage
  const storedToken = localStorage.getItem('token');
  const storedUsername = localStorage.getItem('username');
  if (storedToken && storedUsername) {
    console.log('Token and username found in localStorage:', storedToken, storedUsername);
    const requestUrl = `http://localhost:8765/api/v1/${storedUsername}`;
    const requestUrlImage = `http://localhost:8765/api/v1/${storedUsername}/profileImage`;

    console.log('Requesting user data from:', requestUrl);
    const requestHeaders = new Headers({
        'Authorization': `Bearer ${storedToken}`
    });

   
    fetch(requestUrl, { headers: requestHeaders })
    .then(response => response.json())
    .then(data => {
      console.log('User data:', data);
        //------------------------------------------------- Display user full name--------------------------------------------------
        document.getElementById('company').value  = data.company;
        document.getElementById('userFullName').textContent  = data.fullName; 
        document.getElementById('userFullName1').textContent  = data.fullName;
        document.getElementById('userFullName2').value  = data.fullName;
        document.getElementById('userName').value  = data.userName;
        document.getElementById('emailAddress').value  = data.emailAddress;
        document.getElementById('address').value  = data.address;
        document.getElementById('city').value = data.city;
        document.getElementById('country').value = data.country;
        document.getElementById('postalCode').value = data.postalCode;
        document.getElementById('aboutMe').value = data.aboutMe;
        document.getElementById('quotes').value = data.quotes;
        document.getElementById('tag').value = data.tag;

        console.log('User data for adjust:', data); 
        document.getElementById('userAdjustForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const password = data.password;
        const userFullName = document.getElementById('userFullName2').value;
        const storedUsername = document.getElementById('userName').value;
        const emailAddress = document.getElementById('emailAddress').value;
        const company = document.getElementById('company').value;
        const address = document.getElementById('address').value;
        const city = document.getElementById('city').value;
        const country = document.getElementById('country').value;
        const postalCode = document.getElementById('postalCode').value;
        const aboutMe = document.getElementById('aboutMe').value;
        const quotes = document.getElementById('quotes').value;
        const tag = document.getElementById('tag').value;
        const user = new UserRegistrationDto(userFullName, storedUsername, password,company, emailAddress, address, city, country, postalCode, aboutMe, quotes, tag);
        const userJson = JSON.stringify(user);
        console.log('User data retrive:', userJson);
        
        fetch(`http://localhost:8765/api/v1/user/update/${storedUsername}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${storedToken}`
        },
          body: userJson
        })
        .then(response => { 
          if(response.ok){
            location.reload();
          }
        })
        .catch(error => console.error('Error updating user data:', error));
        });
        
        
    })
    
    .catch(error => console.error('Error fetching user data:', error));
    fetch(requestUrlImage, { headers: requestHeaders })
      .then(response => response.blob())
      .then(blob => {
        const url = URL.createObjectURL(blob);
        document.getElementById('profileImage').src = url;
      })
      .catch(error => console.error('Error fetching profile image:', error));
  }
  

});
//-----------------Upload Profile Image----------------------------------------------------------------------------
document.getElementById('profileImage').addEventListener('click', function() {
  document.getElementById('fileInput').click();
});

document.getElementById('fileInput').addEventListener('change', function(event) {
  const file = event.target.files[0];
  if (file) {
      const reader = new FileReader();
      reader.onload = function(e) {
          document.getElementById('profileImage').src = e.target.result;
      }
      reader.readAsDataURL(file);

      const formData = new FormData();
      formData.append('file', file);

      const storedToken = localStorage.getItem('token');
      const storedUsername = localStorage.getItem('username');

      fetch(`http://localhost:8765/api/v1/${storedUsername}/uploadProfileImage`, {
          method: 'PUT',
          headers: {
              'Authorization': `Bearer ${storedToken}`
          },
          body: formData
      })
      .then(response => {
          if (response.ok) {
              return response.text();
          } else {
              throw new Error('Error uploading image');
          }
      })
      .then(data => {
          console.log(data);
          alert('Image uploaded successfully');
      })
      .catch(error => {
          console.error('Error:', error);
          alert('Error uploading image');
      });
  }
});
function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('username');
  window.location.href = '/Zwallet/pages/logout.html';
}