package util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {
	private String password;
	private String salt;
	private String hash;

	//Constructor used for registering password/salt into db
	public PasswordUtil(String password) {
		this.password = password;
		generateSalt();
		hashPassword();
	}

	//Constructor used for authenticating user
	public PasswordUtil(String password, String salt){
		this.password = password;
		this.salt = salt;
		hashPassword();
	}
	
	private void generateSalt(){
		SecureRandom random = new SecureRandom();
		String ranString = new BigInteger(130, random).toString(32);
		salt = ranString + System.currentTimeMillis();  
	}
	
	private void hashPassword() {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update((password + salt).getBytes());
			hash = new String(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public String getSalt() {
		return salt;
	}

	public String getHash() {
		return hash;
	}
}