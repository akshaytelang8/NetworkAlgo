
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

class CountMinNode {
	long source;
	long destination;
	int size;

	public CountMinNode(long source, long destination, int flowNumber) {
		this.source = source;
		this.destination = destination;
		this.size = flowNumber;
	}
}

public class CountMin {

	List<CountMinNode> inputList = new LinkedList<CountMinNode>();
	final String inputFile = Constant.INPUT_PATH;
	final String outputFile = Constant.OUTPUT_DIR + "CountMinResult.txt";

	Set<String> actualSize = new HashSet<String>();
	int WIDTH = Constant.COUNT_MIN_WIDTH;
	int DEPTH = Constant.COUNT_MIN_DEPTH;

	int countMin[][] = new int[DEPTH][WIDTH];
	int randomArray[] = Constant.RANDOM_HASH_ARRAY;

	public void generateRandomHash() {
		randomArray = new int[DEPTH];
		Random rand = new Random();
		for (int i = 0; i < DEPTH; i++) {
			int idx = rand.nextInt(Constant.PRIME_ARRAY_HASH.length);
			randomArray[i] = Constant.PRIME_ARRAY_HASH[idx];
		}
	}

	public void countMinOnlineOperation(Long source, Long destination, int flowSize) {
		// generateRandomHash();
		for (int i = 0; i < DEPTH; i++) {
			int idx = (int) Math.abs(Long.valueOf((source ^ destination ^ randomArray[i])).hashCode() % WIDTH);
			countMin[i][idx] = countMin[i][idx] + flowSize;
		}
	}

	public int readCountMin(long source, long destination) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < DEPTH; i++) {
			int idx = (int) Math.abs(Long.valueOf((source ^ destination ^ randomArray[i])).hashCode() % WIDTH);
			if (countMin[i][idx] < min) {
				min = countMin[i][idx];
			}
		}
		return min;
	}

	public void generateInputList(Long source, Long destination, int flowSize) {
		inputList.add(new CountMinNode(source, destination, flowSize));
		countMinOnlineOperation(source, destination, flowSize);
	}

	public void printOutput() throws UnknownHostException {
		try {

			FileWriter fileWriter = new FileWriter(outputFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (CountMinNode contact : inputList) {

				Long sourceIp = contact.source;
				Long destIp = contact.destination;

				String sourceIpString = Helper.convertToString(sourceIp);
				String destIpString = Helper.convertToString(destIp);
				int output = readCountMin(sourceIp, destIp);
				bufferedWriter.write(sourceIpString + "\t" + destIpString + "\t" + contact.size + "\t" + output);
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
			System.out.println("Output File Generated");

		}

		catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	public void readInputStringData(String input) {

		String inputTraffic[] = input.split("\\s+");

		String source = inputTraffic[0];
		String destination = inputTraffic[1];
		String flowSize = inputTraffic[2];
		long sourceLong = Helper.convertToInt(source);
		long destLong = Helper.convertToInt(destination);

		generateInputList(sourceLong, destLong, Integer.parseInt(flowSize));
	}

	public static void main(String arg[]) {

		CountMin countMin = new CountMin();
		String line = null;
		System.out.println("Reading input file");
		try {
			String inputFile = countMin.inputFile;
			FileReader fr = new FileReader(inputFile);
			BufferedReader buffReader = new BufferedReader(fr);
			line = buffReader.readLine();
			while ((line = buffReader.readLine()) != null) {
				countMin.readInputStringData(line);
			}

			buffReader.close();
			System.out.println("Input file read");
			countMin.printOutput();
		} catch (Exception e) {
			System.out.println(e.toString());

		}
	}
}
