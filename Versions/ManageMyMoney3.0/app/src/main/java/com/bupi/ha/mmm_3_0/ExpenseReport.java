package com.bupi.ha.mmm_3_0;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bupi.ha.mmm_3_0.db.DataContract;
import com.bupi.ha.mmm_3_0.db.Provider;
import com.bupi.ha.mmm_3_0.helpers.BackStackFragment;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Htet Aung on 7/7/2016.
 * Hello
 */
public class ExpenseReport extends BackStackFragment {
    private ActivityMain activity;

    private ArrayList<Object> selectedExpenseIds;

    private long selectedDateFrom;
    private long selectedDateTo;

    public ExpenseReport() {
        super.BACK_STACK_LEVEL = BackStackLevel.SECOND;
    } // End of constructor.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (ActivityMain) getActivity();
        activity.setTitle(R.string.app_title_expense_report);

        // Selected expenses from the list view for deletion.
        selectedExpenseIds = new ArrayList<>();

        // Inflate the corresponding xml layout and retrieve View object.
        final View rootView = inflater.inflate(R.layout.f_expense_report, container, false);
        // Add listeners.
        AddListeners(rootView);

        // Division spinner.
        Spinner divisionSpinner = (Spinner)
                rootView.findViewById(R.id.spinner_expense_report_division);
        divisionSpinner.setAdapter(Helpers.LoadExpenseDivisions(activity, false));

        long today = Helpers.SetMidnight(activity.calendar);
        selectedDateFrom = today;
        selectedDateTo = today;
        ((ImageTextButton) rootView.findViewById(R.id.image_text_expense_report_date_from))
                .setText(Helpers.dateFormat.format(selectedDateFrom));
        ((ImageTextButton) rootView.findViewById(R.id.image_text_expense_report_date_to))
                .setText(Helpers.dateFormat.format(selectedDateTo));

        LoadExpenses(rootView);
        return rootView;
    } // End of onCreateView.

    private void AddListeners(final View rootView) {
        // Get views.
        final CheckBox checkBoxDateFrom = (CheckBox) rootView.findViewById(
                R.id.check_box_expense_report_start_date);
        final CheckBox checkBoxDateTo = (CheckBox) rootView.findViewById(
                R.id.check_box_expense_report_end_date);
        final CheckBox checkBoxExpenseDivision = (CheckBox) rootView.findViewById(
                R.id.check_box_expense_report_division);
        final CheckBox checkBoxExpenseCategory = (CheckBox) rootView.findViewById(
                R.id.check_box_expense_report_category);
        final ImageTextButton imageTextDateFrom = (ImageTextButton) rootView.findViewById(
                R.id.image_text_expense_report_date_from);
        final ImageTextButton imageTextDateTo = (ImageTextButton) rootView.findViewById(
                R.id.image_text_expense_report_date_to);
        final Spinner spinnerExpenseDivision = (Spinner) rootView.findViewById(
                R.id.spinner_expense_report_division);
        final Spinner spinnerExpenseCategory = (Spinner) rootView.findViewById(
                R.id.spinner_expense_report_category);

        // Un-check checkboxes.
        checkBoxDateFrom.setChecked(false);
        checkBoxDateTo.setChecked(false);
        checkBoxExpenseCategory.setChecked(false);
        checkBoxExpenseCategory.setEnabled(false);

        // Disable.
        imageTextDateFrom.setEnabled(false);
        imageTextDateTo.setEnabled(false);
        checkBoxExpenseCategory.setEnabled(false);
        spinnerExpenseDivision.setEnabled(false);
        spinnerExpenseCategory.setEnabled(false);

        // Set invisible.
        imageTextDateFrom.setVisibility(View.INVISIBLE);
        imageTextDateTo.setVisibility(View.INVISIBLE);
        checkBoxExpenseCategory.setVisibility(View.INVISIBLE);
        spinnerExpenseDivision.setVisibility(View.INVISIBLE);
        spinnerExpenseCategory.setVisibility(View.INVISIBLE);

        //------------------------------------------------------------------------CheckBox Date From
        checkBoxDateFrom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                imageTextDateFrom.setEnabled(isChecked);
                imageTextDateFrom.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                LoadExpenses(rootView);
            } // End of onCheckedChanged.
        }); // End of setOnCheckedChangeListener.

        //-----------------------------------------------------------------------ImageText Date From
        final DatePickerDialog.OnDateSetListener dateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                activity.calendar.set(year, monthOfYear, dayOfMonth);
                selectedDateFrom = Helpers.SetMidnight(activity.calendar);
                imageTextDateFrom.setText(Helpers.dateFormat.format(selectedDateFrom));
                LoadExpenses(rootView);
            } // End of onDateSet.
        }; // End of OnDateSetListener.
        imageTextDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        activity,
                        //R.style.DatePicker,
                        dateSetListenerFrom,
                        activity.calendar.get(Calendar.YEAR),
                        activity.calendar.get(Calendar.MONTH),
                        activity.calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            } // End of onClick.
        }); // End of setOnClickListener.

        //-------------------------------------------------------------------------ImageText Date To
        final DatePickerDialog.OnDateSetListener dateSetListenerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                activity.calendar.set(year, monthOfYear, dayOfMonth);
                selectedDateTo = Helpers.SetMidnight(activity.calendar);
                imageTextDateTo.setText(Helpers.dateFormat.format(selectedDateTo));
                LoadExpenses(rootView);
            } // End of onDateSet.
        }; // End of OnDateSetListener.
        imageTextDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        activity,
                        //R.style.DatePicker,
                        dateSetListenerTo,
                        activity.calendar.get(Calendar.YEAR),
                        activity.calendar.get(Calendar.MONTH),
                        activity.calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            } // End of onClick.
        }); // End of setOnClickListener.

        //--------------------------------------------------------------------------CheckBox Date To
        checkBoxDateTo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                imageTextDateTo.setEnabled(isChecked);
                imageTextDateTo.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                LoadExpenses(rootView);
            } // End of onCheckedChanged.
        }); // End of setOnCheckedChangeListener.

        //-------------------------------------------------------------------------CheckBox Division
        checkBoxExpenseDivision.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Enable certain views if the check box is checked.
                int visibility = isChecked ? View.VISIBLE : View.INVISIBLE;

                checkBoxExpenseCategory.setEnabled(isChecked);
                spinnerExpenseDivision.setEnabled(isChecked);
                checkBoxExpenseCategory.setVisibility(visibility);
                spinnerExpenseDivision.setVisibility(visibility);
                // Enable category spinner if both division and category are checked.
                spinnerExpenseCategory.setEnabled(isChecked && checkBoxExpenseCategory.isChecked());
                visibility = isChecked && checkBoxExpenseCategory.isChecked() ?
                        View.VISIBLE : View.INVISIBLE;
                spinnerExpenseCategory.setVisibility(visibility);

                LoadExpenses(rootView);
            } // End of onCheckedChanged.
        }); // End of setOnCheckedChangeListener.

        //-------------------------------------------------------------------------Category CheckBox
        checkBoxExpenseCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinnerExpenseCategory.setEnabled(isChecked);
                spinnerExpenseCategory.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                LoadExpenses(rootView);
            } // End of onCheckedChanged.
        }); // End of setOnCheckedChangeListener.

        //--------------------------------------------------------------------------Division Spinner
        spinnerExpenseDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadCategories(rootView);
                LoadExpenses(rootView);
            } // End of onItemSelected.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            } // End of onNothingSelected.
        }); // End of setOnItemSelectedListener.

        //--------------------------------------------------------------------------Category Spinner
        spinnerExpenseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadExpenses(rootView);
            } // End of onItemSelected.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            } // End of onNothingSelected.
        }); // End of setOnItemSelectedListener.

        //-----------------------------------------------------------Button Delete Selected Expenses
        Button buttonDeleteSelectedExpenses = (Button) rootView.findViewById(
                R.id.button_delete_selected_expenses);
        buttonDeleteSelectedExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedExpenseIds.size() == 0) {
                    Toast.makeText(
                            activity.getApplicationContext(),
                            getString(R.string.prompt_no_items_selected),
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Confirmation
                    //new AlertDialog.Builder(rootView.getContext(), R.style.AppTheme)
                    new AlertDialog.Builder(activity)//, R.style.Confirmation)
                            //new ContextThemeWrapper(rootView.getContext(), R.style.Confirmation))
                            .setTitle(getString(R.string.prompt_confirmation))
                            .setMessage(getString(R.string.prompt_confirmation_message))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Delete the records
                                    getActivity().getContentResolver().delete(Provider.ContentUri.EXPENSE,
                                            Helpers.WhereChain(DataContract.Expense.TABLE_NAME,
                                                    DataContract.Expense.Columns._ID,
                                                    selectedExpenseIds, "OR"),
                                            null);
                                    Toast.makeText(activity.getApplicationContext(),
                                            getString(R.string.prompt_record_deleted),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    LoadExpenses(rootView);
                                } // End of onClick.
                            })
                            .setNegativeButton(R.string.dialog_cancel, null)
                            .show();
                } // End of if.
            } // End of onClick.
        }); // End of setOnClickListener.

        // ListView
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_expense_report);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout row = (LinearLayout) view;
                CheckedTextView checker = (CheckedTextView) row.getChildAt(row.getChildCount() - 1);
                if (checker.isChecked()) { // Deselection.
                    selectedExpenseIds.remove(id);
                    checker.setChecked(false);
                } else { // New selection.
                    selectedExpenseIds.add(id);
                    checker.setChecked(true);
                } // End of if.
            } // End of onItemClick.
        }); // End of setOnItemClickListener.
    } // End of AddListeners.

    private void LoadCategories(View rootView) {
        Spinner spinnerDivision = (Spinner) rootView.findViewById(
                R.id.spinner_expense_report_division);
        if (spinnerDivision.getSelectedItemPosition() > -1) {
            ((Spinner) rootView.findViewById(R.id.spinner_expense_report_category)).setAdapter(
                    Helpers.LoadExpenseCategoriesByExpenseDivisionId(
                            activity,
                            spinnerDivision.getSelectedItemId()
                    )
            ); // End of setAdapter.
        } // End of if.
    } // End of LoadCategories.

    private void LoadExpenses(View rootView) {
        // Selection String.
        String selection = "";
        String sortOrder = "ExpenseDate";

        CheckBox checkBoxDateFrom = (CheckBox) rootView.findViewById(
                R.id.check_box_expense_report_start_date);
        CheckBox checkBoxDateTo = (CheckBox) rootView.findViewById(
                R.id.check_box_expense_report_end_date);
        CheckBox checkBoxDivision = (CheckBox) rootView.findViewById(
                R.id.check_box_expense_report_division);
        CheckBox checkBoxCategory = (CheckBox) rootView.findViewById(
                R.id.check_box_expense_report_category);
        Spinner spinnerDivision = (Spinner) rootView.findViewById(
                R.id.spinner_expense_report_division);
        Spinner spinnerCategory = (Spinner) rootView.findViewById(
                R.id.spinner_expense_report_category);

        if (checkBoxDateFrom.isChecked()) {
            selection += "ExpenseDate>=" + String.valueOf(selectedDateFrom);
        } // End of if.
        if (checkBoxDateTo.isChecked()) {
            selection += selection.trim().isEmpty() ? "" : " AND ";
            selection += "ExpenseDate<=" + String.valueOf(selectedDateTo);
        } // End of if.
        if (checkBoxDivision.isChecked()) {
            selection += selection.trim().isEmpty() ? "" : " AND ";
            selection += "ExpenseDivisionID="
                    + String.valueOf(spinnerDivision.getSelectedItemId());
        } // End of if.
        if (checkBoxDivision.isChecked() && checkBoxCategory.isChecked()) {
            selection += " AND ";
            selection += "ExpenseCategoryID="
                    + String.valueOf(spinnerCategory.getSelectedItemId());
        } // End of if.

        // Columns to be bound.
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_expense_report);
        listView.setAdapter(Helpers.LoadExpenseReportCheckable(activity, selection, sortOrder));
    } // End of LoadExpenses.
} // End of class.
