import java.io.*;
import java.security.*;

public class ComputeSHA {
	public static void main(String[] args) throws IOException {
		File file;
		try {
			file = new File(args[0]);
			byte[] buffer = new byte[(int)file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(buffer);
			fis.close();
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(buffer);
			byte[] digest = md.digest();
			System.out.println(bytesToHex(digest));
		} catch(Exception e) {
			System.out.println("Exception: " + e);
		}
	}

	public static String bytesToHex(byte[] b) {
      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                         '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
      StringBuffer buf = new StringBuffer();
      for (int j=0; j<b.length; j++) {
         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
         buf.append(hexDigit[b[j] & 0x0f]);
      }
      return buf.toString();
   }

}