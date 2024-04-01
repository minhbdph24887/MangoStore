window.addEventListener('beforeunload', function(event) {
    const checkOutPage = document.querySelector(".checkOutPage");
    if (checkOutPage) {
        const data = JSON.stringify({status: 'cancelled'});
        const beaconUrl = 'http://localhost:8080/mangostore/cart/checkout/update';
        navigator.sendBeacon(beaconUrl, data);
    }
});
