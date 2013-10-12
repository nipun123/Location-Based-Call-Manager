package databasehandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import databasehandler.DataBaseHelper;

public class TestDataInsert {

	private DataBaseHelper helper;
	boolean result;

	public TestDataInsert(DataBaseHelper database) {
		helper = database;
	}

	public void checkDataInsert() {
		insertProfiles();
		insertContacts();
		deleteProfile();
		deleteContact();
		VolumeStateUpdate();
		updateVolume();
		updateAutoSendMessage();
		updateCallBlockState();
		getContactInfo();
		getAllAllowedContacts();
		getAllprofiles();
		selectNameByPosition();
	}

	public void insertProfiles() {
		result = helper.addProfile("jungle", 120.23, 36.89, "0717922436",
				"Call me later i am busy now", 4, true, true);
		
		if (result == true)
			System.out.println("Profile insert successful");
		else
			System.out.println("Profile insert fails");
	}

	public void insertContacts() {

		result = helper.addContact("0711512899", "Galle", "Udara");
		

		if (result == true)
			System.out.println("contact insert success");
		else
			System.out.println("contact insert fails");
	}

	public void deleteProfile() {
		helper.deleteProfile("jungle");
	}

	public void deleteContact() {
		helper.deleteContact("0711512899", "Galle");
	}

	public void VolumeStateUpdate() {
		result = helper.updateVolumeControlState("Library", false);

		if (result == true)
			System.out.println("volumestate change success");
		else
			System.out.println("volumestate change fails");
	}

	public void updateVolume() {
		result = helper.updateRingingVolume("Library", 3);

		if (result == true)
			System.out.println("volume update success");
		else
			System.out.println("volume update fails");
	}

	public void updateAutoSendMessage() {
		result = helper.updateAutoSendmessage("Galle",
				"I am busy now Call Me Later");

		if (result == true)
			System.out.println("AutoSend Message update Successful");
		else
			System.out.println("AutoSend Message update Successful");
	}

	public void updateCallBlockState() {
		result = helper.updateCallBlockState("Library", false);

		if (result == true)
			System.out.println("BlockState update successful");
		else
			System.out.println("BlockState update fails");

	}

	public void getContactInfo() {
		HashMap<String, String> wordList = helper.getContactInfo("0717922436",
				"Galle");

		System.out.println("Contact name is " + wordList.get("Name"));
	}

	public void getAllAllowedContacts() {
		ArrayList<HashMap<String, String>> list = helper
				.getAllAllowedContacts("Home");
		Iterator<HashMap<String, String>> iterator = list.iterator();
		HashMap<String, String> map;

		while (iterator.hasNext()) {
			map = iterator.next();
			System.out.println("contact number = " + map.get("Number")
					+ " name= " + map.get("Name"));
		}
	}

	public void getAllprofiles() {
		ArrayList<HashMap<String, String>> list = helper.getAllProfiles();
		Iterator<HashMap<String, String>> iterator = list.iterator();
		HashMap<String, String> map;

		while (iterator.hasNext()) {
			map = iterator.next();
			System.out.println("Profile name = " + map.get("Name")
					+ " Latitude = " + map.get("Latitude") + " Longitude = "
					+ map.get("Longitude"));
		}
	}

	public void selectNameByPosition() {
		System.out.println("Selected profile name "
				+ helper.getProfileNameFromcordinates(13.14, 15.56));
	}

}
