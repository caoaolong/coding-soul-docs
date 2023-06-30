package club.calong.calang.util;

import java.util.regex.Pattern;

public interface Constants {

    Pattern FUNCTION_PATTERN = Pattern.compile("[\\w_]+\\(.*\\)");
}
