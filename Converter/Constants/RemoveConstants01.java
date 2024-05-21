/*
 * クラス名：RemoveConstants
 * 機能：東京都人口CSV変換除外対象定数
 */
package Converter.Constants;

public class RemoveConstants01 {

    // コンストラクタ
    private RemoveConstants01(){
        //
    }

    // 除外対象行（0:"総数", 1:"区部", 2:"市部", 3:"郡部", 4:"島部")
    public static final String[] REMOVEROWS = {"総数", "区部", "市部", "郡部", "島部"};

    // 除外対象列
    public static final String REMOVECOLUMNS = "総数";

    // 除外文字列
    public static final String REMOVESUF = "（B）";
}
