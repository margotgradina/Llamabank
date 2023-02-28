let userId;
let bankAccountNr;
let bankAccountBalance;
let bankName;
let bankCode;
window.onload = checkSessionStorageForId(), setPageVariablesFromSession();

function setPageVariablesFromSession() {
    bankAccountNr = sessionStorage.getItem("bankAccountNr");
    bankAccountBalance = sessionStorage.getItem("balance");
    userId = sessionStorage.getItem("id");
    bankCode = sessionStorage.getItem("bankCode");
    bankName = sessionStorage.getItem("bankName");

    document.querySelector("#bankname").innerHTML = bankName;
    document.querySelector("#banklogo").src = "/images/" + bankCode + "-logo.png";

    document.querySelector("#userBankAccount").innerHTML = bankAccountNr;
    document.querySelector("#balance").innerHTML = "€" + parseInt(bankAccountBalance).toFixed(2);
    console.log(bankAccountNr);
}

const getNewCases = async () => {
    const response = await fetch('http://localhost:8080/api/transactions/bankAccountNr/' + bankAccountNr);
    const data = await response.json();
    for (let transaction of data) {
        transaction.dateTime = moment(transaction.dateTime).format('YYYY-MM-DD hh:mm');
        transaction.amount = transaction.amount.toFixed(2);
        if (bankAccountNr === transaction.fromIBANBankAccountNr.substr(8)) {
            transaction.amount = "€-" + transaction.amount;
            transaction.amount = transaction.amount.fontcolor('red');
        } else {
            transaction.amount = "€" + transaction.amount;
            transaction.amount = transaction.amount.fontcolor('green');
        }
    }
    $('#loadingLabel').hide();
    $('#transactionTable').DataTable({
        data: data,
        bLengthChange: false,
        aaSorting: [[0, "desc"]],
        columns: [
            {data: 'dateTime', title: 'Date'},
            {data: 'amount', title: 'Amount'},
            {data: 'fromIBANBankAccountNr', title: 'From BankAccount'},
            {data: 'toIBANBankAccountNr', title: 'To BankAccount'},
            {data: 'message', title: 'Message'},
            {data: 'id', title: 'ID'}

        ],
        columnDefs: [
            {
                targets: -5,
                className: 'dt-body-right'
            }
        ]

    });
};

const getAccountholders = async () => {
    const response = await fetch('http://localhost:8080/api/bankaccounts/accountholders/' + bankAccountNr);
    const data = await response.json();
    let names = "";
    for (let accountholders of data) {
        console.log(accountholders);
        names += accountholders + "<br>";
    }
    document.querySelector("#accountholders").innerHTML = names;
};

function requestPin() {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "clientId": userId,
        "linkedBankAccountNumber": bankAccountNr
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/api/approvals/new/pinmachineapproval", requestOptions)
        .then(response => response.text())
        .then(result => {
            console.log(result);
            showPendingAndApprovedPinTerminals();
            toastr["success"]("Request for Pin Terminal is successfully received.");
        })
        .catch(error => {
            console.log('error', error);
            toastr["error"](error);
        });
}

const showPendingAndApprovedPinTerminals = async () => {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    const response = await fetch('http://localhost:8080/api/approvals/count/'+bankAccountNr, requestOptions);
    const data = await response.json();
    let pendingCounter = 0;
    let approvalCounter = 0;
    for (let approvalStatuses of data) {
        console.log(approvalStatuses.counter);
        if (approvalStatuses.approvalStatus == "pending") {
            pendingCounter = approvalStatuses.counter
        }
        if (approvalStatuses.approvalStatus == "approved") {
            approvalCounter = approvalStatuses.counter
        }
        document.querySelector("#pending").innerHTML = pendingCounter;
        document.querySelector("#approved").innerHTML = approvalCounter;
    }
};


function showSpecificSmallBusinessAccountInformation() {
    console.log(bankAccountNr);
    //check if bankaccount is smallbusiness (1) or private (2)
    if (bankAccountNr[0] === "2") {
        document.getElementById("SBA").style.display = "none";
        document.querySelector("#accountType").innerHTML = "Private Bank Account";
    } else {
        document.querySelector("#accountType").innerHTML = "Business Bank Account";
        showPendingAndApprovedPinTerminals();
    }
}

showSpecificSmallBusinessAccountInformation();
getNewCases();
getAccountholders();
