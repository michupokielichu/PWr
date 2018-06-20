package com.myo.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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

		northPanel.setLayout(new GridLayout(6, 2));
		northPanel.add(new JLabel(bundle.getString("settings.length")));
		northPanel.add(new JTextField("2"));

		northPanel.add(new JLabel(bundle.getString("settings.pause")));
		northPanel.add(new JTextField("1"));

		northPanel.add(new JLabel(bundle.getString("settings.class")));
		northPanel.add(new JTextField("1"));
		northPanel.add(new JLabel(bundle.getString("settings.path")));
		northPanel.add(new JTextField(System.getProperty("user.home") + "\\Desktop\\file.mat"));

		northPanel.add(new JLabel());
		northPanel.add(new JLabel());

		northPanel.add(new JLabel());
		northPanel.add(new JButton("Start"));

		leftPanel.add(northPanel, BorderLayout.NORTH);
	}

	@Override
	public void run() {
		while (true) {
			mEmgData.calcultePoints();
		}
	}

}
