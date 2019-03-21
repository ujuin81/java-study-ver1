package me.ujuin81.learningtest.spring.ioc.bean;

public class ConsolePrinter implements Printer {

	@Override
	public void print(String message) {
		System.out.println(message);
	}

}
