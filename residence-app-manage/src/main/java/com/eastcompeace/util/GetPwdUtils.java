package com.eastcompeace.util;

import java.util.Random;

import com.eastcompeace.share.cipher.MD5Cipher;
import com.eastcompeace.share.utils.ByteUtils;

public class GetPwdUtils {

	public static final char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '3', '4', '5',
			'6', '7', '8', '9' ,'a','b','c','d','e','f','g','h','i','j','k','l',
			'm','n','o','p','q','r','s','t','u','v','w','x','y','z','!',
			'@','#','$','%','^','&','*'};
	
	public static final char[] str1={'A', 'B', 'C', 'D', 'E', 'F', 'G',
		'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
		'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	public static final char[] str2={'0', '1', '2', '3', '3', '4', '5',
		'6', '7', '8', '9'};
	
	public static final char[] str3={'a','b','c','d','e','f','g','h','i','j','k','l',
		'm','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	public static final char[] str4={'!','@','#','$','%','^','&','*'};
	
	public static String getPwd() {
		Random random = new Random();
		String reStr ="";
		String reStr1 ="";
		String reStr2 ="";
		String reStr3 ="";
		String reStr4 ="";
		
		for (int j = 0; j < 6; j++) {
			reStr1+= str2[random.nextInt(10)];
		}
		
		for (int j = 0; j < 2; j++) {
			reStr2+= str1[random.nextInt(26)];
		}
		
		for (int j = 0; j < 2; j++) {
			reStr3+= str3[random.nextInt(26)];
		}
		
		for (int j = 0; j < 2; j++) {
			reStr4+= str4[random.nextInt(8)];
		}
		reStr=reStr1+reStr2+reStr3+reStr4;
		return reStr;
	}
	public static boolean chkPwd(String pwd)
	{
		boolean leagal=true;
		for(int i=0;i<str.length;i++)
		{
			int count=0;
			for(int j=0;j<pwd.length();j++)
			{
				if(pwd.charAt(j)==str[i]) count++;
			}
			if(count<2) continue;
			else leagal=false ;
			    break;
		}
		return leagal;
	}
	
	public static String leagalPwd()
	{
		String returnStr = "";
		for (int i = 1; i >0; i++) {
				String pwd=getPwd();
//				System.out.println(i);
//				System.out.println(pwd);
				if(chkPwd(pwd)){
					 returnStr=pwd;
				break;}
			}
		return returnStr;
	}
	public static void main(String[] args) {
		
		String pwd=leagalPwd();
		System.out.println(pwd);
	
		String afMD5=ByteUtils.toHexString(MD5Cipher.encrypt(pwd.getBytes()));
     	System.out.println(afMD5);
     	
     	System.out.println(ByteUtils.toHexString(MD5Cipher.encrypt("123asd#".getBytes())));
		
	}

}
