package com.example.contacts.pojo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.contacts.data.ContactDAO;

@Database(entities = Contact.class, version = 1)
public abstract class ContactDatabase extends RoomDatabase {

    //for singleton
    private static ContactDatabase instance;

    //this method is used to access dao
    //room will fill the body of the method automatically
    public abstract ContactDAO contactDAO();


    //synchronized means that only one thread at a time can access the method
    //so no more than an object can be created in a multi-thread app
    public static synchronized ContactDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ContactDatabase.class, "contact_database")
                    //when incrementing the version number of database on update
                    //you need to specify how the migration happens or the app crashes with IllegalStatException
                    //avoid this using this method, it will delete the database and its tables and create it from scratch
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

}