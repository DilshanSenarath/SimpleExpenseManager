package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.schema;

import android.provider.BaseColumns;

public final class SimpleExpenseManagerContract {

    //Stop initiate the SimpleExpenseManagerContract class
    private SimpleExpenseManagerContract() {}

    //Account table details
    public static class Account implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_NAME_ACCOUNT_NO = "accountNo";
        public static final String COLUMN_NAME_BANK_NAME = "bank_name";
        public static final String COLUMN_NAME_ACCOUNT_HOLDER_NAME = "account_holder_name";
        public static final String COLUMN_NAME_BALANCE = "balance";
    }

    //Transaction table details
    public static class Transaction implements BaseColumns {
        public static final String TABLE_NAME = "transaction";
        public static final String COLUMN_NAME_TRANSACTION_ID = "transaction_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_ACCOUNT_NO = "account_no";
        public static final String COLUMN_NAME_EXPENSE_TYPE = "expense_type";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }

    //Creating sql query
    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Account.TABLE_NAME + " (" +
                    Account.COLUMN_NAME_ACCOUNT_NO + " TEXT PRIMARY KEY," +
                    Account.COLUMN_NAME_BANK_NAME + " TEXT NOT NULL," +
                    Account.COLUMN_NAME_ACCOUNT_HOLDER_NAME + " TEXT NOT NULL," +
                    Account.COLUMN_NAME_BALANCE + " REAL NOT NULL);" +
            "CREATE TABLE " + Transaction.TABLE_NAME + " (" +
                    Transaction.COLUMN_NAME_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Transaction.COLUMN_NAME_DATE + " DATE NOT NULL," +
                    Transaction.COLUMN_NAME_ACCOUNT_NO + " TEXT NOT NULL," +
                    Transaction.COLUMN_NAME_EXPENSE_TYPE + " TEXT NOT NULL," +
                    Transaction.COLUMN_NAME_AMOUNT + " REAL NOT NULL)";

    //Deleting sql query
    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Account.TABLE_NAME + ";" +
            "DROP TABLE IF EXISTS " + Transaction.TABLE_NAME;

}
