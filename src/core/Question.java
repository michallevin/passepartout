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
	private int answerIndex;
	
	private static Random r = new Random();
	
	
	public static Question generateQuestion(Country country) {

		FactType factType = FactType.getRandom();
		Fact answer = Fact.getFact(country.getId(), factType.getId());
		while (answer == null) {
			factType = FactType.getRandom();
			answer = Fact.getFact(country.getId(), factType.getId());
		}
		List<Fact> otherOptions = Fact.getWrongAnswers(factType.getId(), answer.getData());
		
		Question question = new Question();
		question.setQuestionText(factType.getQuestionWording().replace("$countryName", country.getName()));
		
		question.answerIndex = r.nextInt(4);
		question.setOptions(new ArrayList<String>());
		int i = 0;
		for (Fact option : otherOptions) {
			if (i == question.answerIndex)
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
	
}
