# Super*Duper*Drive Cloud Storage
Super*Duper*Drive is a cloud storage application includes three user-facing features:

1. **Simple File Storage:** Upload/download/remove files
2. **Note Management:** Add/update/remove text notes
3. **Password Management:** Save, edit, and delete website credentials.  

## Requirements and Roadmap
There are three layers implemented of the application:

1. The back-end with Spring Boot
2. The front-end with Thymeleaf
3. Application tests with Selenium

### The Back-End
The back-end is all about security and connecting the front-end to database data and actions. 

1. Managing user access with Spring Security
 - Restrict unauthorized users from accessing pages other than the login and signup pages. 
 - Use the security configuration to override the default login page.
 - Implement a custom `AuthenticationProvider` which authorizes user logins by matching their credentials against those stored in the database.  


2. Handling front-end calls with controllers
 - Write controllers for the application that bind application data and functionality to the front-end. That means using Spring MVC's application model to identify the templates served for different requests and populating the view model with data needed by the template. 
 - The controllers also are responsible for determining what, if any, error messages the application displays to the user. When a controller processes front-end requests, it should delegate the individual steps and logic of those requests to other services in the application, but it should interpret the results to ensure a smooth user experience.
 

3. Making calls to the database with MyBatis mappers
 - Design Java classes to match the data in the database. These are POJOs (Plain Old Java Objects) with fields that match the names and data types in the schema.
 - To connect these model classes with database data, implement MyBatis mapper interfaces for each of the model types. These mappers have methods that represent specific SQL queries and statements required by the functionality of the application. They support the basic CRUD (Create, Read, Update, Delete) operations for their respective models at the very least.


### The Front-End

1. Login page
 - Everyone should be allowed access to this page, and users can use this page to login to the application. 
 - Show login errors, like invalid username/password, on this page. 


2. Sign Up page
 - Everyone should be allowed access to this page, and potential users can use this page to sign up for a new account. 
 - Validate that the username supplied does not already exist in the application, and show such signup errors on the page when they arise.
 - Store the user's password securely.


3. Home page
 - The home page is the center of the application and hosts the three required pieces of functionality. The existing template presents them as three tabs that can be clicked through by the user:


 i. Files
  - The user is able to upload files and see any files they previously uploaded.
  - The user is able to view/download or delete previously-uploaded files.
  - Any errors related to file actions should be displayed. For example, a user is not be able to upload two files with the same name.


 ii. Notes
  - The user is able to create notes and see a list of the notes they have previously created.
  - The user is able to edit or delete previously-created notes.

 iii. Credentials
 - The user is able to store credentials for specific websites and see a list of the credentials they've previously stored. The passwords displayed in this page are encrypted.
 - The user is able to view/edit or delete individual credentials. When the user views the credential, they are able to see the unencrypted password.

The home page has a logout button that allows the user to logout of the application and keep their data private.

### Testing

1. Tests for user signup, login, and unauthorized access restrictions.
 - A test that verifies that an unauthorized user can only access the login and signup pages.
 - A test that signs up a new user, logs in, verifies that the home page is accessible, logs out, and verifies that the home page is no longer accessible. 


2. Tests for note creation, viewing, editing, and deletion.
 - A test that creates a note, and verifies it is displayed.
 - A test that edits an existing note and verifies that the changes are displayed.
 - A test that deletes a note and verifies that the note is no longer displayed.


3. Tests for credential creation, viewing, editing, and deletion.
 - A test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
 - A test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
 - A test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
