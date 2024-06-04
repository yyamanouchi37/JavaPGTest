/*
 * クラス名：CSVConverter01
 * 機能：CSVからJSONへのドキュメント変換を行います。
 */
package Converter.csvToJson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.nio.file.Files;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

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
    private static List<File[]> baseFileList = new ArrayList<File[]>();
    // アウトプットマップ （自治体名 = 取得年度格納用リスト）
    private static LinkedHashMap<String, Object> outputMap = new LinkedHashMap<>();
    // 取得年度格納用マップ （年度 = 邦人/外国人マップ）
    private static LinkedHashMap<Integer, Object> layer1 = new LinkedHashMap<>();
    // 年齢別人口マップ （性別 = 人口）
    private static LinkedHashMap<String, Integer> Layer3 = new LinkedHashMap<>();
    // CSV文字列一時退避リスト
    private static List<String[]> inputRow = new ArrayList<String[]>();
    // 削除列インデックス格納リスト
    private static List<Integer> deleteIndex = new ArrayList<Integer>();
    // 削除列キーワード
    private static List<String> delkwcol = Arrays.asList("地域階層", "総数");

    /*
     * CSVデータからJSONデータを作成します。
     */
    public static void main(String[] args) {

        FileHandling.getChildlenData(CSV_DIR, prefectureDirList, prefectureList);
        FileHandling.createDistinationFiles(prefectureList, JSON_DIR, ".json");
        FileHandling.createDataFilePath(prefectureDirList, subDirList);
        FileHandling.createBaseFileList(subDirList, GenericConstants.POPULATION_DIR_NAME, baseFileList);// str代入部分は後に配列から取得する

        baseFileList.forEach(pathArr -> {

            // 配列からパスを取得
            for (File path: pathArr) {

                // 取得年度マップにキーを追加
                layer1.put(parseInt(path.toString().split("_")[1].replace(".csv", "")), null);

                // 一時退避リストにデータ文字列を代入
                try (BufferedReader br = new BufferedReader(new FileReader(path))) {

                    String line = br.readLine();
                    inputRow.add(line.split(","));

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                for (int i = 0; i < inputRow.size(); i++) {

                    // ヘッダー行から削除列のインデックスを取得
                    if (i == 0) {

                        String[] row = inputRow.get(i);
                        for (int j = 0; j < row.length; j++) {

                            for(String str: delkwcol){

                                if(row[j].contains(str)){

                                    deleteIndex.add(j);

                                }

                            }

                        }

                    }

                    // 不要な列を削除

                }

                // String str = path.toString();
                // if (i == 0 && str.contains("tokyo") && str.contains(GenericConstants.POPULATION_DIR_NAME)) {

                // }
            } // パス文字列操作END
        });
    }

    private static Integer parseInt(String replace) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseInt'");
    }
}