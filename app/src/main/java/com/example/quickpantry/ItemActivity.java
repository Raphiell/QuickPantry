package com.example.quickpantry;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.FormatException;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
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

import io.realm.Realm;


public class ItemActivity extends AppCompatActivity {
    private TextView etName, etBrand, etAmount, etPurchased, etBestBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Get intent
        final Intent intent = getIntent();

        // Mode
        final boolean edit = intent.getStringExtra("mode").equals("edit");

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
        if(!edit)
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

        // Date format
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        // If in edit mode, grab the passed item and fill out the fields
        if(edit) {
            // Grab item
            Item item = DatabaseHelper.GetRealm().where(Item.class).equalTo("id", intent.getLongExtra("id", 1)).findFirst();

            // Setup fields
            etName.setText(item.getName());
            etBrand.setText(item.getBrand());
            etAmount.setText(item.getAmount());

            etPurchased.setText(format.format(item.getPurchased()));
            etBestBefore.setText(format.format(item.getBestBefore()));
        }

        btnSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // First validate if everything was entered
                if(validate()) {
                    // Grab all the fields' values
                    final String name, brand, amount;
                    final Date purchased, bestBefore;
                    final Category category;

                    name = etName.getText().toString();
                    brand = etBrand.getText().toString();
                    amount = etAmount.getText().toString();
                    purchased = new Date(etPurchased.getText().toString());
                    bestBefore = new Date(etBestBefore.getText().toString());
                    category = (Category) (categories[spinner.getSelectedItemPosition()]);

                    // If this is a new item, create it
                    if (!edit) {
                        // Save the item
                        DatabaseHelper.AddItem(name, amount, brand, purchased, bestBefore, "", category);
                    }
                    // Else edit
                    else {
                        // Perform a transaction where we grab the item, set it's properties, and save it
                        DatabaseHelper.GetRealm().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                // Grab the item
                                Item _item = realm.where(Item.class).equalTo("id", intent.getLongExtra("id", 1)).findFirst();

                                // Save settings
                                _item.setName(name);
                                _item.setAmount(amount);
                                _item.setBrand(brand);
                                _item.setPurchased(purchased);
                                _item.setBestBefore(bestBefore);
                            }
                        });
                    }

                    // Close
                    finish();
                }
                else
                {
                    // Else give dialog that they didn't enter everything!
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("You need to enter all fields").show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If cancel is clicked, just exit
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Are you sure?
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i)
                        {
                            // Yes
                            case DialogInterface.BUTTON_POSITIVE:
                                // Delete record
                                DatabaseHelper.GetRealm().executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.where(Item.class).equalTo("id", intent.getLongExtra("id", -1)).findFirst().deleteFromRealm();

                                    }
                                });
                                // Return
                                finish();
                                break;
                            // No
                            case DialogInterface.BUTTON_NEGATIVE:
                                // Do nothing
                                break;
                        }
                    }
                };
                builder.setMessage("Are you sure you want to delete this item?").setPositiveButton("Yes", listener).setNegativeButton("Cancel", listener).show();
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

    /**
     * Validates if all the fields have values entered
     * @return True if all fields have values, false otherwise
     */
    private boolean validate()
    {
        if(etName.getText().toString().isEmpty() || etAmount.getText().toString().isEmpty() || etBrand.getText().toString().isEmpty() || etBestBefore.getText().toString().isEmpty() || etPurchased.getText().toString().isEmpty())
        {
            // If any field is empty, return false
            return false;
        }
        // Otherwise return true
        return true;
    }
}
