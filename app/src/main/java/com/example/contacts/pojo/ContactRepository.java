package com.example.contacts.pojo;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.contacts.data.ContactDAO;

import java.util.List;

// this class provides another abstraction layer between
// the different data sources and the rest of the app (room and viewModel)
public class ContactRepository {

    private ContactDAO contactDAO;
    private LiveData<List<Contact>> allContacts;


    // in viewModel we will pass an application
    // application is a subclass of context
    // so we can use it the context to create our database instance
    public ContactRepository(Application application) {

        ContactDatabase contactDatabase = ContactDatabase.getInstance(application);

        // normally we cannot call abstract methods but Room subclasses
        // the abstract notDAO() method in NoteDatabase.getInstance() method
        contactDAO = contactDatabase.contactDAO();

        allContacts = contactDAO.getAllContacts();
    }

    // Room does not allow database operations on the main thread
    // so we have to execute the code on the background thread
    // for insert, update, delete and deleteAll methods
    public void insert(Contact contact) {
        new InsertContactAsyncTask(contactDAO).execute(contact);
    }

    public void update(Contact contact) {
        new UpdateContactAsyncTask(contactDAO).execute(contact);
    }

    public void delete(Contact contact) {
        new DeleteContactAsyncTask(contactDAO).execute(contact);
    }

    public void deleteAllContacts() {
        new DeleteAllContactAsyncTask(contactDAO).execute();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    private static class InsertContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        private ContactDAO contactDAO;

        public InsertContactAsyncTask(ContactDAO contactDAO) {
            this.contactDAO = contactDAO;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDAO.insert(contacts[0]);
            return null;
        }
    }

    private static class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        private ContactDAO contactDAO;

        public UpdateContactAsyncTask(ContactDAO contactDAO) {
            this.contactDAO = contactDAO;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDAO.update(contacts[0]);
            return null;
        }
    }

    private static class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        private ContactDAO contactDAO;

        public DeleteContactAsyncTask(ContactDAO contactDAO) {
            this.contactDAO = contactDAO;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDAO.delete(contacts[0]);
            return null;
        }
    }

    private static class DeleteAllContactAsyncTask extends AsyncTask<Void, Void, Void> {
        private ContactDAO contactDAO;

        public DeleteAllContactAsyncTask(ContactDAO contactDAO) {
            this.contactDAO = contactDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDAO.deleteAllNotes();
            return null;
        }
    }
}
