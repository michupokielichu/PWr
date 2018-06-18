package com.myo.starter;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.enums.StreamEmgType;

public class EmgDataSample {
	static double seconds = 2;
	static double pointsPerSecond = 100;
	private static int CHANNELS_NUMBER = 8;
	public static double[] xData;
	public static double[][] yData;

	public static void main(String[] args) {
		try {
			xData = new double[(int) (seconds * pointsPerSecond)];
//			for (int i = 0; i < CHANNELS_NUMBER; i++) {
				yData = new double[CHANNELS_NUMBER][(int) (seconds * pointsPerSecond)];
//			}

			Hub hub = new Hub("com.example.emg-data-sample");

			System.out.println("Attempting to find a Myo...");
			Myo myo = hub.waitForMyo(10000);

			if (myo == null) {
				throw new RuntimeException("Unable to find a Myo!");
			}

			System.out.println("Connected to a Myo armband!");
			myo.setStreamEmg(StreamEmgType.STREAM_EMG_ENABLED);
			DeviceListener dataCollector = new EmgDataCollector();
			hub.addListener(dataCollector);
			for (int i = 0; i <= xData.length - 1; i++) {
				xData[i] = i;
			}
			double[][] initdata = new double[][] { new double[] { 0 }, new double[] { 0 } };
			// Create Chart
			List<XYChart> charts = new ArrayList<XYChart>();

			for (int i = 0; i < CHANNELS_NUMBER; i++) {
				XYChart chart = QuickChart.getChart("The Myo - Elektromyography", "Time[s]", "EMG", "Channel " + i,
						initdata[0], initdata[1]);
				chart.getStyler().setYAxisMin((double) -120);
				chart.getStyler().setYAxisMax((double) 120);
				charts.add(chart);

			}
			SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(charts);

			sw.displayChartMatrix();
			while (true) {
				hub.run((int) (1000 / pointsPerSecond));
				final double[][] data = getEMGData((EmgDataCollector) dataCollector);
				for (int i = 0; i < CHANNELS_NUMBER; i++) {
					charts.get(i).updateXYSeries("Channel " + i, xData, data[i], null);
					sw.repaintChart(i);
				}


			}
		} catch (Exception e) {
			System.err.println("Error: ");
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static double[][] getEMGData(EmgDataCollector dataCollector) {
		double[] emgData = dataCollector.toArray();
		for (int i = 0; i < CHANNELS_NUMBER; i++) {
			System.arraycopy(yData[i], 0, yData[i], 1, yData[i].length - 1);
			yData[i][0] = emgData[i];				
		}
		return yData ;
	}

}