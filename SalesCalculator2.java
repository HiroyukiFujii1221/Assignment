package jp.alhinc.fujii_hiroyuki.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SalesCalculator2 {

	// ファイルを読み込むメソッド
	public static boolean fileRead
	(String dirPath, HashMap<String, String> names, HashMap<String, Long> sales, String str, String name, String strA) {
		File FileRead;
		FileReader fr;
		BufferedReader br;

		FileRead = new File(dirPath + File.separator + name);

		if (!FileRead.exists()) {
			System.out.println(strA + "定義ファイルが存在しません");
			return false;
		}
		try {
			fr = new FileReader(FileRead);
			br = new BufferedReader(fr);
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return false;
		}
		try {
			String branch;

			while ((branch = br.readLine()) != null) {
				String[] item1 = branch.split(",");
				int b = item1.length;
				if (b != 2) {
					System.out.println(strA + "定義ファイルのフォーマットが不正です");
					return false;
				}
				if (item1[0].matches(str)) {
					names.put(item1[0], item1[1]);
					sales.put(item1[0], (long) 0);
				} else {
					System.out.println(strA + "定義ファイルのフォーマットが不正です");
					return false;
				}
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return false;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return false;
				}
			}
		}
		return true;
	}

	// ファイルを書き出すメソッド
	public static boolean fileWrite
	(String dirPath, HashMap<String, String> names, HashMap<String, Long> sales,String name) {
		ArrayList<Map.Entry<String, Long>> entries;

		entries = new ArrayList<Map.Entry<String, Long>>(sales.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Long>>() {
			@Override
			public int compare(Entry<String, Long> entry1, Entry<String, Long> entry2) {
				return ((Long) entry2.getValue()).compareTo((Long) entry1.getValue());
			}
		});

		File Aggregation;
		FileWriter fw = null;
		BufferedWriter bw = null;
		Aggregation = new File(dirPath + File.separator + name);

		try {
			fw = new FileWriter(Aggregation);
			bw = new BufferedWriter(fw);
			for (Entry<String, Long> s : entries) {
				String p = String.valueOf(s.getKey());
				bw.write(s.getKey() + "," + names.get(p) + "," + s.getValue());
				bw.newLine();
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return false;
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return false;
				}
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		int a = args.length;
		if (a != 1) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, Long> map2 = new HashMap<String, Long>();

		if(!fileRead(args[0], map1, map2, "[0-9]{3}", "branch.lst", "支店")){
			return;
		};

		HashMap<String, String> map3 = new HashMap<String, String>();
		HashMap<String, Long> map4 = new HashMap<String, Long>();

		if(!fileRead(args[0], map3, map4, "[a-zA-Z0-9]{8}", "commodity.lst", "商品")){
			return;
		};

		// 指定した条件に合致する売上げファイルのみを取り出す
		File dir = new File(args[0]);
		File[] files = dir.listFiles();
		ArrayList<File> rcdFiles = new ArrayList<File>();

		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().matches("^[0-9]{8}.rcd$") && files[i].isFile()) {
				rcdFiles.add(files[i]);
			} else {
			}
		}
		// 連番処理 for文の回数はファイルの数よりひとつ少ないので注意
		for (int i = 0; i < rcdFiles.size() - 1; i++) {
			String[] numberString = rcdFiles.get(i).getName().split("\\.");
			int salesNumber1 = Integer.parseInt(numberString[0]);

			numberString = rcdFiles.get(i + 1).getName().split("\\.");
			int salesNumber2 = Integer.parseInt(numberString[0]);

			if (salesNumber2 - salesNumber1 != 1) {
				System.out.println("売上ファイル名が連番になっていません");
				return;
			}
		}

		// 売上げファイルの個数と同じ回数だけ↓の処理を繰り返す
		for (int i = 0; i < rcdFiles.size(); i++) {

			String line;
			// 売上げファイルの情報を格納するためのArrayList
			ArrayList<String> salesArray = new ArrayList<String>();

			FileReader fr = null;
			BufferedReader br = null;
			try {
				// rcdFilesはリスト型でもっている状態なので、getメソッドを使う
				fr = new FileReader(rcdFiles.get(i));
				br = new BufferedReader(fr);

				while ((line = br.readLine()) != null) {
					salesArray.add(line);
				}
				// 売上ファイルの行数が3行以上または2行以下の場合のエラー処理
				if (!(salesArray.size() == 3)) {
					System.out.println(rcdFiles.get(i).getName() + "のフォーマットが不正です");
					return;
				}
				// 売上ファイルの3行目が数字かどうかの判別
				if (!salesArray.get(2).matches("^[0-9]+$")) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
				if (!(map1.containsKey(salesArray.get(0)))) {
					System.out.println(rcdFiles.get(i).getName() + "の支店コードが不正です");
					return;
				}
				if (!(map3.containsKey(salesArray.get(1)))) {
					System.out.println(rcdFiles.get(i).getName() + "の商品コードが不正です");
					return;
				}

			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						System.out.println("予期せぬエラーが発生しました");
						return;
					}
				}
			}

			// 支店コード別に売上げ金額を加算する処理
			long k = Long.parseLong(salesArray.get(2));
			long l1 = map2.get(salesArray.get(0));
			long m1 = k + l1;
			String ms1 = String.valueOf(m1);

			// 支店別集計合計金額が10桁を越えた場合の処理
			if (ms1.matches("[0-9]{11,}")) {
				System.out.println("合計金額が10桁を超えました");
				return;
			}
			map2.put(salesArray.get(0), m1);

			// 商品コード別に売上げ金額を加算する処理
			long l2 = map4.get(salesArray.get(1));
			long m2 = k + l2;
			String ms2 = String.valueOf(m2);

			// 商品別集計合計金額が10桁を越えた場合の処理
			if (ms2.matches("[0-9]{11,}")) {
				System.out.println("合計金額が10桁を超えました");
				return;
			}
			map4.put(salesArray.get(1), m2);
		}
		// 支店別集計ファイルの作成
		if(!fileWrite(args[0], map1, map2, "branch.out")){
			return;
		};

		// 商品別集計ファイルの作成
		if(!fileWrite(args[0], map3, map4, "commodity.out")){
			return;
		};

	}
}