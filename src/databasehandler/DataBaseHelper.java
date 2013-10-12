package databasehandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	public static final String LOGCAT = null;
	public static DataBaseHelper helper;

	public static final String dbName = "callmanager";            // database name

	public static final String profileTable = "Profiles";           // location profiles attributes
	public static final String profileName = "Name";
	public static final String latitude = "Latitude";
	public static final String longitude = "Longitude";
	public static final String blockingState = "BlockState";
	public static final String ringing_Volume = "Volume";
	public static final String volume_Control_State = "VolumeState";
	public static final String forwarding_number = "ForwardingNumber";
	public static final String auto_message = "AutoMessage";

	public static final String contactTable = "Contacts";           // contact details
	public static final String contactName = "Name";
	public static final String contactNumber = "Number";
	public static final String profile_allowed = "Profile";

	public DataBaseHelper(Context applicationContext) {
		super(applicationContext, dbName + ".db", null, 1);
		this.getWritableDatabase();                                 // at the creation get a database instance
		Log.v(LOGCAT, "created");
	}

	public static DataBaseHelper getInstance(Context context) {     // get the
																    // DataBaseHandler
																    // instance
		if (helper == null) {
			helper = new DataBaseHelper(context);
		}
		return helper;
	}

	private SQLiteDatabase open() throws SQLException {             // get the database
														            // instance
		return this.getWritableDatabase();

	}

	@Override
	public void onCreate(SQLiteDatabase database) {                              // create two tables for
													                             // profiles and contacts
		String query;
		query = "CREATE TABLE Profiles (Name TEXT PRIMARY KEY, Latitude "
				+ "REAL,Longitude REAL,BlockState BOOLEAN,Volume INTEGER, VolumeState BOOLEAN,"
				+ "ForwardingNumber TEXT,AutoMessage TEXT)";
		database.execSQL(query);
		query = "CREATE TABLE Contacts(Number TEXT,Profile TEXT,Name TEXT,"
				+ "PRIMARY KEY(Number,Profile))";
		database.execSQL(query);
		Log.v(LOGCAT, "profiles and Contacts created");

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int arg1, int arg2) {         // call   before oncreate method if profile and contacts tables exists avoid creating tables
																																				
		String query1, query2;
		query1 = "DROP TABLE IF EXIST Profiles";
		query2 = "DROP TABLE IF EXIST Contacts";
		database.execSQL(query1);
		database.execSQL(query2);
		Log.v("call comes here", "");
		onCreate(database);

	}

	public boolean addProfile(String name,
			double latitudeValue, // add a new profile to database
			double longitudeValue, String forwardingNumber, String autoMessage,
			int volumelevel, boolean blockingstate, boolean volumeChaneState) {

		SQLiteDatabase database = open();
		ContentValues values = new ContentValues();
		long result;
		values.put(profileName, name);
		values.put(latitude, latitudeValue);
		values.put(longitude, longitudeValue);
		values.put(forwarding_number, forwardingNumber);
		values.put(ringing_Volume, volumelevel);
		values.put(auto_message, autoMessage);
		values.put(blockingState, blockingstate);
		values.put(volume_Control_State, volumeChaneState);

		result = database.insert(profileTable, null, values);
		database.close();

		if (result == -1) {
			return false;
		} else
			return true;
	}

	public boolean addContact(String number, String profile_name, String name) {             // add a new contact
																					
		SQLiteDatabase database = open();
		ContentValues values = new ContentValues();
		long result;
		values.put(contactNumber, number);
		values.put(profile_allowed, profile_name);
		values.put(contactName, name);
		result = database.insert(contactTable, null, values);

		database.close();

		if (result == -1) {
			return false;
		} else
			return true;
	}

	public ArrayList<HashMap<String, String>> getAllProfiles() {                            // get details of allprofiles
		ArrayList<HashMap<String, String>> wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT * FROM Profiles";
		SQLiteDatabase database = open();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(profileName, cursor.getString(0));
				map.put(latitude, cursor.getString(1));
				map.put(longitude, cursor.getString(2));
				map.put(blockingState, cursor.getString(3));
				map.put(ringing_Volume, cursor.getString(4));
				map.put(volume_Control_State, cursor.getString(5));
				map.put(forwarding_number, cursor.getString(6));
				map.put(auto_message, cursor.getString(7));
				wordList.add(map);
			} while (cursor.moveToNext());

		}
		database.close();
		return wordList;
	}

	public ArrayList<HashMap<String, String>> getAllAllowedContacts(                // get details of all contacts in a profile white box
			String profileName) {
		
		ArrayList<HashMap<String, String>> wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT * FROM Contacts WHERE Profile='"
				+ profileName + "' ";
		SQLiteDatabase database = open();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(contactNumber, cursor.getString(0));
				map.put(contactName, cursor.getString(2));
				wordList.add(map);
			} while (cursor.moveToNext());

		}
		database.close();
		return wordList;
	}

	public HashMap<String, String> getProfileInfo(String profileName) {               // get all details of a profile
		HashMap<String, String> wordList = new HashMap<String, String>();
		SQLiteDatabase database = open();
		
		String selectQuery = "SELECT * FROM Profiles WHERE Name='"
				+ profileName + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		
		if (cursor.moveToFirst()) {
			wordList.put(latitude, cursor.getString(1));
			wordList.put(longitude, cursor.getString(2));
			wordList.put(blockingState, cursor.getString(3));
			wordList.put(ringing_Volume, cursor.getString(4));
			wordList.put(volume_Control_State, cursor.getString(5));
			wordList.put(forwarding_number, cursor.getString(6));
			wordList.put(auto_message, cursor.getString(7));
		}
		database.close();
		return wordList;
	}

	public HashMap<String, String> getContactInfo(String Number, String profile) {             // get details of a given contact number
		
		HashMap<String, String> wordList = new HashMap<String, String>();
		SQLiteDatabase database = open();
		String selectQuery = "SELECT * FROM Contacts WHERE Number='" + Number
				+ "' AND Profile ='" + profile + "' ";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			wordList.put(contactNumber, cursor.getString(0));
			wordList.put(contactName, cursor.getString(2));
		}
		database.close();
		return wordList;
	}

	public boolean updateProfileName(String previousName, String newName) {                  // change profile name when location given
		SQLiteDatabase database = open();
		ContentValues values = new ContentValues();
		String[] select = { previousName };
		values.put(profileName, newName);
		int result = database.update(profileTable, values, "Name" + "=?",
				select);
		database.close();

		if (result == -1) {
			return false;
		} else
			return true;

	}

	public boolean updateProfilePosition(String profile, double lat, double lon) {            // change position of a given location name
		SQLiteDatabase database = open();
		ContentValues values = new ContentValues();
		String[] select = { profile };
		values.put(latitude, lat);
		values.put(latitude, lon);
		int result = database.update(profileTable, values, "Name" + "=?",
				select);
		database.close();

		if (result == -1) {
			return false;
		} else
			return true;

	}

	public boolean updateRingingVolume(String profile, int Volume) {                         // update ringing volume of a profile
		SQLiteDatabase database = open();
		ContentValues values = new ContentValues();
		String[] select = { profile };
		values.put(ringing_Volume, Volume);
		int result = database.update(profileTable, values, "Name" + "=?",
				select);
		database.close();

		if (result == -1) {
			return false;
		} else
			return true;

	}

	public boolean updateAutoSendmessage(String profile, String Message) {                  // change send message of a profile
		SQLiteDatabase database = open();
		ContentValues values = new ContentValues();
		String[] select = { profile };
		values.put(auto_message, Message);
		int result = database.update(profileTable, values, "Name" + "=?",
				select);
		database.close();

		if (result == -1) {
			return false;
		} else
			return true;

	}

	public boolean updateCallBlockState(String profile, boolean state) {                   // change callblocking state of a profile
		SQLiteDatabase database = open();
		ContentValues values = new ContentValues();
		String[] select = { profile };
		values.put(blockingState, state);
		int result = database.update(profileTable, values, "Name" + "=?",
				select);
		database.close();

		if (result == -1) {
			return false;
		} else
			return true;

	}

	public boolean updateVolumeControlState(String profile, boolean state) {                // update volume control state of a profile
		SQLiteDatabase database = open();
		ContentValues values = new ContentValues();
		String[] select = { profile };
		values.put(volume_Control_State, state);
		int result = database.update(profileTable, values, "Name" + "=?",
				select);
		database.close();

		if (result == -1) {
			return false;
		} else
			return true;

	}

	public void deleteContact(String Number, String Profile) {                               // delete acontact
		SQLiteDatabase database = open();
		String deleteQuery = "DELETE FROM Contacts WHERE Number='" + Number
				+ "' AND Profile='" + Profile + "'";
		database.execSQL(deleteQuery);
		System.out.println("Contact Deletion successful");
		database.close();
	}

	public void deleteProfile(String profile) {
		SQLiteDatabase database = open();
		String deleteQuery = "DELETE FROM Profiles WHERE Name='" + profile
				+ "'";
		database.execSQL(deleteQuery);
		System.out.println("profile deletion successful");
		database.close();
	}

	public String getProfileNameFromcordinates(double latitude, double longitude) {          // get the most likely location for a given cordinates
		double upLatlimit, downLatLimit, leftLonLimit, rightLonLimit, lat, lon;
		double distance;
		String profileName, selectedName = "";
		upLatlimit = latitude + 1;
		downLatLimit = latitude - 1;
		leftLonLimit = longitude - 1;
		rightLonLimit = longitude + 1;

		SQLiteDatabase database = open();
		System.out.println("start to find name from latitude= " + latitude
				+ " longitude= " + longitude);

		String selectQuery = "SELECT Name,Latitude,Longitude FROM Profiles WHERE "
				+ "Latitude <="
				+ upLatlimit
				+ " AND Latitude >="
				+ downLatLimit
				+ " AND"
				+ " Longitude >= "
				+ leftLonLimit
				+ " AND Longitude <= " + rightLonLimit;

		Cursor cursor = database.rawQuery(selectQuery, null);

		distance = 10;
		if (cursor.moveToFirst()) {
			do {
				profileName = cursor.getString(0);
				lat = Double.parseDouble(cursor.getString(1));
				lon = Double.parseDouble(cursor.getString(2));
				System.out.println(profileName + " " + lat + " " + lon);

				double latgap = latitude - lat;
				double longap = longitude - lon;
				double gap = Math.sqrt((latgap * latgap) + (longap * longap));

				if (gap < distance) {
					distance = gap;
					selectedName = profileName;
				}

			} while (cursor.moveToNext());

		}
		database.close();
		Log.v("", "selected location name= " + selectedName);
		return selectedName;

	}

	public boolean checkContactAllowed(String number, String profileName) {                    // check a contact is allowed in a given locatiion
		ArrayList<HashMap<String, String>> contacts = getAllAllowedContacts(profileName);
		Iterator<HashMap<String, String>> iterator = contacts.iterator();
		HashMap<String, String> map = null;
		String temp;
		boolean contain = false;

		while (iterator.hasNext()) {
			map = iterator.next();
			temp = map.get(contactNumber);
			if (temp.equals(number)) {
				contain = true;
			}
		}

		return contain;
	}

	public void deleteTables() {
		SQLiteDatabase database = open();
		database.delete("Profiles", "1", new String[] {});
		Log.d("Database stuff", "Database table succesfully deleted");
		database.close();

	}

}
