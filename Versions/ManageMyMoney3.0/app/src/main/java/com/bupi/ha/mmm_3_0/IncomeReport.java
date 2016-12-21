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
 * Created by Htet Aung on 7/8/2016.
 * Hello
 */
public class IncomeReport extends BackStackFragment {
    private ActivityMain activity;// = (ActivityMain) getActivity();

    private ArrayList<Object> selectedIncomeIds;

    private long selectedDateFrom;
    private long selectedDateTo;

    public IncomeReport() {
        super.BACK_STACK_LEVEL = BackStackLevel.SECOND;
    } // End of constructor.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (ActivityMain) getActivity();
        activity.setTitle(R.string.app_title_income_report);

        selectedIncomeIds = new ArrayList<>();

        // Inflate the corresponding xml layout and retrieve View object.
        final View rootView = inflater.inflate(R.layout.f_income_report, container, false);
        // Add listeners.
        AddListeners(rootView);

        // Division spinner.
        Spinner divisionSpinner = (Spinner)
                rootView.findViewById(R.id.spinner_income_report_division);
        divisionSpinner.setAdapter(Helpers.LoadIncomeDivisions(activity, false));

        long today = Helpers.SetMidnight(activity.calendar);
        selectedDateFrom = today;
        selectedDateTo = today;
        ((ImageTextButton) rootView.findViewById(R.id.image_text_income_report_date_from))
                .setText(Helpers.dateFormat.format(selectedDateFrom));
        ((ImageTextButton) rootView.findViewById(R.id.image_text_income_report_date_to))
                .setText(Helpers.dateFormat.format(selectedDateTo));

        LoadIncomes(rootView);
        return rootView;
    } // End of onCreateView.

    private void AddListeners(final View rootView) {
        // Get views.
        final CheckBox checkBoxDateFrom = (CheckBox) rootView.findViewById(
                R.id.check_box_income_report_start_date);
        final CheckBox checkBoxDateTo = (CheckBox) rootView.findViewById(
                R.id.check_box_income_report_end_date);
        final CheckBox checkBoxIncomeDivision = (CheckBox) rootView.findViewById(
                R.id.check_box_income_report_division);
        final CheckBox checkBoxIncomeCategory = (CheckBox) rootView.findViewById(
                R.id.check_box_income_report_category);
        final ImageTextButton imageTextDateFrom = (ImageTextButton) rootView.findViewById(
                R.id.image_text_income_report_date_from);
        final ImageTextButton imageTextDateTo = (ImageTextButton) rootView.findViewById(
                R.id.image_text_income_report_date_to);
        final Spinner spinnerIncomeDivision = (Spinner) rootView.findViewById(
                R.id.spinner_income_report_division);
        final Spinner spinnerIncomeCategory = (Spinner) rootView.findViewById(
                R.id.spinner_income_report_category);

        // Un-check checkboxes.
        checkBoxDateFrom.setChecked(false);
        checkBoxDateTo.setChecked(false);
        checkBoxIncomeCategory.setChecked(false);
        checkBoxIncomeCategory.setEnabled(false);

        // Disable.
        imageTextDateFrom.setEnabled(false);
        imageTextDateTo.setEnabled(false);
        checkBoxIncomeCategory.setEnabled(false);
        spinnerIncomeDivision.setEnabled(false);
        spinnerIncomeCategory.setEnabled(false);

        // Set invisible.
        imageTextDateFrom.setVisibility(View.INVISIBLE);
        imageTextDateTo.setVisibility(View.INVISIBLE);
        checkBoxIncomeCategory.setVisibility(View.INVISIBLE);
        spinnerIncomeDivision.setVisibility(View.INVISIBLE);
        spinnerIncomeCategory.setVisibility(View.INVISIBLE);

        //------------------------------------------------------------------------CheckBox Date From
        checkBoxDateFrom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                imageTextDateFrom.setEnabled(isChecked);
                imageTextDateFrom.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                LoadIncomes(rootView);
            } // End of onCheckedChanged.
        }); // End of setOnCheckedChangeListener.

        //-----------------------------------------------------------------------ImageText Date From
        final DatePickerDialog.OnDateSetListener dateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                activity.calendar.set(year, monthOfYear, dayOfMonth);
                selectedDateFrom = Helpers.SetMidnight(activity.calendar);
                imageTextDateFrom.setText(Helpers.dateFormat.format(selectedDateFrom));
                LoadIncomes(rootView);
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
                        activity.calendar.get(Calendar.DAY_OF_MONTH)).show();
            } // End of onClick.
        }); // End of setOnClickListener.

        //-------------------------------------------------------------------------ImageText Date To
        final DatePickerDialog.OnDateSetListener dateSetListenerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                activity.calendar.set(year, monthOfYear, dayOfMonth);
                selectedDateTo = Helpers.SetMidnight(activity.calendar);
                imageTextDateTo.setText(Helpers.dateFormat.format(selectedDateTo));
                LoadIncomes(rootView);
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
                        activity.calendar.get(Calendar.DAY_OF_MONTH)).show();
            } // End of onClick.
        }); // End of setOnClickListener.

        //--------------------------------------------------------------------------CheckBox Date To
        checkBoxDateTo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                imageTextDateTo.setEnabled(isChecked);
                imageTextDateTo.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                LoadIncomes(rootView);
            } // End of onCheckedChanged.
        }); // End of setOnCheckedChangeListener.

        //-------------------------------------------------------------------------CheckBox Division
        checkBoxIncomeDivision.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Enable certain views if the check box is checked.
                int visibility = isChecked ? View.VISIBLE : View.INVISIBLE;

                checkBoxIncomeCategory.setEnabled(isChecked);
                spinnerIncomeDivision.setEnabled(isChecked);
                checkBoxIncomeCategory.setVisibility(visibility);
                spinnerIncomeDivision.setVisibility(visibility);
                // Enable category spinner if both division and category are checked.
                spinnerIncomeCategory.setEnabled(isChecked && checkBoxIncomeCategory.isChecked());
                visibility = isChecked && checkBoxIncomeCategory.isChecked() ?
                        View.VISIBLE : View.INVISIBLE;
                spinnerIncomeCategory.setVisibility(visibility);

                LoadIncomes(rootView);
            } // End of onCheckedChanged.
        }); // End of setOnCheckedChangeListener.

        //-------------------------------------------------------------------------Category CheckBox
        checkBoxIncomeCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinnerIncomeCategory.setEnabled(isChecked);
                spinnerIncomeCategory.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                LoadIncomes(rootView);
            } // End of onCheckedChanged.
        }); // End of setOnCheckedChangeListener.

        //--------------------------------------------------------------------------Division Spinner
        spinnerIncomeDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadCategories(rootView);
                LoadIncomes(rootView);
            } // End of onItemSelected.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            } // End of onNothingSelected.
        }); // End of setOnItemSelectedListener.

        //--------------------------------------------------------------------------Category Spinner
        spinnerIncomeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadIncomes(rootView);
            } // End of onItemSelected.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            } // End of onNothingSelected.
        }); // End of setOnItemSelectedListener.

        //-----------------------------------------------------------Button Delete Selected Incomes
        Button buttonDeleteSelectedIncomes = (Button) rootView.findViewById(
                R.id.button_delete_selected_incomes);
        buttonDeleteSelectedIncomes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedIncomeIds.size() == 0) {
                    Toast.makeText(
                            activity.getApplicationContext(),
                            getResources().getString(R.string.prompt_no_items_selected),
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Confirmation
                    new AlertDialog.Builder(activity)//, R.style.Confirmation)
                            .setTitle(getResources().getString(R.string.prompt_confirmation))
                            .setMessage(getResources().getString(R.string.prompt_confirmation_message))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Delete
                                    getActivity().getContentResolver().delete(Provider.ContentUri.INCOME,
                                            Helpers.WhereChain(DataContract.Income.TABLE_NAME,
                                                    DataContract.Income.Columns._ID,
                                                    selectedIncomeIds, "OR"), null);
                                    Toast.makeText(
                                            activity.getApplicationContext(),
                                            getResources().getString(R.string.prompt_record_deleted),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    LoadIncomes(rootView);
                                } // End of onClick.
                            })
                            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing.
                                } // End of onClick.
                            }).show();
                } // End of if.
            } // End of onClick.
        }); // End of setOnClickListener.

        // ListView
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_income_report);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout row = (LinearLayout) view;
                CheckedTextView checker = (CheckedTextView) row.getChildAt(row.getChildCount() - 1);
                if (checker.isChecked()) { // Deselection.
                    selectedIncomeIds.remove(id);
                    checker.setChecked(false);
                } else { // New selection.
                    selectedIncomeIds.add(id);
                    checker.setChecked(true);
                } // End of if.
            } // End of onItemClick.
        }); // End of setOnItemClickListener.
    } // End of AddListeners.

    private void LoadCategories(View rootView) {
        Spinner spinnerDivision = (Spinner) rootView.findViewById(
                R.id.spinner_income_report_division);
        if (spinnerDivision.getSelectedItemPosition() > -1) {
            ((Spinner) rootView.findViewById(R.id.spinner_income_report_category)).setAdapter(
                    Helpers.LoadIncomeCategoriesByIncomeDivisionId(
                            activity,
                            spinnerDivision.getSelectedItemId()
                    )
            ); // End of setAdapter.
        } // End of if.
    } // End of LoadCategories.

    private void LoadIncomes(View rootView) {
        // Selection String.
        String selection = "";
        String sortOrder = "IncomeDate";

        CheckBox checkBoxDateFrom = (CheckBox) rootView.findViewById(
                R.id.check_box_income_report_start_date);
        CheckBox checkBoxDateTo = (CheckBox) rootView.findViewById(
                R.id.check_box_income_report_end_date);
        CheckBox checkBoxDivision = (CheckBox) rootView.findViewById(
                R.id.check_box_income_report_division);
        CheckBox checkBoxCategory = (CheckBox) rootView.findViewById(
                R.id.check_box_income_report_category);
        Spinner spinnerDivision = (Spinner) rootView.findViewById(
                R.id.spinner_income_report_division);
        Spinner spinnerCategory = (Spinner) rootView.findViewById(
                R.id.spinner_income_report_category);

        if (checkBoxDateFrom.isChecked()) {
            selection += "IncomeDate>=" + String.valueOf(selectedDateFrom);
        } // End of if.
        if (checkBoxDateTo.isChecked()) {
            selection += selection.trim().isEmpty() ? "" : " AND ";
            selection += "IncomeDate<=" + String.valueOf(selectedDateTo);
        } // End of if.
        if (checkBoxDivision.isChecked()) {
            selection += selection.trim().isEmpty() ? "" : " AND ";
            selection += "IncomeDivisionID="
                    + String.valueOf(spinnerDivision.getSelectedItemId());
        } // End of if.
        if (checkBoxDivision.isChecked() && checkBoxCategory.isChecked()) {
            selection += " AND ";
            selection += "IncomeCategoryID="
                    + String.valueOf(spinnerCategory.getSelectedItemId());
        } // End of if.

        // Columns to be bound.
        ((ListView) rootView.findViewById(R.id.list_view_income_report))
                .setAdapter(Helpers.LoadIncomeReportCheckable(activity, selection, sortOrder));
    } // End of LoadIncomes.
} // End of class.
