package databasehandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import databasehandler.DataBaseHelper;

public class TestDataInsert {

	private DataBaseHelper helper;
	Context context;
	boolean result;

	public TestDataInsert(Context context) {
		this.context = context;
		helper = new DataBaseHelper(context);
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
		result = helper.addProfile("jungle", 123, 150);

		if (result == true)
			System.out.println("Profile insert successful");
		else
			System.out.println("Profile insert fails");
	}

	public void insertContacts() {

		result = helper.addContact("0712788499", "Home", "Dasika");

		if (result == true)
			System.out.println("contact insert success");
		else
			System.out.println("contact insert fails");
	}

	public void deleteProfile() {
		helper.deleteProfile("jungle");
	}

	public void deleteContact() {
		helper.deleteContact("0712788499", "Home");
	}

	public void VolumeStateUpdate() {
		result = helper.updateVolumeControlState("Library", false);

		if (result == true)
			System.out.println("volumestate change success");
		else
			System.out.println("volumestate change fails");
	}
	
	public void updateVolume()
	{
		result=helper.updateRingingVolume("Library",100);
		
		if (result == true)
			System.out.println("volume update success");
		else
			System.out.println("volume update fails");
	}
	
	public void updateAutoSendMessage()
	{
		result=helper.updateAutoSendmessage("Library","Call Me Later");
		
		if (result == true)
			System.out.println("AutoSend Message update Successful");
		else
			System.out.println("AutoSend Message update Successful");
	}
	
	public void updateCallBlockState()
	{
		result=helper.updateCallBlockState("Library",false);
		
		if (result == true)
			System.out.println("BlockState update successful");
		else
			System.out.println("BlockState update fails");
		
	}
	
	public void getContactInfo()
	{
		HashMap<String, String> wordList=helper.getContactInfo("0711525388","Home");
		
		System.out.println("Contact name is "+wordList.get("Name"));
	}
	
	public void getAllAllowedContacts()
	{
		ArrayList<HashMap<String, String>> list=helper.getAllAlloedContacts("Home");
		Iterator<HashMap<String, String>> iterator=list.iterator();
		HashMap<String, String> map;
		
		while (iterator.hasNext()) {
			map=iterator.next();
			System.out.println("contact number = "+map.get("Number")+" name= "+map.get("Name"));
		}
	}
	
	public void getAllprofiles()
	{
		ArrayList<HashMap<String, String>> list=helper.getAllProfiles();
		Iterator<HashMap<String, String>> iterator=list.iterator();
		HashMap<String, String> map;
		
		while (iterator.hasNext()) {
			map=iterator.next();
			System.out.println("Profile name = "+map.get("Name")+" Latitude = "+map.get("Latitude")+" Longitude = "+map.get("Longitude"));
		}
	}
	
	public void selectNameByPosition()
	{
		System.out.println("Selected profile name "+helper.getProfileNameFromcordinates(123,150));
	}

}
