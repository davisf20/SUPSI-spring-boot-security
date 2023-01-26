const MIN_LENGTH = 6;
const MAX_LENGTH = 14;

const passwordCheck = function () {
    const password = document.querySelector('#password');
    const confirmPassword = document.querySelector('#password-repeat');
    const passwordMessage = document.querySelector('#passwordMessage');
    const confirmPasswordMessage = document.querySelector('#confirmPasswordMessage');

    const submitButton = document.querySelector('#submit');

    if (password.value.length >= MIN_LENGTH && password.value.length <= MAX_LENGTH) {
        passwordMessage.innerHTML = '';

        // verifica se la nuova password contiene almeno tre categorie
        let categories = 0;
        if (password.match(/[A-Z]/)) {
            categories++;
        }
        if (password.match(/[a-z]/)) {
            categories++;
        }
        if (password.match(/[0-9]/)) {
            categories++;
        }
        if (password.match(/[\W_]/)) {
            categories++;
        }

        if (categories < 3) {
            passwordMessage.innerHTML = '';

            if (password.value === confirmPassword.value) {
                confirmPasswordMessage.innerHTML = '';
                submitButton.disabled = false;
            } else {
                confirmPasswordMessage.style.color = 'red';
                confirmPasswordMessage.innerHTML = 'The passwords do not match';
                submitButton.disabled = true;
            }
        } else {
            passwordMessage.style.color = 'red';
            passwordMessage.innerHTML = 'La nuova password deve contenere almeno tre categorie tra: lettere maiuscole, minuscole, numeri e segni di interpunzione.';
            submitButton.disabled = true;
        }
    } else {
        passwordMessage.style.color = 'red';
        passwordMessage.innerHTML = 'La nuova password deve essere lunga tra ${MIN_LENGTH} e ${MAX_LENGTH} caratteri.';
        submitButton.disabled = true;
    }
};

window.onload = function () {
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirm-password');

    password.addEventListener('input', passwordCheck);
    confirmPassword.addEventListener('input', passwordCheck);
}