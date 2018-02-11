package gui.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import calculator.App;
import domains.Fields;
import domains.Machinery;
import domains.Points;
import logginig.AbstractLogger.LogLevel;
import sqlutils.DBHelper;
import tools.Config;
import tools.IOTools;
import logginig.LogListener;
import logginig.Logger;
import tools.Parameter;

public class WindowLogo extends JWindow implements ActionListener {
	private final String imagePath = Config.getString(Parameter.APP_LOGO_IMAGE);
	private StatusLabel label;
	
	private static final Logger logger = Logger.getLogger(WindowLogo.class);
	
	public WindowLogo(){

		int width = App.dim.width*2/5;
		int height = App.dim.height*2/5;
		
		initIface(width, height);		
		this.setLocation((App.dim.width/2) - width/2, (App.dim.height/2) - height/2);		
		this.setSize(width, height);
		this.setVisible(true);

		if(Config.getBoolean(Parameter.APP_RECREATE_DB)){
			DBHelper.dropDB();
		}

		try {
			initApplication();
		} catch (RuntimeException e) {
			logger.info("Application failed to start: " + e.getMessage());
		}
	}
	
	private void initApplication() throws RuntimeException {
		Logger.subscribe(label);

		try {
			logger.info("Starting application");
			DBHelper.checkDB();

			logger.info("Loading database");
			new Points();
			new Fields();
			new Machinery();
		} catch (SQLException | IOException e){
			throw new RuntimeException(e);
		}
		
		logger.info("Application data ready");

		/*
		 * Прошу не змінювати і не видаляти наступний текст(копірайт) без відома автора.
		 * Це все ж таки моя робота, зроблена з альтруістичних міркувань.
		 * Користуйтеся нею на на здоров`я. Буду дуже радий якщо ця робота принесе користь.
		 * З повагою, Олексій
		 */
		logger.info("Developed by Oleksii Polishchuk");
		Logger.unsubscribe(label);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.setVisible(false);
		WindowMain mw= new WindowMain();
		mw.setSize(App.dim.width*3/4, App.dim.height*3/4);
		mw.setLocationByPlatform(true);
		EventQueue.invokeLater(() -> mw.setVisible(true));
	}

	private void initIface(double width, double height){
		this.setLayout(new BorderLayout());
		
		Image image = IOTools.readImageFromUrl(imagePath);
		if (image != null) {
			image = image.getScaledInstance((int) width, (int) height, Image.SCALE_SMOOTH);
			this.add(new ImagePanel(image), BorderLayout.CENTER);
		}


		JPanel panel = new JPanel();
		panel.setBackground(new Color(237, 237, 237));
		
		JButton button = new JButton("Abort");
		label = new StatusLabel();
		label.setPreferredSize(new Dimension((int)( width - button.getPreferredSize().getWidth()-10), (int )label.getPreferredSize().getHeight()));
		panel.add(label);
		panel.add(button);

		this.add(panel, BorderLayout.SOUTH);
		
		button.addActionListener(this);
	}
	
	static class ImagePanel extends JPanel{
		final Image image;
		
		ImagePanel(Image image) {
			super();
			this.image = image;
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if(image != null){
				g.drawImage(image, 0, 0, this);
			}
		}
	}
	
	public static class StatusLabel extends JLabel implements LogListener{

		StatusLabel() {
			super("Component initialized");
			this.setFont(new Font("Consolas", Font.PLAIN, 16));
		}

		@Override
		public void update(LogLevel level, Class<?> clazz, String message) {
			this.setText(message);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().getClass().isAssignableFrom(JButton.class)){
			System.exit(ABORT);
		}
		
	}
}
