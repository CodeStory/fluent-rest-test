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

public class PostTest extends AbstractTest {
  @Test
  public void post_empty() {
    configure(routes -> routes
        .post("/", () -> Payload.created())
    );

    post("/").should()
      .respond(201)
      .contain("");
  }

  @Test
  public void post_body() {
    configure(routes -> routes
        .post("/", context -> new Payload("text/plain", context.request().content(), 201))
    );

    post("/", "<body>").should().respond(201).contain("<body>");
  }

  @Test
  public void post_form() {
    configure(routes -> routes
        .post("/", context -> new Payload("text/plain", context.get("key1") + "&" + context.get("key2"), 201))
    );

    post("/", "key1", "1st", "key2", "2nd").should().respond(201).contain("1st&2nd");
  }
}
