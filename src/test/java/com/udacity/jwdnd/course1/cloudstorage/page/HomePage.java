package com.udacity.jwdnd.course1.cloudstorage.page;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.NoSuchElementException;

public class HomePage {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "add-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "submit-note-button")
    private WebElement submitNoteButton;

    @FindBy(id = "display-note-title")
    private WebElement firstNoteTitle;

    @FindBy(id = "display-note-description")
    private WebElement firstNoteDescription;

    @FindBy(id = "edit-note-button")
    private WebElement editNoteButton;

    @FindBy(id = "delete-note-button")
    private WebElement deleteNoteButton;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "add-credential-button")
    private WebElement addCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(id = "submit-credential-button")
    private WebElement submitCredentialButton;

    @FindBy(id = "display-credential-url")
    private WebElement firstCredentialUrl;

    @FindBy(id = "display-credential-username")
    private WebElement firstCredentialUsername;

    @FindBy(id = "display-credential-password")
    private WebElement firstCredentialPassword;

    @FindBy(id = "edit-credential-button")
    private WebElement editCredentialButton;

    @FindBy(id = "delete-credential-button")
    private WebElement deleteCredentialButton;

    private WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logoutButton.click();
    }

    /////////////////////////Note//////////////////////
    public void getNodesTab() {
        driver.manage().window().maximize();
        notesTab.click();
    }

    public void createNote(String title, String description) throws InterruptedException {
        getNodesTab();
        Thread.sleep(3000);
        addNoteButton.click();
        Thread.sleep(3000);
        noteTitle.sendKeys(title);
        noteDescription.sendKeys(description);
        submitNoteButton.click();
    }

    public Note getFirstNote() throws InterruptedException {
         getNodesTab();
         Thread.sleep(3000);
        String title = firstNoteTitle.getText();
        String description = firstNoteDescription.getText();
        return new Note(title, description);
    }

    public void editNote(String title, String description) throws InterruptedException {
        // getNodesTab();
        Thread.sleep(3000);
        editNoteButton.click();
        Thread.sleep(3000);
        noteTitle.clear();
        noteTitle.sendKeys(title);
        noteDescription.clear();
        noteDescription.sendKeys(description);
        submitNoteButton.click();
    }

    public void deleteNote() throws InterruptedException {
        getNodesTab();
        Thread.sleep(3000);
        deleteNoteButton.click();
    }

    public boolean noNote(WebDriver driver) {
        return !canFindElement(By.id("display-note-title"), driver) &&
                !canFindElement(By.id("display-note-description"), driver);
    }

    /////////////////////////Credential//////////////////////
    public void getCredentialsTab() {
        driver.manage().window().maximize();
        credentialsTab.click();
    }

    public void createCredential(Credential credential) throws InterruptedException {
        getCredentialsTab();
        Thread.sleep(3000);
        addCredentialButton.click();
        Thread.sleep(3000);
        credentialUrl.sendKeys(credential.getUrl());
        credentialUsername.sendKeys(credential.getUsername());
        credentialPassword.sendKeys(credential.getPassword());
        submitCredentialButton.click();
    }

    public void createCredentials(List<Credential> credentialList) throws InterruptedException {
        for(Credential credential : credentialList) {
            createCredential(credential);
        }
    }

    public Credential getFirstCredential() throws InterruptedException {
        getCredentialsTab();
        Thread.sleep(3000);
        String url = firstCredentialUrl.getText();
        String username = firstCredentialUsername.getText();
        String password = firstCredentialPassword.getText();
        return new Credential(url, username, password);
    }

    public void editCredential(Credential credential) throws InterruptedException {
        getCredentialsTab();
        Thread.sleep(3000);
        editCredentialButton.click();
        Thread.sleep(3000);
        credentialUrl.clear();
        credentialUrl.sendKeys(credential.getUrl());
        credentialUsername.clear();
        credentialUsername.sendKeys(credential.getUsername());
        credentialPassword.clear();
        credentialPassword.sendKeys(credential.getPassword());
        submitCredentialButton.click();
    }

    public void deleteCredential() throws InterruptedException {
        getCredentialsTab();
        Thread.sleep(3000);
        deleteCredentialButton.click();
    }

    public boolean noCredential(WebDriver driver) {
        return !canFindElement(By.id("display-credential-url"), driver) &&
                !canFindElement(By.id("display-credential-username"), driver) &&
                !canFindElement(By.id("display-credential-password"), driver);
    }

    private boolean canFindElement(By key, WebDriver driver) {
        try {
            driver.findElement(key);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }
}
