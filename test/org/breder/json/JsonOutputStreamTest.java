package org.breder.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class JsonOutputStreamTest {

  @Test
  public void testPrimitive() throws Exception {
    Assert.assertEquals("1", execute(1));
    Assert.assertEquals("1", execute(1l));
    Assert.assertEquals("1.2", execute(1.2f));
    Assert.assertEquals("1.23", execute(1.23d));
    Assert.assertEquals("true", execute(true));
    Assert.assertEquals("false", execute(false));
    Assert.assertEquals("null", execute(null));
    Assert.assertEquals("\"a\"", execute("a"));
  }

  @Test
  public void testArrayList() throws Exception {
    List<Integer> list = new ArrayList<Integer>();
    list.add(1);
    list.add(2);
    list.add(3);
    Assert.assertEquals("[1,2,3]", execute(list));
  }

  @Test
  public void testCollection() throws Exception {
    List<Integer> list = new LinkedList<Integer>();
    list.add(1);
    list.add(2);
    list.add(3);
    Assert.assertEquals("[1,2,3]", execute(list));
  }

  @Test
  public void testObject() throws Exception {
    Teste t = new Teste();
    t.a = 1;
    t.b = 2;
    t.c = new Teste();
    t.c.a = 3;
    t.c.b = 4;
    Assert
      .assertEquals(
        "{\"class\":\"Teste\",\"a\":1,\"b\":2,\"c\":{\"class\":\"Teste\",\"a\":3,\"b\":4,\"c\":null,\"d\":null},\"d\":null}",
        execute(t));
  }

  @Test
  public void testClass() throws Exception {
    Teste t = new Teste();
    t.a = 1;
    t.b = 2;
    t.c = new Teste();
    t.d = new Date();
    t.c.a = 3;
    t.c.b = 4;
    String jsonString = execute(t);
    JsonInputStream in =
      new JsonInputStream(
        new ByteArrayInputStream(jsonString.getBytes("utf-8")));
    HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
    map.put("Teste", Teste.class);
    in.setClasses(map);
    Teste readT = (Teste) in.readObject();
    System.out.println(readT.d);
    System.out.println(t.d);
    Assert.assertEquals(t, readT);
    in =
      new JsonInputStream(
        new ByteArrayInputStream(jsonString.getBytes("utf-8")));
    in.setClasses(map);
    Assert.assertEquals(t, readT);
  }

  protected String execute(Object value) throws EOFException, IOException,
    JsonInputStream.SyntaxException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    new JsonOutputStream(bytes).writeObject(value);
    return new String(bytes.toByteArray(), "utf-8");
  }

  protected Map<Object, Object> map(Object[][] entrys) {
    Map<Object, Object> map = new HashMap<Object, Object>();
    for (Object[] entry : entrys) {
      map.put(entry[0], entry[1]);
    }
    return map;
  }

  public static class Teste {
    private int a;
    protected int b;
    public Teste c;
    protected Date d;

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + a;
      result = prime * result + b;
      result = prime * result + ((c == null) ? 0 : c.hashCode());
      result = prime * result + ((d == null) ? 0 : d.hashCode());
      return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      Teste other = (Teste) obj;
      if (a != other.a) {
        return false;
      }
      if (b != other.b) {
        return false;
      }
      if (c == null) {
        if (other.c != null) {
          return false;
        }
      }
      else if (!c.equals(other.c)) {
        return false;
      }
      if (d == null) {
        if (other.d != null) {
          return false;
        }
      }
      else if (!d.equals(other.d)) {
        return false;
      }
      return true;
    }

  }

}
