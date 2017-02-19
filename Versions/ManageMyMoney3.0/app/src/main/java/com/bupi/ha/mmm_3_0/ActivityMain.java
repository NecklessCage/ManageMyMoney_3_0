package com.bupi.ha.mmm_3_0;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.support.annotation.NonNull;

import com.bupi.ha.mmm_3_0.db.Provider;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

import java.util.Calendar;

public class ActivityMain extends AppCompatActivity {

    final public Calendar calendar = Calendar.getInstance();
    public Cursor
            cursorExpenseDivisions,
            cursorIncomeDivisions;
    private Helpers helper = new Helpers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        AddListeners();

        // Add the first fragment.
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new FragmentStartPage())
                .addToBackStack("start_page")
                .commit();
        //fm.executePendingTransactions();
    } // End of onCreate.

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        } // End of if.
    } // End of onBackPressed.

    private void AddListeners() {
        // Bottom Navigation View
        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavigationItemSelected(item);
                return true;
            }
        });
    }

    private void handleBottomNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_expense:
                helper.replaceFragment(BackStackLevel.SECOND, getFragmentManager(),
                        new ExpenseInput(), "ExpenseInput");
                break;
            case R.id.nav_income:
                helper.replaceFragment(BackStackLevel.SECOND, getFragmentManager(),
                        new IncomeInput(), "IncomeInput");
                break;
            case R.id.nav_report:
                helper.replaceFragment(BackStackLevel.SECOND, getFragmentManager(),
                        new Report(), "Report");
                break;
            default: break;
        }
    }

    public void onClickDeleteExpenses(View view) {
        getContentResolver().delete(Provider.ContentUri.EXPENSE, null, null);
        getContentResolver().delete(Provider.ContentUri.CATEGORY, null, null);
        getContentResolver().delete(Provider.ContentUri.DIVISION, null, null);
        // Clear the cursors.
        if (cursorIncomeDivisions != null) {
            cursorIncomeDivisions.close();
        }
        if (cursorExpenseDivisions != null) {
            cursorExpenseDivisions.close();
        }
    } // End of onClickDeleteExpenses

    public void onClickDeleteIncomes(View view) {
        getContentResolver().delete(Provider.ContentUri.INCOME, null, null);
        getContentResolver().delete(Provider.ContentUri.INCOME_CATEGORY, null, null);
        getContentResolver().delete(Provider.ContentUri.INCOME_DIVISION, null, null);
        // Clear the cursors.
        if (cursorIncomeDivisions != null) {
            cursorIncomeDivisions.close();
        } // End of if.
        if (cursorExpenseDivisions != null) {
            cursorExpenseDivisions.close();
        } // End of if.
    } // End of onClickDeleteIncomes.

    public void onClickEditText(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    } // End of onClickEditText.

    //------------------------------------------------------------------------Handlers Expense Input

} // class
