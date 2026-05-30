package first.tutorial.expense_tracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ExpenseDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() { database = dbHelper.getWritableDatabase(); }
    public void close() { if (dbHelper != null) dbHelper.close(); }

    // 🌟 SAVE EXPENSE WITH USER ID
    public long insertExpense(Expense expense, int currentUserId) {
        ContentValues values = new ContentValues();
        values.put("user_id", currentUserId);
        values.put("title", expense.getTitle());
        values.put("amount", expense.getAmount());
        values.put("category", expense.getCategory());
        values.put("date", expense.getDate());
        values.put("is_recurring", expense.getIsRecurring());
        return database.insert("expenses", null, values);
    }

    // 🌟 ONLY FETCH THIS USER'S EXPENSES
    public List<Expense> getUserExpenses(int currentUserId) {
        List<Expense> expensesList = new ArrayList<>();
        Cursor cursor = database.query("expenses", null, "user_id = ?", new String[]{String.valueOf(currentUserId)}, null, null, "date DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                int isRecurring = cursor.getInt(cursor.getColumnIndexOrThrow("is_recurring"));

                Expense expense = new Expense(id, title, amount, category, date, isRecurring);
                expensesList.add(expense);
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return expensesList;
    }

    public void deleteExpense(int id) {
        database.delete("expenses", "id = ?", new String[]{String.valueOf(id)});
    }

    public void updateExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put("title", expense.getTitle());
        values.put("amount", expense.getAmount());
        values.put("category", expense.getCategory());
        values.put("date", expense.getDate());
        values.put("is_recurring", expense.getIsRecurring());
        database.update("expenses", values, "id = ?", new String[]{String.valueOf(expense.getId())});
    }
}