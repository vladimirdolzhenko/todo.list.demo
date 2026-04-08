package com.example.todo.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "todos")
data class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(length = 1000)
    var description: String? = null,

    @Column(nullable = false)
    var completed: Boolean = false,

    @Column(nullable = false)
    var priority: Priority = Priority.MEDIUM,

    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "due_date")
    var dueDate: LocalDateTime? = null
) {

    constructor(): this(title = "")
}


enum class Priority {
    LOW, MEDIUM, HIGH, URGENT
}
