package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.ResultPage;
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

// Tests for Credential Creation, Viewing, Editing, and Deletion
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests {
    @LocalServerPort
    private int port;
    private static WebDriver driver;
    private String baseURL;
    private HomePage homePage;
    private ResultPage resultPage;
    private static final String TEST_URL = "http://www.test.com/";
    private static final String TEST_USERNAME = "tester1";
    private static final String TEST_PASSWORD = "password1";
    private static final String EDIT_URL = "https://www.edit.com/";
    private static final String EDIT_USERNAME = "editor";
    private static final String EDIT_PASSWORD = "password2";

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
        homePage = new HomePage(driver);
        resultPage = new ResultPage(driver);
    }

    @Test
    public void testCredentialCreation() throws InterruptedException {
        createCredential(TEST_URL, TEST_USERNAME, TEST_PASSWORD, homePage, resultPage);
        Credential credential = homePage.getFirstCredential();
        // Assertion
        assertEquals(TEST_URL, credential.getUrl());
        assertEquals(TEST_USERNAME, credential.getUsername());
        assertNotEquals(TEST_PASSWORD, credential.getPassword());
        deleteCredential(homePage, resultPage);
    }

    @Test
    public void testCredentialEditing() throws InterruptedException {
        createCredential(TEST_URL, TEST_USERNAME, TEST_PASSWORD, homePage, resultPage);
        Credential credential = new Credential(EDIT_URL, EDIT_USERNAME, EDIT_PASSWORD);
        homePage.editCredential(credential);
        resultPage.returnHome();
        Credential firstCredential = homePage.getFirstCredential();
        String originalPassword = homePage.getFirstCredentialPassword();
        // Assertion
        assertEquals(EDIT_URL, firstCredential.getUrl());
        assertEquals(EDIT_USERNAME, firstCredential.getUsername());
        assertNotEquals(EDIT_PASSWORD, firstCredential.getPassword());
        assertEquals(EDIT_PASSWORD, originalPassword);
        deleteCredential(homePage, resultPage);
    }

    @Test
    public void testCredentialDeletion() throws InterruptedException {
        createCredential(TEST_URL, TEST_USERNAME, TEST_PASSWORD, homePage, resultPage);
        // Assertion
        assertFalse(homePage.noCredential(driver));

        deleteCredential(homePage, resultPage);
        // Assertion
        assertTrue(homePage.noCredential(driver));
    }

    private void createCredential(
            String url, String username, String password,
            HomePage homePage, ResultPage resultPage) throws InterruptedException {
        signupLogin();
        driver.get(baseURL + "/home");
        Credential credential = new Credential(url, username, password);
        homePage.createCredential(credential);
        resultPage.returnHome();
    }

    private void deleteCredential(HomePage homePage, ResultPage resultPage) throws InterruptedException {
        homePage.deleteCredential();
        resultPage.returnHome();
    }

    private void signupLogin() {
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
    }
}
