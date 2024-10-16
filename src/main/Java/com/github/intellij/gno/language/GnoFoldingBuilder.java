package com.github.intellij.gno.language;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiCodeFragment;
import com.intellij.util.containers.ContainerUtil;
import com.github.intellij.gno.language.psi.GnoProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class GnoFoldingBuilder extends FoldingBuilderEx implements DumbAware {
    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root,
                                                          @NotNull Document document,
                                                          boolean quick) {
        // Initialize the group of folding regions that will expand/collapse together.
        FoldingGroup group = FoldingGroup.newGroup(GnoAnnotator.SIMPLE_PREFIX_STR);
        // Initialize the list of folding regions
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        root.accept(new PsiRecursiveElementWalkingVisitor() {

            public void visitElement(@NotNull PsiCodeFragment literalExpression) {
                super.visitElement(literalExpression);

                String value = GnoLiteralUtil.getStringLiteralContent(literalExpression);
                if (value != null &&
                        value.startsWith(GnoAnnotator.SIMPLE_PREFIX_STR + GnoAnnotator.SIMPLE_SEPARATOR_STR)) {
                    Project project = literalExpression.getProject();
                    String key = value.substring(
                            GnoAnnotator.SIMPLE_PREFIX_STR.length() + GnoAnnotator.SIMPLE_SEPARATOR_STR.length()
                    );
                    // find SimpleProperty for the given key in the project
                    GnoProperty simpleProperty = ContainerUtil.getOnlyItem(GnoUtil.findProperties(project, key));
                    if (simpleProperty != null) {
                        // Add a folding descriptor for the literal expression at this node.
                        descriptors.add(new FoldingDescriptor(literalExpression.getNode(),
                                new TextRange(literalExpression.getTextRange().getStartOffset() + 1,
                                        literalExpression.getTextRange().getEndOffset() - 1),
                                group, Collections.singleton(simpleProperty)));
                    }
                }
            }
        });

        return descriptors.toArray(FoldingDescriptor.EMPTY_ARRAY);
    }

    /**
     * Gets the Simple Language 'value' string corresponding to the 'key'
     *
     * @param node Node corresponding to PsiLiteralExpression containing a string in the format
     *             SIMPLE_PREFIX_STR + SIMPLE_SEPARATOR_STR + Key, where Key is
     *             defined by the Simple language file.
     */
    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        if (node.getPsi() instanceof PsiCodeFragment psiLiteralExpression) {
            String text = GnoLiteralUtil.getStringLiteralContent(psiLiteralExpression);
            if (text == null) {
                return null;
            }

            String key = text.substring(GnoAnnotator.SIMPLE_PREFIX_STR.length() +
                    GnoAnnotator.SIMPLE_SEPARATOR_STR.length());

            GnoProperty simpleProperty = ContainerUtil.getOnlyItem(
                    GnoUtil.findProperties(psiLiteralExpression.getProject(), key)
            );
            if (simpleProperty == null) {
                return StringUtil.THREE_DOTS;
            }

            String propertyValue = simpleProperty.getValue();
            // IMPORTANT: keys can come with no values, so a test for null is needed
            // IMPORTANT: Convert embedded \n to backslash n, so that the string will look
            // like it has LF embedded in it and embedded " to escaped "
            if (propertyValue == null) {
                return StringUtil.THREE_DOTS;
            }

            return propertyValue
                    .replaceAll("\n", "\\n")
                    .replaceAll("\"", "\\\\\"");
        }

        return null;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return true;
    }
}
