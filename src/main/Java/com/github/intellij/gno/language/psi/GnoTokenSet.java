package com.github.intellij.gno.language.psi;

import com.intellij.psi.tree.TokenSet;

public interface GnoTokenSet {
    TokenSet IDENTIFIERS = TokenSet.create(GnoTypes.KEY);

    TokenSet COMMENTS = TokenSet.create(GnoTypes.COMMENT);
}
