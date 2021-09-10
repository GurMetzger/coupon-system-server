package app.core.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static app.core.utils.Constants.IMAGE_STORAGE_DIR;

@Service
public class ImageUtil {
	
	// Attribute
	private Path fileStoragePath;
	
	
	// Methods
	@PostConstruct
	public void init() {
		try {
			this.fileStoragePath = Paths.get(IMAGE_STORAGE_DIR).toAbsolutePath();
			Files.createDirectories(this.fileStoragePath);
		} catch (IOException e) {
			throw new RuntimeException("Could not create directory", e);
		}
	}
	
	/**
	 * Stores the provieded Coupon's Image of the coupon in the main directory.
	 * Returns the Image's file name.
	 * 
	 * @param image the image that is stored
	 * @return Returns the image's original file name
	 * @throws CouponImageStoringException if contains illegal character(s) or storing failed
	 */
	public String storeImage(MultipartFile image) {
		
		// If image is null, no image was sent
		if (image == null) {
			return null;
		}
		
		String imageName = image.getOriginalFilename();
		
		if (imageName.contains("..")) {
			throw new RuntimeException("Image name contains illegal characters '..'");
		}
		
		// Copy the file to the destination directory (if already exists replace)
		try {
			Path targetLocation = this.fileStoragePath.resolve(imageName);
			Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("Storing Image '" + imageName + "' failed", e);
		}
		
		return imageName;
	}
	
	/**
	 * Recieve an original file name of an Image. Returns an array resource of that Image.
	 * Used in the Frontend for displaying the Image.
	 * 
	 * @param imageName The Image's original file name
	 * @return Returns an array resource of the Image
	 */
	public ByteArrayResource getImageInputStream(String imageName) {
		
		if (imageName == null) {
			return null;
		}
		
		try {
			Path targetLocation = this.fileStoragePath.resolve(imageName);
			ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(targetLocation));

			return inputStream;
			
		} catch (InvalidPathException | UnsupportedOperationException | IOException e) {
			throw new RuntimeException("Storing Image '" + imageName + "' failed", e);
		}
	}
	
	public void deleteImage(String imageName) {
		
		if (imageName == null) {
			return;
		}
		
		Path imagePath = Paths.get(this.fileStoragePath + "/" + imageName);
		
		try {
			Files.delete(imagePath);
		} catch (IOException e) {
			throw new RuntimeException("Deleting Image '" + imageName + "' failed", e);
		}
	}
	
}
