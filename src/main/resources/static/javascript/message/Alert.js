function successAlert(message) {
    Swal.fire({
        title: "Successfully",
        text: message,
        icon: "success"
    });
}

function dangerAlert(message) {
    Swal.fire({
        title: "Warning",
        text: message,
        icon: "info"
    });
}

function errorAlert(message) {
    Swal.fire({
        title: "Error!",
        text: message,
        icon: "error"
    });
}

function confirmUpdateAlert(message1, message2) {
    Swal.fire({
        title: "Are you sure?",
        text: message1,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, update it!"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: "Update Successfully!",
                text: message2,
                icon: "success"
            }).then(() => {
                document.querySelector("form").submit();
            });
        }
    });
}