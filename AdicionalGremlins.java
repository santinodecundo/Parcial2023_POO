public interface Condicion {
	boolean cumple(Gremlin gg);
}
public class CondicionExpMinimo implements Condicion {
	private int minimoExp;
	public CondicionAñosMinimo(int minimoExp) {
		this.minimoExp = minimoExp;
	}
	public boolean cumple(Gremlin gg) {
		return gg.getExp()>minimoExp;
	}
}
public class CondicionMultiIgual implements Condicion {
	private double multi;
	public CondicionMultiIgual(double multi) {
		this.multi = multi;
	}
	public boolean cumple(Gremlin gg) {
		return gg.getMulti()==multi;
	}
}
public class CondicionVelocidadIgual implements Condicion {
	private double velocidad;
	public CondicionVelocidadIgual(double velocidad) {
		this.velocidad = velocidad;
	}
	public boolean cumple(Gremlin gg) {
		return gg.getVelocidad()==velocidad;
	}
}
public class CondicionTieneHabilidad implements Condicion {
	private String habilidad;
	public CondicionTieneHabilidad(String habilidad) {
		this.habilidad = habilidad;
	}
	public boolean cumple(Gremlin gg) {
		return gg.getHabilidades.contains(habilidad.toUpperCase());
	}
}
public class CondicionAND implements Condicion {
	private Condicion c1, c2;
	public CondicionAND(Condicion c1, Condicion c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	public boolean cumple(Gremlin gg) {
		return c1.cumple(gg)&&c2.cumple(gg);
	}
}
public class CondicionOR implements Condicion {
	private Condicion c1, c2;
	public CondicionOR(Condicion c1, Condicion c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	public boolean cumple(Gremlin gg) {
		return c1.cumple(gg)||c2.cumple(gg);
	}
}
public class CondicionNOT implements Condicion {
	private Condicion c;
	public CondicionNOT(Condicion c) {
		this.c = c;
	}
	public boolean cumple(Gremlin gg) {
		return !c.cumple(gg);
	}
}
public interface CalculadorMulti {
	double calcular(GremlinSuperior gg);
}
public class CalculadorMultiValorFijo implements CalculadorMulti {
	private double valorFijo;
	public CalculadorMultiValorFijo(double valorFijo) {
		this.valorFijo = valorFijo;
	}
	public double calcular(GremlinSuperior gg) {
		return valorFjo;
	}
}
public class CalculadorMultiVelocidad implements CalculadorMulti {
	public double calcular(GremlinSuperior gg) {
		return gg.getVelocidad();
	}
}
public class CalculadorMultiExp implements CalculadorMulti {
	public double calcular(GremlinSuperior gg) {
		return gg.getExp();
	}
}
public class CalculadorMultiVelocidadExp implements CalculadorMulti {
	public double calcular(GremlinSuperior gg) {
		return gg.getVelocidad()+gg.getExp();
	}
}
public abstract class ElementoCriatura {
	public abstract ArrayList<String> getHabilidades();
	public abstract double getVelocidad();
	public abstract double getMulti();
	public abstract ArrayList<Gremlin> buscar(Condicion cc);
	public abstract Gremlin getMasVeloz();
	public abstract ArrayList<Gremlin> getGremlins();
}
public class Gremlin extends ElementoCriatura implements Comparable<Gremlin> {
	private String nombre;
	private double velocidad, multi;
	private int exp;
	private ArrayList<String> habilidades;
	public Gremlin(String nombre, double velocidad, double multi, int exp) {
		this.nombre = nombre;
		this.velocidad = velocidad;
		this.multi = multi;
		this.exp = exp;
		habilidades = new ArrayList<>();
	}
	public void addHabilidad(String ss) {
		if(!habilidades.contains(ss.toUpperCase()))
			habilidades.add(ss.toUpperCase());
	}
	public ArrayList<String> getHabilidades() {
		return new ArrayList<String>(habilidades);
	}
	public double getVelocidad() {
		return velocidad;
	}
	public double getMulti() {
		return multi;
	}
	public int getExp() {
		return exp;
	}
	public ArrayList<Gremlin> buscar(Condicion cc) {
		ArrayList<Gremlin> retorno = new ArrayList<>();
		if(cc.cumple(this)) 
			retorno.add(this);
		return retorno;
	}
	public Gremlin getMasVeloz() {
		return this;
	}
	public int compareTo(Gremlin otro) {
		// Los gremlin se comparan naturalmente por velocidad para poder hallar el gremlin mas veloz en una horda
		if(velocidad>otro.getVelocidad()) 
			return 1;
		else
			if(otro.getVelocidad()>velocidad)
				return -1;
			else
				return 0;
	}
	public ArrayList<Gremlin> getGremlins() {
		ArrayList<Gremlin> retorno = new ArrayList<>();
		retorno.add(this);
		return retorno;
	}
}
public class GremlinRebeldeMalhumorado extends Gremlin {
	private double restaVelocidad, multiMulti;
	public GremlinRebeldeMalhumorado(String nombre, double velocidad, double multi, int exp, double restaVelocidad, double multiMulti) {
		super(nombre, velocidad, multi, exp);
		this.multiMulti = multiMulti;
		this.restaVelocidad = restaVelocidad;
	}
	public double getVelocidad() {
		return super.getVelocidad()-restaVelocidad;
	}
	public double getMulti() {
		return super.getMulti()*multiMulti;
	}
}
public class GremlinSuperior extends Gremlin {
	private CalculadorMulti calculador;
	public GremlinSuperior(String nombre, double velocidad, double multi, int exp, CalculadorMulti calculador) {
		super(nombre, velocidad, multi, exp);
		this.calculador = calculador;
	}
	public double getMulti() {
		return calculador.calcular(this);
	}
}
public abstract class Horda extends ElementoCriatura {
	private ArrayList<ElementoCriatura> contenido;
	public Horda() {
		contenido = new ArrayList<>();
	}
	public ArrayList<String> getHabilidades() {
		ArrayList<String> retorno = new ArrayList<>();
		for(ElementoCriatura ee : contenido) {
			ArrayList<String> aux = ee.getHabilidades();
			for(String ss : aux) {
				if(!retorno.contains(ss))
					retorno.add(ss);
			}
		}
		return retorno;
	}
	public void addElemento(ElementoCriatura ee) {
		if(!contenido.contains(ee))
			contenido.add(ee);
	}
	public Gremlin getMasVeloz() {
		ArrayList<Gremlin> candidatos = new ArrayList<>();
		for(ElementoCriatura ee : contenido)
			// No importa si se repiten pq el mayor seguira siendo el mismo
			candidatos.add(ee.getMasVeloz());
		return Collections.max(candidatos);
	}
	public double getVelocidad() {
		return getMasVeloz().getVelocidad();
	}
	public ArrayList<Gremlin> getGremlins() {
		ArrayList<Gremlin> retorno = new ArrayList<>();
		for(ElementoCriatura ee : contenido) {
			ArrayList<Gremlin> aux = ee.getGremlins();
			for(Gremlin gg : aux) {
				if(!retorno.contains(gg)) 
					retorno.add(gg);
			}
		}
		return retorno;
	}
}
public class ClanVeloz extends Horda {
	public double getMulti() {
		ArrayList<Gremlin> gremlins = this.getGremlins();
		if(gremlins.size()>=1) 
			return gremlins.get(0).getMulti();
		else
			return 0;
	}
}
public class HordaEstrategica extends Horda {
	public double getMulti() {
		ArrayList<Gremlin> gremlins = this.getGremlins();
		if(gremlins.size()>=2) 
			return gremlins.get(0).getMulti()+gremlins.get(1).getMulti();
		else
			if(gremlins.size()==1) 
				return gremlins.get(0).getMulti();
			else
				return 0;
	}
}
