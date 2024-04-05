const checkButtonCheckOut = document.querySelector(".checkButtonCheckOut");
if (checkButtonCheckOut) {
    function handlePaymentSelection() {
        const selectedPaymentMethod = document.querySelector('input[name="payment"]:checked');
        const invoiceId = document.querySelector('[data-invoice-id]').getAttribute('data-invoice-id');

        if (selectedPaymentMethod) {
            if (selectedPaymentMethod.id === 'cod') {
                window.location.href = `/mangostore/cart/update-status?id=${invoiceId}`;
            } else if (selectedPaymentMethod.id === 'banking') {
                window.location.href = `/mangostore/cart/banking-status?id=${invoiceId}`;
            }
        } else {
            dangerAlert('Please select a payment method.');
        }
    }
}