package com.example.mycontacts;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mycontacts.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyContactsDatabase myContactsDatabase;
    private ArrayList<Contact> contactArrayList;
    private ContactAdapter contactAdapter;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        contactAdapter = new ContactAdapter();
        recyclerView.setAdapter(contactAdapter);

        myContactsDatabase = Room.databaseBuilder(getApplicationContext(), MyContactsDatabase.class,
                "ContactsDb").build();

        loadContacts();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void loadContacts() {
        new GetAllContactsAsyncTask().execute();
    }

    private void deleteContact(Contact contact) {
        new DeleteContactAsyncTask().execute(contact);
    }

    private void addContact(Contact contact) {
        new AddContactAsyncTask().execute(contact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private class GetAllContactsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            contactArrayList = (ArrayList<Contact>) myContactsDatabase.getContactDao().getAllContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            contactAdapter.setContactArrayList(contactArrayList);
        }
    }

    private class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void> {

        @Override
        protected Void doInBackground(Contact... contacts) {
            myContactsDatabase.getContactDao().deleteContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            loadContacts();
        }
    }

    private class AddContactAsyncTask extends AsyncTask<Contact, Void, Void> {

        @Override
        protected Void doInBackground(Contact... contacts) {
            myContactsDatabase.getContactDao().insertContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            loadContacts();
        }
    }

    private class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void> {

        @Override
        protected Void doInBackground(Contact... contacts) {
            myContactsDatabase.getContactDao().updateContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            loadContacts();
        }
    }
}