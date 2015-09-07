package id2216.HosseinMolazemhosseini.Diary;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TabWidget extends TabActivity {
	/** Called when the activity is first created. */
	TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabwidget);

		((GlobalVariables) this.getApplication()).setId(-1);
		Resources res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab		
		
		// Do the same for the other tabs
		intent = new Intent().setClass(this, DiaryLists.class);
		spec = tabHost.newTabSpec("list")
				.setIndicator("List", res.getDrawable(R.drawable.ic_tab_list))
				.setContent(intent);
		tabHost.addTab(spec);

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, DiaryID2216Activity.class);
		spec = tabHost.newTabSpec("new")
				.setIndicator("New", res.getDrawable(R.drawable.ic_tab_new))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

	}

	public void switchTab(int tab) {
		tabHost.setCurrentTab(tab);
	}

}
