package lai.christine.hw3;

import lai.christine.hw3.ContactListFragment.ContactListFragmentListener;
import lai.christine.hw3.DisplayFragment.DisplayFragmentListener;
import lai.christine.hw3.EditFragment.EditFragmentListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

public class ContactListActivity extends ActionBarActivity {

	private ContactListFragment contactListFragment;
	private DisplayFragment displayFragment;
	private EditFragment editFragment;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        
        contactListFragment = (ContactListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_contact_list);
        displayFragment = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_edit);
        
        final boolean dualMode = (displayFragment != null) && (displayFragment.isInLayout());
        
        if (dualMode) {
	        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	        transaction.hide(editFragment);
	        transaction.commit();
	        long contactId = getIntent().getLongExtra(getString(R.string.contactid), -1);
	        displayFragment.setContact(contactId);
        }
        
        contactListFragment.setContactListFragmentListener(new ContactListFragmentListener() {
        	@Override
        	public void onDisplay(long id) {
        		if (dualMode) {
        			displayFragment.setContact(id);
        		} else {
        			Intent intent = new Intent(ContactListActivity.this, DisplayActivity.class);
        			intent.putExtra(getString(R.string.contactid), id);
        			startActivity(intent);
        		}
        	}
        	@Override
        	public void onCreate() {
    			if (dualMode) {
    				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    				transaction.hide(displayFragment);
			        transaction.show(editFragment);
			        transaction.commit();
        			editFragment.setContact(-1);
        		} else {
					Intent intent = new Intent(ContactListActivity.this, EditActivity.class);
					intent.putExtra(getString(R.string.contactid), (long) -1);
					startActivity(intent);
        		}
        	}
        });
        
        if (dualMode) {
	        displayFragment.setDisplayFragmentListener(new DisplayFragmentListener() {
				@Override
				public void onEdit(Contact contact) {
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			        transaction.hide(displayFragment);
			        transaction.show(editFragment);
			        transaction.commit();
			        editFragment.setContact(contact.getId());
				}
			});
        
	        editFragment.setEditFragmentListener(new EditFragmentListener() {
				@Override
				public void onDone(Contact contact) {
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			        transaction.hide(editFragment);
			        transaction.show(displayFragment);
			        transaction.commit();
			        displayFragment.setContact(contact.getId());
				}
				@Override
				public void onCancel() {
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			        transaction.hide(editFragment);
			        transaction.show(displayFragment);
			        transaction.commit();
				}
			});
        }
    }
}
