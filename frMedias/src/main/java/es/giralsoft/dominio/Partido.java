package es.giralsoft.dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.FIELD)
public class Partido {

	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fecha;
	private String rival;
	private boolean local;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "partido", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Participacion> participaciones;
	private String jornada;
	@Lob
	private String puntuaciones;
	@ManyToOne
	private Competicion competicion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getRival() {
		return rival;
	}

	public void setRival(String rival) {
		this.rival = rival;
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public List<Participacion> getParticipaciones() {
		return participaciones;
	}

	public void setParticipaciones(List<Participacion> participaciones) {
		this.participaciones = participaciones;
	}

	public void addParticipacion(Participacion participacion) {
		if (participaciones == null) {
			participaciones = new ArrayList<>();
		}
		participaciones.add(participacion);
	}

	public String getJornada() {
		return jornada;
	}

	public void setJornada(String jornada) {
		this.jornada = jornada;
	}

	public String getPuntuaciones() {
		return puntuaciones;
	}

	public void setPuntuaciones(String puntuaciones) {
		this.puntuaciones = puntuaciones;
	}

	public Competicion getCompeticion() {
		return competicion;
	}

	public void setCompeticion(Competicion competicion) {
		this.competicion = competicion;
	}

	@Override
	public String toString() {
		return "Partido [fecha=" + fecha + ", rival=" + rival + ", jornada=" + jornada + "]";
	}

}
