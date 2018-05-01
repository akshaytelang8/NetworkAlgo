import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CountMinOLD {

	public static final int WIDTH = 100000;
	public static final int DEPTH = 50;
	public static final int RAND = 256;

	public int countMin[][];
	public int randomArr[];
	List<Input> inputList;

	final String OUTPUT_FILE = "E:/Network Algo/Project/CountMinResult.txt";
	List<String> outputList;

	public CountMinOLD() {
		countMin = new int[DEPTH][WIDTH];
		randomArr = new int[RAND];
		Random rand = new Random();
		for (int i = 0; i < RAND; i++) {
			randomArr[i] = rand.nextInt(Integer.MAX_VALUE);
		}

		FileHandler filehandler = new FileHandler();
		inputList = filehandler.readFile();

	}

	public void onlineOperation() {
		for (Input obj : inputList) {
			long source = Helper.convertToInt(obj.source);
			long destination = Helper.convertToInt(obj.destination);
			int size = Integer.parseInt(obj.size);

			countMinOnlineOperation(source, destination, size);
		}
	}

	public void offlineOperation() {
		outputList = new ArrayList<String>();
		for (Input obj : inputList) {
			long source = Helper.convertToInt(obj.source);
			long destination = Helper.convertToInt(obj.destination);
			// int size = Integer.parseInt(obj.size);
			int size = countMinReadOperation(source, destination);
			String space = "    ";
			String output = Helper.convertToString(source) + space + Helper.convertToString(destination) + space
					+ obj.size + "  " + size;

			// System.out.println(output);
			outputList.add(output);
		}
	}

	public void countMinOnlineOperation(Long source, Long destination, int size) {
		for (int i = 0; i < DEPTH; i++) {
			int randIndex = destination.hashCode() % RAND;
			randIndex = Math.abs(randIndex);
			int index = (int) ((randomArr[randIndex] ^ source.hashCode()) % WIDTH);
			index = Math.abs(index);
			countMin[i][index] = size;
			// countMin[i][index] += size;
		}

	}

	public int countMinReadOperation(Long source, Long destination) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < DEPTH; i++) {
			int randIndex = destination.hashCode() % RAND;
			randIndex = Math.abs(randIndex);
			int index = (int) ((randomArr[randIndex] ^ source.hashCode()) % WIDTH);
			index = Math.abs(index);
			int val = countMin[i][index];
			min = Math.min(min, val);
		}
		return min;
	}

	public void writeOutputFile() {
		FileHandler obj = new FileHandler();
		obj.writeFile(outputList, OUTPUT_FILE);
	}

	public static void main(String[] args) {
		CountMinOLD countmin = new CountMinOLD();
		countmin.onlineOperation();
		countmin.offlineOperation();
		countmin.writeOutputFile();
	}
}
