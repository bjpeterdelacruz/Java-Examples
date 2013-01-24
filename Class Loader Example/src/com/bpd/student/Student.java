package com.bpd.student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.ObjectStreamField;
import java.io.Serializable;

/**
 * Represents a college student.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 * @version 2.0
 */
public class Student implements Serializable {

  /**
   * This <code>serialVersionUID</code> was generated for version 1.0 of this class. Use this value
   * in future versions of this class to maintain backwards compatibility.
   */
  private static final long serialVersionUID = 2343791679533368945L;

  static final int SERIALIZATION_VERSION_NUMBER = 2;

  /*
   * The following three fields are found in version 1.0 of this class.
   */
  private static final String CLASS_STANDING = "classStanding";
  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";

  /*
   * These fields are used to maintain backwards compatibility with previous versions of this class.
   * 
   * {@link ClassStanding#GRADUATE} and {@link ClassStanding#UNCLASSIFIED} are not found in version
   * 1.0 of {@link ClassStanding}.
   */
  private static final String IS_GRADUATE_STUDENT = "isGraduateStudent";
  private static final String IS_UNCLASSIFIED_STUDENT = "isUnclassifiedStudent";

  /* Add new member variables here. */
  private ClassStanding classStanding;
  private String name;

  private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[] {
      new ObjectStreamField(CLASS_STANDING, ClassStanding.class),
      new ObjectStreamField(FIRST_NAME, String.class),
      new ObjectStreamField(LAST_NAME, String.class),
      new ObjectStreamField(IS_GRADUATE_STUDENT, Boolean.TYPE),
      new ObjectStreamField(IS_UNCLASSIFIED_STUDENT, Boolean.TYPE) };

  /**
   * Constructs a new Student.
   * 
   * @param name The name of this student.
   * @param standing The class standing for this student.
   */
  public Student(String name, ClassStanding standing) {
    this.name = name;
    this.classStanding = standing;
  }

  /** @return The name of this student. */
  public String getName() {
    return name;
  }

  /** @return The class standing for this student. */
  public ClassStanding getClassStanding() {
    return classStanding;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "Student=[name=" + name + ", classStanding=" + classStanding + "]";
  }

  /**
   * Serializes this object using a custom format.
   * 
   * @param oos The output stream to which to write this object.
   * @throws IOException If there are problems writing this object to the stream.
   */
  private void writeObject(ObjectOutputStream oos) throws IOException {
    PutField fields = oos.putFields();

    if (classStanding == ClassStanding.GRADUATE) {
      fields.put(CLASS_STANDING, ClassStanding.FRESHMAN);
      fields.put(IS_GRADUATE_STUDENT, true);
    }
    else if (classStanding == ClassStanding.UNCLASSIFIED) {
      fields.put(CLASS_STANDING, ClassStanding.FRESHMAN);
      fields.put(IS_UNCLASSIFIED_STUDENT, true);
    }
    else {
      fields.put(CLASS_STANDING, classStanding);
    }

    fields.put(FIRST_NAME, name);
    fields.put(LAST_NAME, "");

    oos.writeFields();
  }

  /**
   * Deserializes a Student object from the given input stream.
   * 
   * @param ois The input stream from which to deserialize a Student object.
   * @throws IOException If there are problems reading in from the input stream.
   * @throws ClassNotFoundException If a class cannot be found.
   */
  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    GetField fields = ois.readFields();

    boolean isGraduateStudent = fields.get(IS_GRADUATE_STUDENT, false);
    boolean isUnclassifiedStudent = fields.get(IS_UNCLASSIFIED_STUDENT, false);
    if (isGraduateStudent) {
      classStanding = ClassStanding.GRADUATE;
    }
    else if (isUnclassifiedStudent) {
      classStanding = ClassStanding.UNCLASSIFIED;
    }
    else {
      classStanding = (ClassStanding) fields.get(CLASS_STANDING, ClassStanding.FRESHMAN);
    }

    name = fields.get(FIRST_NAME, "").toString();
    String lastName = fields.get(LAST_NAME, "").toString();
    if (!lastName.isEmpty()) {
      name += " " + lastName;
    }
  }

}
