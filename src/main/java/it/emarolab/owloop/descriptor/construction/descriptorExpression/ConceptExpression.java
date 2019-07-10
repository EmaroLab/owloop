package it.emarolab.owloop.descriptor.construction.descriptorExpression;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.SemanticRestriction;
import it.emarolab.amor.owlInterface.SemanticRestriction.*;
import it.emarolab.owloop.core.Concept;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorGround.DescriptorGroundInterface;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This interface extends all the interfaces in {@link Concept}.
 * It allows to {@link #readExpressionAxioms()} and {@link #writeExpressionAxioms()}
 * specifically for OWL Classes.
 * It contains several expressions that can be combined in any arbitrary way as they
 * rely on the same ground ({@link ConceptGroundInstance}).
 *
 * <p>
 * <div style="text-align:center;"><small>
 * <b>File</b>:         it.emarolab.owloop.core.Axiom <br>
 * <b>Licence</b>:      GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Authors</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it), Syed Yusha Kareem (kareem.syed.yusha@dibris.unige.it) <br>
 * <b>affiliation</b>:  EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:         01/05/19 <br>
 * </small></div>
 */
public interface ConceptExpression
        extends Concept<OWLReferences,OWLClass>,
        DescriptorGroundInterface<OWLClass> {

    /**
     * To access the {@link DescriptorGroundInterface} facilities.
     * @return the ontology on which {@code this} descriptor is working.
     * @deprecated use {@link #getGround()} instead.
     */
    @Override @Deprecated
    default OWLReferences getGroundOntology() {
        return getGround().getGroundOntology();
    }
    /**
     * To access the {@link DescriptorGroundInterface} facilities.
     * @return the ground instance of {@code this} descriptor.
     * @deprecated use {@link #getInstance()} instead.
     */
    @Override @Deprecated
    default OWLClass getGroundInstance() {
        return getGround().getGroundInstance();
    }

    /**
     * The {@link Concept.Instance} expression for a {@link Descriptor} whose ground is {@link OWLClass}.
     * <p>
     *     It specifies how to {@link #queryIndividualInstances()} and {@link #writeExpressionAxioms()} for the
     *     individuals of the ground Class ({@link #getInstance()).
     * </p>
     *
     * @param <D> the type of the {@link IndividualExpression} descriptor instantiated during
     *           {@link #buildIndividualInstances()} through {@link #getIndividualDescriptor(Object, Object)}.
     */
    interface Instance<D extends IndividualExpression>
            extends Concept.Instance<OWLReferences, OWLClass, OWLNamedIndividual,D>,
            ConceptExpression {

        /**
         * It is an helper that just calls:
         * {@code {@link #getIndividualInstances()}.add( {@link #getOntology()}.getOWLIndividual( propertyName))}
         * in order to add a new individualDescriptor (given by name) in the {@link EntitySet} list.
         * @param individualName the individualDescriptor name to add for synchronisation.
         * @return {@code true} if the axioms changed as a result of the call.
         */
        default boolean addIndividualClassified(String individualName){
            return getIndividualInstances().add( getOntology().getOWLIndividual( individualName));
        }
        /**
         * It is an helper that just calls:
         * {@code {@link #getIndividualInstances()}.add( individualDescriptor)}
         * in order to add a new individualDescriptor in the {@link EntitySet} list.
         * @param individual the individualDescriptor to add for synchronisation.
         * @return {@code true} if the axioms changed as a result of the call.
         */
        default boolean addIndividualClassified(OWLNamedIndividual individual){
            return getIndividualInstances().add( individual);
        }

        /**
         * It is an helper that just calls:
         * {@code {@link #getIndividualInstances()}.remove( {@link #getOntology()}.getOWLIndividual( propertyName))}
         * in order to remove an individualDescriptor (given by name) from the {@link EntitySet} list.
         * @param individualName the individualDescriptor name to remove for synchronisation.
         * @return {@code true} if an element was removed as a result of this call.
         */
        default boolean removeIndividualClassified(String individualName){
            return getIndividualInstances().remove( getOntology().getOWLIndividual( individualName));
        }
        /**
         * It is an helper that just calls:
         * {@code {@link #getIndividualInstances()}.remove( individualDescriptor)}
         * in order to remove an individualDescriptor in the {@link EntitySet} list.
         * @param individual the individualDescriptor to remove for synchronisation.
         * @return {@code true} if an element was removed as a result of this call.
         */
        default boolean removeIndividualClassified(OWLNamedIndividual individual){
            return getIndividualInstances().remove( individual);
        }

        @Override // see super classes for documentation
        default DescriptorEntitySet.Individuals queryIndividualInstances(){
            DescriptorEntitySet.Individuals set = new DescriptorEntitySet.Individuals(getOntology().getIndividualB2Class(getInstance()));
            set.setSingleton( getIndividualInstances().isSingleton());
            return set;
        }

        @Override // see super classes for documentation
        default List<MappingIntent> writeExpressionAxioms(){
            try {
                EntitySet.SynchronisationIntent<OWLNamedIndividual> to = synchroniseIndividualInstanceToExpressionAxioms();
                if (to == null)
                    return getIntent(null);
                List<OWLOntologyChange> changes = new ArrayList<>();
                for (OWLNamedIndividual a : to.getToAdd())
                    changes.add(getOntology().addIndividualB2Class(a, getInstance()));
                for (OWLNamedIndividual b : to.getToRemove())
                    changes.add(getOntology().removeIndividualB2Class(b, getInstance()));
                return getChangingIntent(to, changes);
            } catch (Exception e){
                e.printStackTrace();
                return getIntent( null);
            }
        }
    }

    /**
     * The {@link Concept.Disjoint} expression for a {@link Descriptor} whose ground is {@link OWLClass}.
     * <p>
     *     It specifies how to {@link #queryDisjointConcept()} and {@link #writeExpressionAxioms()} for the
     *     classes disjoint to the ground Class ({@link #getInstance()).
     * </p>
     *
     * @param <D> the type of the {@link ConceptExpression} descriptor instantiated during
     *           {@link #buildDisjointConcept()} through {@link #getDisjointConceptDescriptor(Object, Object)}.
     */
    interface Disjoint<D extends ConceptExpression>
            extends Concept.Disjoint<OWLReferences, OWLClass, D>,
            ConceptExpression {

        /**
         * It is an helper that just calls:
         * {@code {@link #getDisjointConcepts()}.add( {@link #getOntology()}.getOWLClass( propertyName))}
         * in order to add a new class (given by name) in the {@link EntitySet} list.
         * This method should be always synchronised with {@link #writeExpressionAxiomsInconsistencySafe()}
         * to be perfectly aligned with the ontology, since it affects {@link ConceptExpression.Sub} {@link EntitySet}.
         * @param className the class name to add for synchronisation.
         * @return {@code true} if the axioms changed as a result of the call.
         */
        default boolean addDisjointConcept( String className){
            return getDisjointConcepts().add( getOntology().getOWLClass( className));
        }
        /**
         * It is an helper that just calls:
         * {@code {@link #getDisjointConcepts()}.add( cl)}
         * in order to add a new class in the {@link EntitySet} list.
         * This method should be always synchronised with {@link #writeExpressionAxiomsInconsistencySafe()}
         * to be perfectly aligned with the ontology, since it affects {@link ConceptExpression.Sub} {@link EntitySet}.
         * @param cl the class to add for synchronisation.
         * @return {@code true} if the axioms changed as a result of the call.
         */
        default boolean addDisjointConcept( OWLClass cl){
            return getDisjointConcepts().add( cl);
        }

        /**
         * It is an helper that just calls:
         * {@code {@link #getDisjointConcepts()}.remove( {@link #getOntology()}.getOWLClass( propertyName))}
         * in order to remove a class (given by name) from the {@link EntitySet} list.
         * This method should be always synchronised with {@link #writeExpressionAxiomsInconsistencySafe()}
         * to be perfectly aligned with the ontology, since it affects {@link ConceptExpression.Sub} {@link EntitySet}.
         * @param className the class name to remove for synchronisation.
         * @return {@code true} if an element was removed as a result of this call.
         */
        default boolean removeDisjointConcept( String className){
            return getDisjointConcepts().remove( getOntology().getOWLClass( className));
        }
        /**
         * It is an helper that just calls:
         * {@code {@link #getDisjointConcepts()}.remove( cl)}
         * in order to remove a class in the {@link EntitySet} list.
         * This method should be always synchronised with {@link #writeExpressionAxiomsInconsistencySafe()}
         * to be perfectly aligned with the ontology, since it affects {@link ConceptExpression.Sub} {@link EntitySet}.
         * @param cl the class to remove for synchronisation.
         * @return {@code true} if an element was removed as a result of this call.
         */
        default boolean removeDisjointConcept( OWLClass cl){
            return getDisjointConcepts().remove( cl);
        }


        @Override // see super classes for documentation
        default DescriptorEntitySet.Concepts queryDisjointConcept(){
            DescriptorEntitySet.Concepts set = new DescriptorEntitySet.Concepts(getOntology().getDisjointClasses(getInstance()));
            set.remove( getInstance());
            set.setSingleton( getDisjointConcepts().isSingleton());
            return set;
        }

        @Override // see super classes for documentation
        default List<MappingIntent> writeExpressionAxioms(){
            try {
                EntitySet.SynchronisationIntent<OWLClass> to = synchroniseDisjointConceptToExpressionAxioms();
                if ( to == null)
                    return getIntent( null);
                List<OWLOntologyChange> changes = new ArrayList<>();
                for( OWLClass a : to.getToAdd()){
                    Set<OWLClass> s = new HashSet<>();
                    s.add( getInstance());
                    s.add( a);
                    changes.add( getOntology().makeDisjointClasses( s));
                }
                for( OWLClass r : to.getToRemove()){
                    Set<OWLClass> s = new HashSet<>();
                    s.add( getInstance());
                    s.add( r);
                    changes.add( getOntology().removeDisjointClasses( s));
                }
                return getChangingIntent(to, changes);
            } catch ( Exception e){
                e.printStackTrace();
                return getIntent( null);
            }
        }
    }

    /**
     * The {@link Concept.Equivalent} expression for a {@link Descriptor} whose ground is {@link OWLClass}.
     * <p>
     *     It specifies how to {@link #queryEquivalentConcept()} and {@link #writeExpressionAxioms()} for the
     *     classes equivalent to the ground Class ({@link #getInstance()).
     * </p>
     *
     * @param <D> the type of the {@link ConceptExpression} descriptor instantiated during
     *           {@link #buildEquivalentConcept()} through {@link #getEquivalentConceptDescriptor(Object, Object)}.
     */
    interface Equivalent<D extends ConceptExpression>
            extends Concept.Equivalent<OWLReferences, OWLClass, D>,
            ConceptExpression {

        /**
         * It is an helper that just calls:
         * {@code {@link #getEquivalentConcepts()}.add( {@link #getOntology()}.getOWLClass( propertyName))}
         * in order to add a new class (given by name) in the {@link EntitySet} list.
         * @param className the class name to add for synchronisation.
         * @return {@code true} if the axioms changed as a result of the call.
         */
        default boolean addEquivalentConcept( String className){
            return getEquivalentConcepts().add( getOntology().getOWLClass( className));
        }
        /**
         * It is an helper that just calls:
         * {@code {@link #getEquivalentConcepts()}.add( cl)}
         * in order to add a new class in the {@link EntitySet} list.
         * @param cl the class to add for synchronisation.
         * @return {@code true} if the axioms changed as a result of the call.
         */
        default boolean addEquivalentConcept( OWLClass cl){
            return getEquivalentConcepts().add( cl);
        }

        /**
         * It is an helper that just calls:
         * {@code {@link #getEquivalentConcepts()}.remove( {@link #getOntology()}.getOWLClass( propertyName))}
         * in order to remove a class (given by name) from the {@link EntitySet} list.
         * @param className the class name to remove for synchronisation.
         * @return {@code true} if an element was removed as a result of this call.
         */
        default boolean removeEquivalentConcept( String className){
            return getEquivalentConcepts().remove( getOntology().getOWLClass( className));
        }
        /**
         * It is an helper that just calls:
         * {@code {@link #getEquivalentConcepts()}.remove( cl)}
         * in order to remove a class in the {@link EntitySet} list.
         * @param cl the class to remove for synchronisation.
         * @return {@code true} if an element was removed as a result of this call.
         */
        default boolean removeEquivalentConcept( OWLClass cl){
            return getEquivalentConcepts().remove( cl);
        }

        @Override // see super classes for documentation
        default DescriptorEntitySet.Concepts queryEquivalentConcept(){
            DescriptorEntitySet.Concepts set = new DescriptorEntitySet.Concepts(getOntology().getEquivalentClasses(getInstance()));
            set.remove( getInstance());
            set.setSingleton( getEquivalentConcepts().isSingleton());
            return set;
        }

        @Override // see super classes for documentation
        default List<MappingIntent> writeExpressionAxioms(){
            try {
                EntitySet.SynchronisationIntent<OWLClass> to = synchroniseEquivalentConceptToExpressionAxioms();
                if ( to == null)
                    return getIntent( null);
                List<OWLOntologyChange> changes = new ArrayList<>();
                for( OWLClass a : to.getToAdd()){
                    Set<OWLClass> s = new HashSet<>();
                    s.add( getInstance());
                    s.add( a);
                    changes.add( getOntology().makeEquivalentClasses( s));
                }
                for( OWLClass r : to.getToRemove()){
                    Set<OWLClass> s = new HashSet<>();
                    s.add( getInstance());
                    s.add( r);
                    changes.add( getOntology().removeEquivalentClasses( s));
                }
                return getChangingIntent(to, changes);
            } catch ( Exception e){
                e.printStackTrace();
                return getIntent( null);
            }
        }
    }

    /**
     * The {@link Concept.Sub} expression for a {@link Descriptor} whose ground is {@link OWLClass}.
     * <p>
     *     It specifies how to {@link #querySubConcept()} and {@link #writeExpressionAxioms()} for the
     *     sub-classes of the ground Class ({@link #getInstance()).
     * </p>
     *
     * @param <D> the type of the {@link ConceptExpression} descriptor instantiated during
     *           {@link #buildSubConcept()} through {@link #getSubConceptDescriptor(Object, Object)}.
     */
    interface Sub< D extends ConceptExpression>
            extends Concept.Sub<OWLReferences, OWLClass, D>,
            ConceptExpression {

        /**
         * It is an helper that just calls:
         * {@code {@link #getSubConcepts()}.add( {@link #getOntology()}.getOWLClass( propertyName))}
         * in order to add a new class (given by name) in the {@link EntitySet} list.
         * @param className the class name to add for synchronisation.
         * @return {@code true} if the axioms changed as a result of the call.
         */
        default boolean addSubConcept( String className){
            return getSubConcepts().add( getOntology().getOWLClass( className));
        }
        /**
         * It is an helper that just calls:
         * {@code {@link #getSubConcepts()}.add( cl)}
         * in order to add a new class in the {@link EntitySet} list.
         * @param cl the class to add for synchronisation.
         * @return {@code true} if the axioms changed as a result of the call.
         */
        default boolean addSubConcept( OWLClass cl){
            return getSubConcepts().add( cl);
        }

        /**
         * It is an helper that just calls:
         * {@code {@link #getSubConcepts()}.remove( {@link #getOntology()}.getOWLClass( propertyName))}
         * in order to remove a class (given by name) from the {@link EntitySet} list.
         * @param className the class name to remove for synchronisation.
         * @return {@code true} if an element was removed as a result of this call.
         */
        default boolean removeSubConcept( String className){
            return getSubConcepts().remove( getOntology().getOWLClass( className));
        }
        /**
         * It is an helper that just calls:
         * {@code {@link #getSubConcepts()}.remove( cl)}
         * in order to remove a class in the {@link EntitySet} list.
         * @param cl the class to remove for synchronisation.
         * @return {@code true} if an element was removed as a result of this call.
         */
        default boolean removeSubConcept( OWLClass cl){
            return getSubConcepts().remove( cl);
        }

        @Override // see super classes for documentation
        default DescriptorEntitySet.Concepts querySubConcept(){
            DescriptorEntitySet.Concepts set = new DescriptorEntitySet.Concepts(getOntology().getSubClassOf(getInstance()));
            set.setSingleton( getSubConcepts().isSingleton());
            return set;
        }

        @Override // see super classes for documentation
        default List<MappingIntent> writeExpressionAxioms(){
            try {
                EntitySet.SynchronisationIntent<OWLClass> to = synchroniseSubConceptToExpressionAxioms();
                if ( to == null)
                    return getIntent( null);
                List<OWLOntologyChange> changes = new ArrayList<>();
                for (OWLClass a : to.getToAdd())
                    changes.add(getOntology().addSubClassOf(getInstance(), a));
                for (OWLClass r : to.getToRemove())
                    changes.add(getOntology().removeSubClassOf(getInstance(), r));
                return getChangingIntent(to, changes);
            } catch ( Exception e){
                e.printStackTrace();
                return getIntent( null);
            }
        }
    }

    /**
     * The {@link Concept.Super} expression for a {@link Descriptor} whose ground is {@link OWLClass}.
     * <p>
     *     It specifies how to {@link #querySuperConcept()} and {@link #writeExpressionAxioms()} for the
     *     super-classes of the ground Class ({@link #getInstance()).
     * </p>
     *
     * @param <D> the type of the {@link ConceptExpression} descriptor instantiated during
     *           {@link #buildSuperConcept()} through {@link #getSuperConceptDescriptor(Object, Object)}.
     */
    interface Super<D extends ConceptExpression>
            extends Concept.Super<OWLReferences, OWLClass, D>,
            ConceptExpression {

        /**
         * It is an helper that just calls:
         * {@code {@link #getSuperConcepts()}.add( {@link #getOntology()}.getOWLClass( propertyName))}
         * in order to add a new class (given by name) in the {@link EntitySet} list.
         * This method should be always synchronised with {@link #writeExpressionAxiomsInconsistencySafe()}
         * to be perfectly aligned with the ontology, since it affects {@link ConceptExpression.Definition} {@link EntitySet}.
         * @param className the class name to add for synchronisation.
         * @return {@code true} if the axioms changed as a result of the call.
         */
        default boolean addSuperConcept( String className){
            return getSuperConcepts().add( getOntology().getOWLClass( className));
        }
        /**
         * It is an helper that just calls:
         * {@code {@link #getSuperConcepts()}.add( cl)}
         * in order to add a new class in the {@link EntitySet} list.
         * This method should be always synchronised with {@link #writeExpressionAxiomsInconsistencySafe()}
         * to be perfectly aligned with the ontology, since it affects {@link ConceptExpression.Definition} {@link EntitySet}.
         * @param cl the class to add for synchronisation.
         * @return {@code true} if the axioms changed as a result of the call.
         */
        default boolean addSuperConcept( OWLClass cl){
            return getSuperConcepts().add( cl);
        }

        /**
         * It is an helper that just calls:
         * {@code {@link #getSuperConcepts()}.remove( {@link #getOntology()}.getOWLClass( propertyName))}
         * in order to remove a class (given by name) from the {@link EntitySet} list.
         * This method should be always synchronised with {@link #writeExpressionAxiomsInconsistencySafe()}
         * to be perfectly aligned with the ontology, since it affects {@link ConceptExpression.Definition} {@link EntitySet}.
         * @param className the class name to remove for synchronisation.
         * @return {@code true} if an element was removed as a result of this call.
         */
        default boolean removeSuperConcept( String className){
            return getSuperConcepts().remove( getOntology().getOWLClass( className));
        }
        /**
         * It is an helper that just calls:
         * {@code {@link #getSuperConcepts()}.remove( cl)}
         * in order to remove a class in the {@link EntitySet} list.
         * This method should be always synchronised with {@link #writeExpressionAxiomsInconsistencySafe()}
         * to be perfectly aligned with the ontology, since it affects {@link ConceptExpression.Definition} {@link EntitySet}.
         * @param cl the class to remove for synchronisation.
         * @return {@code true} if an element was removed as a result of this call.
         */
        default boolean removeSuperConcept( OWLClass cl){
            return getSuperConcepts().remove( cl);
        }


        @Override // see super classes for documentation
        default DescriptorEntitySet.Concepts querySuperConcept(){
            DescriptorEntitySet.Concepts set = new DescriptorEntitySet.Concepts(getOntology().getSuperClassOf(getInstance()));
            set.setSingleton( getSuperConcepts().isSingleton());
            return set;
        }

        @Override // see super classes for documentation
        default List<MappingIntent> writeExpressionAxioms(){
            try {
                EntitySet.SynchronisationIntent<OWLClass> to = synchroniseSuperConceptToExpressionAxioms();
                if ( to == null)
                    return getIntent( null);
                List<OWLOntologyChange> changes = new ArrayList<>();
                for (OWLClass a : to.getToAdd())
                    changes.add(getOntology().addSubClassOf(a, getInstance()));
                for (OWLClass r : to.getToRemove())
                    changes.add(getOntology().removeSubClassOf(r, getInstance()));
                return getChangingIntent(to, changes);
            } catch ( Exception e){
                e.printStackTrace();
                return getIntent( null);
            }
        }
    }

    /**
     * The {@link Concept.Definition} expression for a {@link Descriptor} whose ground is {@link OWLClass}.
     * <p>
     *     It specifies how to {@link #queryDefinitionConcepts()} and {@link #writeExpressionAxioms()} for
     *     definition (i.e.: {@link SemanticRestriction}) of the ground Class ({@link #getInstance()).
     *     All the restrictions managed by this Class are considered to be a unique class definition
     *     made by their intersection.
     *     <br>
     *     <b>ATTENTION</b>: the {@link #writeExpressionAxioms()} method implemented by
     *     this constructor uses {@link org.semanticweb.owlapi.change.ConvertSuperClassesToEquivalentClass}
     *     and {@link org.semanticweb.owlapi.change.ConvertEquivalentClassesToSuperClasses}.
     *     It may affect {@link ConceptExpression.Super} or {@link ConceptExpression.Sub} descriptors.
     *     Call always this first!!
     * </p>
     */
    interface Definition
            extends Concept.Definition<OWLReferences, OWLClass, SemanticRestriction>,
            ConceptExpression {

        // todo make manipulating a Set<SemanticRestriction> to manage with restrictions that are
        // in intersection or in conjunctions

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over a minimal cardinality of data properties
         * in a specific range of data type based on: {@link #getOWLDataType(Class)}.
         * @param property the name of the restricting data property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param dataType the data type of the restriction.
         * @return a new data type restriction for class definition.
         */
        default SemanticRestriction createMinDataRestriction( String property, int cardinality, Class dataType){
            return createMinDataRestriction( getOWLDataProperty( property), cardinality, dataType);
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over a minimal cardinality of data properties
         * in a specific range of data type based on: {@link #getOWLDataType(Class)}.
         * @param property the restricting data property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param dataType the data type of the restriction.
         * @return a new data type restriction for class definition.
         */
        default SemanticRestriction createMinDataRestriction( OWLDataProperty property, int cardinality, Class dataType){
            return new ClassRestrictedOnMinData( getInstance(), getOWLDataType( dataType), property, cardinality);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMinDataRestriction(OWLDataProperty, int, Class)}.
         * @param property the restricting data property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addMinDataRestriction( OWLDataProperty property, int cardinality, Class dataType){
            return getDefinitionConcepts().add( createMinDataRestriction( property, cardinality, dataType));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMinDataRestriction(String, int, Class)}.
         * @param property the name of the restricting data property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addMinDataRestriction( String property, int cardinality, Class dataType){
            return getDefinitionConcepts().add( createMinDataRestriction( property, cardinality, dataType));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMinDataRestriction(OWLDataProperty, int, Class)}.
         * @param property the restricting data property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeMinDataRestriction( OWLDataProperty property, int cardinality, Class dataType){
            return getDefinitionConcepts().remove( createMinDataRestriction( property, cardinality, dataType));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMinDataRestriction(String, int, Class)}.
         * @param property the name of the restricting data property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeMinDataRestriction( String property, int cardinality, Class dataType){
            return getDefinitionConcepts().remove( createMinDataRestriction( property, cardinality, dataType));
        }

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over a maximal cardinality of data properties
         * in a specific range of data type based on: {@link #getOWLDataType(Class)}.
         * @param property the name of the restricting data property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param dataType the data type of the restriction.
         * @return a new data type restriction for class definition.
         */
        default SemanticRestriction createMaxDataRestriction( String property, int cardinality, Class dataType){
            return createMaxDataRestriction( getOWLDataProperty( property), cardinality, dataType);
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over a maximal cardinality of data properties
         * in a specific range of data type based on: {@link #getOWLDataType(Class)}.
         * @param property the restricting data property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param dataType the data type of the restriction.
         * @return a new data type restriction for class definition.
         */
        default SemanticRestriction createMaxDataRestriction( OWLDataProperty property, int cardinality, Class dataType){
            return new ClassRestrictedOnMaxData( getInstance(), getOWLDataType( dataType), property, cardinality);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMaxDataRestriction(OWLDataProperty, int, Class)}.
         * @param property the restricting data property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addMaxDataRestriction( OWLDataProperty property, int cardinality, Class dataType){
            return getDefinitionConcepts().add( createMaxDataRestriction( property, cardinality, dataType));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMaxDataRestriction(String, int, Class)}.
         * @param property the name of the restricting data property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addMaxDataRestriction( String property, int cardinality, Class dataType){
            return getDefinitionConcepts().add( createMaxDataRestriction( property, cardinality, dataType));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMaxDataRestriction(OWLDataProperty, int, Class)}.
         * @param property the restricting data property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeMaxDataRestriction( OWLDataProperty property, int cardinality, Class dataType){
            return getDefinitionConcepts().remove( createMaxDataRestriction( property, cardinality, dataType));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMaxDataRestriction(String, int, Class)}.
         * @param property the name of the restricting data property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeMaxDataRestriction( String property, int cardinality, Class dataType){
            return getDefinitionConcepts().remove( createMaxDataRestriction( property, cardinality, dataType));
        }

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an exact cardinality of data properties
         * in a specific range of data type based on: {@link #getOWLDataType(Class)}.
         * @param property the name of the restricting data property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param dataType the data type of the restriction.
         * @return a new data type restriction for class definition.
         */
        default SemanticRestriction createExactDataRestriction( String property, int cardinality, Class dataType){
            return createExactDataRestriction( getOWLDataProperty( property), cardinality, dataType);
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an exact cardinality of data properties
         * in a specific range of data type based on: {@link #getOWLDataType(Class)}.
         * @param property the restricting data property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param dataType the data type of the restriction.
         * @return a new data type restriction for class definition.
         */
        default SemanticRestriction createExactDataRestriction( OWLDataProperty property, int cardinality, Class dataType){
            return new ClassRestrictedOnExactData( getInstance(), getOWLDataType( dataType), property, cardinality);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createExactDataRestriction(OWLDataProperty, int, Class)}.
         * @param property the restricting data property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addExactDataRestriction( OWLDataProperty property, int cardinality, Class dataType){
            return getDefinitionConcepts().add( createExactDataRestriction( property, cardinality, dataType));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createExactDataRestriction(String, int, Class)}.
         * @param property the name of the restricting data property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addExactDataRestriction( String property, int cardinality, Class dataType){
            return getDefinitionConcepts().add( createExactDataRestriction( property, cardinality, dataType));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createExactDataRestriction(OWLDataProperty, int, Class)}.
         * @param property the restricting data property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeExactDataRestriction( OWLDataProperty property, int cardinality, Class dataType){
            return getDefinitionConcepts().remove( createExactDataRestriction( property, cardinality, dataType));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createExactDataRestriction(String, int, Class)}.
         * @param property the name of the restricting data property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeExactDataRestriction( String property, int cardinality, Class dataType){
            return getDefinitionConcepts().remove( createExactDataRestriction( property, cardinality, dataType));
        }

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an existential cardinality of data properties
         * in a specific range of data type based on: {@link #getOWLDataType(Class)}.
         * @param property the name of the restricting data property.
         * @param dataType the data type of the restriction.
         * @return a new data type restriction for class definition.
         */
        default SemanticRestriction createSomeDataRestriction( String property, Class dataType){
            return createSomeDataRestriction( getOWLDataProperty( property), dataType);
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an existential cardinality of data properties
         * in a specific range of data type based on: {@link #getOWLDataType(Class)}.
         * @param property the restricting data property.
         * @param dataType the data type of the restriction.
         * @return a new data type restriction for class definition.
         */
        default SemanticRestriction createSomeDataRestriction( OWLDataProperty property, Class dataType){
            return new ClassRestrictedOnSomeData( getInstance(), getOWLDataType( dataType), property);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createSomeDataRestriction(OWLDataProperty, Class)}.
         * @param property the restricting data property.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addSomeDataRestriction( String property, Class dataType){
            return getDefinitionConcepts().add( createSomeDataRestriction( property, dataType));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createSomeDataRestriction(String, Class)}.
         * @param property the name of the restricting data property.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addSomeDataRestriction( OWLDataProperty property, Class dataType){
            return getDefinitionConcepts().add( createSomeDataRestriction( property, dataType));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createSomeDataRestriction(OWLDataProperty, Class)}.
         * @param property the restricting data property.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeSomeDataRestriction( String property, Class dataType){
            return getDefinitionConcepts().remove( createSomeDataRestriction( property, dataType));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createSomeDataRestriction(String, Class)}.
         * @param property the name of the restricting data property.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeSomeDataRestriction( OWLDataProperty property, Class dataType){
            return getDefinitionConcepts().remove( createSomeDataRestriction( property, dataType));
        }

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an universal cardinality of data properties
         * in a specific range of data type based on: {@link #getOWLDataType(Class)}.
         * @param property the name of the restricting data property.
         * @param dataType the data type of the restriction.
         * @return a new data type restriction for class definition.
         */
        default SemanticRestriction createOnlyDataRestriction( String property, Class dataType){
            return createOnlyDataRestriction( getOWLDataProperty( property), dataType);
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an universal cardinality of data properties
         * in a specific range of data type based on: {@link #getOWLDataType(Class)}.
         * @param property the restricting data property.
         * @param dataType the data type of the restriction.
         * @return a new data type restriction for class definition.
         */
        default SemanticRestriction createOnlyDataRestriction( OWLDataProperty property, Class dataType){
            return new ClassRestrictedOnAllData( getInstance(), getOWLDataType( dataType), property);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createOnlyDataRestriction(OWLDataProperty, Class)}.
         * @param property the restricting data property.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addOnlyDataRestriction( String property, Class dataType){
            return getDefinitionConcepts().add( createOnlyDataRestriction( property, dataType));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createOnlyDataRestriction(String, Class)}.
         * @param property the name of the restricting data property.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addOnlyDataRestriction( OWLDataProperty property, Class dataType){
            return getDefinitionConcepts().add( createOnlyDataRestriction( property, dataType));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createOnlyDataRestriction(OWLDataProperty, Class)}.
         * @param property the restricting data property.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeOnlyDataRestriction( String property, Class dataType){
            return getDefinitionConcepts().remove( createOnlyDataRestriction( property, dataType));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createOnlyDataRestriction(String, Class)}.
         * @param property the name of the restricting data property.
         * @param dataType the data type of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeOnlyDataRestriction( OWLDataProperty property, Class dataType){
            return getDefinitionConcepts().remove( createOnlyDataRestriction( property, dataType));
        }

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over a minimal cardinality of object properties
         * within in a specific ontological class.
         * @param property the name of the restricting object property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param cl the name of the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createMinObjectRestriction(String property, int cardinality, String cl){
            return createMinObjectRestriction( getOWLObjectProperty( property), cardinality, getOWLClass( cl));
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over a minimal cardinality of object properties
         * within in a specific ontological class.
         * @param property the restricting object property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param cl the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createMinObjectRestriction(OWLObjectProperty property, int cardinality, OWLClass cl){
            return new ClassRestrictedOnMinObject( getInstance(), cl, property, cardinality);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMinObjectRestriction(OWLObjectProperty, int, OWLClass)}.
         * @param property the restricting object property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param cl the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addMinObjectRestriction( OWLObjectProperty property, int cardinality, OWLClass cl){
            return getDefinitionConcepts().add( createMinObjectRestriction( property, cardinality, cl));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMinObjectRestriction(String, int, String)}.
         * @param property the name of the restricting object property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addMinObjectRestriction( String property, int cardinality, String cl){
            return getDefinitionConcepts().add( createMinObjectRestriction( property, cardinality, cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMinObjectRestriction(OWLObjectProperty, int, OWLClass)}.
         * @param property the restricting object property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param cl of the range class of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeMinObjectRestriction( OWLObjectProperty property, int cardinality, OWLClass cl){
            return getDefinitionConcepts().remove( createMinObjectRestriction( property, cardinality, cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMinObjectRestriction(String, int, String)}.
         * @param property the name of the restricting object property.
         * @param cardinality the cardinality for the minimal property restriction.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeMinObjectRestriction( String property, int cardinality, String cl){
            return getDefinitionConcepts().remove( createMinObjectRestriction( property, cardinality, cl));
        }

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over a maximal cardinality of object properties
         * within in a specific ontological class.
         * @param property the name of the restricting object property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param cl the name of the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createMaxObjectRestriction( String property, int cardinality, String cl){
            return createMaxObjectRestriction( getOWLObjectProperty( property), cardinality, getOWLClass( cl));
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over a maximal cardinality of object properties
         * within in a specific ontological class.
         * @param property the restricting object property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param cl the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createMaxObjectRestriction( OWLObjectProperty property, int cardinality, OWLClass cl){
            return new ClassRestrictedOnMaxObject( getInstance(), cl, property, cardinality);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMaxObjectRestriction(OWLObjectProperty, int, OWLClass)}.
         * @param property the restricting object property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param cl the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addMaxObjectRestriction( OWLObjectProperty property, int cardinality, OWLClass cl){
            return getDefinitionConcepts().add( createMaxObjectRestriction( property, cardinality, cl));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMaxObjectRestriction(String, int, String)}.
         * @param property the name of the restricting object property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addMaxObjectRestriction( String property, int cardinality, String cl){
            return getDefinitionConcepts().add( createMaxObjectRestriction( property, cardinality, cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMaxObjectRestriction(OWLObjectProperty, int, OWLClass)}.
         * @param property the restricting object property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param cl of the range class of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeMaxObjectRestriction( OWLObjectProperty property, int cardinality, OWLClass cl){
            return getDefinitionConcepts().remove( createMaxObjectRestriction( property, cardinality, cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createMaxObjectRestriction(String, int, String)}.
         * @param property the name of the restricting object property.
         * @param cardinality the cardinality for the maximal property restriction.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeMaxObjectRestriction( String property, int cardinality, String cl){
            return getDefinitionConcepts().remove( createMaxObjectRestriction( property, cardinality, cl));
        }

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an exact cardinality of object properties
         * within in a specific ontological class.
         * @param property the name of the restricting object property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param cl the name of the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createExactObjectRestriction( String property, int cardinality, String cl){
            return createMaxObjectRestriction( getOWLObjectProperty( property), cardinality, getOWLClass( cl));
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an exact cardinality of object properties
         * within in a specific ontological class.
         * @param property the restricting object property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param cl the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createExactObjectRestriction( OWLObjectProperty property, int cardinality, OWLClass cl){
            return new ClassRestrictedOnMaxObject( getInstance(), cl, property, cardinality);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createExactObjectRestriction(OWLObjectProperty, int, OWLClass)}.
         * @param property the restricting object property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param cl the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addExactObjectRestriction( OWLObjectProperty property, int cardinality, OWLClass cl){
            return getDefinitionConcepts().add( createExactObjectRestriction( property, cardinality, cl));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createExactObjectRestriction(String, int, String)}.
         * @param property the name of the restricting object property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addExactObjectRestriction( String property, int cardinality, String cl){
            return getDefinitionConcepts().add( createExactObjectRestriction( property, cardinality, cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createExactObjectRestriction(OWLObjectProperty, int, OWLClass)}.
         * @param property the restricting object property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param cl of the range class of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeExactObjectRestriction( OWLObjectProperty property, int cardinality, OWLClass cl){
            return getDefinitionConcepts().remove( createExactObjectRestriction( property, cardinality, cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createExactObjectRestriction(String, int, String)}.
         * @param property the name of the restricting object property.
         * @param cardinality the cardinality for the exact property restriction.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeExactObjectRestriction( String property, int cardinality, String cl){
            return getDefinitionConcepts().remove( createExactObjectRestriction( property, cardinality, cl));
        }

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an existential cardinality of object properties
         * within in a specific ontological class.
         * @param property the name of the restricting object property.
         * @param cl the name of the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createSomeObjectRestriction( String property, String cl){
            return createSomeObjectRestriction( getOWLObjectProperty( property), getOWLClass( cl));
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an existential cardinality of object properties
         * within in a specific ontological class.
         * @param property the restricting object property.
         * @param cl the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createSomeObjectRestriction( OWLObjectProperty property, OWLClass cl){
            return new ClassRestrictedOnSomeObject( getInstance(), cl, property);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createSomeObjectRestriction(OWLObjectProperty, OWLClass)}.
         * @param property the restricting object property.
         * @param cl the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addSomeObjectRestrcition( String property, String cl){
            return getDefinitionConcepts().add( createSomeObjectRestriction( property, cl));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createSomeObjectRestriction(String, String)}.
         * @param property the name of the restricting object property.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addSomeObjectRestrcition( OWLObjectProperty property, OWLClass cl){
            return getDefinitionConcepts().add( createSomeObjectRestriction( property, cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createSomeObjectRestriction(OWLObjectProperty, OWLClass)}.
         * @param property the restricting object property.
         * @param cl of the range class of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeSomeObjectRestrcition( String property, String cl){
            return getDefinitionConcepts().remove( createSomeObjectRestriction( property, cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createSomeObjectRestriction(String, String)}.
         * @param property the name of the restricting object property.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeSomeObjectRestrcition( OWLObjectProperty property, OWLClass cl){
            return getDefinitionConcepts().remove( createSomeObjectRestriction( property, cl));
        }

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an universal cardinality of object properties
         * within in a specific ontological class.
         * @param property the name of the restricting object property.
         * @param cl the name of the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createOnlyObjectRestriction( String property, String cl){
            return createOnlyObjectRestriction( getOWLObjectProperty( property), getOWLClass( cl));
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which restricts over an universal cardinality of object properties
         * within in a specific ontological class.
         * @param property the restricting object property.
         * @param cl the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createOnlyObjectRestriction( OWLObjectProperty property, OWLClass cl){
            return new ClassRestrictedOnAllObject( getInstance(), cl, property);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createOnlyObjectRestriction(OWLObjectProperty, OWLClass)}.
         * @param property the restricting object property.
         * @param cl the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addOnlyObjectRestrcition( String property, String cl){
            return getDefinitionConcepts().add( createOnlyObjectRestriction( property, cl));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createOnlyObjectRestriction(String, String)}.
         * @param property the name of the restricting object property.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addOnlyObjectRestrcition( OWLObjectProperty property, OWLClass cl){
            return getDefinitionConcepts().add( createOnlyObjectRestriction( property, cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createOnlyObjectRestriction(OWLObjectProperty, OWLClass)}.
         * @param property the restricting object property.
         * @param cl of the range class of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeOnlyObjectRestrcition( String property, String cl){
            return getDefinitionConcepts().remove( createOnlyObjectRestriction( property, cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createOnlyObjectRestriction(String, String)}.
         * @param property the name of the restricting object property.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a restriction has been removed from the {@link Concept.Definition}.
         */
        default boolean removeOnlyObjectRestrcition( OWLObjectProperty property, OWLClass cl){
            return getDefinitionConcepts().remove( createOnlyObjectRestriction( property, cl));
        }

        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which represents an equivalent class.
         * @param className the name of the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createClassRestriction(String className){
            return createClassRestriction( getOWLClass( className));
        }
        /**
         * Creates a new class equivalence restriction (to be in conjunction with the others in the specific {@link EntitySet})
         * which represents an equivalent class.
         * @param cl the range class of the restriction.
         * @return a new object type restriction for class definition.
         */
        default SemanticRestriction createClassRestriction( OWLClass cl){
            return new ClassRestrictedOnClass( getInstance(), cl);
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createClassRestriction(String)}.
         * @param className the name of the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addClassRestriction(String className){
            return getDefinitionConcepts().add( createClassRestriction( className));
        }
        /**
         * Adds a new restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createClassRestriction(String)}.
         * @param cl the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean addClassRestriction(OWLClass cl){
            return getDefinitionConcepts().add( createClassRestriction( cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createClassRestriction(String)}.
         * @param cl the name of the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean removeClassRestriction(String cl){
            return getDefinitionConcepts().remove( createClassRestriction( cl));
        }
        /**
         * Removes a restriction to the described {@link EntitySet} (i.e.: {@link #getDefinitionConcepts()})
         * based on {@link #createClassRestriction(String)}.
         * @param cl the range class of the restriction.
         * @return {@code true} if a new restriction has been added to the {@link Concept.Definition}.
         */
        default boolean remvoveClassRestriction(OWLClass cl){
            return getDefinitionConcepts().remove( createClassRestriction( cl));
        }

        @Override
        DescriptorEntitySet.Restrictions getDefinitionConcepts();

        @Override // see super classes for documentation
        default DescriptorEntitySet.Restrictions queryDefinitionConcepts(){
            Set<ApplyingRestriction> restrictions = getOntology().getRestrictions(getInstance());
            for ( ApplyingRestriction a : restrictions)
                if ( a.getRestrictionType().isRestrictionOnClass())
                    if ( a.getValue().equals( getInstance())){
                        restrictions.remove( a);
                        break;
                    }
            DescriptorEntitySet.Restrictions set = new DescriptorEntitySet.Restrictions( restrictions);
            set.setSingleton( getDefinitionConcepts().isSingleton());
            return set;
        }

        @Override // see super classes for documentation
        default List<MappingIntent> writeExpressionAxioms(){
            try {
                EntitySet.SynchronisationIntent<SemanticRestriction> to = synchroniseDefinitionConceptToExpressionAxioms();
                if ( to == null)
                    return getIntent( null);
                List<OWLOntologyChange> changes = new ArrayList<>();

                if ( to.getToAdd().size() > 0 | to.getToRemove().size() > 0){
                    //noinspection unchecked
                    changes.addAll( getOntology().convertEquivalentClassesToSuperClasses( getInstance()));
                    for (SemanticRestriction r : to.getToRemove())
                        changes.add( getOntology().removeRestriction( r));

                    for (SemanticRestriction a : to.getToAdd())
                        changes.add(getOntology().addRestriction(a));

                    changes.addAll(getOntology().convertSuperClassesToEquivalentClass(getInstance(),
                            getDefinitionConcepts()));
                }
                return getChangingIntent(to, changes);
            } catch (Exception e){
                e.printStackTrace();
                return getIntent( null);
            }
        }
    }
}