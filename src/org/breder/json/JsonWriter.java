package org.breder.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Escreve objeto no formato json
 *
 *
 * @author Tecgraf
 */
public class JsonWriter {

  /** String builder */
  private final StringBuilder sb = new StringBuilder();

  /**
   * Construtor
   *
   * @param json
   */
  public JsonWriter(JsonObject json) {
    writeObject(json, 0);
  }

  /**
   * Escreve um objeto
   *
   * @param object
   * @param tab
   */
  protected void writeObject(Object object, int tab) {
    if (object == null) {
      this.writeNull(tab);
    }
    else if (object instanceof String) {
      this.writeString((String) object);
    }
    else if (object instanceof Boolean) {
      this.writeBoolean((Boolean) object);
    }
    else if (object instanceof Integer) {
      this.writeInteger((Integer) object);
    }
    else if (object instanceof Long) {
      this.writeLong((Long) object);
    }
    else if (object instanceof Float) {
      this.writeFloat((Float) object);
    }
    else if (object instanceof Double) {
      this.writeDouble((Double) object);
    }
    else if (object instanceof Number) {
      this.writeNumber((Number) object);
    }
    else if (object instanceof Date) {
      this.writeDate((Date) object);
    }
    else if (object instanceof Throwable) {
      this.writeThrowable((Throwable) object, tab);
    }
    else if (object instanceof ArrayList<?>) {
      this.writeArrayList((ArrayList<?>) object, tab);
    }
    else if (object instanceof Collection<?>) {
      this.writeCollection((Collection<?>) object, tab);
    }
    else if (object instanceof Map<?, ?>) {
      this.writeMap((Map<?, ?>) object, tab);
    }
    else if (object instanceof JsonObject) {
      this.writeMap(((JsonObject) object).map, tab);
    }
    else {
      throw new IllegalArgumentException("class not writable: " + object
        .getClass().getName());
    }
  }

  /**
   * Imprime o null
   *
   * @param tab
   */
  protected void writeNull(int tab) {
    sb.append("null");
  }

  /**
   * Escreve uma String
   *
   * @param text
   */
  protected void writeString(String text) {
    sb.append('\"');
    int length = text.length();
    for (int n = 0; n < length; n++) {
      char c = text.charAt(n);
      if (c == '\\') {
        sb.append('\\');
        sb.append('\\');
      }
      else if (c == '/') {
        sb.append('\\');
        sb.append('/');
      }
      else if (c == '\"') {
        sb.append('\\');
        sb.append('\"');
      }
      else if (c == '\r') {
        sb.append('\\');
        sb.append('r');
      }
      else if (c == '\n') {
        sb.append('\\');
        sb.append('n');
      }
      else if (c == '\t') {
        sb.append('\\');
        sb.append('t');
      }
      else if (c == '\b') {
        sb.append('\\');
        sb.append('b');
      }
      else if (c == '\f') {
        sb.append('\\');
        sb.append('f');
      }
      else {
        sb.append(c);
      }
    }
    sb.append('\"');
  }

  /**
   * Escreve True
   *
   *
   */
  protected void writeTrue() {
    sb.append("true");
  }

  /**
   * Escreve false
   *
   *
   */
  protected void writeFalse() {
    sb.append("false");
  }

  /**
   * Escreve um Boolean
   *
   * @param flag @
   */
  protected void writeBoolean(Boolean flag) {
    if (flag) {
      this.writeTrue();
    }
    else {
      this.writeFalse();
    }
  }

  /**
   * Escreve um Boolean
   *
   * @param flag @
   */
  protected void writeBoolean(boolean flag) {
    if (flag) {
      this.writeTrue();
    }
    else {
      this.writeFalse();
    }
  }

  /**
   * Escreve um Integer
   *
   * @param number @
   */
  protected void writeInteger(Integer number) {
    sb.append(number.toString());
  }

  /**
   * Escreve um int
   *
   * @param number @
   */
  protected void writeInteger(int number) {
    sb.append(Integer.toString(number));
  }

  /**
   * Escreve um Long
   *
   * @param number @
   */
  protected void writeLong(Long number) {
    sb.append(number.toString());
  }

  /**
   * Escreve um Long
   *
   * @param number @
   */
  protected void writeLong(long number) {
    sb.append(Long.toString(number));
  }

  /**
   * Escreve um Float
   *
   * @param number @
   */
  protected void writeFloat(Float number) {
    sb.append(number.toString());
  }

  /**
   * Escreve um float
   *
   * @param number @
   */
  protected void writeFloat(float number) {
    sb.append(Float.toString(number));
  }

  /**
   * Escreve um Double
   *
   * @param number @
   */
  protected void writeDouble(Double number) {
    sb.append(number.toString());
  }

  /**
   * Escreve um Double
   *
   * @param number @
   */
  protected void writeDouble(double number) {
    sb.append(Double.toString(number));
  }

  /**
   * Escreve um número
   *
   * @param number @
   */
  protected void writeNumber(Number number) {
    sb.append(number.toString());
  }

  /**
   * Escreve uma data
   *
   * @param object @
   */
  protected void writeDate(Date object) {
    Calendar c = Calendar.getInstance();
    c.setTime(object);
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH) + 1;
    int day = c.get(Calendar.DAY_OF_MONTH);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    int second = c.get(Calendar.SECOND);
    int milisecond = c.get(Calendar.MILLISECOND);
    StringBuilder sb = new StringBuilder(24);
    if (year < 1000) {
      sb.append('0');
    }
    if (year < 100) {
      sb.append('0');
    }
    if (year < 10) {
      sb.append('0');
    }
    sb.append(year);
    sb.append('-');
    if (month < 10) {
      sb.append('0');
    }
    sb.append(month);
    sb.append('-');
    if (day < 10) {
      sb.append('0');
    }
    sb.append(day);
    sb.append('T');
    if (hour < 10) {
      sb.append('0');
    }
    sb.append(hour);
    sb.append(':');
    if (minute < 10) {
      sb.append('0');
    }
    sb.append(minute);
    sb.append(':');
    if (second < 10) {
      sb.append('0');
    }
    sb.append(second);
    sb.append('.');
    if (milisecond < 100) {
      sb.append('0');
    }
    if (milisecond < 10) {
      sb.append('0');
    }
    sb.append(milisecond);
    sb.append('Z');
    this.writeString(sb.toString());
  }

  /**
   * Escreve um erro
   *
   * @param object @
   */
  protected void writeThrowable(Throwable object, int tab) {
    sb.append(("{\"type\":\"exception\",\"message\":\"" + object.getMessage()
      + "\"}"));
  }

  /**
   * Escreve uma lista
   *
   * @param list @
   */
  protected void writeArrayList(ArrayList<?> list, int tab) {
    sb.append('[');
    int size = list.size();
    for (int n = 0; n < size; n++) {
      this.writeObject(list.get(n), tab);
      if (n != size - 1) {
        sb.append(", ");
      }
    }
    sb.append(']');
  }

  /**
   * Escreve uma coleção
   *
   * @param list @
   */
  protected void writeCollection(Collection<?> list, int tab) {
    sb.append('[');
    Iterator<?> iterator = list.iterator();
    while (iterator.hasNext()) {
      this.writeObject(iterator.next(), tab);
      if (iterator.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append(']');
  }

  /**
   * Escreve um enum
   *
   * @param map @
   */
  @SuppressWarnings("rawtypes")
  protected void writeMap(Map<?, ?> map, int tab) {
    Entry[] entrys = map.entrySet().toArray(new Entry[map.size()]);
    Arrays.sort(entrys, (a, b) -> a.getKey().toString().compareTo(b.getKey()
      .toString()));
    sb.append("{");
    int entrySize = entrys.length;
    if (entrySize > 0) {
      sb.append("\n");
      for (int n = 0; n < entrySize; n++) {
        Object key = entrys[n].getKey();
        Object value = entrys[n].getValue();
        if (value != null) {
          writeTab(tab + 1);
          writeString(key.toString());
          sb.append(':');
          sb.append(' ');
          writeObject(value, tab + 1);
          if (n != entrySize - 1) {
            sb.append(',');
            sb.append('\n');
          }
          else {
            sb.append('\n');
            writeTab(tab);
          }
        }
      }
    }
    sb.append('}');
  }

  protected void writeTab(int tab) {
    for (int n = 0; n < tab; n++) {
      sb.append('\t');
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return sb.toString();
  }

}
