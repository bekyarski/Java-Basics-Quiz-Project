package users;

import quiz.Question;
import quiz.QuestionUtil;
import quiz.Quiz;
import utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static utils.Util.checkMenuInputNumber;

public class RegularUser extends User {
    private HashMap<Quiz, Double> quizResult;
    private HashMap<String, String> completedQuizzes;
    private List<String> assignedQuizzes;

    public RegularUser(String username, String password) {
        super(username, password);
        ;
    }

    public static RegularUser userLogin(String[] userAndPass) {
        RegularUser user = new RegularUser(userAndPass[0], userAndPass[1]);
        return user;
    }

    public void doAQuiz(String quiz, QuestionUtil qutil) {
        ArrayList<String> quizList = new ArrayList<>();
        double totalScore = 0.0;
        int correctAnswers = 0;
        Quiz quizToDo = qutil.getQuiz(quiz);
        String[] questions = quizToDo.getQuestions();
        
        for (int i = 0; i < questions.length; i++) {
            if (qutil.solveQuestion(questions[i])) {
                correctAnswers += 1;
            }
        }
        totalScore = (double) correctAnswers / (double) questions.length;

        String filePath = "src/users/QuizResults/" + this.getUsername() + ".txt";
        try {
            Files.write(Paths.get(filePath), (quiz + "," + totalScore + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("File does not exist!");
        }
    }

    public void getQuizResults() {
        List<String> quizList = loadCompletedQuizzes(this.getUsername());
        HashMap<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < quizList.size(); i++) {
            String[] resultsString = quizList.get(i).split(",");
            String quizName = resultsString[0];
            String quizResult = resultsString[1];
            map.put(quizName, quizResult);
        }
        int inputCheck = 0;
        int check = 0;
        String quiz_name = "";
        if (map.size() == 0) {
            System.out.println("You have not done any quizzes yet");
            return;
        }
        Scanner sc = new Scanner(System.in);
        while (inputCheck == 0) {
            System.out.println("Please, provide quiz name:");
            quiz_name = sc.nextLine().trim();
            if (Util.verifyInput(quiz_name, "Quiz name cannot be blank")) {
                inputCheck = 1;
            }
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals(quiz_name)) {
                System.out.println("Your result from quiz " + entry.getKey() + " is " + entry.getValue());
                check = 1;
                break;
            }
        }
        if (check == 0) {
            System.out.println("You have not done this quiz yet");
        }
    }

    public void getLastFive() {
        List<String> quizList = loadCompletedQuizzes(this.getUsername());
        if (quizList.size() == 0) {
            System.out.println("You have not done any quizzes yet");
        } else if (quizList.size() < 5) {
            for (int i = 0; i < quizList.size(); i++) {
                String[] resultsString = quizList.get(i).split(",");
                String quizName = resultsString[0];
                String quizResult = resultsString[1];
                System.out.println("Your result from quiz " + quizName + " is " + quizResult);
            }
        } else {
            System.out.println("Below are your last 5 completed quizzes");
            for (int i = quizList.size(); i > quizList.size() - 5; i--) {
                String[] resultsString = quizList.get(i - 1).split(",");
                String quizName = resultsString[0];
                String quizResult = resultsString[1];
                System.out.println("Your result from quiz " + quizName + " is " + quizResult);
            }
        }
    }

    public List<String> loadCompletedQuizzes(String username) {
        String filePath = "src/users/QuizResults/" + username  + ".txt";
        List<String> quizList = new LinkedList<>();
        try {
            File results = new File(filePath);
            Scanner myReader = new Scanner(results);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                quizList.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return quizList;
    }

    public void loadAssignedQuizzes(String user) {
        List<String> assignedQuizzes = new LinkedList<>();
        try {
            File myObj = new File("src/users/AssignedQuizzes/" + user + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                assignedQuizzes.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException | NullPointerException e) {
            System.out.println("This is your custom exception.");
            e.printStackTrace();
        }
        this.assignedQuizzes = new LinkedList<>();
        this.assignedQuizzes.addAll(assignedQuizzes);
    }

    public List<String> getAssignedQuizzes() {
        return assignedQuizzes;
    }

    public void setAssignedQuizzes(List<String> assignedQuizzes) {
        this.assignedQuizzes = assignedQuizzes;
    }
}
