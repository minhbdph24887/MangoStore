$(document).ready(function () {
    $('.js-example-basic-multiple').select2();
});

function generateVariants() {
    const sizeSelect = document.getElementById("size");
    const selectedSizeOptions = sizeSelect.selectedOptions;

    const colorSelect = document.getElementById("color");
    const selectedColorOptions = colorSelect.selectedOptions;

    const resultTableBody = document.querySelector("#result tbody");
    resultTableBody.innerHTML = "";

    for (let i = 0; i < selectedSizeOptions.length; i++) {
        const sizeValue = selectedSizeOptions[i].value;

        for (let j = 0; j < selectedColorOptions.length; j++) {
            const colorValue = selectedColorOptions[j].value;

            const row = resultTableBody.insertRow();

            const sizeCell = row.insertCell(0);
            sizeCell.textContent = sizeValue;

            const colorCell = row.insertCell(1);
            colorCell.textContent = colorValue;

            const quantityCell = row.insertCell(2);
            const quantityInput = document.createElement("input");
            quantityInput.type = "number";
            quantityInput.value = 1;
            quantityInput.className = "form-control";
            quantityCell.appendChild(quantityInput);

            const importPriceCell = row.insertCell(3);
            const importPriceInput = document.createElement("input");
            importPriceInput.type = "number";
            importPriceInput.value = 1000;
            importPriceInput.className = "form-control";
            importPriceCell.appendChild(importPriceInput);

            const priceCell = row.insertCell(4);
            const priceInput = document.createElement("input");
            priceInput.type = "number";
            priceInput.value = 1000;
            priceInput.className = "form-control";
            priceCell.appendChild(priceInput);
        }
    }
}

function getDataFromTable() {
    const resultArray = [];
    const tableRows = document.querySelectorAll('#result tbody tr');

    tableRows.forEach(function (row) {
        const size = row.cells[0].textContent;
        const color = row.cells[1].textContent;
        const quantityInput = row.cells[2].querySelector("input").value;
        const importPriceInput = row.cells[3].querySelector("input").value;
        const priceInput = row.cells[4].querySelector("input").value;

        const quantity = parseInt(quantityInput, 10);
        const importPrice = parseFloat(importPriceInput);
        const price = parseFloat(priceInput);

        const variantData = {
            size: size,
            color: color,
            quantity: quantity,
            importPrice: importPrice,
            price: price
        };
        resultArray.push(variantData);
    });
    return resultArray;
}

function addProductDetailAPI() {
    const idProduct = document.getElementById("idProduct").value;
    const idMaterial = document.getElementById("material").value;
    const idOrigin = document.getElementById("origin").value;
    const imagesProduct = document.getElementById("imageInput").files[0].name;
    const describe = document.getElementById("describeInput").value;
    const data = {
        idProduct: idProduct,
        imagesProduct: imagesProduct,
        idMaterial: idMaterial,
        idOrigin: idOrigin,
        describe: describe,
        variantRequests: getDataFromTable(),
    };
    console.log(data)
    if (data.variantRequests === []) {
        alert("Please enter product variation.")
    } else {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "http://localhost:8080" + "/api/mangostore/admin/product-detail/add",
            data: JSON.stringify(data),
            dataType: 'json',
            success: function (responseData) {
                window.open("http://localhost:8080/mangostore/admin/product-detail", "_self")
            },
            error: function (e) {
                console.log("ERROR : ", e);
            }
        });
    }
}