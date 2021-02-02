package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.implDB.DBAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.implDB.DBTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema.SimpleExpenseManagerDbHelper;

public class PersistentExpenseManager extends ExpenseManager{

    public PersistentExpenseManager(Context context) {
        this.dbHelper = new SimpleExpenseManagerDbHelper(context);
        setup();
    }

    @Override
    public void setup() {
        /*** Setup the persistent storage implementation ***/

        AccountDAO dbAccountDAO = new DBAccountDAO(this.dbHelper);
        setAccountsDAO(dbAccountDAO);

        TransactionDAO dbTransactionDAO = new DBTransactionDAO(this.dbHelper);
        setTransactionsDAO(dbTransactionDAO);

        /*** End ***/
    }

}
