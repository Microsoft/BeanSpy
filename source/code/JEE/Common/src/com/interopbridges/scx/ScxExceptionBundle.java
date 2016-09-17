/**
 * Copyright (c) Microsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use 
 * this file except in compliance with the License. You may obtain a copy of the 
 * License at http://www.apache.org/licenses/LICENSE-2.0.
 *  
 * THIS CODE IS PROVIDED *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS 
 * OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION 
 * ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE, 
 * MERCHANTABLITY OR NON-INFRINGEMENT. 
 *
 * See the Apache Version 2.0 License for specific language governing 
 * permissions and limitations under the License.
 */

package com.interopbridges.scx;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * <p>
 * Class and Factor methods for getting access to a Resource Bundler for
 * internationalization of exception messages.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class ScxExceptionBundle {

    /**
     * <p>
     * Static method for retrieving a bundle at the given input.
     * </p>
     * 
     * @param propertyFilename
     *            String containing a full class-path to the property file. For
     *            instance.
     * 
     * @return Reference to the exception bundle specified by the input.
     *         Run-time exceptions are thrown if the file for the default locale
     *         cannot be found.
     */
    public static ScxExceptionBundle getBundle(String propertyFilename) {
        return new ScxExceptionBundle(ResourceBundle
                .getBundle(propertyFilename));
    }

    /**
     * <p>
     * Static method for retrieving a bundle at the given input.
     * </p>
     * 
     * @param propertyFilename
     *            String containing a full class-path to the property file. For
     *            instance
     * 
     * @param locale
     *            A Target/specific locale of resources to get.
     * 
     * @return Reference to the exception bundle specified by the input.
     *         Run-time exceptions are thrown if the file for the default locale
     *         cannot be found.
     */
    public static ScxExceptionBundle getBundle(String propertyFilename,
            Locale locale) {
        return new ScxExceptionBundle(ResourceBundle.getBundle(
                propertyFilename, locale));
    }

    /**
     * <p>
     * Handle to the resource bundle.
     * </p>
     */
    private ResourceBundle _bundle = null;

    /**
     * <p>
     * Private Default Constructor
     * </p>
     * 
     * @param bundle
     *            Java bundle to use
     */
    private ScxExceptionBundle(ResourceBundle bundle) {
        _bundle = bundle;
    }

    /**
     * <p>
     * Get the localized message for the given Exception Code.
     * </p>
     * 
     * @param key
     *            A key to look-up in the resource file.
     * 
     * @param args
     *            List of arguments to fill in to the localized message.
     * 
     * @return A formated, localized String as specified by the input
     *         parameters.
     * 
     */
    public String getString(ScxExceptionCode key, Object[] args) {
        return MessageFormat.format(_bundle.getString(key.getCode()), args);
    }
}
