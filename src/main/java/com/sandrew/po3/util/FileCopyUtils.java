/**********************************************************************
 * <pre> FILE : FileCopyUtils.java CLASS : FileCopyUtils AUTHOR : SuMMeR FUNCTION : TODO
 * ====================================================================== CHANGE HISTORY LOG
 * ---------------------------------------------------------------------- MOD. NO.| DATE | NAME |
 * REASON | CHANGE REQ. ----------------------------------------------------------------------
 * |2011-12-21| SuMMeR| Created | DESCRIPTION: </pre>
 ***********************************************************************/
/**
 * $Id: FileCopyUtils.java,v 0.1 2011-12-21 下午04:11:02 SuMMeR Exp $
 */

package com.sandrew.po3.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2011-12-21
 * @version    :
 */
public class FileCopyUtils
{
	public static final int BUFFER_SIZE = 4096;

	private static Logger logger = LogManager.getLogger(FileCopyUtils.class);

	/**
	 * Copy the contents of the given InputStream to the given OutputStream.
	 * Closes both streams when done.
	 * @param in the stream to copy from
	 * @param out the stream to copy to
	 * @return the number of bytes copied
	 * @throws IOException in case of I/O errors
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException
	{
		Assert.notNull(in, "No InputStream specified");
		Assert.notNull(out, "No OutputStream specified");
		try
		{
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1)
			{
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (IOException ex)
			{
				logger.warn("Could not close InputStream", ex);
			}
			try
			{
				out.close();
			}
			catch (IOException ex)
			{
				logger.warn("Could not close OutputStream", ex);
			}
		}
		/*
		Assert.notNull(in, "No InputStream specified");
		Assert.notNull(out, "No OutputStream specified");
		try
		{
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1)
			{
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
				System.out.println("byteCount ====== " + byteCount);
			}
			out.flush();
			return byteCount;
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (IOException ex)
			{
				logger.warn("Could not close InputStream", ex);
			}
			try
			{
				out.close();
			}
			catch (IOException ex)
			{
				logger.warn("Could not close OutputStream", ex);
			}
		}
		 */
	}

	/**
	 * Copy the contents of the given Reader to the given Writer.
	 * Closes both when done.
	 * @param in the Reader to copy from
	 * @param out the Writer to copy to
	 * @return the number of characters copied
	 * @throws IOException in case of I/O errors
	 */
	public static int copy(Reader in, Writer out) throws IOException
	{
		Assert.notNull(in, "No Reader specified");
		Assert.notNull(out, "No Writer specified");
		try
		{
			int byteCount = 0;
			char[] buffer = new char[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1)
			{
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (IOException ex)
			{
				logger.warn("Could not close Reader", ex);
			}
			try
			{
				out.close();
			}
			catch (IOException ex)
			{
				logger.warn("Could not close Writer", ex);
			}
		}
	}
}
