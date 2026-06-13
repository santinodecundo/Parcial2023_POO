public interface Condicion {
	boolean cumple(Tarea tt);
}
public class CondicionNombreContiene implements Condicion {
	private String texto;
	public CondicionNombreContiene(String texto) {
		this.texto = texto;
	}
	public boolean cumple(Tarea tt) {
		return tt.getNombre().contains(texto);
	}
}
public class CondicionRequisitosContiene implements Condicion {
	private String texto;
	public CondicionRequisitosContiene(String texto) {
		this.texto = texto;
	}
	public boolean cumple(Tarea tt) {
		return tt.getRequisitos().contains(texto.toUpperCase());
	}
}
public class CondicionCostoSuperior implements Condicion {
	private double costo;
	public CondicionCostoSuperior(double costo) {
		this.costo = costo;
	}
	public boolean cumple(Tarea tt) {
		return tt.getCosto()>costo;
	}
}
public class CondicionHorasSuperior implements Condicion {
	private double horas;
	public CondicionHorasSuperior(double horas) {
		this.horas = horas;
	}
	public boolean cumple(Tarea tt) {
		return tt.getHoras()>horas;
	}
}
public class CondicionAND implements Condicion {
	private Condicion c1, c2;
	public CondicionAND(Condicion c1, Condicion c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	public boolean cumple(Tarea tt) {
		return c1.cumple(tt)&&c2.cumple(tt);
	}
}
public class CondicionOR implements Condicion {
	private Condicion c1, c2;
	public CondicionOR(Condicion c1, Condicion c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	public boolean cumple(Tarea tt) {
		return c1.cumple(tt)||c2.cumple(tt);
	}
}
public class CondicionNOT implements Condicion {
	private Condicion c;
	public CondicionNOT(Condicion c) {
		this.c = c;
	}
	public boolean cumple(Tarea tt) {
		return !c.cumple(tt);
	}
}
public abstract class ElementoProyecto {
	private String nombre;
	public ElementoProyecto(String nombre) {
		this.nombre = nombre;
	}
	public String getNombre() {
		return nombre;
	}
	public abstract double getHoras();
	public abstract double getCosto();
	public abstract int getCantidadTareas();
	public abstract Tarea getTareaMasCara();
	public abstract ElementoProyecto getCopia(Condicion cc);
}
public class Tarea extends ElementoProyecto implements Comparable<Tarea> {
	private ArrayList<String> requisitos;
	private double horas, costoPorHora;
	public Tarea(String nombre, double horas, double costoPorHora) {
		super(nombre);
		this.horas = horas;
		this.costoPorHora = costoPorHora;
		requisitos = new ArrayList<>();
	}
	public double getHoras() {
		return horas;
	}
	public double getCosto() {
		return horas*costoPorHora;
	}
	public int getCantidadTareas() {
		return 1;
	}
	public Tarea getTareaMasCara() {
		return this;
	}
	public int compareTo(Tarea otro) {
		double costoEste = getCosto();
		double costoOtro = otro.getCosto();
		if(costoEste>costoOtro) 
			return 1;
		else 
			if(costoOtro>costoEste) 
				return -1;
			else
				return 0;
	}
	public ElementoProyecto getCopia(Condicion cc) {
		if(cc.cumple(this)) 
			return getCopia();
		else 
			return null;
	}
	public Tarea getCopia() {
		Tarea copia = new Tarea(super.getNombre(), horas, costoPorHora);
		for(String ss : requisitos) 
			copia.addRequisito(ss);
		return copia;
	}
	public void addRequisito(String ss) {
		if(!requisitos.contains(ss.toUpperCase()))
			requisitos.add(ss.toUpperCase());
	}
	public ArrayList<String> getRequisitos() {
		return new ArrayList<String>(requisitos);
	}
	public double getCostoPorHora() {
		return costoPorHora;
	}
}
public class Modulo extends ElementoProyecto {
	private String responsable;
	private ArrayList<ElementoProyecto> contenido;
	private double descuento;
	private static double descuentoMax = 35;
	public Modulo(String nombre, String responsable, double descuento) {
		super(nombre);
		this.responsable = responsable;
		this.descuento = descuento;
		contenido = new ArrayList<>();
	}
	public static void setDescuentoMax(double descuento) {
		descuentoMax = descuento;
	}
	public void addContenido(ElementoProyecto ee) {
		if(!contenido.contains(ee))
			contenido.add(ee);
	}
	public double getHoras() {
		double suma = 0;
		for(ElementoProyecto ee : contenido) 
			suma += ee.getHoras();
		return suma;
	}
	public double getCosto() {
		double suma = 0;
		for(ElementoProyecto ee : contenido)
			suma += ee.getCosto();
		return suma;
	}
	public int getCantidadTareas() {
		int suma = 0;
		for(ElementoProyecto ee : contenido) 
			suma += ee.getCantidadTareas();
		return suma;
	}
	public Tarea getTareaMasCara() {
		ArrayList<Tarea> posibles = new ArrayList<>();
		for(ElementoProyecto ee : contenido) 
			posibles.add(ee.getTareaMasCara());
		return Collections.max(posibles);
	}
	public ElementoProyecto getCopia(Condicion cc) {
		ArrayList<ElementoProyecto> contenidoCopia = new ArrayList<>();
		for(ElementoProyecto ee : contenido)
			contenidoCopia.add(ee.getCopia(cc));
		if(contenidoCopia.isEmpty())
			return null;
		Modulo copia = new Modulo(super.getNombre(), responsable, descuento);
		for(ElementoProyecto ee : contenidoCopia) 
			copia.addContenido(ee);
		return copia;
	}
}
public class ModuloDesarrolloExterno extends Modulo {
	public ModuloDesarrolloExterno(String nombre, String responsable, double descuento) {
		super(nombre, responsable, descuento);
	}
	public ElementoProyecto getCopia(Condicion cc) {
		return null;
	}
}
public class TareaConsultoriaEspecializada extends Tarea {
	private double otroCosto;
	private Condicion condicion;
	public TareaConsultoriaEspecializada(String nombre, double horas, double costoPorHora, double otroCosto, Condicion condicion) {
		super(nombre, horas, costoPorHora);
		this.otroCosto = otroCosto;
		this.condicion = condicion;
	}
	public Tarea getCopia() {
		return new TareaConsultoriaEspecializada(super.getNombre(), super.getHoras(), super.getCostoPorHora(), otroCosto, condicion);
	}
	public double getCosto() {
		if(condicion.cumple(this)) 
			return super.getCosto();
		else
			return otroCosto;
	}
}
