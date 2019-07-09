package it.emarolab.owloop.descriptor.utility.objectPropertyDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorGround.ObjectPropertyGround;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.ObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.List;

/**
 * This is an example of a 'compound' ObjectProperty Descriptor which implements 2 {@link ObjectPropertyExpression} interfaces:
 * <ul>
 * <li><b>{@link ObjectPropertyExpression.Sub}</b>:          to describe that an ObjectProperty subsumes another ObjectProperty.</li>
 * <li><b>{@link ObjectPropertyExpression.Super}</b>:        to describe that an ObjectProperty super-sumes another ObjectProperty.</li>
 * </ul>
 * See {@link FullObjectPropertyDesc} for an example of a 'compound' Individual Descriptor that implements all ObjectPropertyExpressions.
 */
public class HierarchicalObjectPropertyDesc
        extends ObjectPropertyGround
        implements ObjectPropertyExpression.Sub<HierarchicalObjectPropertyDesc>,
        ObjectPropertyExpression.Super<HierarchicalObjectPropertyDesc>{

    private DescriptorEntitySet.ObjectProperties subObjectProperties = new DescriptorEntitySet.ObjectProperties();
    private DescriptorEntitySet.ObjectProperties superObjectProperties = new DescriptorEntitySet.ObjectProperties();

    /* Constructors from class: ObjectPropertyGround */

    public HierarchicalObjectPropertyDesc(OWLObjectProperty instance, OWLReferences onto) {
        super(instance, onto);
    }
    public HierarchicalObjectPropertyDesc(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }
    public HierarchicalObjectPropertyDesc(OWLObjectProperty instance, String ontoName) {
        super(instance, ontoName);
    }
    public HierarchicalObjectPropertyDesc(OWLObjectProperty instance, String ontoName, String filePath, String iriPath) {
        super(instance, ontoName, filePath, iriPath);
    }
    public HierarchicalObjectPropertyDesc(OWLObjectProperty instance, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instance, ontoName, filePath, iriPath, bufferingChanges);
    }
    public HierarchicalObjectPropertyDesc(String instanceName, String ontoName) {
        super(instanceName, ontoName);
    }
    public HierarchicalObjectPropertyDesc(String instanceName, String ontoName, String filePath, String iriPath) {
        super(instanceName, ontoName, filePath, iriPath);
    }
    public HierarchicalObjectPropertyDesc(String instanceName, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instanceName, ontoName, filePath, iriPath, bufferingChanges);
    }

    /* Overriding methods in class: ObjectPropertyGround */


    // To read axioms from an ontology
    @Override
    public List<MappingIntent> readExpressionAxioms() {
        List<MappingIntent> r = ObjectPropertyExpression.Sub.super.readExpressionAxioms();
        r.addAll( ObjectPropertyExpression.Super.super.readExpressionAxioms());
        return r;
    }
    // To write axioms to an ontology
    @Override
    public List<MappingIntent> writeExpressionAxioms() {
        List<MappingIntent> r = ObjectPropertyExpression.Sub.super.writeExpressionAxioms();
        r.addAll( ObjectPropertyExpression.Super.super.writeExpressionAxioms());
        return r;
    }

    /* Overriding methods in classes: ObjectProperty and ObjectPropertyExpression */


    // Is used by the descriptors's build() method. It's possible to change the return type based on need.
    @Override
    public HierarchicalObjectPropertyDesc getNewSubObjectProperty(OWLObjectProperty instance, OWLReferences ontology) {
        return new HierarchicalObjectPropertyDesc( instance, ontology);
    }
    // It returns subObjectProperties from the EntitySet (after being read from the ontology)
    @Override
    public DescriptorEntitySet.ObjectProperties getSubObjectProperties() {
        return subObjectProperties;
    }

    // Is used by the descriptors's build() method. It's possible to change the return type based on need.
    @Override
    public HierarchicalObjectPropertyDesc getNewSuperObjectProperty(OWLObjectProperty instance, OWLReferences ontology) {
        return new HierarchicalObjectPropertyDesc( instance, ontology);
    }
    // It returns superObjectProperties from the EntitySet (after being read from the ontology)
    @Override
    public DescriptorEntitySet.ObjectProperties getSuperObjectProperties() {
        return superObjectProperties;
    }

    /* Overriding method in class: Object */


    // To show internal state of the Descriptor
    public String toString() {
        return getClass().getSimpleName() + "{" + "\n" +
                "\n" +
                "\t" + getGround() + ":" + "\n" +
                "\n" +
                "\t\t⊃ " +      subObjectProperties + "\n" +
                "\t\t⊂ " +      superObjectProperties + "\n" +
                "}" + "\n";
    }
}