package br.com.baobatech.textdiffwebtool.domain.model;

/**
 * Keeps the diff information
 */
public class TextDiff {

  public final boolean equal;
  public final int sizeDiff;
  public final int offset;
  public final int length;
  public final String diff;

  public TextDiff(
      final boolean equal,
      final int sizeDiff,
      final int offset,
      final int length,
      final String diff
  ) {
    this.equal = equal;
    this.sizeDiff = sizeDiff;
    this.offset = offset;
    this.length = length;
    this.diff = diff;
  }
}
