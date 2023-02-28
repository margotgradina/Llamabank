let userId;
let bankName;
let bankCode;
let regexOnlyNumbers = new RegExp("^[0-9]+$");
window.onload = checkSessionStorageForId(), setPageVariablesFromSession();
window.onload = getBankAccounts();


function setPageVariablesFromSession() {
    userId = sessionStorage.getItem("id");
    document.querySelector("#bankname").innerHTML = bankName;
    bankCode = sessionStorage.getItem("bankCode");
    bankName = sessionStorage.getItem("bankName");
    document.querySelector("#bankname").innerHTML = bankName;
    document.querySelector("#banklogo").src = "/images/" + bankCode + "-logo.png";

    console.log(userId);
}

function getBankAccounts() {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch("http://localhost:8080/api/bankaccounts?id=" + userId, requestOptions)
        .then(response => response.text())
        .then(result => {
            console.log(result);

            var myObj = JSON.parse(result);
            console.log(myObj);

                for (let bankaccount of myObj) {
                    var select = document.getElementById("dropdown");
                    var option = document.createElement('option');
                    console.log(bankaccount['Iban']);
                    console.log(bankaccount);
                    option.text = bankaccount['Iban'] + " | " + bankaccount['Account Name'] + " | Balance €" + parseInt(bankaccount['Balance']).toFixed(2);
                    option.value = bankaccount['Iban'];
                    select.add(option);
                }

        })
        .catch(error => console.log('error', error));
}

function sendData() {

    if (parseInt(document.getElementById("amount").value) <= 0) {
        toastr["error"]("fill in a valid amount: only numbers and amount higher than 0 allowed");
        document.getElementById("amount").value = "";
    } else {

        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        var raw = JSON.stringify({
            "fromIBANBankAccountNr": document.getElementById("dropdown").value,
            "toIBANBankAccountNr": document.getElementById("toIBANBankAccountNr").value,
            "amount": document.getElementById("amount").value,
            "message": document.getElementById("message").value
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

async function fetchtext(requestOptions) {
    let response = await fetch("http://localhost:8080/api/transactions/add", requestOptions);
    console.log(response.status);
    console.log(response.statusText);
    let data = await response.text();

    if (response.status === 200) {
        console.log(data);
        toastr["success"](data);
        document.getElementById("toIBANBankAccountNr").value = "";
        document.getElementById("amount").value = "";
        document.getElementById("message").value = "";
        $("#dropdown").empty();
        getBankAccounts();

    } else {
        console.log(data);
        toastr["error"](data);
    }
}

function checkAmount() {
    let amount = document.getElementById("amount").value;
    let intAmount = parseInt(amount);
    if ((!regexOnlyNumbers.test(amount)) || intAmount <= 0) {
        toastr["error"]("fill in a valid amount: only numbers and amount higher than 0 allowed");
        document.getElementById("amount").value = "";
    }
}
