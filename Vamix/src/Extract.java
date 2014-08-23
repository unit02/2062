import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.text.MaskFormatter;
import javax.activation.MimetypesFileTypeMap;

public class Extract  extends JPanel {



	boolean isCanceled = false;
	JButton jbCancel = new JButton("Cancel");
	JTextField inputFile = new JTextField("",15);
	JTextField jtextInput = new JTextField("Input File Name");
	JTextField jtextOutput = new JTextField("Output File Name");
	JTextField outputBox = new JTextField("",15);
	JFormattedTextField startTime = new JFormattedTextField(createFormatText());
	JFormattedTextField timeInterval = new JFormattedTextField(createFormatText());
	


	JTextField userInfo = new JTextField("");

	JProgressBar pb = new JProgressBar();
	int prog = 0;


	public void extraction( final JTabbedPane gui){	

		JPanel panel1 = new JPanel(false);
		panel1.setLayout(new GridLayout(1, 1));
		panel1.setPreferredSize(new Dimension(410, 200));
		gui.add("Extract", panel1 );


		SpringLayout layout = new SpringLayout();
		panel1.setLayout(layout);





		userInfo.setEditable(false);
		jtextInput.setEditable(false);


		panel1.add(jtextInput);
		jtextInput.setBorder(null);
		panel1.add(inputFile);
inputFile.setEditable(false);
		//set location of the text field labeling the user input
		layout.putConstraint(SpringLayout.WEST, jtextInput, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, jtextInput, 25, SpringLayout.NORTH, panel1);

		//set location of the user input field for the url
		layout.putConstraint(SpringLayout.WEST, inputFile, 26, SpringLayout.EAST, jtextInput);
		layout.putConstraint(SpringLayout.NORTH, inputFile, 25, SpringLayout.NORTH, panel1);		

		
		final JButton jb = new JButton("Choose");



		jb.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String fileType = "";
				//Create a file chooser
				final JFileChooser fc = new JFileChooser();
				//In response to a button click:
				int returnVal = fc.showOpenDialog(jb);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					 File file = fc.getSelectedFile();
					//gets file type
					 Path source = Paths.get(file.getAbsolutePath());
					    try {
							 fileType = (Files.probeContentType(source));
							//check that the file type contains audio in it
								if( !fileType.contains("audio")){
									userInfo.setText("Please choose an audio file");
								} else{
									inputFile.setText(file.getName());
								}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}		
			}
		});
		
		panel1.add(jb);
		//set location of the user input field for the url
				layout.putConstraint(SpringLayout.WEST, jb, 10, SpringLayout.EAST, inputFile);
				layout.putConstraint(SpringLayout.NORTH, jb, 21, SpringLayout.NORTH, panel1);		

		
		
		jtextOutput.setEditable(false);
		panel1.add(jtextOutput);
		jtextOutput.setBorder(null);
		panel1.add(outputBox);

		//set location of the text field labeling output file name
		layout.putConstraint(SpringLayout.WEST, jtextOutput, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, jtextOutput, 25, SpringLayout.NORTH, inputFile);

		//set location of the user input field for the output file name
		layout.putConstraint(SpringLayout.WEST, outputBox, 15, SpringLayout.EAST, jtextOutput);
		layout.putConstraint(SpringLayout.NORTH, outputBox, 25, SpringLayout.NORTH, inputFile);		



		
		startTime.setText("000000");
		timeInterval.setText("000000");
		startTime.setPreferredSize(new Dimension(62,20));
		timeInterval.setPreferredSize(new Dimension(62,20));
		panel1.add(startTime);
		panel1.add(timeInterval);

		JTextField jtextStart = new JTextField("Start Time");
		JTextField jtextEnd = new JTextField("End Time");
		panel1.add(jtextStart);
		jtextStart.setBorder(null);

		panel1.add(jtextEnd);
		jtextEnd.setBorder(null);


		startTime.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent fEvt) {
				JTextField tField = (JTextField)fEvt.getSource();
				tField.selectAll();
			}
		});


		timeInterval.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent fEvt) {
				JTextField tField = (JTextField)fEvt.getSource();
				tField.selectAll();
			}
		});

		jtextStart.setEditable(false);
		jtextEnd.setEditable(false);

		//set location of the text field labeling start time
		layout.putConstraint(SpringLayout.WEST, jtextStart, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, jtextStart, 25, SpringLayout.NORTH, outputBox);

		//set location of the user input field for the start time
		layout.putConstraint(SpringLayout.WEST, startTime, 15, SpringLayout.EAST, jtextStart);
		layout.putConstraint(SpringLayout.NORTH, startTime, 25, SpringLayout.NORTH, outputBox);		


		//set location of the text field labeling end time
		layout.putConstraint(SpringLayout.WEST, jtextEnd, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, jtextEnd, 25, SpringLayout.NORTH, startTime);

		//set location of the user input field for the end time
		layout.putConstraint(SpringLayout.WEST, timeInterval, 21, SpringLayout.EAST, jtextEnd);
		layout.putConstraint(SpringLayout.NORTH, timeInterval, 25, SpringLayout.NORTH, startTime);		




		panel1.add(userInfo);
		userInfo.setBorder(null);
		layout.putConstraint(SpringLayout.WEST, userInfo, 15, SpringLayout.EAST, panel1);
		layout.putConstraint(SpringLayout.NORTH, userInfo, 25, SpringLayout.NORTH, timeInterval);		



		userInfo.setPreferredSize(new Dimension(250,20));
		JPanel newPane = createExtractButton();
		panel1.add(newPane);
		newPane.setLocation(50,500);

		//set the location of the information text box
		layout.putConstraint(SpringLayout.WEST, userInfo, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, userInfo, 25, SpringLayout.NORTH, timeInterval);

		//set the location of the download button
		layout.putConstraint(SpringLayout.WEST, newPane, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, newPane, 25, SpringLayout.NORTH, userInfo);


		panel1.add(pb);
		pb.setVisible(false);
		//set the location of the progress bar
		layout.putConstraint(SpringLayout.WEST, pb, 15, SpringLayout.WEST, panel1);
		layout.putConstraint(SpringLayout.NORTH, pb, 30, SpringLayout.NORTH, newPane);


	}

	MaskFormatter createFormatText(){
		MaskFormatter mf1 = null;
		try {
			mf1 = new MaskFormatter("##:##:##");
		} catch (ParseException e1) {

			e1.printStackTrace();
		}
		return mf1;
	}

	protected JPanel createExtractButton() {

		JPanel panel = new JPanel(new BorderLayout());
		JButton jbExtract = new JButton("Extract");
	





	

		jbExtract.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ExtractSwingWorker sw = new ExtractSwingWorker();

				String inputName = "Zapac_-_Test_Drive.mp3";
				//String inputName = inputFile.getText();
				String outputName = outputBox.getText();
				

				File f = new File(outputName);
				if(f.exists() && !f.isDirectory()) { 
					userInfo.setText("File already exists!");
					return;
				}

				else {
				
					//else file does not exist and we can download
					sw.execute();
				} // end else

			}//end action performed
		}); // end add action listener



		//add the download button to the component
		panel.add(jbExtract,BorderLayout.LINE_START);

		return panel;

	}

	private class ExtractSwingWorker extends SwingWorker<Integer,String>{

		protected void done(){

		
				userInfo.setText("Extraction has completed!");
			

		}


		@Override
		protected Integer doInBackground() throws Exception {
			//String URLname = inputFile.getText();
			String URLname = "http://ccmixter.org/content/Zapac/Zapac_-_Test_Drive.mp3";
			
			//creates the process for avconv
			//avconv -ss $startTime -t $timeInterval -i $inputfile $newName 
			ProcessBuilder builder = new ProcessBuilder("avconv", "ss",startTime.getText(),"-t",timeInterval.getText(),"-i",outputBox.getText());
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
