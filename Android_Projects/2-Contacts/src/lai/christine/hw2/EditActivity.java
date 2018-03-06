package lai.christine.hw2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

@SuppressLint("SimpleDateFormat")
public class EditActivity extends ActionBarActivity {

	private Contact contact;
	private EditText displayName;
	private EditText firstName;
	private EditText lastName;
	private EditText birthday;
	private EditText homePhone;
	private EditText workPhone;
	private EditText mobilePhone;
	private EditText emailAddress;

	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        
        contact = (Contact) getIntent().getParcelableExtra(getString(R.string.contact));
        
        displayName = (EditText) findViewById(R.id.display_name);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        birthday = (EditText) findViewById(R.id.birthday);
        homePhone = (EditText) findViewById(R.id.home_phone);
        workPhone = (EditText) findViewById(R.id.work_phone);
        mobilePhone = (EditText) findViewById(R.id.mobile_phone);
        emailAddress = (EditText) findViewById(R.id.email_address);
        
        displayName.setText(contact.getDisplayName());
        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());
        birthday.setText("");
        if (contact.getBirthday() != null) {
        	birthday.setText(String.valueOf(contact.getBirthday().getMonth()+1) + "-" + 
	        				 String.valueOf(contact.getBirthday().getDate()) + "-" + 
	        				 String.valueOf(contact.getBirthday().getYear()+1900));
        }
        homePhone.setText(contact.getHomePhone());
        workPhone.setText(contact.getWorkPhone());
        mobilePhone.setText(contact.getMobilePhone());
        emailAddress.setText(contact.getEmailAddress());
    }
    
	private void setEditedContact() {
		contact.setDisplayName(displayName.getText().toString());
		contact.setFirstName(firstName.getText().toString());
		contact.setLastName(lastName.getText().toString());
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy"); 
		try {
			Date birthDate = sdf.parse(birthday.getText().toString());
			contact.setBirthday(birthDate);
		} catch (ParseException e) {
//			Log.d("Date", "ParseError");
		}
		contact.setHomePhone(homePhone.getText().toString());
		contact.setWorkPhone(workPhone.getText().toString());
		contact.setMobilePhone(mobilePhone.getText().toString());
		contact.setEmailAddress(emailAddress.getText().toString());
		getIntent().putExtra(getString(R.string.contact), contact);
		setResult(RESULT_OK, getIntent());
	}
	
	@Override
	public void onBackPressed() {
		setEditedContact();
		super.onBackPressed();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        	case R.id.action_cancel:
        		setResult(RESULT_CANCELED);
        		finish();
        		return true;
        	case R.id.action_done:
        		setEditedContact();
        		finish();
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }

}
