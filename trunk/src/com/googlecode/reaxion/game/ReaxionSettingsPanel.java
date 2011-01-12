package com.googlecode.reaxion.game;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jme.system.GameSettings;
import com.jme.system.jogl.JOGLSystemProvider;
import com.jme.system.lwjgl.LWJGLSystemProvider;

/**
 * Modified version of {@code GameSettingsPanel}, with customized options for
 * Reaxion.
 * @author Khoa Ha
 *
 */
public class ReaxionSettingsPanel extends JPanel {
    private static final Logger logger = Logger
            .getLogger(ReaxionSettingsPanel.class.getName());
    
	private static final long serialVersionUID = 1L;

	public static final int[][] RESOLUTIONS = {
			{640, 480},
			{800, 600},
			{960, 540},
			{1024, 768},
			{1280, 720},
			{1280, 1024},
			{1440, 900},
			{1920, 1080}
		};
	public static final int[] DEPTHS = {
			16,
			24,
			32
		};

	private GameSettings settings;

	private GridBagLayout layout;
	private GridBagConstraints constraints;

	private JComboBox renderer;
	private JComboBox resolution;
	private JComboBox fullscreen;
	private JComboBox music;
	private JComboBox sfx;
	private JComboBox samples;

	private HashMap<String, JComboBox> map;
	private HashMap<String, Object> defaults;

	public ReaxionSettingsPanel(GameSettings settings) {
		this.settings = settings;

		map = new HashMap<String, JComboBox>();
		defaults = new HashMap<String, Object>();
		init();
	}

	private void init() {
		layout = new GridBagLayout();
		setLayout(layout);
		constraints = new GridBagConstraints();

		List<Component> list = getSettingsComponents();
		revert();
		JLabel label = null;
		for (int i = 0; i < list.size(); i++) {
			Component c = list.get(i);
			label = new JLabel(" " + c.getName() + ": ");
			label.setHorizontalAlignment(SwingConstants.RIGHT);

			constraints.gridwidth = 1;
			constraints.anchor = GridBagConstraints.EAST;
			constraints.insets = new Insets(5, 5, 5, 5);
			layout.setConstraints(label, constraints);
			add(label);

			constraints.anchor = GridBagConstraints.WEST;
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			layout.setConstraints(c, constraints);
			add(c);
		}
	}

	public void addSetting(String name, Object[] choices, Object defaultChoice) {
		defaultChoice = settings.getObject(name, defaultChoice);
		logger.info("Default Choice for " + name + " = " + defaultChoice);
		
		JComboBox c = new JComboBox(choices);
		c.setName(name);
		c.setSelectedItem(defaultChoice);

		JLabel label = new JLabel(" " + c.getName() + ": ");
		label.setHorizontalAlignment(SwingConstants.RIGHT);

		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(5, 5, 5, 5);
		layout.setConstraints(label, constraints);
		add(label);

		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(c, constraints);
		add(c);

		map.put(name, c);
		defaults.put(name, defaultChoice);
	}

	protected List<Component> getSettingsComponents() {
		List<Component> components = new ArrayList<Component>();
		components.add(createRenderer());
		components.add(createResolution());
		components.add(createFullscreen());
		components.add(createMusic());
		components.add(createSFX());
		components.add(createSamples());
		return components;
	}

	protected Component createRenderer() {
		renderer = new JComboBox(new Object[] { LWJGLSystemProvider.LWJGL_SYSTEM_IDENTIFIER,
		                                        JOGLSystemProvider.SYSTEM_IDENTIFIER});
		renderer.setName("Renderer");
		return renderer;
	}

	protected Component createResolution() {
		resolution = new JComboBox(getResolutionArray());
		resolution.setName("Resolution");
		return resolution;
	}

	/**
	 * Gets the available modes based on the set of modes for the system and a
	 * resolution.
	 * 
	 * @param theModes
	 * @param width
	 * @param height
	 * @return
	 */
	public static Vector<DisplayMode> getAvailableModesRes(DisplayMode[] theModes, int width, int height) {
		Vector<DisplayMode> modes = new Vector<DisplayMode>();

		for (int[] res : RESOLUTIONS) {
			if (res[0] == width && res[1] == height) {
				for (DisplayMode aMode : theModes) {
					if (aMode.getHeight() == height
							&& aMode.getWidth() == width) {
						modes.add(aMode);
					}
				}
			}
		}
		return modes;
	}

	public static Object[] getResolutionArray() {
		Object[] resolutions = new Object[RESOLUTIONS.length];
		for (int i = 0; i < resolutions.length; i++) {
			resolutions[i] = RESOLUTIONS[i][0] + "x" + RESOLUTIONS[i][1];
		}
		return resolutions;
	}

	public static Object[] getDepthArray() {
		Object[] depths = new Object[DEPTHS.length];
		for (int i = 0; i < depths.length; i++) {
			depths[i] = String.valueOf(DEPTHS[i]);
		}
		return depths;
	}

	protected Component createFullscreen() {
		fullscreen = new JComboBox(new Object[] { "Yes", "No" });
		fullscreen.setName("Fullscreen");
		return fullscreen;
	}

	protected Component createMusic() {
		music = new JComboBox(new Object[] { "Yes", "No" });
		music.setName("Music");
		return music;
	}

	protected Component createSFX() {
		sfx = new JComboBox(new Object[] { "Yes", "No" });
		sfx.setName("Sound Effects");
		return sfx;
	}

	protected Component createSamples() {
		samples = new JComboBox(new Object[] { "0", "2", "4" });
		samples.setName("Smoothing");
		return samples;
	}

	public void defaults() {
		try {
			settings.clear();
			revert();
		} catch(Exception exc) {
			logger.logp(Level.SEVERE, this.getClass().toString(), "defaults()", "Exception", exc);
		}
	}

	public void revert() {
		renderer.setSelectedItem(settings.getRenderer());
		resolution.setSelectedItem(settings.getWidth() + "x" + settings.getHeight());
		fullscreen.setSelectedItem(settings.isFullscreen() ? "Yes" : "No");
		music.setSelectedItem(settings.isMusic() ? "Yes" : "No");
		sfx.setSelectedItem(settings.isSFX() ? "Yes" : "No");
		samples.setSelectedItem(String.valueOf(settings.getSamples()));
		for (String name : map.keySet()) {
			JComboBox combo = map.get(name);
			combo.setSelectedItem(settings.getObject(name, defaults.get(name)));
		}
	}

	public void apply() {
		settings.setRenderer((String) renderer.getSelectedItem());
		String[] parser = ((String) resolution.getSelectedItem()).split("x");
		settings.setWidth(Integer.parseInt(parser[0]));
		settings.setHeight(Integer.parseInt(parser[1]));
		settings.setFullscreen(fullscreen.getSelectedItem().equals("Yes"));
		settings.setMusic(music.getSelectedItem().equals("Yes"));
		settings.setSFX(sfx.getSelectedItem().equals("Yes"));
		settings.setSamples(Integer.parseInt((String) samples.getSelectedItem()));
		for (String name : map.keySet()) {
			settings.setObject(name, map.get(name).getSelectedItem());
		}
	}
	
	public boolean validateDisplay() {
		return true;
	}

	private static boolean ok;
	
	public static final boolean prompt(GameSettings settings) throws InterruptedException {
		return prompt(settings, "Reaxion Settings");
	}
	
	public static final boolean prompt(GameSettings settings, String title) throws InterruptedException {
		final JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setAlwaysOnTop(true);

		final ReaxionSettingsPanel panel = new ReaxionSettingsPanel(settings);

		ok = false;
		
		ActionListener buttonListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JButton b = (JButton) evt.getSource();
				if ("Defaults".equals(b.getText())) {
					panel.defaults();
				} else if ("Revert".equals(b.getText())) {
					panel.revert();
				} else if ("OK".equals(b.getText())) {
					if (panel.validateDisplay()) {
						ok = true;
						panel.apply();
						frame.dispose();
					} else {
						JOptionPane.showMessageDialog(frame, "Invalid display configuration combination", "Invalid Settings", JOptionPane.ERROR_MESSAGE);
					}
				} else if ("Cancel".equals(b.getText())) {
					frame.dispose();
				}
			}
		};

		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		JButton b = new JButton("Defaults");
		b.addActionListener(buttonListener);
		bottom.add(b);
		b = new JButton("Revert");
		b.addActionListener(buttonListener);
		bottom.add(b);
		b = new JButton("OK");
		b.addActionListener(buttonListener);
		bottom.add(b);
        frame.getRootPane().setDefaultButton(b);
        b = new JButton("Cancel");
		b.addActionListener(buttonListener);
		bottom.add(b);

		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(BorderLayout.CENTER, panel);
		c.add(BorderLayout.SOUTH, bottom);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		// Wait for finish before returning
		while (frame.isVisible()) {
			Thread.sleep(50);
		}
		return ok;
	}
}