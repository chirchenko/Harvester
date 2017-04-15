package domains;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import domains.Fields.Field;
import domains.Machinery.Machine;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Domains {
	
	@XmlElement
	public List<Field> field;
	@XmlElement
	public List<Machine> machine;
	
	public Domains() {
		super();
	}

	public Domains(List<Field> field, List<Machine> machine) {
		super();
		this.field = field;
		this.machine = machine;
	}
	
}
