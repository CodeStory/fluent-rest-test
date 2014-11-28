/**
 * Copyright (C) 2013-2014 all@code-story.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package net.codestory.rest;

import net.codestory.http.WebServer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GetTest extends AbstractWebServerTest {
  static WebServer server = new WebServer().startOnRandomPort();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Override
  protected int getPort() {
    return server.port();
  }

  @Test
  public void get() {
    server.configure(routes -> routes.get("/", "hello"));

    get("/").produces("hello");
  }

  @Test
  public void fail_to_get() {
    server.configure(routes -> routes.get("/", "hello"));

    thrown.expect(AssertionError.class);

    get("/").produces("good bye");
  }
}