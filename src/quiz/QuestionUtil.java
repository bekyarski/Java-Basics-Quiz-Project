package quiz;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import utils.Util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.SourceDataLine;

import sun.reflect.generics.tree.ReturnType;

import java.io.FileNotFoundException;

import static utils.Util.checkMenuInput;
import static utils.Util.checkMenuInputNumber;

public class QuestionUtil {

    protected static final String QUESTIONS_DB = "questions";
    protected static final String QUIZ_DB = "quizes";
    protected static ArrayList<String> allQuestionsNames = new ArrayList();
    protected static ArrayList<String> allQuizesNames = new ArrayList();
    public ArrayList<Question> allQuestions = new ArrayList();
    public ArrayList<Quiz> allQuizes = new ArrayList();
    
    //System
    public void startTheSystem(){
        this.allQuestionsNames = fillTheSystem(QUESTIONS_DB);
        this.allQuizesNames = fillTheSystem(QUIZ_DB);
        loadAllQuestions();
        loadAllQuizes();
    }

    public ArrayList<String> fillTheSystem(String db){
        ArrayList<String> result = new ArrayList();
        try {
            File questions = new File(Util.fileFormat(db));
            Scanner myReader = new Scanner(questions);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              result = new ArrayList<String>(Arrays.asList(data.split(",")));
            }
            myReader.close();
            return result;
        } catch (FileNotFoundException e) {
            System.out.println("Could not get all questions");
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    public void saveDataOnExit(){
        allQuestionsNames.clear();
        for (int i = 0; i < this.allQuestions.size(); i++) { 
            Question q = this.allQuestions.get(i);
            delete(q.getName());
            create(q.getName());
            save(q.getName(), q.toString());
            this.allQuestionsNames.add(q.getName());
        }

        delete(QUESTIONS_DB);
        create(QUESTIONS_DB);
        save(QUESTIONS_DB, String.join(",", allQuestionsNames));

        allQuizesNames.clear();
        for (int i = 0; i < this.allQuizes.size(); i++) {
            Quiz q = this.allQuizes.get(i);
            delete(q.getName());
            create(q.getName());
            save(q.getName(), String.join(",", q.getQuestions()));
            this.allQuizesNames.add(q.getName());
        }

        delete(QUIZ_DB);
        create(QUIZ_DB);
        save(QUIZ_DB, String.join(",", allQuizesNames));
    }

    //Quiz
    public void loadAllQuizes(){
        try {
            for (int i = 0; i < this.allQuizesNames.size(); i++) {
                File quiz = new File(Util.fileFormat(allQuizesNames.get(i)));
                Scanner myReader = new Scanner(quiz);
                while (myReader.hasNextLine()) {
                  String data = myReader.nextLine();
                  allQuizes.add(new Quiz(allQuizesNames.get(i), data.split(",")));
//                  System.out.println(allQuizes.get(i).getName());
//                  System.out.println(allQuizes.get(i).getName() + " " + Arrays.toString(allQuizes.get(i).getQuestions()));
                }
                myReader.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not load all questions");
            e.printStackTrace();
        }
    }

    //Question
    public void loadAllQuestions(){
        try {
            for (int i = 0; i < this.allQuestionsNames.size(); i++) {
                File question = new File(Util.fileFormat(this.allQuestionsNames.get(i)));
                Scanner myReader = new Scanner(question);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] info = data.split("/");
                    this.allQuestions.add(new Question(allQuestionsNames.get(i), info[Question.QUESTION_HEAD_POSITION], Integer.parseInt(info[Question.QUESTION_RIGHT_ANSWER_POSITION]), info[Question.QUESTION_ANSWER_POSITION].split(",")));
                }
                myReader.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not load all questions");
            e.printStackTrace();
        }
    }

    public void readQuestion(Question question) {
        String[] questionAnswers = question.getAnswers();
        System.out.println(question.getQuestionBody());
        for (int i = 0; i < questionAnswers.length; i++) {
            String result = questionAnswers[i].replaceAll("[- +.^:\\[\\]]*","");
                System.out.println("Option " + (i+1) + ": " + result);
            }
    }

    public void editQuestion(String questionName, String questionBody, int rightAnswer, String[] answers){

        Util.validate(questionName);
        Util.validate(questionBody);

        if(rightAnswer > Question.NUMBER_OF_ANSWERS && rightAnswer < Question.MIN_NUMBER_OF_ANSWERS){
           System.out.println("The right answer cannot be higher than 3 and lower than 1");
       } if(answers.length < Question.NUMBER_OF_ANSWERS){
           System.out.println("You cannot have less than 3 possible answers");
       }
        for (int i = 0; i < allQuestions.size(); i++) {
            if(allQuestions.get(i).getName().equals(questionName)){
               allQuestions.add(i, new Question(questionName, questionBody, rightAnswer, answers));
               delete(allQuestions.get(i).getName());
               create(allQuestions.get(i).getName());
               save(allQuestions.get(i).getName(), allQuestions.get(i).toString());
               break;
            }
        }
    }

    public void save(String name, String info){
        try {
            FileWriter writer = new FileWriter(Util.fileFormat(name));
            writer.write(info);
            writer.close();
//            System.out.println( name + "Has been saved");
        } catch (IOException e){
            System.out.println("Could not save " + name);
            e.printStackTrace();
        }
    }

    public void create(String item){
        try {
            File file = new File(Util.fileFormat(item));
            if (file.createNewFile()) {
//                System.out.println(item + "Has been created");
            } else {
                System.out.println("Could not save " + item + " ! It already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    public void delete(String item){
        try {
            File file = new File(Util.fileFormat(item));
            if (!file.delete()) {
                System.out.println("Failed to modify: " + item);
            }
        } catch(Exception e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
    public static void listAllQuizzes(QuestionUtil qutil){
        ArrayList<Quiz> allQuizes = new ArrayList<>();
        try {
            allQuizes.addAll(qutil.allQuizes);
        } catch (NullPointerException e) {
            System.out.println("No quizzes exist");
        }
        System.out.println("Here are all available quizzes:");
        for (int i = 0; i < allQuizes.size(); i++) {
            System.out.println(allQuizes.get(i).getName());
        }
    }

    public boolean solveQuestion(String questionName) {
        Util.validate(questionName);
        Question question = getQuestion(questionName);
        int suggestedAnswer = 0;
        readQuestion(question);
        
        String rightAnswerString = checkMenuInputNumber("right answer (1, 2 or 3)");
        suggestedAnswer = Integer.parseInt(rightAnswerString);
        return question.correctAnswer(suggestedAnswer);
    }

    public Question getQuestion(String questionName){
        Question result = new Question();
        for (int i = 0; i < this.allQuestions.size(); i++) {
            if(questionName.equals(this.allQuestions.get(i).getName())){
                result = this.allQuestions.get(i);
                break;
            }
        }
        return result;
    }

    public Quiz getQuiz(String quizName){
        Quiz result = new Quiz();
        for (int i = 0; i < this.allQuizes.size(); i++) {
            if(quizName.equals(this.allQuizes.get(i).getName())){
                result = this.allQuizes.get(i);
                break;
            }
        }
        return result;
    }
}