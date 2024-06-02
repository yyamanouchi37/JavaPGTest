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
    // CSV文字列格納用一時退避リスト
    private static List<String> columnStringList = new ArrayList<String>();
    // 出力用一時退避ハッシュマップ
    private static HashMap<String, Object> outputMap = new LinkedHashMap<>();
    // 行インデックスカウント
    private static int rowIndex = -1;
    // 削除列インデックス格納用リスト
    private static List<Integer> deleteIndex = new ArrayList<Integer>();

    /*
     * CSVデータからJSONデータを作成します。
     */
    public static void main(String[] args) {

        FileHandling.getChildlenData(CSV_DIR, prefectureDirList, prefectureList);
        FileHandling.createDistinationFiles(prefectureList, JSON_DIR, ".json");
        FileHandling.createDataFilePath(prefectureDirList, subDirList);
        FileHandling.createBaseFileList(subDirList, GenericConstants.POPULATION_DIR_NAME, baseFileList);// str代入部分は後に配列から取得する

        baseFileList.forEach(path -> {

            // 出力用ハッシュマップにキー(西暦年)を代入
            for (int i = 0; i < path.length; i++) {

                // 東京都
                if (path[i].toString().contains("tokyo")) {

                    try (BufferedReader br = new BufferedReader(new FileReader(path[i]))) {

                        rowIndex++;
                        String line = br.readLine();

                        // 行操作
                        while (line != null) {

                            String[] beforeArr = line.split(",");

                            if (rowIndex == 0) {

                                for (int j = 0; j < beforeArr.length; j++) {

                                    // 削除列のインデックスを取得
                                    if (beforeArr[j].contains("地域階層") || beforeArr[j].contains("総数")) {
                                        deleteIndex.add(j);
                                    }

                                }

                            }

                            String[] deleteIndexies = deleteIndex.toArray(new String[deleteIndex.size()]);

                            //
                            for(int j = 0; j < beforeArr.length; j++){

                                if(Arrays.toString(deleteIndexies).contains(String.valueOf(j))){
                                    continue;
                                }

                                columnStringList.add(beforeArr[j]);
                            }

                        }

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                // 西暦年を抽出
                String key = path[i].toString()
                        .split("/")[path[i].toString()
                                .split("/").length - 1]
                        .split("_")[1]
                        .replace(".csv", "").trim();

                outputMap.put(key, "");
            }

            System.out.println("");
            outputMap.keySet().forEach(str -> {

                System.out.println(str);
            });
            // 一時退避マップの初期化
            outputMap.clear();

        });

    }
}