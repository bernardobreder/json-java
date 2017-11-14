package org.breder.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.breder.json.JsonInputStream.SyntaxException;

public class JsonObject {

  /** Map os values */
  protected final Map<String, Object> map;

  /**
   * Constructor by empty
   */
  public JsonObject() {
    map = new HashMap<String, Object>();
  }

  /**
   * Constructor by Map
   *
   * @param map
   */
  @SuppressWarnings("unchecked")
  public JsonObject(Map<?, ?> map) {
    this.map = (Map<String, Object>) map;
  }

  /**
   * Constructor by Json content
   *
   * @param content
   * @throws SyntaxException
   */
  public JsonObject(String content) throws SyntaxException {
    try {
      map = new JsonInputStream(new ByteArrayInputStream(content.getBytes(
        StandardCharsets.UTF_8))).readMap().map;
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isEmpty() {
    return map.isEmpty();
  }

  /**
   * Put a value for a key
   *
   * @param key
   * @param value
   * @return this
   */
  public JsonObject putIfPresent(String key, Object value) {
    if (value == null) {
      return this;
    }
    else if (value instanceof Collection<?>) {
      Collection<?> collection = (Collection<?>) value;
      if (collection.isEmpty()) {
        return this;
      }
    }
    else if (value instanceof Object[]) {
      Object[] objects = (Object[]) value;
      if (objects.length == 0) {
        return this;
      }
      value = Arrays.asList(objects);
    }
    else if (value instanceof Map<?, ?>) {
      Map<?, ?> map = (Map<?, ?>) value;
      if (map.isEmpty()) {
        return this;
      }
    }
    else if (value instanceof JsonObject) {
      JsonObject json = (JsonObject) value;
      if (json.isEmpty()) {
        return this;
      }
    }
    else if (value instanceof Optional<?>) {
      Optional<?> optional = (Optional<?>) value;
      if (optional.isPresent()) {
        return putIfPresent(key, optional.get());
      }
      else {
        return this;
      }
    }
    else if (value instanceof String) {
      String string = (String) value;
      if (string.trim().length() == 0) {
        return this;
      }
    }
    return put(key, value);
  }

  /**
   * Put a value for a key
   *
   * @param key
   * @param value
   * @return this
   */
  public JsonObject put(String key, Object value) {
    map.put(key, value);
    return this;
  }

  /**
   * Put a optional value if exist for a key. If the optional value is empty, no
   * key will be registed.
   *
   * @param key
   * @param value
   * @return this
   */
  public JsonObject put(String key, Optional<?> value) {
    if (value.isPresent()) {
      map.put(key, value.get());
    }
    return this;
  }

  /**
   * @param key
   * @return optional value
   */
  public Optional<Object> get(String key) {
    return Optional.ofNullable(map.get(key));
  }

  /**
   * Get the value of the key if it is a list. If it not a list, the value
   * change to a list built by supllier.
   *
   * @param key
   * @param supplier
   * @return value of the key
   */
  @SuppressWarnings("unchecked")
  public List<Object> getAsList(String key, Supplier<List<Object>> supplier) {
    Object object = map.get(key);
    if (object == null || object instanceof List == false) {
      List<Object> list = supplier.get();
      map.put(key, object = list);
    }
    return (List<Object>) object;
  }

  @SuppressWarnings("unchecked")
  public Optional<List<Object>> getAsList(String key) {
    Object object = map.get(key);
    if (object == null || object instanceof List == false) {
      return Optional.empty();
    }
    return Optional.of((List<Object>) object);
  }

  public Optional<List<JsonObject>> getAsJsonList(String key) {
    Optional<List<Object>> listOpt = getAsList(key);
    if (!listOpt.isPresent()) {
      return Optional.empty();
    }
    return Optional.of(listOpt.get().stream() //
      .filter(e -> e instanceof JsonObject) //
      .map(e -> (JsonObject) e) //
      .collect(Collectors.toList()));
  }

  public List<JsonObject> getAsJsonList(String key,
    Supplier<List<JsonObject>> supplier) {
    Optional<List<Object>> listOpt = getAsList(key);
    if (!listOpt.isPresent()) {
      return supplier.get();
    }
    return listOpt.get().stream() //
      .filter(e -> e instanceof JsonObject) //
      .map(e -> (JsonObject) e) //
      .collect(Collectors.toList());
  }

  public <E> List<E> getAsJsonList(String key,
    Function<JsonObject, E> function) {
    Optional<List<Object>> listOpt = getAsList(key);
    if (!listOpt.isPresent()) {
      return new ArrayList<>(0);
    }
    return listOpt.get().stream() //
      .filter(e -> e instanceof JsonObject) //
      .map(e -> (JsonObject) e) //
      .map(function) //
      .collect(Collectors.toList());
  }

  public Set<String> getAsListStringHashSet(String key) {
    return getAsListStringSet(key).orElseGet(() -> new HashSet<>());
  }

  public Optional<Set<String>> getAsListStringSet(String key) {
    Optional<List<Object>> listOpt = getAsList(key);
    if (!listOpt.isPresent()) {
      return Optional.empty();
    }
    return Optional.of(listOpt.get().stream() //
      .filter(e -> e instanceof String) //
      .map(e -> (String) e) //
      .collect(Collectors.toSet()));
  }

  public OptionalDouble getAsDouble(String key) {
    Object object = map.get(key);
    if (object instanceof Number) {
      Number number = (Number) object;
      return OptionalDouble.of(number.doubleValue());
    }
    return OptionalDouble.empty();
  }

  public OptionalLong getAsLong(String key) {
    Object object = map.get(key);
    if (object instanceof Number) {
      Number number = (Number) object;
      return OptionalLong.of(number.longValue());
    }
    return OptionalLong.empty();
  }

  public OptionalInt getAsInt(String key) {
    Object object = map.get(key);
    if (object instanceof Number) {
      Number number = (Number) object;
      return OptionalInt.of(number.intValue());
    }
    return OptionalInt.empty();
  }

  public Optional<String> getAsString(String key) {
    Object object = map.get(key);
    if (object instanceof String) {
      String string = (String) object;
      return Optional.of(string);
    }
    return Optional.empty();
  }

  public Set<Entry<String, Object>> entrys() {
    return map.entrySet();
  }

  public int size() {
    return map.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return new JsonWriter(this).toString();
    //    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    //    try {
    //      try (JsonOutputStream out = new JsonOutputStream(new BufferedOutputStream(
    //        bytes))) {
    //        out.writeObject(map);
    //      }
    //    }
    //    catch (IOException e) {
    //      throw new RuntimeException(e);
    //    }
    //    return new String(bytes.toByteArray(), StandardCharsets.UTF_8);
  }

}
