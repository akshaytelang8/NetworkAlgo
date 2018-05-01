import java.util.*;

public class TwoLevelHashTable {

	Map<Long, List<Long>> map;
	final String OUTPUT_FILE = Constant.OUTPUT_DIR + "TwoLevelHashResult.txt";
	List<String> outputList;

	public void twoLevelHashTableOnlineOperation() {
		try {

			// read file and create input list
			FileHandler filehandler = new FileHandler();
			List<Input> inputList = filehandler.readFile();

			// create two level hash table using java
			map = new LinkedHashMap<Long, List<Long>>();

			for (Input obj : inputList) {
				long source = Helper.convertToInt(obj.source);
				long destination = Helper.convertToInt(obj.destination);

				if (map.containsKey(source)) {
					List<Long> list = map.get(source);
					if (!list.contains(destination)) {
						list.add(destination);
						map.put(source, list);
					}
				} else {
					List<Long> list = new ArrayList<Long>();
					list.add(destination);
					map.put(source, list);
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void readTwoLevelHashTable() {
		outputList = new ArrayList<String>();
		for (long source : map.keySet()) {
			List<Long> list = map.get(source);
			int spread = list.size();
			String space = "\t";
			String output = Helper.convertToString(source) + space + spread + space + spread;
			// System.out.println(output);
			outputList.add(output);
		}
	}

	public void writeOutputFile() {
		FileHandler obj = new FileHandler();
		obj.writeFile(outputList, OUTPUT_FILE);
	}

	public void writeoutput(List<String> list) {

	}

	public static void main(String[] args) {
		TwoLevelHashTable twoLevelHashTable = new TwoLevelHashTable();
		twoLevelHashTable.twoLevelHashTableOnlineOperation();
		twoLevelHashTable.readTwoLevelHashTable();
		twoLevelHashTable.writeOutputFile();
	}
}
