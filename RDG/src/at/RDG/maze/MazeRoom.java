package at.RDG.maze;

import general.Enums.ViewingDirections;

import java.util.HashSet;
import java.util.Set;

public class MazeRoom {
	

	public final static Set<ViewingDirections> allDirs = new HashSet<ViewingDirections>();
	
	private Set<ViewingDirections> opendoors = new HashSet<ViewingDirections>();
	private int posX;
	private int posY;
	
	MazeRoom(int posX, int posY){
		this.posX = posX;
		this.posY = posY;
		if(allDirs.isEmpty()){
			allDirs.add(ViewingDirections.NORTH);
			allDirs.add(ViewingDirections.EAST);
			allDirs.add(ViewingDirections.SOUTH);
			allDirs.add(ViewingDirections.WEST);
		}
	}
	
	MazeRoom openDoor(ViewingDirections dir){
		this.opendoors.add(dir);
		MazeRoom r = null;
		switch(dir){
		case NORTH:
			if((r = Maze.getInstance().getRoom(this.posX, this.posY-1)) != null)
				r._openDoor(ViewingDirections.SOUTH);
			break;
		case EAST:
			if((r = Maze.getInstance().getRoom(this.posX+1, this.posY)) != null)
				r._openDoor(ViewingDirections.WEST);
			break;
		case SOUTH:
			if((r = Maze.getInstance().getRoom(this.posX, this.posY+1)) != null)
				r._openDoor(ViewingDirections.NORTH);
			break;
		case WEST:
			if((r = Maze.getInstance().getRoom(this.posX-1, this.posY)) != null)
				r._openDoor(ViewingDirections.EAST);
			break;
		}
		return r;
	}
	
	private void _openDoor(ViewingDirections dir){
		this.opendoors.add(dir);
	}
	
	MazeRoom closeDoor(ViewingDirections dir){
		this.opendoors.remove(dir);
		MazeRoom r = null;
		switch(dir){
		case NORTH:
			if((r = Maze.getInstance().getRoom(this.posX, this.posY-1)) != null)
				r._closeDoor(ViewingDirections.SOUTH);
			break;
		case EAST:
			if((r = Maze.getInstance().getRoom(this.posX+1, this.posY)) != null)
				r._closeDoor(ViewingDirections.WEST);
			break;
		case SOUTH:
			if((r = Maze.getInstance().getRoom(this.posX, this.posY+1)) != null)
				r._closeDoor(ViewingDirections.NORTH);
			break;
		case WEST:
			if((r = Maze.getInstance().getRoom(this.posX-1, this.posY)) != null)
				r._closeDoor(ViewingDirections.EAST);
			break;
		}
		return r;
	}
	
	private void _closeDoor(ViewingDirections dir){
		this.opendoors.remove(dir);
	}
	
	public boolean isDoorOpen(ViewingDirections dir){
		return this.opendoors.contains(dir);
	}
	
	public Set<ViewingDirections> getOpenDoors(){
		return this.opendoors;
	}
	
	public Set<ViewingDirections> getClosedDoors(){
		Set<ViewingDirections> temp = new HashSet<ViewingDirections>(allDirs);
		temp.removeAll(this.opendoors);
		return temp;
	}
	
	public ViewingDirections[] getOpenDoorsArray(){
		return this.opendoors.toArray(new ViewingDirections[this.opendoors.size()]);
	}
	
	public ViewingDirections[] getClosedDoorsArray(){
		Set<ViewingDirections> temp = new HashSet<ViewingDirections>(allDirs);
		temp.removeAll(this.opendoors);
		return temp.toArray(new ViewingDirections[temp.size()]);
	}
	
	MazeRoom getAdjacentRoom(ViewingDirections dir){
		MazeRoom r = null;
		switch(dir){
		case NORTH:
			r = Maze.getInstance().getRoom(this.posX, this.posY-1);
			break;
		case EAST:
			r = Maze.getInstance().getRoom(this.posX+1, this.posY);
			break;
		case SOUTH:
			r = Maze.getInstance().getRoom(this.posX, this.posY+1);
			break;
		case WEST:
			r = Maze.getInstance().getRoom(this.posX-1, this.posY);
			break;
		}
		return r;
	}
}
