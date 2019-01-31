package com.company;

import java.sql.Timestamp;
import java.util.Scanner;

public class Main {
    static AuthorizedUser authorizedUser = null;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DatabaseConnector db = new DatabaseConnector();

        String choice = "-1";
        do {
            System.out.println("1 - Регистрация");
            System.out.println("2 - Авторизация");
            System.out.println("3 - Вывести Логи");
            System.out.println("4 - Вывести авторизованного пользователя");
            System.out.println("0 - Выход");
            choice = sc.nextLine();
            if (choice.equals("1")){
                //register
                System.out.println("Введите логин: ");
                String login = sc.nextLine();
                System.out.println("Введите email: ");
                String email = sc.nextLine();
                System.out.println("Введите пароль: ");
                String password = sc.nextLine();

                User user = new User(login,email,password);
                if (db.register(user)){
                    System.out.println("Registration successful");
                }
                else {
                    System.out.println("Registration failed");
                }
            }
            else if (choice.equals("2")){
                //authorize
                System.out.println("Введите логин: ");
                String login = sc.nextLine();
                System.out.println("Введите пароль: ");
                String password = sc.nextLine();

                User user = new User(login,password);
                authorizedUser = db.authorize(user);
                if (authorizedUser != null){
                    System.out.println("Hello " + authorizedUser.login);
                    System.out.println("Authorization successful");
                }
                else {
                    System.out.println("Authorization failed");
                }
            }
            else if (choice.equals("3")){
                //authorize

            }
            else if (choice.equals("4")){
                //authorize
                System.out.println();
                System.out.println(authorizedUser);
                System.out.println();
            }

        }
        while (!choice.equals("0"));
    }
}


class User {
    String login;
    String email;
    String password;


    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

}

class AuthorizedUser extends User {
    Timestamp date_of_registration;

    public AuthorizedUser(String login, String email, String password, Timestamp date_of_registration) {
        super(login, email, password);
        this.date_of_registration = date_of_registration;
    }

    @Override
    public String toString() {
        return "Login: " + login +
                ", \nEmail: " + email +
                ", \nDate of registration: " + date_of_registration;
    }
}



