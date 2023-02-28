var fullBankApiUrl = 'AUTO_FILLED'
console.log(fullBankApiUrl);

function loadBankProperties() {
    console.log("start fetching " + performance.now());
    return fetch(fullBankApiUrl + "/api/configuration/properties")
        .then(response => {
            console.log("done fetching 1 " + performance.now());
            return response.json();
        })
        .then(properties => {
            console.log("done fetching 2 " + performance.now());
            for (let property in properties) {
                sessionStorage.setItem(property, properties[property])
            }
            console.log("done fetching 3 " + performance.now());
        });
}

function setWindowTitle(title) {
    document.title = sessionStorage.getItem("bankNameUpperCase") + " | " + title;
}