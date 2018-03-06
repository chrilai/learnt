package lai.christine.hw3;

import java.util.Calendar;

import lai.christine.hw3.DatePickerDialogFragment.OnDatePickerDialogFragmentDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class EditFragment extends Fragment implements OnDatePickerDialogFragmentDateSetListener {

	public interface EditFragmentListener {
		void onDone(Contact contact);
		void onCancel();
	}
	
	private static final int BIRTHDAY = 1;
	
	private EditFragmentListener editFragmentListener;
	
	public void setEditFragmentListener(EditFragmentListener editFragmentListener) {
		this.editFragmentListener = editFragmentListener;
	}
	
	private Contact contact;
	private EditText displayName;
	private EditText firstName;
	private EditText lastName;
//	private EditText birthday;
	private Button birthdayButton;
	private Calendar birthday = Calendar.getInstance();
	private EditText homePhone;
	private EditText workPhone;
	private EditText mobilePhone;
	private EditText emailAddress;

	public void setContact(long contactId) {
        if (contactId != -1) {
        	contact = ContactContentProvider.findContact(getActivity(), contactId);
        } else {
        	contact = new Contact();
        }
        
        displayName.setText(contact.getDisplayName());
        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());
//        birthday.setText(Contact.dateToString(contact.getBirthday()));
        homePhone.setText(contact.getHomePhone());
        workPhone.setText(contact.getWorkPhone());
        mobilePhone.setText(contact.getMobilePhone());
        emailAddress.setText(contact.getEmailAddress());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        
        displayName = (EditText) view.findViewById(R.id.display_name);
        firstName = (EditText) view.findViewById(R.id.first_name);
        lastName = (EditText) view.findViewById(R.id.last_name);
//        birthday = (EditText) view.findViewById(R.id.birthday);
        birthdayButton = (Button) view.findViewById(R.id.birthdayButton);
        homePhone = (EditText) view.findViewById(R.id.home_phone);
        workPhone = (EditText) view.findViewById(R.id.work_phone);
        mobilePhone = (EditText) view.findViewById(R.id.mobile_phone);
        emailAddress = (EditText) view.findViewById(R.id.email_address);
        
        if (savedInstanceState != null) {
        	birthday.setTimeInMillis(savedInstanceState.getLong("birthday"));
        } else {
        	birthday.set(1988, 10, 13);
        }
        updateDateButtonText("Birthday", birthdayButton, birthday);
        
		birthdayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialogFragment fragment = DatePickerDialogFragment.create(EditFragment.this, BIRTHDAY, birthday);
				fragment.show(getActivity().getSupportFragmentManager(), "setting birthday");
			}
		});
        
        setHasOptionsMenu(true);
        return view;
    }
    
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong("birthday", birthday.getTimeInMillis());
	}
	
	private void updateDateButtonText(String header, Button button, Calendar calendar) {
		button.setText(header + ": " + calendar.get(Calendar.YEAR) + "-" +
									   (calendar.get(Calendar.MONTH)+1) + "-" +
									   calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	private void setEditedContact() {
		contact.setDisplayName(displayName.getText().toString());
		contact.setFirstName(firstName.getText().toString());
		contact.setLastName(lastName.getText().toString());
//		contact.setBirthday(Contact.parseDate(birthday.getText().toString()));
		contact.setBirthday(birthday.getTime());
		contact.setHomePhone(homePhone.getText().toString());
		contact.setWorkPhone(workPhone.getText().toString());
		contact.setMobilePhone(mobilePhone.getText().toString());
		contact.setEmailAddress(emailAddress.getText().toString());
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        	case R.id.action_cancel:
        		if (editFragmentListener == null) {
					throw new RuntimeException("You must register an EditFragmentListener");
				}
				editFragmentListener.onCancel();
        		return true;
        	case R.id.action_done:
        		setEditedContact();
        		ContactContentProvider.updateContact(getActivity(), contact);
        		if (editFragmentListener == null) {
					throw new RuntimeException("You must register an EditFragmentListener");
				}
				editFragmentListener.onDone(contact);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onDateSet(int dateId, int year, int month, int day) {
		switch(dateId) {
			case BIRTHDAY:
				birthday.set(year, month, day);
				updateDateButtonText("Birthday", birthdayButton, birthday);
				break;
			default:
				throw new IllegalStateException("unexpected dateId " + dateId);
		}
	}
}
