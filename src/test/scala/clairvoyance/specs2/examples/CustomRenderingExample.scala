package clairvoyance.specs2.examples

import clairvoyance.rendering.CustomRendering
import clairvoyance.specs2.{ClairvoyantSpec, ClairvoyantContext}

class CustomRenderingExample extends ClairvoyantSpec with CustomRendering {
  "The Custom Renderer" should {
    "be invoked" in new context {
      interestingGivens += ("brain" -> Brain(130))
    }
  }

  trait context extends ClairvoyantContext

  def customRendering = {
    case Brain(iq) => s"a Brain with an IQ of $iq"
  }
}

case class Brain(iq: Int)
