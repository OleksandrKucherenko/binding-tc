package com.artfulbits.junit;

import android.content.Context;
import android.support.annotation.NonNull;

import com.artfulbits.benchmark.Meter;

import org.junit.*;
import org.junit.rules.*;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;

/** base class for all our jUnit tests. Provides basic helpers that simplify our tests execution. */
@SuppressWarnings("unused")
public abstract class TestHolder implements Meter.Output {
  /* [ CONSTANTS ] ================================================================================================= */

  /** Size of hash calculation buffer. Default: 4Kb. */
  private final static int BUFFER_SIZE = 4 * 1024;
  /** Default size of read/write operation buffer. Default: 16Kb. */
  private final static int BUFFER_READ_WRITE_SIZE = 4 * BUFFER_SIZE;

  /* [ INJECTIONS ] ================================================================================================ */

  @Rule
  public TestName mTestName = new TestName();

  /* [ MEMBERS ] =================================================================================================== */

  /** Standard Output Logger. Helps to save some useful results of tests as a part of execution. */
  private final StringBuilder mLog = new StringBuilder(64 * 1024).append("\r\n");
  /** Mocked context. */
  private Context mContext;

	/* [ IMPLEMENTATION & HELPERS ] ================================================================================== */

  /** Get fake context. */
  public Context getContext() {
    return mContext;
  }

  //region --> Standard Output

  /** Get instance of the micro-benchmarking tool. */
  protected Meter getMeter() {
    final Meter m = Meter.getInstance();

//    m.getConfig().ShowTopNLongest = 2;
    m.getConfig().UseSystemNanos = false;
    m.getConfig().ShowAccumulatedTime = true;
    m.setOutput(this);

    return m;
  }

  /** Get access to the logs memory storage directly. */
  @NonNull
  protected StringBuilder getRawLogger() {
    return mLog;
  }

  /** {@inheritDoc} */
  public void log(final Level level, final String tag, final String msg) {
    mLog.append(level.toString().charAt(0)).append(" : ")
        .append(tag).append(" : ")
        .append(msg).append("\r\n");
  }

  /** Put message into system output. */
  public void trace(final String msg) {
    log(Level.INFO, "--", msg);
  }
  //endregion

  //region --> Setup/TearDown Log method name
  @Before
  public final void setUp() throws Exception {
    log(Level.INFO, "->", mTestName.getMethodName());

    // create fake context
    mContext = Mockito.mock(Context.class);

    onSetUp();
  }

  @After
  public final void tearDown() throws Exception {
    onTearDown();

    log(Level.INFO, "<-", mTestName.getMethodName());

    // all collected output lines dump into Standard Output
    System.out.append(mLog.toString());
  }
  //endregion

  //region --> Setup/TearDown Overrides

  public void onSetUp() {
    // do nothing, leave for inheritors
  }

  public void onTearDown() {
    // do nothing, leave for inheritors
  }

  //endregion

  //region --> I/O operations

  /**
   * Extract from input stream content into string.
   *
   * @param is input stream instance.
   * @return extracted string.
   */
  public static String toString(final InputStream is) {
    final StringWriter writer = new StringWriter(BUFFER_READ_WRITE_SIZE);
    final InputStreamReader isr = new InputStreamReader(is);

    try {
      copy(isr, writer);
    } catch (final IOException ignored) {
    }

    destroy(isr);
    destroy(writer);

    return writer.toString();
  }

  /**
   * Copy all from reader to writer.
   *
   * @param in instance of reader.
   * @param out instance of writer.
   * @throws IOException read/write operation failure.
   */
  public static void copy(final Reader in, final Writer out) throws IOException {
    char[] buffer = new char[BUFFER_READ_WRITE_SIZE];
    int n = 0;

    while (-1 != (n = in.read(buffer))) {
      out.write(buffer, 0, n);
    }
  }

  /**
   * Copy input stream to output. Method close streams after copy.
   *
   * @param in instance of input stream.
   * @param out instance of output stream.
   * @throws IOException read/write operation failure.
   */
  public static void copy(final InputStream in, final OutputStream out) throws IOException {
    // Transfer bytes from in to out
    final byte[] buf = new byte[BUFFER_READ_WRITE_SIZE];
    int len;

    while ((len = in.read(buf)) > 0) {
      out.write(buf, 0, len);
    }

    destroy(in);
    destroy(out);
  }

  /**
   * Close input stream gracefully.
   *
   * @param is instance of the input stream.
   */
  public static void destroy(final InputStream is) {
    if (null != is) {
      try {
        is.close();
      } catch (final IOException ignored) {
      }
    }
  }

  /**
   * Close output stream gracefully.
   *
   * @param os instance of the output stream.
   */
  public static void destroy(final OutputStream os) {
    if (null != os) {
      try {
        os.flush();
        os.close();
      } catch (final IOException ignored) {
      }
    }
  }

  /**
   * Destroy reader instance.
   *
   * @param reader instance of the reader.
   */
  public static void destroy(final Reader reader) {
    if (null != reader) {
      try {
        reader.close();
      } catch (final IOException ignored) {
      }
    }
  }

  /**
   * Destroy writer instance.
   *
   * @param writer reference on instance.
   */
  public static void destroy(final Writer writer) {
    if (null != writer) {
      try {
        writer.flush();
        writer.close();
      } catch (final IOException ignored) {
      }
    }
  }
  //endregion
}
