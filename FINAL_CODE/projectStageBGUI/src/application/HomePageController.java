package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * class for GUI related functions
 *
 */
public class HomePageController {

	private int countText = 1;
	dbQuerys query = new dbQuerys();
	
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
    
    @FXML
    private Button btnAnalyzeDictionary;
    
    @FXML
    private Label lblClassificationBoth;

    @FXML
    private Label lblClassificationDictionary;
    
    @FXML
    private Hyperlink settingLink;
    
    private String[][] sentimentFeatures;
    private String fileName;
    
    @FXML
    private Button btnSaveSetting;
    
    @FXML
    private Label lblClassificationDictionaryAndSentimentWord;
    
    @FXML
    private Label lblClassificationDictionaryAndSentimentSentence;
    
    @FXML
    private ToggleButton btnAnalyzeSelect;

    @FXML
    private ToggleGroup analyze_type;

    @FXML
    private RadioButton rdSentiment;

    @FXML
    private RadioButton rdBoth;

    @FXML
    private RadioButton rdDictionary;
    
	@FXML
	public void initialize() {
		SentiStrengthController.setDbquery(query);
		DictionaryController.setDbquery(query);
		KNNController.setDbquery(query);
		TextRazorController.setDbquery(query);
		
		/*
		rank_senti_value.textProperty().bind(
                Bindings.format(
                    "%.2f",
                    tank_senti_slider.valueProperty()
                )
            );
        
		
		rank_senti_slider.setOnMouseReleased(event -> {
			rank_senti_value.setText(Math.ceil(rank_senti_slider.getValue()) + "");
        });
		
		rank_dict_slider.setOnMouseReleased(event -> {
			rank_dict_value.setText(Math.ceil(rank_dict_slider.getValue()) + "");
        });
		
		avg_dict_slider.setOnMouseReleased(event -> {
			avg_dict_value.setText(Math.ceil(avg_dict_slider.getValue()) + "");
        });
		
		start_correlation_dict_slider.setOnMouseReleased(event -> {
			start_correlation_dict_value.setText(Math.ceil(start_correlation_dict_slider.getValue()) + "");
        });
		
		end_correlation_dict_slider.setOnMouseReleased(event -> {
			end_correlation_dict_value.setText(Math.ceil(end_correlation_dict_slider.getValue()) + "");
        });
		*/
	}
	
    @FXML
    private Label lblTest;
	
    @FXML
    private Slider start_correlation_dict_slider;

    @FXML
    private Slider rank_senti_slider;

    @FXML
    private Slider avg_dict_slider;

    @FXML
    private Slider end_correlation_dict_slider;

    @FXML
    private Slider rank_sentence_senti_slider;

    @FXML
    private TextField word_rank_value;

    @FXML
    private TextField sent_rank_value;

    @FXML
    private TextField avg_value;

    @FXML
    private TextField corr_start_value;

    @FXML
    private TextField corr_end_value;
    
	private Stage StageSetting = new Stage();

    @FXML
    private TextArea tbTextFile;
	
	private int length = 0;
	
	private File inputFile = null;
	
    @FXML
    private TabPane tabPane1;

    
	@FXML
	void openFile(MouseEvent event) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", ".pdf");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int returnValue = jfc.showOpenDialog(null);
		File selectedFile = null;
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = jfc.getSelectedFile();
			tbfilePath.setText(selectedFile.getAbsolutePath());
		}
			
		inputFile = selectedFile;
	}
	
	@FXML
	void openWebsiteClick(MouseEvent event) {

		wvView.getEngine().load("http://google.com");
	}

	File createFileForNewText(String text) {
		
		File countFile = new File("webTexts\\counter.txt");
		
		try {
			//read text counter
			Scanner myReader = new Scanner(countFile);
			countText = Integer.parseInt(myReader.nextLine());
			myReader.close();
			
			//write text to file "wi.txt"
			String newTextFileName = "webTexts\\w"+countText+".txt";
			FileFunctions.createTextFile(newTextFileName, text);
			
			//write to text counter +1 to number
			countText++;
			FileWriter countWriter = new FileWriter("webTexts\\counter.txt");
			countWriter.write(""+countText);
			countWriter.close();

			return new File(newTextFileName);
			
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return null;
	}

	/* analyze functions*/
	
	@FXML
    void click_analyzeSelect(MouseEvent event) {
		
		textObject textObj = new textObject();

		lblSubject.setWrapText(true);
		
    	String text = tbText.getText();
		File textFile = createFileForNewText(text);
		fileName = textFile.getName();
		if(tabPane1.getSelectionModel().getSelectedIndex() == 1)
			textFile = inputFile;
		String subject = TextRazorController.getTextSubject(textFile);			
		
		lblSubject.setText(subject);	
		textObj.setSubject(subject);
		
    	RadioButton selectedRadioButton = (RadioButton) analyze_type.getSelectedToggle();
    	String toogleGroupValue = selectedRadioButton.getText();
    	
    	System.out.print(tabPane1.getSelectionModel().getSelectedIndex());
    	
    	if(tabPane1.getSelectionModel().getSelectedIndex() == 0)
    	{
	    	switch(toogleGroupValue)
	    	{
		    	case "Dictionary":
		    		clickAnalyzeDictionary(event);
		    		break;
		    	case "Sentiment":
		    		clickAnalyze_sentiment(event);
		    		break;
	    		case "Sentiment + Dictionary":
	    			clickAnalyzeDictionary(event);
	    			clickAnalyze_sentiment(event);
	    			clickAnalyzeBoth(event);
	    			break;
	    	}
    	}
    	else
    	{
	    	switch(toogleGroupValue)
	    	{
		    	case "Dictionary":
		    		AnalyzeDictionary(inputFile);
		    		break;
		    	case "Sentiment":
		    		Analyze_sentiment(inputFile);
		    		break;
	    		case "Sentiment + Dictionary":
	    			AnalyzeDictionary(inputFile);
	    			Analyze_sentiment(inputFile);
	    			AnalyzeBoth(inputFile);
	    			break;
	    	}
    	}
    	
    	/*
		Alert alert=new Alert(AlertType.CONFIRMATION); 
		alert.setTitle("Analyze Result");
		alert.setContentText("which classification correct?");
		
		Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        	clickAddTextToTS();
        }
        */
    	
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Classification");
        alert.setHeaderText("Choose correct classification :");

        ButtonType buttonTypeOne = new ButtonType("Promotion");
        ButtonType buttonTypeTwo = new ButtonType("Personal Experience");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
        	if(!lblClassification.getText().equals("Promotion"))
        		lblClassification.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassification.setStyle("-fx-text-fill: #1b3bae;");
        	if(!lblClassificationWords.getText().equals("Promotion"))
        		lblClassificationWords.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassificationWords.setStyle("-fx-text-fill: #1b3bae;");
        	if(!lblClassificationBoth.getText().equals("Promotion"))
        		lblClassificationBoth.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassificationBoth.setStyle("-fx-text-fill: #1b3bae;");
        	if(!lblClassificationDictionaryAndSentimentWord.getText().equals("Promotion"))
        		lblClassificationDictionaryAndSentimentWord.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassificationDictionaryAndSentimentWord.setStyle("-fx-text-fill: #1b3bae;");
        	if(!lblClassificationDictionaryAndSentimentSentence.getText().equals("Promotion"))
        		lblClassificationDictionaryAndSentimentSentence.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassificationDictionaryAndSentimentSentence.setStyle("-fx-text-fill: #1b3bae;");
        	if(!lblClassificationDictionary.getText().equals("Promotion"))
        		lblClassificationDictionary.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassificationDictionary.setStyle("-fx-text-fill: #1b3bae;");

        } else if (result.get() == buttonTypeTwo) {
        	if(!lblClassification.getText().equals("Personal Experience"))
        		lblClassification.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassification.setStyle("-fx-text-fill: #1b3bae;");
        	if(!lblClassificationWords.getText().equals("Personal Experience"))
        		lblClassificationWords.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassificationWords.setStyle("-fx-text-fill: #1b3bae;");
        	if(!lblClassificationBoth.getText().equals("Personal Experience"))
        		lblClassificationBoth.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassificationBoth.setStyle("-fx-text-fill: #1b3bae;");
        	if(!lblClassificationDictionaryAndSentimentWord.getText().equals("Personal Experience"))
        		lblClassificationDictionaryAndSentimentWord.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassificationDictionaryAndSentimentWord.setStyle("-fx-text-fill: #1b3bae;");
        	if(!lblClassificationDictionaryAndSentimentSentence.getText().equals("Personal Experience"))
        		lblClassificationDictionaryAndSentimentSentence.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassificationDictionaryAndSentimentSentence.setStyle("-fx-text-fill: #1b3bae;");
        	if(!lblClassificationDictionary.getText().equals("Personal Experience"))
        		lblClassificationDictionary.setStyle("-fx-text-fill: #ff0000;");
        	else
        		lblClassificationDictionary.setStyle("-fx-text-fill: #1b3bae;");
        } else {
            // ... user chose CANCEL or closed the dialog
        }
        
		sentimentFeatures = SentiStrengthController.getSentimentFeatureSentences(textFile);
		
		textObj.setSentimentFeatureSentence(SentiStrengthController.sentimentMatrixToString(sentimentFeatures,25));
		textObj.setSentimentFeatureWord(SentiStrengthController.getSentimentFeatureWords(textFile)); 		
		textObj.setFrequencyFeature(DictionaryController.createFrequencyFeature(textFile));
		
		if(lblClassificationDictionaryAndSentimentWord.getText().equals("Personal Experience"))
			textObj.setClassificationID(2);
		else
			textObj.setClassificationID(1);
		
		Alert alert2=new Alert(AlertType.CONFIRMATION); 
		alert2.setTitle("Analyze Result");
		alert2.setHeaderText("Classification: "+lblClassificationDictionaryAndSentimentWord.getText()+"\nSubject: "+lblSubject.getText());
		alert2.setContentText("is classification correct?\nPress OK to add it into training set.");
		  
		Optional<ButtonType> result2 = alert2.showAndWait();
        if (result2.get() == ButtonType.OK){
        	clickAddTextToTS(textObj);
        }

    }
		
	@FXML
	void clickAnalyze_sentiment(MouseEvent event) {
		String text = tbText.getText();			//get text from textarea
		File textFile = createFileForNewText(text);
		fileName = textFile.getName();
		
		Analyze_sentiment(textFile);
		
		//btnAddToTS.setVisible(true);	
	}
		
	private void Analyze_sentiment(File textFile) {
		
		if(textFile!=null) {		
			sentimentFeatures = SentiStrengthController.getSentimentFeatureSentences(textFile);
    		
    		textObject txtObject = new textObject();
    		txtObject.setSentimentFeatureSentence(SentiStrengthController.sentimentMatrixToString(sentimentFeatures,25).replace(' ', ',')+"P");
    		txtObject.setSentimentFeatureWord(SentiStrengthController.getSentimentFeatureWords(textFile).replace(' ', ',')+"P"); 
    		
    		//sentiment knn data for sentence & word
    		SentiStrengthController.createSentimentKNNData(txtObject, length);
    		
    		KNNController.setKNNData("sentiment\\sentimentDataKNNSentenceRank.txt");
    		double KNNresult = KNNController.KNN();
    		
			if(KNNresult == -1.0)
				lblClassification.setText("Error");
			else if(KNNresult == 0.0)
				lblClassification.setText("Promotion");
			else
				lblClassification.setText("Personal Experience");
    			
    		KNNController.setKNNData("sentiment\\sentimentDataKNNWordRank.txt");
    		KNNresult = KNNController.KNN();
    		
			if(KNNresult == -1.0)
				lblClassificationWords.setText("Error");
			else if(KNNresult == 0.0)
				lblClassificationWords.setText("Promotion");
			else
				lblClassificationWords.setText("Personal Experience");
			
    		KNNController.setKNNData("sentiment\\sentimentDataKNNboth.txt");
    		KNNresult = KNNController.KNN();
    		
			if(KNNresult == -1.0)
				lblClassificationBoth.setText("Error");
			else if(KNNresult == 0.0)
				lblClassificationBoth.setText("Promotion");
			else
				lblClassificationBoth.setText("Personal Experience");
		}
	}
	
    @FXML
    void clickAnalyzeDictionary(MouseEvent event) {
		String text = tbText.getText();			//get text from textarea
		File textFile = createFileForNewText(text);
		fileName = textFile.getName();
		
		AnalyzeDictionary(textFile);
    		
    }
	
    void AnalyzeDictionary(File textFile)
    {
    	if(textFile!=null) {		
			
    		textObject txtObject = new textObject();
    		txtObject.setFrequencyFeature(DictionaryController.createFrequencyFeature(textFile));
    		
    		DictionaryController.createDictionaryData(txtObject, length);
    		
    		KNNController.setKNNData("dictionary\\DictionaryDataKNN.txt");
    		double KNNresult = KNNController.KNN();
    		
    			if(KNNresult == -1.0)
    				lblClassificationDictionary.setText("Error");
    			else if(KNNresult == 0.0)
    				lblClassificationDictionary.setText("Promotion");
    			else
    				lblClassificationDictionary.setText("Personal Experience");
    		
		}
    }
    
    @FXML
    void clickAddTextToTS() {
    	textObject newTxt = new textObject();
    	newTxt.setSubject(lblSubject.getText());
		int classficationID = 0;
		if(lblClassification.getText().compareTo("Promotion")==0)
			classficationID = 1;
		else
			classficationID = 2;
    	newTxt.setClassificationID(classficationID);
    	newTxt.setSentimentFeatureSentence(SentiStrengthController.sentimentMatrixToString(sentimentFeatures, 25));
    	 	
    	query.addNewText(fileName, newTxt);
    	lblAddMsg.setText("added to db");
    	lblAddMsg.setVisible(true);
    }
    
    void clickAddTextToTS(textObject textObj) {
    	query.addNewText(fileName, textObj);
    	lblAddMsg.setText("Added to training set.");
    	lblAddMsg.setVisible(true);
    }
	
    @FXML
    void clickAnalyzeBoth(MouseEvent event) {
    	String text = tbText.getText();			//get text from textarea
		File textFile = createFileForNewText(text);
		fileName = textFile.getName();
		
		AnalyzeBoth(textFile);
		
		/*
		Alert alert=new Alert(AlertType.CONFIRMATION); 
		alert.setTitle("Analyze Result");
		alert.setHeaderText("Classification: "+lblClassification.getText()+"\nSubject: "+lblSubject.getText());
		alert.setContentText("is classification correct?");
		  
		Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        	clickAddTextToTS();
        }
    	*/	
    }
    
    void AnalyzeBoth(File textFile)
    {
		if(textFile!=null) {		
			
    		textObject txtObject = new textObject();
			sentimentFeatures = SentiStrengthController.getSentimentFeatureSentences(textFile);
    		
    		txtObject.setFrequencyFeature(DictionaryController.createFrequencyFeature(textFile));
    		txtObject.setSentimentFeatureSentence(SentiStrengthController.sentimentMatrixToString(sentimentFeatures,25).replace(' ', ',')+"P");
    		txtObject.setSentimentFeatureWord(SentiStrengthController.getSentimentFeatureWords(textFile).replace(' ', ',')+"P");
    		
    		/*
    		KNNController.mergeFeaturesData_RankSentence(txtObject);  		
    		KNNController.setKNNData("KNN\\DataKNN-Sentence-dictionary-rank.txt");
    		double KNNresult = KNNController.KNN();
    		*/
    		KNNController.mergeFeaturesData_Rank(txtObject, 1);	
    		KNNController.setKNNData("KNN\\DataKNN-words-dictionary-rank.txt");
    		double KNNresult = KNNController.KNN();
    		
			if(KNNresult == -1.0)
			{
				lblClassificationDictionaryAndSentimentWord.setText("Error");
			}
			else if(KNNresult == 0.0)
			{
				lblClassificationDictionaryAndSentimentWord.setText("Promotion");
			}
			else
			{
				lblClassificationDictionaryAndSentimentWord.setText("Personal Experience");
			}
    		
    		KNNController.mergeFeaturesData_RankSentence(txtObject, 1);
    		KNNController.setKNNData("KNN\\DataKNN-Sentence-dictionary-rank.txt");
    		KNNresult = KNNController.KNN();
    		
			if(KNNresult == -1.0)
			{
				lblClassificationDictionaryAndSentimentSentence.setText("Error");
			}
			else if(KNNresult == 0.0)
			{
				lblClassificationDictionaryAndSentimentSentence.setText("Promotion");
			}
			else
			{
				lblClassificationDictionaryAndSentimentSentence.setText("Personal Experience");
			}
    		
		}
    }
    
    /* setting functions*/
    
    @FXML
    void openSettings(MouseEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingPage2.fxml"));
		try {
			Pane root = loader.load();

			//SearchResultController searchResultController = loader.getController();
			Scene scene = new Scene(root);
			StageSetting.setScene(scene);
			StageSetting.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

    }

    @FXML
    void saveSetting(MouseEvent event) {
    	//set global parameters
    	
		double corr_start_T = start_correlation_dict_slider.getValue();
		
		double corr_end_T = end_correlation_dict_slider.getValue();   
		        
		double avg_T = avg_dict_slider.getValue();
		
		double word_rank_T = rank_senti_slider.getValue();
		
		double sentence_rank_T = rank_sentence_senti_slider.getValue();
		
		KNNController.setSentence_rank_T((int)sentence_rank_T);
		KNNController.setWord_rank_T((int)word_rank_T);
		
		DictionaryController.setAvg_T((int)avg_T);
		DictionaryController.setCorr_start_T((int)corr_start_T);
		DictionaryController.setCorr_end_T((int)corr_end_T);
		
		System.out.print(sentence_rank_T + " " + word_rank_T + " "+ avg_T + " "+ corr_start_T + " "+ corr_end_T + " ");
	    final Node source = (Node) event.getSource();
	    final Stage stage = (Stage) source.getScene().getWindow();
	    stage.close();
    }
	
    /* slider values change*/
    
    @FXML
     void changeValue_word_rank(MouseEvent event) {
    	word_rank_value.setText(Math.ceil(rank_senti_slider.getValue()) + "");
    }
    
    @FXML
    void changeValue_word_rank2(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER))
        	rank_senti_slider.setValue(Double.parseDouble(word_rank_value.getText()));
    }
    
    @FXML
    void changeValue_sentence_rank(MouseEvent event) {
    	sent_rank_value.setText(Math.ceil(rank_sentence_senti_slider.getValue()) + "");
    }
    
    @FXML
    void changeValue_sentence_rank2(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER))
        	rank_sentence_senti_slider.setValue(Double.parseDouble(sent_rank_value.getText()));
    }
    
    @FXML
    void changeValue_dict_avg(MouseEvent event) {
    	avg_value.setText(Math.ceil(avg_dict_slider.getValue()) + "");
    }
    
    @FXML
    void changeValue_dict_avg2(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER))
        	avg_dict_slider.setValue(Double.parseDouble(avg_value.getText()));
    }
    
    @FXML
    void changeValue_dict_corr_start(MouseEvent event) {
    	corr_start_value.setText(String.format("%.2f", start_correlation_dict_slider.getValue()));
    }
    
    @FXML
    void changeValue_dict_corr_start2(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER))
        	start_correlation_dict_slider.setValue(Double.parseDouble(corr_start_value.getText()));
    }
    
    @FXML
    void changeValue_dict_corr_end(MouseEvent event) {
    	corr_end_value.setText(String.format("%.2f", end_correlation_dict_slider.getValue()));
    }
    
    @FXML
    void changeValue_dict_corr_end2(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER))
        	end_correlation_dict_slider.setValue(Double.parseDouble(corr_end_value.getText()));
    }
  
    
}