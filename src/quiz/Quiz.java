package quiz;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import utils.Util;

public class Quiz {

    public static final int MIN_NUNMBER_OF_QUESTIONS = 10;
    public static final int MAX_NUNMBER_OF_QUESTIONS = 20;
    private String name;
    private String[] questions;
    private int questionCount = 0;

    public Quiz(){
        
    }

    public Quiz(String name, String[] questions) {
        Util.validate(name);
        if (questions.length < MIN_NUNMBER_OF_QUESTIONS) {
            System.out.println("The questionsin must be less than 10");
            return;
        }
        if (questions.length > MAX_NUNMBER_OF_QUESTIONS) {
            System.out.println("The questionsin must be more than 20");
            return;
        }
        this.name = name;
        this.questions = questions;
    }

    public void addQuestion(String question) {
        if (this.questions.length <= this.questionCount) {
            System.out.println(
                    "You have reached the maximum number of questions. Do you want to edit or remove question ?");
            return;
        }
        this.questions[this.questionCount] = question;
        this.questionCount++;
    }

    public String getName() {
        return this.name;
    }

    public String[] getQuestions(){
        return this.questions;
    }
}
