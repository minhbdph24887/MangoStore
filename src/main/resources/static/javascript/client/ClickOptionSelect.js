const checkProductClient = document.querySelector('.productClientPage');
if (checkProductClient) {
    function sortProducts() {
        const selectedOption = document.getElementById('priceSortSelect').value;
        const currentUrl = window.location.href;
        const baseUrl = currentUrl.split('?')[0];
        if (selectedOption) {
            window.location.href = baseUrl + '?sortDirection=' + selectedOption;
        } else {
            window.location.href = baseUrl;
        }
    }
}