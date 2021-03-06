/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.api.user.server.dao;

import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;

import java.util.Map;

/**
 * Defines data access object contract for user preferences.
 *
 * @author Yevhenii Voevodin
 */
public interface PreferenceDao {

    /**
     * Sets user preferences, overrides existing preferences if any.
     *
     * @param userId
     *         user identifier
     * @param preferences
     *         new preferences, if preferences are empty - removes user preferences
     * @throws NullPointerException
     *         when preferences or userId is null
     * @throws NotFoundException
     *         when user with given identifier doesn't exist
     * @throws ServerException
     *         when any other error occurs
     */
    void setPreferences(String userId, Map<String, String> preferences) throws ServerException, NotFoundException;

    /**
     * Gets user preferences.
     *
     * <p>Note that this method must always return upgradable map, thus it may be used as:
     * <pre>{@code
     *      Map<String, String> prefs = dao.getPreferences("user123");
     *      prefs.put("key", "secret");
     *      dao.setPreferences("user123", prefs);
     * }</pre>
     *
     * @param userId
     *         user identifier
     * @return user preferences, or empty map
     * @throws NullPointerException
     *         when userId is null
     * @throws ServerException
     *         when any error occurs
     */
    Map<String, String> getPreferences(String userId) throws ServerException;

    /**
     * Gets user preferences filtered with given regexp.
     *
     * <p>Note that this method must always return upgradable map, thus it may be used as:
     * <pre>{@code
     *      Map<String, String> prefs = dao.getPreferences("user123", ".*key.*");
     *      prefs.put("new-key", "secret");
     *      prefs.setPreferences("user123", prefs);
     * }</pre>
     *
     * @param userId
     *         user identifier
     * @param filter
     *         {@link java.util.regex.Pattern regexp} which is used to filter preferences by their keys
     * @return user preferences with keys which match given {@code filter} or empty map if user doesn't have any preferences
     * @throws NullPointerException
     *         {@code userId} or {@code filter} is null
     * @throws ServerException
     *         when any other error occurs
     */
    Map<String, String> getPreferences(String userId, String filter) throws ServerException;

    /**
     * Removes user preferences.
     *
     * <p>Note that this method doesn't throw any exception if user doesn't exist
     * or user doesn't have any preferences
     *
     * @param userId
     *         user identifier
     * @throws NullPointerException
     *         when {@code userId} is null
     * @throws ServerException
     *         when any error occurs
     */
    void remove(String userId) throws ServerException;
}
