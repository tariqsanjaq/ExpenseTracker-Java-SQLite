package first.tutorial.expense_tracker.database;

public class Expense {
    private int id;
    private int userId; // 🌟 NEW
    private String title;
    private double amount;
    private String category;
    private String date;
    private int isRecurring;

    public Expense(String title, double amount, String category, String date, int isRecurring) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isRecurring = isRecurring;
    }

    public Expense(int id, String title, double amount, String category, String date, int isRecurring) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isRecurring = isRecurring;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getIsRecurring() { return isRecurring; }
    public void setIsRecurring(int isRecurring) { this.isRecurring = isRecurring; }
}