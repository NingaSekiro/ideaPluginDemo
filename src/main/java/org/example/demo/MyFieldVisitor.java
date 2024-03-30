package org.example.demo;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

public class MyFieldVisitor extends JavaRecursiveElementVisitor {
    @Override
    public void visitClass(PsiClass aClass) {
        super.visitClass(aClass);
        for(PsiField field: aClass.getAllFields()) {
            System.out.println(field.getName());
        }
    }
}