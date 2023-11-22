package org.example;

import org.example.context.UserContext;
import org.example.repositories.adapters.MySQLTodoRepository;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        UserContext userContext = new UserContext();
        userContext.setTodoRepository(new MySQLTodoRepository());


    }
}