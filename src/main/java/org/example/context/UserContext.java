package org.example.context;

import org.example.repositories.adapters.MySQLTodoRepository;
import org.example.repositories.ports.TodoRepository;

public class UserContext {
    private TodoRepository todoRepository;

    public TodoRepository getTodoRepository() {
        return todoRepository;
    }

    public void setTodoRepository(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
}
