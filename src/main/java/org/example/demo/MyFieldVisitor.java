package org.example.demo;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementVisitor;

public class MyFieldVisitor extends PsiRecursiveElementVisitor {
    @Override
    public void visitElement(@NotNull PsiElement element) {
        super.visitElement(element);
        if (element instanceof PsiField) {
            PsiField field = (PsiField) element;
            // 在这里处理字段
            System.out.println(field.getName());
        }
    }
}
