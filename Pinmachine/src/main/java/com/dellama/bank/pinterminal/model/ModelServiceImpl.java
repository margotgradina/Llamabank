package com.dellama.bank.pinterminal.model;

import com.dellama.bank.pinterminal.dto.ConnectionDTO;
import com.dellama.bank.pinterminal.dto.ConnectionResponseDTO;
import com.dellama.bank.pinterminal.dto.TransactionDTO;
import com.dellama.bank.pinterminal.launcher.Application;
import com.dellama.bank.pinterminal.model.service.ModelService;
import com.dellama.bank.pinterminal.model.service.PersistencyService;
import com.dellama.bank.pinterminal.persistency.TextPersistencyImpl;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class ModelServiceImpl implements ModelService {
    PersistencyService persistencyService = new TextPersistencyImpl();


    @Override
    public ConnectionResponseDTO checkConnection(ConnectionDTO connectionDTO) throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();
        String json = gson.toJson(connectionDTO);

        RequestBody body = RequestBody.create(mediaType, json);
        System.out.println(connectionDTO);
        System.out.println(body.toString());
        Request request = new Request.Builder().url("http://localhost:8080/api/pinmachine/checkConnection").method("POST", body).addHeader("Content-Type", "application/json")

                .build();
        Response response = client.newCall(request).execute();
        String jsonResponse = response.body().string();
        System.out.println(jsonResponse);

        ConnectionResponseDTO connectionResponseDTO = new Gson().fromJson(jsonResponse, ConnectionResponseDTO.class);
        System.out.println(connectionResponseDTO);

        return connectionResponseDTO;
    }

    /**
     * Tries to make a connection via OKHTTP to webapi.
     * @param transactionDTO
     * @return the response from the webapi on the status of the transaction.
     * @throws IOException
     */
    @Override
    public String makeTransaction(TransactionDTO transactionDTO) throws IOException {
        Response response = null;
        String result = null;

        System.out.println(transactionDTO.toString());

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, new Gson().toJson(transactionDTO));

        Request request = new Request.Builder().url("http://localhost:8080/api/pinmachine/maketransaction").method("POST", body).addHeader("Content-Type", "application/json").build();

        try {
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException ioException) {
            ioException.getMessage();
        }

        if ( response == null ) {
            return "no connection found";
        } else if ( response.isSuccessful() ) {
            LocalDateTime localDateTime = LocalDateTime.now();
            Application.getPinMachine().newLastTransactionId();
            addTransactionToTransActionHistory(transactionDTO, localDateTime);
            return "Transaction successful";
        } else {
            return result;
        }
    }

    @Override
    public boolean createJournalPage(LocalDateTime localDateTime) {
        return persistencyService.createJournalPage(localDateTime);
    }


    private boolean addTransactionToTransActionHistory(TransactionDTO transactionDTO, LocalDateTime localDateTime) {
        return persistencyService.addTransactionToJournal(transactionDTO, localDateTime);
    }

    @Override
    public boolean savePinMachineDetails(PinMachine pinMachine) {
        return persistencyService.savePinMachineDetails(pinMachine);
    }

    public boolean loadPinMachineDetails(){
        return persistencyService.loadPinMachineDetails();
    }

}


