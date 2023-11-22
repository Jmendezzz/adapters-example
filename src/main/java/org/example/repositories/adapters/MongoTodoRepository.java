package org.example.repositories.adapters;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.example.infraestructure.singleton.MongoConnection;
import org.example.models.Todo;
import org.example.repositories.ports.TodoRepository;

import java.util.ArrayList;
import java.util.List;

public class MongoTodoRepository implements TodoRepository {
    private MongoCollection<Document> todoCollection;
    public MongoTodoRepository(){
        todoCollection = MongoConnection.getInstance().mongoDatabase.getCollection("todo");
    }

    @Override
    public Todo create(Todo todo) {
        Document todoDocument = new Document()
                .append("description", todo.getDescription())
                .append("isDone", todo.getDone());

        todoCollection.insertOne(todoDocument);
        todo.setId(Long.valueOf(todoDocument.getObjectId("_id").toString()));
        return todo;
    }

    @Override
    public Todo getById(Long id) {
        Document query = new Document("_id", id);
        Document todoDocument = todoCollection.find(query).first();

        return documentToTodo(todoDocument);
    }

    @Override
    public List<Todo> getALl() {
        List<Todo> todos = new ArrayList<>();
        FindIterable<Document> todoDocuments = todoCollection.find();

        try (MongoCursor<Document> cursor = todoDocuments.iterator()) {
            while (cursor.hasNext()) {
                Document todoDocument = cursor.next();
                Todo todo = documentToTodo(todoDocument);
                todos.add(todo);
            }
        }

        return todos;
    }

    @Override
    public Todo update(Todo todo) {
        Document query = new Document("_id", todo.getId());
        Document update = new Document("$set", new Document()
                .append("description", todo.getDescription())
                .append("isDone", todo.getDone()));

        todoCollection.updateOne(query, update);

        return todo;
    }

    @Override
    public Todo deleteById(Long id) {
        Document query = new Document("_id", id);
        Document todoDocument = todoCollection.findOneAndDelete(query);

        return documentToTodo(todoDocument);
    }
    private Todo documentToTodo(Document todoDocument) {
        if (todoDocument == null) {
            return null;
        }

        Long id = todoDocument.getLong("_id");
        String description = todoDocument.getString("description");
        Boolean isDone = todoDocument.getBoolean("isDone");

        return new Todo(id, description, isDone);
    }
}
