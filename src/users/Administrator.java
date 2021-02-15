package users;

import quiz.Quiz;

import java.io.*;

import quiz.Question;
import utils.Menu;
import utils.Util;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Administrator extends User {

    public Administrator(String username, String password) {
        super(username, password);
    }

    public static Administrator adminLogin(String[] userAndPass) {
        Administrator user = new Administrator(userAndPass[0], userAndPass[1]);
        return user;
    }

    public static void assignQuiz(String quiz, String user) {
        String filePath = "src/users/AssignedQuizzes/" + user + ".txt";
        try {
            Files.write(Paths.get(filePath), (quiz + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("File does not exist!");
        }
    }

    public static void seeUserResults(String user) {
        String filePath = "src/users/QuizResults/" + user + ".txt";
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
            System.out.println("There are no results for this user");
            return;
        }

        if (quizList.size() == 0){
            System.out.println("This user has no completed quizzes");
        }else{
            System.out.println("Please see below quiz results for user " + user);
            for (int i = 0; i < quizList.size(); i++) {
                String[] resultsString = quizList.get(i).split(",");
                String quizName = resultsString[0];
                String quizResult = resultsString[1];
                System.out.println("Your result from quiz " + quizName + " is " + quizResult);
            }
        }
    }

    public static List<String> loadAllQuizResults() {
        final File folder = new File("src/users/QuizResults");
        List<String> result = new ArrayList<>();
        search(".*\\.txt", folder, result);
        return result;
    }

    public static void search(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {
            if (f.isDirectory()) {
                search(pattern, f, result);
            }
            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }
        }
    }
}
