package com.lcwd.elestronic.store.Services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	public String uploadImageFile(MultipartFile file,String path) throws IOException;
	
	InputStream getResorce(String path,String name) throws FileNotFoundException;
	
	
}
