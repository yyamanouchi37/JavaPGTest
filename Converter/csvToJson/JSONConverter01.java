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
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

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
    private static List<String[]> csvStringList = new ArrayList<String[]>();
    // 出力用ハッシュマップ
    // private static HashMap<String, Object> outputMap = new HashMap<>();

    /*
     * CSVデータからJSONデータを作成します。
     */
    public static void main(String[] args) {

        FileHandling.getChildlenData(CSV_DIR, prefectureDirList, prefectureList);
        FileHandling.createDistinationFiles(prefectureList, JSON_DIR, ".json");
        FileHandling.createDataFilePath(prefectureDirList, subDirList);
        FileHandling.createBaseFileList(subDirList, GenericConstants.POPULATION_DIR_NAME, baseFileList);// str代入部分は後に配列から取得する

        // リスト中にディレクトリが存在する場合、スキップ
        baseFileList.forEach(file -> {

            for(int i = 0; i < file.length; i++){

                try(BufferedReader br = new BufferedReader(new FileReader(file[i]))) {

                    String line = br.readLine();

                    while(line != null){

                        String[] row = line.split(",");
                        csvStringList.add(row);
                        // System.out.println(Arrays.toString(row));
                        line = br.readLine();

                    }
                    br.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }
        });
        System.out.println(Arrays.toString(csvStringList.get(810)));
        // 文字列「総数」を含む列番を抽出

    }
}