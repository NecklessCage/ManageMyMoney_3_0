package com.bupi.ha.mmm_3_0.db;

import android.provider.BaseColumns;

/**
 * Created by Htet Aung on 6/25/2016.
 */
public class DataContract {

    public DataContract() {
    }

    /**
     * ************************************************************* Inner class for Division Table *
     */
    public static abstract class Division {
        public static final String TABLE_NAME = "tbl_division";

        // Columns
        public static abstract class Columns implements BaseColumns {
            public static final String DIVISION = "division";
        } // End of Division Columns.
    } // End of Division Class.

    /**
     * ************************************************************* Inner class for Category Table *
     */
    public static abstract class Category {
        public static final String TABLE_NAME = "tbl_category";

        // Columns
        public static abstract class Columns implements BaseColumns {
            public static final String
                    CATEGORY = "category",
                    DIVISION = "division";
        } // End of Category Columns.
    } // End of Category Class.

    /**
     * ************************************************************* Inner class for Expenses Table *
     */
    public static abstract class Expense {
        public static final String TABLE_NAME = "tbl_expense";

        // Columns
        public static abstract class Columns implements BaseColumns {
            public static final String
                    DATE = "date",
                    DIVISION = "division",
                    CATEGORY = "category",
                    NOTE = "note",
                    AMOUNT = "amount";
        } // End of Expense Columns.
    } // End of Expense Class.

    //-------------------------------------------------------------------------------Income Division
    public static abstract class IncomeDivision {
        public static final String TABLE_NAME = "tbl_income_division";

        // Columns
        public static abstract class Columns implements BaseColumns {
            public static final String INCOME_DIVISION = "income_division";
        } // End of IncomeDivision Columns.
    } // End of IncomeDivision Class.

    //-------------------------------------------------------------------------------Income Category
    public static abstract class IncomeCategory {
        public static final String TABLE_NAME = "tbl_income_category";

        // Columns
        public static abstract class Columns implements BaseColumns {
            public static final String
                    INCOME_CATEGORY = "income_category",
                    INCOME_DIVISION = "income_division";
        } // End of IncomeCategory Columns.
    } // End of IncomeCategory Class.

    //----------------------------------------------------------------------------------------Income
    public static abstract class Income {
        public static final String TABLE_NAME = "tbl_income";

        // Columns
        public static abstract class Columns implements BaseColumns {
            public static final String
                    INCOME_DATE = "income_date",
                    INCOME_DIVISION = "income_division",
                    INCOME_CATEGORY = "income_category",
                    INCOME_AMOUNT = "income_amount",
                    INCOME_NOTE = "income_note";
        } // End of Income Columns.
    } // End of Income Class.

    //-----------------------------------------------------------------------------Budget Allocation
    public static abstract class BudgetAllocation {
        public static final String TABLE_NAME = "tbl_budget_allocation";

        // Columns
        public static abstract class Columns implements BaseColumns {
            public static final String
                    EXPENSE_CATEGORY = "expense_category",
                    ALLOCATION = "allocation";
        } // End of Income Columns.
    } // End of Income Class.
} // End of DataContract.