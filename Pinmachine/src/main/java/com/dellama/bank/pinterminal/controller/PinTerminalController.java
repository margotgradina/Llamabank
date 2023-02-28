package com.dellama.bank.pinterminal.controller;

import com.dellama.bank.pinterminal.dto.ConnectionDTO;
import com.dellama.bank.pinterminal.dto.ConnectionResponseDTO;
import com.dellama.bank.pinterminal.dto.TransactionDTO;
import com.dellama.bank.pinterminal.launcher.Application;
import com.dellama.bank.pinterminal.model.PinMachine;
import com.dellama.bank.pinterminal.model.service.ModelService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class PinTerminalController {
    //<editor-fold desc="Digit Buttons">
    public Button btnDigit0;
    public Button btnDigit1;
    public Button btnDigit2;
    public Button btnDigit3;
    public Button btnDigit4;
    public Button btnDigit5;
    public Button btnDigit6;
    public Button btnDigit7;
    public Button btnDigit8;
    public Button btnDigit9;
    public Button btnOk;
    public Button btnDelete;
    public Button btnStop;
    public Button btnDot;
    ModelService modelService = Application.getModelService();
    //</editor-fold>
    private String input = "";
    private Date date;
    private Double amount;
    private String fromIBAN;
    private String toIBAN;
    private String identificationNumberPin;
    private String pinCode;
    private String iban;
    private int inputCode;
    private PinMachine pinMachine = Application.getPinMachine();
    private boolean pinOpened;
    private Button prevButtonClicked;
    private LocalDateTime whenLastCharClicked;
    private int charCounter;

    @FXML
    private TextArea pinDisplay;

    private int CHAR_TIME_DIFFERENCE = 1250; //in milliseconds
    @FXML
    private ToggleButton textInputOnOff;
    @FXML
    private GridPane keypad;

    public void initialize() {
        //add chars to digit buttons, didn't succeed doing this at design time in SceneBuilder
        btnDigit1.setText(btnDigit1.getText() + "\nABC");
        btnDigit2.setText(btnDigit2.getText() + "\nDEF");
        btnDigit3.setText(btnDigit3.getText() + "\nGHI");
        btnDigit4.setText(btnDigit4.getText() + "\nJKL");
        btnDigit5.setText(btnDigit5.getText() + "\nMNO");
        btnDigit6.setText(btnDigit6.getText() + "\nPQR");
        btnDigit7.setText(btnDigit7.getText() + "\nSTU");
        btnDigit8.setText(btnDigit8.getText() + "\nVWX");
        btnDigit9.setText(btnDigit9.getText() + "\nYZ");
        modelService.loadPinMachineDetails();
        this.disableKeyboard(true);

    }

    //<editor-fold desc="transaction">

    /**
     * Starts a payment in the pinterminal. Checks if pin is opened and connected first.
     *
     * @param actionEvent
     */
    public void startPayment(ActionEvent actionEvent) {
        if (this.pinOpened != true) {
            pinDisplay.setText("Open pin first");
        } else if (!this.pinMachine.isConnected()) {
            pinDisplay.setText("Connect pin first");
        } else {
            disableKeyboard(false);
            toIBAN = pinMachine.getIbanBankAccountNumber();
            input = "";
            startTransAction();
        }
    }

    /**
     * Continuation of StartPayment Method. Lets user put in the amount of the transaction in double.
     */
    private void startTransAction() {
        textInputOnOff.setDisable(true);
        pinDisplay.setText("Type in amount");
        btnOk.setOnAction(event -> {
            amount = Double.valueOf(input);
            input = "";
            textInputOnOff.setDisable(false);
            setIBAN();
        });
    }

    /**
     * Continuation of StartPayment method. Lets user put in the IBAN account.
     */
    private void setIBAN() {
        pinDisplay.setText("Client: type in IBAN bankaccount and push ok");
        btnDot.setDisable(true);
        textInputOnOff.setSelected(true);
        resetCharTracker();
        btnOk.setOnAction(event -> {
            textInputOnOff.setSelected(false);
            fromIBAN = pinDisplay.getText();
            input = "";
            setPinCode();
        });
    }

    /**
     * Continuation of StartPayment method. Lets user fill in a pincode consisting of digits only
     * This code is currently not being checked, this falls outside the scope.
     */
    private void setPinCode() {
        textInputOnOff.setDisable(true);
        btnDot.setDisable(true);
        pinDisplay.setText("Client: type in pincode and push ok");
        pinCode = input;
        btnOk.setOnAction(event -> {
            input = "";
            textInputOnOff.setDisable(false);
            btnDot.setDisable(false);
            try {
                sendData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * makes call to send transaction to the webapi. Result shows whether the request for transaction has succeeded.
     *
     * @throws IOException
     */
    public void sendData() throws IOException {
        String transactionMessage = "PinTransaction from pinmachineNr " + pinMachine.getPinMachineNumber();
        System.out.println("amount: " + amount + "\nfrom BankAccount: " + fromIBAN + "\nto BankAccount: " + toIBAN);
        String result = modelService.makeTransaction(new TransactionDTO(transactionMessage, amount, fromIBAN, toIBAN));
        pinDisplay.setText(result);
        this.disableKeyboard(true);
    }
    //</editor-fold>

    //<editor-fold desc="open/close">

    /**
     * Closes the pinmachine. Upon closing, the contents of the Transactionhistory of the current day are shown in a new screen.
     *
     * @param actionEvent
     */
    public void closeJournal(ActionEvent actionEvent) {
        if (this.pinOpened != true) {
            pinDisplay.setText("Pin is already closed");
        } else {
            pinDisplay.setText("Good bye! Journal closed.");
            this.pinOpened = false;

            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = LocalDateTime.now().format(formatterDate);
            File file = new File("src/main/resources/Transactionhistory-" + formattedDate + ".txt");

            StringBuilder sB = new StringBuilder();

            try {
                Scanner sc = new Scanner(file);     //file to be scanned
                while (sc.hasNextLine())        //returns true if and only if scanner has another token
                    sB.append(sc.nextLine() + "\n");

            } catch (Exception e) {
                e.printStackTrace();
            }

            modelService.savePinMachineDetails(pinMachine);
            Application.getSceneManager().createJournal(sB.toString());
        }
    }

    /**
     * Opens the pinmaachine. In case it is a new day, a new Transaction history journal text file will be created.
     *
     * @param actionEvent
     * @throws InterruptedException
     */
    public void openJournal(ActionEvent actionEvent) throws InterruptedException {
        if (this.pinOpened) {
            pinDisplay.setText("Pin is already opened");
        } else {
            if (pinMachine.isConnected()) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                String currentDate = dateFormat.format(date);
                //TODO LATEN WACHTEN OP OPENEN
                pinDisplay.setText("Opening Journal... " + currentDate);
                //TODO SET TRANSACTION ID
                setPinToOpened(modelService.createJournalPage(LocalDateTime.now()));

            } else {
                pinDisplay.setText("Connect pin machine first");
            }
        }
    }

    /**
     * Sets status of pin to opened
     *
     * @param isOpened
     */
    public void setPinToOpened(boolean isOpened) {
        if (isOpened) {
            this.pinOpened = true;
            pinDisplay.setText(String.valueOf("Pin opened"));
        } else {
            this.pinOpened = false;
            pinDisplay.setText(String.valueOf("Pin opening failed"));
        }
    }
    //</editor-fold>

    //<editor-fold desc="connection">
    public void startConnectionPinMachine(MouseEvent mouseEvent) {
        if (pinMachine.isConnected()) {
            pinDisplay.setText("Pin is already connected");
        } else {
            disableKeyboard(false);
            pinDisplay.setText("IBAN: ");
            btnDot.setDisable(true);
            textInputOnOff.setSelected(true);
            resetCharTracker();
            getIban();
        }
    }

    public String getIban() {
        btnOk.setOnAction(event -> {
            textInputOnOff.setSelected(false);
            iban = pinDisplay.getText();
            input = "";
            btnDot.setDisable(false);
            pinDisplay.forward();
            pinDisplay.clear();
            getCode(iban);
        });
        return iban;
    }

    private void getCode(String iban) {
        pinDisplay.setText("Code: ");
        textInputOnOff.setDisable(true);
        btnDot.setDisable(true);
        btnOk.setOnAction(event -> {

            inputCode = Integer.parseInt(input);
            input = "";
            pinDisplay.setText("");
            textInputOnOff.setDisable(false);
            btnDot.setDisable(false);

            ConnectionDTO connectionDTO = new ConnectionDTO(iban, inputCode);
            try {
                checkCode(connectionDTO);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        });
    }

    private void checkCode(ConnectionDTO connectionDTO) throws IOException, InterruptedException {

        ConnectionResponseDTO connectionResponse = modelService.checkConnection(connectionDTO);
        identificationNumberPin = String.valueOf(connectionResponse.getIdentificationNumber());
        pinDisplay.setText(connectionResponse.getMessage());
        if (connectionResponse.isConnectionApproved) {
            setPinMachineDetails();
            disableKeyboard(true);
        }
    }

    /**
     * Sets the details of the pinmachine and writes these to an external txt file
     */
    private void setPinMachineDetails() {
        pinMachine.setIbanBankAccountNumber(iban);
        //TODO change pinMachineNumber from hardcoded to generated
        pinMachine.setPinMachineNumber(identificationNumberPin);
        modelService.savePinMachineDetails(pinMachine);
    }
    //</editor-fold>

    //<editor-fold desc="other">

    /**
     * Turns on/off input for letters
     *
     * @param actionEvent
     */
    public void clickTextInputOnOff(ActionEvent actionEvent) {
        resetCharTracker();
    }

    private void resetCharTracker() {
        if (textInputOnOff.isSelected()) {
            //reset timer
            whenLastCharClicked = LocalDateTime.now().minus(CHAR_TIME_DIFFERENCE, ChronoField.MILLI_OF_DAY.getBaseUnit());
            //reset character counter
            charCounter = -1;
            prevButtonClicked = null;
        }
    }

    public void clickOk(ActionEvent actionEvent) {
        input = pinDisplay.getText();
    }

    public void clickDigit(ActionEvent actionEvent) {
        //which button is clicked
        Button currentButtonClicked = (Button) actionEvent.getSource();

        String[] buttonTextLines = currentButtonClicked.getText().split("\n");
        if (!textInputOnOff.isSelected()) { //digit modus
            //first line of button text contains the digit
            input = input + buttonTextLines[0];
        } else { //character modus
            LocalDateTime now = LocalDateTime.now();

            System.out.println("Button pressed at " + now);
            System.out.println("Previous click on this button was at " + whenLastCharClicked);
            long timeDifference = ChronoUnit.MILLIS.between(whenLastCharClicked, now);
            System.out.println(timeDifference);

            whenLastCharClicked = now;
            boolean sameButton = prevButtonClicked == currentButtonClicked;
            prevButtonClicked = currentButtonClicked;

            //second line of button text contains the characters
            //check if button has chars
            if (buttonTextLines.length < 2) return;

            String chars = buttonTextLines[1];

            //if too much time has passed OR not same buttons were pressed reset charCounter
            if (timeDifference > CHAR_TIME_DIFFERENCE || !sameButton) charCounter = 0;
            else {
                charCounter++;
                input = input.substring(0, input.length() - 1);
            }

            String currentChar = chars.substring(charCounter % chars.length(), charCounter % chars.length() + 1);
            input = input + currentChar;
        }

        pinDisplay.setText(input);
    }

    public void clickDelete(ActionEvent actionEvent) {
        resetCharTracker();
        if (input.length() > 0) {
            input = input.substring(0, input.length() - 1);
            pinDisplay.setText(input);
        }
    }

    public void disableKeyboard(boolean toggleOnOff) {
        btnDigit0.setDisable(toggleOnOff);
        btnDigit1.setDisable(toggleOnOff);
        btnDigit2.setDisable(toggleOnOff);
        btnDigit3.setDisable(toggleOnOff);
        btnDigit4.setDisable(toggleOnOff);
        btnDigit5.setDisable(toggleOnOff);
        btnDigit6.setDisable(toggleOnOff);
        btnDigit7.setDisable(toggleOnOff);
        btnDigit8.setDisable(toggleOnOff);
        btnDigit9.setDisable(toggleOnOff);
        textInputOnOff.setDisable(toggleOnOff);
        btnOk.setDisable(toggleOnOff);
        btnStop.setDisable(toggleOnOff);
        btnDelete.setDisable(toggleOnOff);
        btnDot.setDisable(toggleOnOff);
    }

    public void clickDot(ActionEvent actionEvent) {
        input = input + ".";
        pinDisplay.setText(input);
    }

    public void clickStop(ActionEvent actionEvent) {
        pinDisplay.setText("action stopped");
        input = "";
        disableKeyboard(true);
        textInputOnOff.setSelected(false);
    }

    //</editor-fold>
}

