package first.tutorial.expense_tracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private SQLiteDatabase database;
    private final DatabaseHelper dbHelper;

    public ExpenseDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, expense.getTitle());
        values.put(DatabaseHelper.COLUMN_AMOUNT, expense.getAmount());
        values.put(DatabaseHelper.COLUMN_CATEGORY, expense.getCategory());
        values.put(DatabaseHelper.COLUMN_DATE, expense.getDate());
        values.put(DatabaseHelper.COLUMN_IS_RECURRING, expense.getIsRecurring());

        return database.insert(DatabaseHelper.TABLE_EXPENSES, null, values);
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expensesList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_EXPENSES,
                null, null, null, null, null, DatabaseHelper.COLUMN_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                expense.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)));
                expense.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT)));
                expense.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY)));
                expense.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)));
                expense.setIsRecurring(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_RECURRING)));

                expensesList.add(expense);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return expensesList;
    }
}