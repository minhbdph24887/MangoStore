const checkBoxDetailElement = document.querySelector('.checkBoxDetail');
if (checkBoxDetailElement) {
    function initializeStatus() {
        const isActive = document.getElementById('active').checked ? 1 : 0;
        document.getElementById('status').value = isActive;
    }

    document.getElementById('active').addEventListener('change', initializeStatus);

    initializeStatus();
}