// https://github.com/JetBrains/intellij-community/blob/master/java/java-psi-api/src/com/intellij/psi/util/PsiLiteralUtil.java#L454

package com.github.intellij.gno.language;

import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiCodeFragment;

public class GnoLiteralUtil {

    private static final String QUOT = "\"\"\"";

    @Nullable
    public static String getStringLiteralContent(PsiCodeFragment expression) {
        String text = expression.getText();
        int textLength = text.length();
        if (textLength > 1 && text.charAt(0) == '\"' && text.charAt(textLength - 1) == '\"') {
            // String surrounded by single quotation marks (e.g. “text”)
            return text.substring(1, textLength - 1);
        }
        if (textLength > QUOT.length() && text.startsWith(QUOT) && text.endsWith(QUOT)) {
            // String surrounded by triple quotes (e.g. “”“text”“”)
            return text.substring(QUOT.length(), textLength - QUOT.length());
        }
        return null;
    }
}

