package lai.christine.hw2;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactList implements Parcelable {
	
	public interface ChangeListener {
		void changed();
	}
	private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
	private List<Contact> contacts = new ArrayList<Contact>();

	public void addChangeListener(ChangeListener changeListener) {
		listeners.add(changeListener);
	}
	
	public void removeChangeListener(ChangeListener changeListener) {
		listeners.remove(changeListener);
	}
	
	public void merge(Contact contact) {
		boolean found = false;
		for (Contact contactInList : contacts) {
			if (contactInList.getId() == contact.getId()) {
				contactInList.setDisplayName(contact.getDisplayName());
				contactInList.setFirstName(contact.getFirstName());
				contactInList.setLastName(contact.getLastName());
				contactInList.setBirthday(contact.getBirthday());
				contactInList.setHomePhone(contact.getHomePhone());
				contactInList.setWorkPhone(contact.getWorkPhone());
				contactInList.setMobilePhone(contact.getMobilePhone());
				contactInList.setEmailAddress(contact.getEmailAddress());
				found = true;
				break;
			}
		}
		if (!found) {
			contacts.add(contact);
		}
		for (ChangeListener changeListener : listeners) {
			changeListener.changed();
		}
	}
	public boolean add(Contact object) {
		boolean added = contacts.add(object);
		for (ChangeListener changeListener : listeners) {
			changeListener.changed();
		}
		return added;
	}

	public Contact get(int location) {
		return contacts.get(location);
	}

	public Contact remove(int location) {
		Contact removed = contacts.remove(location);
		for (ChangeListener changeListener : listeners) {
			changeListener.changed();
		}
		return removed;
	}

	public Contact set(int location, Contact object) {
		return contacts.set(location, object);
	}

	public int size() {
		return contacts.size();
	}

	@Override
	public String toString() {
		return "ContactList [contacts=" + contacts + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(contacts.size());
		for (Contact contact : contacts) {
			dest.writeParcelable(contact, 0);
		}
	}

	public static Parcelable.Creator<ContactList> CREATOR = new Creator<ContactList>() {
		@Override
		public ContactList[] newArray(int size) {
			return new ContactList[size];
		}
		@Override
		public ContactList createFromParcel(Parcel source) {
			ContactList contactList = new ContactList();
			int size = source.readInt();
			for (int i = 0; i < size; i++) {
				Contact contact = source.readParcelable(getClass().getClassLoader());
				contactList.add(contact);
			}
			return contactList;
		}
	};
}
