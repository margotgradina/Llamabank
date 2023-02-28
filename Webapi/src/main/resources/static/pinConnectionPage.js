window.onload = checkSessionStorageForId();

console.log(sessionStorage.getItem("isAccountManager"));
if (sessionStorage.getItem("isAccountManager") === "false") {
    console.log(sessionStorage.getItem("isAccountManager"));
    document.getElementById("bankAccountNumberField").value = sessionStorage.getItem("bankAccountNr");
    document.getElementById("bankAccountNumberField").disabled = true;
}

const bankAccountNrField = document.getElementById("bankAccountNumberField");
const connectionCodeField = document.getElementById("connectionCodeField");

//on click -> create PinMachine
async function requestConnectionCode() {
    const button = document.getElementById('requestConnectionCodeBtn')

    var myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    const bankAccountNr = document.querySelector('input').value;
    const urlToFetch = "http://localhost:8080/api/pinmachine/requestConnectionCode?bankAccountNr=" + bankAccountNr;

    const response = await fetch(urlToFetch)


    if (response.status === 200) {
        var data = await response.json();
        console.log(data);
        var value = data["connectionCode"];
        bankAccountNrField.value += ""
        connectionCodeField.value += "Connection code: " + value
        button.disabled = true;
    } else {
        toastr['error']("invalid request")
    }
}


