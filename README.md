Clairvoyance
============

Clairvoyance is an extension to Specs2, a Scala test library. Clairvoyance provides a few extensions to capture what is happening in your tests, and then produce business and tester friendly documentation.

It's a pastiche of [Yatspec](http://code.google.com/p/yatspec), a Java testing library written by my colleague Dan Bodart. It addresses the deficiencies we experienced with Fit and Concordion.

Example
-------

The full source to this example is [here](https://github.com/rhyskeepence/clairvoyance/blob/master/src/test/scala/clairvoyance/examples/LoggingExample.scala).

It breaks down like this:

* We create a Spec which extends `ClairvoyantSpec`
* We write our spec in the mutable spec style (my team is scared of the immutable style)
* We create a context which extends `ClairvoyantContext`
* `InterestingGivens` can be added with statements such as `interestingGivens += ("Current date" -> "21/12/2012")`
* The spec method is interpreted into a text specification

```scala
class LoggingExample extends ClairvoyantSpec {
  "The co-ordinator" should {
    "invoke the Doomsday Device on the 21st of December 2012" in new context {
      givenTheDateIs("21/12/2012")
      whenTheCoordinatorRuns
      theDoomsdayDevice should beUnleashed
    }
  }

  trait context extends ClairvoyantContext {
    // test set up and fixtures
  }
}
```

Here is the output of this spec.
![Example output](http://github.com/rhyskeepence/clairvoyance/raw/master/doc/example-output.jpg)

Get This Party Started
----------------------

Add this to your SBT build:

    libraryDependencies ++= Seq(
        "rhyskeepence" %% "clairvoyance" % "1"
    )
    resolvers ++= Seq(
        "rhys's releases" at "https://github.com/rhyskeepence/mvn-repo/raw/master/releases"
    )

Within IntelliJ, run the test as a JUnit test (rather than a specs2 test)

At the moment, you can't run the spec using the specs2 runner in SBT and get the HTML output. I think it's just a matter of specifying the exporter class as an argument. This will be tested and documented very soon now!

Interesting Givens
------------------

These are inputs into your test, which may not be specified in the spec, but should be logged to the output:
```scala
  interestingGivens += ("Current date" -> "21/12/2012")
```

Captured Inputs And Outputs
---------------------------

These are the inputs or outputs to your system, which may not be practical to assert upon, but should be logged.

Perhaps you are using a stub rather than a communicating with a third party:
```scala
class StubGizmometer extends Gizmometer {
}
```

To capture inputs and outputs, just add the `ProducesCapturedInputsAndOutputs` trait and call `captureValue`:
```scala
class StubGizmometer extends Gizmometer with ProducesCapturedInputsAndOutputs {
  def scan(brain: Brain) {
    captureValue(("Brain" -> brain))
  }
}
```

and in your context, register the stub so that clairvoyant knows about it:
```
trait context extends ClairvoyantContext {
    val gizmometer = new StubGizmometer
    override def capturedInputsAndOutputs = Seq(gizmometer)
}
```


TODO
----

* Make it work with SBT rather than requiring JUnit
* Table of contents
* Notes
* Custom Rendering
* SVG sequence diagrams
* Scenerio tables
* Test output is stored as a mutable queue on the spec class, this is a bit dodgy.
