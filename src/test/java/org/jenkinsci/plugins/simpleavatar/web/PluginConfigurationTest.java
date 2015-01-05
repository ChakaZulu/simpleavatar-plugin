/*
 * The MIT License
 *
 * Copyright 2015 Michael Schuele.
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
package org.jenkinsci.plugins.simpleavatar.web;

import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import hudson.ExtensionList;
import hudson.PluginWrapper;
import hudson.tasks.UserAvatarResolver;
import java.io.IOException;
import java.util.List;
import static org.jenkinsci.plugins.simpleavatar.PluginTestConfigs.AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT;
import org.jenkinsci.plugins.simpleavatar.SimpleAvatarResolver;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

/**
 *
 * @author Michael Schuele
 */
public class PluginConfigurationTest {
    
    @Rule
    public final JenkinsRule j = new JenkinsRule();
    
    public PluginConfigurationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void PluginHelpExists() throws IOException, SAXException
    {
        final HtmlPage page = j.createWebClient().goTo("configure");
        WebAssert.assertElementPresentByXPath(page, "//td[@class=\"setting-help\" and contains(a/@helpurl, 'SimpleAvatarResolver')]");
    }
    
    @Test
    public void PluginHelpContentExists() throws IOException, SAXException
    {
        final JenkinsRule.WebClient webClient = j.createWebClient();
        final HtmlPage page = webClient.goTo("configure");
        final List<Object> byXPath = page.getByXPath("//td[@class=\"setting-help\" and contains(a/@helpurl, 'SimpleAvatarResolver')]/a/@helpurl");
        final DomAttr attribute = (DomAttr) byXPath.get(0);
        final String helpPageUrl = attribute.getValue().replace("/jenkins/", "");
        final HtmlPage helpPage = webClient.goTo(helpPageUrl);
        WebAssert.assertTextPresent(helpPage, "The Url template for the jenkins user image");
    }

    @Test
    public void PluginIsAvailable()
    {
        PluginWrapper pluginWrapper = j.getPluginManager().getPlugin("simpleavatar");
        Assert.assertNotNull(pluginWrapper);
    }
    
    @Test
    public void ExtensionIsRegistered()
    {
        ExtensionList<UserAvatarResolver> all = UserAvatarResolver.all();
        Assert.assertTrue("plugin is not registered", all.size() > 0);
    }
    
    @Test 
    public void PluginConfigurationExists() throws IOException, SAXException {
        final HtmlPage page = j.createWebClient().goTo("configure");
        WebAssert.assertElementPresentByXPath(page, "//tr[@name=\"org-jenkinsci-plugins-simpleavatar-SimpleAvatarResolver\"]");
    }
    
    @Test
    public void DefaultPluginConfiguration() throws Exception
    {
        final SimpleAvatarResolver.DescriptorImpl descriptorBefore = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        Assert.assertEquals("avatarUrlTemplate must be empty", null, descriptorBefore.getAvatarUrlTemplate());
    }
    
    @Test
    public void DefaultPluginConfigurationIsSavedInRoundtrip() throws Exception
    {
        final SimpleAvatarResolver.DescriptorImpl descriptorBefore = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        
        j.configRoundtrip();
        
        final SimpleAvatarResolver.DescriptorImpl descriptorAfter = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        j.assertEqualBeans(descriptorBefore, descriptorAfter, "avatarUrlTemplate");
    }
    
    @Test
    public void CustomPluginConfigurationIsSavedInRoundtrip() throws Exception
    {
        final SimpleAvatarResolver.DescriptorImpl descriptorBefore = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptorBefore.setAvatarUrlTemplate(AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT);
        
        j.configRoundtrip();
        
        final SimpleAvatarResolver.DescriptorImpl descriptorAfter = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        Assert.assertEquals("configuration has not been saved", AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT, descriptorAfter.getAvatarUrlTemplate());
    }
    
    @Test
    public void CustomPluginConfigurationIsSaved() throws Exception
    {
        final JenkinsRule.WebClient webClient = j.createWebClient();
        final HtmlPage page = webClient.goTo("configure");     
        final List<Object> byXPath = page.getByXPath("//input[@name=\"_.avatarUrlTemplate\" and ancestor::tr/preceding-sibling::tr/descendant::div[contains(text(),'Simple Avatar')]]");         
        final HtmlInput input = (HtmlInput)byXPath.get(0);
        final HtmlPage newPage = (HtmlPage)input.setValueAttribute(AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT);
        j.submit(newPage.getFormByName("config"));
        
        final SimpleAvatarResolver.DescriptorImpl descriptorAfter = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        Assert.assertEquals("configuration has not been saved", AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT, descriptorAfter.getAvatarUrlTemplate());
    }
}
