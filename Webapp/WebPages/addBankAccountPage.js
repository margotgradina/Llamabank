function showForm(typeForm) {
    console.log(typeForm);


    if (typeForm === "smallBusinessAccount") {
        //sets the label to SB and makes visible
        document.querySelector("#labelAddAccount").innerHTML = "Add a Small Business Account";
        showAndEnable("#labelAddAccount");
        showAndEnable("#sectorSelect");
        hideAndDisable("#existingBankAccount");
        hideAndDisable("#securityCode");

    } else if (typeForm === "privateAccount") {
        hideAndDisable("#labelAddAccount");
        hideAndDisable("#sectorSelect");
        hideAndDisable("#existingBankAccount");
        hideAndDisable("#securityCode");

    } else if (typeForm === "existingAccount") {
        //sets the label to addExistingAccount and makes visible
        document.querySelector("#labelAddAccount").innerHTML = "Add an existing Account";
        showAndEnable("#labelAddAccount");
        showAndEnable("#existingBankAccount");
        showAndEnable("#securityCode");
        hideAndDisable("#sectorSelect");

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

    function sendApplication() {
        let formType = document.querySelector('input[name="group1"]:checked').value;

        if (formType === "smallBusinessAccount") {
            console.log("adding SBAccount")
          // TODO
            // create OpenBankAccountRequest for smallBusinessAccount

        } else if (formType === "privateAccount") {
            console.log("adding privateAccount")
            // TODO
            // create OpenBankAccountRequest for privateAccount
        } else if (formType === "existingAccount"){
            console.log("adding existingAccount")
            //TODO
            // create addAccountHolderRequest
        }
    }


}