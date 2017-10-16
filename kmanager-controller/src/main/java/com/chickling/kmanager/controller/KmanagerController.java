package com.chickling.kmanager.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author Hulva Luva.H
 * @since 2017-10-16
 *
 */
@SpringBootApplication
@ComponentScan("com.chickling.kmanager.controller.**")
public class KmanagerController {

	public static void main(String[] args) {
		SpringApplication.run(KmanagerController.class, args);
	}

}
