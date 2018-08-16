package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eastcompeace.share.utils.FileUtils;

public class JavaTest {

	public static void main(String[] args) {/*
		// TODO Auto-generated method stub
		//String smsContent = "【珠海市香洲区物业电子决策平台】尊敬的业主<phone>，您好！ 您的电子决策账号已经通过后台成功注册，密码为。凭手机号和密码可登陆香洲物业网站，或者微信公众号“香洲物业”参与小区业主大会投票，请注意保管，谢谢。";
		//System.out.println(smsContent.replace("<password>", "123456"));
		//test1();
		String str = "123345567";
		System.out.println(str.charAt(str.length()-1)%2);
		
	*/
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSS");
		StringBuilder fileName = new StringBuilder("IMG_1200.PNG");
		fileName.insert(fileName.lastIndexOf("."), sdf.format(new Date()));
		
		System.out.println(fileName); //IMG_1200 170208 092816 377.PNG
		}
	
	static void test1(){
		while(true){  
            //提示用户输入身份证号  
            System.out.println("请输入身份证号码：");  
            //通过流处理获得用户身份证号  
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));  
            String idNum=null;  
            try {  
                idNum= consoleReader.readLine();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）  
            Pattern idNumPattern = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");  
            //通过Pattern获得Matcher  
            Matcher idNumMatcher = idNumPattern.matcher(idNum);  
            //判断用户输入是否为身份证号  
            if(idNumMatcher.matches()){  
                System.out.println("您的出生年月日是：");  
                //如果是，定义正则表达式提取出身份证中的出生日期  
                Pattern birthDatePattern= Pattern.compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");//身份证上的前6位以及出生年月日  
                //通过Pattern获得Matcher  
                Matcher birthDateMather= birthDatePattern.matcher(idNum);  
                //通过Matcher获得用户的出生年月日  
                if(birthDateMather.find()){  
                    String year = birthDateMather.group(1);  
                    String month = birthDateMather.group(2);  
                    String date = birthDateMather.group(3);  
                    //输出用户的出生年月日  
                    System.out.println(year+"年"+month+"月"+date+"日");                  
                }     
            }else{  
                //如果不是，输出信息提示用户  
            	idNumPattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");  
            	idNumMatcher = idNumPattern.matcher(idNum); 
            	if(idNumMatcher.matches()){ 
            		 System.out.println("您的出生年月日是：");  
                     //如果是，定义正则表达式提取出身份证中的出生日期  
                     Pattern birthDatePattern= Pattern.compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");//身份证上的前6位以及出生年月日  
                     //通过Pattern获得Matcher  
                     Matcher birthDateMather= birthDatePattern.matcher(idNum);  
                     //通过Matcher获得用户的出生年月日  
                     if(birthDateMather.find()){  
                         String year = birthDateMather.group(1);  
                         String month = birthDateMather.group(2);  
                         String date = birthDateMather.group(3);  
                         //输出用户的出生年月日  
                         System.out.println(year+"年"+month+"月"+date+"日");                  
                     }
            	}else{
            		 System.out.println("您输入的并不是身份证号");  	
            	}
            	
            }  
        }  
	}  
	

}
