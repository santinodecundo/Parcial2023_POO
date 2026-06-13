import java.util.ArrayList;

public interface CalculadorAlquiler {
	double calcularPrecio(Equipo eq);
}
public class CalculadorMontoFijo implements CalculadorAlquiler {
	private double montoFijo;
	public CalculadorMontoFijo(double montoFijo) {
		this.montoFijo = montoFijo;
	}
	public double calcularPrecio(Equipo eq) {
		return montoFijo;
	}
}
public class CalculadorPorcentaje implements CalculadorAlquiler {
	private double porcentaje;
	public CalculadorPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	public double calcularPrecio(Equipo eq) {
		return eq.getValor()*porcentaje/100;
	}
}
public class CalculadorMontoYPorcentaje implements CalculadorAlquiler {
	private double montoFijo, porcentaje;
	public CalculadorMontoYPorcentaje(double montoFijo, double porcentaje) {
		this.montoFijo = montoFijo;
		this.porcentaje = porcentaje;
	}
	public double calcularPrecio(Equipo eq) {
		return eq.getValor()*porcentaje/100 + montoFijo;
	}
}
public interface Condicion {
	boolean cumple(Equipo eq);
}
public class CondicionContienePalabra implements Condicion {
	private String palabra;
	public CondicionContienePalabra(String palabra) {
		this.palabra = palabra;
	}
	public boolean cumple(Equipo eq) {
		String[] palabrasDescripcion = eq.getDescripcion().split(" ");
		boolean encontrado = false;
		int i = 0;
		while(i<palabrasDescripcion.length&&!encontrado) {
			if(palabrasDescripcion[i].equalsIgnoreCase(palabra))
				encontrado = true;
			i++;
		}
		return encontrado;
	}
}
public class CondicionAntiguedadMayor implements Condicion {
	private int antiguedad;
	public CondicionAntiguedadMayor(int antiguedad) {
		this.antiguedad = antiguedad;
	}
	public boolean cumple(Equipo eq) {
		return eq.getAntiguedad()>antiguedad;
	}
}
public class CondicionValorMenor implements Condicion {
	private double valor;
	public CondicionValorMenor(double valor) {
		this.valor = valor;
	}
	public boolean cumple(Equipo eq) {
		return eq.getValor()<valor;
	}
}
public class CondicionIdentificacionIgual implements Condicion {
	private int id;
	public CondicionIdentificacionIgual(int id) {
		this.id = id;
	}
	public boolean cumple(Equipo eq) {
		return eq.getId()==id;
	}
}
public class CondicionAND implements Condicion {
	private Condicion c1, c2;
	public CondicionAND(Condicion c1, Condicion c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	public boolean cumple(Equipo eq) {
		return c1.cumple(eq)&&c2.cumple(eq);
	}
}
public class CondicionOR implements Condicion {
	private Condicion c1, c2;
	public CondicionOR(Condicion c1, Condicion c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	public boolean cumple(Equipo eq) {
		return c1.cumple(eq)||c2.cumple(eq);
	}
}
public class CondicionNOT implements Condicion {
	private Condicion c;
	public CondicionNOT(Condicion c) {
		this.c = c;
	}
	public boolean cumple(Equipo eq) {
		return !c.cumple(eq);
	}
}
public abstract class ElementoComputadoras {
	private int id;
	public ElementoComputadoras(int id) {
		this.id = id;
	}
	public abstract int getAntiguedad();
	public abstract double getValor();
	public abstract double getAlquiler();
	public abstract ArrayList<Equipo> buscar(Condicion cc);
	public int getId() {
		return id;
	}
}
public class Equipo extends ElementoComputadoras {
	private String descripcion;
	private double valor;
	private int antiguedad;
	CalculadorAlquiler calculador;
	public Equipo(int id, String descripcion, double valor, int antiguedad, CalculadorAlquiler calculador) {
		super(id);
		this.descripcion = descripcion;
		this.valor = valor;
		this.antiguedad = antiguedad;
		this.calculador = calculador;
	}
	public void setCalculador(CalculadorAlquiler cl) {
		calculador = cl;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public double getValor() {
		return valor;
	}
	public int getAntiguedad() {
		return antiguedad;
	}
	public double getAlquiler() {
		return calculador.calcularPrecio(this);
	}
	public ArrayList<Equipo> buscar(Condicion cc) {
		ArrayList<Equipo> retorno = new ArrayList<>();
		if(cc.cumple(this))
			retorno.add(this);
		return retorno;
	}
}
public class Combo extends ElementoComputadoras {
	private ArrayList<ElementoComputadoras> contenido;
	public Combo(int id) {
		super(id);
		contenido = new ArrayList<>();
	}
	public void addElemento(ElementoComputadoras ee) {
		if(!contenido.contains(ee))
			contenido.add(ee);
	}
	public double getValor() {
		double suma = 0;
		for(ElementoComputadoras ee : contenido) 
			suma += ee.getValor();
		return suma;
	}
	public int getAntiguedad() {
		int mayor = 0;
		for(ElementoComputadoras ee : contenido) {
			int aux = ee.getAntiguedad();
			if(aux>mayor)
				mayor = aux;
		}
		return mayor;
	}
	public double getAlquiler() {
		double suma = 0;
		for(ElementoComputadoras ee : contenido) 
			suma += ee.getAlquiler();
		return suma;
	}
	public ArrayList<Equipo> buscar(Condicion cc) {
		ArrayList<Equipo> retorno = new ArrayList<>();
		for(ElementoComputadoras ee : contenido)
			retorno.addAll(ee.buscar(cc));
		return retorno;
	}
}
public class ElementoConObsolencia extends ElementoComputadoras {
	private int antiguedadMax;
	private ElementoComputadoras elemento;
	private static double perdidaValor = 2;
	private static double descuentoAlquiler = 50;
	public ElementoConObsolencia(int id, int antiguedadMax, ElementoComputadoras elemento) {
		super(id);
		this.antiguedadMax = antiguedadMax;
		this.elemento = elemento;
	}
	public static void setPerdidaValor(double a) {
		perdidaValor = a;
	}
	public static void setDescuentoAlquiler(double a) {
		descuentoAlquiler = a;
	}
	public int getAntiguedad() {
		return elemento.getAntiguedad();
	}
	public double getValor() {
		int diferencia = elemento.getAntiguedad() - antiguedadMax;
		if(diferencia<=0)
			return elemento.getValor();
		else 
			return elemento.getValor()-elemento.getValor()*perdidaValor*diferencia/100;
	}
	public double getAlquiler() {
		if(elemento.getAntiguedad()<=antiguedadMax)
			return elemento.getAlquiler();
		else 
			return elemento.getAlquiler()*descuentoAlquiler/100;
	}
	public ArrayList<Equipo> buscar(Condicion cc) {
		if(elemento.getAntiguedad()<=antiguedadMax)
			return elemento.buscar(cc);
		return new ArrayList<Equipo>();
	}
}
