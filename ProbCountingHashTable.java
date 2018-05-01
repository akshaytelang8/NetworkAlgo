import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProbCountingHashTable {
	Map<Long, BitSet> map;
	Map<Long, Set<Long>> actualMap;
	List<String> outputList;
	final String OUTPUT_FILE = Constant.OUTPUT_DIR + "ProbCountingResult.txt";

	public void probCountingHashTableOnlineOperation() {
		try {

			// read file and create input list
			FileHandler filehandler = new FileHandler();
			List<Input> inputList = filehandler.readFile();

			// create two level hash table using java
			map = new LinkedHashMap<Long, BitSet>();
			actualMap = new LinkedHashMap<Long, Set<Long>>();

			for (Input obj : inputList) {
				Long source = Helper.convertToInt(obj.source);
				Long destination = Helper.convertToInt(obj.destination);
				int index = (destination.hashCode() % Constant.PROB_COUNT_HASH_TABLE_VECTOR_SIZE);
				index = Math.abs(index);

				if (map.containsKey(source)) {
					BitSet bitset = map.get(source);

					if (!bitset.get(index)) {
						bitset.set(index);
						map.put(source, bitset);
					}
				} else {
					BitSet bitset = new BitSet(Constant.PROB_COUNT_HASH_TABLE_VECTOR_SIZE);
					bitset.set(index);
					map.put(source, bitset);
				}
				Set<Long> set = null;
				if (actualMap.containsKey(source)) {
					set = actualMap.get(source);
				} else {
					set = new HashSet<Long>();
				}
				set.add(destination);
				actualMap.put(source, set);

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public int getEstimate(BitSet bitset) {
		int res = 0;
		int oneBitsV = bitset.cardinality();
		int zeroBitsU = Constant.PROB_COUNT_HASH_TABLE_VECTOR_SIZE - oneBitsV;
		int m = bitset.size();
		double v = (double) zeroBitsU / m;
		// n = -m In V
		res = (int) (-Constant.PROB_COUNT_HASH_TABLE_VECTOR_SIZE * Math.log(v));
		return res;

	}

	public void readProbCountingHashTable() {
		outputList = new ArrayList<String>();
		for (long source : map.keySet()) {
			BitSet bitset = map.get(source);
			int spread = getEstimate(bitset);
			int actual = actualMap.get(source).size();
			String space = "\t";
			String output = Helper.convertToString(source) + space + actual + space + spread;
			// String output = Helper.convertToString(source) + space +
			// bitset.cardinality();

			// System.out.println(output);
			outputList.add(output);
		}
	}

	public void writeOutputFile() {
		FileHandler obj = new FileHandler();
		obj.writeFile(outputList, OUTPUT_FILE);
	}

	public static void main(String[] args) {
		ProbCountingHashTable probCountingHashTable = new ProbCountingHashTable();
		probCountingHashTable.probCountingHashTableOnlineOperation();
		probCountingHashTable.readProbCountingHashTable();
		probCountingHashTable.writeOutputFile();
	}

}
