# 📚 Course Search Application

A Spring Boot application that indexes and searches educational courses using Elasticsearch. It allows users to filter and sort courses based on multiple criteria such as category, type, age, price, and upcoming session date.

---

## 📖 About the Project

This project demonstrates how to build a full-stack searchable course catalog using:

- ✅ **Spring Boot 3.0+**
- ✅ **Elasticsearch 8.x (via Java Client)**
- ✅ **Lombok** for concise model code
- ✅ **Jackson + JSR310 module** for working with `Instant` and ISO 8601 dates

The application loads course data from a JSON file at startup and provides a `/api/search` endpoint for full-text and filtered search.

---

## ✨ Features

- 🔍 Full-text search on `title` and `description`
- 🎯 Filters:
  - Category
  - Type
  - Minimum & Maximum Age
  - Price Range
  - Upcoming Session Date (`nextSessionDate`)
- 📊 Sorting:
  - `priceAsc` → Low to High
  - `priceDesc` → High to Low
  - `nextSessionDate` (default)
- 📄 Pagination: `page` and `size` parameters

---

## 📦 Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Data Elasticsearch**
- **Elasticsearch Java Client**
- **Lombok**
- **Jackson (JSR310 Module for Java 8+ Time Support)**

---

## 📁 Project Structure

```
src/
 └── main/
     ├── java/
     │   └── com.course.course_search/
     │       ├── CourseSearchApplication.java
     │       ├── controller/
     │       │   └── CourseSearchController.java
     │       ├── service/
     │       │   ├── SearchService.java
     │       │   └── DataLoaderService.java
     │       ├── repository/
     │       │   └── CourseRepository.java
     │       ├── documents/
     │       │   └── CourseDocument.java
     │       └── dto/
     │           ├── SearchRequest.java
     │           └── SearchResponse.java
     ├── resources/
     │   ├── application.yml
     │   ├── sample-courses.json
     │   └── docker-compose.yml
```

---

## ⚙️ Setup Instructions

### 1️⃣ Prerequisites

- Java 17+
- Maven
- Docker (to run Elasticsearch)

---

### 2️⃣ Start Elasticsearch via Docker

```bash
docker run -p 9200:9200 -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  docker.elastic.co/elasticsearch/elasticsearch:8.13.2
```

📍 *Ensure it's accessible at http://localhost:9200*

---

### 3️⃣ Run the Application

```bash
mvn spring-boot:run
```

This will:
- Load `sample-courses.json` from `resources`
- Index the data into Elasticsearch (if not already present)
- Start the Spring Boot server on `http://localhost:8080`

---

## 🔗 API Endpoint

### `GET /api/search`

#### ✅ Query Parameters (All Optional)

| Param         | Type     | Description                              |
|---------------|----------|------------------------------------------|
| `q`           | String   | Full-text query on `title` and `description` |
| `category`    | String   | Filter by course category                |
| `type`        | String   | Filter by course type                   |
| `minAge`      | Integer  | Minimum eligible age                    |
| `maxAge`      | Integer  | Maximum eligible age                    |
| `minPrice`    | Double   | Minimum price of course                 |
| `maxPrice`    | Double   | Maximum price of course                 |
| `startDate`   | String   | ISO date filter (e.g. 2025-08-01T00:00:00Z) |
| `sort`        | String   | `priceAsc`, `priceDesc` or default by date |
| `page`        | Integer  | Page number (default = 0)               |
| `size`        | Integer  | Results per page (default = 10)         |

---

## 📥 Example Request

```
GET http://localhost:8080/api/search?category=Science&type=COURSE&minAge=10&sort=priceAsc
```

---

## ✅ Example JSON Response

```json
[
  {
    "id": "course1",
    "title": "Introduction to Physics",
    "description": "Explore basic concepts in motion, energy, and forces.",
    "category": "Science",
    "type": "COURSE",
    "gradeRange": "6-8",
    "minAge": 10,
    "maxAge": 14,
    "price": 59.99,
    "nextSessionDate": "2025-08-10T00:00:00Z"
  },
  {
    "id": "course2",
    ...
  }
]
```

---

## 🛠 Common Commands

| Command                     | Purpose                              |
|----------------------------|--------------------------------------|
| `docker ps`                | Check if Elasticsearch is running    |
| `curl localhost:9200`      | Ping Elasticsearch                   |
| `mvn spring-boot:run`      | Run Spring Boot app                  |
| `curl "http://localhost:8080/api/search?..."` | Test search endpoint |

---

## ❓Troubleshooting

- **Elasticsearch decoding error**: Ensure `jackson-datatype-jsr310` is added in `pom.xml` and registered in `JacksonConfig`.
- **Repeated course data**: Data is inserted only if the Elasticsearch index is empty.
- **CORS errors**: If integrating with frontend, configure Spring CORS.

---


