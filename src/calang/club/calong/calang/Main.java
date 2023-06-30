package club.calong.calang;

public class Main {

    public static void main(String[] args) {

        CalangParser parser = new CalangParser("calang/test.calang");
        // 1. 解析脚本文件
        parser.parse();
    }
}
