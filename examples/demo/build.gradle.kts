import dev.teogor.xenoglot.Country
import dev.teogor.xenoglot.Language
import dev.teogor.xenoglot.territorialize

plugins {
  id("kotlin")
  id("application")

  id("dev.teogor.querent")
}

querent {
  languagesSchemaOptions {
    unqualifiedResLocale = Language.English territorialize Country.UnitedStates
    addSupportedLanguages {
      +(Language.Romanian territorialize Country.Romania)
      +(Language.English territorialize Country.UnitedKingdom)
      +(Language.Korean territorialize Country.SouthKorea)
      +(Language.Dutch territorialize Country.Netherlands)
      +(Language.German territorialize Country.Germany)
      +(Language.Chinese territorialize Country.China)
      +Language.Japanese
      +Language.Spanish
      +Language.Hindi
      +Language.Arabic
    }
  }
}

application {
  mainClass.set("MainKt")
}

