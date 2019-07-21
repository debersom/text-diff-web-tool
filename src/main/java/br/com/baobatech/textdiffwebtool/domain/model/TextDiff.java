package br.com.baobatech.textdiffwebtool.domain.model;

/**
 * Keeps the diff information
 */
public class TextDiff {

  public final boolean equals;
  public final int sizeDiff;
  public final double diffPercent;

  public TextDiff(
      final boolean equals,
      final int sizeDiff,
      final double diffPercent
  ) {
    this.equals = equals;
    this.sizeDiff = sizeDiff;
    this.diffPercent = diffPercent;
  }
}
