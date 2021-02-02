package com.wangxun.autotest.ui.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Arrays;

public class ImageUtils {
	private class InvalidTest { }

	private static String DEFAULT_PREVFIX = "thumb_";
	private static Boolean DEFAULT_FORCE = false;

	public static void compareImage(String imagePath1, String imagePath2) {
		String[] images = { imagePath1, imagePath2 };
		if (images.length == 0) {
			System.exit(0);
		}

		// 分析圖片相似度
		String[][] list1 = getPX(images[0]);
		String[][] list2 = getPX(images[1]);
		int xiangsi = 0;
		int busi = 0;

		for (int i = 0; i < list1.length; i++) {
			for (int m = 0; m < list1[i].length; m++) {
				String[] value1 = list1[i][m].toString().split(",");
				String[] value2 = list2[i][m].toString().split(",");
				/*
				 * if (!Arrays.toString(value1).equals(Arrays.toString(value2)))
				 * { busi++; } else { xiangsi++; }
				 */

				if (compareArrays(value1, value2)) {
					busi++;
				} else {
					xiangsi++;
				}
			}
		}

		list1 = getPX(images[1]);
		list2 = getPX(images[0]);

		for (int i = 0; i < list1.length; i++) {
			for (int m = 0; m < list1[i].length; m++) {
				String[] value1 = list1[i][m].toString().split(",");
				String[] value2 = list2[i][m].toString().split(",");
				if (!Arrays.toString(value1).equals(Arrays.toString(value2))) {
					busi++;
				} else {
					xiangsi++;
				}
			}
		}

		System.out.println("before:" + xiangsi);
		System.out.println("busi:" + busi);

		String baifen = "";
		try {
			baifen = ((Double.parseDouble(xiangsi + "") / Double.parseDouble((busi + xiangsi) + "")) + "");
			baifen = baifen.substring(baifen.indexOf(".") + 1, baifen.indexOf(".") + 3);
		} catch (Exception e) {
			baifen = "0";
		}
		if (baifen.length() <= 0) {
			baifen = "0";
		}
		if (busi == 0) {
			baifen = "100";
		}

		System.out.println("相似像素数量：" + xiangsi + " 不相似像素数量：" + busi + " 相似率：" + Integer.parseInt(baifen) + "%");

	}

	private static boolean compareArrays(String[] value1, String[] value2) {

		for (int i = 0; i < value1.length; i++) {
			if ((Math.abs(Integer.parseInt(value1[i].toString()))
					- Math.abs(Integer.parseInt(value2[i].toString())) == 0)) {
				return false;
			}
		}

		return true;
	}

	public static String[][] getPX(String args) {

		int[] rgb = new int[3];

		File file = new File(args);
		BufferedImage bi = null;

		try {
			bi = ImageIO.read(file);
		} catch (Exception e) {
			// TODO: handle exception
		}

		int width = bi.getWidth();
		int height = bi.getHeight();
		int minx = bi.getMinX();
		int miny = bi.getMinY();

		String[][] list = new String[width][height];
		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				int pixel = bi.getRGB(i, j);

				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);

				list[i][j] = rgb[0] + "," + rgb[1] + "," + rgb[2];
				// System.out.println("list :" + rgb[0] + "," + rgb[1] + "," +
				// rgb[2]);
			}
		}

		return list;
	}

	public static void thumbnailImage(File imgFile, int w, int h, String prevfix, boolean force) {
		if (imgFile.exists()) {
			try {
				// ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG,
				// JPEG, WBMP, GIF, gif]

				String types = Arrays.toString(ImageIO.getReaderFormatNames());
				String suffix = null;

				// 获取图片后缀
				if (imgFile.getName().indexOf(".") > -1) {
					suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
				}

				// 类型和图片后缀全部小写，然后判断后缀是否合法
				if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0) {
					System.err.println("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
					return;
				}

				// System.out.println("target image's size, width:{},
				// height:{}.",w,h);
				Image img = ImageIO.read(imgFile);
				if (!force) {

					// 根据原图与要求的缩略图比例，找到最合适的缩略图比例
					int width = img.getWidth(null);
					int height = img.getHeight(null);
					if ((width * 1.0) / w < (height * 1.0) / h) {
						if (width > w) {
							h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
							// log.debug("change image's height, width:{},
							// height:{}.",w,h);
						}
					} else {
						if (height > h) {
							w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
							// log.debug("change image's width, width:{},
							// height:{}.",w,h);
						}
					}
				}
				BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics g = bi.getGraphics();
				g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
				g.dispose();
				String p = imgFile.getPath();
				// 将图片保存在原目录并加上前缀
				ImageIO.write(bi, suffix, new File(
						p.substring(0, p.lastIndexOf(File.separator)) + File.separator + prevfix + imgFile.getName()));
			} catch (IOException e) {
				System.err.println("generate thumbnail image failed.");
			}
		} else {
			System.err.println("the image is not exist.");
		}
	}

	/**
	 * <p>
	 * Title: cutImage
	 * </p>
	 * <p>
	 * Description: 根据原图与裁切size截取局部图片
	 * </p>
	 * 
	 * @param srcImg
	 *            源图片
	 * @param output
	 *            图片输出流
	 * @param rect
	 *            需要截取部分的坐标和大小
	 */
	public static void cutImage(File srcImg, OutputStream output, Rectangle rect) {
		if (srcImg.exists()) {
			FileInputStream fis = null;
			ImageInputStream iis = null;
			try {
				fis = new FileInputStream(srcImg);
				String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");

				String suffix = null;

				if (srcImg.getName().indexOf(".") > -1) {
					suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
				}

				if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase() + ",") < 0) {
					System.err.println("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
					return;
				}

				// 将FileInputStream 转换为ImageInputStream
				iis = ImageIO.createImageInputStream(fis);

				// 根据图片类型获取该种类型的ImageReader
				ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
				reader.setInput(iis, true);
				ImageReadParam param = reader.getDefaultReadParam();
				param.setSourceRegion(rect);
				BufferedImage bi = reader.read(0, param);

				ImageIO.write(bi, suffix, output);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null)
						fis.close();
					if (iis != null)
						iis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("the src image is not exist.");
		}
	}

	public static void thumbnailImage(String imagePath, int w, int h, String prevfix, boolean force) {
		File imgFile = new File(imagePath);
		thumbnailImage(imgFile, w, h, prevfix, force);
	}

	public void thumbnailImage(String imagePath, int w, int h, boolean force) {
		thumbnailImage(imagePath, w, h, DEFAULT_PREVFIX, force);
	}

	public void thumbnailImage(String imagePath, int w, int h) {
		thumbnailImage(imagePath, w, h, DEFAULT_FORCE);
	}

	public static void cutImage(File srcImg, OutputStream output, int x, int y, int width, int height) {
		cutImage(srcImg, output, new Rectangle(x, y, width, height));
	}

	public static void cutImage(File srcImg, String destImg, Rectangle rect) {

		try {
			cutImage(srcImg, new FileOutputStream(destImg), rect);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void cutImage(File srcImg, String destImg, int x, int y, int width, int height) {
		cutImage(srcImg, destImg, new Rectangle(x, y, width, height));
	}

	public static void cutImage(String srcImg, String destImg, int x, int y, int width, int height) {
		cutImage(new File(srcImg), destImg, new Rectangle(x, y, width, height));
	}

	public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath, Integer degree) {
		OutputStream os = null;
		try {
			Image srcImg = ImageIO.read(new File(srcImgPath));
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			// 得到画笔对象
			// Graphics g= buffImg.getGraphics();
			Graphics2D g = buffImg.createGraphics();

			// 设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
					0, null);

			if (null != degree) {
				// 设置水印旋转
				g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}
			// 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
			ImageIcon imgIcon = new ImageIcon(iconPath);
			// 得到Image对象。
			Image img = imgIcon.getImage();
			float alpha = 0.2f; // 透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			// 表示水印图片的位置
			g.drawImage(img, 150, 300, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g.dispose();
			os = new FileOutputStream(targerPath);
			// 生成图片
			ImageIO.write(buffImg, "JPG", os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void markImageByText(String filePath, String targerPath, String markContent, Color markContentColor,
			String fontType, int fontSize) {
		OutputStream os = null;
		try {
			Image srcImg = ImageIO.read(new File(filePath));
			int width = srcImg.getWidth(null);
			int height = srcImg.getHeight(null);
			BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// 得到画笔对象
			Graphics2D g = buffImg.createGraphics();
			g.setColor(markContentColor);
			g.setBackground(Color.white);

			g.drawImage(srcImg, 0, 0, null);

			AttributedString ats = new AttributedString(markContent);
			Font f = new Font(fontType, Font.BOLD, fontSize);

			ats.addAttribute(TextAttribute.FONT, f, 0, markContent.length());
			AttributedCharacterIterator iter = ats.getIterator();

			g.drawString(iter, width / 5, height / 5); // 添加水印的文字和设置水印文字出现的内容

			g.dispose();
			os = new FileOutputStream(targerPath);
			// 生成图片
			ImageIO.write(buffImg, "JPG", os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// ImageUtils.compareImage("C:\\Users\\Wendy\\Desktop\\Image1.png",
		// "C:\\Users\\Wendy\\Desktop\\Image2.png");
		// test();
		// cutImage("C:\\Users\\Wendy\\Desktop\\img.png",
		// "C:\\Users\\Wendy\\Desktop", 0, 146, 720, 405);
		// thumbnailImage("C:\\Users\\Wendy\\Desktop\\cut__1440657046525_img.png",
		// 364, 204, "test", true);
		String srcImgPath = "F:/workplace/java/appautotest/screenShotPiaoWuWeb/20151101_143216_135_.jpg";
		String iconPath = "F:/workplace/java/appautotest/screenShotPiaoWuWeb/log.jpg";
		String targerPath = "F:/workplace/java/appautotest/screenShotPiaoWuWeb/test.jpg";
		int degree = 3;
		markImageByText(iconPath, targerPath, "45454545", Color.red, "黑体", 13);
		markImageByIcon(iconPath, srcImgPath, targerPath, degree);
		/*
		 * String s =
		 * "[大佬孝靚抽 向港隊敬禮激讚勁揪, 隔空傳情話 謝安琪與張繼聰一味靠黑, 【飲食男女】人氣2大芝士撻 「…, 抵玩又夠潮 Reebok與Sandr…, 【飲食男女】人氣2大芝士撻 「…, 抵玩又夠潮 Reebok與Sandr…, 無恥! 匈女記為影相勾跌逃生難民, 【飲食男女】大地藝術祭(一)自己…, 哈利波特出插畫版 重繪魔法世界, 哈利波特出插畫版 重繪魔法世界, 狄志遠宣佈退出民主黨, 港足表現佳 田北辰批噓國歌球迷搏…, 徐正曦借糧搵藉口 周麗淇冇收…]"
		 * ; String[] w2 = s.split(","); List<String> ls = new
		 * ArrayList<String>(); for (int i = 0; i <w2.length; i++) {
		 * ls.add(w2[i]); }
		 * 
		 * for (int i = 0; i < ls.size(); i++) { for (int j = ls.size() -1; j >
		 * i; j--) { if(ls.get(i).equals(ls.get(j))){ ls.remove(j); } } }
		 * System.out.println(ls); compareImage(
		 * "C:\\Users\\Wendy\\Desktop\\testcut__1440657046525_img.png" ,
		 * "C:\\Users\\Wendy\\Desktop\\cut__1440656895566_img.png");
		 */
	}
}
