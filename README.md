# 🎬 Movie Journal API

A RESTful API built with **Spring Boot** and **PostgreSQL** that allows users to track, manage, and organize their personal movie journal. The application features full JWT authentication, user-data isolation, Swagger API documentation, and complete containerization via **Docker & Docker Compose**.

---

## 🚀 Features

* 🔐 **Authentication & Security:** JWT-based authentication with Spring Security.
* 👤 **User Data Isolation:** Users can only view, add, or manage their own movie entries.
* 🗄️ **Database Integration:** Relational database storage powered by **PostgreSQL**.
* 🐳 **Dockerized Setup:** Fully containerized setup with multi-stage Docker builds and `docker-compose`.
* 📖 **API Documentation:** Interactive Swagger UI for testing API endpoints.

---

## 🛠️ Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3
* **Security:** Spring Security & JSON Web Tokens (JWT)
* **Database:** PostgreSQL
* **ORM:** Spring Data JPA / Hibernate
* **Containerization:** Docker & Docker Compose
* **Documentation:** Springdoc OpenAPI / Swagger UI
* **Build Tool:** Maven

---

## 📋 Prerequisites

Make sure you have the following installed on your machine:

* [Docker Desktop](https://www.docker.com/products/docker-desktop/) (includes Docker Engine & Docker Compose)
* [Git](https://git-scm.com/)

---

## ⚙️ How to Run with Docker (Recommended)

Thanks to Docker Compose, you don't need Java or PostgreSQL installed locally to run the application.

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/RaresPituru/movie-journal-api.git]
   cd movie-journal-api