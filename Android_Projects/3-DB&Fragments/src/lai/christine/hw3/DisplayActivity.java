package lai.christine.hw3;

import lai.christine.hw3.DisplayFragment.DisplayFragmentListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class DisplayActivity extends ActionBarActivity {

	private DisplayFragment displayFragment;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        
        displayFragment = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        displayFragment.setDisplayFragmentListener(new DisplayFragmentListener() {
        	@Override
        	public void onEdit(Contact contact) {
				Intent intent = new Intent(DisplayActivity.this, EditActivity.class);
				intent.putExtra(getString(R.string.contactid), contact.getId());
				startActivity(intent);
        	}
        });
    }
    
	@Override
	protected void onStart() {
		super.onStart();
		long contactId = getIntent().getLongExtra(getString(R.string.contactid), -1);
		displayFragment.setContact(contactId);
	}
	
}
