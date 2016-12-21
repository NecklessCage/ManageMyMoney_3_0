package com.bupi.ha.mmm_3_0;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.bupi.ha.mmm_3_0.db.Provider;
import com.bupi.ha.mmm_3_0.helpers.BackStackFragment;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

import java.util.Calendar;

/**
 * Created by user on 12/20/2016.
 * Hello
 */

public class FragmentStartPage extends BackStackFragment {

    private ActivityMain _activity;
    private ListView _weeklyExpense;
    private ListView _monthlyIncome;

    public FragmentStartPage() {
        super.BACK_STACK_LEVEL = BackStackLevel.FIRST;
    } // constructor.

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_start_page, container, false);

        // Initialize
        _activity = (ActivityMain) getActivity();
        _weeklyExpense = (ListView) v.findViewById(R.id.list_view_weekly_expense);
        _monthlyIncome = (ListView) v.findViewById(R.id.list_view_this_month_income);

        populateListView();

        // Button
        v.findViewById(R.id.btn_reset_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _activity.getContentResolver().delete(Provider.ContentUri.EXPENSE, null, null);
                _activity.getContentResolver().delete(Provider.ContentUri.CATEGORY, null, null);
                _activity.getContentResolver().delete(Provider.ContentUri.DIVISION, null, null);
                _activity.getContentResolver().delete(Provider.ContentUri.INCOME, null, null);
                _activity.getContentResolver().delete(Provider.ContentUri.INCOME_CATEGORY, null, null);
                _activity.getContentResolver().delete(Provider.ContentUri.INCOME_DIVISION, null, null);
            } // onClick
        });

        return v;
    } // onCreateView

    private void populateListView() {
        // Expenses
        Calendar todayCal = Calendar.getInstance();
        long today = Helpers.setMidnight(todayCal);
        Calendar aWeekAgoCal = Calendar.getInstance();
        aWeekAgoCal.add(Calendar.DATE, -7);
        long aWeekAgo = Helpers.setMidnight(aWeekAgoCal);
        String selection = "ExpenseDate>=" + aWeekAgo + " AND ExpenseDate<" + today;
        String sortOrder = "ExpenseDate";
        _weeklyExpense.setAdapter(Helpers.LoadExpenseReport(_activity, selection, sortOrder));

        // Incomes
        sortOrder = "IncomeDate";
        Calendar thisMonthCal = Calendar.getInstance();
        thisMonthCal.add(Calendar.MONTH, -1);
        thisMonthCal.set(Calendar.DAY_OF_MONTH, Helpers.numberOfDaysInMonth(
                thisMonthCal.get(Calendar.YEAR),
                thisMonthCal.get(Calendar.MONTH)));
        selection = "IncomeDate>=" + Helpers.setMidnight(thisMonthCal) + " AND IncomeDate<" + today;
        _monthlyIncome.setAdapter(Helpers.LoadIncomeReport(_activity, selection, sortOrder));
    } // populateListView

} // class
