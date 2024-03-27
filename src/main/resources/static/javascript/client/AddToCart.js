const checkDetailProductClient = document.querySelector(".detailProductClientPage");
if (checkDetailProductClient) {
    let availableQuantity = 0;

    function AddToCart() {
        const checkLogin = document.getElementById("checkLogin").value;
        if (checkLogin === "null") {
            dangerAlert("Please log in to your account to add to cart");
        } else {
            const idProduct = document.getElementById("idProduct").value;
            const idSize = document.getElementById("size").value;
            const idColor = document.getElementById("color").value;
            const idProductDetailClient = document.getElementById("productDetailClient").value;
            const quantity = document.querySelector('.quantity-selector input[type="number"]').value;
            if (quantity > availableQuantity) {
                errorAlert("The added quantity must not exceed the actual quantity of the product");
            } else {
                AddToCartProductDetail(idProductDetailClient, idProduct, idSize, idColor, quantity);
            }
        }
    }

    function AddToCartProductDetail(idProductDetailClient, idProduct, idSize, idColor, quantity) {
        const data = {
            idProduct: idProduct,
            idSize: idSize,
            idColor: idColor,
            quantity: quantity,
        }
        $.ajax({
            type: "POST",
            url: "http://localhost:8080" + "/api/mangostore/add-to-cart/client",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: "application/json",
            success: function (response) {
                if (response) {
                    successAlert("Add To Cart SuccessFully").then((result) => {
                        if (result.value) {
                            window.location.href = "http://localhost:8080/mangostore/product/detail/" + idProductDetailClient;
                        }
                    });
                } else {
                    errorAlert("Login has expired, please log in again").then((result) => {
                        if (result.value) {
                            window.location.href = "http://localhost:8080/mangostore/home";
                        }
                    });
                }
            },
            error: function (error) {
                errorAlert("An error occurred.");
                console.log(error);
            }
        });
    }

    document.getElementById('button-minus').addEventListener('click', function () {
        const input = document.querySelector('.quantity-selector input[type="number"]');
        const currentValue = parseInt(input.value, 10);
        if (currentValue > 1) {
            input.value = currentValue - 1;
        }
    });

    document.getElementById('button-plus').addEventListener('click', function () {
        const input = document.querySelector('.quantity-selector input[type="number"]');
        const currentValue = parseInt(input.value, 10);
        input.value = currentValue + 1;
    });

    document.addEventListener("DOMContentLoaded", (event) => {
        document.querySelector('.modal-body').addEventListener('change', (event) => {
            if (event.target.matches('#size') || event.target.matches('#color')) {
                const idProduct = document.getElementById("idProduct").value;
                const idSize = document.getElementById("size").value;
                const idColor = document.getElementById("color").value;
                GetQuantityProductDetail(idProduct, idSize, idColor);
            }
        });
    });

    function GetQuantityProductDetail(idProduct, idSize, idColor) {
        const checkButton = document.getElementById("buttonCheck");
        checkButton.disabled = true;
        const data = {
            idProduct: idProduct,
            idSize: idSize,
            idColor: idColor,
        }
        $.ajax({
            type: "POST",
            url: "http://localhost:8080" + "/api/mangostore/update-quantity/client",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: "application/json",
            success: function (response) {
                if (typeof response === 'number') {
                    $('#showQuantity').text('Còn: ' + response + ' sản phẩm');
                    availableQuantity = response;
                    document.getElementById('button-minus').disabled = false;
                    document.getElementById('button-plus').disabled = false;
                    checkButton.disabled = false;
                } else {
                    $('#showQuantity').text(response);
                    document.getElementById('button-minus').disabled = true;
                    document.getElementById('button-plus').disabled = true;
                    checkButton.disabled = true;
                }
            },
            error: function (xhr, status, error) {
                if (xhr.status === 404) {
                    $('#showQuantity').text('Sản phẩm không tồn tại');
                    document.getElementById('button-minus').disabled = true;
                    document.getElementById('button-plus').disabled = true;
                    checkButton.disabled = true;
                } else {
                    console.error("An error occurred: " + error);
                }
            }
        });
    }
}

const checkAddToFavourite = document.querySelector(".addToFavouritePage");
if (checkAddToFavourite) {
    function AddToFavourite() {
        const checkLogin = document.getElementById("checkLogin").value;
        if (checkLogin === "null") {
            dangerAlert("Please log in to your account to add to favourite");
        } else {
            const idProduct1 = document.getElementById("idProduct1").value;
            const idSize1 = document.getElementById("size1").value;
            const idColor1 = document.getElementById("color1").value;
            const idProductDetailClient1 = document.getElementById("productDetailClient1").value;
            AddToCartProductDetail1(idProductDetailClient1, idProduct1, idSize1, idColor1);
        }
    }

    function AddToCartProductDetail1(idProductDetailClient1, idProduct1, idSize1, idColor1) {
        const data = {
            idProduct: idProduct1,
            idSize: idSize1,
            idColor: idColor1,
        }
        $.ajax({
            type: "POST",
            url: "http://localhost:8080" + "/api/mangostore/add-to-favourite/client",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: "application/json",
            success: function (response) {
                if (response) {
                    successAlert("Add To Favourite SuccessFully").then((result) => {
                        if (result.value) {
                            window.location.href = "http://localhost:8080/mangostore/product/detail/" + idProductDetailClient1;
                        }
                    });
                } else {
                    errorAlert("you already like that product").then((result) => {
                        if (result.value) {
                            window.location.href = "http://localhost:8080/mangostore/product/detail/" + idProductDetailClient1;
                        }
                    });
                }
            },
            error: function (error) {
                errorAlert("An error occurred.");
                console.log(error);
            }
        });
    }

    document.addEventListener("DOMContentLoaded", (event) => {
        document.querySelector('.addToFavourite').addEventListener('change', (event) => {
            if (event.target.matches('#size1') || event.target.matches('#color1')) {
                const idProduct1 = document.getElementById("idProduct1").value;
                const idSize1 = document.getElementById("size1").value;
                const idColor1 = document.getElementById("color1").value;
                GetQuantityProductDetail1(idProduct1, idSize1, idColor1);
            }
        });
    });

    function GetQuantityProductDetail1(idProduct1, idSize1, idColor1) {
        const checkButton1 = document.getElementById("buttonCheck1");
        checkButton1.disabled = true;
        const data = {
            idProduct: idProduct1,
            idSize: idSize1,
            idColor: idColor1,
        }
        $.ajax({
            type: "POST",
            url: "http://localhost:8080" + "/api/mangostore/update-quantity/client",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: "application/json",
            success: function (response) {
                if (typeof response === 'number') {
                    $('#showQuantity1').text('Còn: ' + response + ' sản phẩm');
                    checkButton1.disabled = false;
                } else {
                    $('#showQuantity1').text(response);
                    checkButton1.disabled = true;
                }
            },
            error: function (xhr, status, error) {
                if (xhr.status === 404) {
                    $('#showQuantity1').text('Sản phẩm không tồn tại');
                    checkButton1.disabled = true;
                } else {
                    console.error("An error occurred: " + error);
                }
            }
        });
    }
}