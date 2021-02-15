package utils;

import quiz.Question;
import quiz.QuestionUtil;
import quiz.Quiz;
import users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Util {
    public static boolean verifyInput(String inputString, String message) {
        if (inputString==null) {
            System.out.println(message);
            return false;
        } else if (inputString.isEmpty()) {
            System.out.println(message);
            return false;
        }
        else if (inputString.equals("")) {
            System.out.println(message);
            return false;
        }else if (inputString.equals("null")) {
            System.out.println(message);
            return false;
        } else {
            return true;
        }
    }


    public static boolean validateUsername(String username) {
        //if it contains one special character, add 2 to total score
        if (username.matches("(?=.*[~\\[\\],;!@#$%^=&*()'_-]).*")) {
            System.out.println("Username cannot contain special character");
            return false;
        } else {
            return true;
        }
    }

    public static void validate(String input) {
        if (input.equals(null) || input.isEmpty()) {
            System.out.println("Input cannot be empty");
        }
        return;
    }

    public static String checkMenuInput(String message) {
        int verify = 0;
        String checkString = "";
        Scanner sc = new Scanner(System.in);
        while (verify == 0) {
            System.out.println("Please provide " + message + ":");
            checkString = sc.nextLine().trim().toLowerCase();
            if (Util.verifyInput(checkString, message + " cannot be blank")) {
                verify = 1;
            }
        }
        return checkString;
    }

    public static String checkMenuInputNumber(String message) {
        boolean verify = true;
        String checkString = "";
        Scanner sc = new Scanner(System.in);
        while (verify) {
            System.out.println("Please provide " + message + ":");
            checkString = sc.nextLine().trim().toLowerCase();
            if (Util.verifyInput(checkString, message + " cannot be blank")) {
                if (checkString.equals("1") || checkString.equals("2") || checkString.equals("3")){
                    verify = false;
                }
            }
        }
        return checkString;
    }

    public static String checkMenuInputNumberofQuestions(String message) {
        int verify = 0;
        String checkString = "";
        Scanner sc = new Scanner(System.in);
        while (verify == 0) {
            System.out.println("Please provide " + message + ":");
            checkString = sc.nextLine().trim().toLowerCase();
            if (Util.verifyInput(checkString, message + " cannot be blank")) {
                for (int i = 10; i <= 20; i++) {
                    String str = String.valueOf(i);
                    if (str.equalsIgnoreCase(checkString)){
                        verify = 1;
                        break;
                    }
                }
            }
        }
        return checkString;
    }

    public static boolean userExists(String username) {
        int userCheck = 0;
        Map<String, String> userList = new HashMap<String, String>();
        userList.putAll(User.getUsers("src/users/passwords/Regular Users/user_and_passwords.txt"));
        for (Map.Entry<String, String> entry : userList.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(username)) {
                userCheck = 1;
                break;
            }
        }
        if (userCheck == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void printUsers(){
        Map<String, String> userList = new HashMap<String, String>();
        userList.putAll(User.getUsers("src/users/passwords/Regular Users/user_and_passwords.txt"));
        for (Map.Entry<String, String> entry : userList.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    public static boolean quizExists(QuestionUtil qutil, String quiz) {
        int quizCheck = 0;
        ArrayList<Quiz> allQuizes = new ArrayList<>();
        try {
            allQuizes.addAll(qutil.allQuizes);
        } catch (NullPointerException e) {
            System.out.println("No quizzes exist");
        }
        for (int i = 0; i < allQuizes.size(); i++) {
            if (allQuizes.get(i).getName().equalsIgnoreCase(quiz)){
                quizCheck = 1;
            }
        }
        if (quizCheck == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean questionExists(QuestionUtil qutil, String quiz) {
        int questionCheck = 0;
        ArrayList<Question> allQuestions = new ArrayList<>();
        try {
            allQuestions.addAll(qutil.allQuestions);
        } catch (NullPointerException e) {
            System.out.println("No quizzes exist");
        }
        for (int i = 0; i < allQuestions.size(); i++) {
            if (allQuestions.get(i).getName().equalsIgnoreCase(quiz)){
                questionCheck = 1;
            }
        }
        if (questionCheck == 1) {
            return true;
        } else {
            return false;
        }
    }

    private static int calculatePasswordStrength(String password) {

        //total score of password
        int iPasswordScore = 0;

        if (password.length() < 8)
            return 0;
        else if (password.length() >= 10)
            iPasswordScore += 2;
        else
            iPasswordScore += 1;

        //if it contains one digit, add 2 to total score
        if (password.matches("(?=.*[0-9]).*"))
            iPasswordScore += 2;

        //if it contains one lower case letter, add 2 to total score
        if (password.matches("(?=.*[a-z]).*"))
            iPasswordScore += 2;

        //if it contains one upper case letter, add 2 to total score
        if (password.matches("(?=.*[A-Z]).*"))
            iPasswordScore += 2;

        //if it contains one special character, add 2 to total score
        if (password.matches("(?=.*[~!@#$%^&*()_-]).*"))
            iPasswordScore += 2;

        return iPasswordScore;
    }

    public static String fileFormat(String file) {
        return String.format("src/quiz/Q&Qs/%s.txt", file);
    }

}
