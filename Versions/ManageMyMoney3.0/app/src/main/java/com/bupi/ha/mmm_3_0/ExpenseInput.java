package com.bupi.ha.mmm_3_0;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bupi.ha.mmm_3_0.db.DataContract.Expense;
import com.bupi.ha.mmm_3_0.db.Provider;
import com.bupi.ha.mmm_3_0.helpers.BackStackFragment;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

import java.util.Calendar;

/**
 * Created by Htet Aung on 6/22/2016.
 * Hello
 */
public class ExpenseInput extends BackStackFragment {
    private long _selectedDate;
    private ActivityMain _activity;
    final Calendar _calendar = Calendar.getInstance();

    public ExpenseInput() {
        super.BACK_STACK_LEVEL = BackStackLevel.GROUND;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _activity = (ActivityMain) getActivity();
        _activity.setTitle(R.string.app_title_expense_input);

        // Inflate the corresponding xml layout and retrieve View object.
        View rootView = inflater.inflate(R.layout.f_expense_input, container, false);

        AddListeners(rootView); // Add listeners to the views.

        // Load expense divisions into the corresponding spinner.
        Spinner divisionSpinner = (Spinner) rootView.findViewById(R.id.spinner_expense_input_expense_division);
        divisionSpinner.setAdapter(Helpers.LoadExpenseDivisions(_activity, false));

        // Load expense categories into the corresponding spinner.
        LoadCategoryOptions(rootView);
        SetDefaultDate(rootView);
        LoadExpenses(rootView);
        return rootView;
    } // End of onCreateView.

    private void SetDefaultDate(View rootView) {
        _selectedDate = Helpers.setMidnight(_calendar);
        ((ImageTextButton) rootView.findViewById(
                R.id.image_text_calendar)).setText(Helpers.dateFormat.format(_selectedDate));
    } // End of SetDefaultDate.

    private void LoadCategoryOptions(View rootView) {
        Spinner divisionSpinner = (Spinner) rootView.findViewById(
                R.id.spinner_expense_input_expense_division);
        if (divisionSpinner.getSelectedItemPosition() > -1) {
            Spinner categorySpinner = (Spinner) rootView.findViewById(
                    R.id.spinner_expense_input_expense_category);
            categorySpinner.setAdapter(
                    Helpers.LoadExpenseCategoriesByExpenseDivisionId(
                            _activity,
                            divisionSpinner.getSelectedItemId())
            ); // End of setAdapter.
        } else {
            Toast.makeText(_activity.getApplicationContext(),
                    getString(R.string.prompt_no_division),
                    Toast.LENGTH_SHORT)
                    .show();
        } // End of if.
    } // End of LoadCategoryOptions.

    private void LoadExpenses(View rootView) {
        String selection = "ExpenseDate=" + String.valueOf(_selectedDate);
        String sortOrder = "DivisionName";
        //boolean useAllFilters = ((CheckBox) rootView.findViewById(R.id.check_box_use_all_filters)).isChecked();

        //if (useAllFilters) {
        long expenseDivisionId = ((Spinner) rootView.findViewById(
                R.id.spinner_expense_input_expense_division)).getSelectedItemId();
        long expenseCategoryId = ((Spinner) rootView.findViewById(
                R.id.spinner_expense_input_expense_category)).getSelectedItemId();

        selection += " AND ";
        selection += "ExpenseDivisionID=" + String.valueOf(expenseDivisionId);
        selection += " AND ";
        selection += "ExpenseCategoryID=" + String.valueOf(expenseCategoryId);
        //} // End of if.

        // Bind the data.
        ((ListView) rootView.findViewById(R.id.list_view_expense_input)).setAdapter(
                Helpers.LoadExpenseReport(_activity, selection, sortOrder)
        );
    } // End of LoadExpenses.

    private void AddListeners(final View rootView) {
        //------------------------------------------------------------------------Calendar ImageText
        final ImageTextButton imageTextCalendar = (ImageTextButton) rootView.findViewById(
                R.id.image_text_calendar);
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                _calendar.set(year, monthOfYear, dayOfMonth);
                _selectedDate = Helpers.setMidnight(_calendar);
                imageTextCalendar.setText(Helpers.dateFormat.format(_selectedDate));
                LoadExpenses(rootView);
            } // End of onDateSet.
        }; // End of OnDateSetListener.
        imageTextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        _activity,
                        //R.style.DatePicker,
                        dateSetListener,
                        _calendar.get(Calendar.YEAR),
                        _calendar.get(Calendar.MONTH),
                        _calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            } // End of onClick.
        }); // End of setOnClickListener.

        //--------------------------------------------------------------------------Division Spinner
        Spinner divisionSpinner = (Spinner) rootView.findViewById(
                R.id.spinner_expense_input_expense_division);
        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadCategoryOptions(rootView);
                LoadExpenses(rootView);
            } // End of onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            } // End of onNothingSelected
        }); // End of setOnItemSelectedListener.
        divisionSpinner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Helpers().replaceFragment(ExpenseInput.this.BACK_STACK_LEVEL,
                        _activity.getFragmentManager(), new ExpenseDivisionInsert(),
                        "ExpenseDivisionInsert");
                return true;
            } // End of onLongClick.
        }); // End of setOnLongClickListener.

        //--------------------------------------------------------------------------Category Spinner
        Spinner categorySpinner = (Spinner) rootView.findViewById(
                R.id.spinner_expense_input_expense_category);
        categorySpinner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Helpers().replaceFragment(ExpenseInput.this.BACK_STACK_LEVEL,
                        _activity.getFragmentManager(), new ExpenseCategoryInsert(),
                        "ExpenseCategoryInsert");
                return true;
            } // End of onLongClick.
        }); // End of setOnLongClickListener.

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadExpenses(rootView);
            } // End of onItemSelected.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            } // End of onNothingSelected
        }); // End of setOnItemSelectedListener.

        //------------------------------------------------------------------------Button Add Expense
        Button buttonAddExpense = (Button) rootView.findViewById(R.id.button_insert_expense);
        buttonAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs from the views.
                Spinner spinnerExpenseDivision = (Spinner) rootView.findViewById(
                        R.id.spinner_expense_input_expense_division);
                Spinner spinnerExpenseCategory = (Spinner) rootView.findViewById(
                        R.id.spinner_expense_input_expense_category);
                EditText editTextAmount = (EditText) rootView.findViewById(R.id.edit_text_amount);

                if (spinnerExpenseDivision.getSelectedItemPosition() > -1 &&
                        spinnerExpenseCategory.getSelectedItemPosition() > -1 &&
                        editTextAmount.getText().length() > 0) {
                    long divisionId = spinnerExpenseDivision.getSelectedItemId();
                    long categoryId = spinnerExpenseCategory.getSelectedItemId();
                    int amount = Integer.parseInt(editTextAmount.getText().toString());
                    _selectedDate = Helpers.setMidnight(_calendar);

                    Log.i("ExpenseInput Date: ", String.valueOf(_selectedDate));

                    // Put values in content values object.
                    ContentValues values = new ContentValues();
                    values.put(Expense.Columns.DATE, _selectedDate);
                    values.put(Expense.Columns.DIVISION, divisionId);
                    values.put(Expense.Columns.CATEGORY, categoryId);
                    values.put(Expense.Columns.AMOUNT, amount);

                    // Do insertion.
                    _activity.getContentResolver().insert(Provider.ContentUri.EXPENSE, values);
                    // Clear fields.
                    editTextAmount.setText("");
                    // Prompt the user.
                    Toast.makeText(_activity.getApplicationContext(),
                            getString(R.string.prompt_new_record_insert_complete),
                            Toast.LENGTH_SHORT)
                            .show();
                    LoadExpenses(rootView);
                } else {
                    Toast.makeText(_activity.getApplicationContext(),
                            getString(R.string.prompt_invalid),
                            Toast.LENGTH_SHORT)
                            .show();
                } // End of if.
            } // End of onClick.
        }); // End of setOnClickListener.
    } // End of AddListeners
} // End of ExpenseInput
