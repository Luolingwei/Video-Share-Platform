package com.llingwei.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {

	private String ffmpegEXE;
	
	public MergeVideoMp3(String ffmpegEXE) {
		this.ffmpegEXE = ffmpegEXE;
	}
	
	public void convertor(String videoInputPath, String mp3InputPath,
			double seconds, String videoOutputPath) throws Exception {
//./ffmpeg -i newTest.mp4 -i bgm1.mp3 -t 10 -y newVideo.mp4
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);
		
		command.add("-i");
		command.add(videoInputPath);
		
		command.add("-i");
		command.add(mp3InputPath);
		
		command.add("-t");
		command.add(String.valueOf(seconds));
		
		command.add("-y");
		command.add(videoOutputPath);
		
//		for (String c : command) {
//			System.out.print(c + " ");
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
		MergeVideoMp3 ffmpeg = new MergeVideoMp3("/Users/luolingwei/Application/ffmpeg/ffmpeg");
		try {
			ffmpeg.convertor("/Users/luolingwei/Application/ffmpeg/newTest.mp4", "/Users/luolingwei/Application/ffmpeg/bgm1.mp3", 10.0, "/Users/luolingwei/Application/ffmpeg/newVideo.mp4");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
