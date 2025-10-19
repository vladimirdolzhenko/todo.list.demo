package com.example.todo.service

import com.example.todo.model.Priority
import com.example.todo.model.Todo
import com.example.todo.repository.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class TodoService(private val todoRepository: TodoRepository) {

    fun getAllTodos(): List<Todo> = todoRepository.findAll()

    fun getTodoById(id: Long): Todo? = todoRepository.findById(id).orElse(null)

    fun getActiveTodos(): List<Todo> = todoRepository.findByCompletedFalse()

    fun getCompletedTodos(): List<Todo> = todoRepository.findByCompletedTrue()

    fun getTodosByPriority(priority: Priority): List<Todo> =
        todoRepository.findByPriority(priority)

    fun createTodo(todo: Todo): Todo {
        return todoRepository.save(todo.copy(
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ))
    }

    fun updateTodo(id: Long, updatedTodo: Todo): Todo? {
        return todoRepository.findById(id).map { existingTodo ->
            val updated = existingTodo.copy(
                title = updatedTodo.title,
                description = updatedTodo.description,
                priority = updatedTodo.priority,
                dueDate = updatedTodo.dueDate,
                updatedAt = LocalDateTime.now()
            )
            todoRepository.save(updated)
        }.orElse(null)
    }

    fun toggleComplete(id: Long): Todo? {
        return todoRepository.findById(id).map { todo ->
            val updated = todo.copy(
                completed = !todo.completed,
                updatedAt = LocalDateTime.now()
            )
            todoRepository.save(updated)
        }.orElse(null)
    }

    fun deleteTodo(id: Long): Boolean {
        return if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun deleteCompletedTodos(): Int {
        val completed = todoRepository.findByCompletedTrue()
        todoRepository.deleteAll(completed)
        return completed.size
    }
}
