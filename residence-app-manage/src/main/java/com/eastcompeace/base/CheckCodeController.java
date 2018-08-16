package com.eastcompeace.base;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 生成验证码图片
 * @author caobo
 */
@Controller
public class CheckCodeController {

	/**
	 * 生成验证码图片
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkcode")
	public void getCheckCodeInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("image/jpeg");
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);

		int width = 100, height = 40;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		OutputStream os = response.getOutputStream();
		Graphics g = image.getGraphics();
		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Arial", Font.PLAIN, 24));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		for (int i = 0; i < 5; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 19 * i + 6, 28);
		}

		
		HttpSession session = request.getSession();
		session.removeAttribute(Constant.SESSION_CHECK_CODE);
		session.setAttribute(Constant.SESSION_CHECK_CODE, sRand);
		
		//System.out.println("登录验证码："+sRand);
		
		g.dispose(); 
		ImageIO.write(image, "JPEG", response.getOutputStream());
		
		os.flush();
		os.close();
		os = null;

	}
	
	@RequestMapping("/checkcode2")
	public void getCheckCodeInfo2(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("image/jpeg");
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);

		int width = 100, height = 30;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		OutputStream os = response.getOutputStream();
		Graphics g = image.getGraphics();
		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Arial", Font.PLAIN, 24));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		for (int i = 0; i < 5; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 19 * i + 6, 28);
		}

		
		HttpSession session = request.getSession();
		session.removeAttribute(Constant.SESSION_CHECK_CODE2);
		session.setAttribute(Constant.SESSION_CHECK_CODE2, sRand);
		
		g.dispose(); 
		ImageIO.write(image, "JPEG", response.getOutputStream());
		
		os.flush();
		os.close();
		os = null;

	}
	
	
	
	@RequestMapping("/checkcode3")
	public void getCheckCodeInfo3(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("image/jpeg");
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);
		int width = 100, height = 30;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		OutputStream os = response.getOutputStream();
		Graphics g = image.getGraphics();
		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Arial", Font.PLAIN, 24));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		for (int i = 0; i < 5; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 19 * i + 6, 28);
		}

		
		HttpSession session = request.getSession();
		session.removeAttribute(Constant.SESSION_CHECK_CODE3);
		session.setAttribute(Constant.SESSION_CHECK_CODE3, sRand);
		g.dispose(); 
		ImageIO.write(image, "JPEG", response.getOutputStream());
		
		os.flush();
		os.close();
		os = null;

	}
	@RequestMapping("/checkcodeByPram")
	public void getCheckCodeInfoByPram(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("image/jpeg");
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);
		int width = 100, height = 40;
		String strName = "";
		width = Integer.parseInt(request.getParameter("width"));
		height = Integer.parseInt(request.getParameter("height"));
		strName = request.getParameter("sessionName");
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		OutputStream os = response.getOutputStream();
		Graphics g = image.getGraphics();
		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Arial", Font.PLAIN, 24));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		String sRand = "";
		for (int i = 0; i < 5; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 19 * i + 6, 28);
		}
		HttpSession session = request.getSession();
		session.removeAttribute(strName);
		session.setAttribute(strName, sRand);
		g.dispose(); 
		ImageIO.write(image, "JPEG", response.getOutputStream());
		os.flush();
		os.close();
		os = null;

	}



	private synchronized Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;

		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);

		return new Color(r, g, b);
	}

}
