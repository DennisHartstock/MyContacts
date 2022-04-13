package com.example.mycontacts;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert
    void insertContact(Contact contact);

    @Query("select * from contacts_table")
    List<Contact> getAllContacts();

    @Query("select * from contacts_table where contactId ==:contactId")
    Contact getContact(long contactId);

    @Update
    void updateContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);
}
