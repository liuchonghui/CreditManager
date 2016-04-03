package com.sina.engine.base.request.utils;

import java.security.MessageDigest;

public class MD5Utils {
	public static String encode(String value)
	{
		String result = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("md5");
			byte b[] = md.digest(value.getBytes());
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++)
			{
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result  = buf.toString();
			
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		return result;
	}
}
