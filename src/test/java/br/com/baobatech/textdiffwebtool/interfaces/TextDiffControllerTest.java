package br.com.baobatech.textdiffwebtool.interfaces;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.baobatech.textdiffwebtool.domain.TextComparator;
import br.com.baobatech.textdiffwebtool.domain.error.KeyNotFound;
import br.com.baobatech.textdiffwebtool.domain.error.MissingText;
import br.com.baobatech.textdiffwebtool.domain.model.Side;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(TextDiffController.class)
public class TextDiffControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TextComparator comparator;

  @Test
  public void addValueWithInvalidSideReply() throws Exception {

    // GIVEN
    String key = "1234";
    String body = "{\"value\":\"I am a developer\"}";

    // WHEN
    ResultActions resultActions = mvc.perform(post(format("/api/v1/diff/%s/invalid_side", key))
        .content(body)
        .contentType(MediaType.APPLICATION_JSON));

    // THEN
    resultActions.andExpect(status().is4xxClientError());
  }

  @Test
  public void addValueWithInvalidBodyReply() throws Exception {

    // GIVEN
    String key = "1234";
    String body = "\"value\":\"I am a developer\"}";

    // WHEN
    ResultActions resultActions = mvc.perform(post(format("/api/v1/diff/%s/left", key))
        .content(body)
        .contentType(MediaType.APPLICATION_JSON));

    // THEN
    resultActions.andExpect(status().is4xxClientError());
  }

  @Test
  public void addValueWithRightValuesSuccessful() throws Exception {

    // GIVEN
    String key = "1234";
    String body = "{\"value\":\"I am a developer\"}";

    doNothing().when(comparator).addValue(key, Side.LEFT, "I am a developer");

    // WHEN
    ResultActions resultActions = mvc.perform(post(format("/api/v1/diff/%s/left", key))
        .content(body)
        .contentType(MediaType.APPLICATION_JSON));

    // THEN
    verify(comparator, times(1)).addValue(key, Side.LEFT, "I am a developer");
    resultActions.andExpect(status().isCreated());
  }

  @Test
  public void getDiffWithoutValues() {

    // GIVEN
    String key = "1234";
    doThrow(new KeyNotFound("Key not found")).when(comparator).compare(key);

    // WHEN
    Throwable throwable = catchThrowable(() -> mvc.perform(get(format("/api/v1/diff/%s", key))
        .contentType(MediaType.APPLICATION_JSON)));

    // THEN
    verify(comparator, times(1)).compare(key);
    assertThat(throwable)
        .isNotNull()
        .hasCauseInstanceOf(KeyNotFound.class);
  }

  @Test
  public void getDiffWithoutLeftValues() {

    // GIVEN
    String key = "1234";
    doThrow(new MissingText("Left side has no value added")).when(comparator).compare(key);

    // WHEN
    Throwable throwable = catchThrowable(() -> mvc.perform(get(format("/api/v1/diff/%s", key))
        .contentType(MediaType.APPLICATION_JSON)));

    // THEN
    verify(comparator, times(1)).compare(key);
    assertThat(throwable)
        .isNotNull()
        .hasCauseInstanceOf(MissingText.class);
  }

  @Test
  public void getDiffWithoutRightValues() {

    // GIVEN
    String key = "1234";
    doThrow(new MissingText("Right side has no value added")).when(comparator).compare(key);

    // WHEN
    Throwable throwable = catchThrowable(() -> mvc.perform(get(format("/api/v1/diff/%s", key))
        .contentType(MediaType.APPLICATION_JSON)));

    // THEN
    verify(comparator, times(1)).compare(key);
    assertThat(throwable)
        .isNotNull()
        .hasCauseInstanceOf(MissingText.class);
  }
}
