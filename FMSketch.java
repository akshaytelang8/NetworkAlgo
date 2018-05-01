import java.util.*;

public class FMSketch {

	BitSet fmSketch[];
	int randomArr[];
	List<Input> inputList;
	List<String> outputList;
	final String OUTPUT_FILE = Constant.OUTPUT_DIR + "FMSketchResult.txt";
	Map<Long, Set<Long>> actualMap;

	public FMSketch() {
		fmSketch = new BitSet[Constant.FM_SKETCH_SIZE];
		for (int i = 0; i < Constant.FM_SKETCH_SIZE; i++) {
			fmSketch[i] = new BitSet(Constant.FM_SKETCH_WIDTH);
		}

	}

	public void fmSketchOnlineOperation() {

		try {
			// read file and create input list
			FileHandler filehandler = new FileHandler();
			inputList = filehandler.readFile();

			setRandomArray();

			actualMap = new HashMap<Long, Set<Long>>();

			for (Input obj : inputList) {
				Long source = Helper.convertToInt(obj.source);
				Long destination = Helper.convertToInt(obj.destination);

				// hash to fm
				int randomArrayIndex = destination.hashCode() % Constant.FM_SKETCH_WIDTH;// s
				randomArrayIndex = Math.abs(randomArrayIndex);
				Integer sourceHash = (int) (source ^ randomArr[randomArrayIndex]);
				int actualIndex = sourceHash.hashCode() % Constant.FM_SKETCH_SIZE;// m
				int sketchNumber = Math.abs(actualIndex);

				// geometric hash for destination
				int des = destination.hashCode();
				// calc zeros
				int countZeros = Integer.numberOfLeadingZeros(des);
				countZeros = countZeros % Constant.FM_SKETCH_WIDTH;
				countZeros = Math.abs(countZeros);

				// set bit
				fmSketch[sketchNumber].set(countZeros);

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

	public double getEstimate(Long source) {
		double res = 0;
		double count = 0;

		for (int random : randomArr) {
			Integer XOR = (int) (source ^ random);
			int val = XOR.hashCode() % Constant.FM_SKETCH_SIZE;
			int sketchindex = Math.abs(val);
			BitSet FM = fmSketch[sketchindex];
			int firstZero = FM.nextClearBit(0);
			count += firstZero;

		}

		res = (double) count / Constant.FM_SKETCH_SIZE;
		return res;
	}

	public void readFMSktech() {
		outputList = new ArrayList<String>();
		Set<Long> inputSet = new LinkedHashSet<Long>();
		for (Input obj : inputList) {
			inputSet.add(Helper.convertToInt(obj.source));
		}

		for (Long source : inputSet) {
			double val = getEstimate(source);
			// calculation
			// formula 1
			double power = (double) val / Constant.FM_SKETCH_SIZE;
			power = Math.pow(.5, power);
			double sigma = 1.0 / 0.773;
			double N_s = sigma * Constant.FM_SKETCH_WIDTH * power;

			// formula2
			int m = Constant.FM_SKETCH_SIZE;
			int s = Constant.FM_SKETCH_WIDTH;
			// int N_cap = inputSet.size();
			int N_cap = actualMap.get(source).size();
			double N_sf = m * s / (m - s);
			double temp = (double) (N_s / s) - (N_cap / m);
			int finalAns = (int) (temp * N_sf);

			String space = "\t";
			String output = Helper.convertToString(source) + space + actualMap.get(source).size() + space + N_s;
			outputList.add(output);
		}
	}

	public void setRandomArray() {
		randomArr = new int[Constant.VIRTUAL_BITMAP_VIRTUAL_BITSIZE];// s
		Random rand = new Random();
		for (int i = 0; i < randomArr.length; i++) {
			// int index = rand.nextInt(Constant.PRIME_ARRAY_HASH.length);
			// index = index % Constant.PRIME_ARRAY_HASH.length;
			// randomArr[i] = Constant.PRIME_ARRAY_HASH[index];
			randomArr[i] = rand.nextInt(Integer.MAX_VALUE);
		}

	}

	public void writeOutputFile() {
		FileHandler obj = new FileHandler();
		obj.writeFile(outputList, OUTPUT_FILE);
	}

	public static void main(String[] args) {
		FMSketch fmSkt = new FMSketch();
		fmSkt.fmSketchOnlineOperation();
		fmSkt.readFMSktech();
		fmSkt.writeOutputFile();
	}

}
