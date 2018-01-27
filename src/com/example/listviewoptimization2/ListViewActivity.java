package com.example.listviewoptimization2;

import java.util.ArrayList;
import java.util.List;

import org.tracker.Background;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facerecognition.R;
import com.pojoLayer.DTOApp;

public class ListViewActivity extends Activity implements OnClickListener{

	ListView list;
	Button bttn;
	customAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.facerecognition.R.layout.activity_main);
		Resources res = getResources();
		list = (ListView) findViewById(R.id.listView1);
		bttn = (Button) findViewById(R.id.button1);
		
		
		Intent mServiceIntent = new Intent(this, Background.class);
		startService(mServiceIntent);
		
		ArrayList<DTOApp> DTOList = new ArrayList<DTOApp>();
	 PackageManager packageManager = getPackageManager();
		//get a list of installed apps. 
		List <PackageInfo> packageList = packageManager
			    .getInstalledPackages(PackageManager.GET_META_DATA);//all apps in phone
		final List <PackageInfo> packageList1 = packageManager
			    .getInstalledPackages(0);
			 packageList1.clear();
			 int i=0;
for (PackageInfo packageInfo : packageList) {
	if(((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0)!= true)
	{//check whtether it is system app or user installed app
		  packageList1.add(packageInfo); // add in 2nd list if it is user installed app
          String pkgname=packageList1.get(i).packageName;
          String appName = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
          DTOApp dto=new DTOApp();
        dto.setPkgName(pkgname);
          dto.setName(appName);        
          dto.setSelected(false);
         DTOList.add(dto);
          Log.d("test", dto.getName());
          i++;
	}	
	
	String[] maintitles=new String[DTOList.size()];
	for (int j=0;j<DTOList.size();j++) {
		maintitles[j]=DTOList.get(j).getName();
	}

	
			adapter = new customAdapter(this, maintitles,DTOList);
			list.setAdapter(adapter);
			bttn.setOnClickListener(this);
}
	

	
	}	
	



	class customAdapter extends ArrayAdapter<String> {
		Context context;
		String titleArr[];
		PackageManager packageManager;
		private ArrayList<DTOApp> AppList;
		private ArrayList<DTOApp> NewAppList=new ArrayList<DTOApp>();
		public customAdapter(Context con, String titles[],ArrayList<DTOApp> APPList) {
			super(con, R.layout.row, R.id.textView1, titles);
			this.context = con;
			this.titleArr = titles;
			//getPackageManager().getApplicationIcon("com.AdhamiPiranJhandukhel.com");
			this.packageManager=packageManager;
			this.AppList = new ArrayList<DTOApp>();
			this.AppList.addAll(APPList);
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final DTOApp dtoapp = AppList.get(position);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row, null);
			TextView name = (TextView) convertView.findViewById(R.id.textView1);
			CheckBox check = (CheckBox) convertView
					.findViewById(R.id.checkBox1);
			ImageView image = (ImageView) convertView
					.findViewById(R.id.imageView1);
			check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					dtoapp.setSelected(isChecked);
					String str = dtoapp.isSelected() + "";
					Toast.makeText(context, dtoapp.getName() + str,
							Toast.LENGTH_LONG).show();
					NewAppList.add(dtoapp);
					
				}
			});
			name.setText(dtoapp.getName());
			Drawable icon;
			try {
				icon = getPackageManager().getApplicationIcon(dtoapp.getPkgName());
				image.setImageDrawable(icon);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			NewAppList.add(dtoapp);
			return convertView;
			
		}
}
	public void SetImage(View v) {
		this.startActivity(new Intent(this,com.example.facerecognition.SetImage.class));
		finish();
	}
	public void exit(View v)
	{
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.button1:
			Background.checkedme.clear();
			int i=0;
			int size=adapter.NewAppList.size();
			ArrayList<String> checklist=new ArrayList<String>();
			Log.i("names",size+"");
			for(DTOApp dt:adapter.NewAppList)
			{
				if(dt.isSelected())
				{
				checklist.add(dt.getPkgName());
				Log.i("names", "Checked"+dt.getName()+":"+dt.isSelected());
				if(!Background.checkedme.contains(dt.getPkgName()))
					Background.checkedme.add(dt.getPkgName());
				}
				
			}		
	       
			break;
			}

	}
}