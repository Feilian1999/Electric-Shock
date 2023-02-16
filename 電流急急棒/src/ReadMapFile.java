import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
//ÅªÀÉ
public class ReadMapFile {
	private static Scanner input;
	private static Map<Point, Integer> recordMap;
	private static Random r = new Random();
	// open file clients.txt
	public static void openFile(String fileName) {
		try {
			input = new Scanner(new File(fileName));
		} catch (IOException ioException) {
			System.err.println("Error opening file. Terminating.");
			System.exit(1);
		}
	}

	// read record from file
	public static void readRecords() {
		try {
			recordMap = new HashMap<Point, Integer>();
			int row = 0;
			int col = 0;
			while (input.hasNextLine()) { // file reading
				String[] values = input.nextLine().split("	");
				for (String str : values) {
					recordMap.put(new Point(col, row), Integer.parseInt(str));
					if(Integer.parseInt(str) == 1) {
						int ranNum = r.nextInt(4);
						if(ranNum == 0) {
							recordMap.put(new Point(col, row), 3);
						}
					}
					col = col + 1;
				}
				row++;
				col = 0;
			}

		} catch (NoSuchElementException elementException) {
			System.err.println("File improperly formed. Terminating.");
		} catch (IllegalStateException stateException) {
			System.err.println("Error reading from file. Terminating.");
		}
	} // end method readRecords

	// close file and terminate application
	public static void closeFile() {
		if (input != null)
			input.close();
	}

	public static Map<Point, Integer> getRecordMap() {
		return recordMap;
	}
}
