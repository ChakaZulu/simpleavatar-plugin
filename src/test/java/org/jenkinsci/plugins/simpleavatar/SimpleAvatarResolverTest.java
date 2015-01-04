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
package org.jenkinsci.plugins.simpleavatar;

import hudson.ExtensionList;
import hudson.model.User;
import static org.jenkinsci.plugins.simpleavatar.TestUtil.AddUser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;

/**
 *
 * @author Michael Schuele
 */
public class SimpleAvatarResolverTest {
    
    private User mUser;
    private SimpleAvatarResolver.DescriptorImpl mDescriptor;
    private SimpleAvatarResolver mSimpleAvatarResolver;
    
    @Rule
    public final JenkinsRule j = new JenkinsRule();
    
    public SimpleAvatarResolverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    
        mUser = AddUser("John Doe", "JohnDoe");
        mDescriptor = j.get(SimpleAvatarResolver.DescriptorImpl.class);
        
        ExtensionList<SimpleAvatarResolver> extensionList =
                j.getInstance().getExtensionList(SimpleAvatarResolver.class);
        mSimpleAvatarResolver = extensionList.get(0);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void NullTemplatePluginConfiguration() throws Exception {
        Assert.assertNull("image url is incorrect",
                GetResolvedUrl(PluginTestConfigs.AVATAR_URL_TEMPLATE_NULL));
    }
    
    @Test
    public void EmptyTemplatePluginConfiguration() throws Exception
    {
        Assert.assertNull("image url is incorrect",
                GetResolvedUrl(PluginTestConfigs.AVATAR_URL_TEMPLATE_EMPTY));
    }
    
    @Test
    public void StaticTemplatePluginConfiguration() throws Exception
    {
        Assert.assertEquals("image url is incorrect",
                PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC,
                GetResolvedUrl(PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC));
    }
    
    @Test
    public void StaticTemplateWithEncodedTemplateKeysPluginConfiguration() throws Exception
    {
        Assert.assertEquals("image url is incorrect",
                PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC_WITH_ENCODED_TEMPLATE_KEYS,
                GetResolvedUrl(PluginTestConfigs.AVATAR_URL_TEMPLATE_STATIC_WITH_ENCODED_TEMPLATE_KEYS));
    }
    
    @Test
    public void DynamicTemplatePluginConfiguration() throws Exception
    {
        Assert.assertEquals("image url is incorrect",
                PluginTestConfigs.AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT_EXPECTED_PEOPLE,
                GetResolvedUrl(PluginTestConfigs.AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT));
    }
    
    // util
    private String GetResolvedUrl(String urlTemplateConfig) {
        mDescriptor.setAvatarUrlTemplate(urlTemplateConfig);
        return mSimpleAvatarResolver.findAvatarFor(mUser, 32, 32);
    }
}
