package org.breder.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonObjectTest {

  @Test
  public void test() throws Exception {
    Assert.assertEquals("{\"id\": 1, \"name\": \"test\"}", json(new JsonObject()
      .put("id", 1).put("name", "test")));
    Assert.assertEquals("{\"id\": 1, \"name\": \"test\"}", json(new JsonObject(
      json(new JsonObject().put("id", 1).put("name", "test")))));

    Assert.assertEquals(
      "{\"id\": 1, \"name\": {\"id\": 2, \"name\": \"test\"}}", json(
        new JsonObject().put("id", 1).put("name", new JsonObject().put("id", 2)
          .put("name", "test"))));
    Assert.assertEquals(
      "{\"id\": 1, \"name\": {\"id\": 2, \"name\": \"test\"}}", json(
        new JsonObject(json(new JsonObject().put("id", 1).put("name",
          new JsonObject().put("id", 2).put("name", "test"))))));
  }

  private String json(JsonObject put) {
    return put.toString();
  }

}
