package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

	public Integer calcSum(String filePath) throws IOException{
		LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
			
			@Override
			public Integer doSomthingWithLine(String line, Integer value) {
				return value + Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filePath, sumCallback, 0);
	}
	
	public Integer calcMultiply(String filePath) throws IOException{
		LineCallback<Integer> multiplyCallback = new LineCallback<Integer>() {
			
			@Override
			public Integer doSomthingWithLine(String line, Integer value) {
				return value * Integer.valueOf(line);
			}
		};
		
		return lineReadTemplate(filePath, multiplyCallback, 1);
	}
	
	public String concatenate(String filePath) throws IOException{
		LineCallback<String> concatenateCallback = new LineCallback<String>() {
			
			@Override
			public String doSomthingWithLine(String line, String value) {
				return value + line;
			}
		};
		
		return lineReadTemplate(filePath, concatenateCallback, "");
	}
	
	//���ø� �κ� �޼ҵ�� �и�
	public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			T res = initVal;
			String line = null;
			while((line = br.readLine())!= null) {
				res = callback.doSomthingWithLine(line, res);
			}
			return res;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if(br != null) {
				try { br.close(); } 
				catch(IOException e) { System.out.println(e.getMessage());}				
			}
		}
	}
}
