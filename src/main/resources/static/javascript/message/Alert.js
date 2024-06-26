function successAlert(message) {
    return Swal.fire({
        title: "Successfully",
        text: message,
        icon: "success"
    });
}

function dangerAlert(message) {
    return Swal.fire({
        title: "Warning",
        text: message,
        icon: "info"
    });
}

function errorAlert(message) {
    return Swal.fire({
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

function confirmAlertLink(message1, message2, url) {
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
                title: "Successfully!",
                text: message2,
                icon: "success"
            }).then(() => {
                window.location.href = url;
            });
        }
    });
}

function addAlert(message1, message2) {
    if (!confirm(message1)) {
        return false;
    } else {
        alert(message2);
    }
}