package br.com.baobatech.textdiffwebtool.interfaces;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TextDiffControllerIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void addValueWithInvalidBodyReply() {

    // GIVEN
    String key = "1234";
    String body = "{\"value:\"I am a developer\"}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(body, headers);

    // WHEN
    ResponseEntity<String> entity = restTemplate.postForEntity(
        format("/api/v1/diff/%s/left", key), request, String.class);

    // THEN
    assertThat(entity)
        .isNotNull()
        .hasFieldOrPropertyWithValue("status", 400);
  }

  @Test
  public void addValueWithInvalidSideReply() {

    // GIVEN
    String key = "1234";
    String body = "{\"value\":\"I am a developer\"}";

    // WHEN
    ResponseEntity<String> entity = restTemplate.postForEntity(
        format("/api/v1/diff/%s/invalid_side", key), body, String.class);

    // THEN
    assertThat(entity)
        .isNotNull()
        .hasFieldOrPropertyWithValue("status", 400);
  }

  @Test
  public void addDifferentValuesAndCompareSuccessful() {

    // GIVEN
    String key = "test_diff_values";
    String body1 = "{\"value\":\"I am a developer\"}";
    String body2 = "{\"value\":\"I am a designer\"}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request1 = new HttpEntity<>(body1, headers);
    HttpEntity<String> request2 = new HttpEntity<>(body2, headers);

    // WHEN
    ResponseEntity<String> entity1 = restTemplate.postForEntity(
        format("/api/v1/diff/%s/left", key), request1, String.class);
    ResponseEntity<String> entity2 = restTemplate.postForEntity(
        format("/api/v1/diff/%s/right", key), request2, String.class);
    ResponseEntity<String> entity3 = restTemplate.getForEntity(
        format("/api/v1/diff/%s", key), String.class);

    // THEN
    assertThat(entity1)
        .isNotNull()
        .hasFieldOrPropertyWithValue("status", 201);
    assertThat(entity2)
        .isNotNull()
        .hasFieldOrPropertyWithValue("status", 201);
    assertThat(entity3)
        .isNotNull()
        .hasFieldOrPropertyWithValue("status", 200)
        .hasFieldOrPropertyWithValue("body", "{\"equal\":false,\"sizeDiff\":-1,\"offset\":9,\"length\":6,\"diff\":\"signer\"}");
  }

  @Test
  public void addDSameValuesAndCompareSuccessful() {

    // GIVEN
    String key = "test_same_values";
    String body1 = "{\"value\":\"I am a developer\"}";
    String body2 = "{\"value\":\"I am a developer\"}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request1 = new HttpEntity<>(body1, headers);
    HttpEntity<String> request2 = new HttpEntity<>(body2, headers);

    // WHEN
    ResponseEntity<String> entity1 = restTemplate.postForEntity(
        format("/api/v1/diff/%s/left", key), request1, String.class);
    ResponseEntity<String> entity2 = restTemplate.postForEntity(
        format("/api/v1/diff/%s/right", key), request2, String.class);
    ResponseEntity<String> entity3 = restTemplate.getForEntity(
        format("/api/v1/diff/%s", key), String.class);

    // THEN
    assertThat(entity1)
        .isNotNull()
        .hasFieldOrPropertyWithValue("status", 201);
    assertThat(entity2)
        .isNotNull()
        .hasFieldOrPropertyWithValue("status", 201);
    assertThat(entity3)
        .isNotNull()
        .hasFieldOrPropertyWithValue("status", 200)
        .hasFieldOrPropertyWithValue("body", "{\"equal\":true,\"sizeDiff\":0,\"offset\":-1,\"length\":-1,\"diff\":null}");
  }
}
