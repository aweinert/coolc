package net.alexweinert.coolc.program.symboltables;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.ClassNode;
import net.alexweinert.coolc.program.ast.ClassNode;
import net.alexweinert.coolc.program.ast.Feature;
import net.alexweinert.coolc.program.ast.Formal;
import net.alexweinert.coolc.program.ast.Method;

public class FeatureTable {
    /**
     * Encapsulates the signature of a method, makes referring to return type and formal arguments better readable than
     * a simple list as suggested in the typing rules. Immutable.
     */
    public class MethodSignature {
        /** The return type of the method */
        private IdSymbol returnType;
        /** The types of the formal arguments, from left to right */
        private List<IdSymbol> argumentTypes;

        public MethodSignature(IdSymbol returnType, Collection<IdSymbol> argumentTypes) {
            this.returnType = returnType;
            this.argumentTypes = new LinkedList<>(argumentTypes);
        }

        public IdSymbol getReturnType() {
            return returnType;
        }

        public List<IdSymbol> getArgumentTypes() {
            return argumentTypes;
        }

        /**
         * Two method signatures equal each other when they have the same return type and the same types of formal
         * arguments in the same order.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof MethodSignature)) {
                return false;
            }
            MethodSignature other = (MethodSignature) obj;

            if (argumentTypes == null) {
                if (other.argumentTypes != null) {
                    return false;
                }
            } else if (!argumentTypes.equals(other.argumentTypes)) {
                return false;
            }

            if (returnType == null) {
                if (other.returnType != null) {
                    return false;
                }
            } else if (!returnType.equals(other.returnType)) {
                return false;
            }

            return true;
        }
    }

    /** Type: [ClassName x MethodName -> MethodSignature] */
    private Map<IdSymbol, Map<IdSymbol, MethodSignature>> methodSignatures = new HashMap<>();
    /** Type: [ClassName x AttributeName -> AttributeType] */
    private Map<IdSymbol, Map<IdSymbol, IdSymbol>> attributeTypes = new HashMap<>();

    public FeatureTable(ClassTable classTable) {
        for (ClassNode currentClass : classTable.getClasses()) {
            Map<IdSymbol, MethodSignature> currentMethodSignatures = new HashMap<>();
            Map<IdSymbol, IdSymbol> currentAttributeTypes = new HashMap<>();

            // Make sure that the special variable self has the correct type
            currentAttributeTypes.put(TreeConstants.self, currentClass.getIdentifier());

            // Walk through all the features and gather their declared types
            for (final Feature currentFeature : currentClass.getFeatures()) {
                if (currentFeature instanceof Attribute) {
                    currentAttributeTypes = addAttributeType(classTable, (ClassNode) currentClass, currentAttributeTypes,
                            currentFeature);
                } else if (currentFeature instanceof Method) {
                    currentMethodSignatures = addMethodSignature(classTable, (ClassNode) currentClass,
                            currentMethodSignatures, currentFeature);
                }
            }

            this.methodSignatures.put(currentClass.getIdentifier(), currentMethodSignatures);
            this.attributeTypes.put(currentClass.getIdentifier(), currentAttributeTypes);
        }

        for (ClassNode childClass : classTable.getClasses()) {
            /* Walk through all the parents' features, make sure that the child-definitions fit the parents' definitions
             * and add the unconflicting parents' definitions */
            for (ClassNode parentClass : classTable.getAncestors(childClass)) {
                // Add all the parents' attributes to the child
                for (IdSymbol parentAttribute : this.getAttributeTypes(parentClass.getIdentifier()).keySet()) {
                    if (parentAttribute.equals(TreeConstants.self)) {
                        continue;
                    }

                    // Check that the attribute has not been redefined in the childclass
                    if (this.getAttributeTypes(childClass.getIdentifier()).containsKey(parentAttribute)) {
                        // Find the actual definition of the attribute in the currentClass
                        String errorString = String.format("Attribute %s has already been defined in parent class %s.",
                                parentAttribute, childClass.getIdentifier(), parentClass.getIdentifier());
                        Attribute attributeDefinition = findAttributeDefinition(childClass, parentAttribute);
                        classTable.semantError(childClass.getFilename(), attributeDefinition).println(errorString);

                        // If this error happens, we use the type declared in the child class, which has already been
                        // added
                    } else {
                        IdSymbol parentAttributeType = this.getAttributeTypes(parentClass.getIdentifier()).get(
                                parentAttribute);
                        this.getAttributeTypes(childClass.getIdentifier()).put(parentAttribute, parentAttributeType);
                    }

                }

                // Add all the parents' methods to the child
                for (IdSymbol parentMethod : this.getMethodSignatures(parentClass.getIdentifier()).keySet()) {
                    // Check that, if functions are redefined, their signatures match
                    if (this.getMethodSignatures(childClass.getIdentifier()).containsKey(parentMethod)) {
                        MethodSignature childSignature = this.getMethodSignatures(childClass.getIdentifier()).get(
                                parentMethod);
                        MethodSignature parentSignature = this.getMethodSignatures(parentClass.getIdentifier()).get(
                                parentMethod);
                        if (!childSignature.equals(parentSignature)) {
                            String errorString = generateErrorString(parentMethod, childSignature, parentSignature);
                            classTable.semantError(childClass.getFilename(),
                                    findMethodDefinition(childClass, parentMethod)).println(errorString);

                            // Use the parent's definition instead of the child's
                            this.getMethodSignatures(childClass.getIdentifier()).put(parentMethod, parentSignature);
                        }
                    } else {
                        // Method is not redefined, simply copy the declaration
                        MethodSignature parentSignature = this.getMethodSignatures(parentClass.getIdentifier()).get(
                                parentMethod);
                        this.getMethodSignatures(childClass.getIdentifier()).put(parentMethod, parentSignature);
                    }

                }
            }
        }
    }

    /**
     * Generate the String describing the error if two method signatures do not match. If the return types do not match,
     * this error is reported. If the return types match, but the number of arguments is different, then this error is
     * reported. If the number of arguments are the same, but the types differ at some point, the leftmost location at
     * which the types differ is reported.
     */
    private String generateErrorString(IdSymbol methodSignature, MethodSignature childSignature,
            MethodSignature parentSignature) {
        if (!parentSignature.returnType.equals(childSignature.returnType)) {
            return String.format("In redefined method %s, return type %s is different from original return type %s.",
                    methodSignature, childSignature.getReturnType(), parentSignature.getReturnType());

        } else if (parentSignature.getArgumentTypes().size() != childSignature.getArgumentTypes().size()) {
            return String.format("Incompatible number of formal parameters in redefined method %s.", methodSignature);

        } else {
            // Walk through the types of the arguments and compare them one by one
            List<IdSymbol> parentFormalTypes = parentSignature.getArgumentTypes();
            List<IdSymbol> childFormalTypes = childSignature.getArgumentTypes();

            for (int formalTypeIndex = 0; formalTypeIndex < parentSignature.getArgumentTypes().size(); ++formalTypeIndex) {
                if (!parentFormalTypes.get(formalTypeIndex).equals(childFormalTypes.get(formalTypeIndex))) {
                    return String.format(
                            "In redefined method %s, parameter type %s is different from original type %s.",
                            methodSignature, childFormalTypes.get(formalTypeIndex),
                            parentFormalTypes.get(formalTypeIndex));
                }
            }
        }

        // If we get here, then the two method signatures match, thus there is no error to be reported
        return null;
    }

    /**
     * Finds the attr-node defining the attribute of the given name in the given class
     * 
     * @param currentClass
     *            The class in which to find the actual definition
     * @param attributeName
     *            The name of the attribute for which to find the definition
     * @return The attr-node that defines the attribute of the given name, if the given class has an attribute of this
     *         name
     */
    public Attribute findAttributeDefinition(ClassNode currentClass, IdSymbol attributeName) {
        for (final Feature currentFeature : currentClass.getFeatures()) {
            if (currentFeature instanceof Attribute && ((Attribute) currentFeature).getName().equals(attributeName)) {
                return (Attribute) currentFeature;
            }
        }
        // Should never get here
        return null;
    }

    /**
     * Finds the method-node defining the method of the given name in the given class
     * 
     * @param currentClass
     *            The class in which to find the actual definition
     * @param methodName
     *            The name of the method for which to find the definition
     * @return The method-node that defines the attribute of the given name, if the given class has an attribute of this
     *         name
     */
    public Method findMethodDefinition(ClassNode currentClass, IdSymbol methodName) {
        for (final Feature currentFeature : currentClass.getFeatures()) {
            if (currentFeature instanceof Method && ((Method) currentFeature).getName().equals(methodName)) {
                return (Method) currentFeature;
            }
        }
        // Should never get here
        return null;
    }

    /**
     * @param methodSignatures
     *            The map of ClassNames and MethodNames to their signatures. Is not copied.
     * @param attributeTypes
     *            The map of ClassNames and AttributeNames to their types. Is not copied.
     */
    private FeatureTable(Map<IdSymbol, Map<IdSymbol, MethodSignature>> methodSignatures,
            Map<IdSymbol, Map<IdSymbol, IdSymbol>> attributeTypes) {
        this.methodSignatures = methodSignatures;
        this.attributeTypes = attributeTypes;
    }

    public Map<IdSymbol, IdSymbol> getAttributeTypes(IdSymbol className) {
        return this.attributeTypes.get(className);
    }

    public Map<IdSymbol, MethodSignature> getMethodSignatures(IdSymbol className) {
        return this.methodSignatures.get(className);
    }

    /**
     * Creates a new FeatureTable that contains the same information as this one, plus the fact that
     * className.memberName is of type memberType.
     * 
     * @param className
     *            The name of the class to be extended
     * @param memberName
     *            The name of the memberVariable to be recorded for className
     * @param memberType
     *            The type of className.memberName
     */
    public FeatureTable copyAndExtend(IdSymbol className, IdSymbol memberName, IdSymbol memberType) {
        FeatureTable returnValue = this.deepCopy();

        // Get the attribute types map for the given class, or create one if there is none
        final Map<IdSymbol, IdSymbol> attributeTypes;
        if (returnValue.attributeTypes.containsKey(className)) {
            attributeTypes = returnValue.attributeTypes.get(className);
        } else {
            attributeTypes = new HashMap<IdSymbol, IdSymbol>();
            returnValue.attributeTypes.put(className, attributeTypes);
        }

        attributeTypes.put(memberName, memberType);
        return returnValue;
    }

    /**
     * Creates a deep copy of this object, i.e., all members are cloned as well.
     * 
     * @return A deep copy of this object
     */
    private FeatureTable deepCopy() {
        Map<IdSymbol, Map<IdSymbol, IdSymbol>> attributeTypesClone = new HashMap<>(
                this.attributeTypes);
        Map<IdSymbol, Map<IdSymbol, MethodSignature>> methodSignaturesClone = new HashMap<>(
                this.methodSignatures);

        return new FeatureTable(methodSignaturesClone, attributeTypesClone);
    }

    /**
     * If the currentFeature is of type attr, its name and type are recorded for the current class. If it is multiply
     * defined or if it is the self-attribute, an error message is produced.
     * 
     * @return The inputmap augmented with the information given by the current feature.
     */
    private Map<IdSymbol, IdSymbol> addAttributeType(ClassTable classTable, ClassNode enclosingClass,
            Map<IdSymbol, IdSymbol> attributeTypes, Feature currentFeature) {
        Attribute currentAttribute = (Attribute) currentFeature;

        // Check that the new attribute is not self
        if (currentAttribute.getName().equals(TreeConstants.self)) {
            String errorString = "'self' cannot be the name of an attribute";
            classTable.semantError(enclosingClass.getFilename(), currentFeature).println(errorString);

            // Check that the new attribute has not been previously defined
        } else if (attributeTypes.containsKey(currentAttribute.getName())) {
            String errorString = String
                    .format("Attribute %s is multiply defined in class.", currentAttribute.getName());
            classTable.semantError(enclosingClass.getFilename(), currentFeature).println(errorString);

        } else {
            attributeTypes.put(currentAttribute.getName(), currentAttribute.getTypeDecl());
        }

        return attributeTypes;
    }

    /**
     * If the currentFeature is of type method, its name and signature are recorded for the current class. If it is
     * multiply defined, an error message is produced.
     * 
     * @return The inputmap augmented with the information given by the current feature.
     */
    private Map<IdSymbol, MethodSignature> addMethodSignature(ClassTable classTable, ClassNode enclosingClass,
            Map<IdSymbol, MethodSignature> methodSignatures, Feature currentFeature) {
        Method currentMethod = (Method) currentFeature;

        IdSymbol returnType = currentMethod.getReturnType();
        List<IdSymbol> formalTypes = new LinkedList<>();

        // Construct the list of formal types
        for (final Formal currentFormal : currentMethod.getFormals()) {
            // Make sure that no formal parameter is named 'self'
            if (currentFormal.getIdentifier().equals(TreeConstants.self)) {
                String errorString = "'self' cannot be the name of a formal parameter.";
                classTable.semantError(enclosingClass.getFilename(), currentFormal).println(errorString);
            }

            formalTypes.add(currentFormal.getDeclaredType());
        }

        // Make sure that the method name is unique in the context of the class
        if (methodSignatures.containsKey(currentMethod.getName())) {
            String errorString = String.format("Method %s is multiply defined.", currentMethod.getName());
            classTable.semantError(enclosingClass.getFilename(), currentMethod).println(errorString);
            return methodSignatures;
        }

        MethodSignature signature = new MethodSignature(returnType, formalTypes);
        methodSignatures.put(currentMethod.getName(), signature);

        return methodSignatures;
    }
}
