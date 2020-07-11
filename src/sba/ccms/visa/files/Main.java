/*
* 			CCMS visa files project
* 			Need refactoring
* 			Designed: Hosmane Sousa
* 			MVP1: 11/07/2020
* */

package sba.ccms.visa.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class Main {
	
	// Set files location
	private static final String eptOrigin = "C://CCMS//EPT//";
	private static final String decOrigin = "C://CCMS//DEC//";
	private static final String eaeOrigin = "C://CCMS//EAE//";
	private static final String edaOrigin = "C://CCMS//EDA//";
	private static final String eabOrigin = "C://CCMS//EAB//";
	private static final String ctfOrigin = "C://CCMS//T24.10.90//CTFs//CTF//";
	private static final String ddrOrigin = "C://CCMS//DDR//";
	private static final String mecOrigin = "C://CCMS//MEC//";

	private static String tempFile = null;
	private static final String ccmsBackup = "C://CCMS//BackUp//";

	private static final String ctfFiles = "C://CCMS//T24.10.90//CTFs//CTF_backup//";
	
	// DDR & MEC
	private final static String ddrProcessFolder = "C://CCMS//T24.10.90//DDR//";
	
	private final static String mecProcessFolder = "C://CCMS//T24.10.90//MEC//";

	// DEC, EAE, EAB, EDA, EPT
	private  static final String eptProcessingFolder = "C://CCMS//T24.10.90//EPT//";
	private  static final String decProcessingFolder = "C://CCMS//T24.10.90//DEC//";
	private  static final String eaeProcessingFolder = "C://CCMS//T24.10.90//EAE//";
	private  static final String eabProcessingFolder = "C://CCMS//T24.10.90//EAB//";
	private  static final String edaProcessingFolder = "C://CCMS//T24.10.90//EDA//";


	public static void main(String[] args) throws IOException {
		
		boolean monitorEPT = false;
		boolean monitorEAE = false;
		boolean monitorEAB = false;		
		
		EPT ept = new EPT(eptOrigin);
		DEC dec = new DEC(decOrigin);
		EAE eae = new EAE(eaeOrigin);
		EDA eda = new EDA(edaOrigin);
		EAB eab = new EAB(eabOrigin);
		CTF ctf = new CTF(ctfOrigin);
		DDR ddr = new DDR(ddrOrigin);
		MEC mec = new MEC(mecOrigin);
		
		String tempDirPath = "C://Users//A237430//Desktop//";
		tempFile = getFilesInTempDir(tempDirPath) + "//";
		
		// Copy the files
		
		copyTheFiles(ept,dec,eae,eda,eab,ctf); // Copy to 61.43
	
		copyTheFiles(ept,dec,eae,eda,eab);	   // Copy to 10.90
		
		// Copy DDR & MEC
		
		runDDR(ddr);
		runMEC(mec);
		
		
		// Process the files
		
		File src = null;
		File dest = null;

		File list = new File(tempFile);
		
		// Array of files in the created Temp folder
		String [] listFiles = list.list();
		
		boolean checkEPT = ept.isThereFile();
		
		File [] fileList = list.listFiles();
		
		if (checkEPT == true) {
			
			for ( int i = 0; i < fileList.length; i++) {
				
				if ( listFiles[i].startsWith("ept")) {
					src = new File(tempFile + listFiles[i]);
					dest = new File(eptProcessingFolder + listFiles[i]);
					moveFile(src,dest);
					monitorEPT = monitorForFiles(eptProcessingFolder);
				} 
			}
		} else {
			
			monitorEPT = true;
		}
		
		
		if (eae.isThereFile() == true && monitorEPT == true) {
			
			for ( int j = 0; j < fileList.length; j++) {
				
				if ( listFiles[j].startsWith("eae")) {
					src = new File(tempFile + listFiles[j]);
					dest = new File(eaeProcessingFolder + listFiles[j]);
					moveFile(src,dest);
					monitorEAE = monitorForFiles(eaeProcessingFolder);
				}
			}
			
		} else {
			
			monitorEAE = true;
		}
				
		if ( eab.isThereFile() == true && monitorEAE == true) {
			
			for ( int h = 0; h < fileList.length; h++) {
				
				if (listFiles[h].startsWith("eab")) {
					src = new File(tempFile + listFiles[h]);
					dest = new File(eabProcessingFolder + listFiles[h]);
					moveFile(src,dest);
					monitorEAB = monitorForFiles(eabProcessingFolder);
				}
			}
		
		} else {
			monitorEAB = true;
		}
		
		
		if (dec.isThereFile() == true && monitorEAB == true) {
			
			for ( int s = 0; s < fileList.length; s++) {
				
				if (listFiles[s].startsWith("dec")) {
					src = new File(tempFile + listFiles[s]);
					dest = new File(decProcessingFolder + listFiles[s]);
					moveFile(src,dest);
				}
				
			}
			
		}
		
		if (eda.isThereFile() == true && monitorEAB == true) {
			
			for ( int l = 0; l < fileList.length; l++) {
				
				if (listFiles[l].startsWith("eda")) {
					src = new File(tempFile + listFiles[l]);
					dest = new File(edaProcessingFolder + listFiles[l]);
					moveFile(src,dest);
				}
				
			}
			
		}
		
		
		// Delete temporary directory in 10.90
		System.out.println("Delete Folder MAIN");
		boolean deletedInMain = list.delete();;
		System.out.println("Value of boolean deleted MAIN..." + deletedInMain);
		System.out.println("Folder Deleted MAIN");
//		list.delete();
		// Delete all files in the origin 61.43
		deleteAllFiles(ept,eae,eda, eab,dec);
	}
		
	private static void moveFile(File src, File dest) throws IOException{
  	
		Files.move(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    
	}
	
	public static void copyTheFiles(EPT ept, DEC dec, EAE eae, EDA eda, EAB eab, CTF ctf) throws IOException {
		
			// Copy files
			ept.copyEPT(ccmsBackup);
			
			dec.copyDEC(ccmsBackup);
			
			eae.copyEAE(ccmsBackup);
			
			eda.copyEDA(ccmsBackup);
			
			eab.copyEAB(ccmsBackup);
			
			// Copy to intermediary folder
			ctf.copyCTF();
			
			// Copy to final folder
			ctf.copyCTFdestFolder(ctfFiles);
			
	}
	
	public static void copyTheFiles(EPT ept, DEC dec, EAE eae, EDA eda, EAB eab) throws IOException {
		
			// Copy files
	
			ept.copyEPT(tempFile);
		
			dec.copyDEC(tempFile);
		
			eae.copyEAE(tempFile);
		
			eda.copyEDA(tempFile);
		
			eab.copyEAB(tempFile);
		
	}
	
	public static void runDDR(DDR ddr) {
		
		// DDR to run on 26 of each month
		int dateOfDdr = ddr.date();

		if (dateOfDdr == 26) {
			
			try {
				ddr.copyDDR(ccmsBackup);
				ddr.moveDDR(ddrProcessFolder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	public static void runMEC(MEC mec) {
		
		// MEC to run on 27 of each month
				int dateOfMEC = mec.date();

				if (dateOfMEC == 27) {
					
					try {
						mec.copyMEC(ccmsBackup);
						mec.moveMEC(mecProcessFolder);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}	
	}
	
	public static void deleteAllFiles(EPT ept, EAE eae, EDA eda, EAB eab, DEC dec) {
		// Delete the files in the origin 61.43
	
		ept.deleteFile();
		eae.deleteFile();
		eda.deleteFile();
		dec.deleteFile();
		eab.deleteFile();

	}
	
	public static boolean isThereFile() {
		
		boolean fileOnFolder = false;
			
		File file = new File(tempFile);
			
		if ( file.list().length > 0) {
			fileOnFolder = true;
		}
		return fileOnFolder;

	}
	
	private static String getFilesInTempDir(String directory) throws IOException {
	
	    Path rootDirectory = FileSystems.getDefault().getPath(directory);
	    Path tempDirectory = Files.createTempDirectory(rootDirectory, "ccms-");
	    String dirPath = tempDirectory.toString();
	    return dirPath;
	
	}
	
		
	public static boolean monitorForFiles(String folder) {
		boolean fileDeleted = false;
		Path path = Paths.get(folder);
		try {
			WatchService watcher = path.getFileSystem().newWatchService();
			path.register(watcher, StandardWatchEventKinds.ENTRY_DELETE);
			System.out.println("Monitoring " + folder + " for changes...");
			WatchKey watchKey = watcher.take();
			List<WatchEvent<?>> events = watchKey.pollEvents();
			for (WatchEvent event : events) {
				if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
					System.out.println("Processed: " + event.context().toString());
					fileDeleted = true;
				} 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return fileDeleted;
	}
	
}
