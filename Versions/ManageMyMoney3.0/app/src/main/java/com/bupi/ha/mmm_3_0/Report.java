package com.bupi.ha.mmm_3_0;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bupi.ha.mmm_3_0.helpers.BackStackFragment;
import com.bupi.ha.mmm_3_0.helpers.BackStackLevel;
import com.bupi.ha.mmm_3_0.helpers.Helpers;

/**
 * Created by Htet Aung on 7/7/2016.
 * Hello
 */
public class Report extends BackStackFragment {
    public Report() {
        super.BACK_STACK_LEVEL = BackStackLevel.FIRST;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.app_title_reports);

        // Inflate the corresponding xml layout and retrieve View object.
        View rootView = inflater.inflate(R.layout.f_report, container, false);
        AddListeners(rootView);
        return rootView;
    } // End of onCreateView.

    private void AddListeners(View rootView) {
        Button buttonShowExpenses = (Button) rootView.findViewById(R.id.button_show_expenses);
        buttonShowExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpenseReport frag = new ExpenseReport();
                new Helpers().replaceFragment(Report.this.BACK_STACK_LEVEL,
                        getActivity().getFragmentManager(), frag, "ExpenseReport");
            } // End of onClick.
        }); // End of setOnClickListener.

        Button buttonShowIncomes = (Button) rootView.findViewById(R.id.button_show_incomes);
        buttonShowIncomes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Helpers().replaceFragment(Report.this.BACK_STACK_LEVEL,
                        getActivity().getFragmentManager(),
                        new IncomeReport(), "IncomeReport");
            } // End of onClick.
        }); // End of setOnClickListener.

//
//        Button buttonBarChart = (Button) rootView.findViewById(R.id.button_bar_chart);
//        buttonBarChart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Helpers().replaceFragment(Report.this.BACK_STACK_LEVEL,
//                        getActivity().getFragmentManager(), new BarChartDisplay(),
//                        "BarChartDisplay");
//            } // End of onClick.
//        }); // End of setOnClickListener.
    } // End of AddListeners.
} // End of Report.
