/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// LoginTest.java
import com.mycompany.Mavenproject1.Login;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    Login login = new Login();

    // ---------------- USERNAME TESTS ----------------
    @Test
    public void testValidUsername() {
        assertTrue(login.checkUserName("Chulu_"));
    }

    @Test
    public void testInvalidUsername_NoUnderscore() {
        assertFalse(login.checkUserName("Chulumanco"));
    }

    @Test
    public void testInvalidUsername_TooLong() {
        assertFalse(login.checkUserName("Chulumancoooooooooo"));
    }

    // ---------------- PASSWORD TESTS ----------------
    @Test
    public void testValidPassword() {
        assertTrue(login.checkPasswordComplexity("CMANCO7_!"));
    }

    @Test
    public void testInvalidPassword_NoNumber() {
        assertFalse(login.checkPasswordComplexity("CMANCO_!"));
    }

    @Test
    public void testInvalidPassword_NoCapitalLetter() {
        assertFalse(login.checkPasswordComplexity("cmanco7_!"));
    }

    // ---------------- PHONE NUMBER TESTS ----------------
    @Test
    public void testValidPhoneNumber() {
        assertTrue(login.checkCellPhoneNumber("+27607790384"));
    }

    @Test
    public void testInvalidPhoneNumber_MissingPlus27() {
        assertFalse(login.checkCellPhoneNumber("0607790384"));
    }

    @Test
    public void testInvalidPhoneNumber_WrongLength() {
        assertFalse(login.checkCellPhoneNumber("+2760779084")); // 11 digits instead of 12
    }
}