
public class textObject {
	
	private int textID;
	private String textName;
	private String subject;
	private int classificationID;
	private String sentimentFeature;
	private String frequencyFeature;

	public textObject() {
	}
	
	public textObject(int textID,String textName, String subject,int classificationID,String sentimentFeature,String frequencyFeature) {
		this.textID = textID;
		this.textName = textName;
		this.subject = subject;
		this.classificationID = classificationID;
		this.sentimentFeature = sentimentFeature;
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

	public String getSentimentFeature() {
		return sentimentFeature;
	}

	public void setSentimentFeature(String sentimentFeature) {
		this.sentimentFeature = sentimentFeature;
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
				+ ", classificationID=" + classificationID + ", sentimentFeature=" + sentimentFeature
				+ ", frequencyFeature=" + frequencyFeature + "]";
	}
	

}
