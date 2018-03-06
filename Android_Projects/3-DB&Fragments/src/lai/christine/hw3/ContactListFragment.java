package lai.christine.hw3;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactListFragment extends Fragment {

	public interface ContactListFragmentListener {
		void onCreate();
		void onDisplay(long id);
	}
	
	private ContactListFragmentListener contactListFragmentListener;
	
	public void setContactListFragmentListener(ContactListFragmentListener contactListFragmentListener) {
		this.contactListFragmentListener = contactListFragmentListener;
	}
	
    private static final int CONTACT_LOADER = 1;
    private SimpleCursorAdapter cursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        
        ListView listView = (ListView) view.findViewById(R.id.contact_list);
		
		String[] from = {
				ContactContentProvider.ID,
				ContactContentProvider.DISPLAY_NAME,
				ContactContentProvider.FIRST_NAME,
				ContactContentProvider.LAST_NAME,
				ContactContentProvider.BIRTHDAY,
				ContactContentProvider.HOME_PHONE,
				ContactContentProvider.WORK_PHONE,
				ContactContentProvider.MOBILE_PHONE,
				ContactContentProvider.EMAIL_ADDRESS
		};
		int[] to = {
				-1, // id not displayed in the layout
				R.id.display_name,
				-1,
				-1,
				-1,
				R.id.home_phone,
				-1,
				-1,
				-1
		};
		
		cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.contact, null, from, to, 0);
		listView.setAdapter(cursorAdapter);
        
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Log.d("List", "Clicked " + position + " " + contact);
				// bring up the display activity
				if (contactListFragmentListener == null) {
					throw new RuntimeException("You must register a ListFragmentListener");
				}
				contactListFragmentListener.onDisplay(id);
			}
		});
		
		// start asynchronous loading of the cursor
        getActivity().getSupportLoaderManager().initLoader(CONTACT_LOADER, null, loaderCallbacks);
        setHasOptionsMenu(true);
        return view;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_contact_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.action_create:
    			// bring up the edit activity
				if (contactListFragmentListener == null) {
					throw new RuntimeException("You must register a ListFragmentListener");
				}
				contactListFragmentListener.onCreate();
    			return true;
    		case R.id.action_settings:
    			return true;
			default:
				return super.onOptionsItemSelected(item);
    	}
    }
    
    private LoaderCallbacks<Cursor> loaderCallbacks = new LoaderCallbacks<Cursor>() {
		@Override
		public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
			String[] projection = {
					ContactContentProvider.ID,
					ContactContentProvider.DISPLAY_NAME,
					ContactContentProvider.FIRST_NAME,
					ContactContentProvider.LAST_NAME,
					ContactContentProvider.BIRTHDAY,
					ContactContentProvider.HOME_PHONE,
					ContactContentProvider.WORK_PHONE,
					ContactContentProvider.MOBILE_PHONE,
					ContactContentProvider.EMAIL_ADDRESS};
			return new CursorLoader(
					getActivity(), 
					ContactContentProvider.CONTENT_URI, 
					projection, 
					null, null, // selection & args
					ContactContentProvider.DISPLAY_NAME + " asc");
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			cursorAdapter.swapCursor(cursor); // set the data
		}

		@Override
		public void onLoaderReset(Loader<Cursor> cursor) {
			cursorAdapter.swapCursor(null); // clear the data
		}
	};
}
