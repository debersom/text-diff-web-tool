package br.com.baobatech.textdiffwebtool.infrastructure;

import static java.util.Optional.ofNullable;

import br.com.baobatech.textdiffwebtool.domain.error.KeyNotFound;
import br.com.baobatech.textdiffwebtool.domain.model.Side;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class TextDiffRepository {

  ConcurrentMap<String, MutablePair<String, String>> valuesByKey = new ConcurrentHashMap<>();

  public void addValue(
      @NonNull String key,
      @NonNull final Side side,
      final String text
  ) {
    MutablePair<String, String> pair = valuesByKey.computeIfAbsent(key, k -> new MutablePair<>());
    if (side == Side.LEFT) {
      pair.setLeft(text);
    } else {
      pair.setRight(text);
    }
  }

  public String getValue(
      @NonNull String key,
      @NonNull final Side side
  ) {
    MutablePair<String, String> pair = ofNullable(valuesByKey.get(key))
        .orElseThrow(() -> new KeyNotFound("No value was added for this key"));
    return side == Side.LEFT ? pair.left : pair.right;
  }
}
