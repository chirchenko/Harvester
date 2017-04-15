package tools;

import java.io.File;
import java.sql.SQLException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import domains.Domains;
import domains.Fields;
import domains.Fields.Field;
import domains.Machinery;
import domains.Points;
import domains.Machinery.Machine;
import logginig.Logger;

public class ExportImport {
	private static Logger logger = Logger.getLogger(ExportImport.class);

	public static void exportXML(File file) throws JAXBException {
		try{
			Marshaller jaxbMarshaller = JAXBContext
					.newInstance(Domains.class)
					.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(new Domains(Fields.getFields(), Machinery.getMachinery()), file);
		}catch(JAXBException e){
			logger.info(e.getCause().getMessage());
			throw e;
		}
	}
	
	public static void importXML(File file) throws Exception {
		try{	
			Unmarshaller jaxbUnmarshaller = JAXBContext.newInstance(Domains.class).createUnmarshaller();
			Domains domains = (Domains) jaxbUnmarshaller.unmarshal(file);
			
			if(domains.machine != null){
				for(Machine m : domains.machine){
					m.save();
				}
				Machinery.saveAll();
				Machinery.loadAll();
			}
			if(domains.field != null){
				for(Field f : domains.field){
					f.save();
				}
				
				Fields.saveAll();
				Points.saveAll();
				
				Points.loadAll();
				Fields.loadAll();
			}
		}catch(JAXBException | SQLException e){
			logger.info(e);
			throw new Exception(e);
		}
	}
}
