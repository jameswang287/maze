package kam_wang.cs146.project3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * Models a Maze with methods to generate or solve via DFS and BFS
 * 
 * @author Rico Kam and James Wang
 *
 */
public class Maze
{
   private Cell maze[][];
   private Cell startCell;
   private Cell endCell;
   private int row;
   private int numberOfCells;
   private int dimension;
   private int solveVisitedCells;
   private Random random;

   /**
    * Models a nxn Maze with a dimension
    * 
    * @param dim is the dimension of the maze
    */
   public Maze(int dim)
   {
      this(dim, 1);
   }

   /**
    * Models a nxn Maze with a dimension and seed value for generation
    * 
    * @param dim  is the dimension of the maze
    * @param seed is the specified seed value for maze generation
    */
   public Maze(int dim, int seed)
   {
      random = new java.util.Random(seed);
      dimension = dim;
      numberOfCells = dimension * dimension;
      maze = new Cell[dimension][dimension];
      row = 1;
      // initialize maze with dimensions
      for (int i = 0; i < dimension; i++)
      {
         for (int j = 0; j < dimension; j++)
         {
            maze[i][j] = new Cell();
            maze[i][j].setX(i);
            maze[i][j].setY(j);
         }
         row = row + dimension - 1;
      }
      startCell = maze[0][0];
      endCell = maze[dimension - 1][dimension - 1];
      solveVisitedCells = 0;
   }

   /**
    * Generates a maze from given ascii maze
    * 
    * @param asciiMaze is the ascii maze to parse
    */
   public void generateMazeFrom(String asciiMaze)
   {
      char[][] mazeGrid = new char[dimension * 2 + 1][dimension * 2 + 1];
      String[] rows = asciiMaze.trim().split("\n");
      for (int i = 0; i < rows.length; ++i)
      {
         char[] row = rows[i].toCharArray();
         for (int j = 0; j < row.length; ++j)
         {
            mazeGrid[j][i] = row[j];
         }
      }

      for (int gridX = 0; gridX < dimension; ++gridX)
      {
         for (int gridY = 0; gridY < dimension; ++gridY)
         {
            int x = (gridX * 2) + 1;
            int y = (gridY * 2) + 1;

            int topX = x;
            int topY = y - 1;
            int leftX = x - 1;
            int leftY = y;
            int rightX = x + 1;
            int rightY = y;
            int bottomX = x;
            int bottomY = y + 1;

            boolean[] openings = { (mazeGrid[topX][topY] != '-'), (mazeGrid[leftX][leftY] != '|'),
                  (mazeGrid[rightX][rightY] != '|'), (mazeGrid[bottomX][bottomY] != '-') };

            for (int i = 0; i < 4; ++i)
            {
               Cell cell = maze[gridY][gridX];
               if (openings[i] && i == 0)
               {
                  cell.openNorthWall();
               } else if (openings[i] && i == 1)
               {
                  cell.openWestWall();
               } else if (openings[i] && i == 2)
               {
                  cell.openEastWall();
               } else if (openings[i] && i == 3)
               {
                  cell.openSouthWall();
               }
            }
         }
      }

      startCell.setStart();
      endCell.setEnd();
   }

   /**
    * Generates a maze with DFS and random wall selection
    */
   public void generateMazeDFS()
   {
      // First set up the maze with bounds considerations
      for (int i = 0; i < dimension; i++)
      {
         for (int j = 0; j < dimension; j++)
         {
            Cell c = maze[i][j];
            if (i == 0) // upper bound of maze
            {
               c.setNorth(null);
               c.getWalls()[0] = -1; // -1 is sentinel value meaning maze edge
            } else
            {
               c.setNorth(maze[i - 1][j]); // setting the up wall
               c.getNeighbors()[0] = maze[i - 1][j]; // setting up neighbor
            }
            if (j == 0) // right bound of maze
            {
               c.setWest(null);
               c.getWalls()[3] = -1;
            } else
            {
               c.setWest(maze[i][j - 1]);
               c.getNeighbors()[3] = maze[i][j - 1];
            }
            if (i == dimension - 1) // bottom bound of maze
            {
               c.setSouth(null);
               c.getWalls()[2] = -1;
            } else
            {
               c.setSouth(maze[i + 1][j]);
               c.getNeighbors()[2] = maze[i + 1][j];
            }
            if (j == dimension - 1) // left bound of maze
            {
               c.setEast(null);
               c.getWalls()[1] = -1;
            } else
            {
               c.setEast(maze[i][j + 1]);
               c.getNeighbors()[1] = maze[i][j + 1];
            }
         }
      }
      startCell.setStart();
      endCell.setEnd();
      Stack<Cell> cellStack = new Stack<>();
      int totalCells = numberOfCells;
      Cell currentCell = maze[0][0];
      int visitedCells = 1;
      while (visitedCells < totalCells)
      {
         ArrayList<Cell> neighborsIntact = new ArrayList<>();
         for (int i = 0; i < currentCell.getNeighbors().length; i++)
         {
            Cell neighbor = currentCell.getNeighbors()[i];
            if (neighbor != null)
            {
               if (neighbor.allWallsIntact())
               {
                  neighborsIntact.add(neighbor);
               }
            }
         }
         if (neighborsIntact.size() != 0)
         {
            int rand = (int) (random.nextDouble() * neighborsIntact.size());
            Cell cellToConnect = neighborsIntact.get(rand);
            if (currentCell.compareCellLocation(cellToConnect) == 0)
            {// cell to connect is above currentCell
               currentCell.openNorthWall();
               cellToConnect.openSouthWall();
            } else if (currentCell.compareCellLocation(cellToConnect) == 1)
            { // cell to connect is to the right of
              // currentCell
               currentCell.openEastWall();
               cellToConnect.openWestWall();
            } else if (currentCell.compareCellLocation(cellToConnect) == 2)
            { // cell to connect is below
              // currentCell
               currentCell.openSouthWall();
               cellToConnect.openNorthWall();
            } else
            { // cell to connect is to the left of currentCell
               currentCell.openWestWall();
               cellToConnect.openEastWall();
            }
            cellStack.push(currentCell);
            currentCell = cellToConnect;
            visitedCells++;
         } else
         {
            if (!cellStack.isEmpty())
            {
               currentCell = cellStack.pop();
            }
         }
      }
   }

   /**
    * Solves the maze with BFS
    */
   public void solveWithBFS()
   {
      int traversalSteps = 0;
      Queue<Cell> q = new LinkedList<>();
      q.add(startCell);
      while (!q.isEmpty() && !q.peek().isEnd())
      {
         Cell u = q.remove();
         u.setSteps(traversalSteps);
         for (int i = 0; i < u.getNeighbors().length; i++)
         {
            Cell v = u.getNeighbors()[i];
            int direction = u.compareCellLocation(v);
            if (!(v == null) && (u.getWalls()[direction] == 0) && v.getColor().equals("WHITE"))
            {
               v.setColor("GREY");
               v.setParent(u);
               q.add(v);
               v.setSteps(traversalSteps + 1);
            }
         }
         traversalSteps += 1;
         u.setColor("BLACK");
      }
      endCell.setColor("BLACK");
   }

   /**
    * Solves the maze with DFS
    */
   public void solveWithDFS()
   {
      int traversalSteps = 0;
      Stack<Cell> s = new Stack<>();
      s.push(startCell);
      while (!s.isEmpty() && !s.peek().isEnd())
      {
         Cell u = s.pop();
         for (int i = 0; i < u.getNeighbors().length; i++)
         {
            Cell v = u.getNeighbors()[i];
            int direction = u.compareCellLocation(v);
            if (v != null && (u.getWalls()[direction] == 0) && v.getColor().equals("WHITE"))
            {
               v.setColor("GREY");
               v.setParent(u);
               s.push(v);
            }
         }
         u.setColor("BLACK");
         u.setSteps(traversalSteps++);
      }
      endCell.setSteps(traversalSteps);
      endCell.setColor("BLACK");
   }

   /**
    * Function returns the string representation of the maze unsolved A Cell is
    * represented as: Top N +-+ Middle W | | E +-+ Bottom S Keeps printing top two
    * until lowest level of maze matrix
    * 
    * @return the string of the unsolved maze
    */
   public String getUnsolvedMaze()
   {
      String unsolvedMaze = "";
      // first print the top cellLine
      int layersToConstruct = 2; // Will change to 3 when considering bottom.
      for (int i = 0; i < dimension; i++)
      {
         if (i == dimension - 1)
         {
            layersToConstruct = 3; // this means it has reached the bottom to construct.
         }
         for (int cellLine = 1; cellLine <= layersToConstruct; cellLine++)
         {
            if (cellLine == 1)
            {
               unsolvedMaze += "+";
            }
            if (cellLine == 2)
            {
               unsolvedMaze += "|";
            }
            if ((cellLine == 3) && (i == dimension - 1))
            {
               unsolvedMaze += "+";
            }
            for (int j = 0; j < dimension; j++)
            {
               Cell c = maze[i][j];
               if (cellLine == 1)
               {
                  if ((c.getWalls()[0] != 0) && (!c.isStart()))
                  {
                     unsolvedMaze += "-";
                  } else
                  {
                     unsolvedMaze += " ";
                  }
                  unsolvedMaze += "+";
               } else if (cellLine == 2)
               {
                  unsolvedMaze += " ";
                  if (c.getWalls()[1] != 0)
                     unsolvedMaze += "|";
                  else
                     unsolvedMaze += " ";
               } else if ((cellLine == 3) && (i == dimension - 1))
               {
                  if ((c.getWalls()[2] != 0) && (!c.isEnd()))
                  {
                     unsolvedMaze += "-";
                  } else
                  {
                     unsolvedMaze += " ";
                  }
                  unsolvedMaze += "+";
               }
            }
            unsolvedMaze += "\n";
         }
      }
      return unsolvedMaze;
   }

   /**
    * Generates string with a numerical solved maze showing all steps
    * 
    * @return the numerical solved maze
    */
   public String getSolvedMazeWithNumbers()
   {
      String solved = "";
      int layersToConstruct = 2; // Will change to 3 when considering bottom.
      for (int i = 0; i < dimension; i++)
      {
         if (i == dimension - 1)
            layersToConstruct = 3;
         for (int cellLine = 1; cellLine <= layersToConstruct; cellLine++)
         {
            if (cellLine == 1)
            {
               solved = solved + "+";
            }
            if (cellLine == 2)
            {
               solved = solved + "|";
            }
            if ((cellLine == 3) && (i == dimension - 1))
            {
               solved = solved + "+";
            }
            for (int j = 0; j < dimension; j++)
            {
               Cell c = maze[i][j];
               if (cellLine == 1)
               {
                  if ((c.getWalls()[0] != 0) && (!c.isStart()))
                  {
                     solved = solved + "-";
                  } else
                  {
                     solved = solved + " ";
                  }
                  solved = solved + "+";
               } else if (cellLine == 2)
               {
                  if ((c != null) && c == (maze[0][0]))
                  {// solution
                     solved = solved + "0";
                     solveVisitedCells++;
                  } else if (c.getParent() == null)
                  {
                     solved = solved + " ";
                  } else
                  {// solution
                     if (c.getColor() == "BLACK")
                     {
                        solved = solved + c.getSteps() % 10;
                     } else
                     {
                        solved = solved + " ";
                     }
                     solveVisitedCells++;
                  }
                  if (c.getWalls()[1] != 0)
                  {
                     solved = solved + "|";
                  } else
                  {
                     solved = solved + " ";
                  }
               } else if ((cellLine == 3) && (i == dimension - 1))
               {
                  if ((c.getWalls()[2] != 0) && (!c.isEnd()))
                  {
                     solved = solved + "-";
                  } else
                  {
                     solved = solved + " ";
                  }
                  solved = solved + "+";
               }
            }
            solved = solved + "\n";
         }
      }
      return solved;
   }

   /**
    * Generates string with a hash mark solved maze with path only
    * 
    * @return the hash mark solved maze
    */
   public String getSolvedMazeWithHashmarks()
   {
      Cell currentCell = maze[dimension - 1][dimension - 1];
      int pathLength = 0;
      String pathCoordinates = "";
      while (currentCell != null)
      {
         currentCell.setIsInSolution();
         currentCell = currentCell.getParent();
         pathLength++;
         if (currentCell != null)
         {
            pathCoordinates = " (" + currentCell.getX() + "," + currentCell.getY() + ")" + pathCoordinates;
         }
      }
      String solved = "";
      int layersToConstruct = 2;
      for (int i = 0; i < dimension; i++)
      {
         if (i == dimension - 1)
         {
            layersToConstruct = 3;
         }
         for (int cellLine = 1; cellLine <= layersToConstruct; cellLine++)
         {
            if ((cellLine == 1) || (cellLine == 3) && (i == dimension - 1))
            {
               solved = solved + "+";
            }
            if (cellLine == 2)
            {
               solved = solved + "|";
            }
            for (int j = 0; j < dimension; j++)
            {
               Cell v = maze[i][j];
               if (cellLine == 1)
               {
                  if (v.isStart())
                  {
                     solved = solved + " ";
                  } else if ((v.getWalls()[0] != 0))
                  {
                     solved = solved + "-";
                  } else if (v.getIsInSolution() && v.getNorth() != null && v.getNorth().getIsInSolution())
                  {
                     solved = solved + "#";
                  } else
                  {
                     solved = solved + " ";
                  }
                  solved = solved + "+";
               } else if (cellLine == 2)
               {
                  if (v.getIsInSolution())
                  {
                     solved = solved + "#";
                  } else
                  {
                     solved = solved + " ";
                  }
                  if (v.getWalls()[1] != 0)
                  {
                     solved = solved + "|";
                  } else
                  {
                     solved = solved + " ";
                  }
               } else if ((cellLine == 3) && (i == dimension - 1))
               {
                  if (v.isEnd())
                  {
                     solved = solved + " ";
                  } else if ((v.getWalls()[2] != 0))
                  {
                     solved = solved + "-";
                  } else if (v.getIsInSolution())
                  {
                     solved = solved + "#";
                  } else
                  {
                     solved = solved + " ";
                  }
                  solved = solved + "+";
               }
            }
            solved = solved + "\n";
         }
      }
      solved = solved + "Path:" + pathCoordinates + "\n";
      solved = solved + "Length of path: " + pathLength + "\n";
      solved = solved + "Visited Cells: " + solveVisitedCells;
      return solved;
   }

   /**
    * Resets the maze for solving in another way
    */
   public void reset()
   {
      solveVisitedCells = 0;
      for (int i = 0; i < dimension; i++)
      {
         for (int j = 0; j < dimension; j++)
         {
            maze[i][j].setColor("WHITE");
            maze[i][j].setParent(null);
            maze[i][j].setSteps(0);
         }
      }
      startCell = maze[0][0];
      endCell = maze[dimension - 1][dimension - 1];
   }

   /**
    * Gets visited cells number
    * 
    * @return the number of visited cells
    */
   public int getVisitedCells()
   {
      return solveVisitedCells;
   }
}