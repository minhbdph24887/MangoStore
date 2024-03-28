const checkCart = document.querySelector(".checkCartPage");
if (checkCart) {
    function checkCartIndex() {
        const checkLogin = document.getElementById("checkLogin").value;
        if (checkLogin === "null") {
            dangerAlert("Please log in to go to the shopping cart page");
        } else {
            window.location.href = "http://localhost:8080/mangostore/cart";
        }
    }
}