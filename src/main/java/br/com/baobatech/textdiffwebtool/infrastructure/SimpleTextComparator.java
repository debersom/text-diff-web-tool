package br.com.baobatech.textdiffwebtool.infrastructure;

import br.com.baobatech.textdiffwebtool.domain.TextComparator;
import br.com.baobatech.textdiffwebtool.domain.error.ComparisonError;
import br.com.baobatech.textdiffwebtool.domain.error.InvalidKey;
import br.com.baobatech.textdiffwebtool.domain.error.InvalidSide;
import br.com.baobatech.textdiffwebtool.domain.error.KeyNotFound;
import br.com.baobatech.textdiffwebtool.domain.error.MissingText;
import br.com.baobatech.textdiffwebtool.domain.model.Side;
import br.com.baobatech.textdiffwebtool.domain.model.TextDiff;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class SimpleTextComparator implements TextComparator {

  private final TextDiffRepository repository;

  @Autowired
  public SimpleTextComparator(
      TextDiffRepository repository
  ) {
    this.repository = repository;
  }

  @Override
  public void addValue(
      @NonNull String key,
      @NonNull final Side side,
      final String text
  ) {
    if (StringUtils.isEmpty(key)) {
      throw new InvalidKey("Key should not be empty or null to add value");
    }
    if (side == null) {
      throw new InvalidSide("Side should not be null to add value");
    }

    this.repository.addValue(key, side, text);
  }

  @Override
  public TextDiff compare(
      @NonNull final String key
  ) throws MissingText, KeyNotFound, ComparisonError {
    if (StringUtils.isEmpty(key)) {
      throw new InvalidKey("Key should not be empty or null to add value");
    }

    String leftValue = this.repository.getValue(key, Side.LEFT);
    String rightValue = this.repository.getValue(key, Side.RIGHT);

    if (leftValue == null && rightValue == null) {
      throw new KeyNotFound("No value was added for this key");
    } else if (leftValue == null) {
      throw new MissingText("Left side has no value added");
    } else if (rightValue == null) {
      throw new MissingText("Right side has no value added");
    }

    int indexOfDiff = StringUtils.indexOfDifference(leftValue, rightValue);
    String difference = null;
    if(indexOfDiff >= 0) {
      difference = StringUtils.difference(leftValue, rightValue);
    }

    return new TextDiff(
        indexOfDiff < 0,
        rightValue.length() - leftValue.length(),
        indexOfDiff,
        difference != null ? difference.length() : -1,
        difference
    );
  }
}
