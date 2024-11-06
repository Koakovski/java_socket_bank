package bank.tcp.server;

import java.io.*;
import java.net.*;

import bank.Account.TransactionType;
import bank.AccountService;

public class TCPServer {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escutando na porta " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String messageFromClient = in.readLine();
                String response = TCPServer.handleRequest(messageFromClient);

                out.println(response);

                clientSocket.close();
                System.out.println("Conexão com o cliente fechada.");
            }
        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static enum RequestType {
        TRANSACTION,
        FETCH
    }

    private static String handleRequest(String message) {
        if (message == null || message.isEmpty()) {
            return "Mensagem inválida!";
        }

        String[] parts = message.split(",");
        String typePart = parts[0].trim();

        try {
            RequestType type = parseRequestType(typePart);

            switch (type) {
                case TRANSACTION:
                    if (parts.length != 4) {
                        return "Formato de dados de transação inválido!";
                    }

                    TransactionParams params = parseTransactionParams(parts);

                    return AccountService.doTransaction(params.accountId, params.transactionType, params.valueInCents);
                case FETCH:
                    if (parts.length != 2) {
                        return "Formato de dados de busca inválido!";
                    }

                    int accountId;
                    try {
                        accountId = Integer.parseInt(parts[1].trim());

                    } catch (NumberFormatException e) {
                        throw new RuntimeException("ID de conta inválido.");
                    }

                    return AccountService.fetchAccountById(accountId);
                default:
                    return "Tipo de requisição inválido!";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    };

    private static RequestType parseRequestType(String typePart) {
        RequestType type;
        try {
            type = RequestType.valueOf(typePart.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de requisição inválido!");
        }

        return type;
    }

    private static TransactionParams parseTransactionParams(String[] parts) {
        TransactionParams params = new TransactionParams();

        try {
            params.accountId = Integer.parseInt(parts[1].trim());
            params.transactionType = TransactionType.valueOf(parts[2].trim().toUpperCase());
            params.valueInCents = Integer.parseInt(parts[3].trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException("ID de conta ou valor em centavos inválido.");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de transação inválido.");
        }

        return params;
    }

    private static class TransactionParams {
        int accountId;
        TransactionType transactionType;
        int valueInCents;
    }
}
