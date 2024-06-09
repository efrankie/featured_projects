package spookyobjects;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CryptTest {

	private byte[] plainText = { 0x3A, 0x6B, (byte)0xC1 };
	private byte[] cryptText = { (byte)0xA3, (byte)0x99, (byte)0xC8, 0x62 };
	byte key = (byte)0xA3;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testDecrypt() throws IOException {
		InputStream input = null;
		ByteArrayOutputStream output = null;
		try {
			input = new ByteArrayInputStream(cryptText);
			output = new ByteArrayOutputStream();
			Crypt.decrypt(input, output);
			byte[] answer = output.toByteArray();
			assertTrue(answer.length == plainText.length);
			for(int i = 0; i < answer.length; ++i) {
				assertTrue(answer[i] == plainText[i]);
			}

		} catch(IOException ex) {
			fail("Caught an unexpected IOException.");
		} finally {
			 if(input != null) {
				 input.close();
			 }
			 if(output != null) {
				 output.close();
			 }
		}
	}

	@Test
	void testEncrypt() throws IOException {
		InputStream input = null;
		ByteArrayOutputStream output = null;
		try {
			input = new ByteArrayInputStream(plainText);
			output = new ByteArrayOutputStream();
			Crypt.encrypt(key, input, output);
			byte[] answer = output.toByteArray();
			assertTrue(answer.length == cryptText.length);
			for(int i = 0; i < answer.length; ++i ) {
				assertTrue(answer[i] == cryptText[i]);
			}
		} catch(IOException ex) {
			fail("Caught an unexpected IOException.");
		} finally {
			if(input != null) {
				input.close();
			}
			if(output != null) {
				output.close();
			}
		}
	}
	
	@Test()
	void testDecryptFile() {
		try {
			Crypt.decryptFileTo("crypt.txt", "story.txt");
		} catch(IOException ex) {
			fail("Caught an unexpected IOException.");
		}
	}
	
	@Test
	void testBuildVocabulary() {
		try {
			List<String> vocabulary = Crypt.buildVocabulary("story.txt");
			System.out.println(vocabulary.size());
			assertTrue(vocabulary.size() == 64);
			assertTrue(vocabulary.get(0).equals("a"));
			assertTrue(vocabulary.get(63).equals("worst"));
		} catch(IOException ex) {
			fail("Caught an unexpected IOException.");
		}
	}
}