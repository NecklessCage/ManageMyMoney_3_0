package com.bupi.ha.mmm_3_0.helpers;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.SimpleCursorAdapter;

import com.bupi.ha.mmm_3_0.ActivityMain;
import com.bupi.ha.mmm_3_0.R;
import com.bupi.ha.mmm_3_0.db.DataContract;
import com.bupi.ha.mmm_3_0.db.Provider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Htet Aung on 6/19/2016.
 */
public class Helpers {
    public static DateFormat dateFormat = new SimpleDateFormat("MMM/dd", Locale.getDefault());

    /**
     * Only add the back stack if going from lower to higher level
     *
     * @param fragment - New fragment to be made visible.
     * @param tag      - Fragment's tag used to determining which fragment class it is.
     */
    public void replaceFragment(BackStackLevel currentLevel, FragmentManager manager,
                                BackStackFragment fragment, String tag) {
        switch (currentLevel) {
            case GROUND:
                switch (fragment.BACK_STACK_LEVEL) {
                    case GROUND: // Ground -> Ground.
                        // Do nothing because there's only 1 ground level stack.
                        break;
                    case FIRST: // Ground -> First.
                        rightInAnimation(manager, fragment, tag, true);
                        break;
                    // Levels higher than FIRST shouldn't happen here.
                    default:
                        break;
                } // End of switch.
                break;
            case FIRST:
                switch (fragment.BACK_STACK_LEVEL) {
                    case GROUND: // First -> Ground.
                        leftInAnimation(manager, fragment, tag, false);
                        break;
                    case FIRST: // First -> First.
                        rightInAnimation(manager, fragment, tag, true);
                        break;
                    case SECOND: // First -> Second.
                        rightInAnimation(manager, fragment, tag, true);
                        break;
                    // Levels
                    default:
                        break;
                } // End of switch.
                break;
            case SECOND:
                switch (fragment.BACK_STACK_LEVEL) {
                    case GROUND: // Second -> Ground.
                        leftInAnimation(manager, fragment, tag, false);
                        break;
                    case FIRST: // Second -> First.
                        leftInAnimation(manager, fragment, tag, false);
                        break;
                    case SECOND: // Second -> Second.
                        // This shouldn't happen.
                        break;
                    default:
                        break;
                } // End of switch.
                break;
            default:
                break;
        } // End of switch.
    } // End of replaceFragment.

    private void rightInAnimation(FragmentManager manager, BackStackFragment fragment,
                                  String tag, boolean stack) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        transaction.replace(R.id.fragment_container, fragment, tag);
        if (stack) { // If needed to be added to back stack
            transaction.addToBackStack(fragment.getTag());
        } // End of if.
        transaction.commit();
    } // End of rightInAnimation.

    private void leftInAnimation(FragmentManager manager, BackStackFragment fragment,
                                 String tag, boolean stack) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit,
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit);
        transaction.replace(R.id.fragment_container, fragment, tag);
        if (stack) { // If needed to be added to back stack.
            transaction.addToBackStack(fragment.getTag());
        } // End of if.
        transaction.commit();
    } // End of leftInAnimation.

    //-----------------------------------------------------------------------------------DataLoading
    public static SimpleCursorAdapter LoadExpenseDivisions(ActivityMain activity, boolean forceLoad) {
        // Use a single common cursor for all divisions.
        // Don't re-query if the cursor is already loaded before.
        if (forceLoad || activity.cursorExpenseDivisions == null) {
            activity.cursorExpenseDivisions = activity.getContentResolver().query(
                    Provider.ContentUri.DIVISION, null, null, null,
                    DataContract.Division.Columns.DIVISION);
        } // End of if.

        // Desired columns to be bound.
        String[] cols = {
                DataContract.Division.Columns._ID,
                DataContract.Division.Columns.DIVISION};
        // XML defined views.
        int[] to = new int[]{
                R.id.text_view_list_item_id,
                R.id.text_view_list_item_name
        };
        // Create the data adapter.
        return new SimpleCursorAdapter(activity.getApplicationContext(),
                R.layout.l_list_item, activity.cursorExpenseDivisions, cols, to, 0);
    } // End of LoadExpenseDivisions.

    public static SimpleCursorAdapter LoadExpenseCategoriesByExpenseDivisionId
            (Activity activity, long selectedDivisionId) {
        // Set up 'where' for query.
        String selection = DataContract.Category.Columns.DIVISION + " = ?";
        String[] selectionArgs = {String.valueOf(selectedDivisionId)};

        // Desired columns to be bound.
        String[] cols = {
                DataContract.Category.Columns._ID,
                DataContract.Category.Columns.CATEGORY,
        };
        // XML defined views.
        int[] to = new int[]{
                R.id.text_view_list_item_id,
                R.id.text_view_list_item_name,
        };
        return new SimpleCursorAdapter(
                activity.getApplicationContext(),
                R.layout.l_list_item,
                activity.getContentResolver().query(Provider.ContentUri.CATEGORY,
                        null, selection, selectionArgs, DataContract.Category.Columns.CATEGORY),
                cols, to, 0);
    } // End of LoadExpenseCategoriesByExpenseDivisionId.

    public static SimpleCursorAdapter LoadExpenseReport(
            Activity activity, String selection, String sortOrder) {
        String[] cols = {
                "_id",
                "ExpenseDate",
                "DivisionName",
                "CategoryName",
                "ExpenseAmount",
        };
        // XML defined views.
        int[] to = new int[]{
                R.id.text_view_list_item_id,
                R.id.text_view_list_item_date,
                R.id.text_view_list_item_division,
                R.id.text_view_list_item_category,
                R.id.text_view_list_item_amount,
        };

        return new DateCursorAdapter(
                activity.getApplicationContext(),
                R.layout.l_detailed_list_item,
                activity.getContentResolver().query(
                        Provider.ContentUri.EXPENSE_REPORT, null, selection, null, sortOrder),
                cols,
                to);
    } // End of LoadExpenseReport.

    public static SimpleCursorAdapter LoadExpenseReportCheckable(
            Activity activity, String selection, String sortOrder) {
        String[] cols = {
                "_id",
                "ExpenseDate",
                "DivisionName",
                "CategoryName",
                "ExpenseAmount",
        };
        // XML defined views.
        int[] to = new int[]{
                R.id.checked_text_view_list_item_id,
                R.id.checked_text_view_list_item_date,
                R.id.checked_text_view_list_item_division,
                R.id.checked_text_view_list_item_category,
                R.id.checked_text_view_list_item_amount,
        };

        return new DateCursorAdapter(
                activity.getApplicationContext(),
                R.layout.l_detailed_check_box_list_item,
                activity.getContentResolver().query(
                        Provider.ContentUri.EXPENSE_REPORT, null, selection, null, sortOrder),
                cols,
                to);
    } // End of LoadExpenseReportCheckable.

    //-----------------------------------------------------------------------------------Income Data
    public static SimpleCursorAdapter LoadIncomeDivisions(ActivityMain activity, boolean forceLoad) {
        // Use a single common cursor for all divisions.
        // Don't re-query if the cursor is already loaded before.
        if (forceLoad || activity.cursorIncomeDivisions == null) {
            activity.cursorIncomeDivisions = activity.getContentResolver().query(
                    Provider.ContentUri.INCOME_DIVISION, null, null, null,
                    DataContract.IncomeDivision.Columns.INCOME_DIVISION);
        } // End of if.

        // Desired columns to be bound.
        String[] cols = {
                DataContract.IncomeDivision.Columns._ID,
                DataContract.IncomeDivision.Columns.INCOME_DIVISION};
        // XML defined views.
        int[] to = new int[]{
                R.id.text_view_list_item_id,
                R.id.text_view_list_item_name};
        // Create the data adapter.
        return new SimpleCursorAdapter(activity.getApplicationContext(),
                R.layout.l_list_item, activity.cursorIncomeDivisions, cols, to, 0);
    } // End of LoadIncomeDivisions.

    public static SimpleCursorAdapter LoadIncomeCategoriesByIncomeDivisionId(
            Activity activity, long divisionId) {
        String selection = DataContract.IncomeCategory.Columns.INCOME_DIVISION + "=?";
        String[] selectionArgs = {String.valueOf(divisionId)};

        // Desired columns to be bound.
        String[] cols = {
                DataContract.IncomeCategory.Columns._ID,
                DataContract.IncomeCategory.Columns.INCOME_CATEGORY};
        // XML defined views.
        int[] to = new int[]{
                R.id.text_view_list_item_id,
                R.id.text_view_list_item_name};
        return new SimpleCursorAdapter(
                activity.getApplicationContext(),
                R.layout.l_list_item,
                activity.getContentResolver().query(Provider.ContentUri.INCOME_CATEGORY, null,
                        selection, selectionArgs,
                        DataContract.IncomeCategory.Columns.INCOME_CATEGORY),
                cols,
                to,
                0);
    } // End of LoadIncomeCategoriesByIncomeDivisionId.

    public static SimpleCursorAdapter LoadIncomeReport(
            Activity activity, String selection, String sortOrder) {
        String[] cols = {
                "_id",
                "IncomeDate",
                "IncomeDivisionName",
                "IncomeCategoryName",
                "IncomeAmount",
        };
        // XML defined views.
        int[] to = new int[]{
                R.id.text_view_list_item_id,
                R.id.text_view_list_item_date,
                R.id.text_view_list_item_division,
                R.id.text_view_list_item_category,
                R.id.text_view_list_item_amount,
        };

        return new DateCursorAdapter(
                activity.getApplicationContext(),
                R.layout.l_detailed_list_item,
                activity.getContentResolver().query(
                        Provider.ContentUri.INCOME_REPORT, null, selection, null, sortOrder),
                cols,
                to);
    } // End of LoadIncomeReport.

    public static SimpleCursorAdapter LoadIncomeReportCheckable(
            Activity activity, String selection, String sortOrder) {
        String[] cols = {
                "_id",
                "IncomeDate",
                "IncomeDivisionName",
                "IncomeCategoryName",
                "IncomeAmount",
        };
        // XML defined views.
        int[] to = new int[]{
                R.id.checked_text_view_list_item_id,
                R.id.checked_text_view_list_item_date,
                R.id.checked_text_view_list_item_division,
                R.id.checked_text_view_list_item_category,
                R.id.checked_text_view_list_item_amount,
        };

        return new DateCursorAdapter(
                activity.getApplicationContext(),
                R.layout.l_detailed_check_box_list_item,
                activity.getContentResolver().query(
                        Provider.ContentUri.INCOME_REPORT, null, selection, null, sortOrder),
                cols,
                to);
    } // End of LoadIncomeReportCheckable.

    //----------------------------------------------------------------------------------------Others
    public static long setMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    } // End of setMidnight.

    public static int numberOfDaysInMonth(int year, int month) {
        Calendar monthStart = new GregorianCalendar(year, month, 1);
        return monthStart.getActualMaximum(Calendar.DAY_OF_MONTH);
    } // numberOfDaysInMonth

    public static String WhereChain(
            String tableName, String colName, ArrayList<Object> values, String AndOr) {
        String nameEq = tableName + "." + colName + "=";
        String where = nameEq + String.valueOf(values.get(0));
        for (int j = 1; j < values.size(); j++) {
            where += " " + AndOr + " ";
            where += nameEq + String.valueOf(values.get(j));
        } // End where for.
        return where;
    } // End of WhereChain.
} // End of Helpers.
