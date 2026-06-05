package com.example.myapplication.ui.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ContactActivity extends Activity {

    public static final String EXTRA_SELECTED_CONTACTS = "selectedContacts";
    public static final String EXTRA_TODO_TITLE = "todoTitle";
    public static final String EXTRA_TODO_DESCRIPTION = "todoDescription";

    private ListView contactListView;
    private Button confirmContactsButton;

    private ArrayList<String> contacts;

    private String todoTitle = "";
    private String todoDescription = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        contactListView = findViewById(R.id.contactListView);
        confirmContactsButton = findViewById(R.id.buttonConfirmContacts);

        readTodoData();
        createDummyContacts();
        showContactList();

        contactListView.setOnItemClickListener((parent, view, position, id) -> {
            if (contactListView.isItemChecked(position)) {
                showContactOptions(contacts.get(position));
            }
        });

        confirmContactsButton.setOnClickListener(v -> confirmSelection());
    }

    private void readTodoData() {
        todoTitle = getIntent().getStringExtra(EXTRA_TODO_TITLE);
        todoDescription = getIntent().getStringExtra(EXTRA_TODO_DESCRIPTION);

        if (todoTitle == null) {
            todoTitle = "";
        }

        if (todoDescription == null) {
            todoDescription = "";
        }
    }

    private void createDummyContacts() {
        contacts = new ArrayList<>();
        contacts.add("Max Mustermann");
        contacts.add("Anna Schmidt");
        contacts.add("Peter Wagner");
        contacts.add("Lisa Müller");
    }

    private void showContactList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                contacts
        );

        contactListView.setAdapter(adapter);
    }

    private void showContactOptions(String contactName) {
        String[] options = {"Send Email", "Send SMS", "Call"};

        new AlertDialog.Builder(this)
                .setTitle(contactName)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        sendEmail(contactName);
                    } else if (which == 1) {
                        sendSms(contactName);
                    } else {
                        callContact(contactName);
                    }
                })
                .show();
    }

    private void sendEmail(String contactName) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + getEmailForContact(contactName)));
        intent.putExtra(Intent.EXTRA_SUBJECT, todoTitle);
        intent.putExtra(Intent.EXTRA_TEXT, todoDescription);

        startActivity(intent);
    }

    private void sendSms(String contactName) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + getPhoneForContact(contactName)));
        intent.putExtra("sms_body", todoTitle + "\n\n" + todoDescription);

        startActivity(intent);
    }

    private void callContact(String contactName) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getPhoneForContact(contactName)));

        startActivity(intent);
    }

    private String getEmailForContact(String contactName) {
        if (contactName.equals("Max Mustermann")) {
            return "max@example.com";
        }

        if (contactName.equals("Anna Schmidt")) {
            return "anna@example.com";
        }

        if (contactName.equals("Peter Wagner")) {
            return "peter@example.com";
        }

        return "lisa@example.com";
    }

    private String getPhoneForContact(String contactName) {
        if (contactName.equals("Max Mustermann")) {
            return "491701111111";
        }

        if (contactName.equals("Anna Schmidt")) {
            return "491702222222";
        }

        if (contactName.equals("Peter Wagner")) {
            return "491703333333";
        }

        return "491704444444";
    }

    private void confirmSelection() {
        ArrayList<String> selectedContacts = new ArrayList<>();

        for (int i = 0; i < contacts.size(); i++) {
            if (contactListView.isItemChecked(i)) {
                selectedContacts.add(contacts.get(i));
            }
        }

        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra(EXTRA_SELECTED_CONTACTS, selectedContacts);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}