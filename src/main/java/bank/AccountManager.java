package bank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private List<Account> accounts;
    private String filePath;

    public AccountManager(String filePath) {
        this.accounts = new ArrayList<>();
        this.filePath = filePath;
        loadAccountsFromFile(filePath);
    }

    private void loadAccountsFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[0].trim());
                    int balance = Integer.parseInt(parts[1].trim());
                    Account account = new Account(id, balance);
                    accounts.add(account);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo");
        } catch (NumberFormatException e) {
            throw new RuntimeException("Erro de formatação nos dados da conta");
        }
    }

    public void saveAccountsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Account account : accounts) {
                writer.write(account.getId() + "," + account.getBalance());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever no arquivo");
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Account fetchAccountById(int id) {
        for (Account account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }

        return null;
    }

    public Account fetchOrCreateAccountById(int id) {
        Account account = this.fetchAccountById(id);
        if (account != null) {
            return account;
        }

        Account newAccount = new Account(id);
        accounts.add(newAccount);
        return newAccount;
    }

    public void printAccounts() {
        for (Account account : accounts) {
            System.out.println(account);
        }
    }
}
