package com.demo;

import com.demo.model.BookGemfire;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;


@SpringBootApplication
@ClientCacheApplication(name = "BookDemo")
@EnableGemfireRepositories(basePackages = "com.demo.repository")
@EnableEntityDefinedRegions(basePackageClasses = BookGemfire.class, clientRegionShortcut = ClientRegionShortcut.PROXY)

@EnablePdx
public class BookApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}
}
