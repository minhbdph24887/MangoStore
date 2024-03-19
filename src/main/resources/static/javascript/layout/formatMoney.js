const formatMoneyPage = document.querySelector(".indexFormatMoneyPage");
if (formatMoneyPage) {
    function formatToCurrency(value) {
        const numberValue = parseInt(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).slice(0, -2);
    }

    function formatToNumber(value) {
        return value.replace(/[\D.]+/g, '');
    }

    function onInput(value) {
        document.getElementById('currencyInput').value = formatToCurrency(value);
        document.getElementById('output').value = formatToNumber(formatToCurrency(value));
    }
}

const formatMoneyImportPrice = document.querySelector(".formatMoneyImportPrice");
if (formatMoneyImportPrice) {
    function formatToCurrency(value) {
        const numberValue = parseInt(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).slice(0, -2);
    }

    function formatToNumber(value) {
        return value.replace(/[\D.]+/g, '');
    }

    function onInputImportPrice(value, id) {
        document.getElementById(id).value = formatToCurrency(value);
        document.getElementById('outputImportPrice' + id.slice(-2)).value = formatToNumber(formatToCurrency(value));
    }
}

const formatMoneyPrice = document.querySelector(".formatMoneyPrice");
if (formatMoneyPrice) {
    function formatToCurrency(value) {
        const numberValue = parseInt(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).slice(0, -2);
    }

    function formatToNumber(value) {
        return value.replace(/[\D.]+/g, '');
    }

    function onInputPrice(value, id) {
        document.getElementById(id).value = formatToCurrency(value);
        document.getElementById('outputPrice' + id.slice(-2)).value = formatToNumber(formatToCurrency(value));
    }
}

const formatMoneyFrom = document.querySelector(".fromFormatMoney");
if (formatMoneyFrom) {
    function formatCurrencyOnLoad() {
        const value = document.getElementById('currencyInput').value;
        document.getElementById('currencyInput').value = formatToCurrency(value);
    }

    function prepareValueForSubmit() {
        const value = document.getElementById('currencyInput').value;
        document.getElementById('output').value = formatToNumber(value);
        return true;
    }

    function formatToCurrency(value) {
        const numberValue = parseInt(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).slice(0, -2);
    }

    function formatToNumber(value) {
        return value.replace(/[\D.]+/g, '');
    }

    function onInput(value) {
        document.getElementById('currencyInput').value = formatToCurrency(value);
    }

    window.onload = formatCurrencyOnLoad;

}

const formEditProductDetail = document.querySelector(".formEditProductDetail");
if (formEditProductDetail) {
    function formatCurrencyOnLoad() {
        const valueImportPrice = document.getElementById('importPriceInput').value;
        const valuePrice = document.getElementById('priceInput').value;
        document.getElementById('importPriceInput').value = formatToCurrency(valueImportPrice);
        document.getElementById('priceInput').value = formatToCurrency(valuePrice);
    }

    function prepareValueForSubmit() {
        const valueImportPrice = document.getElementById('importPriceInput').value;
        const valuePrice = document.getElementById('priceInput').value;
        document.getElementById('outputImportPrice').value = formatToNumber(valueImportPrice);
        document.getElementById('outputPrice').value = formatToNumber(valuePrice);
        return true;
    }

    function formatToCurrency(value) {
        const numberValue = parseInt(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).slice(0, -2);
    }

    function formatToNumber(value) {
        return value.replace(/[\D.]+/g, '');
    }

    function onInputImportPrice(value) {
        document.getElementById('importPriceInput').value = formatToCurrency(value);
        document.getElementById('outputImportPrice').value = formatToNumber(formatToCurrency(value));
    }

    function onInputPrice(value) {
        document.getElementById('priceInput').value = formatToCurrency(value);
        document.getElementById('outputPrice').value = formatToNumber(formatToCurrency(value));
    }

    window.onload = formatCurrencyOnLoad;

}