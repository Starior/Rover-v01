import java.io.FileWriter;
import java.io.IOException;

public class Rover {

    public static void main(String[] args) {
        int[][] map = {
                {3, 6, 8},
                {4, 5, 1},
                {9, 9, 9},
                {6, 9, 1}
        };
        calculateRoverPath(map);
    }

    public static void calculateRoverPath(int[][] map) {
        int[] dimensions = new int[map.length * map[0].length];
        int[] unvisitedVertex = new int[map.length * map[0].length];
        int[][] adjacencyMatrix = new int[map.length * map[0].length][map.length * map[0].length];
        int temp, minIndex, min;
        int INF = Integer.MAX_VALUE;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (j + 1 < map[0].length) {
                    adjacencyMatrix[i * map[0].length + j][i * map[0].length + j + 1] = Math.abs(Math.abs(map[i][j]) - map[i][j + 1]) + 1;
                    adjacencyMatrix[i * map[0].length + j + 1][i * map[0].length + j] = Math.abs(Math.abs(map[i][j]) - map[i][j + 1]) + 1;
                }
                if (i + 1 < map.length) {
                    adjacencyMatrix[i * map[0].length + j][i * map[0].length + j + map[0].length] = Math.abs(Math.abs(map[i][j]) - map[i + 1][j]) + 1;
                    adjacencyMatrix[i * map[0].length + j + map[0].length][i * map[0].length + j] = Math.abs(Math.abs(map[i][j]) - map[i + 1][j]) + 1;
                }
            }
        }

        for (int i = 0; i < map.length * map[0].length; i++) {
            dimensions[i] = INF;
            unvisitedVertex[i] = 1;
        }

        dimensions[0] = 0;    // start vertex

        do {
            minIndex = INF;
            min = INF;
            for (int i = 0; i < map.length * map[0].length; i++) {
                if ((unvisitedVertex[i] == 1) && (dimensions[i] < min)) {
                    min = dimensions[i];
                    minIndex = i;
                }
            }

            if (minIndex != INF) {
                for (int i = 0; i < map.length * map[0].length; i++) {
                    if (adjacencyMatrix[minIndex][i] > 0) {
                        temp = min + adjacencyMatrix[minIndex][i];
                        if (temp < dimensions[i]) {
                            dimensions[i] = temp;
                        }
                    }
                }
                unvisitedVertex[minIndex] = 0;
            }
        } while (minIndex < INF);

        int vertex[] = new int[map.length * map[0].length];
        int end = map.length * map[0].length - 1;
        vertex[0] = end + 1;
        int k = 1;
        int weight = dimensions[end];
        int weightTemp = dimensions[end];

        while (end != 0) {
            for (int i = 0; i < map.length * map[0].length; i++)
                if (adjacencyMatrix[i][end] != 0) {
                    temp = weight - adjacencyMatrix[i][end];
                    if (temp == dimensions[i]) {
                        weight = temp;
                        end = i;
                        vertex[k] = i + 1;
                        k++;
                    }
                }
        }

        int[][] result = createMarksArray(map);
        String text = "";
        for (int i = k - 1; i >= 0; i--)
            if (i > 0)
                text = text + "[" + result[vertex[i] - 1][0] + "]" + "[" + result[vertex[i] - 1][1] + "]->";
            else
                text = text + "[" + result[vertex[i] - 1][0] + "]" + "[" + result[vertex[i] - 1][1] + "]";

        try (FileWriter writer = new FileWriter("path-plan.txt", false)) {
            writer.write(text + "\n" + "steps: " + (k - 1) + "\n" + "fuel: " + weightTemp);
            writer.flush();

        } catch (IOException ignored) {

        }

    }

    public static int[][] createMarksArray(int[][] map) {
        int[][] result = new int[map.length * map[0].length][2];
        int count = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                result[count][0] = i;
                result[count][1] = j;
                count++;
            }
        }
        return result;
    }

}
