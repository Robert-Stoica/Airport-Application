package org.comp2211.media;

import java.util.ArrayList;
import java.util.List;
import org.comp2211.calculations.Obstruction;
import org.comp2211.calculations.Runway;

/**
 * Class to hold data when importing it from or exporting it to XML.
 *
 * <p>All members are public, so it acts like a struct. Simply access using <code>xmldata.runways
 * </code> to get data.
 *
 * @author Molive-0
 * @see Media
 */
public class XMLData {
  /** The List of runways to be exported or just imported. */
  public List<Runway> runways;
  /** The List of obstructions to be exported or just imported. */
  public List<Obstruction> obstructions;

  /** Makes an empty XMLData class, with empty lists. */
  public XMLData() {
    this.runways = new ArrayList<>();
    this.obstructions = new ArrayList<>();
  }

  /**
   * Instantiates an XMLData class with predefined lists.
   *
   * @param runways The List of runways to contain within the XMLData.
   * @param obstructions The List of obstructions to contain within the XMLData.
   */
  public XMLData(List<Runway> runways, List<Obstruction> obstructions) {
    this.runways = runways;
    this.obstructions = obstructions;
  }
}
