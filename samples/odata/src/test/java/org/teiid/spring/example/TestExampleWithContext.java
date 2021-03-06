/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.teiid.spring.example;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Application.class, TestConfiguration.class, RestTemplate.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-context.properties")
public class TestExampleWithContext {
    @Autowired
    TestRestTemplate web;

    @LocalServerPort
    private int port;

    private String url() {
        return "http://localhost:" + port+"/foo/odata";
    }

    @Test
    public void test() throws Exception{
        ResponseEntity<String> response = web.getForEntity(url()+"/CUSTOMER", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void testRoot() throws Exception{
        ResponseEntity<String> response = web.getForEntity(url(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        response = web.getForEntity(url()+"/$metadata", String.class);
        assertTrue(response.getBody().contains("/static/org.teiid.v1.xml"));
    }

    @Test
    public void testMetadata() throws Exception {
        TestExample.olingoClient(url());
        TestExample.olingoClient(url()+"/accounts");
    }

    @Test
    public void testWithModelName() throws Exception{
        ResponseEntity<String> response = web.getForEntity(url()+"/accounts/CUSTOMER", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void testWithModelName2() throws Exception{
        ResponseEntity<String> response = web.getForEntity(url()+"/accounts2/CUSTOMER", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void testAPI() throws Exception{
        ResponseEntity<String> response = web.getForEntity("http://localhost:" + port+"/foo/api/hi", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo("hello"));
    }

    @Test
    public void testSwagger() throws Exception{
        ResponseEntity<String> response = web.getForEntity(url()+"/swagger.json", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void testOpenApi() throws Exception{
        ResponseEntity<String> response = web.getForEntity(url()+"/openapi.json", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }
}
