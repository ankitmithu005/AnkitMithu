package com.example.client_server_single;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;
 
/**
 * This is a simple Android mobile client
 * This application read any string massage typed on the text field and 
 * send it to the server when the Send button is pressed
 * Author by Lak J Comspace
 *
 */
public class SlimpleTextClientActivity extends Activity {
 
	private Socket client;
	private PrintWriter printwriter;
	private EditText textField3,textField1,textField2;
	private Button button;
	private String messsage;
	private RadioGroup radioSexGroup;
	  private RadioButton radioSexButton;
	  private Spinner country;
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slimple_text_client);
			
		textField1 = (EditText) findViewById(R.id.editText1); // reference to the text field
		textField2 = (EditText) findViewById(R.id.editText2);
		textField3 = (EditText) findViewById(R.id.editText3);
		radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
		button = (Button) findViewById(R.id.button1); // reference to the send button
		
		 country = (Spinner) findViewById(R.id.spinner1);

	        Locale[] locales = Locale.getAvailableLocales();
	        ArrayList<String> countries = new ArrayList<String>();
	        for (Locale locale : locales) {
	            String country = locale.getDisplayCountry();
	            if (country.trim().length() > 0 && !countries.contains(country)) {
	                countries.add(country);
	            }
	        }
	        Collections.sort(countries);
	        for (String country : countries) {
	            System.out.println(country);
	        }

	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	                android.R.layout.simple_spinner_item, countries);
	        // set the view for the Drop down list
	        dataAdapter
	                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        // set the ArrayAdapter to the spinner
	        country.setAdapter(dataAdapter);
	        country.setSelection(1);

	        System.out.println("# countries found: " + countries.size());

		final ToggleButton passtog=(ToggleButton)findViewById(R.id.tbpassword);
		// Button press event listener
		passtog.setOnClickListener(
				new View.OnClickListener()
				{
			
					@Override
					public void onClick(View v) {
					// TODO Auto-generated method stub
						if(passtog.isChecked())
						{
							textField3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						}
						else
						{
							textField3.setInputType(InputType.TYPE_CLASS_TEXT);
						}
					}
					});
		/*button.setOnClickListener(new View.OnClickListener() {
 
			public void onClick(View v) {
				messsage = textField.getText().toString(); // get the text message on the text field
				textField.setText(""); // Reset the text field to blank
				SendMessage sendMessageTask = new SendMessage();
				sendMessageTask.execute();
				
				Notify("Title: Meeting with Business",
					      "Msg:Pittsburg 10:00 AM EST ");
			}
		});*/
		
		button.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
	 
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
	 
				// set title
				alertDialogBuilder.setTitle("Are u sure");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Click yes to exit!")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							//SlimpleTextClientActivity.this.finish();
							String m1,m2,m3;
							m1 = textField1.getText().toString(); // get the text message on the text field
							m2=textField2.getText().toString();
							m3=textField3.getText().toString();
							int selectedId = radioSexGroup.getCheckedRadioButtonId();
							 
							// find the radiobutton by returned id
							final Calendar c = Calendar.getInstance();
							int year = c.get(Calendar.YEAR);
							int month = c.get(Calendar.MONTH);
							int day = c.get(Calendar.DAY_OF_MONTH);
						     radioSexButton = (RadioButton) findViewById(selectedId);
							textField1.setText("");
							textField2.setText("");
							textField3.setText("");// Reset the text field to blank
							messsage=m1+m2+m3+radioSexButton.getText()+(String) country.getSelectedItem();
							messsage+=Integer.toString(day)+"    "+Integer.toString(month)+"   "+Integer.toString(year);
							SendMessage sendMessageTask = new SendMessage();
							sendMessageTask.execute();
							String a="ankit123";
							
							//Notify("Title: Status:",
								 //     "Msg:Message sent succcessfully ");
							Notify("Title: Status:",
								      "Msg: "+messsage);
						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
				}
			});
	}
		
 
	private class SendMessage extends AsyncTask<Void, Void, Void> {
 
		@Override
		protected Void doInBackground(Void... params) {
			try {
 
				client = new Socket("10.0.2.2", 4444); // connect to the server
				printwriter = new PrintWriter(client.getOutputStream(), true);
				printwriter.write(messsage); // write the message to output stream
 
				printwriter.flush();
				printwriter.close();
				client.close(); // closing the connection
 
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
 
	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 @SuppressWarnings("deprecation")
	 private void Notify(String notificationTitle, String notificationMessage) {
	  NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	  @SuppressWarnings("deprecation")
	  Notification notification = new Notification(R.drawable.ic_launcher,
	    "New Message", System.currentTimeMillis());

	   Intent notificationIntent = new Intent(this, MainActivity.class);
	  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
	    notificationIntent, 0);

	   notification.setLatestEventInfo(SlimpleTextClientActivity.this, notificationTitle,
	    notificationMessage, pendingIntent);
	  notificationManager.notify(9999, notification);
	 }
	 
	 
 
}