package kam_wang.cs146.project3;

/**
 * Models a Cell for a maze with neighbors and walls
 * 
 * @author Rico Kam and James Wang
 *
 */
public class Cell
{
   private int[] walls;
   private Cell[] neighbors;
   private String color;
   private Cell parent;
   private int steps;
   private int x;
   private int y;
   private boolean isStart;
   private boolean isEnd;
   private boolean isInSolution;

   /**
    * Models a cell for a maze with N,E,S,W neighbors, color, parent and walls
    */
   public Cell()
   {
      neighbors = new Cell[4];
      walls = new int[4];
      setAllWalls();
      color = "WHITE";
      parent = null;
      isStart = false;
      isEnd = false;
      isInSolution = false;
      steps = 0;
   }

   /**
    * Sets the x coordinate of the cell
    * 
    * @param newX is the new x coordinate
    */
   public void setX(int newX)
   {
      x = newX;
   }

   /**
    * Gets the x coordinate of the cell
    * 
    * @return the x coordinate
    */
   public int getX()
   {
      return x;
   }

   /**
    * Sets the y coordinate of the cell
    * 
    * @param newY is the new y coordinate
    */
   public void setY(int newY)
   {
      y = newY;
   }

   /**
    * Gets the y coordinate of the cell
    * 
    * @return the y coordinate
    */
   public int getY()
   {
      return y;
   }

   /**
    * Sets cell so that there are 4 walls. Initializes to all solid.
    */
   public void setAllWalls()
   {
      for (int i = 0; i < walls.length; i++)
      {
         walls[i] = 1;
      }
   }

   /**
    * Opens North wall of cell if it is not edge of maze.
    */
   public void openNorthWall()
   {
      if (walls[0] != -1)
         walls[0] = 0;
   }

   /**
    * Opens East wall of cell if it is not edge of maze.
    */
   public void openEastWall()
   {
      if (walls[1] != -1)
         walls[1] = 0;
   }

   /**
    * Opens South wall of cell if it is not edge of maze.
    */
   public void openSouthWall()
   {
      if (walls[2] != -1)
         walls[2] = 0;
   }

   /**
    * Opens West wall of cell if it is not edge of maze.
    */
   public void openWestWall()
   {
      if (walls[3] != -1)
         walls[3] = 0;
   }

   /**
    * Sets the north neighbor of the cell
    * 
    * @param c is the north neighbor to set
    */
   public void setNorth(Cell c)
   {
      neighbors[0] = c;
   }

   /**
    * Sets the east neighbor of the cell
    * 
    * @param c is the east neighbor to set
    */
   public void setEast(Cell c)
   {
      neighbors[1] = c;
   }

   /**
    * Sets the south neighbor of the cell
    * 
    * @param c is the south neighbor to set
    */
   public void setSouth(Cell c)
   {
      neighbors[2] = c;
   }

   /**
    * Sets the west neighbor of the cell
    * 
    * @param c is the west neighbor to set
    */
   public void setWest(Cell c)
   {
      neighbors[3] = c;
   }

   /**
    * Gets the north neighbor of the cell
    * 
    * @return the north neighbor of the cell
    */
   public Cell getNorth()
   {
      return neighbors[0];
   }

   /**
    * Gets the east neighbor of the cell
    * 
    * @return the east neighbor of the cell
    */
   public Cell getEast()
   {
      return neighbors[1];
   }

   /**
    * Gets the south neighbor of the cell
    * 
    * @return the south neighbor of the cell
    */
   public Cell getSouth()
   {
      return neighbors[2];
   }

   /**
    * Gets the west neighbor of the cell
    * 
    * @return the west neighbor of the cell
    */
   public Cell getWest()
   {
      return neighbors[3];
   }

   /**
    * Sets the cell as the starting cell of the maze
    */
   public void setStart()
   {
      isStart = true;
   }

   /**
    * Returns T/F if the cell is the starting cell of the maze
    * 
    * @return T/F if the cell is the starting cell of the maze
    */
   public boolean isStart()
   {
      return isStart;
   }

   /**
    * Sets the cell as the ending cell of the maze
    */
   public void setEnd()
   {
      isEnd = true;
   }

   /**
    * Returns T/F if the cell is the ending cell of the maze
    * 
    * @return T/F if the cell is the ending cell of the maze
    */
   public boolean isEnd()
   {
      return isEnd;
   }

   /**
    * Sets the color of the cell
    * 
    * @param c is the color to set
    */
   public void setColor(String c)
   {
      color = c;
   }

   /**
    * Gets the color of the cell
    * 
    * @return the color of the cell
    */
   public String getColor()
   {
      return color;
   }

   /**
    * Gets the parent of the cell
    * 
    * @return the parent cell of the cell
    */
   public Cell getParent()
   {
      return parent;
   }

   /**
    * Sets the parent of the cell
    * 
    * @param c is the parent to set
    */
   public void setParent(Cell c)
   {
      parent = c;
   }

   /**
    * Sets the cell to be in the solution path
    */
   public void setIsInSolution()
   {
      isInSolution = true;
   }

   /**
    * Checks if cell is in the solution path
    * 
    * @return T/F if the cell is in the solution path
    */
   public boolean getIsInSolution()
   {
      return isInSolution;
   }

   /**
    * Gets the neighbors of the cell, 0 is north, 1 is east, 2 is south, 3 is west
    * 
    * @return the neighbors of the cell
    */
   public Cell[] getNeighbors()
   {
      return neighbors;
   }

   /**
    * Gets the walls of the cell
    * 
    * @return 1 or 0 if there are walls on corresponding side, same indexing as
    *         neighbors
    */
   public int[] getWalls()
   {
      return walls;
   }

   /**
    * Checks if cell is enclosed, meaning all walls are in place
    * 
    * @return T/F if all walls are intact
    */
   public boolean allWallsIntact()
   {
      for (int w : walls)
      {
         if (w == 0)
         {
            return false;
         }
      }
      return true;
   }

   /**
    * Compares two cells if it is neighbor and which neighbor it is
    * 
    * @param cell is the cell to compare to
    * @return which side the cell is a neighbor of, or n/a
    */
   public int compareCellLocation(Cell cell)
   {
      // cell compared is to the north
      if (getNorth() != null && getNorth().equals(cell))
      {
         return 0;
      }
      // cell compared is to the East
      else if (getEast() != null && getEast().equals(cell))
      {
         return 1;
      }
      // cell compared is to the South
      else if (getSouth() != null && getSouth().equals(cell))
      {
         return 2;
      }
      // cell compared is to the West
      else if (getWest() != null && getWest().equals(cell))
      {
         return 3;
      } else
      {
         // not neighbor
         return -10;
      }
   }

   /**
    * Sets the number of steps
    * 
    * @param s is the number of steps to set
    */
   public void setSteps(int s)
   {
      steps = s;
   }

   /**
    * Gets the number of steps
    * 
    * @return the number of steps
    */
   public int getSteps()
   {
      return steps;
   }
}