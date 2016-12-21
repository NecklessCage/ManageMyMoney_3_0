package com.bupi.ha.mmm_3_0;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bupi.ha.mmm_3_0.db.DataContract.Division;
import com.bupi.ha.mmm_3_0.db.Provider;
import com.bupi.ha.mmm_3_0.helpers.BackStackFragment;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

/**
 * Created by Htet Aung on 7/5/2016.
 * Hello
 */
public class ExpenseDivisionInsert extends BackStackFragment {
    private ActivityMain activity;

    public ExpenseDivisionInsert() {
        super.BACK_STACK_LEVEL = BackStackLevel.FIRST;
    } // End of constructor.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (ActivityMain) getActivity();
        activity.setTitle(R.string.app_title_expense_division_insert);

        // Inflate the corresponding xml layout and retrieve View object.
        View rootView = inflater.inflate(R.layout.f_expense_division_insert, container, false);
        AddListeners(rootView);
        // Load divisions.
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_divisions);
        listView.setAdapter(Helpers.LoadExpenseDivisions(activity, false));
        return rootView;
    } // End of onCreateView.

    private void AddListeners(final View rootView) {
        //--------------------------------------------------------------------Button Division Insert
        Button buttonDivisionInsert = (Button) rootView.findViewById(R.id.button_division_insert);
        buttonDivisionInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextNewDivision = (EditText)
                        rootView.findViewById(R.id.edit_text_new_division);
                String division = editTextNewDivision.getText().toString();
                if (division.trim().isEmpty()) {
                    Toast.makeText(activity.getApplicationContext(),
                            getString(R.string.prompt_invalid),
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Set up content values for insertion.
                    ContentValues values = new ContentValues();
                    values.put(Division.Columns.DIVISION, division);
                    // Insert.
                    activity.getContentResolver().insert(Provider.ContentUri.DIVISION, values);
                    // Clear field.
                    editTextNewDivision.setText("");
                    // Prompt the user.
                    Toast.makeText(activity.getApplicationContext(),
                            getString(R.string.prompt_division_insert_complete),
                            Toast.LENGTH_SHORT)
                            .show();
                    // Refresh divisions.
                    ListView listView = (ListView) rootView.findViewById(R.id.list_view_divisions);
                    listView.setAdapter(Helpers.LoadExpenseDivisions(activity, true));
                } // End of if.
            } // End of onClick.
        }); // End of setOnClickListener.
    } // End of AddListeners.
} // End of class.
