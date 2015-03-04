package com.example.stepometerv1;

//import android.R.string;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

//Acceleration class methods inside relate to getting acceleration readings.
public class AccelSensorEventListener implements SensorEventListener {
	TextView output;
	float[] smoothAccel;
	LineGraphView graph;
	boolean phase1 = false;
	boolean phase2= false;
	boolean phase3 =false;
	int step;

	//outputs the acceleration
	public AccelSensorEventListener(TextView outputView, LineGraphView graphView){
		output = outputView;
		smoothAccel = new float[3];
		graph = graphView;
		step = 0;
	}

	public void onAccuracyChanged(Sensor s, int i) {}

	// detects change in acceleration and outputs it to the to the "output"
	// everytime a change is detected.
	public void onSensorChanged(SensorEvent se) {
		//tests if max value has changed
		smoothAccel[0] += (se.values[0] - smoothAccel[0])/900;
		smoothAccel[1] += (se.values[1] - smoothAccel[1])/900;
		smoothAccel[2] += (se.values[2] - smoothAccel[2])/200;
		stepCounter(smoothAccel[2],smoothAccel[1],smoothAccel[0]);

		if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			output.setText(
					"\n---Steps---"+
							"\nSteps: "+Integer.toString(step));
			graph.addPoint(smoothAccel);
		}
	}
	//finite state machine to detect steps
	public void stepCounter(float z, float y, float x)
	{
		if(Math.abs(z)>=0.1 && Math.abs(z)<0.15 && Math.abs(y)>0.1 && Math.abs(x)>0.02) {
			phase1= true;
			return;
		}

		if(Math.abs(z)>=0.15 && phase1 == true) {
			phase2 = true;
			return;
		}

		if(Math.abs(z)<=0.1 && phase1==true && phase2==true) {
			step++;
			phase1=false;
			phase2=false;
		}
	}

	//resets the number of steps
	public void reset() {
		step = 0;
	}

}
