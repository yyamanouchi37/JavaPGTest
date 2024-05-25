/*
 * クラス名：GenericConstants
 * 機能：汎用定数
 */
package Converter.Constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GenericConstants {

    // コンストラクタ
    private GenericConstants() {
        //
    }

    // ワークディレクトリパス
    private static final Path WORK_DIR = Paths.get(System.getProperty("user.dir"));

    // ログ出力切り替え用スイッチ
    public static final int LOG_PTN = 0;

    // 東京人口ヘッダー行数
    public static final int TPH_ROWCOUNT = 2;

    // CSV配備ディレクトリ名
    public static final String CSV_DIR_NAME = "TestDataCSV";

    // JSON配備ディレクトリ名
    public static final String JSON_DIR_NAME = "CreatedJSON";

    // 人口ディレクトリ名
    public static final String POPULATION_DIR_NAME = "Populations";

    public static Path getWork() {

        return WORK_DIR;

    }
}
