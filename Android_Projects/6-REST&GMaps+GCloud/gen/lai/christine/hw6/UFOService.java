/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\Downloads\\Google Drive2\\605.486.81 Mobile Application Development for the Android Platform\\assignments\\lai.christine.hw6\\src\\lai\\christine\\hw6\\UFOService.aidl
 */
package lai.christine.hw6;
public interface UFOService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements lai.christine.hw6.UFOService
{
private static final java.lang.String DESCRIPTOR = "lai.christine.hw6.UFOService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an lai.christine.hw6.UFOService interface,
 * generating a proxy if needed.
 */
public static lai.christine.hw6.UFOService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof lai.christine.hw6.UFOService))) {
return ((lai.christine.hw6.UFOService)iin);
}
return new lai.christine.hw6.UFOService.Stub.Proxy(obj);
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
case TRANSACTION_add:
{
data.enforceInterface(DESCRIPTOR);
lai.christine.hw6.UFOServiceReporter _arg0;
_arg0 = lai.christine.hw6.UFOServiceReporter.Stub.asInterface(data.readStrongBinder());
this.add(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_remove:
{
data.enforceInterface(DESCRIPTOR);
lai.christine.hw6.UFOServiceReporter _arg0;
_arg0 = lai.christine.hw6.UFOServiceReporter.Stub.asInterface(data.readStrongBinder());
this.remove(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements lai.christine.hw6.UFOService
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
@Override public void add(lai.christine.hw6.UFOServiceReporter reporter) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((reporter!=null))?(reporter.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_add, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void remove(lai.christine.hw6.UFOServiceReporter reporter) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((reporter!=null))?(reporter.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_remove, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_remove = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void add(lai.christine.hw6.UFOServiceReporter reporter) throws android.os.RemoteException;
public void remove(lai.christine.hw6.UFOServiceReporter reporter) throws android.os.RemoteException;
}
