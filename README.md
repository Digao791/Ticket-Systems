# Tickets — Ticket Management System

A web-based ticket management system built with **Java, Spring Boot, Maven, PostgreSQL, Flyway, Thymeleaf, and Docker**.

The application provides a centralized environment for managing projects, tracking tickets, assigning responsibilities, and monitoring activities.

The project also integrates **GitHub Actions and GitHub Copilot** to support continuous integration and automated test generation.

## Features

### Authentication and User Management
- User authentication using Spring Security.
- Password encryption using BCrypt.
- Protected routes and authenticated sessions.
- User accounts with roles and activation status.
- User identification and ownership tracking.

### Ticket Management
- Create, view, update, and manage tickets.
- Assign tickets to registered users.
- Associate tickets with projects.
- Track ticket status and progress.
- Manage ticket priority and relevant information.

### Project Management
- Create and manage projects.
- Organize tickets by project.
- Associate project information with authenticated users.

### Comments and Activity Tracking
- Add comments to tickets.
- Maintain ticket activity records.
- Track relevant changes and interactions.

### Database Management
- PostgreSQL for persistent data storage.
- Spring Data JPA and Hibernate for database operations.
- Flyway for version-controlled database migrations.
- Persistent Docker volumes to preserve application data.

### CI/CD and Automated Testing
- GitHub Actions for continuous integration.
- Maven for building and testing the application.
- JUnit 5 and Spring Boot Test for automated testing.
- GitHub Copilot agent for generating missing tests.
- Custom agent instructions and reusable testing skills.
- Automated Pull Request creation for generated tests.

## Technology Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Backend | Spring Boot |
| Security | Spring Security |
| Frontend | Thymeleaf, HTML, CSS |
| Database | PostgreSQL 18 |
| ORM | Spring Data JPA / Hibernate |
| Database migrations | Flyway |
| Build tool | Maven |
| Containerization | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| Testing | JUnit 5, Spring Boot Test |
| AI automation | GitHub Copilot CLI |

## Prerequisites

To run the application, install:

- Git
- Docker Desktop or Docker Engine
- Docker Compose

Java and Maven are optional for local development because the application can be built and executed using Docker.

Verify your installation:

```bash
git --version
docker --version
docker compose version
```

Make sure Docker is running before starting the application.

## Getting Started

### 1. Clone the repository

```bash
git clone <REPOSITORY_URL>
cd tickets
```

### 2. Build and start the application

```bash
docker compose up -d --build
```

Docker Compose will:

1. Create and start the PostgreSQL container.
2. Build the Spring Boot application image.
3. Start the application container.
4. Connect the application to PostgreSQL.
5. Execute pending Flyway migrations during application startup.
6. Initialize the application.

### 3. Access the application

Open:

```text
http://localhost:8080
```

Unauthenticated users are redirected to the login page.

## Docker Architecture

The application uses Docker Compose to orchestrate the backend and database services.

```text
                  Web Browser
                      |
                      | HTTP :8080
                      |
              +------------------+
              |   Spring Boot    |
              |   Ticket System  |
              |                  |
              |   Thymeleaf      |
              |   Spring Security|
              |   JPA / Flyway   |
              +------------------+
                      |
                      | JDBC :5432
                      |
              +------------------+
              |   PostgreSQL 18  |
              |                  |
              |   tickets DB     |
              +------------------+
                      |
              +------------------+
              |   Docker Volume  |
              |   postgres_data  |
              +------------------+
```

The Spring Boot application communicates with PostgreSQL through Docker's internal network using the database service name.

The internal JDBC connection is:

```text
jdbc:postgresql://database:5432/tickets
```

The database hostname is `database`, not `localhost`, because both services run inside Docker.

## Docker Commands

### Start the application

```bash
docker compose up -d
```

### Rebuild and start

Use this command after modifying application code or dependencies:

```bash
docker compose up -d --build
```

### Stop the application

```bash
docker compose down
```

### Restart services

```bash
docker compose restart
```

### View running containers

```bash
docker compose ps
```

### View application logs

```bash
docker compose logs -f app
```

### View database logs

```bash
docker compose logs -f database
```

### Remove containers and database volumes

**Warning:** This command permanently removes database data stored in the Compose-managed volumes.

```bash
docker compose down -v
```

Only use it when intentionally resetting the development environment.

## Database Configuration

The application uses PostgreSQL 18.

Database connection settings are configured through environment variables.

Example `application.properties`:

```properties
spring.application.name=tickets

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/tickets}
spring.datasource.username=${DB_USERNAME:tickets}
spring.datasource.password=${DB_PASSWORD:tickets}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

When running with Docker Compose, environment variables override the local defaults.

Example:

```yaml
environment:
  DB_URL: jdbc:postgresql://database:5432/tickets
  DB_USERNAME: tickets
  DB_PASSWORD: tickets
```

The credentials shown above are development examples and must not be used as production secrets.

## Database Migrations

Flyway manages database schema changes.

Migration files are located at:

```text
src/main/resources/db/migration/
```

The database currently includes migrations for:

- Initial ticket-related tables.
- Ticket comments.
- Ticket status.
- Ticket activities.
- User accounts.
- Ticket ownership relationships.
- Activity ownership relationships.
- Projects.

Flyway automatically applies pending migrations when the application starts.

Example migration naming:

```text
V10__add_project_description.sql
V11__add_ticket_priority.sql
```

Once a migration has been applied or shared, avoid modifying it. Create a new migration instead.

Hibernate uses:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

This ensures that Hibernate validates the database schema rather than creating or modifying tables automatically.

## Database Access

Access the PostgreSQL container:

```bash
docker exec -it tickets-postgres psql -U tickets -d tickets
```

Useful PostgreSQL commands:

```sql
\dt
```

List all database tables.

```sql
SELECT * FROM flyway_schema_history;
```

View applied Flyway migrations.

```sql
SELECT id, name, email, role, enabled FROM users;
```

View registered users.

Exit PostgreSQL:

```text
\q
```

## Authentication and Security

The application uses Spring Security for authentication and authorization.

User passwords are stored using BCrypt hashing.

Protected routes require authentication, and authenticated user information is retrieved from the security context.

The application supports user roles and account activation status.

For production deployments:

- Use strong passwords.
- Store secrets in environment variables or a secrets manager.
- Enable HTTPS.
- Restrict database exposure.
- Avoid committing credentials to the repository.

## Automated Testing

The project uses Maven and JUnit 5 for automated testing.

Run the tests locally:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

Tests are located in:

```text
src/test/java/
```

The testing strategy includes unit tests for business logic and Spring Boot tests where framework integration is required.

## AI-Powered Test Generation

The repository includes a GitHub Copilot agent designed to generate automated tests.

The agent analyzes Java classes, identifies missing test coverage, and generates tests according to project-specific rules.

### Test Generation Modes

| Mode | Description |
|---|---|
| `changes` | Generates tests for newly modified Java classes |
| `missing` | Analyzes the repository and generates missing tests |

The default mode is `changes`.

### Automated Workflow

```text
Push to main
      |
      v
GitHub Actions
      |
      v
Identify modified Java classes
      |
      v
GitHub Copilot Agent
      |
      v
Generate JUnit tests
      |
      v
Execute Maven tests
      |
      v
Create automation branch
      |
      v
Open Pull Request
      |
      v
Review and merge into main
```

Generated tests are created on separate branches.

The agent does not push directly to the main branch.

### Agent and Skill Configuration

The repository includes:

```text
.github/
├── agents/
│   └── test-generator.agent.md
├── skills/
│   └── test-generation/
│       └── SKILL.md
└── workflows/
    ├── ci.yml
    └── test-generator.yml
```

**Test Generator Agent**

Responsible for:

- Identifying changed Java classes.
- Inspecting existing tests.
- Generating missing tests.
- Running the Maven test suite.
- Reporting test results.

**Test Generation Skill**

Defines testing conventions:

- JUnit 5 and Spring Boot Test.
- Mockito when dependency isolation is required.
- Positive and negative test scenarios.
- No modifications to production code.
- No modifications to database migrations.
- Preservation of existing tests.

### Running Test Generation Manually

1. Open the repository on GitHub.
2. Navigate to **Actions**.
3. Select **AI Test Generator**.
4. Click **Run workflow**.
5. Choose `changes` or `missing`.
6. Start the workflow.

When new tests are generated and validation succeeds, the workflow creates a Pull Request.

### Copilot Authentication

The GitHub Actions workflow requires a repository secret:

```text
COPILOT_TOKEN
```

The token must support GitHub Copilot requests.

Never commit authentication tokens to the repository.

## Project Structure

```text
tickets/
├── .github/
│   ├── agents/
│   │   └── test-generator.agent.md
│   ├── skills/
│   │   └── test-generation/
│   │       └── SKILL.md
│   └── workflows/
│       ├── ci.yml
│       └── test-generator.yml
│
├── .mvn/
├── infra/
│   └── Dockerfile
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/rodrigo/tickets/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── db/
│   │       │   └── migration/
│   │       ├── templates/
│   │       └── static/
│   │
│   └── test/
│       └── java/
│
├── compose.yaml
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

### Main Components

| Directory | Responsibility |
|---|---|
| `controller/` | HTTP requests and web navigation |
| `dto/` | Data transfer objects |
| `entity/` | JPA entities |
| `repository/` | Database access |
| `service/` | Business logic |
| `db/migration/` | Flyway migrations |
| `templates/` | Thymeleaf views |
| `static/` | CSS and static assets |
| `src/test/` | Automated tests |
| `.github/agents/` | Custom AI agents |
| `.github/skills/` | Reusable AI instructions |
| `.github/workflows/` | CI/CD automation |
| `infra/` | Docker build configuration |

## Development Workflow

Create a feature branch:

```bash
git checkout main
git pull origin main
git checkout -b feature/my-feature
```

Implement your changes.

Rebuild the application:

```bash
docker compose up -d --build
```

Commit and push:

```bash
git add .
git commit -m "feat: implement new feature"
git push -u origin feature/my-feature
```

Open a Pull Request targeting `main`.

After merging:

```bash
git checkout main
git pull origin main
docker compose up -d --build
```

The CI workflow validates the application, while the AI test generator can create additional tests for newly integrated Java code.

## Troubleshooting

### Docker is not running

Ensure Docker Desktop or Docker Engine is running.

On Windows, verify that WSL 2 and hardware virtualization are enabled.

### Port 8080 is already in use

Check whether another application or container is using port 8080.

### Database connection failed

Check the container status:

```bash
docker compose ps
```

Review PostgreSQL logs:

```bash
docker compose logs database
```

Verify that the application uses the correct internal database hostname and credentials.

### Flyway migration failed

Check the application logs:

```bash
docker compose logs app
```

Do not modify previously applied migrations. Create a new versioned migration for schema changes.

### Authentication failed

Verify that the user exists and that the stored password uses BCrypt hashing.

### Copilot authentication failed

Verify that the `COPILOT_TOKEN` repository secret exists and that the token has the required Copilot Requests permission.

### Generated tests fail

Review the test-generation workflow logs and Maven test reports.

Generated tests should be reviewed before merging.

## Production Considerations

The current Docker Compose configuration is intended primarily for development.

Before deploying to production, consider:

- HTTPS and reverse proxy configuration.
- Secure environment variables.
- Database backups.
- Persistent storage.
- Container health checks.
- Resource limits.
- Monitoring and application logging.
- Secure user and role management.
- CI/CD deployment controls.

## License

No license has been specified for this project.
