package club.calong.calang.util;

import java.util.regex.Pattern;

public interface Constants {

    Pattern FUNCTION_PATTERN = Pattern.compile("^[A-Za-z_]\\w+\\(.*\\)");

    Pattern STRING_PATTERN = Pattern.compile("^\"[^\"]*\"$");

    int EAX_IDX = 0;
    int EBX_IDX = 1;
    int ECX_IDX = 2;
    int EDX_IDX = 3;
}
