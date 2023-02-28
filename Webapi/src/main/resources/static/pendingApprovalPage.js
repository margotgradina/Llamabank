window.onload = checkSessionStorageForId();

var approvalData;
const cardBody = document.getElementById("approvalSpecificsData");
const cardTitle = document.getElementById("approvalSpecificsTitle");

async function getApprovalData(approvalId) {
    const requestOptions = {
        method: 'GET', redirect: 'follow'
    };

    return fetch("http://localhost:8080/api/approvals/" + approvalId, requestOptions)
        .then(response => response.json())
        .catch(error => console.log('error', error));
}

async function setApprovalData() {
    approvalData = await getApprovalData(sessionStorage.getItem("selectedApprovalId"));
}

async function fillApprovalData() {
    await setApprovalData();
    document.getElementById("approvalId").value = approvalData.id;
    document.getElementById("approvalStatus").value = approvalData.approvalStatus;
    document.getElementById("clientId").value = approvalData.client.id;
    document.getElementById("clientName").value = approvalData.client.name;
    document.getElementById("creationTime").value = approvalData.creationTime;

    if (approvalData.approvalStatus === "approved" || approvalData.approvalStatus.approvalStatus === "denied") {
        document.getElementById("approveButton").setAttribute("disabled", "disabled")
        document.getElementById("denyButton").setAttribute("disabled", "disabled")
    }


    switch (approvalData.type) {
        case 'OpenBankAccountApproval':
            fillOpenBankAccountApprovalData()
            break
        case 'AddAccountHolderApproval':
            fillAddAccountHolderApprovalData()
            break
        case 'PinMachineApproval':
            fillPinMachineApprovalData()
            break
    }
}

function fillAddAccountHolderApprovalData() {
    document.getElementById('approvalSpecificsTitle').innerText = 'Add account holder request'
    document.getElementById('bankAccountNumber').value = approvalData.bankAccount.accountNumber

    var newAccountHolderIdColumn = createFieldColumn('newAccountHolder.id', "New account holder id")
    var newAccountHolderNameColumn = createFieldColumn('newAccountHolder.name', "New account holder name")

    var row = document.createElement('div')
    row.className = 'row'
    row.appendChild(newAccountHolderIdColumn)
    row.appendChild(newAccountHolderNameColumn)

    document.getElementById('approvalSpecificsData').appendChild(row)

    document.getElementById('newAccountHolder.id').value = approvalData.newAccountHolder.id
    document.getElementById('newAccountHolder.name').value = approvalData.newAccountHolder.name
}

function fillPinMachineApprovalData() {
    document.getElementById('approvalSpecificsTitle').innerText = 'Pin machine request'
    document.getElementById('bankAccountNumber').value = approvalData.linkedBankAccount.accountNumber

    var sectorColumn = createFieldColumn('linkedBankAccount.sector', 'Business sector')
    document.getElementById('bankAccountNumberRow').appendChild(sectorColumn)
    document.getElementById('linkedBankAccount.sector').value = approvalData.linkedBankAccount.sector


    var activatedColumn = createFieldColumn('consumed', 'Pin machine activation status')

    if (approvalData.approvalStatus === 'approved') {
        var row = document.createElement('div')
        row.className = 'row'
        row.appendChild(activatedColumn)
        document.getElementById('approvalSpecificsData').appendChild(row)
        document.getElementById('consumed').value = approvalData.consumed ? 'true' : 'false'
    }
}

function fillOpenBankAccountApprovalData() {
    var row = document.createElement('div')
    row.className = 'row'

    var bankAccountTypeColumn = createFieldColumn('openBankAccountType', "Bank account type")
    var sectorColumn = createFieldColumn('smallBusinessSector', 'Business sector')

    row.appendChild(bankAccountTypeColumn)

    document.getElementById('approvalSpecificsData').appendChild(row)
    if (approvalData.smallBusinessSector !== null) {
        row.appendChild(sectorColumn)
        document.getElementById('smallBusinessSector').value = approvalData.smallBusinessSector
    }

    document.getElementById("bankAccountNumber").value = approvalData.bankAccountNumber

    document.getElementById('openBankAccountType').value = approvalData.openBankAccountType
    document.getElementById('approvalSpecificsTitle').innerText = 'Open Bank Account Request'
}

async function processRequest(decision) {
    var headers = new Headers()
    headers.append("Content-Type", "application/json")

    var body = JSON.stringify({
        "approvalId": sessionStorage.getItem('selectedApprovalId'),
        "approvalStatus": decision
    })

    var requestOptions = {
        method: 'POST',
        headers: headers,
        body: body,
        redirect: 'follow'
    }

    var response = await fetch("http://localhost:8080/api/approvals/setapprovalstatus", requestOptions)

    var data = await response.text()

    console.log(response.status)
    console.log(response.statusText)

    if (response.status === 200) {
        console.log(data)
        setTimeout(function () {
            window.location.href = "http://localhost:8080/accountManagerDashboard.html"
        }, 2000)
        toastr["success"]("Approval " + approvalData.id +" successfully " + decision);
    } else {
        console.log(data)
        toastr["error"]("Error");
    }
}

function clientContactInfo() {
    var contactDetails = 'Client contact details:' + '\n'
        + '\tName: ' + approvalData.client.name + '\n'
        + '\tEmail: ' + approvalData.client.email + '\n'
        + '\tTelephone: ' + approvalData.client.phoneNumber + '\n'
        + '\n\tAddress: ' + '\n'
        + '\tStreet: ' + approvalData.client.address.street + '\n'
        + '\tStreet number: ' + approvalData.client.address.houseNumber + (approvalData.client.address.houseNumberExtension !== null ? approvalData.client.address.houseNumberExtension : '') + '\n'
        + '\tPostal code: ' + approvalData.client.address.postalCode + '\n'
        + '\tCity: ' + approvalData.client.address.city + '\n'
        + '\tRegion: ' + (approvalData.client.address.region !== null ? approvalData.client.address.region : '') + '\n'
        + '\tCountry: ' + approvalData.client.address.country

    if (approvalData.type === 'AddAccountHolderApproval') {
        var newAccountHolderContactDetails = 'New account holder contact details:' + '\n'
            + '\tName: ' + approvalData.newAccountHolder.name + '\n'
            + '\tEmail: ' + approvalData.newAccountHolder.email + '\n'
            + '\tTelephone: ' + (approvalData.newAccountHolder.phoneNumber !== null ? approvalData.newAccountHolder.phoneNumber : '') + '\n'
            + '\n\tAddress: ' + '\n'
            + '\tStreet: ' + approvalData.newAccountHolder.address.street + '\n'
            + '\tStreet number: ' + approvalData.newAccountHolder.address.houseNumber + approvalData.newAccountHolder.address.houseNumberExtension + '\n'
            + '\tPostal code: ' + approvalData.newAccountHolder.address.postalCode + '\n'
            + '\tCity: ' + approvalData.newAccountHolder.address.city + '\n'
            + '\tRegion: ' + (approvalData.newAccountHolder.address.region !== null ? approvalData.newAccountHolder.address.region : '') + '\n'
            + '\tCountry: ' + approvalData.newAccountHolder.address.country

        contactDetails = contactDetails + '\n\n' + newAccountHolderContactDetails
    }

    alert(contactDetails)
}

function createFieldColumn(propertyName, labelText) {
    var columnElement = document.createElement('div')
    columnElement.className = 'col-sm-6'

    var labelElement = document.createElement('label')
    labelElement.setAttribute('for', propertyName)
    labelElement.innerHTML = labelText


    var fieldElement = document.createElement('input')
    fieldElement.className = 'form-control'
    fieldElement.type = 'text'
    fieldElement.id = propertyName
    fieldElement.name = propertyName
    fieldElement.disabled = true

    columnElement.appendChild(labelElement)
    columnElement.appendChild(fieldElement)
    return columnElement
}

// TODO remove once website is completely implemented
async function loadBankProperties() {
    const response = await (fetch("http://localhost:8080/api/configuration/properties"));
    const properties = await response.json();

    for (let property in properties) {
        sessionStorage.setItem(property, properties[property])
    }
}



loadBankProperties();
setWindowTitle("Approval Info");
fillApprovalData();
