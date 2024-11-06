package bank;

public class AccountService {

    public static String doTransaction(int accountId, Account.TransactionType type, int valueInCents) {
        AccountManager accountManager = AccountService.recoverAccountManager();

        Account account = accountManager.fetchOrCreateAccountById(accountId);
        account.applyTransaction(type, valueInCents);

        accountManager.saveAccountsToFile();

        return account.toString();
    }

    public static String fetchAccountById(int accountId) {
        AccountManager accountManager = AccountService.recoverAccountManager();

        Account account = accountManager.fetchAccountById(accountId);

        if (account != null) {
            return account.toString();
        }

        throw new RuntimeException("Conta com id '" + accountId + "' não encontrada");
    }

    private static AccountManager recoverAccountManager() {
        return new AccountManager("accounts.txt");
    }
}
