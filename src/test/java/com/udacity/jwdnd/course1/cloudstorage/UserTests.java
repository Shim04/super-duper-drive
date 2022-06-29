package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

// Tests for User Signup, Login, and Unauthorized Access Restrictions
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTests {
    @LocalServerPort
    public int port;
    public static WebDriver driver;
    public String baseURL;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void afterAll() {
        driver.quit();
        driver = null;
    }

    @BeforeEach
    public void beforeEach() {
        baseURL = "http://localhost:" + port;
    }

    // Test an unauthorized user can only access the login and signup pages
    @Test
    public void testPagesAccessibility() {
        // Signup Page
        driver.get(baseURL + "/signup");
        assertEquals("Sign Up", driver.getTitle());

        // Login Page
        driver.get(baseURL + "/login");
        assertEquals("Login", driver.getTitle());

        // Home Page
        driver.get(baseURL + "/home");
        assertEquals("Login", driver.getTitle());
    }

    // Test after signing up and logging in, the home page is accessible
    // Test after logging out, the home page is no longer accessible.
    @Test
    public void testUserSignupLoginLogout() {
        String username = "Tester";
        String password = "12345";
        // Signup
        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("Tester", "Tester", username, password);

        // Login
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        // Home Page
        driver.get(baseURL + "/home");
        assertEquals("Home", driver.getTitle());
        HomePage homePage = new HomePage(driver);
        homePage.logout();
        assertEquals("Login", driver.getTitle());
    }
}
