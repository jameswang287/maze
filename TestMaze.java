package kam_wang.cs146.project3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TestMaze
{
   class TestMazeStruct
   {
      String maze;
      int dimension;

      TestMazeStruct(String maze, int dimension)
      {
         this.maze = maze;
         this.dimension = dimension;
      }
   }

   ArrayList<Maze> mazes = new ArrayList<Maze>();

   /**
    * Tests maze parse input files
    */
   void loadASCIIFiles() throws NumberFormatException, IOException
   {
      int dimension = -1;
      File m4 = new File("maze4.txt");
      File m6 = new File("maze6.txt");
      File m8 = new File("maze8.txt");
      File m10 = new File("maze10.txt");
      File m20 = new File("maze20.txt");
      ArrayList<File> files = new ArrayList<>();
      files.add(m4);
      files.add(m6);
      files.add(m8);
      files.add(m10);
      files.add(m20);

      for (File file : files)
      {
         StringBuilder mazeASCII = new StringBuilder();
         if (file.isFile())
         {
            file = file.getAbsoluteFile();
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            dimension = Integer.parseInt(bufferedReader.readLine().split(" ")[0]);

            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
               mazeASCII.append(line + "\n");
            }
            bufferedReader.close();
            Maze maze = new Maze(dimension);
            maze.generateMazeFrom(mazeASCII.toString());
            mazes.add(maze);
         }
      }
   }

   /**
    * Tests read lines to maze
    */
   @Test
   void testGenerateFromASCII() throws NumberFormatException, IOException
   {
      int dimension = -1;
      File[] files = new File("sampleInput").listFiles();

      for (File file : files)
      {
         StringBuilder mazeASCII = new StringBuilder();
         if (file.isFile())
         {
            file = file.getAbsoluteFile();
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            dimension = Integer.parseInt(bufferedReader.readLine().split(" ")[0]);

            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
               mazeASCII.append(line + "\n");
            }

            bufferedReader.close();

            Maze maze = new Maze(dimension);
            maze.generateMazeFrom(mazeASCII.toString());

            assertEquals(maze.getUnsolvedMaze().trim(), mazeASCII.toString().trim());
         }
      }
   }

   /**
    * Tests solving read in mazes via DFS
    */
   @Test
   void testDFS() throws NumberFormatException, IOException
   {
      loadASCIIFiles();
      for (Maze maze : mazes)
      {
         maze.solveWithDFS();
      }
   }

   /**
    * Tests solving read in mazes via BFS
    */
   @Test
   void testBFS() throws NumberFormatException, IOException
   {
      loadASCIIFiles();
      for (Maze maze : mazes)
      {
         maze.solveWithBFS();
      }
   }

   /**
    * Tests generating and solving mazes
    */
   @Test
   void testGenerateAndSolveDFSandBFS()
   {
      Maze a1 = new Maze(4, 1);
      // long startTime = System.nanoTime();
      System.out.println("New maze before generation:");
      System.out.println(a1.getUnsolvedMaze());
      a1.generateMazeDFS();
      // long timeAtGenerate = System.nanoTime();

      System.out.println("New maze after generation:");
      System.out.println(a1.getUnsolvedMaze());

      // long timeBeforeDFSSolve = System.nanoTime();
      a1.solveWithDFS();
      // long timeAfterDFSSolve = System.nanoTime();

      System.out.println("Maze after DFS solve:");
      System.out.println(a1.getSolvedMazeWithNumbers());
      System.out.println(a1.getSolvedMazeWithHashmarks());
      assert (a1.getVisitedCells() == 16);

      a1.reset();
      // long timeBeforeBFS = System.nanoTime();
      a1.solveWithBFS();
      // long timeAfterBFS = System.nanoTime();

      System.out.println("Maze after BFS solve print:");
      System.out.println(a1.getSolvedMazeWithNumbers());
      System.out.println(a1.getSolvedMazeWithHashmarks());
      assert (a1.getVisitedCells() == 16);
      /*
       * System.out.println("Nano Generate time: " + (timeAtGenerate - startTime));
       * System.out.println("Nano BFS Solve time: " + (timeAfterBFS - timeBeforeBFS));
       * System.out.println("Nano DFS Solve time: " + (timeAfterDFSSolve -
       * timeBeforeDFSSolve));
       */
   }
}
