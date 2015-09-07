package id2216.HosseinMolazemhosseini.Diary;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DiaryLists extends ListActivity {
	/** Called when the activity is first created. */

	private DbAdapter db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DbAdapter(this);
	}

	public void onResume() {
		super.onResume();
		{
			db.open();
			Cursor c = db.getAllTitles();
			ListAdapter adapter = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_2, c, new String[] {
							DbAdapter.KEY_NOTE, DbAdapter.KEY_TAG }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			setListAdapter(adapter);

			ListView lv = getListView();
			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// When clicked, show a toast with the TextView text

					((GlobalVariables) getApplication()).setId(id);
					switchTabInActivity(1);
					// Toast.makeText(getApplicationContext(),
					// String.valueOf(id),Toast.LENGTH_SHORT).show();
				}
			});
			db.close();
		}

	}

	public void switchTabInActivity(int indexTabToSwitchTo) {
		TabWidget ta = (TabWidget) this.getParent();
		ta.switchTab(indexTabToSwitchTo);
	}

}