package it.emarolab.owloop.descriptor.utility.objectProperty_compoundDescriptor;


import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorInterface.DescriptorAxioms;
import it.emarolab.owloop.descriptor.construction.descriptorInterface.ObjectPropertyExpression;
import it.emarolab.owloop.descriptor.construction.descriptorBase.ObjectPropertyDescriptorBase;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.List;

/**
 * A basic implementation for an object property with domain and range restrictions.
 * <p>
 *     This is an example of how use the {@link Descriptor}s for implement
 *     an object property that is synchronised w.r.t. its {@link Domain} and {@link Range} restrictions.
 *     <br>
 *     Its purpose is only to instanciate the {@link DescriptorAxioms.Restrictions} for the
 *     respective descriptions, as well as call both interfaces in the
 *     {@link #readSemantic()} and {@link #writeSemantic()} methods.
 *     All its constructions are based on {@link ObjectPropertyDescriptorBase} in order
 *     to automatically manage an {@link ObjectInstance} ground.
 *     <br>
 *     You may want to use this class (see also {@link DefinitionObjectPropertyDesc}
 *     and {@link HierarchicalObjectPropertyDesc},as well as other classes in the
 *     {@link it.emarolab.owloop.descriptor.utility} package) as templates to build a specific
 *     {@link ObjectPropertyExpression} descriptor that fits your needs and maximises the
 *     OWL synchronisation efficiency for object properties.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.owloop.descriptor.utility.objectProperty_compoundDescriptor.DomainObjectPropertyDesc <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        21/05/17 <br>
 * </small></div>
 */
public class DomainObjectPropertyDesc
        extends ObjectPropertyDescriptorBase
        implements ObjectPropertyExpression.Domain, ObjectPropertyExpression.Range {

    private DescriptorAxioms.Restrictions domainRestriction = new DescriptorAxioms.Restrictions();
    private DescriptorAxioms.Restrictions rangeRestriction = new DescriptorAxioms.Restrictions();


    // constructors for ObjectPropertyDescriptorBase

    public DomainObjectPropertyDesc(OWLObjectProperty instance, OWLReferences onto) {
        super(instance, onto);
    }
    public DomainObjectPropertyDesc(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }
    public DomainObjectPropertyDesc(OWLObjectProperty instance, String ontoName) {
        super(instance, ontoName);
    }
    public DomainObjectPropertyDesc(OWLObjectProperty instance, String ontoName, String filePath, String iriPath) {
        super(instance, ontoName, filePath, iriPath);
    }
    public DomainObjectPropertyDesc(OWLObjectProperty instance, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instance, ontoName, filePath, iriPath, bufferingChanges);
    }
    public DomainObjectPropertyDesc(String instanceName, String ontoName) {
        super(instanceName, ontoName);
    }
    public DomainObjectPropertyDesc(String instanceName, String ontoName, String filePath, String iriPath) {
        super(instanceName, ontoName, filePath, iriPath);
    }
    public DomainObjectPropertyDesc(String instanceName, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instanceName, ontoName, filePath, iriPath, bufferingChanges);
    }



    // implementations for Semantic.descriptor

    @Override
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = ObjectPropertyExpression.Domain.super.readSemantic();
        r.addAll( ObjectPropertyExpression.Range.super.readSemantic());
        return r;
    }

    @Override
    public List<MappingIntent> writeSemantic() {
        List<MappingIntent> r = ObjectPropertyExpression.Domain.super.writeSemantic();
        r.addAll( ObjectPropertyExpression.Range.super.writeSemantic());
        return r;
    }

    

    // implementations for ObjectPropertyExpression.Domain
    @Override
    public DescriptorAxioms.Restrictions getDomainObjectProperty() {
        return domainRestriction;
    }

    

    // implementations for ObjectPropertyExpression.Range
    @Override
    public DescriptorAxioms.Restrictions getRangeObjectProperty() {
        return rangeRestriction;
    }



    // implementation for standard object interface
    // equals() and hashCode() is based on DescriptorBase<?> which considers only the ground

    public String toString() {
        return "FullObjectPropertyDesc{" +
                NL + "\t\t\t" + getGround() +
                "," + NL + "\t→ " + domainRestriction +
                "," + NL + "\t← " + rangeRestriction +
                NL + "}";
    }
}
