const MIN_LENGTH = 6;
const MAX_LENGTH = 14;

const passwordCheck = function () {
    const password = document.querySelector('#password');
    const confirmPassword = document.querySelector('#confirm-password');
    const passwordMessage = document.querySelector('#passwordMessage');
    const confirmPasswordMessage = document.querySelector('#confirmPasswordMessage');

    const submitButton = document.querySelector('#submit');

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
};

window.onload = function () {
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirm-password');

    password.addEventListener('input', passwordCheck);
    confirmPassword.addEventListener('input', passwordCheck);
}