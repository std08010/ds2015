package com.ds.di;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class Log4jConsoleTest
{
	static final Logger	log	= Logger.getLogger(Log4jConsoleTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Log4jConsoleTest console = new Log4jConsoleTest();
		console.execute();
	}

	public Log4jConsoleTest()
	{
	}

	public void execute()
	{

		if (log.isTraceEnabled())
		{
			log.trace("Test: TRACE level message.");
		}
		if (log.isDebugEnabled())
		{
			log.debug("Test: DEBUG level message.");
		}
		if (log.isInfoEnabled())
		{
			log.info("Test: INFO level message.");
		}
		if (log.isEnabledFor(Level.WARN))
		{
			log.warn("Test: WARN level message.");
		}
		if (log.isEnabledFor(Level.ERROR))
		{
			log.error("Test: ERROR level message.");
		}
		if (log.isEnabledFor(Level.FATAL))
		{
			log.fatal("Test: FATAL level message.");
		}
	}

}