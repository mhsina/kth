package id2216.HosseinMolazemhosseini.Diary;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DiaryID2216Activity extends Activity {
	/** Called when the activity is first created. */

	private ImageView imgImage;
	private Button btnLoadImage;
	private Button btnSave;
	private Button btnSms, btnSend;
	private Button btnNew;
	private Spinner spinner;
	private CheckBox chkLocation;
	private EditText edtAddress;
	private EditText edtNote, editTo;
	private TextView mDateDisplay;
	private Button mPickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	private long SelectedID = -1;
	static final int DATE_DIALOG_ID = 0;
	private DbAdapter db;
	private ArrayAdapter<CharSequence> adapterSpinner;
	private Uri imageUri;
	final Context context = this;

	public void onResume() {
		super.onResume();
		{
			if (((GlobalVariables) getApplication()).getId() != -1) {
				SelectedID = ((GlobalVariables) getApplication()).getId();
				((GlobalVariables) getApplication()).setId(-1);
				db.open();
				Cursor c = db.getTitle(SelectedID);

				edtNote.setText(c.getString(1));

				imageUri = Uri.parse(c.getString(2));
				Bitmap mBitmap = null;
				try {
					mBitmap = Media.getBitmap(this.getContentResolver(),
							Uri.parse(c.getString(2)));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				imgImage.setImageBitmap(mBitmap);
				int pos = adapterSpinner.getPosition(c.getString(3));
				spinner.setSelection(pos);
				edtAddress.setText(c.getString(4));
				mDateDisplay.setText(c.getString(5));

				db.close();

			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = new DbAdapter(this);

		btnLoadImage = (Button) findViewById(R.id.btnImage);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSms = (Button) findViewById(R.id.btnSms);
		btnNew = (Button) findViewById(R.id.btnNew);
		spinner = (Spinner) findViewById(R.id.spinner1);
		chkLocation = (CheckBox) findViewById(R.id.checkBoxLocation);
		edtAddress = (EditText) findViewById(R.id.editTextAddress);
		edtNote = (EditText) findViewById(R.id.editTextNote);
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mPickDate = (Button) findViewById(R.id.pickDate);
		imgImage = (ImageView) findViewById(R.id.imageView1);

		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// display the current date (this method is below)
		updateDisplay();

		chkLocation.setOnCheckedChangeListener(new myCheckBoxChnageClicker());

		btnSms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.smsdialog);
				dialog.setTitle("Send By Sms");

				btnSend = (Button) dialog.findViewById(R.id.btnSend);
				editTo = (EditText) dialog.findViewById(R.id.editTo);

				btnSend.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (editTo.getText().toString().length() > 0
								&& edtNote.getText().toString().length() > 0) {
							sendSMS(editTo.getText().toString(), edtNote
									.getText().toString());
						}
						dialog.dismiss();
					}
				});
				dialog.show();

			}
		});

		btnNew.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				edtNote.setText("");
				imgImage.setImageBitmap(null);
				edtAddress.setText("");
				mDateDisplay.setText("");
				spinner.setSelection(0);
				SelectedID = -1;
			}
		});

		btnLoadImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);
			}
		});

		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (edtNote.getText().toString().length() > 0) {
					long id;
					db.open();
					if (SelectedID == -1) {
						id = db.insertTitle(edtNote.getText().toString(),
								imageUri.toString(), spinner.getSelectedItem()
										.toString(), edtAddress.getText()
										.toString(), mDateDisplay.getText()
										.toString(), "");
					} else {
						db.updateTitle(SelectedID,
								edtNote.getText().toString(), imageUri
										.toString(), spinner.getSelectedItem()
										.toString(), edtAddress.getText()
										.toString(), mDateDisplay.getText()
										.toString(), "");
					}
					edtNote.setText("");
					imgImage.setImageBitmap(null);
					edtAddress.setText("");
					mDateDisplay.setText("");
					spinner.setSelection(0);
					SelectedID = -1;
					db.close();

					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.savedialog);
					dialog.setTitle("Integrate with Calendar");

					EditText editEmail = (EditText) dialog
							.findViewById(R.id.editEmail);

					Button btnSavetoPhoneCalender = (Button) dialog
							.findViewById(R.id.btnSavetoPhoneCalendar);
					btnSavetoPhoneCalender
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {

									String[] projection = new String[] { "_id",
											"name" };

									Uri calendars = Uri
											.parse("content://calendar/calendars");
									if (Build.VERSION.SDK_INT >= 8) {
										calendars = Uri
												.parse("content://com.android.calendar/calendars");
									}

									try {
										Cursor managedCursor = managedQuery(
												calendars, projection,
												"selected=1", null, null);
										if (managedCursor.moveToFirst()) {
											String calName;
											String calId;
											int nameColumn = managedCursor
													.getColumnIndex("name");
											int idColumn = managedCursor
													.getColumnIndex("_id");
											do {
												calName = managedCursor
														.getString(nameColumn);
												calId = managedCursor
														.getString(idColumn);
											} while (managedCursor.moveToNext());

										}

										ContentValues event = new ContentValues();
										event.put("calendar_id", 1);
										event.put("title", spinner
												.getSelectedItem().toString());
										event.put("description", edtNote
												.getText().toString());
										event.put("eventLocation", edtAddress
												.getText().toString());

										Calendar c = Calendar.getInstance();
										c.set(mYear, mMonth, mDay,
												c.get(Calendar.HOUR_OF_DAY),
												c.get(Calendar.MINUTE), 00);
										long startTime = c.getTimeInMillis(); // System.currentTimeMillis();
										event.put("dtstart", startTime);
										// long endTime = END_TIME_MS;
										// event.put("dtend", endTime);

										Uri eventsUri = Uri
												.parse("content://calendar/events");
										if (Build.VERSION.SDK_INT >= 8) {
											eventsUri = Uri
													.parse("content://com.android.calendar/events");
										}

										Uri url = getContentResolver().insert(
												eventsUri, event);
									} catch (Exception e) {
										Toast.makeText(
												getApplicationContext(),
												"Phone Calendar Provider Error",
												Toast.LENGTH_LONG).show();
									}
									dialog.dismiss();
									switchTabInActivity(0);									
								}
							});

					Button btnSavetoGoogleCalender = (Button) dialog
							.findViewById(R.id.btnSavetoGoogleCalendar);
					btnSavetoGoogleCalender
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {

									dialog.dismiss();
									switchTabInActivity(0);
								}
							});
					Button btnDeny = (Button) dialog
							.findViewById(R.id.btnDeny);
					btnDeny
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {

									dialog.dismiss();
									switchTabInActivity(0);
								}
							});


					dialog.show();

				} else {
					Toast.makeText(getApplicationContext(),
							"Note Box Empty", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		adapterSpinner = ArrayAdapter.createFromResource(this,
				R.array.tag_array, android.R.layout.simple_spinner_item);
		adapterSpinner
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapterSpinner);
		// spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

	}

	public void switchTabInActivity(int indexTabToSwitchTo) {
		// MintTrack ParentActivity;
		// ParentActivity = (MintTrack) this.getParent();
		TabWidget ta = (TabWidget) this.getParent();
		ta.switchTab(indexTabToSwitchTo);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			imageUri = data.getData();

			// Toast.makeText(getApplicationContext(),
			// chosenImageUri.toString(), Toast.LENGTH_SHORT);
			// System.out.println("hi" + chosenImageUri.toString());
			Bitmap mBitmap = null;
			try {
				mBitmap = Media.getBitmap(this.getContentResolver(), imageUri);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			imgImage.setImageBitmap(mBitmap);
		}
	}

	class myCheckBoxChnageClicker implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub

			// Toast.makeText(CheckBoxCheckedDemo.this, "Checked => "+isChecked,
			// Toast.LENGTH_SHORT).show();

			if (isChecked) {
				if (buttonView == chkLocation) {

					LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					Criteria criteria = new Criteria();
					String provider = locationManager.getBestProvider(criteria,
							false);
					Location location = locationManager
							.getLastKnownLocation(provider);
					if (location != null) {
						// Toast.makeText(getApplicationContext(),
						// location.toString(), Toast.LENGTH_SHORT).show();
						Geocoder gCoder = new Geocoder(getApplicationContext());
						ArrayList<Address> addresses;
						try {
							addresses = (ArrayList<Address>) gCoder
									.getFromLocation(location.getLatitude(),
											location.getLongitude(), 1);
							if (addresses != null && addresses.size() > 0) {
								edtAddress.setText(addresses.get(0)
										.getAddressLine(0)
										+ ", "
										+ addresses.get(0).getAddressLine(1));
							}

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						Toast.makeText(getApplicationContext(),
								"Location Temporary unavailable",
								Toast.LENGTH_SHORT).show();
					}

				}
			}

		}
	}

	// updates the date in the TextView
	private void updateDisplay() {
		mDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	private void sendSMS(String phoneNumber, String message) {
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				System.out.println("ok");
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}
}