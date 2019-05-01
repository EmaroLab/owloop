package it.emarolab.owloop.descriptor.utility.owloopPaperTests;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorBase.IndividualDescriptorBase;
import it.emarolab.owloop.descriptor.construction.descriptorBaseInterface.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.IndividualExpression;
import it.emarolab.owloop.descriptor.utility.objectPropertyDescriptor.FullObjectPropertyDesc;

import java.util.List;

public class ObjLinkIndividualDesc
        extends IndividualDescriptorBase
        implements IndividualExpression.ObjectLink<FullObjectPropertyDesc> {

    private DescriptorEntitySet.ObjectSemantics objectLinks = new DescriptorEntitySet.ObjectSemantics();

    // constructors for IndividualDescriptorBase
    public ObjLinkIndividualDesc(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }

    // implementations for Axiom.descriptor
    @Override
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = ObjectLink.super.readSemantic();
        return r;
    }

    @Override
    public List<MappingIntent> writeSemantic() {
        List<MappingIntent> r = ObjectLink.super.writeSemantic();
        return r;
    }


    // implementations for IndividualExpression.ObjectLink
    @Override //called during build...() you can change the returning type to any implementations of ObjectPropertyExpression
    public FullObjectPropertyDesc getNewObjectIndividual(DescriptorEntitySet.ObjectExpression instance, OWLReferences ontology) {
        return new FullObjectPropertyDesc( instance.getExpression(), ontology);
    }

    @Override
    public DescriptorEntitySet.ObjectSemantics getObjectSemantics() {
        return objectLinks;
    }



    // implementation for standard object interface
    // equals() and hashCode() is based on DescriptorBase<?> which considers only the ground
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                NL + "\t\t\t" + getGround() +
                "," + NL + "\t⊨ " + objectLinks +
                NL + "}" + NL;
    }
}