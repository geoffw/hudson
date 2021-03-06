/*
 * The MIT License
 *
 * Copyright (c) 2011, Oracle Corporation, Nikita Levyankov.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.hudsonci.test.ui;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test cases for configure system page.
 * <p/>
 * Date: 5/6/11
 *
 * @author Nikita Levyankov
 */
public class ConfigureHudsonTest extends BaseUITest {

    @Test
    public void testAddJDK() throws Exception {
        String addJDKButtonXpath = "//button[contains(text(), 'Add JDK')]";
        String jdkName = "jdk_6_24";
//        String jdkVersion = "6 Update 22";
        Selenium selenium = getSelenium();
        selenium.open("/");
        //Open Manage Hudson page
        waitForTextPresent("Manage Hudson", null);
        selenium.click("link=Manage Hudson");
        waitForTextPresent("Configure System", null);
        //Open Configure System Page
        selenium.click("link=Configure System");
        waitForElementPresence(addJDKButtonXpath);
        //Validate Add JDK button presence
        assertTrue(selenium.isElementPresent(addJDKButtonXpath));
        selenium.click(addJDKButtonXpath);
        assertTrue(selenium.isElementPresent("//input[@name='_.name']"));
        //name is pre-validated. Non-Empty value is required. Check that error is displayed
        assertTrue(selenium.isTextPresent("Required"));
        //Validate for accept licence checkbox presence
        selenium.isElementPresent("//input[@name='_.acceptLicense']");
        assertTrue(selenium.isTextPresent("You must agree to the license to download the JDK."));
        //Enter required jdk name
        selenium.type("_.name", jdkName);
//        selenium.select("_.id", jdkVersion);
        //Need to accept oracle licence
        selenium.click("_.acceptLicense");
        //Click save button.
        selenium.click("//button[contains(text(), 'Save')]");
        waitForTextPresent("Manage Hudson", null);
        selenium.click("link=Manage Hudson");
        waitForTextPresent("Configure System", null);
        selenium.click("link=Configure System");

        //Re-validate changes
        assertEquals(selenium.getValue("_.name"), jdkName);
//        assertEquals(selenium.getSelectedLabel("_.id"), jdkVersion);
        //Click delete installer and save button.
        selenium.click("//button[contains(text(), 'Delete JDK')]");
        selenium.click("//button[contains(text(), 'Save')]");
    }

    @Test
    public void testChangeSystemMessage() throws Exception {
        Selenium selenium = getSelenium();
        selenium.open("/");
        //Navigate to Configure System page
        waitForTextPresent("Manage Hudson", null);
        selenium.click("link=Manage Hudson");
        waitForTextPresent("Configure System", null);
        selenium.click("link=Configure System");
        waitForTextPresent("System Message", null);
        //Enter a simple message and save
        selenium.type("system_message", "A simple test message\n\n<p>With some html tags</p>");
        selenium.click("//button[contains(text(), 'Save')]");
        selenium.waitForPageToLoad("30000");
        //Verify the message appears
        waitForTextPresent("A simple test message With some html tags", null);
    }

    @Test
    public void testChangeExecutors() throws Exception {
        Selenium selenium = getSelenium();
        selenium.open("/");
        //Check that we have two executors to start with
        waitForElementPresence("//table[@id='executors']/tbody[2]/tr[3]/td[1]");
        assertEquals(selenium.getText("//table[@id='executors']/tbody[2]/tr[3]/td[1]"), "2");
        //Navigate to Configure System page
        selenium.click("link=Manage Hudson");
        waitForTextPresent("Configure System", null);
        selenium.click("link=Configure System");
        waitForTextPresent("# of executors", null);
        //Check and update the number of executors
        assertEquals(selenium.getValue("_.numExecutors"), "2");
        selenium.type("_.numExecutors", "1");
        //Save
        selenium.click("//button[contains(text(), 'Save')]");
        waitForTextPresent("Manage Hudson", null);
        //Verify the number of excutors now
        waitForElementPresence("//table[@id='executors']/tbody[2]/tr[2]/td[1]");
        assertEquals(selenium.getText("//table[@id='executors']/tbody[2]/tr[2]/td[1]"), "1");
    }
}
