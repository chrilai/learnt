package lai.christine.hw3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayFragment extends Fragment {

	public interface DisplayFragmentListener {
		void onEdit(Contact contact);
	}
	
	private DisplayFragmentListener displayFragmentListener;
	
	public void setDisplayFragmentListener(DisplayFragmentListener displayFragmentListener) {
		this.displayFragmentListener = displayFragmentListener;
	}
	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        
        displayName = (TextView) view.findViewById(R.id.display_name);
        firstName = (TextView) view.findViewById(R.id.first_name);
        lastName = (TextView) view.findViewById(R.id.last_name);
        birthday = (TextView) view.findViewById(R.id.birthday);
        homePhone = (TextView) view.findViewById(R.id.home_phone);
        workPhone = (TextView) view.findViewById(R.id.work_phone);
        mobilePhone = (TextView) view.findViewById(R.id.mobile_phone);
        emailAddress = (TextView) view.findViewById(R.id.email_address);
        
        setHasOptionsMenu(true);
        return view;
    }
	
	public void setContact(long contactId) {
		if (contactId != -1) {
        	contact = ContactContentProvider.findContact(getActivity(), contactId);
        } else {
        	contact = new Contact();
        }
		displayName.setText(contact.getDisplayName());
        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());
        birthday.setText(Contact.dateToString(contact.getBirthday()));
        homePhone.setText(contact.getHomePhone());
        workPhone.setText(contact.getWorkPhone());
        mobilePhone.setText(contact.getMobilePhone());
        emailAddress.setText(contact.getEmailAddress());
	}
    
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_display, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.action_edit:
    			// bring up the edit activity
    			if (displayFragmentListener == null) {
					throw new RuntimeException("You must register a DisplayFragmentListener");
				}
				displayFragmentListener.onEdit(contact);
    			return true;
    		case R.id.action_settings:
    			return true;
			default:
				return super.onOptionsItemSelected(item);
    	}
    }
}
