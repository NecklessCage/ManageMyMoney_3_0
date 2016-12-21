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

import com.bupi.ha.mmm_3_0.db.DataContract.Category;
import com.bupi.ha.mmm_3_0.db.Provider;
import com.bupi.ha.mmm_3_0.helpers.BackStackFragment;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

/**
 * Created by Htet Aung on 7/6/2016.
 * Hello
 */
public class ExpenseCategoryInsert extends BackStackFragment {
    private ActivityMain activity;

    public ExpenseCategoryInsert() {
        super.BACK_STACK_LEVEL = BackStackLevel.FIRST;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (ActivityMain) getActivity();
        activity.setTitle(R.string.app_title_expense_category_insert);

        // Inflate the corresponding xml layout and retrieve View object.
        View rootView = inflater.inflate(R.layout.f_expense_category_insert, container, false);
        // Load expense divisions.
        Spinner divisionSpinner = (Spinner) rootView.findViewById(
                R.id.spinner_division_category_insert);
        divisionSpinner.setAdapter(Helpers.LoadExpenseDivisions(activity, false));
        // Add listeners.
        AddListeners(rootView);
        return rootView;
    } // End of onCreateView.

    private void LoadCategories(View rootView) {
        Spinner divisionSpinner = (Spinner) rootView.findViewById(
                R.id.spinner_division_category_insert);
        if (divisionSpinner.getSelectedItemPosition() > -1) {
            ListView listViewCategory = (ListView) rootView.findViewById(R.id.list_view_categories);
            listViewCategory.setAdapter(
                    Helpers.LoadExpenseCategoriesByExpenseDivisionId(
                            activity,
                            divisionSpinner.getSelectedItemId()
                    )
            ); // End of setAdapter.
        } // End of if.
    } // End of LoadCategories.

    private void AddListeners(final View rootView) {
        //--------------------------------------------------------------------------Division Spinner
        Spinner divisionSpinner = (Spinner) rootView.findViewById(
                R.id.spinner_division_category_insert);
        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadCategories(rootView);
            } // End of onItemSelected.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            } // End of onNothingSelected.
        }); // End of setOnItemSelectedListener.

        //-----------------------------------------------------------------------------Insert Button
        Button buttonCategoryInsert = (Button) rootView.findViewById(R.id.button_category_insert);
        buttonCategoryInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from the user input.
                EditText editTextCategory = (EditText) rootView.findViewById(
                        R.id.edit_text_new_category);
                String category = editTextCategory.getText().toString();
                if (category.trim().isEmpty()) {
                    Toast.makeText(activity.getApplicationContext(),
                            getString(R.string.prompt_invalid),
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Spinner spinnerDivision = (Spinner) rootView.findViewById(
                            R.id.spinner_division_category_insert);
                    long divisionId = spinnerDivision.getSelectedItemId();
                    // Create content values object.
                    ContentValues values = new ContentValues();
                    values.put(Category.Columns.CATEGORY, category);
                    values.put(Category.Columns.DIVISION, String.valueOf(divisionId));
                    // Do insertion.
                    activity.getContentResolver().insert(Provider.ContentUri.CATEGORY, values);
                    // Clear fields.
                    editTextCategory.setText("");
                    // Prompt user of the action.
                    Toast.makeText(activity.getApplicationContext(),
                            getString(R.string.prompt_category_insert_complete),
                            Toast.LENGTH_SHORT).show();
                    // Reload the list view.
                    LoadCategories(rootView);
                } // End of if.
            } // End of onClick.
        }); // End of setOnClickListener.
    } // End of AddListeners.
} // End of class.
