package com.clinical.tongxin.util;

import java.io.File;

import android.content.Context;

public class FileUtil {

	private static File result=null;
	public static void CreateDir(File paramFile) {
		if (new File(paramFile.getParent()).exists()) {
			paramFile.mkdirs();
		} else {
			CreateDir(new File(paramFile.getParent()));
			paramFile.mkdirs();
		}
	}

	public static File getApkStorageFile(Context paramContext) {
		return new File(paramContext.getFilesDir() + "/cache");
	}
	public static File getBroadcastFile(Context paramContext) {
		return new File(paramContext.getFilesDir() + "/cache/broadcast");
	}
	public static File getLocalFile(File paramFile, String paramString) {
		try
		{
			File[] arrayOfFile = paramFile.listFiles();
			if(arrayOfFile.length>0)
			{
				for (File file : arrayOfFile)
				{
					if (file.isDirectory())
					{
						// 如果目录可读就执行（一定要加，不然会挂掉）
						if (file.canRead()) {
							getLocalFile(file,paramString); // 如果是目录，递归查找
						}
					}
					else
					{
						String str=file.getName();
						//boolean b=str.contains(paramString);
						boolean b=str.equals(paramString);
						if(b)
						{
							result=file;
							return result;
						}
					}
				}

			}
		}
		catch(Exception ex)
		{

		}
		return result;
	}
}
