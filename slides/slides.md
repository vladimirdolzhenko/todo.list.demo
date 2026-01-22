---
theme: uncover
paginate: true
---
<!-- $ cd ./slides -->
<!-- $ $ npx @marp-team/marp-cli@latest -w slides.md -->
<style>
ul, ol {
    width: 60%;
    font-size: 1.2em;
}
</style>
# Better Together: Kotlin, IntelliJ, Spring 7.0 and Debugging

Vladimir Dolzhenko, 
IntelliJ Kotlin plugin teamlead, JetBrains

---

# K2 mode 

default since IJ 2025.1

---

# What is K2 mode

IntelliJ Kotlin plugin
based on Kotlin 2.0+ compiler

---

# Kotlin 2.0+ compiler

- Addressed tech debt of K1 
- Easy to add new lang features

---

# K2 mode adoption

2025.2 IJ Ultimate:

| Mode | Adoption    |
|------|-------------|
| K1   | 2.3-2.7%    |
| K2   | 97.3-97.7%  |

---

# K2 mode: feedback

https://kotl.in/slack

https://kotl.in/plugin-issue

---

# Kotlin & Spring collab

---

![](images/kotlin_spring.jpeg)

https://twitter.com/kotlin/status/1959892001823895706

---

# Docs & Kotlin examples

https://docs.spring.io/spring-framework/reference/web/webmvc-functional.html

---

# Kotlin APIs 
# in 
# Spring framework

---

# router DSL

---

# Kotlin extension

Replace `resolver.getProperty`

---

# Physical vs Logical view

- router DSL
- jpa entity

---

# TodoApplication

- Run `TodoApplication`
- Open http://localhost:8080/

---

# Spring Debugger

A standalone plugin for IJ 2025.2+

---

- Stop app 
- Open project view

---

# Place a breakpoint

at `Beans.kt` on line `ok().render("index", model)`

---

# Open _Database_ view

---

# Start debugging app

---

# _Database_ view 

`TodoApplication` is appeared 

---

# Active files

`application-demo.properties`
&
`application-local.properties`

---

# `application-demo.properties`

`spring.application.name=Demo`
points actual value is different

---

# Open/refresh 

http://localhost:8080/

---

# Spring properties

```
spring.application.pid
```

---

# Source of Spring property and Navigate

```
application.title
```

---

# Entity manager

```
entityManager.createQuery("from Todo").resultList
```

---

# beanDefinitionNames
```
(beanNameViewResolver.applicationContext 
as AnnotationConfigServletWebServerApplicationContext).
beanDefinitionNames
```

---

# Q / A