package lai.christine.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactListActivity extends ActionBarActivity {

	private static final int CREATE_REQUEST = 11;
	private static final int EDIT_REQUEST = 13;
	private static final int DISPLAY_REQUEST = 17;
    private ContactList contactList;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        
        ListView listView = (ListView) findViewById(R.id.contact_list);
		
        if (savedInstanceState == null) {
        	contactList = new ContactList();
            //contactList.add(new Contact("example","First","Last",new Date(),"(800)EXA-MPLE", null, null, "example@jhu.edu"));
            //contactList.add(new Contact("prof","Scott","Stanchfield",null,"(301)609-0545", null, null, "scott@javadude.com"));
            //contactList.add(new Contact("me","Christine","Lai",new Date(88,10,13),"(917)892-1787", null, null, "chrilai@gmail.com"));
        } else {
        	contactList = savedInstanceState.getParcelable(getString(R.string.contactlist));
        }
        
		listView.setAdapter(new ContactListAdapter(contactList, getLayoutInflater()));
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Contact contact = contactList.get(position);
//				Log.d("List", "Clicked " + position + " " + contact);
				// bring up the display activity
				Intent intent = new Intent(ContactListActivity.this, DisplayActivity.class);
				intent.putExtra(getString(R.string.contact), contact);
				startActivityForResult(intent, DISPLAY_REQUEST);
			}
		});
    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// save contact list
		outState.putParcelable(getString(R.string.contactlist), contactList);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.action_create:
    			// bring up the edit activity
				//Intent intent = new Intent(ContactListActivity.this, EditActivity.class);
				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("application/vnd.lai.christine.data");
				intent.putExtra(getString(R.string.contact), new Contact("", "", "", null, "", "", "", ""));
				startActivityForResult(intent, CREATE_REQUEST);
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
	    	case CREATE_REQUEST:
	    	case DISPLAY_REQUEST:
	    	case EDIT_REQUEST:
	    		Contact contact = (Contact) data.getParcelableExtra(getString(R.string.contact));
	    		contactList.merge(contact);
	    		break;
	    	default:
	    		super.onActivityResult(requestCode, resultCode, data);
	    		break;
    	}
    }
}
