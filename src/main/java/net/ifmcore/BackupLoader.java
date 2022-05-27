package net.ifmcore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.permissions.Permission;

public class BackupLoader {
	public static void loadChunk(Chunk chunk, String backupName, File backupFile) {
		Uni.instance.getServer().getScheduler().runTaskAsynchronously(Uni.instance, new  Runnable(){
			public void run() {
				try {
					Uni.logToOp(Localization.parse("message.backuploader.chunk.start", chunk.getWorld().getName(), chunk.getX(), chunk.getZ(), backupName));
			        File destDir = new File(Uni.getPluginFolderPath()+"/../../world_backup");
			        if(destDir.exists()) {
			        	deleteDirectory(destDir);
			        	destDir.mkdir();
			        }
			        Uni.logToOp("Дирректория world_backup создана");
			        byte[] buffer = new byte[1024];
			        ZipInputStream zis = new ZipInputStream(new FileInputStream(backupFile));
			        ZipEntry zipEntry = zis.getNextEntry();
			        Uni.logToOp("Начинаю распаковку архива...");
			        while (zipEntry != null) {
			            File newFile = newFile(destDir, zipEntry);
			            if (zipEntry.isDirectory()) {
			                if (!newFile.isDirectory() && !newFile.mkdirs()) {
			                    throw new IOException("Failed to create directory " + newFile);
			                }
			            } else {
			                // fix for Windows-created archives
			                File parent = newFile.getParentFile();
			                if (!parent.isDirectory() && !parent.mkdirs()) {
			                    throw new IOException("Failed to create directory " + parent);
			                }
			                
			                // write file content
			                FileOutputStream fos = new FileOutputStream(newFile);
			                int len;
			                while ((len = zis.read(buffer)) > 0) {
			                    fos.write(buffer, 0, len);
			                }
			                fos.close();
			            }
			            zipEntry = zis.getNextEntry();
			        }
			        zis.closeEntry();
			        zis.close();
			        Uni.logToOp("Архив распакован");
			        
			        File worldUID = new File(destDir, "/uid.dat");
			        if(worldUID.exists()) worldUID.delete();
			        
			        Uni.instance.getServer().getScheduler().runTask(Uni.instance, new  Runnable() {
		        		public void run() {
		        			World world = chunk.getWorld();
				        	Uni.logToOp("Начинаю загрузку мира...");
				        	World.Environment environment = World.Environment.NORMAL;
				        	if(world.getName().endsWith("nether")) environment = World.Environment.NETHER;
				        	else if(world.getName().endsWith("the_end")) environment = World.Environment.THE_END;
				        	WorldCreator worldCreator = WorldCreator.name("world_backup");
				        	worldCreator.environment(environment);
							World backupWorld = Bukkit.getServer().createWorld(worldCreator);
							Chunk newChunk = backupWorld.getChunkAt(chunk.getBlock(0, 0, 0).getLocation());
							Uni.logToOp("Мир загружен");
							Uni.logToOp("Начинаю обновление чанка...");
							for(int x = 0; x < 16; x++) {
								for(int z = 0; z < 16; z++) {
									for(int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
										Block block = chunk.getBlock(x, y, z);
										Block newBlock = newChunk.getBlock(x, y, z);
										block.setType(newBlock.getType());
										block.setBlockData(newBlock.getBlockData(), true);
									}
								}
							}
							Uni.logToOp("Чанк обновлен");
							
							Uni.logToOp("Начинаю отгрузку мира");
							Bukkit.getServer().unloadWorld("world_backup", false);
							Uni.logToOp("Мир отгружен");
							
							Uni.logToOp("Очищаю лишние файлы");
							deleteDirectory(destDir);
							Uni.logToOp("Завершено!");
		        		}
	        		});
		    	} catch(Exception e) {
		    		e.printStackTrace();
		    	}
			}
		});
	}
	
	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
	    File destFile = new File(destinationDir, zipEntry.getName());

	    String destDirPath = destinationDir.getCanonicalPath();
	    String destFilePath = destFile.getCanonicalPath();

	    if (!destFilePath.startsWith(destDirPath + File.separator)) {
	        throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
	    }

	    return destFile;
	}
	
	public static boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
}
