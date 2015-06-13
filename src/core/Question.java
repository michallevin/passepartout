package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import db.models.Country;
import db.models.Fact;
import db.models.FactType;

public class Question {

	private String questionText;
	private List<String> options;
	private Integer answerIndex;
	private int score;
	private String posterImage;
	private String label;
	
	private static Random r = new Random();
	
	
	public static Question generateQuestion(Country country, int userId, boolean isLiteral, int difficulty) {

		List<Fact> otherOptions = new ArrayList<Fact>();
		FactType factType = null;
		Fact answer = null;
		
		while (otherOptions.size() < 3) {
			factType = FactType.getRandom(isLiteral);
			answer = Fact.getFact(country.getId(), factType.getId(), userId, isLiteral, difficulty);
			if (answer == null) continue;
			otherOptions = Fact.getWrongAnswers(factType.getId(), country.getId());
		}
		
		Question question = new Question();
		question.setQuestionText(factType.getQuestionWording().replace("$countryName", country.getLabel()));
		
		question.setAnswerIndex(r.nextInt(4));
		question.setOptions(new ArrayList<String>());
		int i = 0;
		for (Fact option : otherOptions) {
			if (i == question.getAnswerIndex())
				question.getOptions().add(answer.getReadableData(country.getLabel()));
			question.getOptions().add(option.getReadableData(country.getLabel()));
			i += 1;
		}
		if (question.getAnswerIndex() == 3)
			question.getOptions().add(answer.getReadableData(country.getLabel()));

		return question;
	}

	public String getQuestionText() {
		return questionText;
	}


	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}


	public List<String> getOptions() {
		return options;
	}


	public void setOptions(List<String> options) {
		this.options = options;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getPosterImage() {
		return posterImage;
	}

	public void setPosterImage(String posterImage) {
		this.posterImage = posterImage;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getAnswerIndex() {
		return answerIndex;
	}

	public void setAnswerIndex(Integer answerIndex) {
		this.answerIndex = answerIndex;
	}
	
}
