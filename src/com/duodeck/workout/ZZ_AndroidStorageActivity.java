package com.duodeck.workout;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duodeck.R;

public class ZZ_AndroidStorageActivity extends Activity implements OnClickListener{

	private String filename = "MySampleFile.txt";
	private String filepath = "MyFileStorage";
	File myInternalFile;
	File myExternalFile;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_storage);

		ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
		File directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);
		myInternalFile = new File(directory , filename);

		Button saveToInternalStorage = 
				(Button) findViewById(R.id.saveInternalStorage);
		saveToInternalStorage.setOnClickListener(this);

		Button readFromInternalStorage = 
				(Button) findViewById(R.id.getInternalStorage);
		readFromInternalStorage.setOnClickListener(this);

		Button saveToExternalStorage = 
				(Button) findViewById(R.id.saveExternalStorage);
		saveToExternalStorage.setOnClickListener(this);

		Button readFromExternalStorage = 
				(Button) findViewById(R.id.getExternalStorage);
		readFromExternalStorage.setOnClickListener(this);

		//check if external storage is available and not read only  
		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {  
			saveToExternalStorage.setEnabled(false);
		} 
		else {
			myExternalFile = new File(getExternalFilesDir(filepath), filename);
		}

	}

	public void onClick(View v) {

		EditText myInputText = (EditText) findViewById(R.id.myInputText);
		TextView responseText = (TextView) findViewById(R.id.responseText);
		String myData = "";

		switch (v.getId()) {
		case R.id.saveInternalStorage:
			try {
				FileOutputStream fos = new FileOutputStream(myInternalFile);
				fos.write(myInputText.getText().toString().getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			myInputText.setText("");
			responseText
			.setText("MySampleFile.txt saved to Internal Storage...");
			break;

		case R.id.getInternalStorage:
			try {
				FileInputStream fis = new FileInputStream(myInternalFile);
				DataInputStream in = new DataInputStream(fis);
				BufferedReader br = 
						new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					myData = myData + strLine;
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			myInputText.setText(myData);
			responseText
			.setText("MySampleFile.txt data retrieved from Internal Storage..." + myData);
			break;

		case R.id.saveExternalStorage:
			try {
				FileOutputStream fos = new FileOutputStream(myExternalFile);
				fos.write(myInputText.getText().toString().getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			myInputText.setText("");
			responseText
			.setText("MySampleFile.txt saved to External Storage...");
			break;

		case R.id.getExternalStorage:
			try {
				FileInputStream fis = new FileInputStream(myExternalFile);
				DataInputStream in = new DataInputStream(fis);
				BufferedReader br = 
						new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					myData = myData + strLine;
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			myInputText.setText(myData);
			responseText
			.setText("MySampleFile.txt data retrieved from Internal Storage..." + myData);
			break;


		}
	}

	private static boolean isExternalStorageReadOnly() {  
		String extStorageState = Environment.getExternalStorageState();  
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {  
			return true;  
		}  
		return false;  
	}  

	private static boolean isExternalStorageAvailable() {  
		String extStorageState = Environment.getExternalStorageState();  
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {  
			return true;  
		}  
		return false;  
	}  

}