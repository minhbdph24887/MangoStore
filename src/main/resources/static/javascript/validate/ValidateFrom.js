const detailProfileClient = document.querySelector(".detailProfileClient");
if (detailProfileClient) {
    function UpdateProfileClient() {
        const numberPhone = document.getElementById("numberPhoneInput").value;
        const email = document.getElementById("emailInput").value;
        const phonePattern = /^0\d{9}$/;
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!phonePattern.test(numberPhone) || isNaN(numberPhone)) {
            dangerAlert("This is not a valid phone number");
            return false;
        } else if (!emailPattern.test(email)) {
            dangerAlert("This is not a valid email address");
            return false;
        } else {
            event.preventDefault();
            confirmUpdateAlert("Do you want to update your profile?", "Update successful");
        }
    }
}

const checkBirthday = document.querySelector(".checkBirthday");
if (checkBirthday) {
    document.getElementById("birthdayInput").addEventListener("change", function () {
        const birthdayInput = document.getElementById("birthdayInput");
        const saveButton = document.querySelector("button.btn.btn-outline-success");
        const currentDate = new Date();
        const enteredDate = new Date(birthdayInput.value);
        if (enteredDate > currentDate) {
            birthdayInput.nextElementSibling.style.display = "inline";
            saveButton.disabled = true;
        } else {
            birthdayInput.nextElementSibling.style.display = "none";
            saveButton.disabled = false;
        }
    });
    document.getElementById("birthdayInput").dispatchEvent(new Event("change"));
}

const checkPassWordClient = document.querySelector(".checkPasswordClientPage");
if (checkPassWordClient) {
    document.addEventListener("DOMContentLoaded", function () {
        const passwordInput = document.getElementById("passwordInput");
        const confirmPasswordInput = document.getElementById("confirmPasswordInput");
        const passwordError = document.getElementById("passwordError");
        const confirmPasswordError = document.getElementById("confirmPasswordError");
        const saveButton = document.getElementById("saveButton");

        saveButton.disabled = true;

        function validatePassword() {
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^\s]{8,16}$/;
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordInput.value;

            if (!passwordRegex.test(password)) {
                passwordError.style.display = "block";
                saveButton.disabled = true;
            } else {
                passwordError.style.display = "none";
                saveButton.disabled = confirmPassword !== password;
            }

            if (confirmPassword !== password) {
                confirmPasswordError.style.display = "block";
                saveButton.disabled = true;
            } else {
                confirmPasswordError.style.display = "none";
            }
        }

        passwordInput.addEventListener("input", validatePassword);
        confirmPasswordInput.addEventListener("input", validatePassword);
    });
}

const updateChangePassword = document.querySelector(".updateChangePasswordPage");
if (updateChangePassword) {
    function UpdateChangePasswordClient() {
        event.preventDefault();
        confirmUpdateAlert("Do you want to update change password ?", "Update Successful");
    }
}

const showPassWord = document.querySelector(".showPassWordPage");
if (showPassWord) {
    document.addEventListener('DOMContentLoaded', (event) => {
        const togglePasswordButton = document.querySelector('.btn.btn-primary');
        const passwordInput = document.getElementById('passwordInput');
        const confirmPasswordInput = document.getElementById('confirmPasswordInput');

        function togglePassword() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            confirmPasswordInput.setAttribute('type', type);
            togglePasswordButton.textContent = type === 'password' ? 'Show Password' : 'Hide Password';
        }

        togglePasswordButton.addEventListener('click', togglePassword);
    });
}