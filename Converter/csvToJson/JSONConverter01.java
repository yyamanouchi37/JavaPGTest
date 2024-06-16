/*
 * クラス名：CSVConverter01
 * 機能：CSVからJSONへのドキュメント変換を行います。
 */
package Converter.csvToJson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import Converter.Constants.GenericConstants;
import Converter.FileHandling.FileHandling;

class JSONConverter01 {

    // CSVディレクトリパス
    private static final Path CSV_DIR = GenericConstants.getWork().resolve(Paths.get(GenericConstants.CSV_DIR_NAME));
    // JSONディレクトリパス
    private static final Path JSON_DIR = GenericConstants.getWork().resolve(Paths.get(GenericConstants.JSON_DIR_NAME));
    // 都道府県名格納用リスト（ローマ字）
    private static List<String> prefectureList = new ArrayList<String>();
    // 都道府県ディレクトリパス格納用リスト
    private static List<String> prefectureDirList = new ArrayList<String>();
    // サブディレクトリ格納用リスト
    private static List<File[]> subDirList = new ArrayList<File[]>();
    // 基準ファイルパス格納用リスト
    private static List<File[]> outputFileList = new ArrayList<File[]>();
    // アウトプットマップ （年度 = 取得年度格納用リスト）
    private static TreeMap<Integer, Object> outputMap = new TreeMap<>();
    // 自治体名格納用マップ （自治体名 = 邦人/外国人マップ）
    private static LinkedHashMap<String, Object> layer1 = new LinkedHashMap<>();
    // 邦人/外国人マップ
    private static LinkedHashMap<String, Object> layer2 = new LinkedHashMap<>();
    // 年齢別人口マップ （国籍 = 性別）
    private static LinkedHashMap<String, Integer> layer3 = new LinkedHashMap<>();
    // Layer3加工用一時退避マップ
    private static LinkedHashMap<String, Integer> layer3sub = new LinkedHashMap<>();
    // CSV文字列一時退避リスト
    private static List<String[]> inputRow = new ArrayList<String[]>();
    // 削除列キーワード
    private static String[] deleteKeyWord = { "地域階層", "総数", "総世帯数" };
    // ヘッダー行インデックス
    private static int headerRowIndex = 0;
    // ヘッダー列インデックス
    private static LinkedHashMap<String, Integer> headerColumnIndex = new LinkedHashMap<>();
    // 削除列インデックス格納リスト
    private static List<Integer> deleteIndex = new ArrayList<Integer>();

    /*
     * CSVデータからJSONデータを作成します。
     */
    public static void main(String[] args) {

        FileHandling.getChildlenData(CSV_DIR, prefectureDirList, prefectureList);
        FileHandling.createDistinationFiles(prefectureList, JSON_DIR, ".json");
        FileHandling.createDataFilePath(prefectureDirList.get(1), subDirList);
        FileHandling.createBaseFileList(subDirList, GenericConstants.POPULATION_DIR_NAME, outputFileList);// str代入部分は後に配列から取得する

        outputFileList.forEach(fileArr -> {
            // 配列からパスを取得（ファイル毎に処理を実行）
            for(File path: fileArr) {

                // 行を一時退避リストに代入
                try (BufferedReader br = new BufferedReader(new FileReader(path))) {

                    String line = br.readLine();
                    while (line != null) {
                        inputRow.add(line.split(","));
                        line = br.readLine();
                    }
                    br.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // ヘッダー行の分別
                String[] header = inputRow.get(headerRowIndex);
                for (int colIdx = 0; colIdx < header.length; colIdx++) {
                    // 削除インデックスの取得
                    for (String deleteColumn : deleteKeyWord) {
                        if (header[colIdx].contains(deleteColumn)) {
                            // 削除行インデックス
                            deleteIndex.add(colIdx);
                        }
                    }
                    // 最下層レイヤーヘッダーインデックスの取得
                    if (header[colIdx].contains("日本人")) {
                        if (header[colIdx].contains("男")) {
                            headerColumnIndex.put("日本人/男", colIdx);
                        }
                        if (header[colIdx].contains("女")) {
                            headerColumnIndex.put("日本人/女", colIdx);
                        }
                        if (header[colIdx].contains("世帯数")) {
                            headerColumnIndex.put("日本人/世帯数", colIdx);
                        }
                    }

                    if (header[colIdx].contains("外国人")) {
                        if (header[colIdx].contains("男")) {
                            headerColumnIndex.put("外国人/男", colIdx);
                        }
                        if (header[colIdx].contains("女")) {
                            headerColumnIndex.put("外国人/女", colIdx);
                        }
                        if (header[colIdx].contains("世帯数")) {
                            headerColumnIndex.put("外国人/世帯数", colIdx);
                        }
                    }
                }

                // 出力用ハッシュマップの生成
                inputRow.forEach(row -> {
                    for (int colIdx = 0; colIdx < row.length; colIdx++) {
                        // 削除列をスキップ
                        for (int del : deleteIndex) {
                            if (colIdx == del) {
                                continue;
                            }
                        }

                        if (colIdx == headerColumnIndex.get("日本人/男")) {
                            layer3.put("日本人/男", Integer.valueOf(row[colIdx]));
                        }
                        if (colIdx == headerColumnIndex.get("日本人/女")) {
                            layer3.put("日本人/女", Integer.valueOf(row[colIdx]));
                        }
                        if (colIdx == headerColumnIndex.get("日本人/世帯数")) {
                            layer3.put("日本人/世帯数", Integer.valueOf(row[colIdx]));
                        }
                        if (colIdx == headerColumnIndex.get("外国人/男")) {
                            layer3.put("外国人/男", Integer.valueOf(row[colIdx]));
                        }
                        if (colIdx == headerColumnIndex.get("外国人/女")) {
                            layer3.put("外国人/女", Integer.valueOf(row[colIdx]));
                        }
                        if (colIdx == headerColumnIndex.get("外国人/世帯数")) {
                            layer3.put("外国人/世帯数", Integer.valueOf(row[colIdx]));
                        }
                    }

                    // 最下層マップを加工してLayer2に代入
                    Set<Map.Entry<String, Integer>> l3entries = layer3.entrySet();
                    for(Map.Entry<String, Integer> l3entry: l3entries){
                        // layer2:日本人
                        if(l3entry.getKey().contains("日本人")){
                            String l3Key = l3entry.getKey().split("/")[1];
                            layer3sub.put(l3Key, l3entry.getValue());
                        }
                    }

                    layer2.put("自治体コード", row[1]);
                    layer2.put("日本人",layer3sub);
                    layer3sub.clear();

                    for(Map.Entry<String, Integer> l3entry: l3entries){
                        // layer2:外国人
                        if(l3entry.getKey().contains("外国人")){
                            String l3Key = l3entry.getKey().split("/")[1];
                            layer3sub.put(l3Key, l3entry.getValue());
                        }
                    }
                    layer2.put("外国人",layer3sub);
                    layer3sub.clear();

                    layer1.put(row[2], layer2);

                });

                System.out.println(inputRow.size());// デバッグ
                inputRow.clear();

                // 取得年度マップにキーを追加 ※後ろに移動
                outputMap.put(Integer.valueOf(path.toString().split("_")[1].replace(".csv", "")), layer1);

            } // パス文字列操作END
        });

        // System.out.println(outputFileList.size());
        // outputFileList.forEach(arr -> {
        // System.out.println(Arrays.toString(arr));
        // });
        // for(Entry<Integer, Object> entry: outputMap.entrySet()){
        // System.out.println(entry);
        // }
    }

}