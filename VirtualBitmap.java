import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class VirtualBitmap {
	BitSet bitset;
	int randomArr[];
	List<Input> inputList;
	List<String> outputList;
	final String OUTPUT_FILE = Constant.OUTPUT_DIR + "VirtualBitMap.txt";
	Map<Long, Set<Long>> actualMap;

	public void virtualbitmapOnlineOperation() {

		try {
			// read file and create input list
			FileHandler filehandler = new FileHandler();
			inputList = filehandler.readFile();

			// set m and s
			setRandomArray();

			bitset = new BitSet(Constant.VIRTUAL_BITMAP_ACTUAL_ARRAYSIZE);// m
			actualMap = new HashMap<Long, Set<Long>>();

			for (Input obj : inputList) {
				Long source = Helper.convertToInt(obj.source);
				Long destination = Helper.convertToInt(obj.destination);

				// hash to virtual bitmap
				int randomArrayIndex = destination.hashCode() % Constant.VIRTUAL_BITMAP_VIRTUAL_BITSIZE;// s
				randomArrayIndex = Math.abs(randomArrayIndex);
				Integer sourceHash = (int) (source ^ randomArr[randomArrayIndex]);
				int actualIndex = sourceHash.hashCode() % Constant.VIRTUAL_BITMAP_ACTUAL_ARRAYSIZE;// m
				actualIndex = Math.abs(actualIndex);
				bitset.set(actualIndex);

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

	public int getEstimate(Long source) {
		int res = 0;
		BitSet virtualBitset = new BitSet(Constant.VIRTUAL_BITMAP_VIRTUAL_BITSIZE);
		for (int i = 0; i < randomArr.length; i++) {
			Integer index = (int) (source ^ randomArr[i]) % Constant.VIRTUAL_BITMAP_ACTUAL_ARRAYSIZE;
			index = Math.abs(index);
			if (bitset.get(index))
				virtualBitset.set(i);

		}

		double s = Constant.VIRTUAL_BITMAP_VIRTUAL_BITSIZE;

		double Vm = bitset.size() - bitset.cardinality();
		double V_m = Vm / Constant.VIRTUAL_BITMAP_ACTUAL_ARRAYSIZE;
		double Vs = virtualBitset.size() - virtualBitset.cardinality();
		double V_s = Vs / s;

		res = (int) (s * Math.log(V_m) - s * Math.log(V_s));
		return res;

	}

	public void readVirtualBitmap() {
		outputList = new ArrayList<String>();
		Set<Long> inputSet = new LinkedHashSet<Long>();
		for (Input obj : inputList) {
			inputSet.add(Helper.convertToInt(obj.source));
		}

		for (Long source : inputSet) {
			int spread = getEstimate(source);
			String space = "\t";
			String output = Helper.convertToString(source) + space + actualMap.get(source).size() + space + spread;
			outputList.add(output);
		}
	}

	public void setRandomArray() {
		randomArr = new int[Constant.VIRTUAL_BITMAP_VIRTUAL_BITSIZE];// s
		Random rand = new Random();
		for (int i = 0; i < randomArr.length; i++) {
			int index = rand.nextInt(Constant.PRIME_ARRAY_HASH.length);
			index = index % Constant.PRIME_ARRAY_HASH.length;
			randomArr[i] = Constant.PRIME_ARRAY_HASH[index];
			// randomArr[i] = rand.nextInt(Integer.MAX_VALUE);
		}

	}

	public void writeOutputFile() {
		FileHandler obj = new FileHandler();
		obj.writeFile(outputList, OUTPUT_FILE);
	}

	public static void main(String[] args) {
		VirtualBitmap virtualBitmap = new VirtualBitmap();
		virtualBitmap.virtualbitmapOnlineOperation();
		virtualBitmap.readVirtualBitmap();
		virtualBitmap.writeOutputFile();

	}

}
