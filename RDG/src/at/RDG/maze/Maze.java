package at.RDG.maze;

import general.Enums.ViewingDirections;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import at.RDG.network.ArgumentOutOfRangeException;

/**
 * @author Clemens
 *
 */
public class Maze {
	
	private static Maze INSTANCE = null;
	private static Random random = new Random();

	private int sizeX = 9;
	private int sizeY = 9;

	private int startX = 0;
	private int startY = 0;

	private int endX = 8;
	private int endY = 8;
	
	private boolean treasureRoom = false;
	private int treasureX;
	private int treasureY;
	
	private int correctPaths = 1;

	private Set<MazeRoom> startSet;
	private Set<MazeRoom> notFinishedStartSet;
	private Set<MazeRoom> endSet;
	private Set<MazeRoom> notFinishedEndSet;
	private Set<MazeRoom> freeSet;

	private MazeRoom maze[][] = null;

	private Maze() {
		this.startSet = new HashSet<MazeRoom>();
		this.notFinishedStartSet = new HashSet<MazeRoom>();
		this.endSet = new HashSet<MazeRoom>();
		this.notFinishedEndSet = new HashSet<MazeRoom>();
		this.freeSet = new HashSet<MazeRoom>();
		this.treasureX = this.sizeX/2;
		this.treasureY = this.sizeY/2;
	}

	/**
	 * Sets the size of the maze to the given one and sets the start point in
	 * the top left corner and the end point in the bottom right corner.
	 * 
	 * @param sizeX
	 *            Size of the maze in x direction.
	 * @param sizeY
	 *            Size of the maze in y direction.
	 * @throws ArgumentOutOfRangeException
	 *             Is thrown if one of the params are less then 2.
	 */
	public void setSize(int sizeX, int sizeY)
			throws ArgumentOutOfRangeException {
		if (sizeX < 2 || sizeY < 2)
			throw new ArgumentOutOfRangeException(
					"The size cannot be smaller then 2.");
		this.sizeX = sizeX;
		this.sizeY = sizeY;

		this.startX = 0;
		this.startY = 0;

		this.endX = this.sizeX - 1;
		this.endY = this.sizeY - 1;
		
		this.treasureX = this.sizeX/2;
		this.treasureY = this.sizeY/2;
	}

	/**
	 * Sets the start point of the maze if the passed coordinates are inside the
	 * maze and are not the same as the end point.
	 * 
	 * @param startX
	 *            X coordinate of start point
	 * @param startY
	 *            Y coordinate of start point
	 * @throws ArgumentOutOfRangeException
	 *             Is thrown if the passed coordinates are outside the maze.
	 * @throws IllegalArgumentException
	 *             Is thrown if the passed coordinates are the same as the
	 *             coordinates of the end point.
	 */
	public void setStart(int startX, int startY)
			throws ArgumentOutOfRangeException, IllegalArgumentException {
		if (startX < 0 || startX > (this.sizeX - 1) || startY < 0
				|| startY > (this.sizeY - 1))
			throw new ArgumentOutOfRangeException(
					"The start point can only be inside the maze");
		if (startX == this.endX && startY == this.endY)
			throw new IllegalArgumentException(
					"The start point and end point of the maze cannot be the same.");
		this.startX = startX;
		this.startY = startY;
	}

	/**
	 * Sets the end point of the maze if the passed coordinates are inside the
	 * maze and are not the same as the start point
	 * 
	 * @param endX
	 *            X coordinate of end point
	 * @param endY
	 *            Y coordinate of end point
	 * @throws ArgumentOutOfRangeException
	 *             Is thrown if the passed coordinates are outside the maze.
	 * @throws IllegalArgumentException
	 *             Is thrown if the passed coordinates are the same as the
	 *             coordinates of the start point.
	 */
	public void setEnd(int endX, int endY) throws ArgumentOutOfRangeException,
			IllegalArgumentException {
		if (endX < 0 || endX > (this.sizeX - 1) || endY < 0
				|| endY > (this.sizeY - 1))
			throw new ArgumentOutOfRangeException(
					"The end point can only be inside the maze");
		if (endX == this.startX && endY == this.startY)
			throw new IllegalArgumentException(
					"The end point and start point of the maze cannot be the same.");
		this.endX = endX;
		this.endY = endY;
	}
	
	/**Activate or deactivate the treasure room in the middle of the maze.
	 * Default value = false;
	 * @param active true = active
	 */
	public void setTreasureRoom(boolean active){
		this.treasureRoom = active;
	}
	
	/**
	 * The Amount of paths the labyrinth has at most. If it is not possible to create all paths it will skip it.
	 * @param amount must be greater then 0
	 * @throws ArgumentOutOfRangeException is thrown if the amount is not greater then 0
	 */
	public void setAmountCorrectPaths(int amount) throws ArgumentOutOfRangeException{
		if(amount < 1){
			throw new ArgumentOutOfRangeException("The amount must be greater then 0");
		}
		this.correctPaths = amount;
	}

	/**
	 * Generates a maze with the defined parameters from {@link Maze#setSize(int sizeX, int sizeY)}, {@link Maze#setStart(int startX, int startY)} and {@link Maze#setEnd(int endX, int endY)}.
	 */
	public void generate() {
		this.maze = new MazeRoom[this.sizeX][this.sizeY];
		for (int x = 0; x < this.sizeX; x++){
			for(int y = 0; y < this.sizeY; y++){
				MazeRoom r = new MazeRoom(x,y);
				this.maze[x][y] = r;
				if(x == this.startX && y == this.startY){
					this.startSet.add(r);
					this.notFinishedStartSet.add(r);
				}else if(x == this.endX && y == this.endY){
					this.endSet.add(r);
					this.notFinishedEndSet.add(r);
				}else
					this.freeSet.add(r);
			}
		}
		
		if(this.treasureRoom){
			this.freeSet.remove(this.maze[this.treasureX][this.treasureY]);
		}
		
		while(!this.freeSet.isEmpty()){
			if(!this.notFinishedStartSet.isEmpty()){
				this.openRandomDoor(this.notFinishedStartSet, this.startSet, this.freeSet);
			}
			if(!this.notFinishedEndSet.isEmpty()){
				this.openRandomDoor(this.notFinishedEndSet, this.endSet, this.freeSet);
			}
		}

		Set<MazeRoom> borderStartSet = new HashSet<MazeRoom>();
		for(MazeRoom r : this.startSet){
			for(ViewingDirections d : MazeRoom.allDirs){
				if(this.endSet.contains(r.getAdjacentRoom(d))){
					borderStartSet.add(r);
				}
			}
		}
		Set<MazeRoom> borderEndSet = new HashSet<MazeRoom>();
		for(MazeRoom r : this.endSet){
			for(ViewingDirections d : MazeRoom.allDirs){
				if(this.startSet.contains(r.getAdjacentRoom(d))){
					borderEndSet.add(r);
				}
			}
		}
		
		int paths = 0;
		while(paths < this.correctPaths){
			if(borderStartSet.isEmpty())
				break;
			if(this.openRandomDoor(borderEndSet, this.startSet, borderStartSet, true))
				paths++;
		}
		
		if(this.treasureRoom){
			MazeRoom r = maze[this.treasureX][this.treasureY];
			r.openDoor(ViewingDirections.EAST);
			r.openDoor(ViewingDirections.WEST);
		}
	}

	/**
	 * Prints the maze with ASCII symbols into the console if it was generated.
	 */
	private void print() {
		if(this.maze == null)
			return;
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < this.maze[0].length; y++) {
			for (int i = 0; i < 3; i++) {
				for (int x = 0; x < this.maze.length; x++) {
					if (i == 0) {
						sb.append("#");
						if (this.maze[x][y].isDoorOpen(ViewingDirections.NORTH))
							sb.append(" ");
						else
							sb.append("#");
						if (x == this.maze.length - 1)
							sb.append("#");
					} else if (i == 1) {
						if (this.maze[x][y].isDoorOpen(ViewingDirections.WEST))
							sb.append(" ");
						else
							sb.append("#");
						sb.append(" ");
						if (x == this.maze.length - 1)
							if (this.maze[x][y].isDoorOpen(ViewingDirections.EAST))
								sb.append(" ");
							else
								sb.append("#");
					} else if (y != this.maze[0].length - 1)
						continue;
					else {
						sb.append("#");
						if (this.maze[x][y].isDoorOpen(ViewingDirections.SOUTH))
							sb.append(" ");
						else
							sb.append("#");
						if (x == this.maze.length - 1)
							sb.append("#");
					}
				}
				if (i != 2)
					sb.append("\n");
			}
		}
		System.out.println(sb.toString());
	}

	public static void main(String[] args) {
		Maze maze = Maze.getInstance();
		maze.setTreasureRoom(true);
		maze.setAmountCorrectPaths(2);
		maze.generate();
		maze.print();
	}
	
	/**Returns the requested {@link MazeRoom}
	 * @param posX X coordinate of requested Room
	 * @param posY Y coordinate of requested Room
	 * @return the requested Room or null if it does not exist
	 */
	public MazeRoom getRoom(int posX, int posY){
		if(posX < 0 || posX > (this.sizeX-1) || posY < 0 || posY > (this.sizeY-1))
			return null;
		return this.maze[posX][posY];
	}
	
	public int getTreasureRoomX(){
		return this.treasureX;
	}
	
	public int getTreasureRoomY(){
		return this.treasureY;
	}
	
	/**
	 * @return the instance of the maze.
	 */
	public static Maze getInstance(){
		if(INSTANCE == null)
			INSTANCE = new Maze();
		return INSTANCE;
	}
	
	private boolean openRandomDoor(Set<MazeRoom> notFinished, Set<MazeRoom> additional, Set<MazeRoom> compare){
		return openRandomDoor(notFinished,additional,compare,false);
	}
	
	private boolean openRandomDoor(Set<MazeRoom> notFinished, Set<MazeRoom> additional, Set<MazeRoom> compare, boolean ignoreFinished){
		//gets all not finished Rooms returns if there are none
		MazeRoom[] rooms = notFinished.toArray(new MazeRoom[notFinished.size()]);
		if(rooms.length == 0){
			return false;
		}
		//gets a random room
		MazeRoom r = rooms[random.nextInt(rooms.length)];
		//gets all closed doors of that random room if there are non it removes the room from the not finished set and returns
		ViewingDirections[] closedDoors = r.getClosedDoorsArray();
		if(closedDoors.length == 0){
			notFinished.remove(r);
			return false;
		}
		//gets a random direction
		ViewingDirections dir = closedDoors[random.nextInt(closedDoors.length)];
		//closes the door on that random direction
		MazeRoom connectedRoom = r.openDoor(dir);
		//if the now connected room is a free one it finishes the opening process if not it closes the door
		if(compare.contains(connectedRoom)){
			compare.remove(connectedRoom);
			notFinished.add(connectedRoom);
			additional.add(connectedRoom);
		}else{
			r.closeDoor(dir);
			return false;
		}
		if(ignoreFinished)
			return true;
		//finally it checks if the current room is already finished
		int freeRooms = 0;
		//checks for each possible direction if the adjacent room in the freeSet
		for (ViewingDirections d : MazeRoom.allDirs){
			MazeRoom adjacentRoom = r.getAdjacentRoom(d);
			if(adjacentRoom == null)
				continue;
			if(!compare.contains(adjacentRoom))
				continue;
			freeRooms ++;
		}
		//if the room has no free adjacent room it removes it from the notFinished set
		if(freeRooms == 0)
			notFinished.remove(r);
		return true;
	}

}
