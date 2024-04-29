/**
 * Copyright (C) 2013-2015 all@code-story.net
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

import net.codestory.http.filters.basic.BasicAuthFilter;
import net.codestory.http.payload.Payload;
import org.junit.Test;

import java.util.Collections;

import static net.codestory.http.security.Users.singleUser;
import static org.assertj.core.api.Assertions.assertThat;

public class GetTest extends AbstractTest {
  @Test
  public void get() {
    configure(routes -> routes
        .get("/", "hello World")
    );

    get("/").should().respond(200).succeed().haveType("text/html").contain("hello");
  }

  @Test
  public void fail_to_get() {
    configure(routes -> routes
        .get("/", "hello")
    );

    shouldFail("Expecting [hello] to contain [good bye]");

    get("/").should().contain("good bye");
  }

  @Test
  public void get_with_header() {
    configure(routes -> routes
        .get("/", context -> context.header("name"))
    );

    get("/").withHeader("name", "value").should().contain("value");
  }

  @Test
  public void get_with_headers() {
    configure(routes -> routes
        .get("/", context -> context.header("first") + context.header("second"))
    );

    get("/").withHeader("first", "1").withHeader("second", "2").should().contain("12");
  }

  @Test
  public void get_with_preemptive_authentication() {
    configure(routes -> routes
        .filter(new BasicAuthFilter("/", "realm", singleUser("login", "pwd")))
        .get("/", context -> "Secret")
    );

    get("/").withPreemptiveAuthentication("login", "pwd").should().contain("Secret");
    get("/").withPreemptiveAuthentication("", "").should().respond(401);
    get("/").should().respond(401);
  }

  @Test
  public void get_with_basic_authentication() {
    configure(routes -> routes
        .filter(new BasicAuthFilter("/", "realm", singleUser("login", "pwd")))
        .get("/", context -> "Secret")
    );

    get("/").withAuthentication("login", "pwd").should().contain("Secret");
    get("/").withAuthentication("", "").should().respond(401);
    get("/").should().respond(401).fail();
  }

  @Test
  public void get_cookie() {
    configure(routes -> routes
        .get("/", context -> new Payload("Hello").withCookie("name", "value"))
    );

    get("/").should().haveCookie("name", "value");
  }

  @Test
  public void get_cookies() {
    configure(routes -> routes
        .get("/", context -> new Payload("").withCookie("first", "1st").withCookie("second", "2nd"))
    );

    get("/").should().haveCookie("first", "1st").haveCookie("second", "2nd");
  }

  @Test
  public void fail_without_cookie() {
    configure(routes -> routes
        .get("/", context -> "")
    );

    shouldFail("Expecting [cookie ??] to be [??]. It was [null]");

    get("/").should().haveCookie("??", "??");
  }

  @Test
  public void fail_with_wrong_cookie() {
    configure(routes -> routes
        .get("/", context -> new Payload("").withCookie("name", "value"))
    );

    shouldFail("Expecting [cookie name] to be [??]. It was [value]");

    get("/").should().haveCookie("name", "??");
  }

  @Test
  public void get_header() {
    configure(routes -> routes
        .get("/", context -> new Payload("").withHeader("name", "value"))
    );

    get("/").should().haveHeader("name", "value");
  }

  @Test
  public void fail_without_header() {
    configure(routes -> routes
        .get("/", context -> new Payload(""))
    );

    shouldFail("Expecting [header name] to be [??]. It was [null]");

    get("/").should().haveHeader("name", "??");
  }

  @Test
  public void fail_with_wrong_header() {
    configure(routes -> routes
        .get("/", context -> new Payload("").withHeader("name", "value"))
    );

    shouldFail("Expecting [header name] to be [??]. It was [value]");

    get("/").should().haveHeader("name", "??");
  }

  @Test
  public void empty() {
    configure(routes -> routes
        .get("/", "")
    );

    get("/").should().beEmpty();
  }

  @Test
  public void not() {
    configure(routes -> routes
        .get("/", "Hello")
    );

    get("/").should().not().beEmpty();
    get("/").should().not().contain("Bye");
  }

  @Test
  public void chain() {
    configure(routes -> routes
        .get("/", "Hello")
    );

    get("/")
        .should().contain("Hello")
        .should().not().beEmpty()
        .should().not().contain("Bye");
  }

  @Test
  public void get_response() {
    configure(routes -> routes
        .get("/", "Hello")
    );

    Response response = get("/").response();

    assertThat(response.contentType()).isEqualTo("text/html;charset=UTF-8");
    assertThat(response.content()).isEqualTo("Hello");
    assertThat(response.code()).isEqualTo(200);
  }

  @Test
  public void get_response_with_headers() {
    configure(routes -> routes
        .get("/", (context) -> {
          context.response().setHeader("foo", "bar");
          context.response().setHeader("baz", "qux");
          return "Hello foo";
        })
    );
    Response response = get("/").response();
    assertThat(response.content()).isEqualTo("Hello foo");
    assertThat(response.header("foo")).isEqualTo(Collections.singletonList("bar"));
    assertThat(response.header("baz")).isEqualTo(Collections.singletonList("qux"));
  }

  @Test
  public void get_response_with_follow_redirect_false() {
    configure(routes -> routes
        .get("/", (context) -> Payload.temporaryRedirect("http://foo.bar"))
    );
    Response response = get("/").withFollowRedirect(false).response();
    assertThat(response.code()).isEqualTo(307);
    assertThat(response.header("Location")).isEqualTo(Collections.singletonList("http://foo.bar"));
  }
}
