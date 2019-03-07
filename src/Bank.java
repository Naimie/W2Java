// Peter Idestam-Almquist, 2019-02-04.

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

// TODO: Make this class thread-safe and as performant as possible.
class Bank {
	// Instance variables.
	private int accountCounter = 0;
	private Map<Integer, Account> accounts = new HashMap<Integer, Account>();
	
	// Instance methods.

	int newAccount(int balance) {
		int accountId = accountCounter++;
		Account account = new Account(accountId, balance);
		accounts.put(accountId, account);
		return accountId;
	}
	
	int getAccountBalance(int accountId) {
		return accounts.get(accountId).getBalance();
	}
	
	void runOperation(Operation operation) {
		Account account = accounts.get(operation.getAccountId());
		account.setBalance(account.getBalance() + operation.getAmount());
	}
		
	// TODO: If you are not solving the advanced task you should remove this method runTransaction.
	void runTransaction(Transaction transaction) {
		List<Integer> accountIds = transaction.getAccountIds();
		List<Operation> operations = transaction.getOperations();

		Account account = null;
		for (int i = 0; i < operations.size(); i++) {
			account = accounts.get(operations.get(i).getAccountId());
			account.setBalance(account.getBalance() + operations.get(i).getAmount());
		}
	}
}
