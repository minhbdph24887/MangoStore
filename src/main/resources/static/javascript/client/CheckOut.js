document.addEventListener('DOMContentLoaded', (event) => {
    const checkOutPage = document.querySelector(".checkOutPage");
    if (checkOutPage) {
        const invoiceCreationDateElement = document.getElementById('checkCreateInvoice');
        const invoiceCreationDate = invoiceCreationDateElement.textContent;
        const invoiceCreationMoment = moment(invoiceCreationDate, 'HH:mm:ss : DD/MM/YYYY');
        let alertShown = false;

        function checkInvoiceExpiry() {
            const now = moment();
            if (now.diff(invoiceCreationMoment, 'minutes') >= 1) {
                if (!alertShown) {
                    alertShown = true;
                    dangerAlert('The invoice has expired, please create a new invoice.').then((result) => {
                        if (result.value) {
                            window.location.href = 'http://localhost:8080/mangostore/home';
                        }
                    });
                }
                clearInterval(intervalId);
            }
        }
        const intervalId = setInterval(checkInvoiceExpiry, 1000);
    }
});
