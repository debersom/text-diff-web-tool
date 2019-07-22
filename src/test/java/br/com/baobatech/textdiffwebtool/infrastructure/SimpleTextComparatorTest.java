package br.com.baobatech.textdiffwebtool.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.baobatech.textdiffwebtool.domain.error.InvalidKey;
import br.com.baobatech.textdiffwebtool.domain.error.InvalidSide;
import br.com.baobatech.textdiffwebtool.domain.error.KeyNotFound;
import br.com.baobatech.textdiffwebtool.domain.error.MissingText;
import br.com.baobatech.textdiffwebtool.domain.model.Side;
import br.com.baobatech.textdiffwebtool.domain.model.TextDiff;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleTextComparatorTest {

  @Test
  public void addsValueWithNullKeyThrowsException() {
    // GIVEN
    SimpleTextComparator comparator = new SimpleTextComparator(null);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.addValue(null, Side.LEFT, "ABC"));

    // THEN
    assertThat(throwable)
        .isNotNull()
        .isInstanceOf(InvalidKey.class)
        .hasMessage("Key should not be empty or null to add value");
  }

  @Test
  public void addsValueWithEmptyKeyThrowsException() {
    // GIVEN
    SimpleTextComparator comparator = new SimpleTextComparator(null);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.addValue("", Side.LEFT, "ABC"));

    // THEN
    assertThat(throwable)
        .isNotNull()
        .isInstanceOf(InvalidKey.class)
        .hasMessage("Key should not be empty or null to add value");
  }

  @Test
  public void addsValueWithNullSideThrowsException() {
    // GIVEN
    SimpleTextComparator comparator = new SimpleTextComparator(null);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.addValue("123", null, "ABC"));

    // THEN
    assertThat(throwable)
        .isNotNull()
        .isInstanceOf(InvalidSide.class)
        .hasMessage("Side should not be null to add value");
  }

  @Test
  public void addsNullValueWithKeyAndLeftSideSuccessful() {
    // GIVEN
    TextDiffRepository repository = Mockito.mock(TextDiffRepository.class);
    String key = "123";
    Side side = Side.LEFT;

    SimpleTextComparator comparator = new SimpleTextComparator(repository);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.addValue(key, side, null));

    // THEN
    assertThat(throwable).doesNotThrowAnyException();
    verify(repository, times(1)).addValue(key, side, null);
  }

  @Test
  public void addsEmptyValueWithKeyAndRightSideSuccessful() {
    // GIVEN
    TextDiffRepository repository = Mockito.mock(TextDiffRepository.class);
    String key = "123";
    Side side = Side.RIGHT;

    SimpleTextComparator comparator = new SimpleTextComparator(repository);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.addValue(key, side, ""));

    // THEN
    assertThat(throwable).doesNotThrowAnyException();
    verify(repository, times(1)).addValue(key, side, "");
  }

  @Test
  public void addsValueWithKeyAndRightLeftSuccessful() {
    // GIVEN
    TextDiffRepository repository = Mockito.mock(TextDiffRepository.class);
    String key = "123";
    Side side = Side.RIGHT;
    String value = "Value 1234";

    SimpleTextComparator comparator = new SimpleTextComparator(repository);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.addValue(key, side, value));

    // THEN
    assertThat(throwable).doesNotThrowAnyException();
    verify(repository, times(1)).addValue(key, side, value);
  }

  @Test
  public void compareWithNullKeyThrowsException() {
    // GIVEN
    SimpleTextComparator comparator = new SimpleTextComparator(null);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.compare(null));

    // THEN
    assertThat(throwable)
        .isNotNull()
        .isInstanceOf(InvalidKey.class)
        .hasMessage("Key should not be empty or null to add value");
  }

  @Test
  public void compareWithEmptyKeyThrowsException() {
    // GIVEN
    SimpleTextComparator comparator = new SimpleTextComparator(null);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.compare(null));

    // THEN
    assertThat(throwable)
        .isNotNull()
        .isInstanceOf(InvalidKey.class)
        .hasMessage("Key should not be empty or null to add value");
  }

  @Test
  public void compareWithValidKeyWithoutLeftSideThrowsException() {
    // GIVEN
    TextDiffRepository repository = Mockito.mock(TextDiffRepository.class);
    String key = "123";
    doReturn(null).when(repository).getValue(key, Side.LEFT);
    doReturn("ABC").when(repository).getValue(key, Side.RIGHT);

    SimpleTextComparator comparator = new SimpleTextComparator(repository);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.compare(key));

    // THEN
    assertThat(throwable)
        .isNotNull()
        .isInstanceOf(MissingText.class)
        .hasMessage("Left side has no value added");
  }

  @Test
  public void compareWithValidKeyWithoutRightSideThrowsException() {
    // GIVEN
    TextDiffRepository repository = Mockito.mock(TextDiffRepository.class);
    String key = "123";
    doReturn("ABC").when(repository).getValue(key, Side.LEFT);
    doReturn(null).when(repository).getValue(key, Side.RIGHT);

    SimpleTextComparator comparator = new SimpleTextComparator(repository);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.compare(key));

    // THEN
    assertThat(throwable)
        .isNotNull()
        .isInstanceOf(MissingText.class)
        .hasMessage("Right side has no value added");
  }

  @Test
  public void compareWithValidKeyWithoutAnySideThrowsException() {
    // GIVEN
    TextDiffRepository repository = Mockito.mock(TextDiffRepository.class);
    String key = "123";
    doReturn(null).when(repository).getValue(key, Side.LEFT);
    doReturn(null).when(repository).getValue(key, Side.RIGHT);

    SimpleTextComparator comparator = new SimpleTextComparator(repository);

    // WHEN
    Throwable throwable = catchThrowable(() -> comparator.compare(key));

    // THEN
    assertThat(throwable)
        .isNotNull()
        .isInstanceOf(KeyNotFound.class)
        .hasMessage("No value was added for this key");
  }

  @Test
  public void compareWithValidKeyAndEqualsValues() {
    // GIVEN
    TextDiffRepository repository = Mockito.mock(TextDiffRepository.class);
    String key = "123";
    doReturn("ABC").when(repository).getValue(key, Side.LEFT);
    doReturn("ABC").when(repository).getValue(key, Side.RIGHT);

    SimpleTextComparator comparator = new SimpleTextComparator(repository);

    // WHEN
    TextDiff textDiff = comparator.compare(key);

    // THEN
    verify(repository, times(1)).getValue(key, Side.LEFT);
    verify(repository, times(1)).getValue(key, Side.RIGHT);
    assertThat(textDiff)
        .isNotNull()
        .hasFieldOrPropertyWithValue("equal", true)
        .hasFieldOrPropertyWithValue("sizeDiff", 0)
        .hasFieldOrPropertyWithValue("offset", -1)
        .hasFieldOrPropertyWithValue("length", -1)
        .hasFieldOrPropertyWithValue("diff", null);
  }

  @Test
  public void compareWithValidKeyAndDiffValues() {
    // GIVEN
    TextDiffRepository repository = Mockito.mock(TextDiffRepository.class);
    String key = "123";
    doReturn("i am a developer").when(repository).getValue(key, Side.LEFT);
    doReturn("i am a designer").when(repository).getValue(key, Side.RIGHT);

    SimpleTextComparator comparator = new SimpleTextComparator(repository);

    // WHEN
    TextDiff textDiff = comparator.compare(key);

    // THEN
    verify(repository, times(1)).getValue(key, Side.LEFT);
    verify(repository, times(1)).getValue(key, Side.RIGHT);
    assertThat(textDiff)
        .isNotNull()
        .hasFieldOrPropertyWithValue("equal", false)
        .hasFieldOrPropertyWithValue("sizeDiff", -1)
        .hasFieldOrPropertyWithValue("offset", 9)
        .hasFieldOrPropertyWithValue("length", 6)
        .hasFieldOrPropertyWithValue("diff", "signer");
  }
}
