package com.bpd.student;

/**
 * Represents a student's class standing.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 * @version 2.0
 */
public enum ClassStanding {

  /** Freshman. */
  FRESHMAN("Freshman"),
  /** Sophomore. */
  SOPHOMORE("Sophomore"),
  /** Junior. */
  JUNIOR("Junior"),
  /** Senior. */
  SENIOR("Senior"),
  /** Graduate student. */
  GRADUATE("Graduate"),
  /** Unclassified student. */
  UNCLASSIFIED("Unclassified");

  private String displayName;

  /**
   * Creates a new ClassStanding enum.
   * 
   * @param displayName The display name for this enum.
   */
  ClassStanding(String displayName) {
    this.displayName = displayName;
  }

  /** @return The display name. */
  public String getDisplayName() {
    return displayName;
  }

}
