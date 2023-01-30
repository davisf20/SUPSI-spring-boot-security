const MIN_LENGTH = 6;
const MAX_LENGTH = 14;
const sanitizerPattern = new RegExp("[<>\"']");

const changePasswordCheck = function () {
    const oldPassword = document.querySelector('#oldPassword');
    const password = document.querySelector('#password');
    const confirmPassword = document.querySelector('#confirm-password');
    const passwordMessage = document.querySelector('#passwordMessage');
    const confirmPasswordMessage = document.querySelector('#confirmPasswordMessage');

    const submitButton = document.querySelector('#submit');

    console.log("SIUUUUUU");

    if (oldPassword.value === password.value) {
        passwordMessage.style.color = 'red';
        passwordMessage.innerHTML = 'The new password must be different from the current one.';
        submitButton.disabled = true;
    } else {
        passwordMessage.innerHTML = '';
        if (password.value.length >= MIN_LENGTH && password.value.length <= MAX_LENGTH) {
            passwordMessage.innerHTML = '';

            if (password.value.match(/[A-Za-z]/) && password.value.match(/[0-9]/) && password.value.match(/[\W_]/)) {
                passwordMessage.innerHTML = '';

                if (password.value.match(sanitizerPattern)) {
                    confirmPasswordMessage.style.color = 'red';
                    confirmPasswordMessage.innerHTML = 'Punctuation marks cannot be <, >, /, ", \'';
                    submitButton.disabled = true;
                } else {
                    confirmPasswordMessage.innerHTML = '';
                    if (password.value === confirmPassword.value) {
                        confirmPasswordMessage.innerHTML = '';
                        submitButton.disabled = false;
                    } else {
                        confirmPasswordMessage.style.color = 'red';
                        confirmPasswordMessage.innerHTML = 'The passwords do not match';
                        submitButton.disabled = true;
                    }
                }
            } else {
                passwordMessage.style.color = 'red';
                passwordMessage.innerHTML = 'The password must contain at least upper or lower case letters, numbers and punctuation marks or non-alphabetic characters.';
                submitButton.disabled = true;
            }
        } else {
            passwordMessage.style.color = 'red';
            passwordMessage.innerHTML = `The new password must be between ${MIN_LENGTH} and ${MAX_LENGTH} characters long.`;
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