const MIN_LENGTH = 6;
const MAX_LENGTH = 14;

const changePasswordCheck = function () {
    const oldPassword = document.querySelector('#oldPassword');
    const password = document.querySelector('#password');
    const confirmPassword = document.querySelector('#confirm-password');
    const passwordMessage = document.querySelector('#passwordMessage');
    const confirmPasswordMessage = document.querySelector('#confirmPasswordMessage');

    const submitButton = document.querySelector('#submit');

    if (oldPassword.value === password.value) {
        passwordMessage.style.color = 'red';
        passwordMessage.innerHTML = 'La nuova password deve essere diversa da quella attuale.';
        submitButton.disabled = true;
    } else {
        passwordMessage.innerHTML = '';
        if (password.value.length >= MIN_LENGTH && password.value.length <= MAX_LENGTH) {

            if (password.value.match(/[A-Za-z]/) && password.value.match(/[0-9]/) && password.value.match(/[\W_]/)) {
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
                passwordMessage.innerHTML = 'La password deve contenere almeno lettere maiuscole o minuscole, numeri e segni di interpunzione o caratteri non alfabetici.';
                submitButton.disabled = true;
            }
        } else {
            passwordMessage.style.color = 'red';
            passwordMessage.innerHTML = `La nuova password deve essere lunga tra ${MIN_LENGTH} e ${MAX_LENGTH} caratteri.`;
            submitButton.disabled = true;
        }
    }
}

window.onload = function () {
    const oldPassword = document.getElementById('oldPassword');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirm-password');

    oldPassword.addEventListener('input', changePasswordCheck);
    password.addEventListener('input', changePasswordCheck);
    confirmPassword.addEventListener('input', changePasswordCheck);
}