/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\Downloads\\Google Drive2\\605.486.81 Mobile Application Development for the Android Platform\\assignments\\lai.christine.hw6\\src\\lai\\christine\\hw6\\UFOServiceReporter.aidl
 */
package lai.christine.hw6;
public interface UFOServiceReporter extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements lai.christine.hw6.UFOServiceReporter
{
private static final java.lang.String DESCRIPTOR = "lai.christine.hw6.UFOServiceReporter";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an lai.christine.hw6.UFOServiceReporter interface,
 * generating a proxy if needed.
 */
public static lai.christine.hw6.UFOServiceReporter asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof lai.christine.hw6.UFOServiceReporter))) {
return ((lai.christine.hw6.UFOServiceReporter)iin);
}
return new lai.christine.hw6.UFOServiceReporter.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_report:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<lai.christine.hw6.UFOPosition> _arg0;
_arg0 = data.createTypedArrayList(lai.christine.hw6.UFOPosition.CREATOR);
this.report(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements lai.christine.hw6.UFOServiceReporter
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void report(java.util.List<lai.christine.hw6.UFOPosition> ufoList) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedList(ufoList);
mRemote.transact(Stub.TRANSACTION_report, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_report = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void report(java.util.List<lai.christine.hw6.UFOPosition> ufoList) throws android.os.RemoteException;
}
