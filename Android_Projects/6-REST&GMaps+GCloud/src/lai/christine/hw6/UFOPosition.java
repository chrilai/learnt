package lai.christine.hw6;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class UFOPosition implements Parcelable {
	
	private int shipNumber;
	private double lat;
	private double lon;
	
	public UFOPosition() {
		super();
	}
	public UFOPosition(int shipNumber, double lat, double lon) {
		super();
		this.shipNumber = shipNumber;
		this.lat = lat;
		this.lon = lon;
	}
	
	public int getShipNumber() {
		return shipNumber;
	}
	public void setShipNumber(int shipNumber) {
		this.shipNumber = shipNumber;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	public String toString() {
		return String.format("{\"ship\":\"%d\", \"lat\":\"%f\", \"lon\":\"%f\"}", shipNumber, lat, lon);
	}
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(shipNumber);
		dest.writeDouble(lat);
		dest.writeDouble(lon);
	}
	
	public static Parcelable.Creator<UFOPosition> CREATOR = new Creator<UFOPosition>() {
		@Override
		public UFOPosition createFromParcel(Parcel source) {
			UFOPosition ufoPosition = new UFOPosition();
			ufoPosition.shipNumber = source.readInt();
			ufoPosition.lat = source.readDouble();
			ufoPosition.lon = source.readDouble();
			return ufoPosition;
		}
		@Override
		public UFOPosition[] newArray(int size) {
			return new UFOPosition[size];
		}
	};
}
