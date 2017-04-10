package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class LogoWindow extends JWindow {
	private String imagePath = "resources/img/logo.jpg";
	public LogoWindow(double width, double height){
		
		init(width, height);
		this.setSize((int)width, (int)height);
	}
	
	private void init(double width, double height){
		Image image = null; 
		try {
			image = ImageIO.read(new File(imagePath));
			image = image.getScaledInstance((int) width, (int) height, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setBackground(new Color(206, 224, 255));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
	    c.gridx = 0;
	    c.gridy = 0;
	    c.gridwidth = 6;
	    c.gridheight = 4;
//	    c.anchor = ;
		this.add(new ImagePanel(image), c);
		
//		JLabel statusText = new JLabel("sdffffffffffffffffffff");			   
//	    c.fill = GridBagConstraints.HORIZONTAL;
//	    c.gridx = 0;
//	    c.gridy = 4;
//	    c.gridwidth = 5;
//	    c.gridheight = 1;
//		c.anchor = GridBagConstraints.LAST_LINE_START;
//		this.add(statusText, c);
//		
//		JButton exitBtn = new JButton("Abort");
//		exitBtn.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.exit(ABORT);				
//			}
//		});
//		c.gridx = 6;
//		c.gridy = 4;
//		c.gridwidth = 1;
//		c.anchor = GridBagConstraints.LAST_LINE_END;
//		this.add(exitBtn, c);
//		
	}
	
	public static class ImagePanel extends JPanel{
		Image image;
		
		public ImagePanel(Image image) {
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

	
	
	
}
