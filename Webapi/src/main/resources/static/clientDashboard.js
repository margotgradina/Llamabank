window.onload = setId();
window.onload = checkSessionStorageForId();

function setId() {
    userId = sessionStorage.getItem("id");
    document.querySelector("#userId").innerHTML = userId;
    console.log(userId);
}

function loadTableData() {

    var myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    var requestOptions = {
        method: 'GET', headers: myHeaders, redirect: 'follow',

    };
    const table = document.getElementById("bankAccountsTable");


    const urlToFetch = 'http://localhost:8080/api/bankaccounts?id=' + userId;
    const urlToOpen = "http://localhost:8080/transactions.html";
    const keyNameToSetToSessions = "bankAccountNr";


    fillTable(urlToFetch, table, requestOptions, urlToOpen, keyNameToSetToSessions);

}

/**
 * function to hover over row
 * @param cellElement
 */
function colorChangeOnHover(cellElement) {
    cellElement.onmouseover = function () {
        this.parentNode.style.backgroundColor = "#f0caa6";
    }
    cellElement.onmouseout = function () {
        this.parentNode.style.backgroundColor = "#fff";
    }
}

/**
 *
 * @param node child node
 * @param keyName
 */
function setValueToSessionOnClick(node, keyName) {
    const valueName = node.parentNode.firstElementChild.textContent;
    console.log(valueName);
    sessionStorage.setItem(keyName, valueName);
}

/**
 *
 * @param node
 */
function setValueBalanceToSessionOnClick(node) {
    const valueNameBalance = node.parentNode.children.item(2).textContent;
    console.log(valueNameBalance);
    sessionStorage.setItem("balance", valueNameBalance);
}


/**
 *
 * @param urlToFetch
 * @param table
 * @param requestOptions
 * @param urlToOpen
 * @param keyName
 * @returns {Promise<void>}
 */
async function fillTable(urlToFetch, table, requestOptions, urlToOpen, keyName) {
    const tableHead = table.querySelector("thead")
    const tableBody = table.querySelector("tbody")

    const response = await fetch(urlToFetch, requestOptions);
    const data = await response.json();
    console.log(data[0]);

    //clear table
    tableHead.innerHTML = "<tr></tr>";
    tableBody.innerHTML = "";

    //creating headers
    for (const headerText in data[0]) {
        const HeaderElement = document.createElement('th');
        HeaderElement.textContent = headerText;
        tableHead.querySelector('tr ').appendChild(HeaderElement);
    }

    //filling table in with content
    for (let i = 0; i < data.length; i++) {
        const obj = Object.values(data[i]);
        const rowElement = document.createElement("tr");

        for (const cellText of obj) {
            const cellElement = document.createElement("td");
            cellElement.textContent = cellText;

            //changes color on hover
            colorChangeOnHover(cellElement);
            //choose bank account number on click

            if (urlToOpen !== null) {
                cellElement.onclick = function () {
                    setValueToSessionOnClick(this, keyName);
                    setValueBalanceToSessionOnClick(this);
                    //opens new page on click
                    window.location.href = urlToOpen;
                }
            } else {
               //TODO remove else condition?
                cellElement.onclick = function () {
                    setValueToSessionOnClick(this, keyName);
                    setValueBalanceToSessionOnClick(this);
                }
            }
            rowElement.appendChild(cellElement);
        }
        tableBody.appendChild(rowElement)
    }
}

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
