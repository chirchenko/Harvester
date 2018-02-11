package gui.window;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import calculator.App;
import tools.Config;
import tools.IOTools;
import tools.Parameter;

@SuppressWarnings("serial")
class WindowAbout extends JFrame {

	public WindowAbout() throws HeadlessException {
		
		setSize(App.dim.width*2/5, App.dim.height*2/5);
		setLocationByPlatform(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setLayout(new BorderLayout());	
		setResizable(false);
		
		JPanel left = new JPanel();
		left.setLayout(new GridBagLayout());
		JLabel aboutLabel = new JLabel("Harvester");
		aboutLabel.setFont(new Font("Consolas", Font.BOLD, 16));
		aboutLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		
		BufferedImage icon = IOTools.readImageFromUrl(Config.getString(Parameter.APP_ICON_PATH));
    	setIconImage(icon); 

		JLabel iconLabel;
        if(icon != null){
        	iconLabel = new JLabel(new ImageIcon(icon));
        }else{
        	iconLabel = new JLabel(){
		        @Override
		        public void paint(Graphics g){
        			g.drawLine(0, 0, 64, 64);
        			g.drawLine(64, 0, 0, 64);
        		}
        	};
        }
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        left.add(aboutLabel, c);
        
        c.gridy = 1;
        left.add(iconLabel, c);
        
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(new BevelBorder(BevelBorder.LOWERED));
        
        JTextArea text = new JTextArea();
        text.setBackground(left.getBackground());
        text.setEditable(false);
        
        String str = "Design and development by Oleksii Polishchuk\n"
        		   + "Idea and scientific supervising by Dmytro Chyrchenko\n"
        		   + "\n"
        		   + "This project was created as part of D. Chyrchenko's PhD\n"
        		   + "All rights reserved";
        text.setText(str);
        right.add(text);
        
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);
        
	}
	
	public void display(){
		setVisible(true);
	}
}
