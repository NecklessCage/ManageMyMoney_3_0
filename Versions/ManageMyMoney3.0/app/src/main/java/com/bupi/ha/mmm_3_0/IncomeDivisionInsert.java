package com.bupi.ha.mmm_3_0;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bupi.ha.mmm_3_0.db.DataContract.IncomeDivision;
import com.bupi.ha.mmm_3_0.db.Provider;
import com.bupi.ha.mmm_3_0.helpers.BackStackFragment;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

/**
 * Created by Htet Aung on 7/8/2016.
 * Hello
 */
public class IncomeDivisionInsert extends BackStackFragment {
    private ActivityMain activity;

    public IncomeDivisionInsert() {
        super.BACK_STACK_LEVEL = BackStackLevel.SECOND;
    } // End of constructor.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (ActivityMain) getActivity();
        activity.setTitle(R.string.app_title_income_division_insert);

        // Inflate the corresponding xml layout and retrieve View object.
        View rootView = inflater.inflate(R.layout.f_income_division_insert, container, false);
        AddListeners(rootView);

        // Bind data to list view.
        ((ListView) rootView.findViewById(R.id.list_view_income_divisions)).setAdapter(
                Helpers.LoadIncomeDivisions((ActivityMain) getActivity(), false)
        );

        return rootView;
    } // End of onCreateView.

    private void AddListeners(final View rootView) {
        //-------------------------------------------------------------Button Income Division Insert
        Button buttonIncomeDivisionInsert = (Button)
                rootView.findViewById(R.id.button_income_division_insert);
        buttonIncomeDivisionInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextNewIncomeDivision = (EditText) rootView.findViewById(
                        R.id.edit_text_new_income_division);
                String incomeDivision = editTextNewIncomeDivision.getText().toString();
                // Set up content values for insertion.
                ContentValues values = new ContentValues();
                values.put(IncomeDivision.Columns.INCOME_DIVISION, incomeDivision);
                // Insert.
                Uri uri = getActivity().getContentResolver().insert(
                        Provider.ContentUri.INCOME_DIVISION, values);
                //Log.i("HOLA!", uri.toString());
                // Clear field.
                editTextNewIncomeDivision.setText("");

                // Bind data to list view.
                ((ListView) rootView.findViewById(R.id.list_view_income_divisions)).setAdapter(
                        Helpers.LoadIncomeDivisions((ActivityMain) getActivity(), true)
                );

                Toast.makeText(
                        activity.getApplicationContext(),
                        getString(R.string.prompt_division_insert_complete),
                        Toast.LENGTH_SHORT)
                        .show();
            } // End of onClick.
        }); // End of setOnClickListener.
    } // End of AddListeners
} // End of class.
