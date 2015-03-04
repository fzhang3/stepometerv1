package com.example.stepometerv1;

import java.util.Arrays;

import com.example.stepometerv1.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			//TextView title = (TextView) rootView.findViewById(R.id.title);
			//title.setText("Lab 2");

			LinearLayout lmain = (LinearLayout) rootView.findViewById(R.id.lmain);
			lmain.setOrientation(LinearLayout.VERTICAL);

			SensorManager sensorManager = (SensorManager)
					rootView.getContext().getSystemService(SENSOR_SERVICE);

			TextView data = (TextView) rootView.findViewById(R.id.data);
			final LineGraphView graph = new LineGraphView(rootView.getContext(),
					100,
					Arrays.asList("x", "y", "z"));
			//lmain.addView(graph);

			graph.setVisibility(View.VISIBLE);
			Sensor accelerometer =
					sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
			final SensorEventListener a = new AccelSensorEventListener(data,graph);
			sensorManager.registerListener(a, accelerometer,
					SensorManager.SENSOR_DELAY_FASTEST);
			
			Button reset = (Button) rootView.findViewById(R.id.reset);
			reset.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((AccelSensorEventListener) a).reset();
				}
			});
			
			Button purge = (Button) rootView.findViewById(R.id.purge);
			purge.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					graph.purge();
				}
			});

			return rootView;
		}
	}
}