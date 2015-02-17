package net.alexweinert.coolc.program.signatures;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

public class DeclaredClassSignature {
    final private Map<IdSymbol, Attribute> attributes;
    final private Map<IdSymbol, MethodSignature> methods;

    private class SignatureBuilder extends ASTVisitor {
        private final List<Attribute> attributes = new LinkedList<>();
        private final List<MethodSignature> methodSignatures = new LinkedList<>();
        private final Class containingClass;

        public SignatureBuilder(Class classNode) {
            this.containingClass = classNode;
        }

        @Override
        public void visitAttributePostorder(Attribute attribute) {
            this.attributes.add(attribute);
        }

        @Override
        public void visitMethodPostorder(Method method) {
            this.methodSignatures.add(MethodSignature.create(method));
        }
    }

    public DeclaredClassSignature create(Class classNode) {
        final SignatureBuilder signatureBuilder = new SignatureBuilder(classNode);
        classNode.acceptVisitor(signatureBuilder);

        final Map<IdSymbol, Attribute> attributeMap = new HashMap<>();
        for (Attribute attribute : signatureBuilder.attributes) {
            attributeMap.put(attribute.getName(), attribute);
        }

        final Map<IdSymbol, MethodSignature> methods = new HashMap<>();
        for (MethodSignature signature : signatureBuilder.methodSignatures) {
            methods.put(signature.getIdentifier(), signature);
        }

        return new DeclaredClassSignature(attributes, methods);
    }

    private DeclaredClassSignature(Map<IdSymbol, Attribute> attributes, Map<IdSymbol, MethodSignature> methods) {
        this.attributes = attributes;
        this.methods = methods;
    }
}
