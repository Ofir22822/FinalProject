package application;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;

public class HomePageController {

	public int countText = 1;
	
	@FXML
	private Button btnFile;

	@FXML
	private Button btnWebsite;

	@FXML
	private ListView<?> listSubjects;

	@FXML
	private WebView wvView;

	@FXML
	private TextField tbfilePath;

	@FXML
	private Button btnAnalyze;

	@FXML
	private TextArea tbText;

    @FXML
    private Label lblSubject;
    
    @FXML
    private Label lblClassification;
    
    @FXML
    private Button btnAddToTS;

    @FXML
    private Label lblAddMsg;
    
    @FXML
    private Label lblClassificationWords;
    
    private String[][] sentimentFeatures;
    private String fileName;
	
	@FXML
	void openFile(MouseEvent event) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", ".pdf");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			tbfilePath.setText(selectedFile.getAbsolutePath());
		}

	}

	@FXML
	void openWebsiteClick(MouseEvent event) {

		wvView.getEngine().load("http://google.com");

		// open website in browser
		/*
		 * String url = "http://www.google.com";
		 * 
		 * if (Desktop.isDesktopSupported()) { Desktop desktop = Desktop.getDesktop();
		 * try { desktop.browse(new URI(url)); } catch (IOException | URISyntaxException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); } } else {
		 * Runtime runtime = Runtime.getRuntime(); try {
		 * runtime.exec("/usr/bin/firefox -new-window " + url);
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 */
	}

	File createTextFile(String text) {
		
		File textFile = null;
		String[] textLine = text.split("\\. "); //split text at dot - .
		File countFile = new File("webTexts\\counter.txt");
		try {
			//read text counter
			Scanner myReader = new Scanner(countFile);
			countText = Integer.parseInt(myReader.nextLine());
			myReader.close();
			//write text to file "wi.txt"
			FileWriter myWriter = new FileWriter("webTexts\\w"+countText+".txt");
			
			//write to text counter +1 to number
			countText++;
			FileWriter countWriter = new FileWriter("webTexts\\counter.txt");
			countWriter.write(""+countText);
			countWriter.close();
			
			//write lines from text in file
			for(String line : textLine)
			{
				System.out.println(line);
				myWriter.write(line+".\n");
			}
			myWriter.close();
			textFile = new File("webTexts\\w"+(--countText)+".txt");

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return textFile;
	}
	
	@FXML
	void clickAnalyze(MouseEvent event) {
		String text = tbText.getText();			//get text from textarea
		File textFile = createTextFile(text);
		fileName = textFile.getName();
		if(textFile!=null) {		
			sentimentFeatures = SentiStrengthController.getSentimentFeature(textFile);
			String subject = TextRazorController.getTextSubject(textFile);			
			
			lblSubject.setText(subject);
    		if(!TextRazorController.checkExistSubject(subject))
    		{
    			Alert alert=new Alert(AlertType.INFORMATION); 
    			alert.setTitle("Subject Result");
    			alert.setHeaderText("subject not Exist");
    			alert.showAndWait();   			
    		}
    		
    		textObject txtObject = new textObject();
    		txtObject.setSentimentFeatureSentence(SentiStrengthController.sentimentMatrixToString(sentimentFeatures,25).replace(' ', ',')+"P");
    		txtObject.setSentimentFeatureWord(SentiStrengthController.getTextWordsSentiment(textFile).replace(' ', ',')+"P");
    		txtObject.setSubject(subject);
    		
    		boolean isSubjectExist = TextRazorController.checkExistSubject(subject);
    		if(isSubjectExist == true)
    		{
    			//predict or knn with texts of same subject			
    			int i = 1;
    		}
    		else
    		{
    			//predict or knn with texts of different subject
    			int i = 0;
    		}  
    		
    		KNNController.setKNNData("sentimentDataSentence.txt", txtObject);
    		double KNNresult = KNNController.KNN();
    		
    			if(KNNresult == -1.0)
    				lblClassification.setText("Error");
    			else if(KNNresult == 0.0)
    				lblClassification.setText("Promotion");
    			else
    				lblClassification.setText("Personal Experience");
    			
    		KNNController.setKNNData("sentimentDataWord.txt", txtObject);
    		KNNresult = KNNController.KNN();
    		
			if(KNNresult == -1.0)
				lblClassificationWords.setText("Error");
			else if(KNNresult == 0.0)
				lblClassificationWords.setText("Promotion");
			else
				lblClassificationWords.setText("Personal Experience");
		}
		
		Alert alert=new Alert(AlertType.CONFIRMATION); 
		alert.setTitle("Analyze Result");
		alert.setHeaderText("Classification: "+lblClassification.getText()+"\nSubject: "+lblSubject.getText());
		alert.setContentText("is classification correct?");
		  
		Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        	clickAddTextToTS();
        }
		//btnAddToTS.setVisible(true);	
	}


    @FXML
    void clickAddTextToTS() {
    	dbQuerys query = new dbQuerys();
    	query.addNewText(fileName, lblSubject.getText(), lblClassification.getText(), SentiStrengthController.sentimentMatrixToString(sentimentFeatures, 25));
    	lblAddMsg.setText("added to db");
    	lblAddMsg.setVisible(true);
    }
	
}