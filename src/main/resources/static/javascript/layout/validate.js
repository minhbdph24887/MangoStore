if (document.getElementById("checkValidateVariant")) {
    function validateFrom(inputName, message) {
        const inputElement = document.querySelector('input[name="' + inputName + '"]');
        const inputButton = document.querySelector('input[type="submit"]');
        const returnValidateSpan = document.getElementById("returnValidate")

        inputButton.disabled = true;
        inputButton.style.opacity = "0.5";

        inputElement.addEventListener("input", function () {
            if (inputElement.value.trim() === "") {
                returnValidateSpan.textContent = message;
                inputButton.disabled = true;
                inputButton.style.opacity = "0.5";
            } else {
                returnValidateSpan.textContent = "";
                inputButton.disabled = false;
                inputButton.style.opacity = "1";
            }
        });
    }

    window.onload = function () {
        validateFrom("nameColor", "Please enter NameColor.");
    }
}