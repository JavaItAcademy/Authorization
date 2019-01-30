package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DatabaseConnector db = new DatabaseConnector();

        String choice = "-1";
        do {
            System.out.println("1 - Регистрация");
            System.out.println("2 - Авторизация");
            System.out.println("3 - Вывести Логи");
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
                if (db.authorize(user)){
                    System.out.println("Authorization successful");
                }
                else {
                    System.out.println("Authorization failed");
                }
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
// is_blocked => true  => blocked
//               false => not blocked

}

