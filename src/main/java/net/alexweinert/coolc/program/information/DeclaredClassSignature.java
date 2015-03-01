package net.alexweinert.coolc.program.information;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

public class DeclaredClassSignature extends ClassSignature {
    private static class SignatureBuilder extends ASTVisitor {
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
    public static DeclaredClassSignature create(Class classNode) {
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

    public static DeclaredClassSignature createObjectSignature() {
        final Map<IdSymbol, Attribute> attributes = new HashMap<>();
        final Map<IdSymbol, MethodSignature> methods = new HashMap<>();

        methods.put(IdTable.getInstance().addString("abort"), MethodSignature.createObjectAbortSignature());
        methods.put(IdTable.getInstance().addString("type_name"), MethodSignature.createObjectTypeNameSignature());
        methods.put(IdTable.getInstance().addString("copy"), MethodSignature.createObjectCopySignature());
        return new DeclaredClassSignature(attributes, methods);
    }

    public static DeclaredClassSignature createIntSignature() {
        return new DeclaredClassSignature(Collections.<IdSymbol, Attribute> emptyMap(),
                Collections.<IdSymbol, MethodSignature> emptyMap());
    }

    public static DeclaredClassSignature createBoolSignature() {
        return new DeclaredClassSignature(Collections.<IdSymbol, Attribute> emptyMap(),
                Collections.<IdSymbol, MethodSignature> emptyMap());
    }

    public static DeclaredClassSignature createStringSignature() {
        final Map<IdSymbol, Attribute> attributes = new HashMap<>();
        final Map<IdSymbol, MethodSignature> methods = new HashMap<>();

        methods.put(IdTable.getInstance().addString("length"), MethodSignature.createStringLengthSignature());
        methods.put(IdTable.getInstance().addString("concat"), MethodSignature.createStringConcatSignature());
        methods.put(IdTable.getInstance().addString("substr"), MethodSignature.createStringSubstrSignature());
        return new DeclaredClassSignature(attributes, methods);
    }

    public static DeclaredClassSignature createIOSignature() {
        final Map<IdSymbol, Attribute> attributes = new HashMap<>();
        final Map<IdSymbol, MethodSignature> methods = new HashMap<>();

        methods.put(IdTable.getInstance().addString("out_string"), MethodSignature.createIOOutStringSignature());
        methods.put(IdTable.getInstance().addString("out_int"), MethodSignature.createIOOutIntSignature());
        methods.put(IdTable.getInstance().addString("in_string"), MethodSignature.createIOInStringSignature());
        methods.put(IdTable.getInstance().addString("in_int"), MethodSignature.createIOInIntSignature());
        return new DeclaredClassSignature(attributes, methods);
    }

    private DeclaredClassSignature(Map<IdSymbol, Attribute> attributes, Map<IdSymbol, MethodSignature> methods) {
        super(attributes, methods);
    }
}
