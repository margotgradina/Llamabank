window.onload = checkSessionStorageForId();
window.onload = setSession();

toastr.options = {
    "closeButton": true,
    "debug": false,
    "newestOnTop": false,
    "progressBar": false,
    "positionClass": "toast-bottom-center",
    "preventDuplicates": true,
    "onclick": null,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
}
function logout(){
    sessionStorage.removeItem("bankAccountNr");
    sessionStorage.removeItem("id");
    window.location.href = 'http://localhost:8080/welcome.html'
}

function setSession() {
    let bankAccountNr = sessionStorage.getItem("bankAccountNr");
    document.querySelector("#bankAccountNumber").value = bankAccountNr;
    $("#banklogo").attr("src", "/images/" + sessionStorage.getItem("bankCode") + "-logo.png");
    // $("#banklogo").attr("alt", sessionStorage.getItem("bankName"));
    $("#bankname").text(sessionStorage.getItem("bankName"));
}

function validateSecurityCode() {
    let regexOnlyNumbers = new RegExp("^[0-9]+$");
    let securityCode = document.querySelector("#securityCode").value;
    if ((!regexOnlyNumbers.test(securityCode)) || securityCode.length > 5 || securityCode.length < 5) {
        toastr["error"]("fill in a valid securityCode, 5 numbers only");
        document.querySelector("#securityCode").value = "";
    }
}

function validateNewAccountHolderId() {
    let regexOnlyNumbers = new RegExp("^[0-9]+$");
    let newAccountHolderId = document.querySelector("#newAccountHolderId").value;
    if (!regexOnlyNumbers.test(newAccountHolderId)) {
        toastr["error"]("fill in a valid accountHolderId, numbers only");
        document.querySelector("#newAccountHolderId").value = "";
    } else if (newAccountHolderId === sessionStorage.getItem("id")){
        toastr["error"]("you cannot add yourself, fill in a valid accountHolderId");
        document.querySelector("#newAccountHolderId").value = "";
    }
}

function sendAddAccountHolderRequest() {
    let securityCode = document.querySelector("#securityCode").value;
    let newAccountHolderId = document.querySelector("#newAccountHolderId").value;
    let userId = sessionStorage.getItem("id");
    let bankAccountNumber = document.querySelector("#bankAccountNumber").value;

    if (newAccountHolderId.length === 0){
        toastr["error"]("fill in new accountholder id");
    } else if(securityCode.length === 0){
        toastr["error"]("fill in valid security code");
    } else {

        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
            "clientId": userId,
            "bankAccountNumber": bankAccountNumber,
            "securityCode": securityCode,
            "newAccountHolderId": newAccountHolderId
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
    let response = await fetch("http://localhost:8080/api/approvals/new/addaccountholderapproval", requestOptions);
    console.log(response.status);
    console.log(response.statusText);
    let data = await response.text();

    if (response.status === 200) {
        console.log(data);
        setTimeout(function () {
            window.location.href = "http://localhost:8080/clientDashboard.html";
        }, 3000);
        toastr["success"]("request to add new AccountHolder successful");
    } else {
        console.log(data);
        toastr["error"]("invalid request");
    }
}

var coll = document.getElementsByClassName("collapsible");
for (var i = 0; i < coll.length; i++) {
    coll[i].addEventListener("click", function () {
        this.classList.toggle("active");
        var content = this.nextElementSibling;
        if (content.style.display === "block") {
            content.style.display = "none";
        } else {
            content.style.display = "block";
        }
    });
}

