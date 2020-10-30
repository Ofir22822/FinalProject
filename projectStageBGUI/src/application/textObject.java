package application;

public class textObject {
	
	private int textID;
	private String textName;
	private String subject;
	private int classificationID;
	private String sentimentFeatureSentence;
	private String sentimentFeatureWord;
	private String frequencyFeature;

	public textObject() {
	}
	
	public textObject(int textID,String textName, String subject,int classificationID,String sentimentFeatureSentence,String frequencyFeature) {
		this.textID = textID;
		this.textName = textName;
		this.subject = subject;
		this.classificationID = classificationID;
		this.sentimentFeatureSentence = sentimentFeatureSentence;
		this.sentimentFeatureWord = "";
		this.frequencyFeature = frequencyFeature;
	}
	
	public textObject(int textID,String textName, String subject,int classificationID,String sentimentFeatureSentence,String sentimentFeatureWord,String frequencyFeature) {
		this.textID = textID;
		this.textName = textName;
		this.subject = subject;
		this.classificationID = classificationID;
		this.sentimentFeatureSentence = sentimentFeatureSentence;
		this.sentimentFeatureWord = sentimentFeatureWord;
		this.frequencyFeature = frequencyFeature;
	}

	public int getTextID() {
		return textID;
	}

	public void setTextID(int textID) {
		this.textID = textID;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getClassificationID() {
		return classificationID;
	}

	public void setClassificationID(int classificationID) {
		this.classificationID = classificationID;
	}

	public String getSentimentFeatureSentence() {
		return sentimentFeatureSentence;
	}

	public void setSentimentFeatureSentence(String sentimentFeatureSentence) {
		this.sentimentFeatureSentence = sentimentFeatureSentence;
	}

	public String getSentimentFeatureWord() {
		return sentimentFeatureWord;
	}

	public void setSentimentFeatureWord(String sentimentFeatureWord) {
		this.sentimentFeatureWord = sentimentFeatureWord;
	}

	public String getFrequencyFeature() {
		return frequencyFeature;
	}

	public void setFrequencyFeature(String frequencyFeature) {
		this.frequencyFeature = frequencyFeature;
	}	
	public String getTextName() {
		return textName;
	}

	public void setTextName(String textName) {
		this.textName = textName;
	}

	@Override
	public String toString() {
		return "textObject [textID=" + textID + ", textName=" + textName + ", subject=" + subject
				+ ", classificationID=" + classificationID + ", sentimentFeatureSentence=" + sentimentFeatureSentence
				+ ", sentimentFeatureWord=" + sentimentFeatureWord
				+ ", frequencyFeature=" + frequencyFeature + "]";
	}
	

}
