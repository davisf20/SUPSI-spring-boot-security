window.onload = function () {
    const searchInput = document.getElementById('searchInput');
    const searchResults = document.getElementById('searchResults');

    const base = window.location.origin;
    const path = window.location.pathname;
    const url = "/customers/search";

    const option = {method: 'GET'};

    searchInput.addEventListener('input', function(event) {
        const searchValue = document.getElementById('searchInput').value;

        fetch(url + `?param=` + searchValue, option)
            .then(function (response) {
                return response.text();
            })
            .then(function (data) {
                searchResults.innerHTML = '';
                searchResults.innerHTML = data;
            });
    });
}