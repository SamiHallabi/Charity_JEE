# Charity_JEE

Charity_JEE is a Spring Boot web application for managing charity actions, donations, participations, organizations, and administrator approval workflows.

The application provides both:

- a Thymeleaf web interface for visitors, users, organizations, and super admins;
- REST APIs for users, organizations, actions, donations, participations, media, and admin operations.

## Main Features

- Public home page and charity action exploration.
- User registration and login.
- Organization registration with manual approval by a super admin.
- Approved organization workspace to create, edit, archive, and manage media for charity actions.
- Donations and participation tracking.
- Super admin dashboard for organization approval/rejection and user management.
- File upload support for action media.
- Email service hooks for welcome, donation confirmation, and organization approval messages.
- Stripe integration hooks for payment flow.
- Google OAuth2 configuration support.

## User Roles

### Visitor

A visitor can:

- view the home page;
- explore active charity actions;
- search/filter actions;
- register as a normal user;
- register an organization;
- log in.

### User

A registered user can:

- manage their profile;
- donate to charity actions;
- participate in charity actions;
- view donation history;
- view participation history.

### Organization Admin

In this project, an organization account is also the administrator account for that organization.

When an organization registers, it is created with:

```text
status = PENDING
```

While pending, the organization cannot access its account. A super admin must approve it first. Once approved, it can log in with role:

```text
ROLE_ADMIN_ORG
```

Then it can:

- access `/org/actions`;
- create charity actions;
- edit its own actions;
- archive its own actions;
- upload and delete media for its own actions;
- view collection progress and collected amounts.

### Super Admin

A super admin can:

- access `/admin/dashboard`;
- view pending organizations;
- approve or reject organizations;
- view all organizations;
- view and delete users.

The default super admin is created automatically by `DataInitializer`:

```text
Email: admin@charity.com
Password: Admin1234!
```

## Tech Stack

- Java 17
- Spring Boot 4.0.5
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Lombok
- Bootstrap 5
- Bootstrap Icons
- Stripe Java SDK
- Java Mail
- Google OAuth2 client
- MongoDB dependencies are present, but Mongo auto-configuration is currently disabled in `CharityApplication`.

## Project Structure

```text
src/main/java/com/ag/charity
  config/              Spring Security, web, Mongo, and data initialization config
  controller/          REST controllers
  controller/mvc/      Thymeleaf MVC controllers
  DTO/                 Request and response DTOs
  entities/enums/      Application enums
  entities/jpa/        JPA entities
  entities/mongo/      Mongo document models
  repositories/jpa/    JPA repositories
  repositories/mongo/  Mongo repositories
  service/             Business services

src/main/resources
  templates/           Thymeleaf pages
  static/css/          Custom CSS
  application.properties
```

## Requirements

- Java 17 or later
- Maven wrapper included in the project
- MySQL running locally
- A database named `charity_db_jee`

Create the database:

```sql
CREATE DATABASE charity_db_jee;
```

Default database configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/charity_db_jee?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

Update these values if your MySQL username or password is different.

## Environment Variables

The project can run with default placeholder values, but real integrations should use environment variables:

```text
MAIL_USERNAME
MAIL_PASSWORD
GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET
STRIPE_SECRET_KEY
```

Example on Windows PowerShell:

```powershell
$env:MAIL_USERNAME="your-email@gmail.com"
$env:MAIL_PASSWORD="your-app-password"
$env:STRIPE_SECRET_KEY="sk_test_xxx"
```

## Run the Application

From the project root:

```powershell
.\mvnw.cmd spring-boot:run
```

Then open:

```text
http://localhost:8080
```

If port `8080` is already used:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

Then open:

```text
http://localhost:8081
```

## Build and Test

Compile the project:

```powershell
.\mvnw.cmd -DskipTests compile
```

Run tests:

```powershell
.\mvnw.cmd test
```

Package the application:

```powershell
.\mvnw.cmd clean package
```

## Main Web Pages

| Page | URL | Access |
| --- | --- | --- |
| Home | `/` | Public |
| Explore actions | `/explore` | Public |
| Action details | `/actions/{id}` | Public |
| Login | `/auth/login` | Public |
| User registration | `/auth/register` | Public |
| Organization registration | `/auth/register-org` | Public |
| User profile | `/user/profile` | Authenticated user |
| User donations | `/user/donations` | Authenticated user |
| User participations | `/user/participations` | Authenticated user |
| Organization profile | `/org/profile` | Approved organization |
| Organization actions dashboard | `/org/actions` | Approved organization |
| Create organization action | `/org/actions/create` | Approved organization |
| Edit organization action | `/org/actions/{id}/edit` | Approved organization owning the action |
| Admin dashboard | `/admin/dashboard` | Super admin |
| Organization approvals | `/admin/organizations` | Super admin |

## Main REST APIs

| Area | Base URL |
| --- | --- |
| Authentication | `/api/auth` |
| Users | `/api/users` |
| Organizations | `/api/organizations` |
| Charity actions | `/api/actions` |
| Donations | `/api/donations` |
| Participations | `/api/participations` |
| Media | `/api/media` |
| Admin | `/api/admin` |

## Organization Approval Flow

1. An organization registers from `/auth/register-org`.
2. The organization is saved with `PENDING` status.
3. It cannot log in while pending.
4. The super admin logs in using `admin@charity.com / Admin1234!`.
5. The super admin opens `/admin/organizations`.
6. The super admin approves or rejects the organization.
7. If approved, the organization can log in and manage its charity actions.

## Charity Action Management Flow

After approval, an organization can:

1. Open `/org/actions`.
2. View statistics:
   - active actions;
   - archived actions;
   - total collected amount.
3. Create an action with:
   - title;
   - description;
   - category;
   - event date;
   - location;
   - fundraising goal.
4. Edit the action later.
5. Upload images or videos.
6. Archive the action when it is no longer active.

The application checks that an organization can only edit, archive, or manage media for its own actions.

## Notes About MongoDB

The project contains Mongo document classes and repositories for:

- `Notification`
- `UserActivity`

However, Mongo auto-configuration is currently disabled in `CharityApplication`:

```java
@SpringBootApplication(exclude = {
    MongoAutoConfiguration.class,
    DataMongoAutoConfiguration.class,
    DataMongoRepositoriesAutoConfiguration.class
})
```

To enable MongoDB, remove these exclusions and configure:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/charity_mongo
```

## File Uploads

Uploaded media files are stored in:

```text
uploads/
```

The upload directory is configured with:

```properties
app.upload.dir=uploads
```

## Security Notes

- Pending organizations cannot authenticate.
- Only approved organizations receive `ROLE_ADMIN_ORG`.
- `/org/**` routes require `ROLE_ADMIN_ORG`.
- `/admin/**` routes require `ROLE_SUPER_ADMIN`.
- Public users can only view active public action pages.
- Organization action management verifies ownership before edit, archive, upload, or media deletion.

## Troubleshooting

### Port 8080 is already in use

Run the application on another port:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### MySQL connection fails

Check that:

- MySQL is running;
- database `charity_db_jee` exists;
- username/password in `application.properties` are correct.

### Maven cannot download dependencies

If Maven fails with a certificate error, check your Java trust store, proxy, antivirus HTTPS inspection, or corporate network settings.

## License

No license file is currently included. Add a `LICENSE` file before publishing or distributing the project.
