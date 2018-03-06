package lai.christine.hw3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
//import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class Contact implements Parcelable {
	
	private long id = -1;
	private String displayName;
	private String firstName;
	private String lastName;
	private Date birthday;
	private String homePhone;
	private String workPhone;
	private String mobilePhone;
	private String emailAddress;
	
	public Contact() {
		super();
	}
	Contact(String displayName, String firstName, String lastName, 
			Date birthday, 
			String homePhone, String workPhone, String mobilePhone, 
			String emailAddress) {
		this.displayName = displayName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.homePhone = homePhone;
		this.workPhone = workPhone;
		this.mobilePhone = mobilePhone;
		this.emailAddress = emailAddress;
	}
	Contact(long id, 
		    String displayName, String firstName, String lastName, 
		    Date birthday, 
		    String homePhone, String workPhone, String mobilePhone, 
		    String emailAddress) {
		this.id = id;
		this.displayName = displayName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.homePhone = homePhone;
		this.workPhone = workPhone;
		this.mobilePhone = mobilePhone;
		this.emailAddress = emailAddress;
	}
	
	public long getId() {
		return id;
	}
	void setId(long id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	@Override
	public String toString() {
		return "Contact [id=" + id + ", displayName=" + displayName
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", birthday=" + birthday + ", homePhone=" + homePhone
				+ ", workPhone=" + workPhone + ", mobilePhone=" + mobilePhone
				+ ", emailAddress=" + emailAddress + "]";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(displayName);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(dateToString(birthday));
		dest.writeString(homePhone);
		dest.writeString(workPhone);
		dest.writeString(mobilePhone);
		dest.writeString(emailAddress);
	}
	
	public static Parcelable.Creator<Contact> CREATOR = new Creator<Contact>() {
		@Override
		public Contact[] newArray(int size) {
			return new Contact[size];
		}
		@Override
		public Contact createFromParcel(Parcel source) {
			long id = source.readLong();
			String displayName = source.readString();
			String firstName = source.readString();
			String lastName = source.readString();
			String birthdayString = source.readString();
			Date birthday = parseDate(birthdayString);
			String homePhone = source.readString();
			String workPhone = source.readString();
			String mobilePhone = source.readString();
			String emailAddress = source.readString();
			return new Contact(id, displayName, firstName, lastName, birthday, 
							   homePhone, workPhone, mobilePhone, emailAddress);
		}
	};
	
	public static Date parseDate(String dateString) {
		if (dateString != "") {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
    		try {
    			return sdf.parse(dateString);
    		} catch (ParseException e) {
//    			Log.d("Date", "ParseError");
    		}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static String dateToString(Date date) {
		if (date == null) {
			return "";
		}
		return String.valueOf(date.getYear()+1900) + "-" + 
			   String.valueOf(date.getMonth()+1) + "-" + 
			   String.valueOf(date.getDate());
	}
}
