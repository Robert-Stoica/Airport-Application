package org.comp2211.media;

import org.comp2211.Calculations.Obstruction;
import org.comp2211.Calculations.Runway;

import javax.xml.stream.*;
import java.io.*;

/**
 * Reads and writes obstructions and runways from XML files.
 *
 * <p/> Media uses the StAX api to manipulate the files, as files only need to be read from
 * beginning to end and StAX is efficient at this.
 *
 * @author molive
 * @see XMLData
 */
public class Media {
  /**
   * The global filename to be used for {@link #importXML()} and {@link #exportXML(XMLData)}.
   */
  private static File filename;

  /**
   * Gets the global filename {@link #filename}.
   *
   * @return Returns the filename as a String.
   */
  public static File getFilename() {
    return filename;
  }

  /**
   * Sets the global filename {@link #filename}.
   *
   * @param filename The String to assign to the filename. Should be a full path.
   */
  public static void setFilename(File filename) {
    Media.filename = filename;
  }

  private static String getString(XMLStreamReader xml, String name) throws XMLStreamException {
    var text = "";
    var next = 0;
    while (xml.hasNext()) {
      next = xml.next();
      if (next == XMLStreamConstants.CHARACTERS) {
        text =
            xml.getText().replace("&3", ">")
                .replace("&2", "<")
                .replace("&1", "&");
      }
      if (next == XMLStreamConstants.END_ELEMENT
          && xml.getLocalName().equals(name)) {
        break;
      }
    }
    do {
      if (next == XMLStreamConstants.END_ELEMENT
          && xml.getLocalName().equals(name)) {
        break;
      }
      next = xml.nextTag();
    } while (xml.hasNext());
    return text;
  }

  private static int getInt(XMLStreamReader xml, String name) throws XMLStreamException {
    var text = 0;
    var next = 0;
    while (xml.hasNext()) {
      next = xml.next();
      if (next == XMLStreamConstants.CHARACTERS) {
        try {
          text = Integer.parseInt(xml.getText());
        } catch (NumberFormatException e) {
          throw new XMLStreamException(e);
        }
      }
      if (next == XMLStreamConstants.END_ELEMENT
          && xml.getLocalName().equals(name)) {
        break;
      }
    }
    do {
      if (next == XMLStreamConstants.END_ELEMENT
          && xml.getLocalName().equals(name)) {
        break;
      }
      next = xml.nextTag();
    } while (xml.hasNext());
    return text;
  }

  /**
   * Imports runways and obstructions from an XML file specified by the global filename variable.
   * Equivalent to <code>Media.importXML(Media.getFilename())</code>.
   *
   * @see XMLData
   * @see #importXML(File)
   * @return Returns a filled XMLData class.
   * @throws XMLStreamException Throws when any error occurs in reading.
   */
  public static XMLData importXML() throws XMLStreamException {
    return importXML(Media.getFilename());
  }

  /**
   * Imports runways and obstructions from an XML file specified by a parameter.
   *
   * <p/> Some of the worst code I've ever written.
   *
   * @see XMLData
   * @param filename The filename of the file to import from.
   * @return Returns a filled XMLData class.
   * @throws XMLStreamException Throws when any error occurs in reading.
   */
  public static XMLData importXML(File filename) throws XMLStreamException {
    Reader fileReader;
    try {
      fileReader = new FileReader(filename);
    } catch (FileNotFoundException f) {
      throw new XMLStreamException(f);
    }
    XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
    XMLStreamReader xml = xmlInputFactory.createXMLStreamReader(fileReader);

    XMLData data = new XMLData();

    while (xml.hasNext()) {
      if (xml.next() == XMLStreamConstants.START_ELEMENT) {
        switch (xml.getLocalName()) {
          case "Runways" -> {
            while (xml.hasNext()) {
              var next = xml.nextTag();
              if (next == XMLStreamConstants.START_ELEMENT) {
                if (xml.getLocalName().equals("Runway")) {
                  int tora = 0;
                  int toda = 0;
                  int asda = 0;
                  int lda = 0;
                  int dThreshold = 0;
                  int clearway = 0;
                  int stopway = 0;
                  int resa = 0;
                  int stripEnd = 0;
                  int bProtection = 0;
                  int als = 0;
                  int tocs = 0;
                  int runStrip = 0;
                  String name = "";
                  int distanceFromCl = 0;
                  int distanceFromRun = 0;
                  while (xml.hasNext()) {
                    var runwayNext = xml.nextTag();
                    if (runwayNext == XMLStreamConstants.END_ELEMENT) {
                      if (xml.getLocalName().equals("Runway")) {
                        break;
                      }
                    }
                    if (runwayNext == XMLStreamConstants.START_ELEMENT) {
                      switch (xml.getLocalName()) {
                        case "tora" -> tora = getInt(xml, "tora");
                        case "toda" -> toda = getInt(xml, "toda");
                        case "asda" -> asda = getInt(xml, "asda");
                        case "lda" -> lda = getInt(xml, "lda");
                        case "dThreshold" -> dThreshold = getInt(xml, "dThreshold");
                        case "clearway" -> clearway = getInt(xml, "clearway");
                        case "stopway" -> stopway = getInt(xml, "stopway");
                        case "resa" -> resa = getInt(xml, "resa");
                        case "stripEnd" -> stripEnd = getInt(xml, "stripEnd");
                        case "bProtection" -> bProtection = getInt(xml, "bProtection");
                        case "als" -> als = getInt(xml, "als");
                        case "tocs" -> tocs = getInt(xml, "tocs");
                        case "runStrip" -> runStrip = getInt(xml, "runStrip");
                        case "distanceFromCl" -> distanceFromCl = getInt(xml, "distanceFromCl");
                        case "distanceFromRun" -> distanceFromRun = getInt(xml, "distanceFromRun");
                        case "name" -> name = getString(xml, "name");
                        default -> { }
                      }
                    }
                  }
                  data.runways.add(new Runway(name, tora,lda,dThreshold));
                }
              }
              if (next == XMLStreamConstants.END_ELEMENT) {
                if (xml.getLocalName().equals("Runways")) {
                  break;
                }
              }
            }
          }
          case "Obstructions" -> {
            while (xml.hasNext()) {
              var next = xml.nextTag();
              if (next == XMLStreamConstants.START_ELEMENT) {
                if (xml.getLocalName().equals("Obstruction")) {
                  String name = "";
                  int distanceFromCl = 0;
                  int distanceAlongCl = 0;
                  int height = 0;
                  while (xml.hasNext()) {
                    var obstructionNext = xml.nextTag();
                    if (obstructionNext == XMLStreamConstants.END_ELEMENT) {
                      if (xml.getLocalName().equals("Obstruction")) {
                        break;
                      }
                    }
                    if (obstructionNext == XMLStreamConstants.START_ELEMENT) {
                      switch (xml.getLocalName()) {
                        case "name" -> name = getString(xml, "name");
                        case "distanceFromCl" -> distanceFromCl = getInt(xml, "distanceFromCl");
                        case "distanceAlongCl" -> distanceAlongCl = getInt(xml, "distanceAlongCl");
                        case "height" -> height = getInt(xml, "height");
                        default -> { }
                      }
                    }
                  }
                  data.obstructions.add(new Obstruction(distanceFromCl, height, distanceAlongCl));
                }
              }
              if (next == XMLStreamConstants.END_ELEMENT) {
                if (xml.getLocalName().equals("Obstructions")) {
                  break;
                }
              }
            }
          }
          default -> { }
        }
      }
    }
    xml.close();
    return data;
  }

  /**
   * Exports runways and obstructions to an XML file specified by the global filename variable.
   * Equivalent to <code>Media.exportXML(data, Media.getFilename())</code>.
   *
   * @see XMLData
   * @see #exportXML(XMLData, File)
   * @param data The data to write out to the file.
   * @throws XMLStreamException Throws when any error occurs in writing.
   */
  public static void exportXML(XMLData data) throws XMLStreamException {
    exportXML(data, Media.getFilename());
  }

  /**
   * Exports runways and obstructions to an XML file specified by a parameter.
   *
   * @see XMLData
   * @param data The data to write out to the file.
   * @param filename The filename of the file to export to.
   * @throws XMLStreamException Throws when any error occurs in writing.
   */
  public static void exportXML(XMLData data, File filename) throws XMLStreamException {
    Writer fileWriter;
    try {
      fileWriter = new FileWriter(filename);
    } catch (IOException f) {
      throw new XMLStreamException(f);
    }
    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
    XMLStreamWriter xml = xmlOutputFactory.createXMLStreamWriter(fileWriter);

    String schema = "file://media/schema.xsd";

    xml.writeStartDocument("utf-8", "1.0");
    xml.setPrefix("airport", schema);
    xml.writeStartElement("Airport");
    xml.writeNamespace("airport", schema);
    xml.writeStartElement(schema, "Runways");
    for (Runway r : data.runways) {
      xml.writeStartElement(schema, "Runway");
      xml.writeStartElement(schema, "tora");
      xml.writeCharacters(Integer.toString(r.getTora()));
      xml.writeEndElement();
      xml.writeStartElement(schema, "toda");
      xml.writeCharacters(Integer.toString(r.getToda()));
      xml.writeEndElement();
      xml.writeStartElement(schema, "asda");
      xml.writeCharacters(Integer.toString(r.getAsda()));
      xml.writeEndElement();
      xml.writeStartElement(schema, "lda");
      xml.writeCharacters(Integer.toString(r.getLda()));
      xml.writeEndElement();
      xml.writeStartElement(schema, "dThreshold");
      xml.writeCharacters(Integer.toString(r.getDisplacedThreshold()));
      xml.writeEndElement();
      xml.writeStartElement(schema, "clearway");
      xml.writeCharacters(Integer.toString(r.getClearway()));
      xml.writeEndElement();
      xml.writeStartElement(schema, "stopway");
      xml.writeCharacters(Integer.toString(r.getStopway()));
      xml.writeEndElement();
      xml.writeStartElement(schema, "resa");
      xml.writeCharacters(Integer.toString(r.getResa()));
      xml.writeEndElement();
      xml.writeStartElement(schema, "stripEnd");
      xml.writeCharacters(Integer.toString(r.getStripEnd()));
      xml.writeEndElement();
      xml.writeStartElement(schema, "bProtection");
      xml.writeCharacters(Integer.toString(r.getbProtection()));
      xml.writeEndElement();
      xml.writeEndElement();
    }
    xml.writeEndElement();
    xml.writeStartElement(schema, "Obstructions");
    for (Obstruction o : data.obstructions) {
      xml.writeStartElement(schema, "Obstruction");
      xml.writeStartElement(schema, "distanceFromCl");
      xml.writeCharacters(Integer.toString(o.getDistanceFromCl()));
      xml.writeEndElement();
      xml.writeStartElement(schema, "height");
      xml.writeCharacters(Integer.toString(o.getHeight()));
      xml.writeEndElement();
      xml.writeEndElement();
    }
    xml.writeEndElement();
    xml.writeEndElement();
    xml.writeEndDocument();
    xml.flush();
    xml.close();
    try {
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      throw new XMLStreamException(e);
    }
  }
}
