package com.myo.view;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.myo.controller.EmgData;

public class MainView extends JFrame implements Runnable {
	private static final long serialVersionUID = 2543084721802695565L;
	private EmgData mEmgData;

	public MainView(EmgData emgData) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(4, 2));
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

		setVisible(true);
		mEmgData = emgData;

		List<JPanel> charts = emgData.getPanels();
		for (int i = 0; i < charts.size(); i++) {
			add(charts.get(i));
		}

	}

	@Override
	public void run() {
		while (true) {
			mEmgData.calcultePoints();
		}
	}

}
