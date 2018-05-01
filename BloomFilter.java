import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BloomFilter {
	BitSet bloomfilter;
	Set<Long> set;
	Set<Long> setFirstHalf;
	List<Input> inputList;
	List<Long> distinctList;
	int m;
	int n;
	final int K = 4;
	int rand[];
	// use formula k = ln 2 (m/n)

	public void bloomFilterOnlineOperation() {
		try {
			// read file and create input list
			FileHandler filehandler = new FileHandler();
			inputList = filehandler.readFile();
			set = new LinkedHashSet<Long>();

			setFirstHalf = new LinkedHashSet<Long>();

			rand = new int[K];
			Random rnd = new Random();
			for (int i = 0; i < K; i++) {
				rand[i] = rnd.nextInt(Integer.MAX_VALUE);
			}

			// calculate m and n;
			for (Input obj : inputList) {
				long source = Helper.convertToInt(obj.source);
				// long destination = Helper.convertToInt(obj.destination);
				set.add(source);
			}

			distinctList = new ArrayList<Long>(set);

			n = set.size() / 2;
			m = (int) (Math.pow(2, K) * n);
			bloomfilter = new BitSet(m);
			// int count = 0;
			for (int idx = 0; idx < distinctList.size() / 2; idx++) {
				Long source = distinctList.get(idx);
				setFirstHalf.add(source);
				for (int i = 0; i < K; i++) {
					int index = source.hashCode() ^ rand[i];
					index = index % m;
					index = Math.abs(index);
					bloomfilter.set(index);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void readBloomFilter() {

		int falsePostive = 0;
		for (int idx = distinctList.size() / 2; idx < distinctList.size(); idx++) {
			Long source = distinctList.get(idx);
			Boolean flag = true;

			for (int i = 0; i < K; i++) {
				int index = source.hashCode() ^ rand[i];
				index = index % m;
				index = Math.abs(index);
				flag = flag && bloomfilter.get(index);

			}

			if (flag && !setFirstHalf.contains(source)) {
				falsePostive++;
			}
		}

		System.out.println("Number of elements as false positive: " + falsePostive);
		System.out.println("Number of elements encoded: " + setFirstHalf.size());
		System.out.println("Number of elements tested: " + setFirstHalf.size());
		System.out.println("Number of hash functions: " + K);
		double ratio = (double) falsePostive / setFirstHalf.size();
		BigDecimal d = new BigDecimal(ratio);
		System.out.println("Theoratical False positive ratio " + Math.pow(.5, K));
		System.out.println("Actual False positive ratio " + d.toString());

	}

	public static void main(String[] args) {
		BloomFilter bloomFilter = new BloomFilter();
		bloomFilter.bloomFilterOnlineOperation();
		bloomFilter.readBloomFilter();

	}

}
