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
    // CSV文字列格納用一時退避リスト
    // private static List<String[]> csvStringList = new ArrayList<String[]>();
    // 出力用ハッシュマップ
    // private static HashMap<String, Object> outputMap = new HashMap<>();

    /*
     * CSVデータからJSONデータを作成します。
     */
    public static void main(String[] args) {

        FileHandling.getChildlenData(CSV_DIR, prefectureDirList, prefectureList);
        FileHandling.createDistinationFiles(prefectureList, JSON_DIR, ".json");
        FileHandling.createDataFilePath(prefectureDirList, subDirList);

        System.out.println(Arrays.toString(subDirList.get(2)));
        // 文字列「総数」を含む列番を抽出

    }
}