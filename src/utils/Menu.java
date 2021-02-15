package utils;

import users.Administrator;
import users.RegularUser;
import users.User;
import quiz.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static utils.Util.*;
import static utils.Util.checkMenuInputNumberofQuestions;

public class Menu {

    public static QuestionUtil intro() {
        QuestionUtil qutil = new QuestionUtil();
        qutil.startTheSystem();
        int check = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello, what do you want to do?\n1 - Sign Up\n2 - Sign In\n3 - Sign In as Admin\n4 - Exit the program\nPlease provide a number:");
        while (check == 0) {
            String input_command = sc.nextLine().trim();
            if (input_command.equalsIgnoreCase("1")) {
                check = 1;
                RegularUser.createUser();
            } else if (input_command.equalsIgnoreCase("2")) {
                check = 1;
                RegularUser user = RegularUser.userLogin(User.login("Regular user"));
                user.loadAssignedQuizzes(user.getUsername());
                RegularUserMenu(user, qutil);
            } else if (input_command.equalsIgnoreCase("3")) {
                check = 1;
                Administrator user = Administrator.adminLogin(User.login("Administrator"));
                AdminUserMenu(user, qutil);
            } else if (input_command.equalsIgnoreCase("4")) {
                qutil.saveDataOnExit();
                System.out.println("Thank you for playing!");
                System.exit(0);
            } else {
                System.out.println("This is not a valid command, please try again.\n1 - Sign Up\n2 - Sign In\n3 - Sign In as Admin\n4 - Exit the program");
            }
        }
        return qutil;
    }

    public static void RegularUserMenu(RegularUser user, QuestionUtil qutil) {
        int check = 0;
        Scanner sc = new Scanner(System.in);
        while (check == 0) {
            System.out.println("\nWhat do you want to do now\n1 - See all available quizzes\n2 - See assigned quizzes\n3 - Do a quiz\n4 - See quiz results\n5 - See results from last five quizzes\n6 - Return to main menu\nPlease provide a number:");
            String input_command = sc.nextLine().trim();
            if (input_command.equalsIgnoreCase("1")) {
                QuestionUtil.listAllQuizzes(qutil);
            } else if (input_command.equalsIgnoreCase("2")) {
                List<String> quizList = user.getAssignedQuizzes();
                if (quizList.size() == 0) {
                    System.out.println("You do not have any assigned quizzes");
                } else {
                    System.out.println("Here is a list of yor assigned quizzes");
                    for (int i = 0; i < quizList.size(); i++) {
                        System.out.println(quizList.get(i));
                    }
                }
            } else if (input_command.equalsIgnoreCase("3")) {
                System.out.println("Please choose which quiz you want to do");
                QuestionUtil.listAllQuizzes(qutil);
                String quiz = checkMenuInput("quiz");
                while (!Util.quizExists(qutil, quiz)) {
                    quiz = checkMenuInput("quiz");
                    if (!Util.quizExists(qutil, quiz)) {
                        System.out.println("Quiz with that name does not exist");
                    }
                }
                user.doAQuiz(quiz,qutil);
            } else if (input_command.equalsIgnoreCase("4")) {
                user.getQuizResults();
            } else if (input_command.equalsIgnoreCase("5")) {
                user.getLastFive();
            } else if (input_command.equalsIgnoreCase("6")) {
                intro();
                check = 1;
            } else {
                System.out.println("This is not a valid command, please try again.");
            }
        }
    }

    public static void AdminUserMenu(Administrator user, QuestionUtil qutil) {
        int check = 0;
        Scanner sc = new Scanner(System.in);
        while (check == 0) {
            System.out.println("\nWhat do you want to do now?\n1 - Create a quiz\n2 - Edit a question\n3 - Create a  question\n4 - See results per user\n5 - Assign quiz to a user\n6 - Return to main menu\nPlease provide a number:");
            String input_command = sc.nextLine().trim();
            if (input_command.equalsIgnoreCase("1")) {
                String quizName = checkMenuInput("quiz name");
                ArrayList<Quiz> allQuizes = new ArrayList<>();
                try {
                    qutil.allQuizes.size();
                    allQuizes.addAll(qutil.allQuizes);
                } catch (NullPointerException e) {
                    System.out.println("No quizzes exist");
                }
                for (int i = 0; i < allQuizes.size(); i++) {
                    if (allQuizes.get(i).getName().equalsIgnoreCase(quizName)){
                        System.out.println("Such quiz already exists");
                        quizName = checkMenuInput("quiz name");
                    }
                }
                String numberOfQuestionString = "0";
                int numberOfQuestion = 0;
                numberOfQuestionString = checkMenuInputNumberofQuestions("how manu questiion you want to have in the quiz (number between 10 and 20)");
                try{
                    numberOfQuestion = Integer.parseInt(numberOfQuestionString);
                }catch (NumberFormatException ex) {

                }
//                int numberOfQuestion = 10;
                String[] questionsString = new String[numberOfQuestion];
                String quizContent = "";
                for (int i = 0; i < numberOfQuestion; i++) {
                    String questionName = "";
                    while (!Util.questionExists(qutil,questionName)) {
                        questionName = checkMenuInput("question name");
                        if (!Util.questionExists(qutil,questionName)) {
                            System.out.println("Question does not exist");
                        }
                    }
                    questionsString[i] = questionName;
                    if (i<numberOfQuestion-1){
                        quizContent = quizContent + questionName + ",";
                    }else{
                        quizContent = quizContent + questionName;
                    }

                }
                Quiz quiz = new Quiz(quizName, questionsString);
                qutil.save(quiz.getName(),quizContent);
                try {
                    if (allQuizes.size() == 0){
                        Files.write(Paths.get("src/quiz/Q&Qs/quizes.txt"), (quizName).getBytes(), StandardOpenOption.APPEND);
                    }else{
                        Files.write(Paths.get("src/quiz/Q&Qs/quizes.txt"), ("," + quizName).getBytes(), StandardOpenOption.APPEND);
                    }
                } catch (IOException e) {
                    System.out.println("File does not exist!");
                }
                qutil.allQuizes.add(quiz);
//                qutil.startTheSystem();
//                Create a quiz
            } else if (input_command.equalsIgnoreCase("2")) {
                String questionName = "";
                String questionBody = "";
                String rightAnswerString = "4";
                int rightAnswer = 0;
                while (!Util.questionExists(qutil,questionName)) {
                    questionName = checkMenuInput("question name");
                    if (!Util.questionExists(qutil,questionName)) {
                        System.out.println("Question does not exist");
                    }
                }
                questionBody = checkMenuInput("question body");
                System.out.println("Please, provide 3 answers, one by one:");
                String[] answers = new String[3];
                for (int i = 0; i < 3; i++) {
                    answers[i] = checkMenuInput("answer");
                }
                rightAnswerString = checkMenuInputNumber("right answer");
                try{
                    rightAnswer = Integer.parseInt(rightAnswerString);
                }catch (NumberFormatException ex) {
                }
                qutil.delete(questionName);
                qutil.create(questionName);
                qutil.editQuestion(questionName, questionBody, rightAnswer, answers);
            } else if (input_command.equalsIgnoreCase("3")) {
                String questionName = "";
                String questionBody = "";
                String rightAnswerString = "4";
                int rightAnswer = 0;
                questionName = checkMenuInput("question name");
                questionBody = checkMenuInput("question body");
                System.out.println("Please, provide 3 answers, one by one:");
                String[] answers = new String[3];
                for (int i = 0; i < 3; i++) {
                    answers[i] = checkMenuInput("answer");
                }
                rightAnswerString = checkMenuInputNumber("right answer");
                try{
                    rightAnswer = Integer.parseInt(rightAnswerString);
                }catch (NumberFormatException ex) {
                }
                Question question = new Question(questionName, questionBody, rightAnswer, answers);
                qutil.create(questionName);
                qutil.save(questionName,question.toString());
                try {
                    Files.write(Paths.get("src/quiz/Q&Qs/questions.txt"), ("," + questionName).getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    System.out.println("File does not exist!");
                }
                qutil.allQuestions.add(question);
                //qutil.startTheSystem();
            } else if (input_command.equalsIgnoreCase("4")) {
                String userName = "";
                userName = checkMenuInput("user name");
                Administrator.seeUserResults(userName);
            } else if (input_command.equalsIgnoreCase("5")) {
                System.out.println("Please choose to which user you want to assign a quiz");
                Util.printUsers();
                String userName = "";
                while (!Util.userExists(userName)) {
                    userName = checkMenuInput("username");
                    if (!Util.userExists(userName)) {
                        System.out.println("User does not exist");
                    }
                }
                System.out.println("Please choose which quiz you want to assign");
                QuestionUtil.listAllQuizzes(qutil);
                String quiz = checkMenuInput("quiz");
                while (!Util.quizExists(qutil, quiz)) {
                    quiz = checkMenuInput("quiz");
                    if (!Util.quizExists(qutil, quiz)) {
                        System.out.println("Quiz with that name does not exist");
                    }
                }
                Administrator.assignQuiz(quiz,userName);
                System.out.println("Quiz " + quiz + " assigned to user " + userName);
            } else if (input_command.equalsIgnoreCase("6")) {
                intro();
                check = 1;
            } else {
                System.out.println("This is not a valid command, please try again.");
            }
        }
    }

}
