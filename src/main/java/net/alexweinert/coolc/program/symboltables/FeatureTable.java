package net.alexweinert.coolc.program.symboltables;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.ast.Class_;
import net.alexweinert.coolc.program.ast.Feature;
import net.alexweinert.coolc.program.ast.attr;
import net.alexweinert.coolc.program.ast.class_c;
import net.alexweinert.coolc.program.ast.formalc;
import net.alexweinert.coolc.program.ast.method;

public class FeatureTable {
    /**
     * Encapsulates the signature of a method, makes referring to return type and formal arguments better readable than
     * a simple list as suggested in the typing rules. Immutable.
     */
    public class MethodSignature {
        /** The return type of the method */
        private AbstractSymbol returnType;
        /** The types of the formal arguments, from left to right */
        private List<AbstractSymbol> argumentTypes;

        public MethodSignature(AbstractSymbol returnType, Collection<AbstractSymbol> argumentTypes) {
            this.returnType = returnType;
            this.argumentTypes = new LinkedList<>(argumentTypes);
        }

        public AbstractSymbol getReturnType() {
            return returnType;
        }

        public List<AbstractSymbol> getArgumentTypes() {
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
    private Map<AbstractSymbol, Map<AbstractSymbol, MethodSignature>> methodSignatures = new HashMap<>();
    /** Type: [ClassName x AttributeName -> AttributeType] */
    private Map<AbstractSymbol, Map<AbstractSymbol, AbstractSymbol>> attributeTypes = new HashMap<>();

    public FeatureTable(ClassTable classTable) {
        for (Class_ currentClass : classTable.getClasses()) {
            Map<AbstractSymbol, MethodSignature> currentMethodSignatures = new HashMap<>();
            Map<AbstractSymbol, AbstractSymbol> currentAttributeTypes = new HashMap<>();

            // Make sure that the special variable self has the correct type
            currentAttributeTypes.put(TreeConstants.self, currentClass.getName());

            // Walk through all the features and gather their declared types
            for (int featureIndex = 0; featureIndex < currentClass.getFeatures().getLength(); ++featureIndex) {
                Feature currentFeature = (Feature) currentClass.getFeatures().getNth(featureIndex);
                if (currentFeature instanceof attr) {
                    currentAttributeTypes = addAttributeType(classTable, (class_c) currentClass, currentAttributeTypes,
                            currentFeature);
                } else if (currentFeature instanceof method) {
                    currentMethodSignatures = addMethodSignature(classTable, (class_c) currentClass,
                            currentMethodSignatures, currentFeature);
                }
            }

            this.methodSignatures.put(currentClass.getName(), currentMethodSignatures);
            this.attributeTypes.put(currentClass.getName(), currentAttributeTypes);
        }

        for (Class_ childClass : classTable.getClasses()) {
            /* Walk through all the parents' features, make sure that the child-definitions fit the parents' definitions
             * and add the unconflicting parents' definitions */
            for (Class_ parentClass : classTable.getAncestors(childClass)) {
                // Add all the parents' attributes to the child
                for (AbstractSymbol parentAttribute : this.getAttributeTypes(parentClass.getName()).keySet()) {
                    if (parentAttribute.equals(TreeConstants.self)) {
                        continue;
                    }

                    // Check that the attribute has not been redefined in the childclass
                    if (this.getAttributeTypes(childClass.getName()).containsKey(parentAttribute)) {
                        // Find the actual definition of the attribute in the currentClass
                        String errorString = String.format("Attribute %s has already been defined in parent class %s.",
                                parentAttribute, childClass.getName(), parentClass.getName());
                        attr attributeDefinition = findAttributeDefinition(childClass, parentAttribute);
                        classTable.semantError(childClass.getFilename(), attributeDefinition).println(errorString);

                        // If this error happens, we use the type declared in the child class, which has already been
                        // added
                    } else {
                        AbstractSymbol parentAttributeType = this.getAttributeTypes(parentClass.getName()).get(
                                parentAttribute);
                        this.getAttributeTypes(childClass.getName()).put(parentAttribute, parentAttributeType);
                    }

                }

                // Add all the parents' methods to the child
                for (AbstractSymbol parentMethod : this.getMethodSignatures(parentClass.getName()).keySet()) {
                    // Check that, if functions are redefined, their signatures match
                    if (this.getMethodSignatures(childClass.getName()).containsKey(parentMethod)) {
                        MethodSignature childSignature = this.getMethodSignatures(childClass.getName()).get(
                                parentMethod);
                        MethodSignature parentSignature = this.getMethodSignatures(parentClass.getName()).get(
                                parentMethod);
                        if (!childSignature.equals(parentSignature)) {
                            String errorString = generateErrorString(parentMethod, childSignature, parentSignature);
                            classTable.semantError(childClass.getFilename(),
                                    findMethodDefinition(childClass, parentMethod)).println(errorString);

                            // Use the parent's definition instead of the child's
                            this.getMethodSignatures(childClass.getName()).put(parentMethod, parentSignature);
                        }
                    } else {
                        // Method is not redefined, simply copy the declaration
                        MethodSignature parentSignature = this.getMethodSignatures(parentClass.getName()).get(
                                parentMethod);
                        this.getMethodSignatures(childClass.getName()).put(parentMethod, parentSignature);
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
    private String generateErrorString(AbstractSymbol methodSignature, MethodSignature childSignature,
            MethodSignature parentSignature) {
        if (!parentSignature.returnType.equals(childSignature.returnType)) {
            return String.format("In redefined method %s, return type %s is different from original return type %s.",
                    methodSignature, childSignature.getReturnType(), parentSignature.getReturnType());

        } else if (parentSignature.getArgumentTypes().size() != childSignature.getArgumentTypes().size()) {
            return String.format("Incompatible number of formal parameters in redefined method %s.", methodSignature);

        } else {
            // Walk through the types of the arguments and compare them one by one
            List<AbstractSymbol> parentFormalTypes = parentSignature.getArgumentTypes();
            List<AbstractSymbol> childFormalTypes = childSignature.getArgumentTypes();

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
    public attr findAttributeDefinition(Class_ currentClass, AbstractSymbol attributeName) {
        for (int featureIndex = 0; featureIndex < currentClass.getFeatures().getLength(); ++featureIndex) {
            Feature currentFeature = (Feature) currentClass.getFeatures().getNth(featureIndex);
            if (currentFeature instanceof attr && ((attr) currentFeature).name.equals(attributeName)) {
                return (attr) currentFeature;
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
    public method findMethodDefinition(Class_ currentClass, AbstractSymbol methodName) {
        for (int featureIndex = 0; featureIndex < currentClass.getFeatures().getLength(); ++featureIndex) {
            Feature currentFeature = (Feature) currentClass.getFeatures().getNth(featureIndex);
            if (currentFeature instanceof method && ((method) currentFeature).name.equals(methodName)) {
                return (method) currentFeature;
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
    private FeatureTable(Map<AbstractSymbol, Map<AbstractSymbol, MethodSignature>> methodSignatures,
            Map<AbstractSymbol, Map<AbstractSymbol, AbstractSymbol>> attributeTypes) {
        this.methodSignatures = methodSignatures;
        this.attributeTypes = attributeTypes;
    }

    public Map<AbstractSymbol, AbstractSymbol> getAttributeTypes(AbstractSymbol className) {
        return this.attributeTypes.get(className);
    }

    public Map<AbstractSymbol, MethodSignature> getMethodSignatures(AbstractSymbol className) {
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
    public FeatureTable copyAndExtend(AbstractSymbol className, AbstractSymbol memberName, AbstractSymbol memberType) {
        FeatureTable returnValue = this.deepCopy();

        // Get the attribute types map for the given class, or create one if there is none
        final Map<AbstractSymbol, AbstractSymbol> attributeTypes;
        if (returnValue.attributeTypes.containsKey(className)) {
            attributeTypes = returnValue.attributeTypes.get(className);
        } else {
            attributeTypes = new HashMap<AbstractSymbol, AbstractSymbol>();
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
        Map<AbstractSymbol, Map<AbstractSymbol, AbstractSymbol>> attributeTypesClone = new HashMap<>(
                this.attributeTypes);
        Map<AbstractSymbol, Map<AbstractSymbol, MethodSignature>> methodSignaturesClone = new HashMap<>(
                this.methodSignatures);

        return new FeatureTable(methodSignaturesClone, attributeTypesClone);
    }

    /**
     * If the currentFeature is of type attr, its name and type are recorded for the current class. If it
     * is multiply defined or if it is the self-attribute, an error message is produced.
     * @return The inputmap augmented with the information given by the current feature.
     */
    private Map<AbstractSymbol, AbstractSymbol> addAttributeType(ClassTable classTable, class_c enclosingClass,
            Map<AbstractSymbol, AbstractSymbol> attributeTypes, Feature currentFeature) {
        attr currentAttribute = (attr) currentFeature;
        
        // Check that the new attribute is not self
        if (currentAttribute.name.equals(TreeConstants.self)) {
            String errorString = "'self' cannot be the name of an attribute";
            classTable.semantError(enclosingClass.getFilename(), currentFeature).println(errorString);
            
        // Check that the new attribute has not been previously defined
        } else if (attributeTypes.containsKey(currentAttribute.name)) {
            String errorString = String.format("Attribute %s is multiply defined in class.", currentAttribute.name);
            classTable.semantError(enclosingClass.getFilename(), currentFeature).println(errorString);

        } else {
            attributeTypes.put(currentAttribute.name, currentAttribute.type_decl);
        }

        return attributeTypes;
    }

    /**
     * If the currentFeature is of type method, its name and signature are recorded for the current class. If it
     * is multiply defined, an error message is produced.
     * @return The inputmap augmented with the information given by the current feature.
     */
    private Map<AbstractSymbol, MethodSignature> addMethodSignature(ClassTable classTable, class_c enclosingClass,
            Map<AbstractSymbol, MethodSignature> methodSignatures, Feature currentFeature) {
        method currentMethod = (method) currentFeature;

        AbstractSymbol returnType = currentMethod.return_type;
        List<AbstractSymbol> formalTypes = new LinkedList<>();

        // Construct the list of formal types
        for (int formalIndex = 0; formalIndex < currentMethod.formals.getLength(); ++formalIndex) {
            formalc currentFormal = (formalc) currentMethod.formals.getNth(formalIndex);
            
            // Make sure that no formal parameter is named 'self'
            if (currentFormal.name.equals(TreeConstants.self)) {
                String errorString = "'self' cannot be the name of a formal parameter.";
                classTable.semantError(enclosingClass.getFilename(), currentFormal).println(errorString);
            }

            formalTypes.add(currentFormal.type_decl);
        }

        // Make sure that the method name is unique in the context of the class
        if (methodSignatures.containsKey(currentMethod.name)) {
            String errorString = String.format("Method %s is multiply defined.", currentMethod.name);
            classTable.semantError(enclosingClass.getFilename(), currentMethod).println(errorString);
            return methodSignatures;
        }

        MethodSignature signature = new MethodSignature(returnType, formalTypes);
        methodSignatures.put(currentMethod.name, signature);

        return methodSignatures;
    }
}
