package com.bupi.ha.mmm_3_0;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
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

import com.bupi.ha.mmm_3_0.db.DataContract.Income;
import com.bupi.ha.mmm_3_0.db.Provider;
import com.bupi.ha.mmm_3_0.helpers.BackStackFragment;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

import java.util.Calendar;

/**
 * Created by Htet Aung on 7/8/2016.
 * Hello
 */
public class IncomeInput extends BackStackFragment {
    private ActivityMain activity;
    private long selectedIncomeDate;

    public IncomeInput() {
        super.BACK_STACK_LEVEL = BackStackLevel.FIRST;
    } // End of constructor.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (ActivityMain) getActivity();
        activity.setTitle(R.string.app_title_income_input);

        // Inflate the corresponding xml layout and retrieve View object.
        View rootView = inflater.inflate(R.layout.f_income_input, container, false);
        AddListeners(rootView); // Add listeners to the views.

        // Find the spinner to be bound to the data and set its adapter.
        ((Spinner) rootView.findViewById(R.id.spinner_income_input_income_division)).setAdapter(
                Helpers.LoadIncomeDivisions(activity, true)
        );
        // Load income categories.
        LoadIncomeCategoryOptions(rootView);
        SetDefaultDate(rootView);
        LoadIncomes(rootView);
        return rootView;
    } // End of onCreateView.

    private void SetDefaultDate(View rootView) {
        selectedIncomeDate = Helpers.setMidnight(Calendar.getInstance());
        ((ImageTextButton) rootView.findViewById(R.id.image_text_income_calendar))
                .setText(Helpers.dateFormat.format(selectedIncomeDate));
    } // End of SetDefaultDate.

    private void LoadIncomes(final View rootView) {
        String selection = "IncomeDate=" + String.valueOf(selectedIncomeDate);
        String sortOrder = "IncomeDivisionName";
        //boolean useAllFilters = ((CheckBox) rootView.findViewById(R.id.check_box_use_all_filters)).isChecked();

        //if (useAllFilters) {
        long incomeDivisionId = ((Spinner) rootView.findViewById(
                R.id.spinner_income_input_income_division)).getSelectedItemId();
        long incomeCategoryId = ((Spinner) rootView.findViewById(
                R.id.spinner_income_input_income_category)).getSelectedItemId();

        selection += " AND ";
        selection += "IncomeDivisionID=" + String.valueOf(incomeDivisionId);
        selection += " AND ";
        selection += "IncomeCategoryID=" + String.valueOf(incomeCategoryId);
        //} // End of if.

        // Bind the data.
        ((ListView) rootView.findViewById(R.id.list_view_income_input)).setAdapter(
                Helpers.LoadIncomeReport(activity, selection, sortOrder)
        );
    } // End of LoadIncomes.

    private void LoadIncomeCategoryOptions(View rootView) {
        Spinner incomeDivisionSpinner = (Spinner) rootView.findViewById(
                R.id.spinner_income_input_income_division);
        if (incomeDivisionSpinner.getSelectedItemPosition() > -1) {
            ((Spinner) rootView.findViewById(R.id.spinner_income_input_income_category)).setAdapter(
                    Helpers.LoadIncomeCategoriesByIncomeDivisionId(
                            activity,
                            incomeDivisionSpinner.getSelectedItemId())
            );
        } else {
            Toast.makeText(activity.getApplicationContext(),
                    getString(R.string.prompt_no_division),
                    Toast.LENGTH_SHORT)
                    .show();
        } // End of if.
    } // End of LoadIncomeCategoryOptions.

    private void AddListeners(final View rootView) {
        //------------------------------------------------------------------------Calendar ImageText
        final ImageTextButton imageTextIncomeCalendar = (ImageTextButton) rootView.findViewById(
                R.id.image_text_income_calendar);
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                activity.calendar.set(year, monthOfYear, dayOfMonth);
                selectedIncomeDate = Helpers.setMidnight(activity.calendar);
                imageTextIncomeCalendar.setText(Helpers.dateFormat.format(selectedIncomeDate));
                // Reload data.
                LoadIncomes(rootView);
            } // End of onDateSet.
        }; // End of OnDateSetListener.
        imageTextIncomeCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        activity,
                        //R.style.DatePicker,
                        dateSetListener,
                        activity.calendar.get(Calendar.YEAR),
                        activity.calendar.get(Calendar.MONTH),
                        activity.calendar.get(Calendar.DAY_OF_MONTH)).show();
            } // End of onClick.
        }); // End of setOnClickListener.

        //--------------------------------------------------------------------------Division Spinner
        Spinner spinnerIncomeDivision = (Spinner) rootView.findViewById(
                R.id.spinner_income_input_income_division);
        spinnerIncomeDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadIncomeCategoryOptions(rootView);
                LoadIncomes(rootView);
            } // End of onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            } // End of onNothingSelected
        }); // End of setOnItemSelectedListener.
        spinnerIncomeDivision.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Helpers().replaceFragment(IncomeInput.this.BACK_STACK_LEVEL,
                        activity.getFragmentManager(), new IncomeDivisionInsert(),
                        "IncomeDivisionInsert");
                return true;
            } // End of onLongClick.
        }); // End of setOnLongClickListener.

        //--------------------------------------------------------------------------Category Spinner
        Spinner incomeCategorySpinner = (Spinner) rootView.findViewById(
                R.id.spinner_income_input_income_category);
        incomeCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadIncomes(rootView);
            } // End of onItemSelected.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            } // End of onNothingSelected.
        }); // End of setOnItemSelectedListener.
        incomeCategorySpinner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Helpers().replaceFragment(IncomeInput.this.BACK_STACK_LEVEL,
                        activity.getFragmentManager(), new IncomeCategoryInsert(),
                        "IncomeCategoryInsert");
                return true;
            } // End of onLongClick.
        }); // End of setOnLongClickListener.

        //------------------------------------------------------------------------Button Add Expense
        Button buttonAddIncome = (Button) rootView.findViewById(R.id.button_insert_income);
        buttonAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs from the views.
                Spinner incomeDivisionSpinner = (Spinner) rootView.findViewById(R.id.spinner_income_input_income_division);
                Spinner incomeCategorySpinner = (Spinner) rootView.findViewById(R.id.spinner_income_input_income_category);
                EditText incomeNoteEditText = (EditText) rootView.findViewById(R.id.edit_text_income_note);
                EditText editTextIncomeAmount = (EditText) rootView.findViewById(R.id.edit_text_income_amount);

                if (incomeDivisionSpinner.getSelectedItemPosition() > -1 &&
                        incomeCategorySpinner.getSelectedItemPosition() > -1 &&
                        editTextIncomeAmount.getText().length() > 0) {
                    long incomeDivisionId = incomeDivisionSpinner.getSelectedItemId();
                    long incomeCategoryId = incomeCategorySpinner.getSelectedItemId();
                    String incomeNote = incomeNoteEditText.getText().toString();
                    int incomeAmount = Integer.parseInt(editTextIncomeAmount.getText().toString());

                    // Put values in content values object.
                    ContentValues values = new ContentValues();
                    values.put(Income.Columns.INCOME_DATE, selectedIncomeDate);
                    values.put(Income.Columns.INCOME_DIVISION, incomeDivisionId);
                    values.put(Income.Columns.INCOME_CATEGORY, incomeCategoryId);
                    values.put(Income.Columns.INCOME_NOTE, incomeNote);
                    values.put(Income.Columns.INCOME_AMOUNT, incomeAmount);

                    // Do insertion.
                    activity.getContentResolver().insert(Provider.ContentUri.INCOME, values);
                    // Clear fields.
                    editTextIncomeAmount.setText("");
                    // Prompt the user.
                    Toast.makeText(activity.getApplicationContext(),
                            getString(R.string.prompt_new_record_insert_complete),
                            Toast.LENGTH_SHORT)
                            .show();
                    LoadIncomes(rootView);
                } else {
                    Toast.makeText(activity.getApplicationContext(),
                            getString(R.string.prompt_invalid),
                            Toast.LENGTH_SHORT)
                            .show();
                } // End of if.
            } // End of onClick.
        }); // End of setOnClickListener.
    } // End of AddListeners.
} // End of class.
