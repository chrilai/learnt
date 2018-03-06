package lai.christine.hw2;

import java.util.ArrayList;
import java.util.List;

import lai.christine.hw2.ContactList.ChangeListener;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ContactListAdapter implements ListAdapter {
	
	private List<DataSetObserver> observers = new ArrayList<DataSetObserver>();
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		observers.add(observer);
	}
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		observers.remove(observer);
	}
	
	private ContactList contactList;
	private LayoutInflater layoutInflator;
	
	public ContactListAdapter(ContactList contactList, LayoutInflater layoutInflator) {
		super();
		this.contactList = contactList;
		this.layoutInflator = layoutInflator;
		
		contactList.addChangeListener(new ChangeListener() {
			@Override
			public void changed() {
				for (DataSetObserver observer : observers) {
					observer.onChanged();
				}
			}
		});
	}
	
	@Override
	public int getCount() {
		return contactList.size();
	}

	@Override
	public Object getItem(int position) {
		return contactList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflator.inflate(R.layout.contact, null);
		}
		TextView displayName = (TextView) convertView.findViewById(R.id.display_name);
		TextView homePhone = (TextView) convertView.findViewById(R.id.home_phone);
		Contact contact = (Contact) getItem(position);
		displayName.setText(contact.getDisplayName());
		homePhone.setText(contact.getHomePhone());
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return contactList.size() == 0;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

}
