package com.example.myapplication.ui.tododetail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.contacts.ContactActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TodoDetailActivity extends Activity {

    private static final int REQUEST_SELECT_CONTACTS = 3;

    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_DUE_DATE = "dueDate";
    public static final String EXTRA_DUE_TIME = "dueTime";
    public static final String EXTRA_DONE = "done";
    public static final String EXTRA_FAVORITE = "favorite";
    public static final String EXTRA_DELETE = "delete";
    public static final String EXTRA_LINKED_CONTACTS = "linkedContacts";

    private EditText nameEditText;
    private EditText descriptionEditText;

    private TextView linkedContactsTextView;

    private Button contactsButton;
    private Button dateButton;
    private Button timeButton;
    private Button saveButton;
    private Button deleteButton;

    private CheckBox doneCheckBox;
    private CheckBox favoriteCheckBox;

    private int todoPosition = -1;
    private String selectedDate = "";
    private String selectedTime = "";

    private ArrayList<String> selectedContacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);

        findViews();
        readIntentData();
        updateLinkedContactsText();
        setButtonActions();
    }

    private void findViews() {
        nameEditText = findViewById(R.id.editTodoName);
        descriptionEditText = findViewById(R.id.editTodoDescription);
        linkedContactsTextView = findViewById(R.id.textLinkedContacts);

        dateButton = findViewById(R.id.buttonDate);
        timeButton = findViewById(R.id.buttonTime);
        saveButton = findViewById(R.id.buttonSave);
        deleteButton = findViewById(R.id.buttonDelete);
        contactsButton = findViewById(R.id.buttonContacts);

        doneCheckBox = findViewById(R.id.checkDone);
        favoriteCheckBox = findViewById(R.id.checkFavorite);
    }

    private void setButtonActions() {
        dateButton.setOnClickListener(v -> showDatePicker());
        timeButton.setOnClickListener(v -> showTimePicker());
        saveButton.setOnClickListener(v -> saveTodo());
        deleteButton.setOnClickListener(v -> deleteTodo());
        contactsButton.setOnClickListener(v -> openContactActivity());
    }

    private void openContactActivity() {
        Intent intent = new Intent(
                TodoDetailActivity.this,
                ContactActivity.class
        );

        intent.putExtra(
                ContactActivity.EXTRA_TODO_TITLE,
                nameEditText.getText().toString()
        );

        intent.putExtra(
                ContactActivity.EXTRA_TODO_DESCRIPTION,
                descriptionEditText.getText().toString()
        );

        startActivityForResult(intent, REQUEST_SELECT_CONTACTS);
    }

    private void updateLinkedContactsText() {
        if (selectedContacts.isEmpty()) {
            linkedContactsTextView.setText("No contacts selected");
            return;
        }

        StringBuilder builder = new StringBuilder();

        for (String contact : selectedContacts) {
            builder.append(contact).append("\n");
        }

        linkedContactsTextView.setText(builder.toString().trim());
    }

    private void readIntentData() {
        Intent intent = getIntent();

        todoPosition = intent.getIntExtra(EXTRA_POSITION, -1);

        String name = intent.getStringExtra(EXTRA_NAME);
        String description = intent.getStringExtra(EXTRA_DESCRIPTION);
        String dueDate = intent.getStringExtra(EXTRA_DUE_DATE);
        String dueTime = intent.getStringExtra(EXTRA_DUE_TIME);
        boolean done = intent.getBooleanExtra(EXTRA_DONE, false);
        boolean favorite = intent.getBooleanExtra(EXTRA_FAVORITE, false);

        if (name != null) {
            nameEditText.setText(name);
        }

        if (description != null) {
            descriptionEditText.setText(description);
        }

        if (dueDate != null && !dueDate.isEmpty()) {
            selectedDate = dueDate;
            dateButton.setText(dueDate);
        } else {
            dateButton.setText("Select date");
        }

        if (dueTime != null && !dueTime.isEmpty()) {
            selectedTime = dueTime;
            timeButton.setText(dueTime);
        } else {
            timeButton.setText("Select time");
        }

        doneCheckBox.setChecked(done);
        favoriteCheckBox.setChecked(favorite);

        ArrayList<String> contacts =
                intent.getStringArrayListExtra(EXTRA_LINKED_CONTACTS);

        if (contacts != null) {
            selectedContacts = contacts;
        }
    }

    private void saveTodo() {
        Intent resultIntent = new Intent();

        resultIntent.putExtra(EXTRA_POSITION, todoPosition);
        resultIntent.putExtra(EXTRA_NAME, nameEditText.getText().toString());
        resultIntent.putExtra(EXTRA_DESCRIPTION, descriptionEditText.getText().toString());
        resultIntent.putExtra(EXTRA_DUE_DATE, selectedDate);
        resultIntent.putExtra(EXTRA_DUE_TIME, selectedTime);
        resultIntent.putExtra(EXTRA_DONE, doneCheckBox.isChecked());
        resultIntent.putExtra(EXTRA_FAVORITE, favoriteCheckBox.isChecked());
        resultIntent.putStringArrayListExtra(EXTRA_LINKED_CONTACTS, selectedContacts);
        resultIntent.putExtra(EXTRA_DELETE, false);

        setResult(RESULT_OK, resultIntent);

        Toast.makeText(
                TodoDetailActivity.this,
                "Todo saved",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }

    private void deleteTodo() {
        Intent resultIntent = new Intent();

        resultIntent.putExtra(EXTRA_POSITION, todoPosition);
        resultIntent.putExtra(EXTRA_DELETE, true);

        setResult(RESULT_OK, resultIntent);

        Toast.makeText(
                TodoDetailActivity.this,
                "Todo deleted",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                TodoDetailActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = String.format(
                            Locale.US,
                            "%02d/%02d/%04d",
                            selectedMonth + 1,
                            selectedDay,
                            selectedYear
                    );

                    dateButton.setText(selectedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                TodoDetailActivity.this,
                (view, selectedHour, selectedMinute) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    selectedCalendar.set(Calendar.MINUTE, selectedMinute);

                    int hour12 = selectedCalendar.get(Calendar.HOUR);
                    if (hour12 == 0) {
                        hour12 = 12;
                    }

                    String amPm = selectedCalendar.get(Calendar.AM_PM) == Calendar.AM
                            ? "AM"
                            : "PM";

                    selectedTime = String.format(
                            Locale.US,
                            "%d:%02d %s",
                            hour12,
                            selectedMinute,
                            amPm
                    );

                    timeButton.setText(selectedTime);
                },
                hour,
                minute,
                false
        );

        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_CONTACTS && resultCode == RESULT_OK && data != null) {
            ArrayList<String> contacts =
                    data.getStringArrayListExtra(ContactActivity.EXTRA_SELECTED_CONTACTS);

            if (contacts != null) {
                selectedContacts = contacts;
                updateLinkedContactsText();
            }
        }
    }
}