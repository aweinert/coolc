package net.alexweinert.coolc.program.information;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

public class DeclaredClassSignature extends ClassSignature {
    private class SignatureBuilder extends ASTVisitor {
        private final List<Attribute> attributes = new LinkedList<>();
        private final List<MethodSignature> methodSignatures = new LinkedList<>();

        @Override
        public void visitAttributePostorder(Attribute attribute) {
            this.attributes.add(attribute);
        }

        @Override
        public void visitMethodPostorder(Method method) {
            this.methodSignatures.add(MethodSignature.create(method));
        }
    }

    /**
     * The given class may not contain any two attributes of the same name and no two methods of the same name
     */
    public DeclaredClassSignature create(Class classNode) {
        final SignatureBuilder signatureBuilder = new SignatureBuilder();
        classNode.acceptVisitor(signatureBuilder);

        final Map<IdSymbol, Attribute> attributeMap = new HashMap<>();
        for (Attribute attribute : signatureBuilder.attributes) {
            attributeMap.put(attribute.getName(), attribute);
        }

        final Map<IdSymbol, MethodSignature> methodMap = new HashMap<>();
        for (MethodSignature signature : signatureBuilder.methodSignatures) {
            methodMap.put(signature.getIdentifier(), signature);
        }

        return new DeclaredClassSignature(attributeMap, methodMap);
    }

    private DeclaredClassSignature(Map<IdSymbol, Attribute> attributes, Map<IdSymbol, MethodSignature> methods) {
        super(attributes, methods);
    }
}
