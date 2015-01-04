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

/**
 *
 * @author Michael Schuele
 */
public class PluginTestConfigs {
    public static final String AVATAR_URL_TEMPLATE_DOMAIN = "http://intranet.local";
    public static final String AVATAR_URL_TEMPLATE_EMPTY = "";
    public static final String AVATAR_URL_TEMPLATE_NULL = null;
    public static final String AVATAR_URL_TEMPLATE_STATIC = AVATAR_URL_TEMPLATE_DOMAIN + "/image.png";
    public static final String AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT = AVATAR_URL_TEMPLATE_DOMAIN + "/<u>/<i>/image_<w>_<h>.png";
    public static final String AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT_EXPECTED_USER = AVATAR_URL_TEMPLATE_DOMAIN + "/John Doe/JohnDoe/image_48_48.png";
    public static final String AVATAR_URL_TEMPLATE_NAME_ID_WIDTH_HEIGHT_EXPECTED_PEOPLE = AVATAR_URL_TEMPLATE_DOMAIN + "/John Doe/JohnDoe/image_32_32.png";
    public static final String AVATAR_URL_TEMPLATE_STATIC_WITH_ENCODED_TEMPLATE_KEYS = AVATAR_URL_TEMPLATE_DOMAIN + "/image.png?%3Cu%3E=user&%3Ch%3E=16";
}
