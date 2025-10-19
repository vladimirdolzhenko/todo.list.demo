let currentFilter = 'all';

document.addEventListener('DOMContentLoaded', () => {
    loadTodos();
});

async function loadTodos() {
    try {
        const response = await fetch(`/api/todos?filter=${currentFilter}`);
        const todos = await response.json();
        renderTodos(todos);
        updateStats(todos);
    } catch (error) {
        console.error('Error loading todos:', error);
    }
}

function renderTodos(todos) {
    const todoList = document.getElementById('todoList');

    if (todos.length === 0) {
        todoList.innerHTML = `
            <div class="empty-state">
                <h2>No todos yet!</h2>
                <p>Add your first todo to get started.</p>
            </div>
        `;
        return;
    }

    todoList.innerHTML = todos.map(todo => `
        <div class="todo-item ${todo.completed ? 'completed' : ''} priority-${todo.priority}">
            <input
                type="checkbox"
                class="todo-checkbox"
                ${todo.completed ? 'checked' : ''}
                onchange="toggleTodo(${todo.id})"
            />
            <div class="todo-content">
                <h3>${escapeHtml(todo.title)}</h3>
                ${todo.description ? `<p>${escapeHtml(todo.description)}</p>` : ''}
                <div class="todo-meta">
                    <span class="priority-badge ${todo.priority}">${todo.priority}</span>
                    ${todo.dueDate ? `<span>📅 Due: ${formatDate(todo.dueDate)}</span>` : ''}
                    <span>Created: ${formatDate(todo.createdAt)}</span>
                </div>
            </div>
            <div class="todo-actions">
                <button class="delete-btn" onclick="deleteTodo(${todo.id})">Delete</button>
            </div>
        </div>
    `).join('');
}

async function addTodo() {
    const title = document.getElementById('todoTitle').value.trim();
    const description = document.getElementById('todoDescription').value.trim();
    const priority = document.getElementById('todoPriority').value;
    const dueDate = document.getElementById('todoDueDate').value;

    if (!title) {
        alert('Please enter a todo title');
        return;
    }

    const todo = {
        title,
        description: description || null,
        priority,
        dueDate: dueDate || null,
        completed: false
    };

    try {
        const response = await fetch('/api/todos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(todo)
        });

        if (response.ok) {
            document.getElementById('todoTitle').value = '';
            document.getElementById('todoDescription').value = '';
            document.getElementById('todoDueDate').value = '';
            loadTodos();
        }
    } catch (error) {
        console.error('Error adding todo:', error);
        alert('Failed to add todo');
    }
}

async function toggleTodo(id) {
    try {
        const response = await fetch(`/api/todos/${id}/toggle`, {
            method: 'PATCH'
        });

        if (response.ok) {
            loadTodos();
        }
    } catch (error) {
        console.error('Error toggling todo:', error);
    }
}

async function deleteTodo(id) {
    if (!confirm('Are you sure you want to delete this todo?')) {
        return;
    }

    try {
        const response = await fetch(`/api/todos/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            loadTodos();
        }
    } catch (error) {
        console.error('Error deleting todo:', error);
    }
}

async function clearCompleted() {
    if (!confirm('Delete all completed todos?')) {
        return;
    }

    try {
        const response = await fetch('/api/todos/completed', {
            method: 'DELETE'
        });

        if (response.ok) {
            loadTodos();
        }
    } catch (error) {
        console.error('Error clearing completed todos:', error);
    }
}

function filterTodos(filter) {
    currentFilter = filter;

    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });

    event.target.classList.add('active');
    loadTodos();
}

function updateStats(todos) {
    const activeCount = todos.filter(todo => !todo.completed).length;
    document.getElementById('todoCount').textContent =
        `${activeCount} item${activeCount !== 1 ? 's' : ''} left`;
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Allow Enter key to add todo
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('todoTitle').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            addTodo();
        }
    });
});
