window.onload = setId();
window.onload = checkSessionStorageForId();

function setId() {
    userId = sessionStorage.getItem("id");
    document.querySelector("#userId").innerHTML = userId;
    console.log(userId);
}

function loadTableData() {
    loadBankAccountData()
    loadApprovalData()
}

function loadBankAccountData() {

    var myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    var requestOptions = {
        method: 'GET', headers: myHeaders, redirect: 'follow',

    };
    let table = document.getElementById("bankAccountsTableSortedOnBalance");
    let urlToFetch = "http://localhost:8080/api/bankaccounts/bankAccountsByBalance?id=" + userId;
    let urlToOpen = null;
    let keyNameToSetToSessions = "aaaaa";

    //TODO filter in descending order, add session
    fillTable(urlToFetch, table, requestOptions, urlToOpen, keyNameToSetToSessions)

}

function loadApprovalData () {
    var myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    var requestOptions = {
        method: 'GET', headers: myHeaders, redirect: 'follow',

    };
    let table = document.getElementById("pendingApprovalsTable");
    let urlToFetch = "http://localhost:8080/api/approvals/pending?accountManagerId=" + sessionStorage.getItem("id");
    let urlToOpen = "http://localhost:8080/pendingApprovalPage.html";
    let keyNameToSetToSessions = "selectedApprovalId";

    fillTable(urlToFetch, table, requestOptions, urlToOpen, keyNameToSetToSessions)
}

function goToPinConnectionCodePage() {
    sessionStorage.setItem("previousPage", "http://localhost:8080/accountManagerDashboard.html")
    window.location='http://localhost:8080/pinConnectionPage.html'
}

setWindowTitle("Account manager dashboard");

function setWelcomeName()  {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch("http://localhost:8080/api/useraccounts/"+userId, requestOptions)
        .then(response => response.text())
        .then(result => {
            var myObj = JSON.parse(result);
            document.querySelector("#name").innerHTML = myObj.name;
        })
        .catch(error => console.log('error', error));
}

setWelcomeName();

