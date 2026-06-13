import java.util.ArrayList;

public interface Condicion {
	boolean cumple(ElementoTrabajo ee);
}
public class CondicionTiempoMenor implements Condicion {
	private double tiempo;
	public CondicionTiempoMenor(double tiempo) {
		this.tiempo = tiempo;
	}
	public boolean cumple(ElementoTrabajo ee) {
		return ee.getTiempo()<tiempo;
	}
}
public class CondicionEmpleadosMenor implements Condicion {
	private int empleados;
	public CondicionEmpleadosMenor(int empleados) {
		this.empleados = empleados;
	}
	public boolean cumple(ElementoTrabajo ee) {
		return ee.getEmpleados()<empleados;
	}
}
public class CondicionCostoMenor implements Condicion {
	private double costo;
	public CondicionCostoMenor(double costo) {
		this.costo = costo;
	}
	public boolean cumple(ElementoTrabajo ee) {
		return ee.getCosto()<costo;
	}
}
public class CondicionMaterial implements Condicion {
	private String mat;
	public CondicionMaterial(String mat) {
		this.mat = mat;
	}
	public boolean cumple(ElementoTrabajo ee) {
		return ee.getMateriales().contains(mat.toUpperCase());
	}
}
public class CondicionRequisito implements Condicion {
	private String req;
	public CondicionRequisito(String req) {
		this.req = req;
	}
	public boolean cumple(ElementoTrabajo ee) {
		return ee.getRequisitos().contains(req.toUpperCase());
	}
}
public class CondicionAND implements Condicion {
	private Condicion c1, c2;
	public CondicionAND(Condicion c1, Condicion c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	public boolean cumple(ElementoTrabajo ee) {
		return c1.cumple(ee)&&c2.cumple(ee);
	}
}
public class CondicionOR implements Condicion {
	private Condicion c1, c2;
	public CondicionOR(Condicion c1, Condicion c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	public boolean cumple(ElementoTrabajo ee) {
		return c1.cumple(ee)||c2.cumple(ee);
	}
}
public class CondicionNOT implements Condicion {
	private Condicion c;
	public CondicionNOT(Condicion c) {
		this.c = c;
	}
	public boolean cumple(ElementoTrabajo ee) {
		return !c.cumple(ee);
	}
}
public abstract class ElementoTrabajo {
	private String nombre;
	public ElementoTrabajo(String nombre) {
		this.nombre = nombre;
	}
	public abstract double getCosto();
	public abstract ArrayList<String> getMateriales();
	// No especifica de que forma se obtienen los requisitos por lo que
	// Directamente tomare la union y con rep
	public abstract ArrayList<String> getRequisitos();
	public abstract double getTiempo();
	public abstract int getEmpleados();
	public abstract ElementoTrabajo getCopia(Condicion cc);
	public String getNombre() {
		return nombre;
	}
	public abstract ArrayList<Tarea> buscar(Condicion cc);
}
public abstract class Presupuesto extends ElementoTrabajo {
	private ArrayList<ElementoTrabajo> tareas;
	public Presupuesto(String nombre) {
		super(nombre);
		tareas = new ArrayList<>();
	}
	public ArrayList<ElementoTrabajo> getTareas() {
		return new ArrayList<ElementoTrabajo>(tareas);
	}
	public void addTarea(ElementoTrabajo ee) {
		if(!tareas.contains(ee)) 
			tareas.add(ee);
	}
	public ArrayList<String> getRequisitos() {
		ArrayList<String> retorno = new ArrayList<>();
		for(ElementoTrabajo ee : tareas) {
			ArrayList<String> aux = ee.getRequisitos();
			for(String ss : aux)
				retorno.add(ss);
		}
		return retorno;
	}
	public ArrayList<Tarea> buscar(Condicion cc) {
		ArrayList<Tarea> retorno = new ArrayList<>();
		for(ElementoTrabajo ee : tareas)
			retorno.addAll(ee.buscar(cc));
		return retorno;
	}
}
public class PresupuestoUrgente extends Presupuesto {
	private static double extra = 50;
	public PresupuestoUrgente(String nombre) {
		super(nombre);
	}
	public void setExtra(double ext) {
		extra = ext;
	}
	public double getCosto() {
		double suma = 0;
		for(ElementoTrabajo ee : super.getTareas()) 
			suma += ee.getCosto();
		return suma+suma*extra/100;
	}
	public ArrayList<String> getMateriales() {
		ArrayList<String> retorno = new ArrayList<>();
		for(ElementoTrabajo ee : super.getTareas()) {
			ArrayList<String> aux = ee.getMateriales();
			retorno.addAll(aux);
		}
		return retorno;
	}
	public double getTiempo() {
		double mayor = 0;
		for(ElementoTrabajo ee : super.getTareas()) {
			double aux = ee.getTiempo();
			if(aux>mayor) 
				mayor = aux;
		}
		return mayor;
	}
	public int getEmpleados() {
		int suma = 0;
		for(ElementoTrabajo ee : super.getTareas()) 
			suma += ee.getEmpleados();
		return suma;
	}
	public ElementoTrabajo getCopia(Condicion cc) {
		PresupuestoUrgente copia = new PresupuestoUrgente(super.getNombre());
		boolean vacia = true;
		for(ElementoTrabajo ee : super.getTareas()) {
			ElementoTrabajo copiaAux = ee.getCopia(cc);
			if(copiaAux!=null) {
				vacia = false;
				copia.addTarea(copiaAux);
			}
		}
		if(vacia == true)
			return null;
		else 
			return copia;
	}
}
public class PresupuestoComplejo extends Presupuesto {
	private double descuento;
	public PresupuestoComplejo(String nombre, double descuento) {
		super(nombre);
		this.descuento = descuento;
	}
	public double getCosto() {
		double suma = 0;
		for(ElementoTrabajo ee : super.getTareas())
			suma += ee.getCosto();
		return suma-suma*descuento/100;
	}
	public ArrayList<String> getMateriales() {
		ArrayList<String> retorno = new ArrayList<>();
		for(ElementoTrabajo ee : super.getTareas()) {
			ArrayList<String> aux = ee.getMateriales();
			for(String ss : aux) {
				if(!retorno.contains(ss))
					retorno.add(ss);
			}
		}
		return retorno;
	}
	public double getTiempo() {
		double suma = 0;
		for(ElementoTrabajo ee : super.getTareas()) {
			suma += ee.getTiempo();
		}
		return suma;
	}
	public int getEmpleados() {
		int max = 0;
		for(ElementoTrabajo ee : super.getTareas()) {
			int aux = ee.getEmpleados();
			if(aux>max)
				max = aux;
		}
		return max;
	}
	public ElementoTrabajo getCopia(Condicion cc) {
		PresupuestoComplejo copia = new PresupuestoComplejo(super.getNombre(), descuento);
		boolean vacia = true;
		for(ElementoTrabajo ee : super.getTareas()) {
			ElementoTrabajo copiaAux = ee.getCopia(cc);
			if(copiaAux!=null) {
				vacia = false;
				copia.addTarea(copiaAux);
			}
		}
		if(vacia == true)
			return null;
		else 
			return copia;
	}
	public double getDescuento() {
		return descuento;
	}
}
public class PresupuestoComplejoAcotado extends PresupuestoComplejo {
	private Condicion cc;
	public PresupuestoComplejoAcotado(String nombre, double descuento, Condicion cc) {
		super(nombre, descuento);
		this.cc = cc;
	}
	public void setCondicion(Condicion cc) {
		this.cc = cc;
	}
	public void addTarea(ElementoTrabajo ee) {
		if(cc.cumple(ee)) 
			super.addTarea(ee);
	}
	public ElementoTrabajo getCopia(Condicion cc) {
		PresupuestoComplejoAcotado copia = new PresupuestoComplejoAcotado(super.getNombre(), super.getDescuento(), this.cc);
		boolean vacia = true;
		for(ElementoTrabajo ee : super.getTareas()) {
			ElementoTrabajo copiaAux = ee.getCopia(cc);
			if(copiaAux!=null) {
				vacia = false;
				copia.addTarea(copiaAux);
			}
		}
		if(vacia == true)
			return null;
		else 
			return copia;
	}
}
public class Tarea extends ElementoTrabajo {
	private ArrayList<String> materiales;
	private double costo, tiempo;
	private int empleados;
	public Tarea(String nombre, double costo, double tiempo, int empleados) {
		super(nombre);
		this.costo = costo;
		this.tiempo = tiempo;
		this.empleados = empleados;
		materiales = new ArrayList<>();
	}
	public double getCosto() {
		return costo;
	}
	public double getTiempo() {
		return tiempo;
	}
	public int getEmpleados() {
		return empleados;
	}
	public ArrayList<String> getMateriales() {
		return new ArrayList<String>(materiales);
	}
	public void addMaterial(String mat) {
		if(!materiales.contains(mat.toUpperCase()))
			materiales.add(mat.toUpperCase());
	}
	public ElementoTrabajo getCopia(Condicion cc) {
		if(cc.cumple(this)) {
			return getCopia();
		} else {
			return null;
		}
	}
	public ElementoTrabajo getCopia() {
		Tarea aux = new Tarea(super.getNombre(), costo, tiempo, empleados);
		for(String ss : materiales)
			aux.addMaterial(ss);
		return aux;
	}
	public ArrayList<String> getRequisitos() {
		return new ArrayList<>();
	}
	public ArrayList<Tarea> buscar(Condicion cc) {
		ArrayList<Tarea> retorno = new ArrayList<>();
		if(cc.cumple(this)) {
			retorno.add(this);
		}
		return retorno;
	}
}
public class TareaEspecial extends Tarea {
	private ArrayList<String> requisitos;
	public TareaEspecial(String nombre, double costo, double tiempo, int empleados) {
		super(nombre, costo, tiempo, empleados);
		requisitos = new ArrayList<>();
	}
	public ArrayList<String> getRequisitos() {
		return new ArrayList<String>(requisitos);
	}
	public void addRequisito(String ss) {
		if(!requisitos.contains(ss.toUpperCase()))
			requisitos.add(ss.toUpperCase());
	}
	public ElementoTrabajo getCopia() {
		TareaEspecial aux = new TareaEspecial(super.getNombre(), super.getCosto(), super.getTiempo(), super.getEmpleados());
		for(String ss : super.getMateriales())
			aux.addMaterial(ss);
		for(String ss : requisitos)
			aux.addRequisito(ss);
		return aux;
	}
}
