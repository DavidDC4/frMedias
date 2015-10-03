package es.giralsoft.dominio;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

@Entity
@Access(AccessType.FIELD)
public class Participacion {

	@Id
	@GeneratedValue
	private Long id;
	private int goles;
	@Min(0)
	private int asistencias;
	@Min(0)
	private double nota;
	@ManyToOne(optional = false)
	private Jugador jugador;
	@ManyToOne
	private Partido partido;
	@Transient
	private int partidos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getGoles() {
		return goles;
	}

	public void setGoles(int goles) {
		this.goles = goles;
	}

	public int getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(int asistencias) {
		this.asistencias = asistencias;
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	public Partido getPartido() {
		return partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}

	public int getPartidos() {
		return partidos;
	}

	public void setPartidos(int partidos) {
		this.partidos = partidos;
	}

}
