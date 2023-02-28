window.onload = checkSessionStorageForBankProps();
window.sessionStorage;

loadBankProperties().then(function() {
    console.log("done loadBankProperties " + performance.now());
    $("#bankname").text(sessionStorage.getItem("bankName"));
});

// let's you login by pushing enter on either field
var passwordField = document.getElementById("password");
var usernameField = document.getElementById("username");

function getListener() {
    return function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            document.getElementById("btnLogin").click();
        }
    };
}

passwordField.addEventListener("keypress", getListener());
usernameField.addEventListener("keypress", getListener());


function redirectPage(isAccountManager) {
    if (isAccountManager){
        window.location.href = sessionStorage.getItem("bankFullApiUrl") + "/accountManagerDashboard.html";
    } else {
        window.location.href = sessionStorage.getItem("bankFullApiUrl") + "/clientDashboard.html";
    }
}

async function fetchtext(requestOptions) {
    let response = await fetch(sessionStorage.getItem("bankFullApiUrl") + "/api/useraccounts/login", requestOptions);

    if (response.status === 200) {
        let data = await response.text();
        let result = JSON.parse(data);
        sessionStorage.setItem("id", result.id);
        sessionStorage.setItem("isAccountManager", result.accountManager);
        redirectPage(result.accountManager);
    } else {
        let data = await response.text();
        console.log(data);
        toastr["error"](data);
    }
}

function login() {
    let username = document.querySelector("#username").value;
    let password = document.querySelector("#password").value;
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    if (username.length === 0 || password.length === 0) {
        toastr["error"]("fill in both fields");
    } else {

        var raw = JSON.stringify({
            "username": username,
            "password": password,
        });

        var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };

        fetchtext(requestOptions);
    }



}