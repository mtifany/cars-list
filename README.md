Cars and models application which stores different car brands and models with photos. It supports user authentication and allows user to:

1.Browse through paginated list of cars models with its photos

2.Display unique models names

3.Get all models by brand name

4.Search by model name

5.Edit the model (name and photo) by the user with Admin role.

**Prerequisites:**

**Java:** Ensure you have Java 17 installed.

**Maven:** Verify that Apache Maven is installed and configured.

**PostgreSQL:**
Install PostgreSQL locally or ensure access to a running instance.
Create a database for the project.
Update the database connection details in application.yml:
Copy code
   ```
spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/cars_db
        username: postgres
        password: postgres
   ```
**S3 Configuration:**
Provide valid AWS S3 credentials in your environment variables or application.yml file:
   ```
aws:
  s3:
    photos-folder: <your-folder-name>
    region: <your-region>
    bucket: <your-bucket>
    access-key-id: <your-access-key>
    secret-access-key:<your-secret-key>
   ```
**Start the application:** `mvn spring-boot:run`


