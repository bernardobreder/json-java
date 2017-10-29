package org.breder.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonWriterTest {

  @Test
  public void test() {
    Assert.assertEquals(
      "{\n\t\"obj\": {\n\t\t\"ect\": {\n\t\t\t\"id\": 1\n\t\t}\n\t}\n}",
      new JsonWriter(new JsonObject().put("obj", new JsonObject().put("ect",
        new JsonObject().put("id", 1)))).toString());
  }

  @Test
  public void testMapEmpty() {
    Assert.assertEquals("{\n\t\"obj\": {}\n}", new JsonWriter(new JsonObject()
      .put("obj", new JsonObject())).toString());
  }

  @Test
  public void testMapTwoElement() {
    Assert.assertEquals("{\n\t\"a\": 1,\n\t\"b\": 2\n}", new JsonWriter(
      new JsonObject().put("a", 1).put("b", 2)).toString());
  }

}
