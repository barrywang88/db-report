import ammonite.main.Router.{doc, main}

@main
def main(x: Int@doc("this is x"), y:Int@doc("this is y")) = {
  val result = x*y
  println(result)
  Map("key" -> result)
}