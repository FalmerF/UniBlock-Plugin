package net.ifmcore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;

import org.bukkit.event.world.ChunkLoadEvent;

public class MapSaver implements Runnable, Listener {
	public MapSaver instance;
	public String folder;
	Iterator<World> worldIterator;
	Iterator<Chunk> chunkIterator;
	World currentWorld;
	List<Material> materials;
	
	public MapSaver() {
		instance = this;
		folder = Uni.getPluginFolderPath()+"/chunks_data/";
		File dir = new File(folder);
		dir.mkdir();
		materials = Arrays.asList(Material.values());
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Uni.instance, this, 5l, 10l);
		
//		for(Material mat : materials) {
//			if(mat.isBlock())
//				Uni.log(mat.name()+" = "+materials.indexOf(mat));
//		}
	}

	@Override
	public void run() {
		if(worldIterator == null) {
			worldIterator = Bukkit.getWorlds().iterator();
		}
		if(chunkIterator == null) {
			if(worldIterator.hasNext()) {
				currentWorld = worldIterator.next();
				chunkIterator = Arrays.asList(currentWorld.getLoadedChunks()).iterator();
			}
			else
				worldIterator = null;
			return;
		}
		if(chunkIterator.hasNext()) {
			Chunk chunk = chunkIterator.next();
			try {
				saveChunk(chunk);
			} catch(Exception e) {}
		}
		else {
			chunkIterator = null;
		}
	}
	
	void saveChunk(Chunk chunk) throws IOException  {
		String data = "";
		World world = chunk.getWorld();
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				Block block = world.getHighestBlockAt(chunk.getX()*16+x, chunk.getZ()*16+z);
				data += materials.indexOf(block.getType())+":"+block.getY()+" ";
			}
		}
	    BufferedWriter writer = new BufferedWriter(new FileWriter(getChunkFile(chunk)));
	    writer.write(data);
	    writer.close();
	}
	
	File getChunkFile(Chunk chunk) {
		try {
			World world = chunk.getWorld();
			String file_name = folder+world.getName()+"_"+chunk.getX()+"_"+chunk.getZ()+".data";
			File f = new File(file_name);
			if(!f.exists())
				f.createNewFile();
			return f;
		} catch(Exception e) {
			return null;
		}
	}
	
	@EventHandler
	public void onChunkLoadEvent(ChunkLoadEvent event) {
		if(event.isNewChunk())
			try {
				saveChunk(event.getChunk());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
