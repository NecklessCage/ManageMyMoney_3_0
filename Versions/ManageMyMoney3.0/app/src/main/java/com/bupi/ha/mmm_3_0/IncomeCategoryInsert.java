package com.bupi.ha.mmm_3_0;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bupi.ha.mmm_3_0.db.DataContract.IncomeCategory;
import com.bupi.ha.mmm_3_0.db.Provider;
import com.bupi.ha.mmm_3_0.helpers.BackStackFragment;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

/**
 * Created by Htet Aung on 7/8/2016.
 * Hello
 */
public class IncomeCategoryInsert extends BackStackFragment {
    private ActivityMain activity;

    public IncomeCategoryInsert() {
        super.BACK_STACK_LEVEL = BackStackLevel.SECOND;
    } // End of constructor.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (ActivityMain) getActivity();
        activity.setTitle(R.string.app_title_income_category_insert);

        // Inflate the corresponding xml layout and retrieve View object.
        View rootView = inflater.inflate(R.layout.f_income_category_insert, container, false);
        // Set adapter to the spinner.
        ((Spinner) rootView.findViewById(
                R.id.spinner_income_division_category_insert)).setAdapter(
                Helpers.LoadIncomeDivisions((ActivityMain) getActivity(), false)
        );
        AddListeners(rootView);
        return rootView;
    } // End of onCreateView.

    private void LoadIncomeCategoryOptions(View rootView) {
        Spinner incomeDivisionSpinner = (Spinner) rootView.findViewById(
                R.id.spinner_income_division_category_insert);
        if (incomeDivisionSpinner.getSelectedItemPosition() > -1) {
            ((ListView) rootView.findViewById(R.id.list_view_income_categories)).setAdapter(
                    Helpers.LoadIncomeCategoriesByIncomeDivisionId(
                            getActivity(),
                            incomeDivisionSpinner.getSelectedItemId())
            );
        } // End of if.
    } // End of LoadIncomeCategoryOptions.

    private void AddListeners(final View rootView) {
        //--------------------------------------------------------------------------Division Spinner
        Spinner incomeDivisionSpinner = (Spinner) rootView.findViewById(
                R.id.spinner_income_division_category_insert);
        incomeDivisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadIncomeCategoryOptions(rootView);
            } // End of onItemSelected.


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            } // End of onNothingSelected.
        }); // End of setOnItemSelectedListener.

        //-----------------------------------------------------------------------------Insert Button
        Button buttonIncomeCategoryInsert = (Button)
                rootView.findViewById(R.id.button_income_category_insert);
        buttonIncomeCategoryInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from the user input.
                Spinner spinnerIncomeDivision = (Spinner) rootView.findViewById(
                        R.id.spinner_income_division_category_insert);
                long incomeDivisionId = spinnerIncomeDivision.getSelectedItemId();
                EditText editTextIncomeCategory = (EditText) rootView.findViewById(
                        R.id.edit_text_new_income_category);
                String incomeCategory = editTextIncomeCategory.getText().toString();
                // Create content values object.
                ContentValues values = new ContentValues();
                values.put(IncomeCategory.Columns.INCOME_CATEGORY, incomeCategory);
                values.put(IncomeCategory.Columns.INCOME_DIVISION, String.valueOf(incomeDivisionId));
                // Do insertion.
                getActivity().getContentResolver().insert(
                        Provider.ContentUri.INCOME_CATEGORY, values);
                // Clear fields.
                editTextIncomeCategory.setText("");
                // Prompt user of the action.
                Toast.makeText(activity.getApplicationContext(),
                        getString(R.string.prompt_category_insert_complete),
                        Toast.LENGTH_SHORT)
                        .show();

                LoadIncomeCategoryOptions(rootView);
            } // End of onClick.
        }); // End of setOnClickListener.
    } // End of AddListeners.
}
