

function logSubmit(event) {
    // console.log("submitted")
    event.preventDefault();

    var formData = new FormData(document.querySelector('#registrationForm'));
    var jsonObject = JSON.stringify(Object.fromEntries(formData));
    console.log(jsonObject);

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: jsonObject,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/api/useraccounts/addNewClient", requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));

}
const form = document.getElementById('registrationForm');
form.addEventListener('submit', logSubmit);
