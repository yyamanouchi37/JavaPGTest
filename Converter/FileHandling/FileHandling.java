/*
 * クラス名：GenarateDF
 * 格納用ファイル生成クラス
 */
package Converter.FileHandling;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class FileHandling {

    /*
     * コンストラクタ
     */
    private FileHandling() {
        //
    }

    /*
     * データディレクトリ直下の情報を取得します。
     *
     * @param dir 読み込みルートディレクトリ
     *
     * @param chidDirs 直下のディレクトリパス
     *
     * @param names ファイル名リスト
     */
    public static void getChildlenData(Path dir, List<String> childDirs, List<String> names) {

        // データディレクトリ直下のディレクトリ名を転写
        try (Stream<Path> stream = Files.list(Paths.get(dir.toString()))) {

            stream.forEach(subDir -> {

                String str = subDir.toString();
                childDirs.add(str);
                String[] arr = str.split("/");
                names.add(arr[arr.length - 1]);

            });

        } catch (IOException e) {

            System.out.println(e + "：規定の場所にファイルがセットされていません。");

        }

    }

    /*
     * 保存先のファイルを生成します。
     *
     * @param fns ファイル名リスト：file names
     *
     * @param dir JSON格納先トップディレクトリパス：directory
     *
     * @param ext 拡張子：extension
     */
    public static void createDistinationFiles(List<String> fns, Path dir, String ext) {

        fns.forEach(fn -> {

            Path path = dir.resolve(Paths.get(fn + ext));

            if (!Files.exists(path)) {
                try {

                    Files.createFile(path);

                } catch (IOException e) {

                    System.out.println(e + "：ファイルの作成に失敗しました。");

                }
            }

        });
    }

    /*
     * データファイル取得元リストを生成
     *
     * @param input
     *
     * @param output
     */
    public static void createDataFilePath(List<String> input, List<File[]> output) {
        input.forEach(dir -> {

            File file = new File(dir);
            File[] subDir = file.listFiles();

            output.add(subDir);

        });
    }

}
