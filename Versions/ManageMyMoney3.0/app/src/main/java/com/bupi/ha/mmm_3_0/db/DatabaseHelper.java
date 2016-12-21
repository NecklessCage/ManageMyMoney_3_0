package com.bupi.ha.mmm_3_0.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bupi.ha.mmm_3_0.db.DataContract.BudgetAllocation;
import com.bupi.ha.mmm_3_0.db.DataContract.Category;
import com.bupi.ha.mmm_3_0.db.DataContract.Division;
import com.bupi.ha.mmm_3_0.db.DataContract.Expense;
import com.bupi.ha.mmm_3_0.db.DataContract.Income;
import com.bupi.ha.mmm_3_0.db.DataContract.IncomeCategory;
import com.bupi.ha.mmm_3_0.db.DataContract.IncomeDivision;

/**
 * Created by Htet Aung on 6/25/2016.
 * Hello
 */
class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * Types *
     */
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INT = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final int DATABASE_VERSION = 5; // In 3, income-related tables are added. // 4 is for 2_1 // 5: added "note" in expense and income
    private static final String DATABASE_NAME = "manage_my_money.db";
    /**
     * Division table queries *
     */
    private static final String QUERY_CREATE_DIVISION_TABLE =
            "CREATE TABLE " + Division.TABLE_NAME + " (" +
                    Division.Columns._ID + " INTEGER PRIMARY KEY," +
                    Division.Columns.DIVISION + TYPE_TEXT +
                    ")";
    private static final String QUERY_DROP_DIVISION_TABLE =
            "DROP TABLE IF EXISTS " + Division.TABLE_NAME;
    /**
     * Category table queries *
     */
    private static final String QUERY_CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + Category.TABLE_NAME + " (" +
                    Category.Columns._ID + " INTEGER PRIMARY KEY," +
                    Category.Columns.CATEGORY + TYPE_TEXT + COMMA_SEP +
                    Category.Columns.DIVISION + TYPE_INT + COMMA_SEP +
                    "FOREIGN KEY (" + Category.Columns.DIVISION + ") REFERENCES " + Division.TABLE_NAME + " (" + Division.Columns._ID + ")" +
                    ")";
    private static final String QUERY_DROP_CATEGORY_TABLE =
            "DROP TABLE IF EXISTS " + Category.TABLE_NAME;
    /**
     * Expense table queries *
     */
    private static final String QUERY_CREATE_EXPENSE_TABLE =
            "CREATE TABLE " + Expense.TABLE_NAME + " (" +
                    Expense.Columns._ID + " INTEGER PRIMARY KEY, " +
                    Expense.Columns.DATE + TYPE_INT + COMMA_SEP +
                    Expense.Columns.DIVISION + TYPE_INT + COMMA_SEP +
                    Expense.Columns.CATEGORY + TYPE_INT + COMMA_SEP +
                    Expense.Columns.NOTE + TYPE_TEXT + COMMA_SEP +                                  // ADDED in version 5
                    Expense.Columns.AMOUNT + TYPE_INT + COMMA_SEP +
                    "FOREIGN KEY ( " + Expense.Columns.CATEGORY + " ) REFERENCES " + Category.TABLE_NAME + " ( " + Category.Columns._ID + " ) " +
                    "FOREIGN KEY ( " + Expense.Columns.DIVISION + " ) REFERENCES " + Division.TABLE_NAME + " ( " + Division.Columns._ID + " ) " +
                    ")";
    private static final String QUERY_DROP_EXPENSE_TABLE =
            "DROP TABLE IF EXISTS " + Expense.TABLE_NAME;
    /**
     * IncomeDivision table queries *
     */
    private static final String QUERY_CREATE_INCOME_DIVISION_TABLE =
            "CREATE TABLE " + IncomeDivision.TABLE_NAME + " (" +
                    IncomeDivision.Columns._ID + " INTEGER PRIMARY KEY," +
                    IncomeDivision.Columns.INCOME_DIVISION + TYPE_TEXT +
                    ")";
    private static final String QUERY_DROP_INCOME_DIVISION_TABLE =
            "DROP TABLE IF EXISTS " + IncomeDivision.TABLE_NAME;
    /**
     * IncomeCategory table queries *
     */
    private static final String QUERY_CREATE_INCOME_CATEGORY_TABLE =
            "CREATE TABLE " + IncomeCategory.TABLE_NAME + " (" +
                    IncomeCategory.Columns._ID + " INTEGER PRIMARY KEY," +
                    IncomeCategory.Columns.INCOME_CATEGORY + TYPE_TEXT + COMMA_SEP +
                    IncomeCategory.Columns.INCOME_DIVISION + TYPE_INT + COMMA_SEP +
                    "FOREIGN KEY (" + IncomeCategory.Columns.INCOME_DIVISION + ") REFERENCES " + IncomeDivision.TABLE_NAME + " (" + IncomeDivision.Columns._ID + ")" +
                    ")";
    private static final String QUERY_DROP_INCOME_CATEGORY_TABLE =
            "DROP TABLE IF EXISTS " + IncomeCategory.TABLE_NAME;
    /**
     * Income table queries *
     */
    private static final String QUERY_CREATE_INCOME_TABLE =
            "CREATE TABLE " + Income.TABLE_NAME + " (" +
                    Income.Columns._ID + " INTEGER PRIMARY KEY, " +
                    Income.Columns.INCOME_DATE + TYPE_INT + COMMA_SEP +
                    Income.Columns.INCOME_DIVISION + TYPE_INT + COMMA_SEP +
                    Income.Columns.INCOME_CATEGORY + TYPE_INT + COMMA_SEP +
                    Income.Columns.INCOME_NOTE + TYPE_TEXT + COMMA_SEP +
                    Income.Columns.INCOME_AMOUNT + TYPE_INT + COMMA_SEP +
                    "FOREIGN KEY ( " + Income.Columns.INCOME_CATEGORY + " ) REFERENCES " + IncomeCategory.TABLE_NAME + " ( " + IncomeCategory.Columns._ID + " ) " +
                    "FOREIGN KEY ( " + Income.Columns.INCOME_DIVISION + " ) REFERENCES " + IncomeDivision.TABLE_NAME + " ( " + IncomeDivision.Columns._ID + " ) " +
                    ")";
    private static final String QUERY_DROP_INCOME_TABLE =
            "DROP TABLE IF EXISTS " + Income.TABLE_NAME;

    /**
     * BudgetAllocation table queries *
     */
    private static final String QUERY_CREATE_BUDGET_ALLOCATION_TABLE =
            "CREATE TABLE " + BudgetAllocation.TABLE_NAME + " (" +
                    BudgetAllocation.Columns._ID + " INTEGER PRIMARY KEY, " +
                    BudgetAllocation.Columns.EXPENSE_CATEGORY + TYPE_INT + COMMA_SEP +
                    BudgetAllocation.Columns.ALLOCATION + TYPE_INT + COMMA_SEP +
                    "FOREIGN KEY ( " + BudgetAllocation.Columns.EXPENSE_CATEGORY + " ) REFERENCES " + Category.TABLE_NAME + " ( " + Category.Columns._ID + " ) " +
                    ")";
    private static final String QUERY_DROP_BUDGET_ALLOCATION_TABLE =
            "DROP TABLE IF EXISTS " + BudgetAllocation.TABLE_NAME;

    // Singleton instance
    private static DatabaseHelper dbHelper;

    /**
     * Constructor *
     */
    private DatabaseHelper(Context context) { // This is never used.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    } // End of constructor.

    static synchronized DatabaseHelper getInstance(Context context) {
        /* Use the application context, which will ensure that you
           don't accidentally leak an Activity's context.
           See this article for more information: http://bit.ly/6LRzfx
        */
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context.getApplicationContext());
        } // End of if.
        return dbHelper;
    } // End of getInstance.

    public void onCreate(SQLiteDatabase db) {
        // Expense-related tables.
        db.execSQL(QUERY_CREATE_DIVISION_TABLE);
        db.execSQL(QUERY_CREATE_CATEGORY_TABLE);
        db.execSQL(QUERY_CREATE_EXPENSE_TABLE);
        // Income-related tables.
        db.execSQL(QUERY_CREATE_INCOME_DIVISION_TABLE);
        db.execSQL(QUERY_CREATE_INCOME_CATEGORY_TABLE);
        db.execSQL(QUERY_CREATE_INCOME_TABLE);
        //
        db.execSQL(QUERY_CREATE_BUDGET_ALLOCATION_TABLE);
    } // End of onCreate.

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(QUERY_DROP_BUDGET_ALLOCATION_TABLE);

        db.execSQL(QUERY_DROP_EXPENSE_TABLE);
        db.execSQL(QUERY_DROP_CATEGORY_TABLE);
        db.execSQL(QUERY_DROP_DIVISION_TABLE);

        db.execSQL(QUERY_DROP_INCOME_TABLE);
        db.execSQL(QUERY_DROP_INCOME_CATEGORY_TABLE);
        db.execSQL(QUERY_DROP_INCOME_DIVISION_TABLE);
        onCreate(db);
    } // End of onUpgrade.

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    } // End of onDowngrade.
} // End of class.
