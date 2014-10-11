package fr.insa_lyon.citi.robotomy.tools.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.logging.Level;

import android.os.Environment;


/**
 * This class take in charge of all the I/O procedures to write a log file onto
 * the sdcard.
 */
public class FileLogger {
	private long maxFileLengthKB = 2000;
	protected Formatter formatter;
	protected boolean logOpen;
	private String fileName;
	private PrintWriter writer;
	private boolean append;

	private static final String LOG_FILENAME = "pyp-android.log";

	/**
	 * if set to true, a new file will be created everytime the file reaches the
	 * maxFileLengthKB size
	 */
	private boolean infNumberOfFiles = false;

	public FileLogger() {
		fileName = LOG_FILENAME;
		append = true;
	}

	/**
	 * Open a the file to write. You should set a file name before calling this
	 * method.
	 * 
	 * @throws IOException
	 */
	public void open() throws IOException {
		File logFile = null;
		FileOutputStream fileOutputStream = null;
		logFile = getWrittableFile();

		fileOutputStream = new FileOutputStream(logFile, append);
		if (fileOutputStream != null) {
			writer = new PrintWriter(fileOutputStream);
		}
		logOpen = true;
	}

	/**
	 * returns a file that we can write on according to the settings
	 * (maxFileLengthKB; maxNumberOfFiles ;fileName). </br> Returns null if none
	 * is avaliable
	 * 
	 * @return
	 * @throws IOException
	 */
	private File getWrittableFile() throws IOException {
		File logFile;
		int i = 0;
		do {
			fileName += i++;
			logFile = getSDCardFile();
			if (!logFile.exists()) {
				logFile.createNewFile();
			}

		} while ((logFile.length() / 1024) > maxFileLengthKB
				&& infNumberOfFiles);

		if (!infNumberOfFiles && (logFile.length() / 1024) > maxFileLengthKB) {
			// means that we want to use only one log file and it reaches to
			// it's max size.
			// we will then clear it.
			clear();
			logFile.delete();
			logFile.createNewFile();
		}
		return logFile;
	}

	/**
	 * Clear the file.
	 * 
	 * @throws IOException
	 */
	public void clear() throws IOException {
		// throw new UnsupportedOperationException();
		boolean saveAppend = append;
		append = false;
		open();
		writer.print("\n");
		writer.flush();
		close();
		append = saveAppend;

	}

	/**
	 * Close a the file.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (writer != null) {
			writer.close();
		}
	}

	/**
	 * Write to log
	 * 
	 * @param clientID
	 * @param name
	 * @param time
	 * @param level
	 * @param message
	 * @param throwable
	 *            - if set, write an output the debug tools "logcat". Can be
	 *            null.
	 */
	public void doLog(String tag, Level level, String message) {
		if (logOpen && formatter != null && writer != null) {
			writer.print(formatter.format(tag,
					Calendar.getInstance().getTime(), level, message));
			writer.print("\n");
			writer.flush();

		}
	}

	public long getLogSize() {
		return -1L;
	}

	public void setFileName(String fileName) {
		if (fileName != null) {
			this.fileName = fileName;
		}
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	private File getSDCardFile() throws IOException {
		String externalStorageState = Environment.getExternalStorageState();
		File externalStorageDirectory = Environment
				.getExternalStorageDirectory();
		File file = null;
		if (externalStorageState.equals("mounted")
				&& externalStorageDirectory != null) {
			file = new File(externalStorageDirectory, fileName);
		}
		if (file == null) {
			throw new IOException("no sd card");
		}
		return file;
	}

	public final void setFormatter(Formatter formatter)
			throws IllegalArgumentException {
		if (formatter == null) {
			throw new IllegalArgumentException(
					"The formatter must not be null.");
		} else {
			this.formatter = formatter;
			return;
		}
	}

	/**
	 * set the maximum size of the log file
	 * 
	 * @param maxFileLengthKB
	 *            the maxFileLengthKB to set
	 */
	@SuppressWarnings("unused")
	private void setMaxFileLengthKB(long maxFileLengthKB) {
		this.maxFileLengthKB = maxFileLengthKB;
	}

	/**
	 * the maximum size of the log file
	 * 
	 * @return the maxFileLengthKB
	 */
	@SuppressWarnings("unused")
	private long getMaxFileLengthKB() {
		return maxFileLengthKB;
	}

	/**
	 * If set to true, a new file will be created everytime the file reaches the
	 * maxFileLengthKB size
	 * 
	 * @param infNumberOfFiles
	 *            the infNumberOfFiles to set
	 */
	public void setInfNumberOfFiles(boolean infNumberOfFiles) {
		this.infNumberOfFiles = infNumberOfFiles;
	}

	/**
	 * If set to true, a new file will be created everytime the file reaches the
	 * maxFileLengthKB size
	 * 
	 * @return the infNumberOfFiles
	 */
	public boolean isInfNumberOfFiles() {
		return infNumberOfFiles;
	}
	
}
