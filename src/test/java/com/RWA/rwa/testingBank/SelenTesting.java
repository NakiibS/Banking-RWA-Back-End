package com.RWA.rwa.testingBank;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SelenTesting {

    private WebDriver driver;
    private WebDriverWait wait;
    @Before
    public void setUp(){
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("http://localhost:5173/");

    }


    @Test
    public void testSuccessfullogin(){
        //Introducimos las credenciales admin admin

        WebElement usernameField = driver.findElement(By.name("username"));
//        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("username")));
        usernameField.sendKeys("admin");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        WebElement loginButton = driver.findElement(By.id("btn-login"));
        loginButton.click();

        // Verificación
        try {
            WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("mail")));
            Assert.assertTrue(successMessage.isDisplayed());
        } catch (TimeoutException e) {
            Assert.fail("Credenciales incorrectas o Login fallido");
        }
    }
    @After
    public void afterTest(){
        driver.quit();
    }

}
