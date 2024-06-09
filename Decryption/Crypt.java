package spookyobjects;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Crypt {

	public static void decrypt(InputStream input, OutputStream output) throws IOException {
		byte key = (byte) input.read();
		int nextByte;
		while ((nextByte = input.read()) != -1) {
			output.write(nextByte ^ key);
		}
	}

	public static void encrypt(byte key, InputStream input, OutputStream output) throws IOException {
		int nextByte;
		output.write(key);
		while ((nextByte = input.read()) != -1) {
			output.write(nextByte ^ key);
		}
	}

	public static void decryptFileTo(String fileName, String fileName2) throws IOException {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			input = new FileInputStream(fileName);
			output = new FileOutputStream(fileName2);
			decrypt(input, output);
		} finally {
			if (input != null) {
				input.close();
			}
			if (output != null) {
				output.close();
			}
		}

	}

	public static List<String> buildVocabulary(String fileName) throws IOException {
		Scanner input = null;
		try {
			input = new Scanner(new FileReader("story.txt"));
			input.useDelimiter("[ \t\n\r\\.,\"-]+");
			Set<String> uniqueWords = new HashSet<String>();
			while (input.hasNext()) {
				uniqueWords.add(input.next().toLowerCase());
			}
			List<String> vocabulary = new ArrayList<String>(uniqueWords);
			Collections.sort(vocabulary);
			return vocabulary;
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}
}
