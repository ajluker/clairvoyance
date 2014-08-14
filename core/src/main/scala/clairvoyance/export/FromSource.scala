package clairvoyance.export

import clairvoyance.export.NaiveMultilineMethodNameJoiner.join
import clairvoyance.io.Files.{currentWorkingDirectory, listFiles}
import java.io.File
import scala.annotation.tailrec
import scala.util.Properties.isWin

object FromSource {
  val fileSeparator = if (isWin) "\\\\" else "/"

  /**
   * @param location   a string containing class name, file name, and line number
   * @param lineNumber the line number related to the location
   * @return
   */
  def getCodeFrom(location: String, lineNumber: Int): List[(Int, String)] = {
    val sourceFilePath = location.split(" ")(0).replaceAll("\\.", fileSeparator) + ".scala"
    val sourceFile = listFiles(currentWorkingDirectory).find(_.getPath.endsWith(sourceFilePath))
    val content = readLines(sourceFile).getOrElse(Seq.empty)
    readToEndOfMethod(content, lineNumber)
  }

  def getCodeFrom(location: String, testName: String): List[(Int, String)] = {
    val sourceFilePath = location.split(" ")(0).replaceAll("\\.", fileSeparator) + ".scala"
    val sourceFile = listFiles(currentWorkingDirectory).find(_.getPath.endsWith(sourceFilePath))
    val content = readLines(sourceFile).getOrElse(Seq.empty)
    val zippedContent: Seq[(String, Int)] = content.zipWithIndex
    val joined = join(zippedContent)
    val (line, lineNumber) = joined.find(_._1.replace("\"", "").contains(testName)).get
    readToEndOfMethod(content, if (line.trim().matches(".+\\{.+\\}|.+ =\\s+[^\\{]+")) lineNumber else lineNumber + 1)
  }

  @tailrec
  private def readToEndOfMethod(content: Seq[String], lineNumber: Int, indentLevel: Int = 0, res: List[(Int, String)] = List()): List[(Int, String)] = {
    if (content.size < lineNumber || lineNumber < 1) {
      res.reverse

    } else if (content(lineNumber).trim().endsWith("{") || content(lineNumber).trim().endsWith("=")) {
      readToEndOfMethod(content, lineNumber + 1, indentLevel + 1, addLine(lineNumber, content, res))

    } else if (content(lineNumber).trim().matches(".+\\{.+\\}")) {
      val line = content(lineNumber).trim()
      (lineNumber, line.substring(line.indexOf("{") + 1, line.lastIndexOf("}")).trim) :: res.reverse

    } else if (content(lineNumber).trim().matches(".+ =\\s+[^\\{]+")) {
      val line = content(lineNumber).trim()
      (lineNumber, line.substring(line.indexOf("=") + 1).trim) :: res.reverse

    } else if (content(lineNumber).trim().startsWith("}") && indentLevel == 0) {
      res.reverse

    } else if (content(lineNumber).trim().startsWith("}")) {
      readToEndOfMethod(content, lineNumber + 1, indentLevel - 1, addLine(lineNumber, content, res))

    } else if (content(lineNumber).trim().endsWith("}")) {
      res.reverse

    } else {
      readToEndOfMethod(content, lineNumber + 1, indentLevel, addLine(lineNumber, content, res))
    }
  }

  private def addLine(lineNumber: Int, content: Seq[String], lines: List[(Int, String)]): List[(Int, String)] =
    (lineNumber + 1, content(lineNumber).trim()) :: lines

  private def readLines(path: Option[File]) =
    path.map(scala.io.Source.fromFile(_).getLines().toIndexedSeq)
}
