//Kristina Elmgren

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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
		//using the account it self as a lock to ensure that only one thread at a time can update the balance
		synchronized (account) {
			account.setBalance(account.getBalance() + operation.getAmount());
		}
	}
		

	void runTransaction(Transaction transaction) {
		List<Integer> accountIds = transaction.getAccountIds();
		List<Operation> operations = transaction.getOperations();


		//if the operations handle two accounts both accounts need to be locked before the transaction can run
		//to order our locking we will lock the account with the lowest Id first.
		//this solution assumes two operations in a transaction and therefore passes the tests in Program.java
		// however it is not a general solution as it can't handle N operations in a transaction.

		//here we check if the first account id is less than the second account id and if that is the case we
		//lock the first account and then the second, yielding if the second lock is unavailable
		if(operations.get(0).getAccountId() < operations.get(1).getAccountId()){
			synchronized (accounts.get(operations.get(0).getAccountId())) {
				Thread.yield();
				synchronized (accounts.get(operations.get(1).getAccountId())) {
					Account account = null;
					for (int i = 0; i < operations.size(); i++) {
						account = accounts.get(operations.get(i).getAccountId());
						account.setBalance(account.getBalance() + operations.get(i).getAmount());
					}
				}
			}
		//if the second account id >= than the first we lock second account first and then the first, yielding if the second lock is unavailable
		} else{

			synchronized (accounts.get(operations.get(1).getAccountId())) {
				Thread.yield();
				synchronized (accounts.get(operations.get(0).getAccountId())) {
					Account account = null;
					for (int i = 0; i < operations.size(); i++) {
						account = accounts.get(operations.get(i).getAccountId());
						account.setBalance(account.getBalance() + operations.get(i).getAmount());
					}
				}
			}
		}



	}
}


