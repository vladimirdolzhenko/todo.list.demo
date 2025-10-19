package com.example.todo.controller;

import com.example.todo.model.Priority;
import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("todos", todoService.getAllTodos());
        model.addAttribute("priorities", Priority.values());
        return "index";
    }

    @GetMapping("/api/todos")
    @ResponseBody
    public List<Todo> getAllTodos(@RequestParam(required = false) String filter) {
        if (filter != null) {
            return switch (filter) {
                case "active" -> todoService.getActiveTodos();
                case "completed" -> todoService.getCompletedTodos();
                default -> todoService.getAllTodos();
            };
        }
        return todoService.getAllTodos();
    }

    @GetMapping("/api/todos/{id}")
    @ResponseBody
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.getTodoById(id);
        return todo != null ? ResponseEntity.ok(todo) : ResponseEntity.notFound().build();
    }

    @PostMapping("/api/todos")
    @ResponseBody
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo created = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/api/todos/{id}")
    @ResponseBody
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Todo updated = todoService.updateTodo(id, todo);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/api/todos/{id}/toggle")
    @ResponseBody
    public ResponseEntity<Todo> toggleComplete(@PathVariable Long id) {
        Todo updated = todoService.toggleComplete(id);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/todos/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        boolean deleted = todoService.deleteTodo(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/todos/completed")
    @ResponseBody
    public ResponseEntity<Integer> deleteCompletedTodos() {
        int count = todoService.deleteCompletedTodos();
        return ResponseEntity.ok(count);
    }
}
