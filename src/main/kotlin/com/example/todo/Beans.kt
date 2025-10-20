package com.example.todo

import com.example.todo.model.Priority
import com.example.todo.model.Todo
import com.example.todo.service.TodoService
import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.bodyWithType
import org.springframework.web.servlet.function.router

class Beans: BeanRegistrarDsl({
    registerBean(::mainRouter, "mainRouter")
})

fun mainRouter(todoService: TodoService) = router {

    GET("/") {
        withAttribute("todos", todoService.getAllTodos())
        withAttribute("priorities", Priority.values())
        ok().render("index")
    }

    GET("/api/todos/{id}") { request ->
        val id = request.pathVariable("id").toLong()
        todoService.getTodoById(id)?.let {
            ok().contentType(APPLICATION_JSON).bodyWithType(it)
        } ?: notFound().build()
    }

    GET("/api/todos") {
        it.params()["filter"]?.firstOrNull()?.let { filter ->
            return@GET when (filter) {
                "active" -> ok().contentType(APPLICATION_JSON)
                    .bodyWithType(todoService.getActiveTodos())

                "completed" -> ok().contentType(APPLICATION_JSON)
                    .bodyWithType(todoService.getCompletedTodos())

                else -> ok().contentType(APPLICATION_JSON)
                    .bodyWithType(todoService.getAllTodos())
            }
        }
        ok().contentType(APPLICATION_JSON).bodyWithType(todoService.getAllTodos())
    }

    POST("/api/todos") { request ->
        val todo: Todo = request.body(Todo::class.java)
        val created = todoService.createTodo(todo)
        status(HttpStatus.CREATED).body(created)
    }

    PUT("/api/todos/{id}") { request ->
        val id = request.pathVariable("id").toLong()
        val todo: Todo = request.body(Todo::class.java)
        todoService.updateTodo(id, todo)?.let {
            ok().contentType(APPLICATION_JSON).bodyWithType(it)
        } ?: notFound().build()
    }

    PATCH("/api/todos/{id}/toggle") {
        val id = it.pathVariable("id").toLong()
        todoService.toggleComplete(id)?.let { updated ->
            ok().contentType(APPLICATION_JSON).bodyWithType(updated)
        } ?: notFound().build()
    }

    DELETE("/api/todos/{id}") { request ->
        val id = request.pathVariable("id").toLong()
        val deleted = todoService.deleteTodo(id)
        if (deleted) noContent().build() else notFound().build()
    }

    DELETE("/api/todos/completed") { request ->
        val count = todoService.deleteCompletedTodos()
        ok().body(count)
    }
}