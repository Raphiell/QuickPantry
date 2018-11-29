package com.example.quickpantry;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.example.quickpantry.Database.Category;
import com.example.quickpantry.Database.DatabaseHelper;
import com.example.quickpantry.Database.Item;


public class ItemActivity extends AppCompatActivity {
    private TextView etName, etBrand, etAmount, etPurchased, etBestBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Get intent
        Intent intent = getIntent();

        // Grab all categories
        final Object[] categories = DatabaseHelper.GetRealm().where(Category.class).findAll().toArray();

        // Convert to a list of strings
        String[] categoryNames = new String[categories.length];

        for(int i = 0; i < categoryNames.length; i++)
        {
            categoryNames[i] = ((Category)categories[i]).getName();
        }

        // Create array adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setup spinner contents
        final Spinner spinner = findViewById(R.id.spnCategory);
        spinner.setAdapter(adapter);

        // Grab and set Save button
        Button btnSaveItem = findViewById(R.id.btnSaveItem);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnDelete = findViewById(R.id.btnDelete);

        // If this is a new item, no delete is needed
        if(intent.getStringExtra("mode").equals("new"))
        {
            btnDelete.setEnabled(false);
        }

        // Grab values from fields
        etName = findViewById(R.id.etName);
        etBrand = findViewById(R.id.etBrand);
        etAmount = findViewById(R.id.etAmount);
        etPurchased = findViewById(R.id.etPurchased);
        etBestBefore = findViewById(R.id.etBestBefore);

        // Setup properties of edit texts to show the date picker
        etPurchased.setFocusable(false);
        etPurchased.setClickable(true);
        etPurchased.setLongClickable(false);
        etBestBefore.setFocusable(false);
        etBestBefore.setClickable(true);
        etBestBefore.setLongClickable(false);

        // Setup the default date of the purchased field to today
        etPurchased.setText(String.format("%d/%d/%d", Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));

        // Setup on click listeners
        etPurchased.setOnClickListener(datePickerClickListener((EditText)etPurchased));
        etBestBefore.setOnClickListener(datePickerClickListener((EditText)etBestBefore));

        // Potential item to edit
        Item item;

        // Date format
        DateFormat format = new SimpleDateFormat("yyyy/mm/dd");

        // If in edit mode, grab the passed item and fill out the fields
        if(intent.getStringExtra("mode").equals("edit"))
        {
            item = DatabaseHelper.GetRealm().where(Item.class).equalTo("id", String.valueOf(intent.getIntExtra("item", 1))).findFirst();

            etName.setText(item.getName());
            etBrand.setText(item.getBrand());
            etAmount.setText(item.getAmount());

            try {
                etPurchased.setText(format.parse(item.getPurchased().toString()).toString());
                etBestBefore.setText(format.parse(item.getBestBefore().toString()).toString());
            }
            catch(ParseException e)
            {
                Toast.makeText(getApplicationContext(), "Error reading date", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        btnSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Grab all the fields' values
                String name, brand, amount;
                Date purchased, bestBefore;
                Category category;

                name = etName.getText().toString();
                brand = etBrand.getText().toString();
                amount = etAmount.getText().toString();

                purchased = new Date(etPurchased.getText().toString());
                bestBefore = new Date(etBestBefore.getText().toString());

                category = (Category)(categories[spinner.getSelectedItemPosition()]);

                // Save the item and return to the previous view
                DatabaseHelper.AddItem(name, amount, brand, purchased, bestBefore, "", category);

                // Close
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If cancel is clicked, just exit
                finish();
            }
        });
    }

    /**
     * Return a on click listener to display a date picker, then change the value of an edit text
     * @param field Edit text to pass the value to
     * @return      Returns the on click listener
     */
    private View.OnClickListener datePickerClickListener(final EditText field)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                field.setText(String.format("%d/%d/%d", year, monthOfYear + 1, dayOfMonth));
                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDateRange(new MonthAdapter.CalendarDay(1950, 0, 1), null)
                        .setDoneText("Done")
                        .setCancelText("Cancel")
                        .setThemeDark();
                // If the field was empty to start, just get today's date
                if(field.getText().toString().isEmpty())
                {
                    cdp.setPreselectedDate(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                }
                // Otherwise, show the date that was there before
                else
                {
                    String[] date = field.getText().toString().split("/");
                    Toast.makeText(getApplicationContext(), field.getText().toString(), Toast.LENGTH_LONG).show();
                    cdp.setPreselectedDate(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));
                }
                cdp.show(getSupportFragmentManager(),"Something");
            }
        };
    }
}
