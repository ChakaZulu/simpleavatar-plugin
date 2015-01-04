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

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.User;
import hudson.tasks.UserAvatarResolver;
import hudson.util.FormValidation;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author Michael Schuele
 */
@Extension
public final class SimpleAvatarResolver extends UserAvatarResolver implements Describable<SimpleAvatarResolver> {
    //private static final String KEY_USER = "%3Cu%3E";
    private static final String KEY_USER = "<u>";
    //private static final String KEY_ID = "%3Ci%3E";
    private static final String KEY_ID = "<i>";
    //private static final String KEY_WIDTH = "%3Cw%3E";
    private static final String KEY_WIDTH = "<w>";
    //private static final String KEY_HEIGHT = "%3Ch%3E";
    private static final String KEY_HEIGHT = "<h>";
    
    public SimpleAvatarResolver() {
    }
    
    @Override
    public Descriptor<SimpleAvatarResolver> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }

    @Override
    public String findAvatarFor(User user, int width, int height) {
        String avatarUrlTemplate = ((DescriptorImpl)getDescriptor()).getAvatarUrlTemplate();
        if (avatarUrlTemplate == null || avatarUrlTemplate.isEmpty())
        {
            return null;
        }
        return avatarUrlTemplate
                .replace(KEY_USER, user.getFullName())
                .replace(KEY_ID, user.getId())
                .replace(KEY_WIDTH, String.valueOf(width))
                .replace(KEY_HEIGHT, String.valueOf(height));
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<SimpleAvatarResolver> {
        
        public DescriptorImpl()
        {
            super(SimpleAvatarResolver.class);
            load();
        }
        
        private String avatarUrlTemplate;
        
        // called by global.jelly
        public String getAvatarUrlTemplate() {
            return avatarUrlTemplate;
        }

        // called by bindJSON
        public void setAvatarUrlTemplate(String avatarUrlTemplate) {
            this.avatarUrlTemplate = avatarUrlTemplate;//getEscapedURL(avatarUrlTemplate);
        }
        
        private String getEscapedURL(String string) {
            try {
                String decodedURLString = URLDecoder.decode(string, "UTF-8");
                URL decodedURL = new URL(decodedURLString);
                URI escapedURI = new URI(decodedURL.getProtocol(), decodedURL.getUserInfo(), decodedURL.getHost(), decodedURL.getPort(), decodedURL.getPath(), decodedURL.getQuery(), decodedURL.getRef()); 
                return escapedURI.toURL().toString(); 
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(SimpleAvatarResolver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(SimpleAvatarResolver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(SimpleAvatarResolver.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }
        
        @Override
        public String getDisplayName() {
            return "Simple Avatar Resolver";
        }
        
        public FormValidation doCheckAvatarUrlTemplate(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set an url template");
            return FormValidation.ok();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            req.bindJSON(this, formData);
            save();
            return super.configure(req,formData);
        }
    }
}

