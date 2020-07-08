package com.example.contacts.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.contacts.pojo.Contact;
import com.example.contacts.pojo.ContactRepository;

import java.util.List;


// viewModel is only removed from the memory when the lifecycle of the corresponding activity is over
// it stores and process data for the UI and communicate with the model
// viewModel life configuration changes: when rotate the device,
// changes the text size, changes device language ...etc so data wont be lost
// AndroidViewModel is a subclass of viewModel the difference between the two is in
// AndroidViewModel we pass Application context in the constructor
public class ContactViewModel extends AndroidViewModel {

    private ContactRepository repository;
    private LiveData<List<Contact>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactRepository(application);
        allContacts = repository.getAllNotes();
    }

    public void insert(Contact contact) {
        repository.insert(contact);
    }

    public void update(Contact contact ) {
        repository.update(contact);
    }

    public void delete(Contact contact ) {
        repository.delete(contact);
    }

    public void deleteAllContacts() {
        repository.deleteAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

}