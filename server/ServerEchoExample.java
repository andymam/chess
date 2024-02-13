public class ServerEchoExample {
  public static void main(String[] args) {
    new ServerEchoExample().run();
  }

  private void run() {
    Spark.port(8080);
    Spark.post("/echo", this::echoBody);
  }

  private Object echoBody(Request req, Response res) {
    var bodyObj = getBody(req, Map.class);

    res.type("application/json");
    return new Gson().toJson(bodyObj);
  }

  private static <T> T getBody(Request request, Class<T> clazz) {
    var body = new Gson().fromJson(request.body(), clazz);
    if (body == null) {
      throw new RuntimeException("missing required body");
    }
    return body;
  }


  public Object errorHandler(Exception e, Request req, Response res) {
    var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
    res.type("application/json");
    res.status(500);
    res.body(body);
    return body;
  }
}