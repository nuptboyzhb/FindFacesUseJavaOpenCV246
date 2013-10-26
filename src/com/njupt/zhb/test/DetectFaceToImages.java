package com.njupt.zhb.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import com.njupt.zhb.utils.ImageUtils;

/*
 *@author: ZhengHaibo  
 *web:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *2013-9-3  Nanjing,njupt,China
 */
public class DetectFaceToImages extends Thread{
	private String imagePaths;

	public String getImagePaths() {
		return imagePaths;
	}
	
	public void setImagePaths(String imagePaths) {
		this.imagePaths = imagePaths;
	}
	
	/**
	 * 
	 */
	public DetectFaceToImages(String imagePaths) {
        this.imagePaths = imagePaths;
	}
    /**
     * 人脸识别
     * @param imagePath 识别图像的路径
     */
	public void detectFaces(String imagePath) {
		System.out.println("\nRunning DetectFaceDemo");
		System.out.println(getClass().getResource("lbpcascade_frontalface.xml")
				.getPath());
		String xmlfilePath = getClass()
				.getResource("lbpcascade_frontalface.xml").getPath()
				.substring(1);
		CascadeClassifier faceDetector = new CascadeClassifier(xmlfilePath);
		Mat image = Highgui.imread(imagePath);
		// Detect faces in the image.
		// MatOfRect is a special container class for Rect.
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);
		System.out.println(String.format("Detected %s faces",
				faceDetections.toArray().length));
		if (faceDetections.toArray().length>0) {
			int i = 0;
			//String imageInPath = imagePath.substring(0,imagePath.lastIndexOf("\\"));
			//System.out.println(imageInPath);
			File file = new File(imagePath+".faces");
			file.mkdir();
			for (Rect rect : faceDetections.toArray()) {
				i++;
				System.out.println("face_" + i + ":x=" + rect.x + ",y=" + rect.y
						+ ",width=" + rect.width + ",height=" + rect.height);
				ImageUtils.cut(imagePath, imagePath + ".faces/face_" + i
						+ ".jpg", rect.x, rect.y, rect.width, rect.height);// 测试OK
			}
		}
	}
	/**
	 * 
	 * @param directory new File(imagePath)
	 * @param descendIntoSubDirectories 是否搜索子文件夹
	 * @return 返回directory下的所有文件路径
	 * @throws IOException
	 */
	public ArrayList<String> getAllImages(File directory, boolean descendIntoSubDirectories) throws IOException {
        ArrayList<String> resultList = new ArrayList<String>(256);
        File[] f = directory.listFiles();
        for (File file : f) {
            if (file != null && (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")) && !file.getName().startsWith("tn_")) {
                resultList.add(file.getCanonicalPath());
            }
            if (descendIntoSubDirectories && file.isDirectory()) {
                ArrayList<String> tmp = getAllImages(file, true);
                if (tmp != null) {
                    resultList.addAll(tmp);
                }
            }
        }
        if (resultList.size() > 0)
            return resultList;
        else
            return null;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ArrayList<String> imagePathArrayList;
		try {
			File file = new java.io.File(imagePaths);
			if (file.isFile()) {
				detectFaces(imagePaths);
				return;
			}
			imagePathArrayList = getAllImages(file,true);
			for (int i = 0; i < imagePathArrayList.size(); i++) {
				detectFaces(imagePathArrayList.get(i));
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
}
