package com.demo;

import com.demo.geode.model.BookGemfire;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;


@SpringBootApplication()
@ClientCacheApplication(name = "geodeDemo", logLevel = "debug",
		locators = {@ClientCacheApplication.Locator(host="localhost",port = 10334)})
@EnableGemfireRepositories(basePackages = "com.demo.geode.repository")
@EnableEntityDefinedRegions(basePackageClasses = BookGemfire.class, clientRegionShortcut = ClientRegionShortcut.PROXY)

@EnablePdx
public class BookApplication {
//	private static final Logger log = LoggerFactory.getLogger(BookApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}
}
