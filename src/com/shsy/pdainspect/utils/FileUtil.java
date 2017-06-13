package com.shsy.pdainspect.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Base64;

public class FileUtil {


	public static String encodeBase64File(String path){
		File file = new File(path);
		byte[] buffer = null;
		try {
			FileInputStream inputFile = new FileInputStream(file);
			buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Base64.encodeToString(buffer, Base64.DEFAULT);
	}

	public static void decoderBase64File(String base64Code, String savePath) {
		byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
		try {
			FileOutputStream out = new FileOutputStream(savePath);
			out.write(buffer);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
