package org.example.repositories.adapters;

import org.example.infraestructure.singleton.MySQLConnection;
import org.example.models.Todo;
import org.example.repositories.ports.TodoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MySQLTodoRepository implements TodoRepository {

    private Connection connection;
    public MySQLTodoRepository(){
        try{
            connection = MySQLConnection.getInstance();

        }catch (SQLException e) {
            System.out.printf("Error en la conexion: " + e.getMessage());
        }
    }
    @Override
    public Todo create(Todo todo) {
        String insertQuery = "INSERT INTO todo (description, is_done) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, todo.getDescription());
            preparedStatement.setBoolean(2, todo.getDone());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creación del todo fallida, no se generaron claves.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    todo.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creación del todo fallida, no se generaron claves.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todo;
    }

    @Override
    public Todo getById(Long id) {
        String selectByIdQuery = "SELECT * FROM todo WHERE id = ?";
        Todo todo = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectByIdQuery)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String description = resultSet.getString("description");
                    Boolean isDone = resultSet.getBoolean("is_done");
                    todo = new Todo(id, description, isDone);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todo;
    }

    @Override
    public List<Todo> getALl() {
        List<Todo> todos = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM todo";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                Boolean isDone = resultSet.getBoolean("is_done");

                Todo todo = new Todo(id, description, isDone);
                todos.add(todo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todos;
    }

    @Override
    public Todo update(Todo todo) {
        String updateQuery = "UPDATE todo SET description = ?, is_done = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, todo.getDescription());
            preparedStatement.setBoolean(2, todo.getDone());
            preparedStatement.setLong(3, todo.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Actualización del todo fallida, no se encontró el registro.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todo;
    }

    @Override
    public Todo deleteById(Long id) {
        String deleteQuery = "DELETE FROM todo WHERE id = ?";
        Todo todo = getById(id);

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todo;
    }
}
