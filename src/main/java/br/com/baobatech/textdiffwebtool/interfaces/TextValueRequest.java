package br.com.baobatech.textdiffwebtool.interfaces;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TextValueRequest {

  public final String value;

  public TextValueRequest(
      @JsonProperty("value") final String value
  ) {
    this.value = value;
  }
}
