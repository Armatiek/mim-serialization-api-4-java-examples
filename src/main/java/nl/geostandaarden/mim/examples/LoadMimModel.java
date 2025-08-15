package nl.geostandaarden.mim.examples;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;
import nl.geostandaarden.mim.MimSerializationApi;
import nl.geostandaarden.mim.error.*;
import nl.geostandaarden.mim.interfaces.AttribuutsoortType;
import nl.geostandaarden.mim_1_2.relatiesoort.*;
import nl.geostandaarden.mim_1_2.relatiesoort.ext.*;
import nl.geostandaarden.mim_1_2.relatiesoort.ref.RefType;
import nl.geostandaarden.mim_1_2.relatiesoort.ref.RefTypeEx;

/**
 * Example that shows how to load (unmarshal, deserialize) an existing MIM XML serialization
 */
public class LoadMimModel {
  
  private static final String SAMPLE_PATH = "/serializations/1.2/Fietsenwinkel-relatiesoort-leidend.xml";
  
  private nl.geostandaarden.mim_1_2.relatiesoort.MimModel mimModel;
 
  /* Loads an existing MIM serialization from the classpath: */
  public void loadModel() throws MimSerializationApiLoadException {
    /* Get an inputstream of the example serialization: */
    InputStream mimSerialization = LoadMimModel.class.getResourceAsStream(SAMPLE_PATH);
    
    /* Load the model (MIM version 1.2 - Relatiesoort leidend) while validating the serialization: */
    this.mimModel = (nl.geostandaarden.mim_1_2.relatiesoort.MimModel) MimSerializationApi.loadModel(mimSerialization, new ValidationEventHandler() {
      
      @Override
      public boolean handleEvent(ValidationEvent event) {
        System.out.println("Validation error: " + event.getMessage() + " (" + event.getSeverity() + ")");
        return event.getSeverity() == ValidationEvent.WARNING;
      }
      
    });
  }
  
  /* Display the names of all Objecttypes in the first domain of the model: */
  public void displayNamesOfAllObjecttypesInFirstDomain() {
    List<Objecttype> objectTypesInFirstDomain = mimModel.getInformatiemodel().getPackages().getDomein().get(0).getObjecttypen().getObjecttype();
    objectTypesInFirstDomain.forEach(
      objectType -> System.out.println(objectType.getNaam())
    );
  }
  
  /* Displays an XHTML field as string: */
  public void displayXhtmlContent() throws MimSerializationApiXhtmlException {
    /* Get an Objecttype by its name using a helper method: */
    Objecttype objectType = mimModel.getObjecttypeByName("Bankrekening");
    
    /* Cast its definition field to the XhtmlTextEx interface: */
    XhtmlTextEx definitie = (XhtmlTextEx) objectType.getDefinitie();
    
    /* Display the XHTML text using the helper method getContentAsString: */
    System.out.println(definitie.getContentAsString());
  }
  
  public void followReferences() {
    /* Get an Objecttype by its name using a helper method: */
    Objecttype objectType = mimModel.getObjecttypeByName("Leverancier");
    
    /* Iterate the references to the supertypes: */
    objectType.getSupertypen().getGeneralisatieObjecttypen().forEach(
      gen -> 
        {
          RefType objectypeRef = gen.getSupertype().getObjecttypeRef();
          
          /* Cast the RefType to a RefTypeEx: */
          RefTypeEx refType = (RefTypeEx) objectypeRef;
          
          /* Get the name of the supertype: */
          System.out.println(((Objecttype) refType.getTarget()).getNaam());
        }     
    );
  }
  
  /* Displays the type of an Attribuutsoort: */
  public void displayAttribuutsoortType() {
    /* Get an Objecttype by its name using a helper method: */
    Objecttype objectType = mimModel.getObjecttypeByName("Leverancier");
    
    /* Get its attribuut with name "kvk nummer": */
    Optional<Attribuutsoort> attr = ((ObjecttypeEx) objectType).getAttribuutsoort("kvk nummer");
    if (attr.isEmpty()) {
      System.out.println("Attribuut \"kvk nummer\" of Objecttype \"Leverancier\" not found");
      return;
    }
    
    /* Cast the Attribuutsoort to an AttribuutsoortEx: */
    AttribuutsoortEx attrEx = (AttribuutsoortEx) attr.get();

    AttribuutsoortType type = attrEx.getAttribuutsoortType();    
    if (type instanceof Datatype) {
      System.out.println("Datatype: " + ((Datatype) type).getValue());
    } else if (type instanceof PrimitiefDatatype) {
      System.out.println("PrimitiefDatatype: " + ((PrimitiefDatatype) type).getNaam());
    } else if (type instanceof GestructureerdDatatype) {
      System.out.println("GestructureerdDatatype: " + ((PrimitiefDatatype) type).getNaam());
    } else if (type instanceof Codelijst) {
      System.out.println("Codelijst: " + ((Codelijst) type).getNaam());
    } else if (type instanceof Referentielijst) {
      System.out.println("Referentielijst: " + ((Referentielijst) type).getNaam());
    } else if (type instanceof Enumeratie) {
      System.out.println("Enumeratie: " + ((Enumeratie) type).getNaam());
    } else if (type instanceof Keuze) {
      System.out.println("Keuze: " + ((Keuze) type).getNaam());
    } else if (type instanceof Constructie) {
      System.out.println("Constructie: " + ((Constructie) type).getId());
    }
  }
  
  public static void main(String[] args) throws Exception {
    LoadMimModel lmm = new LoadMimModel();
    try {
      lmm.loadModel();
      lmm.displayNamesOfAllObjecttypesInFirstDomain();
      lmm.displayXhtmlContent();
      lmm.followReferences();
      lmm.displayAttribuutsoortType();
    } catch (MimSerializationApiException e) {
      e.printStackTrace(System.err);
    }
  }

}