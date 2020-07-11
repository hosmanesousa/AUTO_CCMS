package sba.ccms.visa.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CTF {
	
	private String path;
	private String tempDirPath;
	private String tempFile;
	
	public CTF(String path) {
		this.path = path;
		tempDirPath ="C://Users//A237430//Desktop//";
		try {
			tempFile = getFilesInTempDir(tempDirPath) + "//";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPath() {
		return path;
	}

	public List<File> fileOnFolder() {
				
		File file = new File(getPath());
		if ( file.list().length == 0) {
			System.out.println("No files on this folder");
		} 
		
		String [] fileName = file.list();
		File[] files = file.listFiles();

		List<File> renamedFiles = new ArrayList<>();
		
		for ( int i = 0; i < files.length; i++) {
			
			if ( !fileName[i].startsWith("CTF") ) {
				renameCTForigin(fileName[i]);
				renamedFiles.add(renameCTF(fileName[i]));
			} 
		}
		return renamedFiles;
	}
	
	
	public File renameCTForigin(String fileName) {
				
		File file1 = new File(getPath() + fileName);
		File file2 = new File(getPath() + "CTF_" + fileName);
		boolean renamed = file1.renameTo(file2);		
		if ( renamed ) {
			return file2;
		}
		return file1;	
	}
	
	public File renameCTF(String fileName) {
		
		String initials = "CTF_";
		String newFileName  = initials.concat(fileName);
		File file = new File(newFileName);
		
		return file;
	}
	
	
	public void sortCTF(List<File> listName) {
		
		Collections.sort(listName);
	}
		
	public void copyCTF() throws IOException{
		
			List<File> file = fileOnFolder();
			
			String [] fileNames = new String[file.size()];
			  for (int i = 0; i < file.size(); i++) {
//				System.out.println("The value of file here is " + file.get(i).toString());
			    fileNames[i] = file.get(i).toString();
				
			    File source = new File(getPath() + fileNames[i]);
			    File destination = new File(tempFile + fileNames[i]);
			    
			    try {
			    	copyFile(source, destination);//,StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			  } 
		}
	}
	
	public void copyCTFdestFolder(String copyDestination) throws IOException{
				
		File files = new File(tempFile);
		
		String[] fileNames = files.list();
		
		if ( files.list().length > 0 ) {
			
			for ( int i = 0; i < fileNames.length; i++) {
				
			    File source = new File(tempFile + fileNames[i]);
			    File destination = new File(copyDestination + fileNames[i]);
				
			    try {
			    	moveFile(source, destination);//,StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			  } 
			}
		}
			
		System.out.println("Delete Folder");
		boolean deleted = files.delete();
		System.out.println("Value of boolean deleted..." + deleted);
		System.out.println("Folder Deleted");

	}
		
	
	private static String getFilesInTempDir(String directory) throws IOException {
		
	    Path rootDirectory = FileSystems.getDefault().getPath(directory);
	    Path tempDirectory = Files.createTempDirectory(rootDirectory, "ctf-");
	    String dirPath = tempDirectory.toString();
	    return dirPath;
	
	}
	
	private void copyFile(File src, File dest) throws IOException{
	
			Files.copy(src.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
	}
	
	private void moveFile(File src, File dest) throws IOException{
    	
		Files.move(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    
	}	

}
	

			

	


