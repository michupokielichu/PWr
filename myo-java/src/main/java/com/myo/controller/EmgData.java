package com.myo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import com.myo.data.collector.EmgDataCollector;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.enums.StreamEmgType;

public class EmgData {
	static double seconds = 2;
	static double pointsPerSecond = 100;
	private static int CHANNELS_NUMBER = 8;
	public static double[] xData;
	public static double[][] yData;
	List<XYChart> charts;
	Hub hub;
	List<JPanel> panels;
	// SwingWrapper<XYChart> sw;
	DeviceListener dataCollector;

	public EmgData() {
		initialize();
	}

	private void initialize() {

		try {
			xData = new double[(int) (seconds * pointsPerSecond)];
			yData = new double[CHANNELS_NUMBER][(int) (seconds * pointsPerSecond)];

			hub = new Hub("com.example.emg-data-sample");

			System.out.println("Attempting to find a Myo...");
			Myo myo = hub.waitForMyo(10000);

			if (myo == null) {
				throw new RuntimeException("Unable to find a Myo!");
			}

			System.out.println("Connected to a Myo armband!");
			myo.setStreamEmg(StreamEmgType.STREAM_EMG_ENABLED);
			dataCollector = new EmgDataCollector();
			hub.addListener(dataCollector);
			for (int i = 0; i <= xData.length - 1; i++) {
				xData[i] = i;
			}
			double[][] initdata = new double[][] { new double[] { 0 }, new double[] { 0 } };
			charts = new ArrayList<XYChart>();
			panels = new ArrayList<>();
			for (int i = 0; i < CHANNELS_NUMBER; i++) {
				XYChart chart = QuickChart.getChart("The Myo - Elektromyography", "Time[s]", "EMG", "Channel " + i,
						initdata[0], initdata[1]);
				chart.getStyler().setYAxisMin((double) -120);
				chart.getStyler().setYAxisMax((double) 120);
				charts.add(chart);
				panels.add(new XChartPanel<XYChart>(chart));
			}
		} catch (Exception e) {
			System.err.println("Error: ");
			e.printStackTrace();
			System.exit(1);
		}
	}

	@SuppressWarnings("unchecked")
	public void calcultePoints() {
		hub.run((int) (1000 / pointsPerSecond));
		final double[][] data = getEMGData((EmgDataCollector) dataCollector);
		for (int i = 0; i < CHANNELS_NUMBER; i++) {
			charts.get(i).updateXYSeries("Channel " + i, xData, data[i], null);
			((XChartPanel<XYChart>) panels.get(i)).repaint();
		}
	}

	private static double[][] getEMGData(EmgDataCollector dataCollector) {
		double[] emgData = dataCollector.toArray();
		for (int i = 0; i < CHANNELS_NUMBER; i++) {
			System.arraycopy(yData[i], 0, yData[i], 1, yData[i].length - 1);
			yData[i][0] = emgData[i];
		}
		return yData;
	}

	public List<XYChart> getCharts() {
		return charts;
	}

	public List<JPanel> getPanels() {
		return panels;
	}

}