package com.dellama.bank.pinterminal.persistency;

import com.dellama.bank.pinterminal.dto.TransactionDTO;
import com.dellama.bank.pinterminal.launcher.Application;
import com.dellama.bank.pinterminal.model.PinMachine;
import com.dellama.bank.pinterminal.model.service.PersistencyService;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TextPersistencyImpl implements PersistencyService {

    /**
     * Adds a transaction to the journalpage of the given date
     * @param transactionDTO
     * @param localDateTime
     * @return true if added, false if not.
     */
    @Override
    public boolean addTransactionToJournal(TransactionDTO transactionDTO, LocalDateTime localDateTime) {
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = localDateTime.format(formatterDate);
        boolean addedToJournal = false;

        //setup
        String header = String.format("JOURNAL %s\n\n", formattedDate);
        String transactionToAppend = this.formatTransaction(transactionDTO, localDateTime);
        String fileName = "src/main/resources/Transactionhistory-" + formattedDate + ".txt";

        try (FileWriter fileWriter = new FileWriter(fileName, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(transactionToAppend);
            addedToJournal = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addedToJournal;
    }

    /**
     * Creates a journal page with the date of the time when pin is opened. In case the file does not exists, it creates a txtfile with a header.
     * @param localDateTime
     * @return returns true if
     */
    @Override
    public boolean createJournalPage(LocalDateTime localDateTime) {
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = localDateTime.format(formatterDate);
        boolean created = false;

        //setup
        String header = "JOURNAL " + formattedDate + "\n\n";
        String fileName = "src/main/resources/Transactionhistory-" + formattedDate + ".txt";

        //check if file already exists (in case of opening and closing on same day), if not then add header to file
        boolean fileExists = checkFileExists(fileName);

        try (FileWriter fileWriter = new FileWriter(fileName, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            if (!fileExists) {
                printWriter.println(header);
            }
            created = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return created;
    }

    /**
     * Saves the pinmachine details in a new file or overwrites in case of an existing file.
     * @param pinMachine
     * @return true if saved, false if not.
     */
    @Override
    public boolean savePinMachineDetails(PinMachine pinMachine) {
        boolean saved = false;

        //setup
        StringBuilder sB = new StringBuilder();
        sB.append(String.format("%s,", pinMachine.getPinMachineNumber()));
        sB.append(String.format("%s,", pinMachine.getIbanBankAccountNumber()));
        sB.append(String.format("%d", pinMachine.getLastTransactionId()));

        String pinMachineDetails = sB.toString();
        String fileName = "src/main/resources/PinMachineDetails.txt";
        boolean fileExists = checkFileExists(fileName);
        //check if file already exists (in case of opening and closing on same day), if not then add header to file

        //if file already exists, replace
        try (FileWriter fileWriter = new FileWriter(fileName, false);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(pinMachineDetails);
            saved = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saved;
    }

    /**
     * Tries to load the pinMachineDetails from the txt file.
     * @Return true if succeeded, false if not.
     */
    public boolean loadPinMachineDetails() {
        boolean succeeded = false;
        PinMachine pinMachine = Application.getPinMachine();
        String fileName = "src/main/resources/PinMachineDetails.txt";
        File pinMachineFile = new File(fileName);
        try {
            Scanner invoer = new Scanner(pinMachineFile);
            while (invoer.hasNextLine()) {
                String[] lines = invoer.nextLine().split(",");
                //de volgorde van de tabel hoefr niet per se getzelfde te zijn als de input constructor, maar dit maakt het wel makkelijker. Anders moet je de juiste waarde nog
                String pinMachineNumber = lines[0];
                String iban = lines[1];
                String lastTransactionId = lines[2];
                if (pinMachineNumber.equals("null")){
                    pinMachine.setPinMachineNumber(null);
                } else {
                    pinMachine.setPinMachineNumber(lines[0]);
                }
                if (iban.equals("null")){
                    pinMachine.setIbanBankAccountNumber(null);
                } else {
                    pinMachine.setIbanBankAccountNumber(lines[1]);
                }
                pinMachine.setLastTransactionId(Integer.parseInt(lines[2]));
                succeeded = true;
            }
        } catch (FileNotFoundException nietGevonden) {
            System.out.println("Het bestand is niet gevonden.");
        }
        return succeeded;
    }

    /**
     * Checks if the file already exists.
     * @param fileName
     * @return true if exists, false if not.
     */
    private boolean checkFileExists(String fileName) {
        File existingFile = new File(fileName);
        return existingFile.exists();
    }

    /**
     * Formats the TransactionDTO info to be implemented into the journalpage
     * @param transactionDTO
     * @param localDateTime
     * @return a string with the formatted information
     */
    private String formatTransaction(TransactionDTO transactionDTO, LocalDateTime localDateTime) {
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        String date = localDateTime.format(formatterDate);
        String time = localDateTime.format(formatterTime);

        StringBuilder sB = new StringBuilder();
        sB.append(String.format("%-15s %s\n", "TransactionID:", Application.getPinMachine().getLastTransactionId()));
        sB.append(String.format("%-15s %s\n", "Date:", date));
        sB.append(String.format("%-15s %s\n", "Time:", time));
        sB.append(String.format("%-15s %s\n", "Debet number:", transactionDTO.getFromIBANBankAccountNr()));
        sB.append(String.format("%-15s €%.2f\n", "Amount:", transactionDTO.getAmount()));
        return sB.toString();
    }

}
