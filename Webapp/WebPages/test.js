var styles = {
    input: {
      color: "#000645",
      "font-family": "system-ui, -apple-system, BlinkMacSystemFont",
      "letter-spacing": "0.02em"
    },
    ".invalid": {
      color: "#cc0001"
    },
    "::placeholder": {
      color: "#757575"
    }
  };
  
  var hostedFields = assembly.hostedFields({
    environment: "pre-live"
  });
  var accountName = hostedFields.create("bankAccountName", {
    styles: styles,
    placeholder: "Account Holder Full Name"
  });
  var routingNumber = hostedFields.create("bankRoutingNumber", {
    styles: styles
  });
  var accountNumber = hostedFields.create("bankAccountNumber", {
    styles: styles
  });
  accountName.mount("#account-name-field");
  routingNumber.mount("#routing-number-field");
  accountNumber.mount("#account-number-field");
  
  var fields = [accountName, routingNumber, accountNumber];
  fields.forEach((field) => {
    field.on("change", function (event) {
      var errorElement = document.querySelector(
        "." + event.fieldType + "-container .error"
      );
      toggleError(errorElement, event);
    });
  });
  
  var form = document.querySelector("form");
  var submitButton = form.querySelector(".button");
  var responseContainer = document.querySelector(".response");
  
  function toggleError(element, event) {
    if (event.error) {
      element.innerText = event.error.message;
      element.classList.add("visible");
    } else {
      element.innerText = "";
      element.classList.remove("visible");
    }
  }
  
  form.addEventListener("submit", function (event) {
    event.preventDefault();
    submitButton.disabled = true;
    submitButton.classList.add("button-loading");
  
    hostedFields
      .createBankAccount({
        token: "YOUR_AUTH_TOKEN",
        user_id: "YOUR_USER_ID",
        bank_name: form.querySelector("#bank-name-field").value,
        account_type: form.querySelector("#account-type-field").value,
        holder_type: form.querySelector("#holder-type-field").value,
        country: form.querySelector("#country-field").value,
        payout_currency:
          form.querySelector("#payout-currency-field").value || undefined,
        currency: form.querySelector("#currency-field").value || undefined
      })
      .then(function (response) {
        resetSubmitButton();
        // handle create card account succeeded
        responseContainer.classList.add("response-success");
        responseContainer.innerText = "Bank account successfully created.";
      })
      .catch(function (response) {
        resetSubmitButton();
        // handle errors
        responseContainer.classList.add("response-error");
        if (response.errors && response.errors.token) {
          responseContainer.innerText = "Your token is not authorized.";
        } else {
          responseContainer.innerText =
            "There was an error creating your bank account.";
        }
      });
  });
  
  function resetSubmitButton() {
    submitButton.disabled = false;
    submitButton.classList.remove("button-loading");
  }
  
  