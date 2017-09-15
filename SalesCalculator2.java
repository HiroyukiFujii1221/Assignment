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
	public static void main(String[]args) {
		int a = args.length;
		if(a != 1){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}

		File branchFile = new File(args[0] +File.separator+ "branch.lst");
			if(!branchFile.exists()) {
				System.out.println("支店定義ファイルが存在しません");
				return;
			}

		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, Long> map2 = new HashMap<String, Long>();

		FileReader fr;
		BufferedReader br = null;

		try{
			fr = new FileReader(branchFile);
			br = new BufferedReader(fr);
			String branch;

			while((branch = br.readLine()) != null) {
				String[] item1 = branch.split(",");
				int b = item1.length;
				if(b != 2){
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;
				}
				if(item1[0].matches("[0-9]{3}")){
					map1.put(item1[0] , item1[1]);
					map2.put(item1[0], (long)0);
				} else {
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;
				}
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		} finally{
			if (br != null){
	            try {
	                br.close();
	            } catch (IOException e) {
	                System.out.println("予期せぬエラーが発生しました");
	                return;
	            }
			}
		}

		File commodityFile = new File(args[0] +File.separator+ "commodity.lst");
			if(!commodityFile.exists()) {
				System.out.println("商品定義ファイルが存在しません");
				return;
			}

		HashMap<String, String> map3 = new HashMap<String, String>();
		HashMap<String, Long> map4 = new HashMap<String, Long>();

		try {
			fr = new FileReader(commodityFile);
			br = new BufferedReader(fr);
			String commodity;

			while((commodity =br.readLine()) != null) {
				String[] item2 = commodity.split(",");
				int b = item2.length;

				if(b != 2){
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
				if(item2[0].matches("[a-zA-Z0-9]{8}")){
				map3.put(item2[0], item2[1]);
				map4.put(item2[0], (long)0);
				} else{
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
			}
		} catch (IOException e) {
			System.out.println("商品定義ファイルのフォーマットが不正です");
			return;
		} finally{
			if (br != null){
	            try {
	                br.close();
	            } catch (IOException e) {
	                System.out.println("予期せぬエラーが発生しました");
	                return;
	            }
			}
		}

		//指定した条件に合致する売上げファイルのみを取り出す
		File dir = new File(args[0]);
		File[] files = dir.listFiles();
		ArrayList<File> rcdFiles = new ArrayList<File>();

		for (int i = 0; i < files.length; i++){
			if(files[i].getName().matches("^[0-9]{8}.rcd$") && files[i].isFile()){
				rcdFiles.add(files[i]);
			} else{
			}
		}
		//連番処理
		for(int i = 0; i <rcdFiles.size() - 1; i++){
			String[] numberString = rcdFiles.get(i).getName().split("\\.");
			int salesNumber1 = Integer.parseInt(numberString[0]);

			numberString = rcdFiles.get(i + 1).getName().split("\\.");
			int salesNumber2 = Integer.parseInt(numberString[0]);

			if(salesNumber2 - salesNumber1 != 1){
				System.out.println("売上ファイル名が連番になっていません");
				return;
			}

		}



		//売上げファイルの個数と同じ回数だけ↓の処理を繰り返す
		for(int i = 0; i < rcdFiles.size(); i++){

			String line;
			//売上げファイルの情報を格納するためのArrayList
			ArrayList<String> salesArray = new ArrayList<String>();

			try{
				//rcdFilesはリスト型でもっている状態なので、getメソッドを使う
				fr = new FileReader(rcdFiles.get(i));
				br = new BufferedReader(fr);

				while((line = br.readLine()) != null){
					salesArray.add(line);
				}
				//売上ファイルの行数が3行以上または2行以下の場合のエラー処理
				if(!(salesArray.size() == 3)){
					System.out.print(rcdFiles.get(i).getName() + "のフォーマットが不正です");
					return;
				}
				//売上ファイルの3行目が数字かどうかの判別
				if(!salesArray.get(2).matches("^[0-9]+$")){
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
				if(!(map1.containsKey(salesArray.get(0)))){
					System.out.println(rcdFiles.get(i).getName() + "の支店コードが不正です");
					return;
				}
				if(!(map3.containsKey(salesArray.get(1)))){
					System.out.println(rcdFiles.get(i).getName() + "の商品コードが不正です");
					return;
				}

			} catch(IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			} finally{
				if (br != null){
		            try {
		                br.close();
		            } catch (IOException e) {
		            	System.out.println("予期せぬエラーが発生しました");
		            	return;
		            }
				}
			}

			//支店コード別に売上げ金額を加算する処理
			long k = Long.parseLong(salesArray.get(2));
			long l1 = map2.get(salesArray.get(0));
			long m1 = k + l1;
			String ms1 = String.valueOf(m1);

			//支店別集計合計金額が10桁を越えた場合の処理
			if(ms1.matches("[0-9]{11,}")){
				System.out.println("合計金額が10桁を超えました");
				return;
			}
			map2.put(salesArray.get(0), m1);

			//商品コード別に売上げ金額を加算する処理
			long l2 = map4.get(salesArray.get(1));
			long m2 = k + l2;
			String ms2 = String.valueOf(m2);

			//商品別集計合計金額が10桁を越えた場合の処理
			if(ms2.matches("[0-9]{11,}")){
				System.out.println("合計金額が10桁を超えました");
				return;
			}
			map4.put(salesArray.get(1), m2);
			}

	    ArrayList<Map.Entry<String,Long>> entries1 =
	              new ArrayList<Map.Entry<String,Long>>(map2.entrySet());
	        Collections.sort(entries1, new Comparator<Map.Entry<String,Long>>() {

	            @Override
	            public int compare(
	                  Entry<String,Long> entry1, Entry<String,Long> entry2) {
	                return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
	            }
	        });

		    //支店別集計ファイルの作成
			File branchAggregation = new File(args[0] +File.separator+ "branch.out");
			FileWriter fw = null;
			BufferedWriter bw = null;

			try {
				fw = new FileWriter(branchAggregation);
				bw = new BufferedWriter(fw);

				for (Entry<String,Long> s : entries1) {
					String p = String.valueOf(s.getKey());
						bw.write(s.getKey() + ","+ map1.get(p) +","+ s.getValue());
						bw.newLine();
				}
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			} finally{
				if (bw != null){
		            try {
		                bw.close();
		            } catch (IOException e) {
		            	System.out.println("予期せぬエラーが発生しました");
		            	return;
		            }
				}
				if (fw != null){
		            try {
		                fw.close();
		            } catch (IOException e) {
		            	System.out.println("予期せぬエラーが発生しました");
		            	return;
		            }
				}
			}

		ArrayList<Map.Entry<String,Long>> entries2 =
	              new ArrayList<Map.Entry<String,Long>>(map4.entrySet());
	        Collections.sort(entries2, new Comparator<Map.Entry<String,Long>>() {

	            @Override
	            public int compare(
	                  Entry<String,Long> entry1, Entry<String,Long> entry2) {
	                return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
	            }
	    });
	      //商品別集計ファイルの作成
	        File commodityAggregation = new File(args[0] +File.separator+ "commodity.out");

			try {
				fw = new FileWriter(commodityAggregation);
				bw = new BufferedWriter(fw);

				for (Entry<String,Long> s : entries2) {
					String p = String.valueOf(s.getKey());
						bw.write(s.getKey() + ","+ map3.get(p) +","+ s.getValue());
						bw.newLine();
				}
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			} finally {
				if (bw != null){
		            try {
		                bw.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
				}
				if (fw != null){
		            try {
		                fw.close();
		            } catch (IOException e) {
		            	System.out.println("予期せぬエラーが発生しました");
		            	return;
		            }
				}
			}
	}
}