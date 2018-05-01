import java.io.*;
import java.util.*;

public class FileHandler {

	public List<Input> readFile() {

		String line = null;
		List<Input> list = new ArrayList<Input>();
		String inputPath = Constant.INPUT_PATH;
		try {
			System.out.println("Reading Input");

			FileReader fileReader = new FileReader(inputPath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			boolean isFirst = true;
			while ((line = bufferedReader.readLine()) != null) {
				if (isFirst) {
					isFirst = false;
					continue;
				}
				list.add(convert(line));

			}

			bufferedReader.close();
			System.out.println("Read Complete");
			return list;

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + inputPath + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + inputPath + "'");
		} finally {
			return list;
		}
	}

	public void writeFile(List<String> list, String outputFile) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {

			StringBuilder sb = new StringBuilder();
			for (String s : list) {
				sb.append(s);
				sb.append("\n");
			}
			String output = sb.toString();
			fw = new FileWriter(outputFile);
			bw = new BufferedWriter(fw);
			bw.write(output);

			bw.close();
			fw.close();
			System.out.println("Output File Generated");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public Input convert(String ip) {
		Input obj = new Input();
		StringBuilder sb = new StringBuilder();
		for (String s : ip.split(" ")) {
			if (s == "" || s.length() == 0) {
				continue;
			} else {
				sb.append(s);
				sb.append("#");
			}

		}
		String s = sb.toString();
		String arr[] = s.split("#");
		obj.source = arr[0];
		obj.destination = arr[1];
		obj.size = arr[2];
		return obj;
	}

}