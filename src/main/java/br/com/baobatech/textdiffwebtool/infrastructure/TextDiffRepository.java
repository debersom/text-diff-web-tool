package br.com.baobatech.textdiffwebtool.infrastructure;

import br.com.baobatech.textdiffwebtool.domain.model.Side;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class TextDiffRepository {

  public void addValue(
      @NonNull String key,
      @NonNull final Side side,
      final String text
  ) {

  }

  public String getValue(
      @NonNull String key,
      @NonNull final Side side
  ) {
    return null;
  }
}
