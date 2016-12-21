package com.bupi.ha.mmm_3_0;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bupi.ha.mmm_3_0.helpers.BackStackFragment;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

/**
 * Created by user on 12/20/2016.
 * Hello
 */

public class FragmentStartPage extends BackStackFragment {

    private ActivityMain _activity;
    private View _view;
    private TextView _startPageTitle;
    private ListView _weeklyExpense;

    public FragmentStartPage() {
        super.BACK_STACK_LEVEL = BackStackLevel.FIRST;
    } // constructor.

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.f_start_page, container, false);
        _activity = (ActivityMain) getActivity();
        _startPageTitle = (TextView) _view.findViewById(R.id.text_view_start_page_title);
        _weeklyExpense = (ListView) _view.findViewById(R.id.list_view_weekly_snapshot);

        populateListView();

        return _view;
    } // onCreateView

    private void populateListView() {
        String selection = "";
        String sortOrder = "ExpenseDate";
        _weeklyExpense.setAdapter(Helpers.LoadExpenseReport(_activity, selection, sortOrder));
    } // populateListView

} // class
