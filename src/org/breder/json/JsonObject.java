package org.breder.json;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.breder.json.JsonInputStream.SyntaxException;

public class JsonObject {

  protected final Map<String, Object> map;

  public JsonObject() {
    map = new HashMap<String, Object>();
  }

  public JsonObject(String content) throws SyntaxException {
    try {
      map = new JsonInputStream(new ByteArrayInputStream(content.getBytes(
        StandardCharsets.UTF_8))).readMap();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public JsonObject put(String key, Object value) {
    map.put(key, value);
    return this;
  }

  public Object get(String key) {
    return map.get(key);
  }

  public Set<Entry<String, Object>> entrys() {
    return map.entrySet();
  }

  public int size() {
    return map.size();
  }

  public String toJson() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try {
      try (JsonOutputStream out = new JsonOutputStream(new BufferedOutputStream(
        bytes))) {
        out.writeObject(map);
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new String(bytes.toByteArray(), StandardCharsets.UTF_8);
  }

  public void write(OutputStream output) throws IOException {
    try (JsonOutputStream out = new JsonOutputStream(output)) {
      out.writeObject(this);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return map.toString();
  }

}
