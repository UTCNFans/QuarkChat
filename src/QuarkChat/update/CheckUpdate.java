package QuarkChat.update;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import QuarkChat.errorhandle.LogFile;

public class CheckUpdate {
	public static final String updateURL 		= "https://raw.githubusercontent.com/UTCNFans/QuarkChat/master/version"; // update URL, from where to download updates
	public static final int currentVersion 		= 11; // current version (0.11)
	
	private static int transformINT(byte arr[])
	{
		int value = 0;
		for(int i=17; i<=18; i++)
		{
			value += arr[i] - '0'; // transform in number from ASCII
			value *= 10;
		}
		
		return value;
	}
	
	public static boolean isUpdate()
	{
		URL siteURL		   		= null;
		byte siteVersionBuf[] 	= new byte[30];
		
		try {
			siteURL = new URL(updateURL);
			LogFile.logger.log(Level.INFO, "Connected to site for updates");
		} catch (MalformedURLException error) {
			LogFile.logger.log(Level.WARNING, "MalformedURLException", error);
			return false; // could not connect to the site
		} 
		
		try {
			siteURL.openConnection().getInputStream().read(siteVersionBuf, 0, 20);
		} catch (IOException error) {
			LogFile.logger.log(Level.WARNING, "IOException", error);
		}
		
		int siteVersion = transformINT(siteVersionBuf);
		
		if(siteVersion > currentVersion)
		{
			return true; // an update is available
		}
				
		return false; // no update yet available
	}
}
