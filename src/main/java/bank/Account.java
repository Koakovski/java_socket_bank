package bank;

public class Account {
    private int _id;
    private int _balanceInCents;

    Account(int id) {
        this._id = id;
        this._balanceInCents = 0;
    }

    Account(int id, int balanceInCents) {
        this._id = id;
        this._balanceInCents = balanceInCents;
    }

    public int getId() {
        return this._id;
    }

    public int getBalance() {
        return this._balanceInCents;
    }

    public void applyTransaction(TransactionType type, int valueInCents) {
        switch (type) {
            case DEPOSIT:
                this._balanceInCents += valueInCents;
                break;

            case WITHDRAW:
                if (valueInCents <= this._balanceInCents) {
                    this._balanceInCents -= valueInCents;
                } else {
                    throw new InsufficientBalanceException("Saldo insuficiente para realizar a retirada.");
                }
                break;

            default:
                throw new IllegalArgumentException("Tipo de transação desconhecido: " + type);
        }
    }

    public static enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }
    

    public static class InsufficientBalanceException extends RuntimeException {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }

    @Override
    public String toString() {
        return "Account{id=" + _id + ", balanceInCents=" + _balanceInCents + " cents}";
    }
}
