package com.tuyano.springboot;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;


@Configuration
public class ResourceFiles {
	
	@Value("classpath:report/Blank_A4.jrxml")
	private Resource myfile;
	
	@Bean(name = "myfile")
	public String myfile() {
        try (InputStream is = myfile.getInputStream()) {
            return StreamUtils.copyToString(is, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
