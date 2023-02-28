function checkSessionStorageForId() {
    if (sessionStorage.getItem("id") === null) {
        console.log("not logged in");
        window.location.href = "http://localhost:8080/welcome.html";
    }
}

function checkSessionStorageForBankProps(){
    if (sessionStorage.getItem("bankName") === null) {
        console.log("failed to load bankdetails");
        window.location.href = "http://localhost:8080/welcome.html";
    }
}