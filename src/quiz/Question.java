package quiz;

import java.util.Arrays;
import utils.Util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.SourceDataLine;
import java.io.FileNotFoundException;

public class Question {

    public static final int NUMBER_OF_ANSWERS = 3;
    public static final int MIN_NUMBER_OF_ANSWERS = 1;
    public static final int QUESTION_HEAD_POSITION = 0;
    public static final int QUESTION_ANSWER_POSITION = 1;
    public static final int QUESTION_RIGHT_ANSWER_POSITION = 2;
    private String name;
    private String questionBody;
    private int rightAnswer;
    private String[] answers = new String[NUMBER_OF_ANSWERS];

    public Question(){

    }

    public Question(String name, String questionBody, int rightAnswer, String[] answers) {
         Util.validate(name);
         Util.validate(questionBody);
         if(rightAnswer > NUMBER_OF_ANSWERS && rightAnswer < MIN_NUMBER_OF_ANSWERS){
            System.out.println("The right answer cannot be higher than 3 and lower than 1");
        } if(answers.length < NUMBER_OF_ANSWERS){
            System.out.println("You cannot have less than 3 possible answers");
        }

         this.name = name;
         this.questionBody = questionBody;
         this.rightAnswer = rightAnswer;
         this.answers = answers;
    }

    public boolean correctAnswer(int answer){
        if (answer == this.rightAnswer){
            System.out.println("Correct");
            return true;
        }
        System.out.println("False, right answer is number: " + this.rightAnswer);
        return false;
    }

    public String getName() {
        return this.name;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public String[] getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return questionBody + "/" + Arrays.toString(answers) + "/" + rightAnswer;
    }
    //edit question object question = new question with changes
}
