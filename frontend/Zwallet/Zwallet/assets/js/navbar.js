document.addEventListener('DOMContentLoaded', function () {
    const navItems = document.querySelectorAll('.sidebar-wrapper .nav li'); // Get all navigation items

    navItems.forEach(item => {
        item.addEventListener('click', function () {
            // Remove 'active' from all items
            navItems.forEach(nav => nav.classList.remove('active')); 

            // Add 'active' to the clicked item
            this.classList.add('active');
        });
    });
});