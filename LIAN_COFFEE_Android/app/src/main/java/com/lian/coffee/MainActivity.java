package com.lian.coffee;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DB db;
    private EditText etTitle, etAmount;
    private Button btnIncome, btnExpense;
    private TextView tvIncomeTotal, tvExpenseTotal, tvProfitTotal, tvLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);

        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        btnIncome = findViewById(R.id.btnIncome);
        btnExpense = findViewById(R.id.btnExpense);
        tvIncomeTotal = findViewById(R.id.tvIncomeTotal);
        tvExpenseTotal = findViewById(R.id.tvExpenseTotal);
        tvProfitTotal = findViewById(R.id.tvProfitTotal);
        tvLogs = findViewById(R.id.tvLogs);

        btnIncome.setOnClickListener(v -> saveTransaction("INCOME"));
        btnExpense.setOnClickListener(v -> saveTransaction("EXPENSE"));

        updateUI();
    }

    private void saveTransaction(String type) {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();

        if (title.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "لطفاً عنوان و مبلغ را وارد کنید", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        if (db.addTransaction(title, amount, type)) {
            Toast.makeText(this, "تراکنش با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
            etTitle.setText("");
            etAmount.setText("");
            updateUI();
        } else {
            Toast.makeText(this, "خطا در ثبت تراکنش", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        double income = db.getTotalIncome();
        double expense = db.getTotalExpense();
        double profit = income - expense;

        tvIncomeTotal.setText("مجموع درآمد: " + String.format("%,.0f", income) + " تومان");
        tvExpenseTotal.setText("مجموع هزینه: " + String.format("%,.0f", expense) + " تومان");
        tvProfitTotal.setText("سود خالص: " + String.format("%,.0f", profit) + " تومان");

        Cursor cursor = db.getAllTransactions();
        StringBuilder logs = new StringBuilder();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_TITLE));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DB.COLUMN_AMOUNT));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_TYPE));
            String typeFa = type.equals("INCOME") ? "[درآمد]" : "[هزینه]";
            logs.append(typeFa).append(" ").append(title).append(" : ").append(String.format("%,.0f", amount)).append(" تومان
");
        }
        cursor.close();
        tvLogs.setText(logs.toString());
    }
}
