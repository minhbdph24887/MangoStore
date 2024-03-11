$(document).ready(function () {
    $("#createOrderButton").click(function () {
        console.error = function () {
        };
        $.ajax({
            type: "POST",
            url: "http://localhost:8080" + "/api/mangostore/admin/sell/create",
            success: function (response) {
                window.open("http://localhost:8080/mangostore/admin/sell", "_self")
            },
            error: function (e) {
                alert("Please pay your bills in advance");
            }
        });
    });
});
