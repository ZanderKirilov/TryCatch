package com.example.sethcohen.muchotrabajo.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DBManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "MYDBNewChance";
    private static final int DB_VERSION = 1;
    private static Context context;
//    private static ArrayList<Item> itemsAdd = new ArrayList<>();

    private static final String SQL_CREATE_USERS = "CREATE TABLE Users(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "email TEXT NOT NULL UNIQUE," +
            "password TEXT NOT NULL," +
            "name TEXT NOT NULL," +
            "image BLOB" +
            ");";

    private static final String SQL_CREATE_CATEGORY = "CREATE TABLE Category(" +
            "id INTEGER PRIMARY KEY," +
            "category TEXT NOT NULL UNIQUE" +
            ");";

    private static final String SQL_CREATE_ITEMS = "CREATE TABLE Items(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "title TEXT NOT NULL," +
            "description TEXT NOT NULL," +
            "location TEXT NOT NULL," +
            "salary REAL NOT NULL," +
            "company TEXT NOT NULL," +
            "category_id INTEGER NOT NULL," +
            "FOREIGN KEY(category_id) REFERENCES Category(id)" +
            ");";

    private static final String SQL_PUT_CATEGORY = "INSERT INTO Category('id', 'category') VALUES" +
            "(1, 'Advertising')," +
            "(2, 'Architecture')," +
            "(3, 'Art')," +
            "(4, 'Aviation')," +
            "(5, 'Banking')," +
            "(6, 'Design')," +
            "(7, 'Hospitality')," +
            "(8, 'Human resources')," +
            "(9, 'IT')," +
            "(10, 'Marketing')," +
            "(11, 'PR')," +
            "(12, 'Security')," +
            "(13, 'Sport')," +
            "(14, 'Transport and Logistic');";



    private static DBManager ourInstance;

    public static DBManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DBManager(context);
            DBManager.context = context;
        }
        return ourInstance;
    }

    //-----------------------------------------------------------
    private DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON");
        sqLiteDatabase.execSQL(SQL_CREATE_USERS);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY);
        sqLiteDatabase.execSQL(SQL_CREATE_ITEMS);
        sqLiteDatabase.execSQL(SQL_PUT_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public void addUser(User u) {
        if (userExists(u.getEmail())) {
            Toast.makeText(context, "E-mail " + u.getEmail() + " already exists!", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put("email", u.getEmail());
        values.put("password", u.getPassword());
        values.put("name", u.getName());
        long id = getWritableDatabase().insert("Users", null, values);
        u.setId((int) id);
    }

    public void updateUser(User user) {

        ContentValues values = new ContentValues();
        values.put("password", user.getPassword());
        values.put("name", user.getName());

        if (user.getUserImageBytes() != null) {
            byte[] data = user.getUserImageBytes();
            values.put("image", data);
        }

        getWritableDatabase().update("Users", values, "id=" + user.getId(), null);
    }

    public boolean validLogin(String email, String password) {
        boolean valid = false;
        Cursor cursor = ourInstance.getWritableDatabase().rawQuery("SELECT email, password FROM Users", null);
        while (cursor.moveToNext()) {
            String registeredEmail = cursor.getString(cursor.getColumnIndex("email"));
            String registeredPassword = cursor.getString(cursor.getColumnIndex("password"));
            if (registeredEmail.equals(email) && registeredPassword.equals(password)) {
                valid = true;
                break;
            }
        }

        cursor.close();

        return valid;
    }

    public boolean userExists(String newEmail) {

        boolean exists = false;
        Cursor cursor = ourInstance.getWritableDatabase().rawQuery("SELECT email FROM Users", null);

        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndex("email"));
            if (newEmail.equals(email)) {
                exists = true;
                break;
            }
        }

        cursor.close();
        return exists;
    }

    public User getUser(String enteringEmail, String enteringPassword) {
        Cursor cursor = ourInstance.getWritableDatabase().rawQuery("SELECT  id, email, password FROM Users", null);
        Integer id = null;

        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String password = cursor.getString(cursor.getColumnIndex("password"));

            if (enteringEmail.equals(email) && enteringPassword.equals(password)) {
                id = cursor.getInt(cursor.getColumnIndex("id"));
                break;
            }
        }

        cursor.close();
        return getUserByID(id);
    }

    public User getUserByID(Integer userID) {

        User user = null;
        Cursor cursor = ourInstance.getWritableDatabase().rawQuery("SELECT email, password, name, image FROM Users WHERE id=?", new String[]{userID + ""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        String email = cursor.getString(cursor.getColumnIndex("email"));
        String password = cursor.getString(cursor.getColumnIndex("password"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

        user = new User(name, email, password);
        user.setId(userID);
        user.setUserImageBytes(image);

        cursor.close();
        return user;
    }


    //*****************Add items to DB****************//

    public int addNewItem(Item item){

        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("description", item.getDescription());
        values.put("location", item.getLocation());
        values.put("company", item.getCompanyName());
        values.put("salary", item.getSalary());
        values.put("category_id", item.getCategoryId());

        long id = getWritableDatabase().insert("Items", null, values);
        item.setId((int) id);
        return (int) id;
    }

    public ArrayList<String> getCategories() {

        ArrayList<String> categories = new ArrayList<>();
        Cursor cursor = ourInstance.getWritableDatabase().rawQuery("SELECT category FROM Category", null);
        while (cursor.moveToNext()) {
            categories.add(cursor.getString((cursor.getColumnIndex("category"))));
        }

        cursor.close();
        return categories;
    }

    public int getCategoryID(String category) {

        Cursor cursor = ourInstance.getWritableDatabase().rawQuery("SELECT id FROM Category WHERE category =?", new String[]{category + ""});
        int id = -100;

        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return id;
    }

    public ArrayList<Item> getAllItems(int userID) {

        Cursor cursor = ourInstance.getWritableDatabase().rawQuery("SELECT * FROM Items", null);

        return getItemsByCursorQwery(cursor);
    }

    private ArrayList<Item> getItemsByCursorQwery(Cursor cursor) {

        GetItemById g = new GetItemById();

        return g.doInBackground(cursor);
    }


    public ArrayList<Item> getItemByCategory(String category) {

        Cursor cursor = ourInstance.getWritableDatabase().rawQuery("SELECT * FROM Items WHERE category_id = " + getCategoryID(category), null);

        return getItemsByCursorQwery(cursor);
    }

    private class GetItemById extends AsyncTask<Cursor, Void, ArrayList<Item>> {

        @Override
        protected ArrayList<Item> doInBackground(Cursor... cursors) {

            Cursor cursor = cursors[0];
            ArrayList<Item> items = new ArrayList<>();
            while (cursor.moveToNext()) {

                String title = cursor.getString(cursor.getColumnIndex("title"));
                double salary = cursor.getDouble(cursor.getColumnIndex("salary"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String company = cursor.getString(cursor.getColumnIndex("company"));
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                String location = cursor.getString(cursor.getColumnIndex("location"));

                Item item = new Item(title, description, company, salary, location, category_id);

                items.add(item);
            }

            cursor.close();

            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            super.onPostExecute(items);
        }
    }
}
