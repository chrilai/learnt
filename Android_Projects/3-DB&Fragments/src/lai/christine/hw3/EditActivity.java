package lai.christine.hw3;

import lai.christine.hw3.EditFragment.EditFragmentListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class EditActivity extends ActionBarActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        
        EditFragment editFragment = 
        		(EditFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_edit);
        long contactId = getIntent().getLongExtra(getString(R.string.contactid), -1);
        editFragment.setContact(contactId);
        
        editFragment.setEditFragmentListener(new EditFragmentListener() {
        	@Override
        	public void onDone(Contact contact) {
        		getIntent().putExtra(getString(R.string.contactid), contact.getId());
        		finish();
        	}
        	@Override
        	public void onCancel() {
        		finish();
        	}
        });
    }
}
