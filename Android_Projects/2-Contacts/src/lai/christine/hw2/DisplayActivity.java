package lai.christine.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayActivity extends ActionBarActivity {

	private static final int EDIT_REQUEST = 13;
	private TextView displayName;
	private TextView firstName;
	private TextView lastName;
	private TextView birthday;
	private TextView homePhone;
	private TextView workPhone;
	private TextView mobilePhone;
	private TextView emailAddress;
	private Contact contact;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        
        contact = (Contact) getIntent().getParcelableExtra(getString(R.string.contact));
        
        displayName = (TextView) findViewById(R.id.display_name);
        firstName = (TextView) findViewById(R.id.first_name);
        lastName = (TextView) findViewById(R.id.last_name);
        birthday = (TextView) findViewById(R.id.birthday);
        homePhone = (TextView) findViewById(R.id.home_phone);
        workPhone = (TextView) findViewById(R.id.work_phone);
        mobilePhone = (TextView) findViewById(R.id.mobile_phone);
        emailAddress = (TextView) findViewById(R.id.email_address);
        
        setContactText();
    }
    
	@SuppressWarnings("deprecation")
	private void setContactText() {
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
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK, getIntent());
		super.onBackPressed();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// save contact
		outState.putParcelable(getString(R.string.contact), contact);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.action_edit:
    			// bring up the edit activity
				//Intent intent = new Intent(DisplayActivity.this, EditActivity.class);
    			Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("application/vnd.lai.christine.data");
				intent.putExtra(getString(R.string.contact), contact);
				startActivityForResult(intent, EDIT_REQUEST);
    			return true;
    		case R.id.action_settings:
    			return true;
			default:
				return super.onOptionsItemSelected(item);
    	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_CANCELED) {
    		return;
    	}
    	switch(requestCode) {
	    	case EDIT_REQUEST:
	    		contact = (Contact) data.getParcelableExtra(getString(R.string.contact));
	    		getIntent().putExtra(getString(R.string.contact), contact);
	    		setContactText();
	    		break;
	    	default:
	    		super.onActivityResult(requestCode, resultCode, data);
	    		break;
    	}
    }
}
