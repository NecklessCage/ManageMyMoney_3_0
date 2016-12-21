package com.bupi.ha.mmm_3_0;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bupi.ha.mmm_3_0.db.Provider;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

import java.util.Calendar;

public class ActivityMain extends AppCompatActivity {

    private boolean isFABOpen = false;

    private FloatingActionButton fam;
    private android.support.design.widget.FloatingActionButton fab1, fab2, fab3, fab4;

    final public Calendar calendar = Calendar.getInstance();
    public Cursor
            cursorExpenseDivisions,
            cursorIncomeDivisions;
    Helpers helper = new Helpers();

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

    private void showFABMenu() {
        fam.animate().rotation(-180);
        //fam.setImageDrawable(ContextCompat.getDrawable(ActivityMain.this, R.drawable.caret_arrow_right));
        isFABOpen = true;
        fab1.animate().translationX(-getResources().getDimension(R.dimen.offset_1));
        fab2.animate().translationX(-getResources().getDimension(R.dimen.offset_2));
        fab3.animate().translationX(-getResources().getDimension(R.dimen.offset_3));
        fab4.animate().translationX(-getResources().getDimension(R.dimen.offset_4));
    }

    private void closeFABMenu() {
        fam.animate().rotation(0);
        //fam.setImageDrawable(ContextCompat.getDrawable(ActivityMain.this, R.drawable.caret_arrow_left));
        isFABOpen = false;
        fab1.animate().translationX(0);
        fab2.animate().translationX(0);
        fab3.animate().translationX(0);
        fab4.animate().translationX(0);
    }

    private void AddListeners() {
        // Fab Menu
        fam = (FloatingActionButton) findViewById(R.id.fam);
        fab4 = (FloatingActionButton) findViewById(R.id.fab_snapshot);
        fab3 = (FloatingActionButton) findViewById(R.id.fab_report);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_income);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_expense);

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFABOpen) {
                    closeFABMenu();
                } else {
                    showFABMenu();
                } // if
            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.replaceFragment(BackStackLevel.SECOND, getFragmentManager(),
                        new FragmentStartPage(), "Snapshot");
                closeFABMenu();
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helper.replaceFragment(BackStackLevel.SECOND, getFragmentManager(),
                        new Report(), "Report");
                closeFABMenu();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helper.replaceFragment(BackStackLevel.SECOND, getFragmentManager(),
                        new IncomeInput(), "IncomeInput");
                closeFABMenu();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helper.replaceFragment(BackStackLevel.SECOND, getFragmentManager(),
                        new ExpenseInput(), "ExpenseInput");
                closeFABMenu();
            }
        });

//        //****** First argument of replaceFragment is always set as SECOND level back stack.********
//        ImageView imageViewExpense = (ImageView) findViewById(R.id.image_view_expense);
//        imageViewExpense.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                helper.replaceFragment(BackStackLevel.SECOND, getFragmentManager(),
//                        new ExpenseInput(), "ExpenseInput");
//            } // End of onClick.
//        }); // End of setOnClickListener.
//
//        ImageView imageViewIncome = (ImageView) findViewById(R.id.image_view_income);
//        imageViewIncome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                helper.replaceFragment(BackStackLevel.SECOND, getFragmentManager(),
//                        new IncomeInput(), "IncomeInput");
//            } // End of onClick.
//        }); // End of setOnClickListener.
//
//        ImageView imageViewReport = (ImageView) findViewById(R.id.image_view_report);
//        imageViewReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                helper.replaceFragment(BackStackLevel.SECOND, getFragmentManager(),
//                        new Report(), "Report");
//            } // End of onClick.
//        }); // End of setOnClickListener.
    } // End of AddListeners.

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
