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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GetTest extends AbstractTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void get() {
    server.configure(routes -> routes.get("/", "hello"));

    get("/")
      .produces(200)
      .produces("hello")
      .produces("text/html;charset=UTF-8", "hello")
      .produces(200, "hello")
      .produces(200, "text/html;charset=UTF-8", "hello");
  }

  @Test
  public void fail_to_get() {
    server.configure(routes -> routes.get("/", "hello"));

    thrown.expect(AssertionError.class);
    thrown.expectMessage("Expecting \"good bye\" was \"hello\"");

    get("/").produces("good bye");
  }

  @Test
  public void get_with_header() {
    server.configure(routes -> routes.get("/", context -> context.header("name")));

    get("/").withHeader("name", "value").produces("value");
  }
}
