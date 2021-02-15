package users;

import quiz.Quiz;
import utils.Menu;
import utils.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.List;

public class User {
        private static String filePath = "src/users/passwords/Regular Users/user_and_passwords.txt";
    private static String username;
    private static String password;
    protected List<Quiz> quiz;

    public User(String username,String password){
        this.username = username;
        this.password = password;
    }

    //create user can(should) be moved to the default constructor
    public static void createUser() {
        Scanner sc = new Scanner(System.in);
        String input_username = "";
        String input_password = "";
        Map<String, String> userList = new HashMap<String, String>();
        userList.putAll(getUsers(filePath));
        int userCheck = 0;
        do {
            int verify = 0;
            System.out.println("Username:");
            input_username = sc.nextLine().trim().toLowerCase();
            if (!Util.verifyInput(input_username, "Username cannot be blank")) {
                verify = 1;
            }
            if (!Util.validateUsername(input_username)) {
                verify = 1;
            }
            for (Map.Entry<String, String> entry : userList.entrySet()) {
                if (entry.getKey().equals(input_username)) {
                    System.out.println("User name already exists!");
                    verify = 1;
                    break;
                }
            }
            if (verify == 0) {
                userCheck = 1;
            }
        } while (userCheck == 0);

        userCheck = 0;

        do {
            int verify = 0;
            System.out.println("Password:");
            input_password = sc.nextLine().trim().toLowerCase();
            if (!Util.verifyInput(input_password, "Password cannot be blank")) {
                verify = 1;
            }
            ;
            if (verify == 0) {
                userCheck = 1;
            }
        } while (userCheck == 0);
//        RegularUser user = new RegularUser(input_username, input_password);
        Map<String, String> userPass = new HashMap<String, String>();
        userPass.put(input_username, input_password);
        System.out.println("User " + input_username + " has been created!");
        saveUser(input_username, input_password);
        try {
            File myObj = new File("src/users/AssignedQuizzes/" + input_username + ".txt");
            myObj.createNewFile();
            File myObjTwo = new File("src/users/QuizResults/" + input_username + ".txt");
            myObjTwo.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        Menu.intro();
    }

    public static String[] login(String userType) {
        Map<String, String> userList = new HashMap<String, String>();
        String userFilePath = "";
        int userCheck = 0;
        String username_input = "";
        String password_input = "";
        do {
            int verify = 0;
            Scanner sc = new Scanner(System.in);
            System.out.println("Please, provide a user name:");
            username_input = sc.nextLine().trim().toLowerCase();
            System.out.println("Please, provide a password:");
            password_input = sc.nextLine().trim().toLowerCase();
            if (!Util.verifyInput(password_input, "Password cannot be blank")) {
                verify = 1;
            }
            if (!Util.verifyInput(username_input, "Username cannot be blank")) {
                verify = 1;
            }

            if (verify == 0) {
                if (userType.equals("Regular user")){
                    userFilePath = filePath;
                }else if (userType.equals("Administrator")){
                    userFilePath = "src/users/passwords/Administrators/user_and_passwords.txt";
                }
                userList.putAll(getUsers(userFilePath));
                for (Map.Entry<String, String> entry : userList.entrySet()) {
                    if (entry.getKey().equals(username_input) && entry.getValue().equals(password_input)) {
                        userCheck = 1;
                        break;
                    }
                }
                if (userCheck == 1) {
                    System.out.println("You are now logged in");
                } else {
                    System.out.println("Incorrect username or password, please try again!");
                }
            }
        } while (userCheck == 0);
        String[] arr = new String[] {username_input,password_input};
        return arr;
    }

    public static void saveUser(String key, String value) {
        try {
            Files.write(Paths.get(filePath), (key + "," + value + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("File does not exist!");
        }
    }

    public static Map<String, String> getUsers(String filePath) {
        Map<String, String> userAndPassList = new HashMap<String, String>();

        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String keyValue = myReader.nextLine();
                int splitter = keyValue.indexOf(",");
                userAndPassList.put(keyValue.substring(0, splitter), keyValue.substring(splitter + 1));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return userAndPassList;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }

    protected  void setQuiz(List<Quiz> quiz) {
        this.quiz = quiz;
    }
}

