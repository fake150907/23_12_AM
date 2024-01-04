package com.KoreaIT.java.AM;

import com.KoreaIT.java.AM.controller.Container;

public class Main {

	public static void main(String[] args) {
		Container.init();
		new App().run();
	}
}
