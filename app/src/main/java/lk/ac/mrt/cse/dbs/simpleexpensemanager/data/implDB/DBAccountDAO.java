package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.implDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema.SimpleExpenseManagerContract;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema.SimpleExpenseManagerDbHelper;

public class DBAccountDAO implements AccountDAO {
    private final Map<String, Account> accounts;
    SimpleExpenseManagerDbHelper dbHelper;

    public DBAccountDAO(SimpleExpenseManagerDbHelper dbHelper) {
        //get all accounts frm database and create a hash map
        this.accounts = new HashMap<>();

        this.dbHelper = dbHelper;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SimpleExpenseManagerContract.Account.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String accountNo = cursor.getString(
                    cursor.getColumnIndexOrThrow(SimpleExpenseManagerContract.Account.COLUMN_NAME_ACCOUNT_NO));
            String bankName = cursor.getString(
                    cursor.getColumnIndexOrThrow(SimpleExpenseManagerContract.Account.COLUMN_NAME_BANK_NAME));
            String accountHolderName = cursor.getString(
                    cursor.getColumnIndexOrThrow(SimpleExpenseManagerContract.Account.COLUMN_NAME_ACCOUNT_HOLDER_NAME));
            Double accountBalance = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(SimpleExpenseManagerContract.Account.COLUMN_NAME_BALANCE));
            Account account = new Account(accountNo,bankName,accountHolderName,accountBalance);
            accounts.put(account.getAccountNo(), account);
        }
        cursor.close();
    }

    @Override
    public List<String> getAccountNumbersList() {
        return new ArrayList<>(accounts.keySet());
    }

    @Override
    public List<Account> getAccountsList() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        if (accounts.containsKey(accountNo)) {
            return accounts.get(accountNo);
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account){
        //Put new account to the hash map
        if (!accounts.containsKey(account.getAccountNo())){
            accounts.put(account.getAccountNo(), account);
        }else{
            return;
        }

        //Save account to the database

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //create values for inserting
        ContentValues values = new ContentValues();
        values.put(SimpleExpenseManagerContract.Account.COLUMN_NAME_ACCOUNT_NO, account.getAccountNo());
        values.put(SimpleExpenseManagerContract.Account.COLUMN_NAME_BANK_NAME, account.getBankName());
        values.put(SimpleExpenseManagerContract.Account.COLUMN_NAME_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        values.put(SimpleExpenseManagerContract.Account.COLUMN_NAME_BALANCE, account.getBalance());

        db.insert(SimpleExpenseManagerContract.Account.TABLE_NAME, null, values);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        accounts.remove(accountNo);

        //remove from database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = SimpleExpenseManagerContract.Account.COLUMN_NAME_ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };

        db.delete(SimpleExpenseManagerContract.Account.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Account account = accounts.get(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        accounts.put(accountNo, account);

        //Update database account table
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        double balance = account.getBalance();
        ContentValues values = new ContentValues();
        values.put(SimpleExpenseManagerContract.Account.COLUMN_NAME_BALANCE, balance);

        String selection = SimpleExpenseManagerContract.Account.COLUMN_NAME_BALANCE + " = ?";
        String[] selectionArgs = { accountNo };

        db.update(
                SimpleExpenseManagerContract.Account.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

}
