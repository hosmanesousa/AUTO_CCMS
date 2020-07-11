package sba.ccms.visa.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DEC {
	
	private String path;
	
	public DEC(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public List<File> fileOnFolder() {
		
		File file = new File(getPath());
		if ( file.list().length == 0) {
			System.out.println("No files on " + getPath());
			return Collections.emptyList();
		}  
		
		String [] fileName = file.list();
		File[] files = file.listFiles();
		File[] newRenameFile = file.listFiles();
		
		for ( int i = 0; i < files.length; i++) {
			if ( !fileName[i].startsWith("dec") ) {
				newRenameFile[i] = renameDEC(fileName[i]);
			}
		}
		List<File> filelist = Arrays.asList(newRenameFile);
		// Sort the list
		sortDEC(filelist);
		
		return filelist;
	}
	
	public File renameDEC(String fileName) {
				
		File file1 = new File(getPath() + fileName);
		File file2 = new File(getPath() + "dec_" + fileName);
		boolean renamed = file1.renameTo(file2);		
		if ( renamed ) {
			return file2;
		}
		return file1;	
	}
	
	public void sortDEC(List<File> listName) {
		
		Collections.sort(listName);
	}
	
	public void copyDEC(String copyDestination) throws IOException{
		
			List<File> ficheiros = fileOnFolder();
					
			String [] fileNames = new String[ficheiros.size()];
			  for (int i = 0; i < ficheiros.size(); i++) {
			    fileNames[i] = ficheiros.get(i).getName();
										
    			File source = new File(getPath() + fileNames[i]);
			    File destination = new File(copyDestination + fileNames[i]);
			    
			    try {
			    	copyFile(source, destination);//,StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			  } 
		}
	}
	
	
	public void moveDEC(String moveDEC) throws IOException{
				
		List<File> ficheiros = fileOnFolder();

		String [] fileNames = new String[ficheiros.size()];
			for (int i = 0; i < ficheiros.size(); i++) {
				fileNames[i] = ficheiros.get(i).getName();
								
				File source = new File(getPath() + fileNames[i]);
				File destination = new File(moveDEC + fileNames[i]);
	    
				try {
					moveFile(source, destination); 
				} catch( IOException ex) {
		    	 ex.printStackTrace();
				} 
			}
	}
	
	public boolean isThereFile() {
		
		boolean fileOnFolder = false;
		
		File file = new File(getPath());
		
		if ( file.list().length > 0) {
			fileOnFolder = true;
		}
		return fileOnFolder;
	}
	
	public void deleteFile() {
		
		boolean fileStillOnFolder = isThereFile();
		
		File file = new File(getPath() + "//");
		
		File [] fileRemoved = file.listFiles();
		
		for ( int i = 0; i < file.listFiles().length; i++) {
		
			if (fileStillOnFolder) {
				System.out.println("File deleted " + fileRemoved[i]);
				fileRemoved[i].delete();
			}
		}
	}
	
	private void copyFile(File src, File dest) throws IOException{
	
			Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
			
	private void moveFile(File src, File dest) throws IOException{
		    	
			Files.move(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    
	}	

//	
//	public static void main(String[] args) {
//		
//		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//		
//		
//		DEC dec = new DEC("C://CCMS//Testing_folder_ccms//originFile//");
//		
//		String backUp = "C://CCMS//Testing_folder_ccms//Dec_backup//";
//		
//		try {
//			dec.copyDEC(backUp);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			e.getMessage();
//		}
//		
//		String moveDec = "C://CCMS//Testing_folder_ccms//DEC_move//";
//		
//		Runnable task = new Runnable() {
//			public void run() {
//				try {
//					dec.moveDEC(moveDec);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				// DEC_move
//			}
//		};
//		
//		int delay = 2;
//		
//		scheduler.schedule(task, delay, TimeUnit.MINUTES);
//		scheduler.shutdown();
//		
//		
//	}
	
//	public void checkForFile() {
//		File file = new File("C://CCMS//Testing_folder_ccms//DEC_move//");
//		
//		while ( file.list() > 0) {
//			
//		}
//	}
}
