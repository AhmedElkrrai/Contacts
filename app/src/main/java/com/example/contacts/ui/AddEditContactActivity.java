package com.example.contacts.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contacts.R;


public class AddEditContactActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "package com.example.room.EXTRA_ID";
    public static final String EXTRA_FIRST_NAME =
            "package com.example.room.EXTRA_FIRST_NAME";
    public static final String EXTRA_LAST_NAME =
            "package com.example.room.EXTRA_LAST_NAME";
    public static final String EXTRA_PRIORITY =
            "package com.example.room.EXTRA_priority";

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        editTextFirstName = findViewById(R.id.add_first_name_tv);
        editTextLastName = findViewById(R.id.add_last_name_tv);
        numberPicker = findViewById(R.id.number_picker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Contact");
            editTextLastName.setText(intent.getStringExtra(EXTRA_LAST_NAME));
            editTextFirstName.setText(intent.getStringExtra(EXTRA_FIRST_NAME));
            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else setTitle("Add Contact");
    }

    private void saveContact() {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        int priority = numberPicker.getValue();

        if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter a Contact", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_FIRST_NAME, firstName);
        data.putExtra(EXTRA_LAST_NAME, lastName);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1)
            data.putExtra(EXTRA_ID, id);

        setResult(RESULT_OK, data);
        //finish sending the extra data to the ManiActivity and open it
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
