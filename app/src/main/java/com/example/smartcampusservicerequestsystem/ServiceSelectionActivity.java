package com.example.smartcampusservicerequestsystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ServiceSelectionActivity extends AppCompatActivity {

    private Spinner spinnerService;
    private LinearLayout layoutHostel, layoutWifi, layoutLab, layoutEvent;
    private Button btnDatePicker, btnTimePicker, btnSubmit;
    private TextInputEditText etHostelDesc, etWifiLocation, etParticipants;
    private SwitchMaterial switchUrgent;
    private RadioGroup rgEquipment;

    private String selectedDate = "";
    private String selectedTime = "";
    private String name, email, phone, gender;
    private List<String> interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);

        // Retrieve data from Intent
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        gender = getIntent().getStringExtra("gender");
        interests = getIntent().getStringArrayListExtra("interests");

        initViews();
        setupSpinner();
        setupPickers();

        btnSubmit.setOnClickListener(v -> {
            if (validateServiceInputs()) {
                showConfirmationDialog();
            }
        });
    }

    private void initViews() {
        spinnerService = findViewById(R.id.spinnerService);
        layoutHostel = findViewById(R.id.layoutHostel);
        layoutWifi = findViewById(R.id.layoutWifi);
        layoutLab = findViewById(R.id.layoutLab);
        layoutEvent = findViewById(R.id.layoutEvent);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnTimePicker = findViewById(R.id.btnTimePicker);
        btnSubmit = findViewById(R.id.btnSubmit);
        etHostelDesc = findViewById(R.id.etHostelDesc);
        etWifiLocation = findViewById(R.id.etWifiLocation);
        etParticipants = findViewById(R.id.etParticipants);
        switchUrgent = findViewById(R.id.switchUrgent);
        rgEquipment = findViewById(R.id.rgEquipment);
    }

    private void setupSpinner() {
        String[] services = {"Select Service", "Hostel Complaint", "WiFi Issue", "Lab Equipment Problem", "Event Registration"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, services);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService.setAdapter(adapter);

        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideAllLayouts();
                switch (position) {
                    case 1: layoutHostel.setVisibility(View.VISIBLE); break;
                    case 2: layoutWifi.setVisibility(View.VISIBLE); break;
                    case 3: layoutLab.setVisibility(View.VISIBLE); break;
                    case 4: layoutEvent.setVisibility(View.VISIBLE); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void hideAllLayouts() {
        layoutHostel.setVisibility(View.GONE);
        layoutWifi.setVisibility(View.GONE);
        layoutLab.setVisibility(View.GONE);
        layoutEvent.setVisibility(View.GONE);
    }

    private void setupPickers() {
        btnDatePicker.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                btnDatePicker.setText("Date: " + selectedDate);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnTimePicker.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedTime = hourOfDay + ":" + String.format("%02d", minute);
                btnTimePicker.setText("Time: " + selectedTime);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });
    }

    private boolean validateServiceInputs() {
        int position = spinnerService.getSelectedItemPosition();
        if (position == 0) {
            Toast.makeText(this, "Please select a service", Toast.LENGTH_SHORT).show();
            return false;
        }

        switch (position) {
            case 1:
                if (selectedDate.isEmpty()) {
                    Toast.makeText(this, "Select date", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (etHostelDesc.getText().toString().isEmpty()) {
                    etHostelDesc.setError("Required");
                    return false;
                }
                break;
            case 2:
                if (etWifiLocation.getText().toString().isEmpty()) {
                    etWifiLocation.setError("Required");
                    return false;
                }
                break;
            case 3:
                if (rgEquipment.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "Select equipment type", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case 4:
                if (selectedTime.isEmpty()) {
                    Toast.makeText(this, "Select time", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (etParticipants.getText().toString().isEmpty()) {
                    etParticipants.setError("Required");
                    return false;
                }
                break;
        }
        return true;
    }

    private void showConfirmationDialog() {
        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(name).append("\n");
        details.append("Email: ").append(email).append("\n");
        details.append("Gender: ").append(gender).append("\n");
        details.append("Interests: ").append(interests.toString()).append("\n\n");
        details.append("Service: ").append(spinnerService.getSelectedItem().toString()).append("\n");

        int position = spinnerService.getSelectedItemPosition();
        if (position == 1) {
            details.append("Date: ").append(selectedDate).append("\n");
            details.append("Description: ").append(etHostelDesc.getText().toString());
        } else if (position == 2) {
            details.append("Urgent: ").append(switchUrgent.isChecked() ? "Yes" : "No").append("\n");
            details.append("Location: ").append(etWifiLocation.getText().toString());
        } else if (position == 3) {
            RadioButton rb = findViewById(rgEquipment.getCheckedRadioButtonId());
            details.append("Equipment: ").append(rb.getText().toString());
        } else if (position == 4) {
            details.append("Time: ").append(selectedTime).append("\n");
            details.append("Participants: ").append(etParticipants.getText().toString());
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Request")
                .setMessage(details.toString())
                .setPositiveButton("Submit", (dialog, which) -> {
                    Toast.makeText(this, "Request Submitted Successfully!", Toast.LENGTH_LONG).show();
                    finishAffinity(); // Exit app or go back to main
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
