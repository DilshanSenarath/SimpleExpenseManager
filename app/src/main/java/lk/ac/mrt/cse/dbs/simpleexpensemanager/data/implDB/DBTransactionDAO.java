package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.implDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema.SimpleExpenseManagerContract;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema.SimpleExpenseManagerDbHelper;

public class DBTransactionDAO implements TransactionDAO {
    private final List<Transaction> transactions;
    private final SimpleExpenseManagerDbHelper dbHelper;

    public DBTransactionDAO(SimpleExpenseManagerDbHelper dbHelper) {
        transactions = new LinkedList<>();

        //Get transaction data from database and save it in linked list
        this.dbHelper = dbHelper;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] selectionArg = {};
        String t = "";

        Cursor cursor = db.query(
                SimpleExpenseManagerContract.Transaction.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String strDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(SimpleExpenseManagerContract.Transaction.COLUMN_NAME_DATE));
            String[] temp = strDate.split("-");
            String newStrDate = temp[2]+"-"+temp[1]+"-"+temp[0];
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date;
            try{
                date = dateFormat.parse(newStrDate);
            }catch (ParseException e){
                return;
            }
            String accountNo = cursor.getString(
                    cursor.getColumnIndexOrThrow(SimpleExpenseManagerContract.Transaction.COLUMN_NAME_ACCOUNT_NO));
            String strExpenseType = cursor.getString(
                    cursor.getColumnIndexOrThrow(SimpleExpenseManagerContract.Transaction.COLUMN_NAME_EXPENSE_TYPE));
            ExpenseType expenseType;
            if (strExpenseType.equals("expense")){
                expenseType = ExpenseType.EXPENSE;
            }else{
                expenseType = ExpenseType.INCOME;
            }
            Double amount = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(SimpleExpenseManagerContract.Transaction.COLUMN_NAME_AMOUNT));
            Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
            transactions.add(transaction);
        }
        cursor.close();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        transactions.add(transaction);

        //Insert to the database

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);

        String strExpenseType;
        if (expenseType == ExpenseType.EXPENSE){
            strExpenseType = "expense";
        }else{
            strExpenseType = "income";
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //create values for inserting
        ContentValues values = new ContentValues();
        values.put(SimpleExpenseManagerContract.Transaction.COLUMN_NAME_DATE, strDate);
        values.put(SimpleExpenseManagerContract.Transaction.COLUMN_NAME_ACCOUNT_NO, accountNo);
        values.put(SimpleExpenseManagerContract.Transaction.COLUMN_NAME_EXPENSE_TYPE, strExpenseType);
        values.put(SimpleExpenseManagerContract.Transaction.COLUMN_NAME_AMOUNT, amount);

        db.insert(SimpleExpenseManagerContract.Transaction.TABLE_NAME, null, values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}
