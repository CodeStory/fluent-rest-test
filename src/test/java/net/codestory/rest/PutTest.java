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

import net.codestory.http.payload.Payload;
import org.junit.Test;

public class PutTest extends AbstractTest {
  @Test
  public void put_empty() {
    server.configure(routes -> routes
        .put("/", () -> Payload.created())
    );

    put("/")
      .produces(201)
      .produces("")
      .produces(201, "");
  }

  @Test
  public void put_body() {
    server.configure(routes -> routes
        .put("/", context -> new Payload("text/plain", context.contentAsString(), 201))
    );

    put("/", "<body>")
      .produces(201)
      .produces("<body>")
      .produces("text/plain", "<body>")
      .produces(201, "<body>")
      .produces(201, "text/plain", "<body>");
  }

  @Test
  public void put_form() {
    server.configure(routes -> routes
        .put("/", context -> new Payload("text/plain", context.get("key1") + "&" + context.get("key2"), 201))
    );

    put("/", "key1", "1st", "key2", "2nd")
      .produces(201)
      .produces("1st&2nd")
      .produces("text/plain", "1st&2nd")
      .produces(201, "1st&2nd")
      .produces(201, "text/plain", "1st&2nd");
  }
}
