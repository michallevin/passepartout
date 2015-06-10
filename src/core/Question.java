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
	
	
	public static Question generateQuestion(Country country, int userId, boolean isLiteral) {

		FactType factType = FactType.getRandom(isLiteral);
		Fact answer = Fact.getFact(country.getId(), factType.getId(), userId);
		while (answer == null) {
			factType = FactType.getRandom(isLiteral);
			answer = Fact.getFact(country.getId(), factType.getId(), userId);
		}
		List<Fact> otherOptions = Fact.getWrongAnswers(factType.getId(), answer.getData());
		
		Question question = new Question();
		question.setQuestionText(factType.getQuestionWording().replace("$countryName", country.getName()));
		
		question.setAnswerIndex(r.nextInt(4));
		question.setOptions(new ArrayList<String>());
		int i = 0;
		for (Fact option : otherOptions) {
			if (i == question.getAnswerIndex())
				question.getOptions().add(answer.getData());
			question.getOptions().add(option.getData());
			i += 1;
		}
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
