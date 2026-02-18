package com.example.smartcampusservicerequestsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPhone;
    private RadioGroup rgGender;
    private CheckBox cbCoding, cbSports, cbMusic;
    private Button btnNext;
    private FloatingActionButton fabInfo;
    private View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        rgGender = findViewById(R.id.rgGender);
        cbCoding = findViewById(R.id.cbCoding);
        cbSports = findViewById(R.id.cbSports);
        cbMusic = findViewById(R.id.cbMusic);
        btnNext = findViewById(R.id.btnNext);
        fabInfo = findViewById(R.id.fabInfo);
        mainLayout = findViewById(R.id.main);

        // TextWatcher to enable/disable button based on input (Bonus Feature)
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etName.addTextChangedListener(watcher);
        etEmail.addTextChangedListener(watcher);
        etPhone.addTextChangedListener(watcher);

        btnNext.setOnClickListener(v -> {
            if (validateForm()) {
                navigateToServiceSelection();
            }
        });

        fabInfo.setOnClickListener(v -> 
            Toast.makeText(this, "Fill registration details to proceed", Toast.LENGTH_SHORT).show()
        );

        // Bonus Feature: Long press to reset form
        mainLayout.setOnLongClickListener(v -> {
            resetForm();
            Toast.makeText(MainActivity.this, "Form Reset", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void checkInputs() {
        boolean isNameFilled = !etName.getText().toString().trim().isEmpty();
        boolean isEmailFilled = !etEmail.getText().toString().trim().isEmpty();
        boolean isPhoneFilled = !etPhone.getText().toString().trim().isEmpty();
        
        btnNext.setEnabled(isNameFilled && isEmailFilled && isPhoneFilled);
    }

    private boolean validateForm() {
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void resetForm() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        rgGender.clearCheck();
        cbCoding.setChecked(false);
        cbSports.setChecked(false);
        cbMusic.setChecked(false);
        btnNext.setEnabled(false);
    }

    private void navigateToServiceSelection() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        RadioButton rbSelectedGender = findViewById(selectedGenderId);
        String gender = rbSelectedGender.getText().toString();

        ArrayList<String> interests = new ArrayList<>();
        if (cbCoding.isChecked()) interests.add("Coding");
        if (cbSports.isChecked()) interests.add("Sports");
        if (cbMusic.isChecked()) interests.add("Music");

        Intent intent = new Intent(this, ServiceSelectionActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        intent.putExtra("gender", gender);
        intent.putStringArrayListExtra("interests", interests);
        startActivity(intent);
    }
}
