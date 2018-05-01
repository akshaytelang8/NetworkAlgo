
public class Helper {

	public static long convertToInt(String ip) {

		String[] arr = ip.split("\\.");
		long result = 0;

		for (int i = 0; i < arr.length; i++) {
			int power = 3 - i;
			int ipadd = Integer.parseInt(arr[i]);
			result += ipadd * Math.pow(256, power);
		}

		return result;
	}

	public static String convertToString(long i) {

		return ((i >> 24) & 0xFF) + "." +

				((i >> 16) & 0xFF) + "." +

				((i >> 8) & 0xFF) + "." +

				(i & 0xFF);
	}
}
