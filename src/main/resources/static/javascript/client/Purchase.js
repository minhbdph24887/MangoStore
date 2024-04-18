const indexPurchasePage = document.querySelector(".IndexPurchasePage");
if (indexPurchasePage) {
    function changeStatus(status) {
        window.location.href = 'http://localhost:8080/mangostore/purchase?status=' + status;
    }
}

const cancelPurchasePage = document.querySelector(".CancelPurchasePage");
if (cancelPurchasePage) {
    function confirmCancelPurchaseClient(id) {
        const url = 'http://localhost:8080/mangostore/purchase/remove?id=' + id;
        confirmAlertLink("Bạn Muốn Hủy Hóa Đơn?", "Hủy Thành Công.", url);
    }
}