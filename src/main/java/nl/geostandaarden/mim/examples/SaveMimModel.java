package nl.geostandaarden.mim.examples;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;
import nl.geostandaarden.mim.MimSerializationApi;
import nl.geostandaarden.mim.MimSerializationApi.MIM_RELATIEMODELLERINGSTYPE;
import nl.geostandaarden.mim.MimSerializationApi.MIM_VERSION;
import nl.geostandaarden.mim_1_2.relatiesoort.Domein;
import nl.geostandaarden.mim_1_2.relatiesoort.Informatiemodel.Packages;
import nl.geostandaarden.mim_1_2.relatiesoort.Relatiemodelleringstype;
import nl.geostandaarden.mim_1_2.relatiesoort.XhtmlTextEx;

/**
 * Example that shows how to create a new MIM model, set the required properties 
 * of the Informatiemodel and Domein and save (marshal, serialize) the model while 
 * validating it.
 */
public class SaveMimModel {

  public static void main(String[] args) throws Exception {
    /* Create a new MIM model (MIM version 1.2, Relatiesoort leidend): */
    nl.geostandaarden.mim_1_2.relatiesoort.MimModel mimModel = (nl.geostandaarden.mim_1_2.relatiesoort.MimModel) MimSerializationApi.newModel(MIM_VERSION.VERSION_1_2, MIM_RELATIEMODELLERINGSTYPE.RELATIESOORT_LEIDEND);
    nl.geostandaarden.mim_1_2.relatiesoort.Informatiemodel model = mimModel.getInformatiemodel();
    
    /* Set its required properties: */
    model.setNaam("Mijn model");
    model.setHerkomst("Dit is de herkomst");
    XhtmlTextEx xhtmlText = new XhtmlTextEx();
    xhtmlText.setContentAsString("<xhtml:body><xhtml:p>Dit is een tekst met <xhtml:b>mixed</xhtml:b> <xhtml:i>content</xhtml:i>.</xhtml:p></xhtml:body>");
    model.setDefinitie(xhtmlText);
    model.setHerkomstDefinitie("Dit is de herkomst van de definitie");
    model.setDatumOpname("2025-01-01");
    model.setInformatiemodeltype("Conceptueel");
    model.setInformatiedomein("detailhandel");
    model.setRelatiemodelleringstype(Relatiemodelleringstype.RELATIESOORT_LEIDEND);
    model.setMIMVersie("1.2");
    model.setMIMTaal("NL");
    
    /* Add a new domain and set its required properties: */
    Packages packages = new Packages();
    model.setPackages(packages);
    Domein domein = new Domein();
    packages.getDomein().add(domein);
    
    domein.setId("my-domain");
    domein.setNaam("MyName");
    domein.setHerkomst("Dit is de herkomst");
    xhtmlText = new XhtmlTextEx();
    xhtmlText.setContentAsString("<xhtml:body><xhtml:p>Dit is een tekst met <xhtml:b>mixed</xhtml:b> <xhtml:i>content</xhtml:i>.</xhtml:p></xhtml:body>");
    domein.setDefinitie(xhtmlText);
    domein.setHerkomstDefinitie("Dit is de herkomst van de definitie");
    domein.setDatumOpname("2025-01-01");
    
    /* Save (marshall, deserialize) the model: */ 
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    mimModel.save(baos, new ValidationEventHandler() {
      
      @Override
      public boolean handleEvent(ValidationEvent event) {
        System.out.println("Validation error: " + event.getMessage() + " (" + event.getSeverity() + ")");
        return event.getSeverity() == ValidationEvent.WARNING;
      }
      
    });
    
    /* Display the MIM serialization: */
    System.out.println(new String(baos.toByteArray(), StandardCharsets.UTF_8));
  }

}