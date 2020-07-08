package com.example.contacts.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.contacts.R;
import com.example.contacts.pojo.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ContactViewModel ContactViewModel;
    public static final int ADD_Contact_REQUEST = 1;
    public static final int EDIT_Contact_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.add_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
                startActivityForResult(intent, ADD_Contact_REQUEST);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ContactAdapter ContactAdapter = new ContactAdapter();
        recyclerView.setAdapter(ContactAdapter);

        // we do not initialize ContactViewModel using new keyword or whenever a new activity is created
        // a new viewModel will be created, instead we ask the android system to create a viewModel
        // because the system knows when to create a new one or provide an exciting viewModel
        ContactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        ContactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> Contacts) {
                ContactAdapter.setList(Contacts);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Delete Contact");
                builder.setMessage("Are you sure you want to delete the contact");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContactViewModel.delete(ContactAdapter.getContactAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(MainActivity.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Contact contact = ContactAdapter.getContactAt(viewHolder.getAdapterPosition());
                        ContactViewModel.delete(ContactAdapter.getContactAt(viewHolder.getAdapterPosition()));
                        ContactViewModel.insert(contact);
                    }
                });

                builder.show();
            }
        }).attachToRecyclerView(recyclerView);

        ContactAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact Contact) {
                Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
                intent.putExtra(AddEditContactActivity.EXTRA_LAST_NAME, Contact.getLastName());
                intent.putExtra(AddEditContactActivity.EXTRA_FIRST_NAME, Contact.getFirstName());
                intent.putExtra(AddEditContactActivity.EXTRA_ID, Contact.getId());
                startActivityForResult(intent, EDIT_Contact_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_Contact_REQUEST && resultCode == RESULT_OK) {
            String firstName = data.getStringExtra(AddEditContactActivity.EXTRA_FIRST_NAME);
            String lastName = data.getStringExtra(AddEditContactActivity.EXTRA_LAST_NAME);

            Contact Contact = new Contact(firstName, lastName);
            ContactViewModel.insert(Contact);

            Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_Contact_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditContactActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Contact can not be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditContactActivity.EXTRA_FIRST_NAME);
            String description = data.getStringExtra(AddEditContactActivity.EXTRA_LAST_NAME);

            Contact Contact = new Contact(title, description);
            Contact.setId(id);

            ContactViewModel.update(Contact);
            Toast.makeText(this, "Contact Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Contact not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_contacts:
                showDialog("Delete All Contacts", "Are you sure you want to delete all contacts?");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDialog(String title, CharSequence message ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    ContactViewModel.deleteAllContacts();
                    Toast.makeText(MainActivity.this, "All Contacts Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}