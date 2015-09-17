package com.example.heartpatientsimulator;

import java.io.InputStream;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class Graph extends Activity {
	final static int PULSE_WIDTH = 100;
	final static double PULSE_COUNT_THEADSHOLD = 1.5;
	final static double FFT_WINDOWS_LEN = 1024;
	private int pulseCountAcc = 0;
	private double tempPulseLockVal = 0d;
	private final Handler mHandler = new Handler();
	private Runnable mTimer1;
	private Runnable mTimer2;
	private GraphView graphView;
	private GraphViewSeries exampleSeries1; 
	private GraphViewSeries exampleSeries2;
	Button plottingControl;
	private double XValue=0;
	private double YValue=0;
	
	private boolean isPlot = true;
	private boolean isPulseChecklock = false;
	int n = 0;
	
	static String[] signalDataArr;
	static String inputPath;
	
	InputStream inFile = null;
    String[] index = null;
    
    public void plottingControl(View view){
  		if(isPlot == true){
  			isPlot = false;
  			plottingControl.setText("Start");
  		}
  		else if(isPlot == false){
  			isPlot = true;
  			plottingControl.setText("Stop");
  		}
  		else{
  			isPlot = false;
  			plottingControl.setText("Start");
  		}
  	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph);
		
		inputPath = "1.txt";
		InputStream input;
		AssetManager assetManager = getAssets();
		
		plottingControl = (Button) findViewById(R.id.StartStop);
		
	    try{
	    	input=assetManager.open(inputPath);
	    	int size =input.available();
	    	byte[] buffer = new byte [size];
	    	input.read(buffer);
	    	input.close();
	    	String text = new String(buffer);
	    	signalDataArr = text.split("\n");
	    
	    }catch(Exception e){
	    	Log.d("specGram2", "Expection=" +e);
	    }
	    Log.d("audioBuf size","Expection= "+ signalDataArr.length);
		
    exampleSeries1 = new GraphViewSeries(new GraphViewData[] {
			    new	GraphViewData (0, -.241d)
	});
	
	exampleSeries1.getStyle().color = Color.GREEN;
		 
	 graphView = new LineGraphView(
		      this // context
		      , "ECG" // heading
		); 
		
		graphView.addSeries(exampleSeries1); // data
		graphView.setViewPort(0, 2300);
		graphView.setManualYAxisBounds(.399, -.625);
		graphView.setHorizontalScrollBarEnabled(true);
		graphView.setScalable(true); 
		graphView.setScrollable(true);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.GRAY);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.GRAY);
		LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
		layout.addView(graphView);
		
		exampleSeries2 = new GraphViewSeries(new GraphViewData[] {
			    new	GraphViewData (0, 0d)
	});
	
	exampleSeries2.getStyle().color = Color.GREEN;
		 
	 graphView = new LineGraphView(
		      this // context
		      , "ECG" // heading
		); 
		
		graphView.addSeries(exampleSeries2); // data
		graphView.setViewPort(0, 1.5);
		graphView.setManualYAxisBounds(30, 0);
		graphView.setHorizontalScrollBarEnabled(true);
		graphView.setScalable(true); 
		graphView.setScrollable(true);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.GRAY);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.GRAY);
		layout = (LinearLayout) findViewById(R.id.graph);
		layout.addView(graphView);
	}
	
	@Override
	protected void onPause() {
		mHandler.removeCallbacks(mTimer1);
		mHandler.removeCallbacks(mTimer2);
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume(); 
		mTimer1 = new Runnable() {
			@Override
			public void run() {
				if(isPlot == true){
				if((n < signalDataArr.length) && (n + PULSE_WIDTH < signalDataArr.length)){
					for (int k = 0; k < PULSE_WIDTH; k = k + 1){
						XValue += 1d;
						YValue = Double.parseDouble(signalDataArr[
						                                          n+k]);
						exampleSeries1.appendData(new GraphViewData(XValue, YValue), true,230000);
						
						if((YValue >=PULSE_COUNT_THEADSHOLD) && (isPulseChecklock == false)){
							tempPulseLockVal = YValue;
							isPulseChecklock = true;
						}
						else if((YValue <= tempPulseLockVal) && (isPulseChecklock == true)){
							pulseCountAcc++;
							isPulseChecklock = false;
						}
						else{
						}
					}
					n = n + PULSE_WIDTH;	
				}
				else{
					n = 0;
				}
			}
				mHandler.postDelayed(this, 200);//speed (lower the faster)
			}
	};
		mHandler.postDelayed(mTimer1, 1); //timer to display top graph (larger the longer it takes)
	
		mTimer2 = new Runnable() {
			@Override
			public void run() {
				XValue += 1d;
				exampleSeries2.appendData(new GraphViewData (XValue,29d), true, 100);
				exampleSeries2.getStyle().color = Color.GREEN;
				mHandler.postDelayed(this, 800); //speed of bottom graph (lower the number the faster)
			}
		};
		mHandler.postDelayed(mTimer2, 1);
	}
 }
