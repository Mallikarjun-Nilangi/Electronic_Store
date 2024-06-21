package com.lcwd.elestronic.store.Services.Impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.elestronic.store.Exceptions.BadApiRequestException;
import com.lcwd.elestronic.store.Services.FileService;

@Service
public class FileServiceImpl implements FileService {
	
	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadImageFile(MultipartFile file, String path) throws IOException {
		
		
		String originalFilename = file.getOriginalFilename();
		
		logger.info("Filename : {} ",originalFilename);
		
		
		String filename = UUID.randomUUID().toString();
		
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		
		String fileNameWithExtension = filename+extension;
		
		String fullPathWithFileName = path + fileNameWithExtension;
		
		logger.info("full image path : {} ",fullPathWithFileName);
		
		if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
			
			// file save
			File folder = new File(path);
			
			logger.info("file extension is : {}",extension);
			
			if(!folder.exists()) {
				
				//create folder 
				folder.mkdirs();
			}
			
			//upload file
			
			Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
			
			return fileNameWithExtension;
			
		}else {
			
			throw new BadApiRequestException("File with this "+extension+" not allowed");
		}
		
	}

	@Override
	public InputStream getResorce(String path, String name) throws FileNotFoundException {
	  
		String fullPath = path+File.separator+name;
		
		InputStream inputStream = new FileInputStream(fullPath);
		
		
		return inputStream;
	}

	

}
