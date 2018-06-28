package com.myo.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.myo.controller.EmgData;

public class MainView extends JFrame implements Runnable {
	ResourceBundle bundle = ResourceBundle.getBundle("Translation");
	private static final long serialVersionUID = 2543084721802695565L;
	private EmgData mEmgData;
	JSplitPane mSplitPane;
	JPanel leftPanel;
	JPanel rightPanel;
	JTextField length;
	JTextField pause;
	JTextField classType;
	JTextField path;
	JTextField points;
	JButton start;
	JButton stop;

	public MainView(EmgData emgData) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		mEmgData = emgData;
		JFrame.setDefaultLookAndFeelDecorated(true);

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setTitle(bundle.getString("app.name"));
		setSize(1024, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setVisible(true);

		initializeLeftPanel();

		rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(4, 2));

		List<JPanel> charts = emgData.getPanels();
		for (int i = 0; i < charts.size(); i++) {
			rightPanel.add(charts.get(i));
		}

		mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mSplitPane.setLeftComponent(leftPanel);
		mSplitPane.setRightComponent(rightPanel);

		add(mSplitPane, BorderLayout.CENTER);
	}

	private void initializeLeftPanel() {
		leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());

		JPanel northPanel = new JPanel();

		length = new JTextField("2");
		pause = new JTextField("1");
		classType = new JTextField("1");
		path = new JTextField(System.getProperty("user.home") + "\\Desktop\\file.mat");
		points = new JTextField("100");
		start = new JButton(bundle.getString("button.start"));
		stop = new JButton(bundle.getString("button.stop"));

		northPanel.setLayout(new GridLayout(7, 2));
		northPanel.add(new JLabel(bundle.getString("settings.length")));
		northPanel.add(length);

		northPanel.add(new JLabel(bundle.getString("settings.pause")));
		northPanel.add(pause);

		northPanel.add(new JLabel(bundle.getString("settings.class")));
		northPanel.add(classType);

		northPanel.add(new JLabel(bundle.getString("settings.path")));
		northPanel.add(path);

		northPanel.add(new JLabel(bundle.getString("settings.points")));
		northPanel.add(points);

		northPanel.add(new JLabel());
		northPanel.add(new JLabel());

		northPanel.add(start);
		northPanel.add(stop);

		leftPanel.add(northPanel, BorderLayout.NORTH);

		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mEmgData.start();
			}
		});
		
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mEmgData.stop();
			}
		});
	}

	@Override
	public void run() {
		while (true) {
			mEmgData.calcultePoints();
		}
	}

}
