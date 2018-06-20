package com.myo.starter;

import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import com.myo.controller.EmgData;
import com.myo.view.MainView;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		EmgData emg = new EmgData();

		JFrame mainView = new MainView(emg);
		Thread thread = new Thread((Runnable) mainView);
		thread.start();

	}

}
