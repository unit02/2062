import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;





public class Download extends JPanel {
	
	
	boolean isCanceled = false;
	 JButton jbCancel = new JButton("Cancel");
	JCheckBox trueCheck = new JCheckBox("Open Source", false);
	JTextField urlInput = new JTextField("",15);
	JTextField jtextInput = new JTextField("Input URL");
	JTextField userInfo = new JTextField("");

	JProgressBar pb = new JProgressBar();
	int prog = 0;
	public void download( final JTabbedPane gui){	


		JPanel panel1 = new JPanel(false);
		panel1.setLayout(new GridLayout(1, 1));
		panel1.setPreferredSize(new Dimension(410, 200));
		gui.add("Download", panel1 );
		

		SpringLayout layout = new SpringLayout();
		panel1.setLayout(layout);

		userInfo.setEditable(false);
		jtextInput.setEditable(false);

		panel1.add(jtextInput);
		jtextInput.setBorder(null);
		panel1.add(urlInput);
		
		//set location of the text field labeling the user input
		layout.putConstraint(SpringLayout.WEST, jtextInput, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, jtextInput, 25, SpringLayout.NORTH, panel1);
		
		//set location of the user input field for the url
		layout.putConstraint(SpringLayout.WEST, urlInput, 15, SpringLayout.EAST, jtextInput);
		layout.putConstraint(SpringLayout.NORTH, urlInput, 25, SpringLayout.NORTH, panel1);		

		panel1.add(trueCheck);
		trueCheck.setBorder(null);
		panel1.add(userInfo);
		userInfo.setBorder(null);
		userInfo.setPreferredSize(new Dimension(250,20));
		JPanel newPane = createDownloadButton();
		panel1.add(newPane);
		newPane.setLocation(50,500);

		//set the location of the open source check box
		layout.putConstraint(SpringLayout.WEST, trueCheck, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, trueCheck, 25, SpringLayout.NORTH, jtextInput);

		//set the location of the information text box
		layout.putConstraint(SpringLayout.WEST, userInfo, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, userInfo, 25, SpringLayout.NORTH, trueCheck);

		//set the location of the download button
		layout.putConstraint(SpringLayout.WEST, newPane, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, newPane, 25, SpringLayout.NORTH, userInfo);


		panel1.add(pb);
		pb.setVisible(false);
		//set the location of the progress bar
		layout.putConstraint(SpringLayout.WEST, pb, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, pb, 30, SpringLayout.NORTH, newPane);




	}


	protected JPanel createDownloadButton() {
		
		JPanel panel = new JPanel(new BorderLayout());
		JButton jbDownload = new JButton("Download");
		
		
		jbCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				isCanceled = true;
				
			}
			
			
		});
		
		
		
		
		panel.add(jbCancel,BorderLayout.LINE_END);

		jbCancel.setVisible(false);

		jbDownload.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DownloadSwingWorker sw = new DownloadSwingWorker();
				if(!trueCheck.isSelected()){
					userInfo.setText("Please only download open source!");
					return;
				}
				String outputName = null;
				String input = urlInput.getText();
				if (input.length() == 0){
					userInfo.setText("Please enter a URL");
					return;
				}
				else {
					
				
					ProcessBuilder builder = new ProcessBuilder("basename", urlInput.getText());
					Process process = null;
				
						try {
							process = builder.start();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						InputStream stdout = process.getInputStream();
						InputStream stderr = process.getErrorStream();
						BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
						String line = null;
						
						try {
							outputName = stdoutBuffered.readLine();
						
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
	
				
				
				
			
				
						//outputName = getFileName(input);
						//System.out.print(input);
					
					//outputName = input.substring( input.lastIndexOf('/')+1, input.length() );
				}
				File f = new File(outputName);
				if(f.exists() && !f.isDirectory()) { 
					userInfo.setText("File already exists!");
					return;
				}
				else {
					//set the size of the progress bar
					pb.setPreferredSize(new Dimension(250,20));	
					//make the progress bar appear on screen
					pb.setVisible(true);
					//make the cancel button appear
					jbCancel.setVisible(true);
					//set isCancwlled to false
					isCanceled = false;
					//else file does not exist and we can download
					sw.execute();
				} // end else

			}//end action performed
		}); // end add action listener



		//add the download button to the component
		panel.add(jbDownload,BorderLayout.LINE_START);

		return panel;

	}

	private class DownloadSwingWorker extends SwingWorker<Integer,String>{

		protected void done(){
			
			if (isCanceled) {
				pb.setBackground(Color.PINK);
				pb.setForeground(Color.PINK);
				userInfo.setText("Download was cancelled");
			
				pb.setVisible(false);
				pb.setBackground(null);
				jbCancel.setVisible(false);
			}
			else{
				userInfo.setText("Download has completed!");
			}
			
		}


		@Override
		protected Integer doInBackground() throws Exception {
			//String URLname = urlInput.getText();
			String URLname = "http://ccmixter.org/content/Zapac/Zapac_-_Test_Drive.mp3";
			prog = 0;
			//creates the process for wget
			ProcessBuilder builder = new ProcessBuilder("wget", "--progress=dot",URLname);
			Process process = builder.start();

			//redirects input and error streams
			InputStream stdout = process.getInputStream();
			InputStream stderr = process.getErrorStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stderr));
			String line = null;

			//sends the dot progress bar to the process function for processing
			while ((line = stdoutBuffered.readLine()) != null ) {	
				if (isCanceled) {
					process.destroy();
				}
				publish(line);
			}
			return null;
		}

		@Override
		protected void process(List<String> chunks) {
			//updates progress bar for each dot 
			for(String chu:chunks){
				prog++;
				pb.setValue(prog);
			}
			


		}

	}
	
	
	
}
