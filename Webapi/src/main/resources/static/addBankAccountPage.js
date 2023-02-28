let userId;
window.onload = checkSessionStorageForId();
window.onload = setId();

function returnToDashboard(){
    window.location.href = "http://localhost:8080/clientDashboard.html";
}

function setId() {
    userId = sessionStorage.getItem("id");
    console.log(userId);
    document.querySelector("#bankname").innerHTML = sessionStorage.getItem("bankName");
}

//region hide and disable form
function showForm(typeForm) {
    if (typeForm === "smallBusinessAccount") {
        //sets the label to SB and makes visible
        document.querySelector("#labelAddAccount").innerHTML = "Add a Small Business Account";
        showAndEnable("#labelAddAccount");
        showAndEnable("#sector");
        hideAndDisable("#existingBankAccount");
        hideAndDisable("#securityCode");
        showAndEnable("#buttonNewAccount");
        hideAndDisable("#buttonExistingAccount");

    } else if (typeForm === "privateAccount") {
        hideAndDisable("#labelAddAccount");
        hideAndDisable("#sector");
        hideAndDisable("#existingBankAccount");
        hideAndDisable("#securityCode");
        showAndEnable("#buttonNewAccount")
        hideAndDisable("#buttonExistingAccount");

    } else if (typeForm === "existingAccount") {
        //sets the label to addExistingAccount and makes visible
        document.querySelector("#labelAddAccount").innerHTML = "Add an existing Account";
        showAndEnable("#labelAddAccount");
        showAndEnable("#existingBankAccount");
        showAndEnable("#securityCode");
        hideAndDisable("#sector");
        showAndEnable("#buttonExistingAccount");
        hideAndDisable("#buttonNewAccount");


    }
}

function hideAndDisable(element) {
    var element = document.querySelector(element);
    element.hidden = true;
    element.disabled = true;
}

function showAndEnable(element) {
    var element = document.querySelector(element);
    element.hidden = false;
    element.disabled = false;
}

//endregion

//region newAccountFetch
async function fetchtextForNewAccount(requestOptions) {
    let response = await fetch("http://localhost:8080/api/approvals/new/openbankaccountapproval", requestOptions)
    console.log(response.status);
    console.log(response.statusText);
    let data = await response.text();

    if (response.status === 200) {
        console.log(data);
        setTimeout(function () {
            window.location.href = "http://localhost:8080/clientDashboard.html";
        }, 2000);
        toastr["success"]("application for new bank account succeeded");
    } else {
        console.log(data);
        toastr["error"](data);
    }
}

function sendApplicationForNewAccount() {
    let clientId = userId;
    let typeAccount = document.querySelector('input[name="typeAccount"]:checked').value;
    let sector = document.querySelector("#sector");

    //makes sure the user chooses a sector
    if (sector.required && sector.hidden === false && sector.value === "") {
        toastr["error"]("choose a sector");
        return;
    }

    //setup fetch request details
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    //depending on the type of account, set the json data
    if (typeAccount === "smallBusinessAccount") {
        var raw = JSON.stringify({
            "clientId": clientId,
            "sector": sector.value
        });
    } else {
        var raw = JSON.stringify({
            "clientId": clientId,
        });
    }

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetchtextForNewAccount(requestOptions);
}
//endregion

//region existingAccountFetch
async function fetchtextForExistingAccount(requestOptions) {
    let response = await fetch("http://localhost:8080/api/approvals/checkaccountholderapproval", requestOptions)
    console.log(response.status);
    console.log(response.statusText);
    let data = await response.text();

    if (response.status === 200) {
        console.log(data);
        setTimeout(function () {
            window.location.href = "http://localhost:8080/clientDashboard.html";
        }, 2000);
        toastr["success"](data);
    } else {
        console.log(data);
        toastr["error"](data);
    }
}

function sendApplicationForExistingAccount() {
    let newAccountHolderId = userId;
    // let typeAccount = document.querySelector('input[name="typeAccount"]:checked');
    let bankAccountNumber = document.querySelector("#existingBankAccount");
    let securityCode = document.querySelector("#securityCode");

    if (bankAccountNumber.required && bankAccountNumber.hidden === false && bankAccountNumber.value === "") {
        toastr["error"]("Fill in a valid bankaccount");
    } else if (securityCode.required && securityCode.hidden === false && securityCode.value === "") {
        toastr["error"]("Fill in a valid securitycode, 5 numbers only");
    } else {

        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
            "newAccountHolderId": newAccountHolderId,
            "bankAccountNumber": bankAccountNumber.value,
            "securityCode": securityCode.value,
        });

        var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };

        fetchtextForExistingAccount(requestOptions);

    }
}
//endregion

//region validation functions
function validateBankAccountNr() {
    let regexOnlyNumbers = new RegExp("^[0-9]+$");
    let existingBankAccount = document.querySelector("#existingBankAccount").value;
    if ((!regexOnlyNumbers.test(existingBankAccount)) || existingBankAccount.length > 10 || existingBankAccount.length < 10) {
        toastr["error"]("fill in a valid bank account number, consisting of 10 numbers");
        document.querySelector("#existingBankAccount").value = "";
    }

}

function validateSecurityCode() {
    let regexOnlyNumbers = new RegExp("^[0-9]+$");
    let securityCode = document.querySelector("#securityCode").value;
    if ((!regexOnlyNumbers.test(securityCode)) || securityCode.length > 5 || securityCode.length < 5) {
        toastr["error"]("fill in a valid securityCode, numbers only");
        document.querySelector("#securityCode").value = "";
    }
}
//endregion

function showExplanation(){
    toastr["info"]("the BankAccount number matches the last 10 digits of the IBAN");
}
