// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.sps.data.LoginInfo;
import com.google.sps.utils.UserConstants;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class HistoryServletTest {
    private static final String USER_ID = "userId";
    private static final User USER = new User("email@email.com", "authDomain", USER_ID);
    private static final Gson GSON = new Gson();

    private final LocalServiceTestHelper datastoreHelper =
            new LocalServiceTestHelper(
                    new LocalDatastoreServiceTestConfig()
                            .setDefaultHighRepJobPolicyUnappliedJobPercentage(0));

    private DatastoreService datastore;

    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;
    @Mock UserService userService;

    @Before
    public void setUp() {
        datastoreHelper.setUp();
        datastore = DatastoreServiceFactory.getDatastoreService();
        MockitoAnnotations.openMocks(this);
        when(userService.getCurrentUser()).thenReturn(USER);
    }

    @After
    public void tearDown() {
        datastoreHelper.tearDown();
    }

    private HistoryServlet getHistoryServlet() {
        return new HistoryServlet(userService);
    }

}
