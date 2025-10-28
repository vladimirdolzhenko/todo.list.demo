package com.example.todo

import com.example.todo.model.Todo
import com.example.todo.service.TodoService
import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.core.env.PropertyResolver
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.bodyWithType
import org.springframework.web.servlet.function.router

class Beans: BeanRegistrarDsl({
    registerBean(::mainRouter)
})

fun mainRouter(
    todoService: TodoService,
    resolver: PropertyResolver
) = router {
    GET("/") {
        val model = mapOf(
            "title" to resolver.getProperty("application.title", String::class.java, "Application title")
        )
        ok().render("index", model)
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

    DELETE("/api/todos/completed") {
        val count = todoService.deleteCompletedTodos()
        ok().body(count)
    }
}
