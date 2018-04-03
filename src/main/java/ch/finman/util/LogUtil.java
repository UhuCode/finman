package ch.finman.util;
/**
 * 
 */


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JTextArea;


/**
 * @author kohler
 *
 */
public class LogUtil {
	
	public String loggerName;
	public File loggerFile;
		
	public static final String DB_NAME = "LogUtil.db";
	public static final String TABLE_NAME = "logTable";
	public static final String LOG_TIME = "logtime";
	public static final String LOG_TYPE = "logtype";
	public static final String LOG_TEXT = "logtext";
	
	public static final String TYPE_OUT = "INFO";
	public static final String TYPE_DEB	= "DEBUG";
	public static final String TYPE_ERR = "ERROR";
	
	private boolean verbose;
	private boolean logtodb;
	private boolean debug;

	private LogUtil(String loggerName) {
		this(loggerName, DB_NAME);
	}

	private LogUtil(String loggerName, String loggerFile) {
		setLoggerName(loggerName);
		setLoggerFile(loggerFile);
	}
	
	public static LogUtil getLogger(Class source) {
		return getLogger(source, true);
	}
	
	public static LogUtil getLogger(Class source, boolean verbose) {
		return getLogger(source, verbose, false);
	}
	
	public static LogUtil getLogger(Class source, boolean verbose, boolean debug) {
		return getLogger(source, verbose, debug, false);
	}
	
	public static LogUtil getLogger(Class source, boolean verbose, boolean debug, boolean logtodb) {
		LogUtil logger = new LogUtil(source.getSimpleName());
		logger.setVerbose(verbose);
		logger.setDebug(debug);
		logger.setLogToDb(logtodb);
		return logger;	
	}
	
	private void setLoggerName(String itemName) {
		loggerName = itemName;
	}

	private void setLoggerFile(String fileName) {
		loggerFile = new File(fileName);
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setLogToDb(boolean logtodb) {
		this.logtodb = logtodb;
	}

    private static String formatDate(long time) {
        return SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(new Date(time));
    }

    private static String formatDate() {
        return formatDate(System.currentTimeMillis());
    }

    private static long formatTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        return calendar.getTimeInMillis();
    }

    private static String formatRecord(String source, String record) {
    	return String.format("%1$-20s %2$-15s %3$s",formatDate(),"[" + source + "]",record);
    }

	public void sayOut(String record) {
		if (verbose) { System.out.println(formatRecord(loggerName, record)); }
		if (logtodb) { sayLog(TYPE_OUT, formatRecord(loggerName, record)); }
	}

	public void sayDeb(String record) {
		if (debug) { System.out.println(formatRecord(loggerName, record)); }
		if (debug && logtodb) { sayLog(TYPE_DEB, formatRecord(loggerName, record)); }
	}

	public void sayErr(String record) {
		if (verbose) { System.err.println(formatRecord(loggerName, record)); }
		if (logtodb) { sayLog(TYPE_ERR, formatRecord(loggerName, record)); }
	}

	private void sayLog(String type, String record) {
//		try {
//			logToDb(type, record);
//		} catch (SqlJetException e) {
//			System.err.println(formatRecord(loggerName, e.getMessage()));
//		}
	}

//	private void logToDb(String type, String record) throws SqlJetException {
//		if (loggerFile.exists()) {
//			SqlJetDb db = SqlJetDb.open(loggerFile, true);
//			try {
//				db.beginTransaction(SqlJetTransactionMode.WRITE);
//				try {
//					ISqlJetTable table = db.getTable(TABLE_NAME);
//					table.insert(null, formatTime(), type, formatRecord(loggerName, record));
//				} finally {
//					db.commit();
//				}
//			} catch (SqlJetException e) {
//				System.err.println(formatRecord(loggerName, e.getMessage()));
//			} finally {
//				db.close();
//			}
//		}
//	}
	
	
}