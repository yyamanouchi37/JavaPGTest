/*
 * クラス名：CSVConverter01
 * 機能：CSVからJSONへのドキュメント変換を行います。
 */
package Converter.csvToJson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
// import java.io.IOException;
// import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
// import java.util.stream.Stream;
import java.util.Arrays;

import Converter.Constants.GenericConstants;
import Converter.FileHandling.FileHandling;

class JSONConverter01 {

    // 都道府県名ディレクトリ名格納用リスト（ローマ字）
    private static List<String> prefectures = new ArrayList<String>();
    // CSVディレクトリ配下のディレクトリ絶対パス格納用
    private static List<String> childCsvDirs = new ArrayList<String>();
    // 総数列番格納用リスト
    // private static List<Integer> numberOfTotal = new ArrayList<Integer>();
    // 出力用ハッシュマップ
    private static HashMap<String, Object> outputMap = new HashMap<>();
    // 一時退避リスト
    private static List<String[]> csvStrings = new ArrayList<String[]>();

    /*
     * CSVデータからJSONデータを作成します。
     */
    public static void main(String[] args) {

        // CSVディレクトリ絶対パスを取得
        Path workDir = Paths.get(System.getProperty("user.dir"));
        Path csvDir = workDir.resolve(Paths.get(GenericConstants.CSV_DIR_NAME));
        // JSONディレクトリ絶対パスを取得
        Path jsonDir = workDir.resolve(Paths.get(GenericConstants.JSON_DIR_NAME));

        FileHandling.getChildlenData(csvDir, childCsvDirs, prefectures);
        FileHandling.createDistinationFiles(prefectures, jsonDir, ".json");
        prefectures.clear();

        // 年度を取得
        childCsvDirs.forEach(dir -> {

            File file = new File(dir);
            File[] subDir = file.listFiles();

            if(subDir.length < 1) {
                // 都道府県名配下のディレクトリが存在しない
            }

            for(int i = 0; i < subDir.length; i++){

                if(subDir[i].toString().contains("Populations")){

                    File[] subSub = subDir[i].listFiles();

                    for(int j = 0; j < subSub.length; j++){

                        // 出力用マップにキーを代入
                        outputMap.put(subSub[j].toString().split("_")[1].replace(".csv", ""), "");

                        // 一時退避リストにデータを代入
                        try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(
                                new FileInputStream(subSub[j])
                        ));){

                            while(br.readLine() != null){

                                String[] arr = br.readLine().split(",");

                                // 空の配列を除外。
                                if(arr.length == 0){
                                    continue;
                                }

                                System.out.println(Arrays.toString(arr));

                                // 合計列のindexを取得
                                // 合計列を削除
                                // 配列に再代入

                                // 一時配列リストに代入
                                csvStrings.add(arr);
                                
                            };

                        } catch (IOException e){

                        }

                        // System.out.println(csvStrings);
                    }
                    // System.out.println(outputMap);
                    outputMap.clear();

                }
            }
        });

        // 文字列「総数」を含む列番を抽出

    }
}