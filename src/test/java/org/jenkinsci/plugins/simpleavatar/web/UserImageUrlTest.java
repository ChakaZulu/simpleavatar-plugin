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

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import hudson.model.User;
import java.io.IOException;
import java.util.ListIterator;
import org.jenkinsci.plugins.simpleavatar.PluginTestConfigs;
import org.jenkinsci.plugins.simpleavatar.SimpleAvatarResolver;
import static org.jenkinsci.plugins.simpleavatar.TestUtil.AddUser;
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
public class UserImageUrlTest {
    
    @Rule
    public final JenkinsRule j = new JenkinsRule();
    private JenkinsRule.WebClient wc;
    
    public UserImageUrlTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        wc = j.createWebClient();
    }
    
    @After
    public void tearDown() {
    }

    // people page
    @Test
    public void OtherUserImageOnPeoplePageForNullTemplate() throws IOException, SAXException, InterruptedException
    {
        final SimpleAvatarResolver.DescriptorImpl descriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptor.setAvatarUrlTemplate(PluginTestConfigs.AVATAR_URL_TEMPLATE_NULL);
        User user = AddUser("John Doe", "JohnDoe");
        Assert.assertFalse("image url is incorrect", PeopleUserImageUrl(user).startsWith(PluginTestConfigs.AVATAR_URL_TEMPLATE_DOMAIN));
    }

    @Test
    public void OtherUserImageOnPeoplePageForEmptyTemplate() throws IOException, SAXException, InterruptedException
    {
        final SimpleAvatarResolver.DescriptorImpl descriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptor.setAvatarUrlTemplate(PluginTestConfigs.AVATAR_URL_TEMPLATE_EMPTY);
        User user = AddUser("John Doe", "JohnDoe");
        Assert.assertFalse("image url is incorrect", PeopleUserImageUrl(user).startsWith(PluginTestConfigs.AVATAR_URL_TEMPLATE_DOMAIN));
    }

    @Test
    public void UserImageOnPeoplePageForStaticTemplate() throws IOException, SAXException, InterruptedException
    {
        final SimpleAvatarResolver.DescriptorImpl descriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptor.setAvatarUrlTemplate(PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC);
        User user = AddUser("John Doe", "JohnDoe");
        Assert.assertEquals("image url is incorrect", PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC, PeopleUserImageUrl(user));
    }
    
    @Test
    public void UserImageOnPeoplePageForStaticTemplateWithEncodedTemplateKeys() throws IOException, SAXException, InterruptedException
    {
        final SimpleAvatarResolver.DescriptorImpl descriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptor.setAvatarUrlTemplate(PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC_WITH_ENCODED_TEMPLATE_KEYS);
        User user = AddUser("John Doe", "JohnDoe");
        Assert.assertEquals("image url is incorrect", PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC_WITH_ENCODED_TEMPLATE_KEYS, PeopleUserImageUrl(user));
    }
    
    @Test
    public void UserImageOnPeoplePageForDynamicTemplate() throws IOException, SAXException, InterruptedException
    {
        final SimpleAvatarResolver.DescriptorImpl descriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptor.setAvatarUrlTemplate(PluginTestConfigs.AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT);
        User user = AddUser("John Doe", "JohnDoe");
        Assert.assertEquals("image url is incorrect", PluginTestConfigs.AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT_EXPECTED_PEOPLE, PeopleUserImageUrl(user));
    }
    
    // user page
    @Test
    public void OtherUserImageOnUserPageForNullTemplate() throws IOException, SAXException, InterruptedException
    {
        final SimpleAvatarResolver.DescriptorImpl descriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptor.setAvatarUrlTemplate(PluginTestConfigs.AVATAR_URL_TEMPLATE_NULL);
        User user = AddUser("John Doe", "JohnDoe");
        Assert.assertFalse("image url is incorrect", UserImageUrl(user).startsWith(PluginTestConfigs.AVATAR_URL_TEMPLATE_DOMAIN));
    }

    @Test
    public void OtherUserImageOnUserPageForEmptyTemplate() throws IOException, SAXException, InterruptedException
    {
        final SimpleAvatarResolver.DescriptorImpl descriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptor.setAvatarUrlTemplate(PluginTestConfigs.AVATAR_URL_TEMPLATE_EMPTY);
        User user = AddUser("John Doe", "JohnDoe");
        Assert.assertFalse("image url is incorrect", UserImageUrl(user).startsWith(PluginTestConfigs.AVATAR_URL_TEMPLATE_DOMAIN));
    }

    @Test
    public void UserImageOnUserPageForStaticTemplate() throws IOException, SAXException
    {
        final SimpleAvatarResolver.DescriptorImpl descriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptor.setAvatarUrlTemplate(PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC);
        User user = AddUser("John Doe", "JohnDoe");
        Assert.assertEquals("image url is incorrect", PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC, UserImageUrl(user));
    }
    
    @Test
    public void UserImageOnUserPageForStaticTemplateWithEncodedTemplateKeys() throws IOException, SAXException
    {
        final SimpleAvatarResolver.DescriptorImpl descriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptor.setAvatarUrlTemplate(PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC_WITH_ENCODED_TEMPLATE_KEYS);
        User user = AddUser("John Doe", "JohnDoe");
        Assert.assertEquals("image url is incorrect", PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC_WITH_ENCODED_TEMPLATE_KEYS, UserImageUrl(user));
    }
    
    @Test
    public void UserImageOnUserPageForDynamicTemplate() throws IOException, SAXException
    {
        final SimpleAvatarResolver.DescriptorImpl descriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        descriptor.setAvatarUrlTemplate(PluginTestConfigs.AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT);
        User user = AddUser("John Doe", "JohnDoe");
        Assert.assertEquals("image url is incorrect", PluginTestConfigs.AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT_EXPECTED_USER, UserImageUrl(user));
    }
    
    // utils
    private String UserImageUrl(User user) throws IOException, SAXException
    {
        HtmlElement element = wc.goTo("user/" + user.getId()).getElementById("main-panel").getElementsByTagName("img").get(0);
        return element.getAttribute("src");
    }
    
    private String PeopleUserImageUrl(User user) throws IOException, SAXException, InterruptedException
    {
        HtmlPage htmlPage = goAndWaitForLoadOfPeople();
        
        DomAttr attr = (DomAttr)htmlPage.getByXPath("//tr[contains(@id,'"+user.getId()+"')]/descendant::img/@src").get(0);
        return attr.getValue();
    }

    // taken from gravatar plugin UserGravatarResolverIntegrationTest.java
    private HtmlPage goAndWaitForLoadOfPeople() throws InterruptedException, IOException, SAXException {
        HtmlPage htmlPage = wc.goTo("asynchPeople");
        while(getStatus(htmlPage).isDisplayed()) {
            //the asynch part has not yet finished, so we wait.
            Thread.sleep(500);
        }
        return htmlPage;
    }

    // taken from gravatar plugin UserGravatarResolverIntegrationTest.java
    private HtmlElement getStatus(HtmlPage htmlPage) {
        final HtmlElement statusById = htmlPage.getElementById("status");
        if(statusById != null) {
            return statusById;
        }
        final ListIterator<HtmlElement> tablesOnPage = htmlPage.getElementsByTagName("table").listIterator();
        while (tablesOnPage.hasNext()) {
            HtmlElement next = tablesOnPage.next();
            if("progress-bar".equalsIgnoreCase(next.getAttribute("class"))) {
                return next;
            }
        }
        return null;
    }
}
