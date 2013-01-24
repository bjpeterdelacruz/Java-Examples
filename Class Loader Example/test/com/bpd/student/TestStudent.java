package com.bpd.student;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections15.Factory;
import org.junit.Test;
import com.bpd.utils.validation.Validator;

/**
 * Tests the serialization and deserialization processes for different versions of {@link Student}.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestStudent {

  private static final String SERIALIZATION_VERSION_NUMBER = "SERIALIZATION_VERSION_NUMBER";

  private static List<VersionInfo> getVersions() {
    return Arrays.asList(new VersionInfo(1, getClassLoader("StudentV1.jar"),
        getData("StudentV1.old")), new VersionInfo(Student.SERIALIZATION_VERSION_NUMBER,
        getCurrentClassLoader(), getCurrentData()));
  }

  /**
   * Returns a factory for constructing a class loader that has the given JAR resource in its list
   * of URLs.
   * 
   * @param jarResourceName The JAR resource name.
   * @return A factory for constructing a class loader that has the given JAR resource in its list
   * of URLs.
   */
  private static Factory<ClassLoader> getClassLoader(final String jarResourceName) {
    Validator.checkNull(jarResourceName);
    return new Factory<ClassLoader>() {
      @Override
      public ClassLoader create() {
        URL url = TestStudent.class.getResource(jarResourceName);
        if (url == null) {
          throw new RuntimeException("missing resource: " + jarResourceName);
        }
        // Make the URL of the JAR resource the first search path.
        final List<URL> urls = new ArrayList<>();
        urls.add(url);
        URLClassLoader cl = (URLClassLoader) TestStudent.class.getClassLoader();
        // Then get all of the other URLs from the class loader that loaded this class.
        urls.addAll(Arrays.asList(cl.getURLs()));
        return AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {

          @Override
          public URLClassLoader run() {
            return new URLClassLoader(urls.toArray(new URL[urls.size()]), null);
          }

        });
      }
    };
  }

  /**
   * Returns a factory for constructing an input stream using the given resource.
   * 
   * @param resourceName The resource that will be returned as an input stream.
   * @return A factory for constructing an input stream using the given resource.
   */
  private static Factory<InputStream> getData(final String resourceName) {
    return new Factory<InputStream>() {
      @Override
      public InputStream create() {
        InputStream in = TestStudent.class.getResourceAsStream(resourceName);
        if (in == null) {
          throw new RuntimeException("missing resource: " + resourceName);
        }
        return in;
      }
    };
  }

  /**
   * Returns a factory for constructing a class loader that loaded this class.
   * 
   * @return A factory for constructing a class loader that loaded this class.
   */
  private static Factory<ClassLoader> getCurrentClassLoader() {
    return new Factory<ClassLoader>() {
      @Override
      public ClassLoader create() {
        return TestStudent.class.getClassLoader();
      }
    };
  }

  /**
   * Returns a factory for constructing an input stream that contains objects written to it.
   * 
   * @return A factory for constructing an input stream that contains objects written to it.
   */
  private static Factory<InputStream> getCurrentData() {
    return new Factory<InputStream>() {
      @Override
      public InputStream create() {
        try {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          ObjectOutputStream oout = new ObjectOutputStream(out);
          for (ClassStanding standing : ClassStanding.values()) {
            String alphanumeric = new BigInteger(130, new SecureRandom()).toString(32);
            oout.writeObject(new Student(alphanumeric, standing));
          }
          oout.close();
          return new ByteArrayInputStream(out.toByteArray());
        }
        catch (Exception e) {
          throw new RuntimeException("", e);
        }
      }
    };
  }

  /**
   * This test will load all versions of {@link Student}, each in its own class loader, and try to
   * deserialize each version of the object, which is stored in either a file or byte array. For
   * example, after loading version 1 of this class, it will try to deserialize versions 1 and 2 of
   * the object; and after loading version 2 in a separate class loader, it will also try to
   * deserialize the same two objects.
   * 
   * @throws Exception If something bad happens during this test.
   */
  @Test
  public void loadSerializedData() throws Exception {
    List<VersionInfo> versionInfos = getVersions();
    for (VersionInfo versionInfo1 : versionInfos) {
      // Load this test class within the class loader, and then run the test.
      ClassLoader classLoader = versionInfo1.classLoaderSource.create();
      Class<?> studentClass = classLoader.loadClass(Student.class.getName());
      int version = getVersion(studentClass);
      assertEquals(version, versionInfo1.versionNumber);
      String msg = "*** Version " + version + " of " + studentClass.getName() + " ***";
      System.out.println(msg);

      Class<?> testClass = classLoader.loadClass(TestStudent.class.getName());
      Method method = testClass.getDeclaredMethod("testReadSerializedData", InputStream.class);
      method.setAccessible(true);

      for (VersionInfo versionInfo2 : versionInfos) {
        InputStream in = versionInfo2.serializedDataSource.create();
        System.out.print("Deserializing version " + versionInfo2.versionNumber);
        System.out.println(" of object...");
        method.invoke(null, in);
        System.out.println();
      }
      System.out.println();
    }
  }

  private static int getVersion(Class<?> serverStatusClass) throws Exception {
    try {
      Field versionField = serverStatusClass.getDeclaredField(SERIALIZATION_VERSION_NUMBER);
      versionField.setAccessible(true);
      return versionField.getInt(null);
    }
    catch (NoSuchFieldException nsfe) {
      System.out.println("No such field: " + nsfe.getMessage());
      return 1;
    }
  }

  /**
   * Reads in all objects from the given serialized input stream until the end has been reached.
   * 
   * @param serializedStream The input stream from which to read in an object.
   * @throws Exception If there were problems reading in an object.
   */
  public static void testReadSerializedData(InputStream serializedStream) throws Exception {
    ObjectInputStream ois = new ObjectInputStream(serializedStream);
    try {
      while (true) {
        System.out.println(ois.readObject());
      }
    }
    catch (EOFException eofe) {
    }
  }

  /**
   * For a given class, contains its version number, the factory for creating the class loader to
   * load this class, and the factory for creating the input stream from which to read objects
   * created by this version of this class.
   * 
   * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
   */
  private static class VersionInfo {

    private final int versionNumber;
    private final Factory<ClassLoader> classLoaderSource;
    private final Factory<InputStream> serializedDataSource;

    /**
     * Creates a new VersionInfo.
     * 
     * @param versionNumber The version number of a class.
     * @param classLoaderSource The factory for creating the class loader to load this class.
     * @param serializedDataSource The factory for creating the input stream from which to read in
     * objects created by this version of this class.
     */
    public VersionInfo(int versionNumber, Factory<ClassLoader> classLoaderSource,
        Factory<InputStream> serializedDataSource) {
      this.versionNumber = versionNumber;
      this.classLoaderSource = classLoaderSource;
      this.serializedDataSource = serializedDataSource;
    }

  }

}
