plugins {
  id("kotlin")
  id("application")

  id("dev.teogor.querent.plugin")
}

querent {

}

application {
  mainClass.set("MainKt")
}

