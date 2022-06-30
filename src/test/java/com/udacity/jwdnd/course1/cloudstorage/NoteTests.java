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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
    private static final String TEST_TITLE = "Note Title";
    private static final String TEST_DESCRIPTION = "Note Description";
    private static final String EDIT_TITLE = "Edit Title";
    private static final String EDIT_DESCRIPTION = "Edit Description";

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
        createNote(TEST_TITLE, TEST_DESCRIPTION, homePage, resultPage);
        Note note = homePage.getFirstNote();
        // Assertion
        assertEquals(TEST_TITLE, note.getNoteTitle());
        assertEquals(TEST_DESCRIPTION, note.getNoteDescription());
        deleteNote(homePage, resultPage);
    }

    @Test
    public void testNoteEditing() throws InterruptedException {
        createNote(TEST_TITLE, TEST_DESCRIPTION, homePage, resultPage);
        homePage.editNote(EDIT_TITLE, EDIT_DESCRIPTION);
        resultPage.returnHome();
        Note note = homePage.getFirstNote();
        // Assertion
        assertEquals(EDIT_TITLE, note.getNoteTitle());
        assertEquals(EDIT_DESCRIPTION, note.getNoteDescription());
        deleteNote(homePage, resultPage);
    }

    @Test
    public void testNoteDeletion() throws InterruptedException {
        createNote(TEST_TITLE, TEST_DESCRIPTION, homePage, resultPage);
        // Assertion
        assertFalse(homePage.noNote(driver));

        deleteNote(homePage, resultPage);
        // Assertion
        assertTrue(homePage.noNote(driver));
    }

    private void createNote(String title, String description, HomePage homePage, ResultPage resultPage) throws InterruptedException {
        signupLogin();
        driver.get(baseURL + "/home");
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
