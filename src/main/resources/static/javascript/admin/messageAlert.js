document.addEventListener('DOMContentLoaded', function () {
    setTimeout(function () {
        const messageDiv = document.getElementById('messageAlert');
        if (messageDiv) {
            messageDiv.style.display = 'none';
        }
    }, 5000);
});