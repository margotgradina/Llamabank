window.onload = checkSessionStorageForBankProps();
$("#bankname").text(sessionStorage.getItem("bankName"));

let regexLettersAndSpaces = new RegExp("^[a-zA-Z\\s]+$");
let regexLettersNumbersSpaces = new RegExp("^[a-zA-Z\\s]*$");
let regexOnlyNumbers = new RegExp("^[0-9]+$");


function redirectPage() {
    const url = "http://localhost:8080/login.html";
    window.location.href = url;
}

async function fetchtext(requestOptions) {
    let response = await fetch("http://localhost:8080/api/useraccounts/addNewClient", requestOptions);
    console.log(response.status);
    console.log(response.statusText);

    if (response.status === 200) {
        let data = await response.text();
        console.log(data);
        // sessionStorage.setItem("id", data);
        redirectPage();
    } else{
        let data = await response.text();
        toastr["error"](data);
    }
}

function logSubmit(event) {
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

    fetchtext(requestOptions);

}

//region checkValidations
function checkValidationBSN() {
    let bsn = document.querySelector("#bsn").value;
    if (bsn.length > 9 || bsn.length < 8) {
        toastr["error"]("bsn must be 8 or 9 characters");
        resetField("#bsn");
    }

}

function checkValidationPostalCode() {
    let postalCodeNLRegex = new RegExp("^[1-9][0-9]{3} ?(?!sa|sd|ss|SA|SD|SS)[A-Za-z]{2}$");
    let postalCodeInternationalRegex = new RegExp("^[a-z0-9][a-z0-9\\- ]{0,10}[a-z0-9]$");
    let postalCode = document.querySelector("#postalCode").value;
    if (!postalCodeNLRegex.test(postalCode) || postalCode === "") {
        toastr["error"]("fill in a valid postal code");
        resetField("#postalCode");
    }

}

function checkValidationName() {
    let name = document.querySelector("#name").value;
    if (!regexLettersAndSpaces.test(name)) {
        toastr["error"]("fill in a valid name, no special characters or numbers allowed");
        resetField("#name");
    }
}

function checkValidationStreetName() {
    let street = document.querySelector("#street").value;
    if (!regexLettersNumbersSpaces.test(street)) {
        toastr["error"]("fill in a valid streetname, no special characters allowed");
        resetField("#street");
    }
}

function checkValidationHouseNumber() {
    let houseNumber = document.querySelector("#houseNumber").value;
    if (!regexOnlyNumbers.test(houseNumber)) {
        toastr["error"]("fill in a valid house number, only numbers allowed");
        resetField("#houseNumber");
    }
}

function checkValidationHouseNumberExtension() {
    //TODO ?
}

function checkValidationCity() {
    let city = document.querySelector("#city").value;
    if (!regexLettersAndSpaces.test(city)) {
        toastr["error"]("fill in a valid city, no special characters or numbers allowed");
        resetField("#city");
    }
}

function checkValidationRegion() {
    let region = document.querySelector("#region").value;
    if (!regexLettersAndSpaces.test(region)) {
        setupToastr();
        toastr["error"]("fill in a valid region, no special characters or numbers allowed");
        resetField("#region");
    }
}

function checkValidationEmail() {
    let email = document.querySelector("#email").value;
    if ((email.includes("@") === false) || (email.includes(".") === false)){
    toastr["error"]("fill in a valid email");
        resetField("#email");
    }
}

function checkValidationPhoneNumber() {
    let phoneNumber = document.querySelector("#phoneNumber").value;
    let regexPhoneNumber = new RegExp("^(?:0|(?:\\+|00) ?31 ?)(?:(?:[1-9] ?(?:[0-9] ?){8})|(?:6 ?-? ?[1-9] ?(?:[0-9] ?){7})|(?:[1,2,3,4,5,7,8,9]\\d ?-? ?[1-9] ?(?:[0-9] ?){6})|(?:[1,2,3,4,5,7,8,9]\\d{2} ?-? ?[1-9] ?(?:[0-9] ?){5}))$")
    if (!regexPhoneNumber.test(phoneNumber)) {
        toastr["error"]("fill in a valid phonenumber");
        resetField("#phoneNumber");
    }
}

function checkValidationUsername() {
    let userName = document.querySelector("#userName").value;
    if (userName.length < 5 || userName.length > 30) {
        toastr["error"]("username must be between 5 and 30 characters");
        resetField("#userName");
    }

    let regexUsername = new RegExp("^[A-Za-z0-9_-]*$");
    if (!regexUsername.test(userName)) {
        toastr["error"]("fill in a valid username, only letters, numbers, dashes and underscores allowed");
        resetField("#userName");
    }
}

function checkValidationPassword() {
    let password = document.querySelector("#password").value;
    if (password.length < 5 || password.length > 30) {
        toastr["error"]("username must be between 5 and 30 characters");
        resetField("#userName");
    }
}

//endregion

function resetField(elementId) {
    document.querySelector(elementId).value = "";
}

const form = document.getElementById('registrationForm');
form.addEventListener('submit', logSubmit);
