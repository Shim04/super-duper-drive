package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Note Creation, Viewing, Editing, and Deletion
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteTests {
    @LocalServerPort
    private int port;
    private static WebDriver driver;
    private String baseURL;
    private HomePage homePage;
    private ResultPage resultPage;

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
    public void testNoteCreationAndViewing() throws InterruptedException {
        String title = "Note Title";
        String description = "Note Description";
        createNote(title, description, homePage, resultPage);
        Note note = homePage.getFirstNote();
        // Assertion
        assertEquals(title, note.getNoteTitle());
        assertEquals(description, note.getNoteDescription());
        deleteNote(homePage, resultPage);
    }

    @Test
    public void testNoteEditing() throws InterruptedException {
        createNote("Note Title", "Note Description", homePage, resultPage);
        homePage.editNote("Edit Title", "Edit Description");
        resultPage.returnHome();
        Note note = homePage.getFirstNote();
        // Assertion
        assertEquals("Edit Title", note.getNoteTitle());
        assertEquals("Edit Description", note.getNoteDescription());
        deleteNote(homePage, resultPage);
    }

    @Test
    public void testNoteDeletion() throws InterruptedException {
        String title = "Note Title";
        String description = "Note Description";
        createNote(title, description, homePage, resultPage);
        // Assertion
        assertFalse(homePage.noNote(driver));

        deleteNote(homePage, resultPage);
        WebDriverWait wait = new WebDriverWait(driver, 3);
        WebElement marker = null;
        try {
            marker = wait.until(webDriver -> webDriver.findElement(By.id("return-home-link")));
        } catch (TimeoutException e) {
        }
        // Assertion
        assertTrue(homePage.noNote(driver));
    }

    private void createNote(String title, String description, HomePage homePage, ResultPage resultPage) throws InterruptedException {
        signupLogin();
        driver.get(baseURL + "/home");
        WebDriverWait wait = new WebDriverWait(driver, 3);
        WebElement homeMarker = wait.until(webDriver -> webDriver.findElement(By.id("nav-files-tab")));
        assertNotNull(homeMarker);
        homePage.createNote(title, description);
        resultPage.returnHome();
    }

    private void deleteNote(HomePage homePage, ResultPage resultPage) throws InterruptedException {
        homePage.deleteNote();
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
