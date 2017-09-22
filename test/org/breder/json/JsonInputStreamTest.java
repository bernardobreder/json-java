package org.breder.json;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class JsonInputStreamTest {

  @Test(expected = EOFException.class)
  public void testEmpty() throws Exception {
    Assert.assertEquals(null, execute(""));
  }

  @Test
  public void testPrimitive() throws Exception {
    Assert.assertEquals(Integer.valueOf(1), execute("1"));
    Assert.assertEquals(Integer.valueOf(-1), execute("-1"));
    Assert.assertEquals(Integer.valueOf(12), execute("12"));
    Assert.assertEquals(Integer.valueOf(123), execute("123"));
    Assert.assertTrue((Boolean) execute("true"));
    Assert.assertFalse((Boolean) execute("false"));
    Assert.assertNull(execute("null"));
    Assert.assertEquals("a", execute("\"a\""));
    Assert.assertEquals("\r\n\t\f/\\", execute("\"\\r\\n\\t\\f\\/\\\\\""));

  }

  @Test
  public void testArray() throws Exception {
    Assert.assertEquals(new ArrayList<Object>(), execute("[]"));
    Assert.assertEquals(new ArrayList<Object>(Arrays.asList("a")),
      execute("[\"a\"]"));
    Assert
      .assertEquals(new ArrayList<Object>(Arrays.asList(1)), execute("[1]"));
    Assert.assertEquals(new ArrayList<Object>(Arrays.asList(true, false)),
      execute("[true,false]"));
    Assert.assertEquals(new ArrayList<Object>(Arrays.asList(null, null)),
      execute("[null,null]"));

  }

  @Test
  public void testMap() throws Exception {
    Assert.assertEquals(map(new Object[][] {}), execute("{}"));
    Assert.assertEquals(map(new Object[][] { { "a", 1 } }),
      execute("{\"a\":1}"));
    Assert.assertEquals(map(new Object[][] { { "a", 1 }, { "b", 2 } }),
      execute("{\"a\":1,\"b\":2}"));
    Assert.assertEquals(
      map(new Object[][] { { "a", 1 }, { "b", 2 }, { "c", 3 } }),
      execute("{\"a\":1,\"b\":2 , \"c\" : 3 }"));
    Assert.assertEquals(map(new Object[][] { { "a", 1 }, { "b", 2 },
        { "c", 3 }, { "d", 4 } }),
      execute("{\"a\":1,\"b\":2 , \"c\" : 3 , \t \"d\"  :\n\r\t4 }"));
    Assert.assertEquals(map(new Object[][] { { "a", "b" } }),
      execute("{\"a\":\"b\"}"));
    Assert.assertEquals(map(new Object[][] { { "a",
        map(new Object[][] { { "b", "c" } }) } }),
      execute("{\"a\":{\"b\":\"c\"}}"));

  }

  protected Object execute(String code) throws EOFException, IOException,
    JsonInputStream.SyntaxException {
    return new JsonInputStream(new ByteArrayInputStream(code.getBytes("utf-8")))
      .readObject();
  }

  protected Map<Object, Object> map(Object[][] entrys) {
    Map<Object, Object> map = new HashMap<Object, Object>();
    for (Object[] entry : entrys) {
      map.put(entry[0], entry[1]);
    }
    return map;
  }

}
