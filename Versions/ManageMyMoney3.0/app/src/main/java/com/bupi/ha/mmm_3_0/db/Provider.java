package com.bupi.ha.mmm_3_0.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bupi.ha.mmm_3_0.db.DataContract.BudgetAllocation;
import com.bupi.ha.mmm_3_0.db.DataContract.Category;
import com.bupi.ha.mmm_3_0.db.DataContract.Division;
import com.bupi.ha.mmm_3_0.db.DataContract.Expense;
import com.bupi.ha.mmm_3_0.db.DataContract.Income;
import com.bupi.ha.mmm_3_0.db.DataContract.IncomeCategory;
import com.bupi.ha.mmm_3_0.db.DataContract.IncomeDivision;

import java.util.HashMap;

/**
 * Created by Htet Aung on 6/25/2016.
 * Hello
 */
public class Provider extends ContentProvider {
    // Provider name to be used in the manifest.xml
    static final String PROVIDER_NAME = "com.bupi.ha.mmm_3_0";

    /*
    // URIs
    public static final Uri
            CONTENT_URI_DIVISION = Uri.parse("content://" + PROVIDER_NAME + "/" + Division.TABLE_NAME),
            CONTENT_URI_CATEGORY = Uri.parse("content://" + PROVIDER_NAME + "/" + Category.TABLE_NAME),
            CONTENT_URI_EXPENSE = Uri.parse("content://" + PROVIDER_NAME + "/" + Expense.TABLE_NAME),
            CONTENT_URI_EXPENSE_REPORT = Uri.parse("content://" + PROVIDER_NAME + "/" + Expense.TABLE_NAME + "_report"),
            CONTENT_URI_INCOME_DIVISION = Uri.parse("content://" + PROVIDER_NAME + "/" + IncomeDivision.TABLE_NAME),
            CONTENT_URI_INCOME_CATEGORY = Uri.parse("content://" + PROVIDER_NAME + "/" + IncomeCategory.TABLE_NAME),
            CONTENT_URI_INCOME = Uri.parse("content://" + PROVIDER_NAME + "/" + Income.TABLE_NAME);
            */

    public static abstract class ContentUri {
        public static final Uri
                DIVISION = Uri.parse("content://" + PROVIDER_NAME + "/" + Division.TABLE_NAME),
                CATEGORY = Uri.parse("content://" + PROVIDER_NAME + "/" + Category.TABLE_NAME),
                EXPENSE = Uri.parse("content://" + PROVIDER_NAME + "/" + Expense.TABLE_NAME),
                EXPENSE_REPORT = Uri.parse("content://" + PROVIDER_NAME + "/" + Expense.TABLE_NAME + "_report"),
                INCOME_DIVISION = Uri.parse("content://" + PROVIDER_NAME + "/" + IncomeDivision.TABLE_NAME),
                INCOME_CATEGORY = Uri.parse("content://" + PROVIDER_NAME + "/" + IncomeCategory.TABLE_NAME),
                INCOME = Uri.parse("content://" + PROVIDER_NAME + "/" + Income.TABLE_NAME),
                INCOME_REPORT = Uri.parse("content://" + PROVIDER_NAME + "/" + Income.TABLE_NAME + "_report"),
                BUDGET_ALLOCATION = Uri.parse("content://" + PROVIDER_NAME + "/" + BudgetAllocation.TABLE_NAME);
    } //

    static final int
            DIVISION = 0,
            CATEGORY = 1,
            EXPENSE = 2,
            EXPENSE_REPORT = 3,
            INCOME_DIVISION = 4,
            INCOME_CATEGORY = 5,
            INCOME = 6,
            INCOME_REPORT = 7,
            BUDGET_ALLOCATION = 8;
    static final UriMatcher uriMatcher;
    private static HashMap<String, String> PROJECTION_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Expense-related Uris.
        uriMatcher.addURI(PROVIDER_NAME, Division.TABLE_NAME, DIVISION);
        uriMatcher.addURI(PROVIDER_NAME, Category.TABLE_NAME, CATEGORY);
        uriMatcher.addURI(PROVIDER_NAME, Expense.TABLE_NAME, EXPENSE);
        uriMatcher.addURI(PROVIDER_NAME, Expense.TABLE_NAME + "_report", EXPENSE_REPORT);
        // Income-related Uris.
        uriMatcher.addURI(PROVIDER_NAME, IncomeDivision.TABLE_NAME, INCOME_DIVISION);
        uriMatcher.addURI(PROVIDER_NAME, IncomeCategory.TABLE_NAME, INCOME_CATEGORY);
        uriMatcher.addURI(PROVIDER_NAME, Income.TABLE_NAME, INCOME);
        uriMatcher.addURI(PROVIDER_NAME, Income.TABLE_NAME + "_report", INCOME_REPORT);
        //
        uriMatcher.addURI(PROVIDER_NAME, BudgetAllocation.TABLE_NAME, BUDGET_ALLOCATION);
    } // End of static initializer.

    // Database Instance
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        // Get database helper.
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
        // Get writable database
        db = databaseHelper.getWritableDatabase();
        return db != null;
    } // End of onCreate

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri _uri = null; // Uri of the newly added entry to be returned.
        long rowId; // Stores the Id of the newly inserted entry.

        // Match the inputs uri the appropriate table.
        switch (uriMatcher.match(uri)) {
            //-----------------------------------------------------------Expense-related insertions.
            case DIVISION:
                rowId = db.insertOrThrow(Division.TABLE_NAME, "", values);
                if (rowId > 0) { // If the insertion was successful.
                    // Get the content uri of the newly added entry.
                    _uri = ContentUris.withAppendedId(ContentUri.DIVISION, rowId);
                } // End of if
                break;
            case CATEGORY:
                rowId = db.insertOrThrow(Category.TABLE_NAME, "", values);
                if (rowId > 0) { // If the insertion was successful.
                    // Get the content uri of the newly added entry.
                    _uri = ContentUris.withAppendedId(ContentUri.CATEGORY, rowId);
                } // End of if
                break;
            case EXPENSE:
                rowId = db.insertOrThrow(Expense.TABLE_NAME, "", values);
                if (rowId > 0) { // If the insertion was successful.
                    // Get the content uri of the newly added entry.
                    _uri = ContentUris.withAppendedId(ContentUri.EXPENSE, rowId);
                } // End of if
                break;
            //------------------------------------------------------------Income-related insertions.
            case INCOME_DIVISION:
                rowId = db.insertOrThrow(IncomeDivision.TABLE_NAME, "", values);
                if (rowId > 0) { // If the insertion was successful.
                    // Get the content uri of the newly added entry.
                    _uri = ContentUris.withAppendedId(ContentUri.INCOME_DIVISION, rowId);
                } // End of if
                break;
            case INCOME_CATEGORY:
                rowId = db.insertOrThrow(IncomeCategory.TABLE_NAME, "", values);
                if (rowId > 0) { // If the insertion was successful.
                    // Get the content uri of the newly added entry.
                    _uri = ContentUris.withAppendedId(ContentUri.INCOME_CATEGORY, rowId);
                } // End of if
                break;
            case INCOME:
                rowId = db.insertOrThrow(Income.TABLE_NAME, "", values);
                if (rowId > 0) { // If the insertion was successful.
                    // Get the content uri of the newly added entry.
                    _uri = ContentUris.withAppendedId(ContentUri.INCOME, rowId);
                } // End of if
                break;
            case BUDGET_ALLOCATION:
                rowId = db.insertOrThrow(BudgetAllocation.TABLE_NAME, "", values);
                if (rowId > 0) { // If the insertion was successful.
                    // Get the content uri of the newly added entry.
                    _uri = ContentUris.withAppendedId(ContentUri.BUDGET_ALLOCATION, rowId);
                } // End of if
                break;
            default:
                throw new SQLException("Failed to insert row into " + uri);
        } // End of switch.

        // Register for notification.
        getContext().getContentResolver().notifyChange(_uri, null);
        return _uri;
    } // End of insert.

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;

        // Uri matching.
        switch (uriMatcher.match(uri)) {
            //--------------------------------------------------------------Expense-related updates.
            case DIVISION:
                count = db.update(Division.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CATEGORY:
                count = db.update(Category.TABLE_NAME, values, selection, selectionArgs);
                break;
            case EXPENSE:
                count = db.update(Expense.TABLE_NAME, values, selection, selectionArgs);
                break;
            //---------------------------------------------------------------Income-related updates.
            case INCOME_DIVISION:
                count = db.update(IncomeDivision.TABLE_NAME, values, selection, selectionArgs);
                break;
            case INCOME_CATEGORY:
                count = db.update(IncomeCategory.TABLE_NAME, values, selection, selectionArgs);
                break;
            case INCOME:
                count = db.update(Income.TABLE_NAME, values, selection, selectionArgs);
                break;
            //
            case BUDGET_ALLOCATION:
                count = db.update(BudgetAllocation.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        } // End of switch.

        // Register for notification.
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    } // End of update.

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;

        switch (uriMatcher.match(uri)) {
            //------------------------------------------------------------Expense-related deletions.
            case DIVISION:
                count = db.delete(Division.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORY:
                count = db.delete(Category.TABLE_NAME, selection, selectionArgs);
                break;
            case EXPENSE:
                count = db.delete(Expense.TABLE_NAME, selection, selectionArgs);
                break;
            //-------------------------------------------------------------Income-related deletions.
            case INCOME_DIVISION:
                count = db.delete(IncomeDivision.TABLE_NAME, selection, selectionArgs);
                break;
            case INCOME_CATEGORY:
                count = db.delete(IncomeCategory.TABLE_NAME, selection, selectionArgs);
                break;
            case INCOME:
                count = db.delete(Income.TABLE_NAME, selection, selectionArgs);
                break;
            //
            case BUDGET_ALLOCATION:
                count = db.delete(BudgetAllocation.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        } // End of switch.

        // Register for notification
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    } // End of delete.

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Query builder
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setProjectionMap(PROJECTION_MAP);

        switch (uriMatcher.match(uri)) {
            //-----------------------------------------------------------Expense-related selections.
            case DIVISION:
                qb.setTables(Division.TABLE_NAME);
                break;
            case CATEGORY:
                qb.setTables(Category.TABLE_NAME);
                break;
            case EXPENSE:
                qb.setTables(Expense.TABLE_NAME);
                break;
            case EXPENSE_REPORT:
                qb.setTables(ExpenseReportInnerJoin());
                qb.setProjectionMap(ExpenseReportInnerJoinProjection());
                break;
            //------------------------------------------------------------Income-related selections.
            case INCOME_DIVISION:
                qb.setTables(IncomeDivision.TABLE_NAME);
                break;
            case INCOME_CATEGORY:
                qb.setTables(IncomeCategory.TABLE_NAME);
                break;
            case INCOME:
                qb.setTables(Income.TABLE_NAME);
                break;
            case INCOME_REPORT:
                qb.setTables(IncomeReportInnerJoin());
                qb.setProjectionMap(IncomeReportInnerJoinProjection());
                break;
            //
            case BUDGET_ALLOCATION:
                qb.setTables(BudgetAllocation.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        } // End of switch

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    } // End of query.

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case DIVISION:
                return "vnd.android.cursor.dir/vnd.com.pikyi.bubu.mmm_2_0." + Division.TABLE_NAME;
            case CATEGORY:
                return "vnd.android.cursor.dir/vnd.com.pikyi.bubu.mmm_2_0." + Category.TABLE_NAME;
            case EXPENSE:
                return "vnd.android.cursor.dir/vnd.com.pikyi.bubu.mmm_2_0." + Expense.TABLE_NAME;
            case EXPENSE_REPORT:
                return "vnd.android.cursor.dir/vnd.com.pikyi.bubu.mmm_2_0." + Expense.TABLE_NAME + "_report";
            case INCOME_DIVISION:
                return "vnd.android.cursor.dir/vnd.com.pikyi.bubu.mmm_2_0." + IncomeDivision.TABLE_NAME;
            case INCOME_CATEGORY:
                return "vnd.android.cursor.dir/vnd.com.pikyi.bubu.mmm_2_0." + IncomeCategory.TABLE_NAME;
            case INCOME:
                return "vnd.android.cursor.dir/vnd.com.pikyi.bubu.mmm_2_0." + Income.TABLE_NAME;
            case INCOME_REPORT:
                return "vnd.android.cursor.dir/vnd.com.pikyi.bubu.mmm_2_0." + Income.TABLE_NAME + "_report";
            case BUDGET_ALLOCATION:
                return "vnd.android.cursor.dir/vnd.com.pikyi.bubu.mmm_2_0." + BudgetAllocation.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        } // End of switch.
    } // End of getType.

    //---------------------------------------------------------------------------------------Methods
    private String ExpenseReportInnerJoin() {
        String table = "";
        // tbl_expense and tbl_category inner join.
        table += Expense.TABLE_NAME + " INNER JOIN " + Category.TABLE_NAME;
        // tbl_expense and tbl_category inner join condition.
        table += " ON (" + Expense.TABLE_NAME + "." + Expense.Columns.CATEGORY + "="
                + Category.TABLE_NAME + "." + Category.Columns._ID + ")";

        // In turn, inner join the resulting table to tbl_division.
        table += " INNER JOIN " + Division.TABLE_NAME;
        // tbl_expense and tbl_division inner join condition.
        table += " ON (" + Expense.TABLE_NAME + "." + Expense.Columns.DIVISION + "="
                + Division.TABLE_NAME + "." + Division.Columns._ID + ")";
        // Return the result.
        return table;
    } // End of ExpenseReportInnerJoin.

    private HashMap<String, String> ExpenseReportInnerJoinProjection() {
        HashMap<String, String> projection = new HashMap<>();
        projection.put(Expense.TABLE_NAME + "." + Expense.Columns._ID, Expense.TABLE_NAME + "." + Expense.Columns._ID + " AS _id");
        projection.put(Expense.TABLE_NAME + "." + Expense.Columns.DATE, Expense.TABLE_NAME + "." + Expense.Columns.DATE + " AS ExpenseDate");
        projection.put(Expense.TABLE_NAME + "." + Expense.Columns.DIVISION, Expense.TABLE_NAME + "." + Expense.Columns.DIVISION + " AS ExpenseDivisionID");
        projection.put(Expense.TABLE_NAME + "." + Expense.Columns.CATEGORY, Expense.TABLE_NAME + "." + Expense.Columns.CATEGORY + " AS ExpenseCategoryID");
        projection.put(Expense.TABLE_NAME + "." + Expense.Columns.AMOUNT, Expense.TABLE_NAME + "." + Expense.Columns.AMOUNT + " AS ExpenseAmount");
        projection.put(Category.TABLE_NAME + "." + Category.Columns._ID, Category.TABLE_NAME + "." + Category.Columns._ID + " AS CategoryID");
        projection.put(Category.TABLE_NAME + "." + Category.Columns.CATEGORY, Category.TABLE_NAME + "." + Category.Columns.CATEGORY + " AS CategoryName");
        projection.put(Category.TABLE_NAME + "." + Category.Columns.DIVISION, Category.TABLE_NAME + "." + Category.Columns.DIVISION + " AS CategoryDivisionID");
        projection.put(Division.TABLE_NAME + "." + Division.Columns._ID, Division.TABLE_NAME + "." + Division.Columns._ID + " AS DivisionID");
        projection.put(Division.TABLE_NAME + "." + Division.Columns.DIVISION, Division.TABLE_NAME + "." + Division.Columns.DIVISION + " AS DivisionName");
        return projection;
    } // End of ExpenseReportInnerJoinProjection.

    private String IncomeReportInnerJoin() {
        String table = "";
        // tbl_income and tbl_income_category inner join.
        table += Income.TABLE_NAME + " INNER JOIN " + IncomeCategory.TABLE_NAME;
        // tbl_income and tbl_income_category inner join condition.
        table += " ON (" + Income.TABLE_NAME + "." + Income.Columns.INCOME_CATEGORY + "="
                + IncomeCategory.TABLE_NAME + "." + IncomeCategory.Columns._ID + ")";

        // In turn, inner join the resulting table to tbl_income_division.
        table += " INNER JOIN " + IncomeDivision.TABLE_NAME;
        // tbl_income and tbl_income_division inner join condition.
        table += " ON (" + Income.TABLE_NAME + "." + Income.Columns.INCOME_DIVISION + "="
                + IncomeDivision.TABLE_NAME + "." + IncomeDivision.Columns._ID + ")";
        // Return the result.
        return table;
    } // End of IncomeReportInnerJoin.

    /**
     * The following creates alias for the columns.
     *
     * @return
     */
    private HashMap<String, String> IncomeReportInnerJoinProjection() {
        HashMap<String, String> projection = new HashMap<>();
        projection.put(Income.TABLE_NAME + "." + Income.Columns._ID, Income.TABLE_NAME + "." + Income.Columns._ID + " AS _id");
        projection.put(Income.TABLE_NAME + "." + Income.Columns.INCOME_DATE, Income.TABLE_NAME + "." + Income.Columns.INCOME_DATE + " AS IncomeDate");
        projection.put(Income.TABLE_NAME + "." + Income.Columns.INCOME_DIVISION, Income.TABLE_NAME + "." + Income.Columns.INCOME_DIVISION + " AS IncomeDivisionID");
        projection.put(Income.TABLE_NAME + "." + Income.Columns.INCOME_CATEGORY, Income.TABLE_NAME + "." + Income.Columns.INCOME_CATEGORY + " AS IncomeCategoryID");
        projection.put(Income.TABLE_NAME + "." + Income.Columns.INCOME_AMOUNT, Income.TABLE_NAME + "." + Income.Columns.INCOME_AMOUNT + " AS IncomeAmount");
        projection.put(IncomeCategory.TABLE_NAME + "." + IncomeCategory.Columns._ID, IncomeCategory.TABLE_NAME + "." + IncomeCategory.Columns._ID + " AS IncomeCategoryID");
        projection.put(IncomeCategory.TABLE_NAME + "." + IncomeCategory.Columns.INCOME_CATEGORY, IncomeCategory.TABLE_NAME + "." + IncomeCategory.Columns.INCOME_CATEGORY + " AS IncomeCategoryName");
        projection.put(IncomeCategory.TABLE_NAME + "." + IncomeCategory.Columns.INCOME_DIVISION, IncomeCategory.TABLE_NAME + "." + IncomeCategory.Columns.INCOME_DIVISION + " AS IncomeCategoryDivisionID");
        projection.put(IncomeDivision.TABLE_NAME + "." + IncomeDivision.Columns._ID, IncomeDivision.TABLE_NAME + "." + IncomeDivision.Columns._ID + " AS IncomeDivisionID");
        projection.put(IncomeDivision.TABLE_NAME + "." + IncomeDivision.Columns.INCOME_DIVISION, IncomeDivision.TABLE_NAME + "." + IncomeDivision.Columns.INCOME_DIVISION + " AS IncomeDivisionName");
        return projection;
    } // End of IncomeReportInnerJoinProjection.
} // End of Provider class.
