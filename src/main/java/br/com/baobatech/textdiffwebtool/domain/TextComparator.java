package br.com.baobatech.textdiffwebtool.domain;

import br.com.baobatech.textdiffwebtool.domain.error.ComparisonError;
import br.com.baobatech.textdiffwebtool.domain.error.KeyNotFound;
import br.com.baobatech.textdiffwebtool.domain.error.MissingText;
import br.com.baobatech.textdiffwebtool.domain.model.Side;
import br.com.baobatech.textdiffwebtool.domain.model.TextDiff;

/**
 * Responsible for compare two texts and provide useful information about differences.
 */
public interface TextComparator {

  /**
   * Adds a text for a specific side to be stored for future comparison. it will be identified by
   * the key.
   */
  void addValue(String key, Side side, String text);

  /**
   * Compares two texts previously added and returns the diff info.
   *
   * @param key the ID for each text pair
   * @return the information about the differences
   * @throws MissingText thrown if one side was not added previously
   * @throws KeyNotFound thrown if there is no text added for any side for the key
   * @throws ComparisonError thrown if any unexpected error occurs
   */
  TextDiff compare(String key) throws MissingText, KeyNotFound, ComparisonError;
}
