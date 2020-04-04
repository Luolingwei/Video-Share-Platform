package com.imooc.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpegTest {

	private String ffmpegEXE;

	public FFMpegTest(String ffmpegEXE) {
		this.ffmpegEXE = ffmpegEXE;
	}

	public void convertor(String videoInputPath, String videoOutputPath) throws Exception {
//		ffmpeg -i input.mp4 -y output.avi

		// 空格地方切开加入List即可执行
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);
		command.add("-i");
		command.add(videoInputPath);
		command.add("-y");
		command.add(videoOutputPath);
//		for (String c: command){
//			System.out.print(c+" ");
//		}
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();

		InputStream errorStream = process.getErrorStream();
		InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String line = "";
		while ((line = bufferedReader.readLine()) != null){
		}

		if (bufferedReader!=null) bufferedReader.close();
		if (inputStreamReader!=null) inputStreamReader.close();
		if (errorStream!=null) errorStream.close();

	}

	public static void main(String[] args) {

		FFMpegTest ffmpeg = new FFMpegTest("/Users/luolingwei/Application/ffmpeg/ffmpeg");

		try {
			ffmpeg.convertor("/Users/luolingwei/Application/ffmpeg/IMG_1980.MOV", "/Users/luolingwei/Application/ffmpeg/test.mp4");
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
