package org.domain.sck.session.action;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

import org.domain.sck.dto.ConceptoUsuarioDTO;
import org.domain.sck.dto.NegocioUsuarioDTO;
import org.domain.sck.dto.SucursalesUsuarioDTO;
import org.domain.sck.dto.UsuarioIngresoDTO;
import org.domain.sck.dto.UsuarioSegurDTO;
import org.domain.sck.dto.ZonaUsuarioDTO;
import org.domain.sck.entity.CanalUsuarioCargo;
import org.domain.sck.entity.LcredCargos;
import org.domain.sck.entity.LcredPerfiles;
import org.domain.sck.entity.PerfilFuncionCanal;
import org.domain.sck.entity.Sociedad;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.UsuarioPerfil;
import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.entity.ZonaSucursalNegocioConcepto;
import org.domain.sck.entity.enums.ConceptosType;
import org.domain.sck.entity.enums.FuncionesType;
import org.domain.sck.entity.enums.TipoCuentasKupferType;
import org.domain.sck.session.service.intranetsap.IntranetSapService;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

@Name("usuarioAction")
@Scope(ScopeType.CONVERSATION)
public class UsuarioAction {

	@Logger
	private Log log;

	@In(value = "#{entityManager}")
	EntityManager entityManager;

	@In
	IntranetSapService intranetSapService;

	@In
	ScoringService scoringService;

	@In(value = "#{identity.usuarioSegur}", scope = ScopeType.SESSION)
	private UsuarioSegurDTO usuarioSegur;

	// @In(value="#{identity.usuarioCargo}", scope = ScopeType.SESSION)
	// private UsuarioCargo usuarioCargoAux;

	private String mensaje;
	private String accion;
	private List<Usuariosegur> listaUsuarios;
	private List<LcredPerfiles> listaPerfiles;
	private List<LcredPerfiles> listaUsuariosPerfilesSeleccionado;
	private List<UsuarioPerfil> listaUsuarioPerfiles;
	private List<LcredCargos> listaCargos;
	private List<UsuarioIngresoDTO> listaUsuariosDefinidos;
	private List<UsuarioCargo> listaUsuarioCargos;
	private String password;

	private boolean administrador;
	private boolean otrasSolictudes;
	private boolean noNotificaInicial;
	private boolean eliminado;

	private Usuariosegur usuario;
	private LcredCargos cargo;
	private Sociedad sociedadAux;

	private String[] conceptos = { "Z", "S", "N", "C" };
	private String[] canales;
	private String[] perfiles;
	private String[] perfiles2;

	private String[] perfilesGC;
	private String[] perfiles2GC;

	private String[] perfilesMX;
	private String[] perfiles2MX;

	private boolean zona;
	private boolean sucursal;
	private boolean negocio;
	private boolean concepto;

	private boolean pestanakupferExpres;
	private boolean pestanaGrandesCuentas;
	private boolean pestanaMixto;

	private int numeropestana;
	private int pestanaUno = 1;
	private int pestanaDos = 2;
	private int pestanaTres = 3;
	private int pestanacuarto = 4;
	private int pestanaCinco = 5;

	private Long montoDesdeKX;
	private Long montoHastaKX;
	private Long montoDesdeCompKX;
	private Long montoHastaCompKX;

	private Long montoDesdeGC;
	private Long montoHastaGC;
	private Long montoDesdeCompGC;
	private Long montoHastaCompGC;

	private Long montoDesdeMX;
	private Long montoHastaMX;
	private Long montoDesdeCompMX;
	private Long montoHastaCompMX;

	private boolean generarSolicitudes;
	private boolean aqpruebaRechaza;
	private boolean ejecutivoNegocio;
	private boolean analistaFinanciero;
	private boolean agregarComentarios;
	private boolean soloVisualizar;

	private boolean generarSolicitudesGC;
	private boolean aqpruebaRechazaGC;
	private boolean ejecutivoNegocioGC;
	private boolean analistaFinancieroGC;
	private boolean agregarComentariosGC;
	private boolean soloVisualizarGC;

	private boolean generarSolicitudesMX;
	private boolean aqpruebaRechazaMX;
	private boolean ejecutivoNegocioMX;
	private boolean analistaFinancieroMX;
	private boolean agregarComentariosMX;
	private boolean soloVisualizarMX;

	private final int NUMERO = 6;

	/* lista del kupfer express */
	private List<ZonaUsuarioDTO> listaZonaKxGs;
	private List<ZonaUsuarioDTO> listaZonaKxEn;
	private List<ZonaUsuarioDTO> listaZonaKxAr;
	private List<ZonaUsuarioDTO> listaZonaKxAf;
	private List<ZonaUsuarioDTO> listaZonaKxAc;
	private List<ZonaUsuarioDTO> listaZonaKxSv;

	private List<SucursalesUsuarioDTO> listaSucursalKxGs;
	private List<SucursalesUsuarioDTO> listaSucursalKxEn;
	private List<SucursalesUsuarioDTO> listaSucursalKxAr;
	private List<SucursalesUsuarioDTO> listaSucursalKxAf;
	private List<SucursalesUsuarioDTO> listaSucursalKxAc;
	private List<SucursalesUsuarioDTO> listaSucursalKxSv;

	private List<NegocioUsuarioDTO> listaNegocioKxGs;
	private List<NegocioUsuarioDTO> listaNegocioKxEn;
	private List<NegocioUsuarioDTO> listaNegocioKxAr;
	private List<NegocioUsuarioDTO> listaNegocioKxAf;
	private List<NegocioUsuarioDTO> listaNegocioKxAc;
	private List<NegocioUsuarioDTO> listaNegocioKxSv;

	private List<ConceptoUsuarioDTO> listaConceptoKxGs;
	private List<ConceptoUsuarioDTO> listaConceptoKxEn;
	private List<ConceptoUsuarioDTO> listaConceptoKxAr;
	private List<ConceptoUsuarioDTO> listaConceptoKxAf;
	private List<ConceptoUsuarioDTO> listaConceptoKxAc;
	private List<ConceptoUsuarioDTO> listaConceptoKxSv;

	/* lista del Grandes Cuentas */
	private List<ZonaUsuarioDTO> listaZonaGcGs;
	private List<ZonaUsuarioDTO> listaZonaGcEn;
	private List<ZonaUsuarioDTO> listaZonaGcAr;
	private List<ZonaUsuarioDTO> listaZonaGcAf;
	private List<ZonaUsuarioDTO> listaZonaGcAc;
	private List<ZonaUsuarioDTO> listaZonaGcSv;

	private List<SucursalesUsuarioDTO> listaSucursalGcGs;
	private List<SucursalesUsuarioDTO> listaSucursalGcEn;
	private List<SucursalesUsuarioDTO> listaSucursalGcAr;
	private List<SucursalesUsuarioDTO> listaSucursalGcAf;
	private List<SucursalesUsuarioDTO> listaSucursalGcAc;
	private List<SucursalesUsuarioDTO> listaSucursalGcSv;

	private List<NegocioUsuarioDTO> listaNegocioGcGs;
	private List<NegocioUsuarioDTO> listaNegocioGcEn;
	private List<NegocioUsuarioDTO> listaNegocioGcAr;
	private List<NegocioUsuarioDTO> listaNegocioGcAf;
	private List<NegocioUsuarioDTO> listaNegocioGcAc;
	private List<NegocioUsuarioDTO> listaNegocioGcSv;

	private List<ConceptoUsuarioDTO> listaConceptoGcGs;
	private List<ConceptoUsuarioDTO> listaConceptoGcEn;
	private List<ConceptoUsuarioDTO> listaConceptoGcAr;
	private List<ConceptoUsuarioDTO> listaConceptoGcAf;
	private List<ConceptoUsuarioDTO> listaConceptoGcAc;
	private List<ConceptoUsuarioDTO> listaConceptoGcSv;

	/* lista del Mixto */
	private List<ZonaUsuarioDTO> listaZonaMxGs;
	private List<ZonaUsuarioDTO> listaZonaMxEn;
	private List<ZonaUsuarioDTO> listaZonaMxAr;
	private List<ZonaUsuarioDTO> listaZonaMxAf;
	private List<ZonaUsuarioDTO> listaZonaMxAc;
	private List<ZonaUsuarioDTO> listaZonaMxSv;

	private List<SucursalesUsuarioDTO> listaSucursalMxGs;
	private List<SucursalesUsuarioDTO> listaSucursalMxEn;
	private List<SucursalesUsuarioDTO> listaSucursalMxAr;
	private List<SucursalesUsuarioDTO> listaSucursalMxAf;
	private List<SucursalesUsuarioDTO> listaSucursalMxAc;
	private List<SucursalesUsuarioDTO> listaSucursalMxSv;

	private List<NegocioUsuarioDTO> listaNegocioMxGs;
	private List<NegocioUsuarioDTO> listaNegocioMxEn;
	private List<NegocioUsuarioDTO> listaNegocioMxAr;
	private List<NegocioUsuarioDTO> listaNegocioMxAf;
	private List<NegocioUsuarioDTO> listaNegocioMxAc;
	private List<NegocioUsuarioDTO> listaNegocioMxSv;

	private List<ConceptoUsuarioDTO> listaConceptoMxGs;
	private List<ConceptoUsuarioDTO> listaConceptoMxEn;
	private List<ConceptoUsuarioDTO> listaConceptoMxAr;
	private List<ConceptoUsuarioDTO> listaConceptoMxAf;
	private List<ConceptoUsuarioDTO> listaConceptoMxAc;
	private List<ConceptoUsuarioDTO> listaConceptoMxSv;

	/* kupfer Express */
	private String marcarZonaKxGs;
	private String marcarSucursalKxGs;
	private String marcarNegocioKxGs;
	private String marcarConceptoKxGs;

	private String marcarZonaKxEn;
	private String marcarSucursalKxEn;
	private String marcarNegocioKxEn;
	private String marcarConceptoKxEn;

	private String marcarZonaKxAr;
	private String marcarSucursalKxAr;
	private String marcarNegocioKxAr;
	private String marcarConceptoKxAr;

	private String marcarZonaKxAf;
	private String marcarSucursalKxAf;
	private String marcarNegocioKxAf;
	private String marcarConceptoKxAf;

	private String marcarZonaKxAc;
	private String marcarSucursalKxAc;
	private String marcarNegocioKxAc;
	private String marcarConceptoKxAc;

	private String marcarZonaKxSv;
	private String marcarSucursalKxSv;
	private String marcarNegocioKxSv;
	private String marcarConceptoKxSv;

	/* grandes cuentas */
	private String marcarZonaGcGs;
	private String marcarSucursalGcGs;
	private String marcarNegocioGcGs;
	private String marcarConceptoGcGs;

	private String marcarZonaGcEn;
	private String marcarSucursalGcEn;
	private String marcarNegocioGcEn;
	private String marcarConceptoGcEn;

	private String marcarZonaGcAr;
	private String marcarSucursalGcAr;
	private String marcarNegocioGcAr;
	private String marcarConceptoGcAr;

	private String marcarZonaGcAf;
	private String marcarSucursalGcAf;
	private String marcarNegocioGcAf;
	private String marcarConceptoGcAf;

	private String marcarZonaGcAc;
	private String marcarSucursalGcAc;
	private String marcarNegocioGcAc;
	private String marcarConceptoGcAc;

	private String marcarZonaGcSv;
	private String marcarSucursalGcSv;
	private String marcarNegocioGcSv;
	private String marcarConceptoGcSv;

	/* Mixto */
	private String marcarZonaMxGs;
	private String marcarSucursalMxGs;
	private String marcarNegocioMxGs;
	private String marcarConceptoMxGs;

	private String marcarZonaMxEn;
	private String marcarSucursalMxEn;
	private String marcarNegocioMxEn;
	private String marcarConceptoMxEn;

	private String marcarZonaMxAr;
	private String marcarSucursalMxAr;
	private String marcarNegocioMxAr;
	private String marcarConceptoMxAr;

	private String marcarZonaMxAf;
	private String marcarSucursalMxAf;
	private String marcarNegocioMxAf;
	private String marcarConceptoMxAf;

	private String marcarZonaMxAc;
	private String marcarSucursalMxAc;
	private String marcarNegocioMxAc;
	private String marcarConceptoMxAc;

	private String marcarZonaMxSv;
	private String marcarSucursalMxSv;
	private String marcarNegocioMxSv;
	private String marcarConceptoMxSv;

	private List<CanalUsuarioCargo> listaCanalUsuarioCargos;
	private List<PerfilFuncionCanal> listaPerfilFuncionCanals;
	private List<ZonaSucursalNegocioConcepto> listaZonaSucursalNegocioConceptos;
	private List<Sociedad> listaSociedad;

	@SuppressWarnings("unchecked")
	@Create
	public void init() {
		log.debug("iniciando UsuarioPerfilAction...");

		try {
			sacarUsuarios();

			List<LcredCargos> cargos = (List<LcredCargos>) entityManager
					.createQuery("select lc from LcredCargos lc")
					.getResultList();
			
			if (cargos != null) {
				this.setListaCargos(cargos);
			}
			
			List<Sociedad> sociedades = (List<Sociedad>) entityManager
					.createQuery("select s from Sociedad s")
					.getResultList();
			if (sociedades != null) {
				this.setListaSociedad(sociedades);
			}			

			if (this.listaUsuariosDefinidos == null) {
				this.setListaUsuariosDefinidos(scoringService
						.getSacarListaUsuarios());
			}

			sacarUsuariosNuevosDefinidos();

			this.setZona(false);
			this.setSucursal(false);
			this.setNegocio(false);
			this.setConcepto(false);

			/* kupfer Express */
			this.setMarcarZonaKxGs("Marcar");
			this.setMarcarSucursalKxGs("Marcar");
			this.setMarcarNegocioKxGs("Marcar");
			this.setMarcarConceptoKxGs("Marcar");

			this.setMarcarZonaKxEn("Marcar");
			this.setMarcarSucursalKxEn("Marcar");
			this.setMarcarNegocioKxEn("Marcar");
			this.setMarcarConceptoKxEn("Marcar");

			this.setMarcarZonaKxAr("Marcar");
			this.setMarcarSucursalKxAr("Marcar");
			this.setMarcarNegocioKxAr("Marcar");
			this.setMarcarConceptoKxAr("Marcar");

			this.setMarcarZonaKxAf("Marcar");
			this.setMarcarSucursalKxAf("Marcar");
			this.setMarcarNegocioKxAf("Marcar");
			this.setMarcarConceptoKxAf("Marcar");

			this.setMarcarZonaKxAc("Marcar");
			this.setMarcarSucursalKxAc("Marcar");
			this.setMarcarNegocioKxAc("Marcar");
			this.setMarcarConceptoKxAc("Marcar");

			this.setMarcarZonaKxSv("Marcar");
			this.setMarcarSucursalKxSv("Marcar");
			this.setMarcarNegocioKxSv("Marcar");
			this.setMarcarConceptoKxSv("Marcar");

			/* grandes cuentas */
			this.setMarcarZonaGcGs("Marcar");
			this.setMarcarSucursalGcGs("Marcar");
			this.setMarcarNegocioGcGs("Marcar");
			this.setMarcarConceptoGcGs("Marcar");

			this.setMarcarZonaGcEn("Marcar");
			this.setMarcarSucursalGcEn("Marcar");
			this.setMarcarNegocioGcEn("Marcar");
			this.setMarcarConceptoGcEn("Marcar");

			this.setMarcarZonaGcAr("Marcar");
			this.setMarcarSucursalGcAr("Marcar");
			this.setMarcarNegocioGcAr("Marcar");
			this.setMarcarConceptoGcAr("Marcar");

			this.setMarcarZonaGcAf("Marcar");
			this.setMarcarSucursalGcAf("Marcar");
			this.setMarcarNegocioGcAf("Marcar");
			this.setMarcarConceptoGcAf("Marcar");

			this.setMarcarZonaGcAc("Marcar");
			this.setMarcarSucursalGcAc("Marcar");
			this.setMarcarNegocioGcAc("Marcar");
			this.setMarcarConceptoGcAc("Marcar");

			this.setMarcarZonaGcSv("Marcar");
			this.setMarcarSucursalGcSv("Marcar");
			this.setMarcarNegocioGcSv("Marcar");
			this.setMarcarConceptoGcSv("Marcar");

			/* Mixto */
			this.setMarcarZonaMxGs("Marcar");
			this.setMarcarSucursalMxGs("Marcar");
			this.setMarcarNegocioMxGs("Marcar");
			this.setMarcarConceptoMxGs("Marcar");

			this.setMarcarZonaMxEn("Marcar");
			this.setMarcarSucursalMxEn("Marcar");
			this.setMarcarNegocioMxEn("Marcar");
			this.setMarcarConceptoMxEn("Marcar");

			this.setMarcarZonaMxAr("Marcar");
			this.setMarcarSucursalMxAr("Marcar");
			this.setMarcarNegocioMxAr("Marcar");
			this.setMarcarConceptoMxAr("Marcar");

			this.setMarcarZonaMxAf("Marcar");
			this.setMarcarSucursalMxAf("Marcar");
			this.setMarcarNegocioMxAf("Marcar");
			this.setMarcarConceptoMxAf("Marcar");

			this.setMarcarZonaMxAc("Marcar");
			this.setMarcarSucursalMxAc("Marcar");
			this.setMarcarNegocioMxAc("Marcar");
			this.setMarcarConceptoMxAc("Marcar");

			this.setMarcarZonaMxSv("Marcar");
			this.setMarcarSucursalMxSv("Marcar");
			this.setMarcarNegocioMxSv("Marcar");
			this.setMarcarConceptoMxSv("Marcar");

			for (int i = 0; i < this.NUMERO; i++) { /* llena de lista de zona */
				if (i == 0) {
					this.setListaZonaKxGs(intranetSapService
							.sacarListaZonas(true));
					this.setListaZonaGcGs(intranetSapService
							.sacarListaZonas(true));
					this.setListaZonaMxGs(intranetSapService
							.sacarListaZonas(true));
				} else if (i == 1) {
					this.setListaZonaKxEn(intranetSapService
							.sacarListaZonas(false));
					this.setListaZonaGcEn(intranetSapService
							.sacarListaZonas(false));
					this.setListaZonaMxEn(intranetSapService
							.sacarListaZonas(false));
				} else if (i == 2) {
					this.setListaZonaKxAr(intranetSapService
							.sacarListaZonas(false));
					this.setListaZonaGcAr(intranetSapService
							.sacarListaZonas(false));
					this.setListaZonaMxAr(intranetSapService
							.sacarListaZonas(false));
				} else if (i == 3) {
					this.setListaZonaKxAf(intranetSapService
							.sacarListaZonas(false));
					this.setListaZonaGcAf(intranetSapService
							.sacarListaZonas(false));
					this.setListaZonaMxAf(intranetSapService
							.sacarListaZonas(false));
				} else if (i == 4) {
					this.setListaZonaKxAc(intranetSapService
							.sacarListaZonas(false));
					this.setListaZonaGcAc(intranetSapService
							.sacarListaZonas(false));
					this.setListaZonaMxAc(intranetSapService
							.sacarListaZonas(false));
				} else if (i == 5) {
					this.setListaZonaKxSv(intranetSapService
							.sacarListaZonas(false));
					this.setListaZonaGcSv(intranetSapService
							.sacarListaZonas(false));
					this.setListaZonaMxSv(intranetSapService
							.sacarListaZonas(false));
				}
			}

			for (int i = 0; i < this.NUMERO; i++) {/*
													 * llena de lista de
													 * sucursales
													 */
				if (i == 0) {
					this.setListaSucursalKxGs(intranetSapService
							.sacarListaSucursales(true));
					this.setListaSucursalGcGs(intranetSapService
							.sacarListaSucursales(true));
					this.setListaSucursalMxGs(intranetSapService
							.sacarListaSucursales(true));
				} else if (i == 1) {
					this.setListaSucursalKxEn(intranetSapService
							.sacarListaSucursales(false));
					this.setListaSucursalGcEn(intranetSapService
							.sacarListaSucursales(false));
					this.setListaSucursalMxEn(intranetSapService
							.sacarListaSucursales(false));
				} else if (i == 2) {
					this.setListaSucursalKxAr(intranetSapService
							.sacarListaSucursales(false));
					this.setListaSucursalGcAr(intranetSapService
							.sacarListaSucursales(false));
					this.setListaSucursalMxAr(intranetSapService
							.sacarListaSucursales(false));
				} else if (i == 3) {
					this.setListaSucursalKxAf(intranetSapService
							.sacarListaSucursales(false));
					this.setListaSucursalGcAf(intranetSapService
							.sacarListaSucursales(false));
					this.setListaSucursalMxAf(intranetSapService
							.sacarListaSucursales(false));
				} else if (i == 4) {
					this.setListaSucursalKxAc(intranetSapService
							.sacarListaSucursales(false));
					this.setListaSucursalGcAc(intranetSapService
							.sacarListaSucursales(false));
					this.setListaSucursalMxAc(intranetSapService
							.sacarListaSucursales(false));
				} else if (i == 5) {
					this.setListaSucursalKxSv(intranetSapService
							.sacarListaSucursales(false));
					this.setListaSucursalGcSv(intranetSapService
							.sacarListaSucursales(false));
					this.setListaSucursalMxSv(intranetSapService
							.sacarListaSucursales(false));
				}
			}

			for (int i = 0; i < this.NUMERO; i++) {/*
													 * sacar la lista de
													 * negocios
													 */
				if (i == 0) {
					this.setListaNegocioKxGs(intranetSapService
							.sacarListaNegocios(true));
					this.setListaNegocioGcGs(intranetSapService
							.sacarListaNegocios(true));
					this.setListaNegocioMxGs(intranetSapService
							.sacarListaNegocios(true));
				} else if (i == 1) {
					this.setListaNegocioKxEn(intranetSapService
							.sacarListaNegocios(false));
					this.setListaNegocioGcEn(intranetSapService
							.sacarListaNegocios(false));
					this.setListaNegocioMxEn(intranetSapService
							.sacarListaNegocios(false));
				} else if (i == 2) {
					this.setListaNegocioKxAr(intranetSapService
							.sacarListaNegocios(false));
					this.setListaNegocioGcAr(intranetSapService
							.sacarListaNegocios(false));
					this.setListaNegocioMxAr(intranetSapService
							.sacarListaNegocios(false));
				} else if (i == 3) {
					this.setListaNegocioKxAf(intranetSapService
							.sacarListaNegocios(false));
					this.setListaNegocioGcAf(intranetSapService
							.sacarListaNegocios(false));
					this.setListaNegocioMxAf(intranetSapService
							.sacarListaNegocios(false));
				} else if (i == 4) {
					this.setListaNegocioKxAc(intranetSapService
							.sacarListaNegocios(false));
					this.setListaNegocioGcAc(intranetSapService
							.sacarListaNegocios(false));
					this.setListaNegocioMxAc(intranetSapService
							.sacarListaNegocios(false));
				} else if (i == 5) {
					this.setListaNegocioKxSv(intranetSapService
							.sacarListaNegocios(false));
					this.setListaNegocioGcSv(intranetSapService
							.sacarListaNegocios(false));
					this.setListaNegocioMxSv(intranetSapService
							.sacarListaNegocios(false));
				}
			}

			for (int i = 0; i < this.NUMERO; i++) {/*
													 * sacar las listas
													 * conceptos
													 */
				if (i == 0) {
					this.setListaConceptoKxGs(intranetSapService
							.sacarListaConceptos(true));
					this.setListaConceptoGcGs(intranetSapService
							.sacarListaConceptos(true));
					this.setListaConceptoMxGs(intranetSapService
							.sacarListaConceptos(true));
				} else if (i == 1) {
					this.setListaConceptoKxEn(intranetSapService
							.sacarListaConceptos(false));
					this.setListaConceptoGcEn(intranetSapService
							.sacarListaConceptos(false));
					this.setListaConceptoMxEn(intranetSapService
							.sacarListaConceptos(false));
				} else if (i == 2) {
					this.setListaConceptoKxAr(intranetSapService
							.sacarListaConceptos(false));
					this.setListaConceptoGcAr(intranetSapService
							.sacarListaConceptos(false));
					this.setListaConceptoMxAr(intranetSapService
							.sacarListaConceptos(false));
				} else if (i == 3) {
					this.setListaConceptoKxAf(intranetSapService
							.sacarListaConceptos(false));
					this.setListaConceptoGcAf(intranetSapService
							.sacarListaConceptos(false));
					this.setListaConceptoMxAf(intranetSapService
							.sacarListaConceptos(false));
				} else if (i == 4) {
					this.setListaConceptoKxAc(intranetSapService
							.sacarListaConceptos(false));
					this.setListaConceptoGcAc(intranetSapService
							.sacarListaConceptos(false));
					this.setListaConceptoMxAc(intranetSapService
							.sacarListaConceptos(false));
				} else if (i == 5) {
					this.setListaConceptoKxSv(intranetSapService
							.sacarListaConceptos(false));
					this.setListaConceptoGcSv(intranetSapService
							.sacarListaConceptos(false));
					this.setListaConceptoMxSv(intranetSapService
							.sacarListaConceptos(false));
				}
			}

			/* desahabiltacion de los canales */
			this.setPestanakupferExpres(true);
			this.setPestanaGrandesCuentas(true);
			this.setPestanaMixto(true);

			/* desahabilitar pestañas de los perfiles KX */
			this.setGenerarSolicitudes(true);
			this.setAqpruebaRechaza(true);
			this.setAgregarComentarios(true);
			this.setEjecutivoNegocio(true);
			this.setAnalistaFinanciero(true);
			this.setSoloVisualizar(true);

			/* desahabilitar pestañas de los perfiles GC */
			this.setGenerarSolicitudesGC(true);
			this.setAqpruebaRechazaGC(true);
			this.setAgregarComentariosGC(true);
			this.setEjecutivoNegocioGC(true);
			this.setAnalistaFinancieroGC(true);
			this.setSoloVisualizarGC(true);

			/* desahabilitar pestañas de los perfiles MX */
			this.setGenerarSolicitudesMX(true);
			this.setAqpruebaRechazaMX(true);
			this.setAgregarComentariosMX(true);
			this.setEjecutivoNegocioMX(true);
			this.setAnalistaFinancieroMX(true);
			this.setSoloVisualizarMX(true);

		} catch (Exception e) {
			log.error("Error, al sacar la lista de usuarios del sistema: #0",
					e.getMessage());
		}
		return;

	}

	@Destroy
	public void destroy() {
		log.debug("destruyendo instancia UsuarioPerfilAction");
	}

	@SuppressWarnings("unchecked")
	public void sacarUsuarios() {
		try {
			List<Usuariosegur> lista = (List<Usuariosegur>) entityManager
					.createQuery(
							"select u from Usuariosegur u where u.alias <> null and u.nombre <> null  order by u.nombre")
					.getResultList();

			if (lista != null) {
				listaUsuarios = new ArrayList<Usuariosegur>(0);
				for (Usuariosegur usuario : lista) {
					if (usuario != null && !usuario.getAlias().equals("")
							&& !usuario.getAlias().equals("xxxx")
							&& !usuario.getAlias().equals("xxxxxx")) {
						if (usuario.getNombre() != null
								&& !usuario.getNombre().equals("")) {
							if (!listaUsuarios.contains(usuario)) {
								listaUsuarios.add(usuario);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Error, al sacar los datos de usuarios #0",
					e.getMessage());
		}

	}

	public void eliminarUsuarioPerfil(UsuarioPerfil usutip) {
		if (usutip != null) {
			entityManager.remove(usutip);
			entityManager.flush();
			/* actualizar las lista y grilla */
			if (listaUsuariosPerfilesSeleccionado != null) {
				this.listaUsuariosPerfilesSeleccionado.clear();
			}
			if (listaPerfiles != null) {
				listaPerfiles.clear();
			}
			consultarUsuarioPerfil();
			sacarListaUsuarioPerfilesAsignados();

		}
	}

	@SuppressWarnings({ "unchecked" })
	public void consultarUsuarioPerfil() {
		String query = null;
		if (this.usuario == null) {
			setMensaje("Debe Seleccionar un usuario para consultar.");
			return;
		}

		try {
			query = " select lcp from LcredPerfiles lcp "
					+ "  where lcp.codPerfil not in ( select ut.perfil.codPerfil  "
					+ "                          from UsuarioPerfil ut"
					+ "                          where ut.usuario.alias=:usu)";

			log.debug("usuario #0", this.usuario.getAlias());
			List<LcredPerfiles> lista = (List<LcredPerfiles>) entityManager
					.createQuery(query)
					.setParameter("usu", this.usuario.getAlias())
					.getResultList();

			if (lista == null) {
				this.listaPerfiles = (List<LcredPerfiles>) entityManager
						.createQuery(" select t from LcredPerfiles t ")
						.getResultList();
			} else {
				setListaPerfiles(lista);
			}
			sacarListaUsuarioPerfilesAsignados();
		} catch (Exception e) {
			log.error("Error, al sacar la lista de tipo de usuario. #0",
					e.getMessage());
		}

	}

	public void asignarUsuarioPerfiles() {
		if (this.listaUsuariosPerfilesSeleccionado == null) {
			setMensaje("Tiene que seleccionar por lo menos un perfil de usuario antes de asignar.");
			return;
		}
		try {
			for (LcredPerfiles tipo : listaUsuariosPerfilesSeleccionado) {
				if (tipo != null) {
					UsuarioPerfil nuevo = new UsuarioPerfil();
					nuevo.setPerfil(tipo);
					nuevo.setUsuario(this.usuario);
					entityManager.persist(nuevo);
				}
			}
			entityManager.flush();
			setMensaje("Los perfiles de usuarios fueron asignado exitosamente...!!!");

			/* se vuelve a sacar los datos... */
			if (listaUsuariosPerfilesSeleccionado != null) {
				this.listaUsuariosPerfilesSeleccionado.clear();
			}
			if (listaPerfiles != null) {
				listaPerfiles.clear();
			}
			consultarUsuarioPerfil();
			sacarListaUsuarioPerfilesAsignados();

		} catch (Exception e) {
			log.error("Error, al sacar la lista de usuarios asignados.",
					e.getMessage());
		}
	}

	public void obtenerUsuario() {
		if (usuario != null) {
			log.error("Usuario : #0", usuario.getNombre());
			this.setPassword(usuario.getPassword());
			if (usuario.getEliminado() != null) {
				this.setEliminado(usuario.getEliminado());
			} else {
				this.setEliminado(false);
			}

			UsuarioCargo usuarioCargo = scoringService.getUsuarioCargo(usuario.getIdPersonal());
			if (usuarioCargo != null) {
				this.setCargo(usuarioCargo.getCargo());
				conceptos = new String[4];
				if (usuarioCargo.getZona() != null) {
					conceptos[0] = "Z";
					this.zona = true;
				} else {
					conceptos[0] = "";
				}

				if (usuarioCargo.getSucursal() != null) {
					conceptos[1] = "S";
					this.sucursal = true;
				} else {
					conceptos[1] = "";
				}

				if (usuarioCargo.getNegocio() != null) {
					conceptos[2] = "N";
					this.negocio = true;
				} else {
					conceptos[2] = "";
				}

				if (usuarioCargo.getConcepto() != null) {
					conceptos[3] = "C";
					this.concepto = true;
				} else {
					conceptos[3] = "";
				}

				if (usuarioCargo.getAdministrador() != null) {
					this.setAdministrador(usuarioCargo.getAdministrador());
				} else {
					this.setAdministrador(false);
				}

				if (usuarioCargo.getEnvioAutomatico() != null) {
					this.setOtrasSolictudes(usuarioCargo.getEnvioAutomatico());
				} else {
					this.setOtrasSolictudes(false);
				}

				if (usuarioCargo.getnoNotificaInicial() != null) {
					this.setnoNotificaInicial(usuarioCargo
							.getnoNotificaInicial());
				} else {
					this.setnoNotificaInicial(false);
				}

				listaCanalUsuarioCargos = scoringService
						.getListaCanalUsuarioCargo(usuarioCargo
								.getIdUsuarioCargo());
				if (listaCanalUsuarioCargos != null
						&& listaCanalUsuarioCargos.size() > 0) {
					long codigo = 0;
					for (CanalUsuarioCargo cuc : listaCanalUsuarioCargos) {
						listaPerfilFuncionCanals = scoringService
								.getListaPerfilFuncionCanal(
										cuc.getIdCanalUsuarioCargo(),
										listaPerfilFuncionCanals);
						if (listaPerfilFuncionCanals != null) {
							for (PerfilFuncionCanal pfc : listaPerfilFuncionCanals) {
								if (codigo != pfc.getIdPerfilFuncionCanal()) {
									listaZonaSucursalNegocioConceptos = scoringService
											.getListaZonaSucursalNegocioConcepto(
													pfc.getIdPerfilFuncionCanal(),
													listaZonaSucursalNegocioConceptos);
									codigo = pfc.getIdPerfilFuncionCanal();
								}
							}
						}
					}
				} else {
					this.setAccion("GUARDAR");
					return;
				}

				if (listaCanalUsuarioCargos != null) {
					canales = new String[3];
					for (CanalUsuarioCargo cuc : listaCanalUsuarioCargos) {
						if (cuc.getTipoCuenta().name()
								.equals(TipoCuentasKupferType.KX.name())) {
							canales[0] = "KX";
							pestanakupferExpres = false;
						} else if (cuc.getTipoCuenta().name()
								.equals(TipoCuentasKupferType.GC.name())) {
							canales[1] = "GC";
							pestanaGrandesCuentas = false;
						} else if (cuc.getTipoCuenta().name()
								.equals(TipoCuentasKupferType.MX.name())) {
							canales[2] = "MX";
							pestanaMixto = false;
						}
					}
				}

				if (listaPerfilFuncionCanals != null) {
					for (CanalUsuarioCargo cuc : listaCanalUsuarioCargos) {
						if (cuc.getTipoCuenta().name()
								.equals(TipoCuentasKupferType.KX.name())) {
							List<PerfilFuncionCanal> perfilFuncionCanales = new ArrayList<PerfilFuncionCanal>(
									0);
							for (PerfilFuncionCanal pfc : listaPerfilFuncionCanals) {
								// log.debug("pfc.getCanalUsuarioCargo().getIdCanalUsuarioCargo() :"+
								// pfc.getCanalUsuarioCargo().getIdCanalUsuarioCargo()
								// +"cuc.getIdCanalUsuarioCargo() :"+cuc.getIdCanalUsuarioCargo());
								if (pfc.getCanalUsuarioCargo()
										.getIdCanalUsuarioCargo().longValue() == cuc
										.getIdCanalUsuarioCargo().longValue()) {
									log.debug("perfil funcion Canales : #0",
											pfc.getFuncion());
									perfilFuncionCanales.add(pfc);
								}
							}

							if (perfilFuncionCanales != null
									&& perfilFuncionCanales.size() > 0) {
								/* monto de kx */
								PerfilFuncionCanal montos = perfilFuncionCanales
										.get(0);
								if (montos != null) {
									this.setMontoDesdeKX(montos
											.getMontoMinimo());
									this.setMontoHastaKX(montos
											.getMontoMaximo());
									this.setMontoDesdeCompKX(montos
											.getMontoMinimoCompartido());
									this.setMontoHastaCompKX(montos
											.getMontoMaximoCompartido());
								}

								this.perfiles = new String[3];
								this.perfiles2 = new String[3];
								for (PerfilFuncionCanal pfc : perfilFuncionCanales) {
									if (pfc.getFuncion()
											.name()
											.equals(FuncionesType.GENERARSOLICITUD
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);

										perfiles[0] = "0";
										this.generarSolicitudes = true;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.GENERARSOLICITUD);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.GENERARSOLICITUD);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.GENERARSOLICITUD);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.GENERARSOLICITUD);
											}
										}

									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.APRUEBARECHAZO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfiles[1] = "1";
										this.aqpruebaRechaza = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.APRUEBARECHAZO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.APRUEBARECHAZO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.APRUEBARECHAZO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.APRUEBARECHAZO);
											}
										}

									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.AGREGARCOMENTARIO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfiles[2] = "2";
										this.agregarComentarios = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.ANALISTANEGOCIO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfiles2[0] = "3";
										this.ejecutivoNegocio = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.ANALISTANEGOCIO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.ANALISTANEGOCIO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.ANALISTANEGOCIO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.ANALISTANEGOCIO);
											}
										}

									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.ANALISTAFINANCIERO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfiles2[1] = "4";
										this.analistaFinanciero = false;

										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}

									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.SOLOVISUALIZAR
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfiles2[2] = "5";
										this.soloVisualizar = false;

										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.SOLOVISUALIZAR);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.SOLOVISUALIZAR);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.SOLOVISUALIZAR);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.KX,
														FuncionesType.SOLOVISUALIZAR);
											}
										}

									}
								}
							}
						} else if (cuc.getTipoCuenta().name()
								.equals(TipoCuentasKupferType.GC.name())) {
							List<PerfilFuncionCanal> perfilFuncionCanales = new ArrayList<PerfilFuncionCanal>(
									0);
							for (PerfilFuncionCanal pfc : listaPerfilFuncionCanals) {
								if (pfc.getCanalUsuarioCargo()
										.getIdCanalUsuarioCargo().longValue() == cuc
										.getIdCanalUsuarioCargo().longValue()) {
									log.debug("perfil funcion Canales : #0",
											pfc.getFuncion());
									perfilFuncionCanales.add(pfc);
								}
							}

							if (perfilFuncionCanales != null
									&& perfilFuncionCanales.size() > 0) {
								/* monto de kx */
								PerfilFuncionCanal montos = perfilFuncionCanales
										.get(0);
								if (montos != null) {
									this.setMontoDesdeGC(montos
											.getMontoMinimo());
									this.setMontoHastaGC(montos
											.getMontoMaximo());
									this.setMontoDesdeCompGC(montos
											.getMontoMinimoCompartido());
									this.setMontoHastaCompGC(montos
											.getMontoMaximoCompartido());
								}

								this.perfilesGC = new String[3];
								this.perfiles2GC = new String[3];
								for (PerfilFuncionCanal pfc : perfilFuncionCanales) {
									if (pfc.getFuncion()
											.name()
											.equals(FuncionesType.GENERARSOLICITUD
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfilesGC[0] = "0";
										this.generarSolicitudesGC = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.GENERARSOLICITUD);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.GENERARSOLICITUD);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.GENERARSOLICITUD);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.GENERARSOLICITUD);
											}
										}

									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.APRUEBARECHAZO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfilesGC[1] = "1";
										this.aqpruebaRechazaGC = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.APRUEBARECHAZO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.APRUEBARECHAZO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.APRUEBARECHAZO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.APRUEBARECHAZO);
											}
										}
									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.AGREGARCOMENTARIO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfilesGC[2] = "2";
										this.agregarComentariosGC = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.ANALISTANEGOCIO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);

										perfiles2GC[0] = "3";
										this.ejecutivoNegocioGC = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.ANALISTANEGOCIO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.ANALISTANEGOCIO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.ANALISTANEGOCIO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.ANALISTANEGOCIO);
											}
										}

									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.ANALISTAFINANCIERO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfiles2GC[1] = "4";
										this.analistaFinancieroGC = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}

									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.SOLOVISUALIZAR
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfiles2GC[2] = "5";
										this.soloVisualizarGC = false;

										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.SOLOVISUALIZAR);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.SOLOVISUALIZAR);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.SOLOVISUALIZAR);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.GC,
														FuncionesType.SOLOVISUALIZAR);
											}
										}

									}
								}
							}

						} else if (cuc.getTipoCuenta().name()
								.equals(TipoCuentasKupferType.MX.name())) {

							List<PerfilFuncionCanal> perfilFuncionCanales = new ArrayList<PerfilFuncionCanal>(
									0);
							for (PerfilFuncionCanal pfc : listaPerfilFuncionCanals) {
								if (pfc.getCanalUsuarioCargo()
										.getIdCanalUsuarioCargo().longValue() == cuc
										.getIdCanalUsuarioCargo().longValue()) {
									log.debug("perfil funcion Canales : #0",
											pfc.getFuncion());
									perfilFuncionCanales.add(pfc);
								}
							}

							if (perfilFuncionCanales != null
									&& perfilFuncionCanales.size() > 0) {
								/* monto de kx */
								PerfilFuncionCanal montos = perfilFuncionCanales
										.get(0);
								if (montos != null) {
									this.setMontoDesdeMX(montos
											.getMontoMinimo());
									this.setMontoHastaMX(montos
											.getMontoMaximo());
									this.setMontoDesdeCompMX(montos
											.getMontoMinimoCompartido());
									this.setMontoHastaCompMX(montos
											.getMontoMaximoCompartido());
								}

								this.perfilesMX = new String[3];
								this.perfiles2MX = new String[3];
								for (PerfilFuncionCanal pfc : perfilFuncionCanales) {
									if (pfc.getFuncion()
											.name()
											.equals(FuncionesType.GENERARSOLICITUD
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfilesMX[0] = "0";
										this.generarSolicitudesMX = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.GENERARSOLICITUD);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.GENERARSOLICITUD);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.GENERARSOLICITUD);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.GENERARSOLICITUD);
											}
										}

									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.APRUEBARECHAZO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfilesMX[1] = "1";
										this.aqpruebaRechazaMX = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.APRUEBARECHAZO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.APRUEBARECHAZO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.APRUEBARECHAZO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.APRUEBARECHAZO);
											}
										}
									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.AGREGARCOMENTARIO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfilesMX[2] = "2";
										this.agregarComentariosMX = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.AGREGARCOMENTARIO);
											}
										}
									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.ANALISTANEGOCIO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfiles2MX[0] = "3";
										this.ejecutivoNegocioMX = false;
										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.ANALISTANEGOCIO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.ANALISTANEGOCIO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.ANALISTANEGOCIO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.ANALISTANEGOCIO);
											}
										}

									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.ANALISTAFINANCIERO
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfiles2MX[1] = "4";
										this.analistaFinancieroMX = false;

										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.ANALISTAFINANCIERO);
											}
										}

									} else if (pfc
											.getFuncion()
											.name()
											.equals(FuncionesType.SOLOVISUALIZAR
													.name())) {
										List<ZonaSucursalNegocioConcepto> zonasSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> sucursalesSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> negociosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										List<ZonaSucursalNegocioConcepto> conceptosSelec = new ArrayList<ZonaSucursalNegocioConcepto>(
												0);
										perfiles2MX[2] = "5";
										this.soloVisualizarMX = false;

										for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
											if (zsnc.getPerfilFuncionCanal()
													.getIdPerfilFuncionCanal()
													.longValue() == pfc
													.getIdPerfilFuncionCanal()
													.longValue()) {
												if (zsnc.getTipoConcepto()
														.name()
														.equals(ConceptosType.ZONA
																.name())) {
													zonasSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.SUCURSAL
																.name())) {
													sucursalesSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.NEGOCIO
																.name())) {
													negociosSelec.add(zsnc);
												} else if (zsnc
														.getTipoConcepto()
														.name()
														.equals(ConceptosType.CONCEPTO
																.name())) {
													conceptosSelec.add(zsnc);
												}
											}
										}

										if (zonasSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : zonasSelec) {
												evaluarZonasTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.SOLOVISUALIZAR);
											}
										}
										if (sucursalesSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : sucursalesSelec) {
												evaluarSucursalesTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.SOLOVISUALIZAR);
											}
										}
										if (negociosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : negociosSelec) {
												evaluarNegociosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.SOLOVISUALIZAR);
											}
										}
										if (conceptosSelec != null) {
											for (ZonaSucursalNegocioConcepto zsnc : conceptosSelec) {
												evaluarConceptosTickeadas(
														zsnc,
														TipoCuentasKupferType.MX,
														FuncionesType.SOLOVISUALIZAR);
											}
										}
									}
								}
							}
						}
					}
				}
				this.setAccion("MODIFICAR");
			} else {
				this.setCargo(null);
				conceptos = new String[4];
				this.setAccion("GUARDAR");
			}
		}
	}

	public void evaluarZonasTickeadas(ZonaSucursalNegocioConcepto zona,
			TipoCuentasKupferType tipo, FuncionesType funcion) {
		List<ZonaUsuarioDTO> listaFinal = new ArrayList<ZonaUsuarioDTO>(0);
		if (TipoCuentasKupferType.KX.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaKxGs) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaKxGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaKxAr) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaKxAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaKxAc) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaKxAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaKxEn) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaKxEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaKxAf) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaKxAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaKxSv) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaKxSv(listaFinal);

			}

		} else if (TipoCuentasKupferType.GC.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaGcGs) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaGcGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaGcAr) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaGcAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaGcAc) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaGcAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaGcEn) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaGcEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaGcAf) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaGcAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaGcSv) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaGcSv(listaFinal);

			}

		} else if (TipoCuentasKupferType.MX.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaMxGs) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaMxGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaMxAr) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaMxAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaMxAc) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaMxAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaMxEn) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaMxEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaMxAf) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaMxAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (ZonaUsuarioDTO z : listaZonaMxSv) {
					if (z.getCodigo().equals(zona.getCodigo())) {
						z.setStatus(true);
						listaFinal.add(z);
					} else {
						listaFinal.add(z);
					}
				}
				this.setListaZonaMxSv(listaFinal);

			}
		}
		return;
	}

	public void evaluarSucursalesTickeadas(
			ZonaSucursalNegocioConcepto sucursal, TipoCuentasKupferType tipo,
			FuncionesType funcion) {
		List<SucursalesUsuarioDTO> listaFinal = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (TipoCuentasKupferType.KX.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalKxGs) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalKxGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalKxAr) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalKxAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalKxAc) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalKxAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalKxEn) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalKxEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalKxAf) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalKxAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalKxSv) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalKxSv(listaFinal);

			}

		} else if (TipoCuentasKupferType.GC.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalGcGs) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalGcGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalGcAr) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalGcAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalGcAc) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalGcAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalGcEn) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalGcEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalGcAf) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalGcAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalGcSv) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalGcSv(listaFinal);

			}

		} else if (TipoCuentasKupferType.MX.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalMxGs) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalMxGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalMxAr) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalMxAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalMxAc) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalMxAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalMxEn) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalMxEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalMxAf) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalMxAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (SucursalesUsuarioDTO s : listaSucursalMxSv) {
					if (s.getCodigo().equals(sucursal.getCodigo())) {
						s.setStatus(true);
						listaFinal.add(s);
					} else {
						listaFinal.add(s);
					}
				}
				this.setListaSucursalMxSv(listaFinal);

			}
		}
		return;
	}

	public void evaluarNegociosTickeadas(ZonaSucursalNegocioConcepto negocio,
			TipoCuentasKupferType tipo, FuncionesType funcion) {
		List<NegocioUsuarioDTO> listaFinal = new ArrayList<NegocioUsuarioDTO>(0);

		if (TipoCuentasKupferType.KX.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioKxGs) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioKxGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioKxAr) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioKxAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioKxAc) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioKxAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioKxEn) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioKxEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioKxAf) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioKxAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioKxSv) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioKxSv(listaFinal);

			}

		} else if (TipoCuentasKupferType.GC.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioGcGs) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioGcGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioGcAr) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioGcAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioGcAc) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioGcAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioGcEn) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioGcEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioGcAf) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioGcAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioGcSv) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioGcSv(listaFinal);

			}

		} else if (TipoCuentasKupferType.MX.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioMxGs) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioMxGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioMxAr) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioMxAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioMxAc) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioMxAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioMxEn) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioMxEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioMxAf) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioMxAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (NegocioUsuarioDTO n : listaNegocioMxSv) {
					if (n.getCodigo().equals(negocio.getCodigo())) {
						n.setStatus(true);
						listaFinal.add(n);
					} else {
						listaFinal.add(n);
					}
				}
				this.setListaNegocioMxSv(listaFinal);

			}
		}
		return;
	}

	public void evaluarConceptosTickeadas(ZonaSucursalNegocioConcepto concepto,
			TipoCuentasKupferType tipo, FuncionesType funcion) {
		List<ConceptoUsuarioDTO> listaFinal = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (TipoCuentasKupferType.KX.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoKxGs) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoKxGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoKxAr) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoKxAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoKxAc) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoKxAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoKxEn) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoKxEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoKxAf) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoKxAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoKxSv) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoKxSv(listaFinal);

			}

		} else if (TipoCuentasKupferType.GC.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoGcGs) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoGcGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoGcAr) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoGcAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoGcAc) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoGcAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoGcEn) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoGcEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoGcAf) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoGcAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoGcSv) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoGcSv(listaFinal);

			}

		} else if (TipoCuentasKupferType.MX.name().equals(tipo.name())) {
			if (FuncionesType.GENERARSOLICITUD.name().equals(funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoMxGs) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoMxGs(listaFinal);
			} else if (FuncionesType.APRUEBARECHAZO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoMxAr) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoMxAr(listaFinal);
			} else if (FuncionesType.AGREGARCOMENTARIO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoMxAc) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoMxAc(listaFinal);
			} else if (FuncionesType.ANALISTANEGOCIO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoMxEn) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoMxEn(listaFinal);
			} else if (FuncionesType.ANALISTAFINANCIERO.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoMxAf) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoMxAf(listaFinal);
			} else if (FuncionesType.SOLOVISUALIZAR.name().equals(
					funcion.name())) {
				for (ConceptoUsuarioDTO c : listaConceptoMxSv) {
					if (c.getCodigo().equals(concepto.getCodigo())) {
						c.setStatus(true);
						listaFinal.add(c);
					} else {
						listaFinal.add(c);
					}
				}
				this.setListaConceptoMxSv(listaFinal);

			}
		}
		return;
	}

	public void obtenerCargo() {
		if (cargo != null) {
			log.error("cargo : #0", cargo.getDesCargo());
			conceptos = new String[4];
			if (cargo.getDetalleZona() != null) {
				if (cargo.getDetalleZona().equals("S")) {
					conceptos[0] = "Z";
					this.setZona(true);
				} else {
					conceptos[0] = "";
					this.setZona(false);
				}
			} else {
				conceptos[0] = "";
				this.setZona(false);
			}

			if (cargo.getDetalleSucursal() != null) {
				if (cargo.getDetalleSucursal().equals("S")) {
					conceptos[1] = "S";
					this.setSucursal(true);
				} else {
					conceptos[1] = "";
					this.setSucursal(false);
				}
			} else {
				conceptos[1] = "";
				this.setSucursal(false);
			}

			if (cargo.getDetalleNegocio() != null) {
				if (cargo.getDetalleNegocio().equals("S")) {
					conceptos[2] = "N";
					this.setNegocio(true);
				} else {
					conceptos[2] = "";
					this.setNegocio(false);
				}
			} else {
				conceptos[2] = "";
				this.setNegocio(false);
			}

			if (cargo.getDetalleConcepto() != null) {
				if (cargo.getDetalleConcepto().equals("S")) {
					conceptos[3] = "C";
					this.setConcepto(true);
				} else {
					conceptos[3] = "";
					this.setConcepto(false);
				}
			} else {
				conceptos[3] = "";
				this.setConcepto(false);
			}

		}
	}
	
	public void seleccionSociedad() {
		if (sociedadAux != null) {
			log.info("Sociedad : #0", sociedadAux.getRazonSocial());
		}
	}

	public void habilitarSegunConcepto() {
		String[] conceptoAux = new String[4];
		this.setZona(false);
		this.setSucursal(false);
		this.setNegocio(false);
		this.setConcepto(false);

		if (conceptos != null) {
			for (String c : conceptos) {
				if (c.equals("Z")) {
					conceptoAux[0] = "Z";
					this.setZona(true);
				}
				if (c.equals("S")) {
					conceptoAux[1] = "S";
					this.setSucursal(true);
				}
				if (c.equals("N")) {
					conceptoAux[2] = "N";
					this.setNegocio(true);
				}
				if (c.equals("C")) {
					conceptoAux[3] = "C";
					this.setConcepto(true);
				}
			}
		}

		setConceptos(conceptoAux);
	}

	public void habilitarPertanas() {
		if (this.canales != null) {
			log.debug("seleccionado #0", this.canales.length);

			if (verificarPestanaCanal(TipoCuentasKupferType.KX.name()) == true) {
				this.pestanakupferExpres = false;
			} else {
				this.pestanakupferExpres = true;
			}

			if (verificarPestanaCanal(TipoCuentasKupferType.GC.name()) == true) {
				this.pestanaGrandesCuentas = false;
			} else {
				this.pestanaGrandesCuentas = true;

			}
			if (verificarPestanaCanal(TipoCuentasKupferType.MX.name()) == true) {
				this.pestanaMixto = false;
			} else {
				this.pestanaMixto = true;
			}

		}
	}

	public boolean verificarPestanaCanal(String canal) {
		boolean exito = false;
		for (String codigo : canales) {
			if (codigo.equals(canal)) {
				exito = true;
				break;
			}
		}
		return exito;
	}

	public void habilitarPertanasSecundaria() {
		if (this.perfiles != null) {
			log.debug("perfiles seleccionados #0", this.perfiles.length);
			if (verificarPerfiles("0") == true) {
				this.generarSolicitudes = false;
			} else {
				this.generarSolicitudes = true;
			}

			if (verificarPerfiles("1") == true) {
				this.aqpruebaRechaza = false;
			} else {
				this.aqpruebaRechaza = true;

			}
			if (verificarPerfiles("2") == true) {
				this.agregarComentarios = false;
			} else {
				this.agregarComentarios = true;
			}
		}
	}

	public void habilitarPertanasSecundaria2() {
		if (this.perfiles2 != null) {
			log.debug("perfiles2 seleccionados #0", this.perfiles2.length);
			if (verificarPerfiles2("3") == true) {
				this.ejecutivoNegocio = false;
			} else {
				this.ejecutivoNegocio = true;
			}
			if (verificarPerfiles2("4") == true) {
				this.analistaFinanciero = false;
			} else {
				this.analistaFinanciero = true;
			}
			if (verificarPerfiles2("5") == true) {
				this.soloVisualizar = false;
			} else {
				this.soloVisualizar = true;
			}
		}
	}

	public void habilitarPertanasSecundariaGC() {
		if (this.perfilesGC != null) {
			log.debug("perfiles seleccionados #0", this.perfilesGC.length);
			if (verificarPerfilesGC("0") == true) {
				this.generarSolicitudesGC = false;
			} else {
				this.generarSolicitudesGC = true;
			}

			if (verificarPerfilesGC("1") == true) {
				this.aqpruebaRechazaGC = false;
			} else {
				this.aqpruebaRechazaGC = true;

			}
			if (verificarPerfilesGC("2") == true) {
				this.agregarComentariosGC = false;
			} else {
				this.agregarComentariosGC = true;
			}
		}
	}

	public void habilitarPertanasSecundaria2GC() {
		if (this.perfiles2GC != null) {
			log.debug("perfiles2 seleccionados #0", this.perfiles2GC.length);
			if (verificarPerfiles2GC("3") == true) {
				this.ejecutivoNegocioGC = false;
			} else {
				this.ejecutivoNegocioGC = true;
			}
			if (verificarPerfiles2GC("4") == true) {
				this.analistaFinancieroGC = false;
			} else {
				this.analistaFinancieroGC = true;
			}
			if (verificarPerfiles2GC("5") == true) {
				this.soloVisualizarGC = false;
			} else {
				this.soloVisualizarGC = true;
			}
		}
	}

	public void habilitarPertanasSecundariaMX() {
		if (this.perfilesMX != null) {
			log.debug("perfiles seleccionados #0", this.perfilesMX.length);
			if (verificarPerfilesMX("0") == true) {
				this.generarSolicitudesMX = false;
			} else {
				this.generarSolicitudesMX = true;
			}

			if (verificarPerfilesMX("1") == true) {
				this.aqpruebaRechazaMX = false;
			} else {
				this.aqpruebaRechazaMX = true;

			}
			if (verificarPerfilesMX("2") == true) {
				this.agregarComentariosMX = false;
			} else {
				this.agregarComentariosMX = true;
			}
		}
	}

	public void habilitarPertanasSecundaria2MX() {
		if (this.perfiles2MX != null) {
			log.debug("perfiles2 seleccionados #0", this.perfiles2MX.length);
			if (verificarPerfiles2MX("3") == true) {
				this.ejecutivoNegocioMX = false;
			} else {
				this.ejecutivoNegocioMX = true;
			}
			if (verificarPerfiles2MX("4") == true) {
				this.analistaFinancieroMX = false;
			} else {
				this.analistaFinancieroMX = true;
			}
			if (verificarPerfiles2MX("5") == true) {
				this.soloVisualizarMX = false;
			} else {
				this.soloVisualizarMX = true;
			}
		}
	}

	public boolean verificarPerfiles(String codigo) {
		boolean exito = false;
		if (this.perfiles != null) {
			for (String nuevo : this.perfiles) {
				if (nuevo.equals(codigo)) {
					exito = true;
					break;
				}
			}
		}
		return exito;

	}

	public boolean verificarPerfiles2(String codigo) {
		boolean exito = false;
		if (this.perfiles2 != null) {
			for (String nuevo : this.perfiles2) {
				if (nuevo.equals(codigo)) {
					exito = true;
					break;
				}
			}
		}
		return exito;

	}

	public boolean verificarPerfilesGC(String codigo) {
		boolean exito = false;
		if (this.perfilesGC != null) {
			for (String nuevo : this.perfilesGC) {
				if (nuevo.equals(codigo)) {
					exito = true;
					break;
				}
			}
		}
		return exito;

	}

	public boolean verificarPerfiles2GC(String codigo) {
		boolean exito = false;
		if (this.perfiles2GC != null) {
			for (String nuevo : this.perfiles2GC) {
				if (nuevo.equals(codigo)) {
					exito = true;
					break;
				}
			}
		}
		return exito;

	}

	public boolean verificarPerfilesMX(String codigo) {
		boolean exito = false;
		if (this.perfilesMX != null) {
			for (String nuevo : this.perfilesMX) {
				if (nuevo.equals(codigo)) {
					exito = true;
					break;
				}
			}
		}
		return exito;

	}

	public boolean verificarPerfiles2MX(String codigo) {
		boolean exito = false;
		if (this.perfiles2MX != null) {
			for (String nuevo : this.perfiles2MX) {
				if (nuevo.equals(codigo)) {
					exito = true;
					break;
				}
			}
		}
		return exito;

	}

	@SuppressWarnings("unchecked")
	public void sacarListaUsuarioPerfilesAsignados() {
		String query = null;
		try {
			if (this.usuario != null) {

				query = " select ut from UsuarioPerfil ut "
						+ "  where ut.usuario.alias=:usu ";
				log.debug("usuario #0", this.usuario.getAlias());
				listaUsuarioPerfiles = (List<UsuarioPerfil>) entityManager
						.createQuery(query)
						.setParameter("usu", this.usuario.getAlias())
						.getResultList();
			}
		} catch (Exception e) {
			log.error("Error, al sacar la lista de usuarios asigandos.",
					e.getMessage());
		}
	}

	public void limpiar() {
		if (listaPerfiles != null) {
			listaPerfiles.clear();
		}
		if (listaUsuariosPerfilesSeleccionado != null) {
			listaUsuariosPerfilesSeleccionado.clear();
		}
		if (listaUsuarioPerfiles != null) {
			listaUsuarioPerfiles.clear();
		}
		if (usuario != null) {
			setUsuario(null);
		}
		setMensaje(null);
	}

	public void obtenerMontoKX() {
		if (this.montoDesdeKX != null) {
			log.debug("monto desde kx #0", this.montoDesdeKX);
		}

		if (this.montoHastaKX != null) {
			log.debug("monto hasta kx #0", this.montoHastaKX);

		}

		if (this.montoDesdeKX != null && this.montoHastaKX != null) {
			if (this.montoHastaKX.longValue() < this.montoDesdeKX.longValue()) {
				this.setMontoHastaKX((long) 0);
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"El monto de maximo del canal kupfer express debe ser mayor al monto minimo.");
			}

		}
	}

	public void obtenerMontoCompKX() {
		if (this.montoDesdeCompKX != null) {
			log.debug("monto desde compartida kx #0", this.montoDesdeCompKX);
		}

		if (this.montoHastaCompKX != null) {
			log.debug("monto hasta compartida kx #0", this.montoHastaCompKX);

		}
		if (this.montoDesdeCompKX != null && this.montoHastaCompKX != null) {
			if (this.montoHastaCompKX.longValue() < this.montoDesdeCompKX
					.longValue()) {
				this.setMontoHastaCompKX((long) 0);
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"El monto de maximo compartido del canal kupfer express debe ser mayor al monto minimo compartido.");
			}

		}
	}

	public void obtenerMontoGC() {
		if (this.montoDesdeGC != null) {
			log.debug("monto desde GC #0", this.montoDesdeGC);
		}

		if (this.montoHastaGC != null) {
			log.debug("monto hasta GC #0", this.montoHastaGC);

		}

		if (this.montoDesdeGC != null && this.montoHastaGC != null) {
			if (this.montoHastaGC.longValue() < this.montoDesdeGC.longValue()) {
				this.setMontoHastaGC((long) 0);
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"El monto de maximo del canal Grandes Cuentas debe ser mayor al monto minimo.");
			}

		}
	}

	public void obtenerMontoCompGC() {
		if (this.montoDesdeCompGC != null) {
			log.debug("monto desde compartida GC #0", this.montoDesdeCompGC);
		}

		if (this.montoHastaCompGC != null) {
			log.debug("monto hasta compartida GC #0", this.montoHastaCompGC);

		}
		if (this.montoDesdeCompGC != null && this.montoHastaCompGC != null) {
			if (this.montoDesdeCompGC.longValue() < this.montoDesdeCompGC
					.longValue()) {
				this.setMontoHastaCompGC((long) 0);
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"El monto de maximo compartido del canal Grandes Cuentas debe ser mayor al monto minimo compartido.");
			}

		}

	}

	public void obtenerMontoMX() {
		if (this.montoDesdeMX != null) {
			log.debug("monto desde MX #0", this.montoDesdeMX);
		}

		if (this.montoHastaMX != null) {
			log.debug("monto hasta MX #0", this.montoHastaMX);

		}

		if (this.montoDesdeMX != null && this.montoHastaMX != null) {
			if (this.montoHastaMX.longValue() < this.montoDesdeMX.longValue()) {
				this.setMontoHastaMX((long) 0);
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"El monto de maximo del canal Mixto debe ser mayor al monto minimo.");
			}

		}
	}

	public void obtenerMontoCompMX() {
		if (this.montoDesdeCompMX != null) {
			log.debug("monto desde compartida MX #0", this.montoDesdeCompMX);
		}

		if (this.montoHastaCompMX != null) {
			log.debug("monto hasta compartida MX #0", this.montoHastaCompMX);

		}
		if (this.montoDesdeCompMX != null && this.montoHastaCompMX != null) {
			if (this.montoHastaCompMX.longValue() < this.montoDesdeCompMX
					.longValue()) {
				this.setMontoHastaCompMX((long) 0);
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"El monto de maximo compartido del canal Mixto debe ser mayor al monto minimo compartido.");
			}

		}

	}

	public void verificarTicket() {
		log.debug("administrador #0", this.administrador);
		log.debug("otras solicitudes #0", this.otrasSolictudes);
		log.debug("eliminado #0", this.eliminado);
	}

	/* metodos globales */
	public void verificaZona(ZonaUsuarioDTO objeto) {
		try {
			if (objeto != null) {
				log.debug("ticket #0", objeto.isStatus());
			}
		} catch (Exception e) {
			log.error("Error,  no poder sacar los los ojebtos de zona");
		}
	}

	public void verificaSucursal(SucursalesUsuarioDTO objeto) {
		try {
			if (objeto != null) {
				log.debug("ticket de sucursal #0", objeto.isStatus());
			}
		} catch (Exception e) {
			log.error("Error,  no poder sacar los los ojebtos");
		}
	}

	public void verificaNegocio(NegocioUsuarioDTO objeto) {
		try {
			if (objeto != null) {
				log.debug("ticket de negocio  #0", objeto.isStatus());
			}
		} catch (Exception e) {
			log.error("Error,  no poder sacar los los ojebtos");
		}
	}

	public void verificaConcepto(ConceptoUsuarioDTO objeto) {
		try {
			if (objeto != null) {
				log.debug("ticket de concepto  #0", objeto.isStatus());
			}
		} catch (Exception e) {
			log.error("Error,  no poder sacar los los ojebtos");
		}
	}

	/* kupfer express */
	public void verificarTodosZonaKxGs(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaKxGs("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaKxGs) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaKxGs("Marcar");
			for (ZonaUsuarioDTO z : listaZonaKxGs) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaKxGs(zonaNueva);
	}

	public void verificarTodosZonaKxEn(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaKxEn("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaKxEn) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaKxEn("Marcar");
			for (ZonaUsuarioDTO z : listaZonaKxEn) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaKxEn(zonaNueva);
	}

	public void verificarTodosZonaKxAr(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaKxAr("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaKxAr) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaKxAr("Marcar");
			for (ZonaUsuarioDTO z : listaZonaKxAr) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaKxAr(zonaNueva);
	}

	public void verificarTodosZonaKxAf(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaKxAf("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaKxAf) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaKxAf("Marcar");
			for (ZonaUsuarioDTO z : listaZonaKxAf) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaKxAf(zonaNueva);
	}

	public void verificarTodosZonaKxAc(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaKxAc("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaKxAc) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaKxAc("Marcar");
			for (ZonaUsuarioDTO z : listaZonaKxAc) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaKxAc(zonaNueva);
	}

	public void verificarTodosZonaKxSv(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaKxSv("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaKxSv) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaKxSv("Marcar");
			for (ZonaUsuarioDTO z : listaZonaKxSv) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaKxSv(zonaNueva);
	}

	public void verificarTodosSucursalKxGs(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalKxGs("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxGs) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalKxGs("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxGs) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalKxGs(sucursalNueva);
	}

	public void verificarTodosSucursalKxEn(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalKxEn("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxEn) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalKxEn("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxEn) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalKxEn(sucursalNueva);
	}

	public void verificarTodosSucursalKxAr(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalKxAr("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxAr) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalKxAr("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxAr) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalKxAr(sucursalNueva);
	}

	public void verificarTodosSucursalKxAf(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalKxAf("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxAf) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalKxAf("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxAf) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalKxAf(sucursalNueva);
	}

	public void verificarTodosSucursalKxAc(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalKxAc("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxAc) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalKxAc("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxAc) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalKxAc(sucursalNueva);
	}

	public void verificarTodosSucursalKxSv(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalKxSv("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxSv) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalKxSv("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalKxSv) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalKxSv(sucursalNueva);
	}

	public void verificarTodosNegocioKxGs(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioKxGs("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioKxGs) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioKxGs("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioKxGs) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioKxGs(negocioNueva);
	}

	public void verificarTodosNegocioKxEn(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioKxEn("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioKxEn) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioKxEn("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioKxEn) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioKxEn(negocioNueva);
	}

	public void verificarTodosNegocioKxAr(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioKxAr("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioKxAr) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioKxAr("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioKxAr) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioKxAr(negocioNueva);
	}

	public void verificarTodosNegocioKxAf(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioKxAf("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioKxAf) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioKxAf("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioKxAf) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioKxAf(negocioNueva);
	}

	public void verificarTodosNegocioKxAc(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioKxAc("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioKxAc) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioKxAc("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioKxAc) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioKxAc(negocioNueva);
	}

	public void verificarTodosNegocioKxSv(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioKxSv("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioKxSv) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioKxSv("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioKxSv) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioKxSv(negocioNueva);
	}

	public void verificarTodosConceptoKxGs(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoKxGs("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxGs) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoKxGs("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxGs) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoKxGs(conceptoNueva);
	}

	public void verificarTodosConceptoKxEn(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoKxEn("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxEn) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoKxEn("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxEn) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoKxEn(conceptoNueva);
	}

	public void verificarTodosConceptoKxAr(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoKxAr("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxAr) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoKxAr("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxAr) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoKxAr(conceptoNueva);
	}

	public void verificarTodosConceptoKxAf(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoKxAf("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxAf) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoKxAf("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxAf) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoKxAf(conceptoNueva);
	}

	public void verificarTodosConceptoKxAc(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoKxAc("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxAc) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoKxAc("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxAc) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoKxAc(conceptoNueva);
	}

	public void verificarTodosConceptoKxSv(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoKxSv("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxSv) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoKxSv("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoKxSv) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoKxSv(conceptoNueva);
	}

	/* Grande Cuentas */

	public void verificarTodosZonaGcGs(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaGcGs("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaGcGs) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaGcGs("Marcar");
			for (ZonaUsuarioDTO z : listaZonaGcGs) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaGcGs(zonaNueva);
	}

	public void verificarTodosZonaGcEn(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaGcEn("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaGcEn) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaGcEn("Marcar");
			for (ZonaUsuarioDTO z : listaZonaGcEn) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaGcEn(zonaNueva);
	}

	public void verificarTodosZonaGcAr(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaGcAr("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaGcAr) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaGcAr("Marcar");
			for (ZonaUsuarioDTO z : listaZonaGcAr) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaGcAr(zonaNueva);
	}

	public void verificarTodosZonaGcAf(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaGcAf("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaGcAf) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaGcAf("Marcar");
			for (ZonaUsuarioDTO z : listaZonaGcAf) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaGcAf(zonaNueva);
	}

	public void verificarTodosZonaGcAc(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaGcAc("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaGcAc) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaGcAc("Marcar");
			for (ZonaUsuarioDTO z : listaZonaGcAc) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaGcAc(zonaNueva);
	}

	public void verificarTodosZonaGcSv(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaGcSv("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaGcSv) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaGcSv("Marcar");
			for (ZonaUsuarioDTO z : listaZonaGcSv) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaGcSv(zonaNueva);
	}

	public void verificarTodosSucursalGcGs(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalGcGs("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcGs) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalGcGs("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcGs) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalGcGs(sucursalNueva);
	}

	public void verificarTodosSucursalGcEn(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalGcEn("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcEn) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalGcEn("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcEn) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalGcEn(sucursalNueva);
	}

	public void verificarTodosSucursalGcAr(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalGcAr("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcAr) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalGcAr("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcAr) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalGcAr(sucursalNueva);
	}

	public void verificarTodosSucursalGcAf(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalGcAf("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcAf) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalGcAf("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcAf) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalGcAf(sucursalNueva);
	}

	public void verificarTodosSucursalGcAc(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalGcAc("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcAc) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalGcAc("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcAc) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalGcAc(sucursalNueva);
	}

	public void verificarTodosSucursalGcSv(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalGcSv("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcSv) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalGcSv("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalGcSv) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalGcSv(sucursalNueva);
	}

	public void verificarTodosNegocioGcGs(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioGcGs("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioGcGs) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioGcGs("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioGcGs) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioGcGs(negocioNueva);
	}

	public void verificarTodosNegocioGcEn(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioGcEn("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioGcEn) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioGcEn("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioGcEn) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioGcEn(negocioNueva);
	}

	public void verificarTodosNegocioGcAr(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioGcAr("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioGcAr) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioGcAr("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioGcAr) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioGcAr(negocioNueva);
	}

	public void verificarTodosNegocioGcAf(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioGcAf("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioGcAf) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioGcAf("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioGcAf) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioGcAf(negocioNueva);
	}

	public void verificarTodosNegocioGcAc(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioGcAc("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioGcAc) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioGcAc("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioGcAc) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioGcAc(negocioNueva);
	}

	public void verificarTodosNegocioGcSv(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioGcSv("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioGcSv) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioGcSv("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioGcSv) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioGcSv(negocioNueva);
	}

	public void verificarTodosConceptoGcGs(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoGcGs("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcGs) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoGcGs("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcGs) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoGcGs(conceptoNueva);
	}

	public void verificarTodosConceptoGcEn(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoGcEn("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcEn) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoGcEn("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcEn) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoGcEn(conceptoNueva);
	}

	public void verificarTodosConceptoGcAr(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoGcAr("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcAr) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoGcAr("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcAr) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoGcAr(conceptoNueva);
	}

	public void verificarTodosConceptoGcAf(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoGcAf("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcAf) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoGcAf("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcAf) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoGcAf(conceptoNueva);
	}

	public void verificarTodosConceptoGcAc(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoGcAc("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcAc) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoGcAc("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcAc) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoGcAc(conceptoNueva);
	}

	public void verificarTodosConceptoGcSv(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoGcSv("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcSv) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoGcSv("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoGcSv) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoGcSv(conceptoNueva);
	}

	/* Mixto */
	public void verificarTodosZonaMxGs(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaMxGs("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaMxGs) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaMxGs("Marcar");
			for (ZonaUsuarioDTO z : listaZonaMxGs) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaMxGs(zonaNueva);
	}

	public void verificarTodosZonaMxEn(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaMxEn("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaMxEn) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaMxEn("Marcar");
			for (ZonaUsuarioDTO z : listaZonaMxEn) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaMxEn(zonaNueva);
	}

	public void verificarTodosZonaMxAr(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaMxAr("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaMxAr) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaMxAr("Marcar");
			for (ZonaUsuarioDTO z : listaZonaMxAr) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaMxAr(zonaNueva);
	}

	public void verificarTodosZonaMxAf(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaMxAf("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaMxAf) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaMxAf("Marcar");
			for (ZonaUsuarioDTO z : listaZonaMxAf) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaMxAf(zonaNueva);
	}

	public void verificarTodosZonaMxAc(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaMxAc("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaMxAc) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaMxAc("Marcar");
			for (ZonaUsuarioDTO z : listaZonaMxAc) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaMxAc(zonaNueva);
	}

	public void verificarTodosZonaMxSv(int opcion) {
		List<ZonaUsuarioDTO> zonaNueva = new ArrayList<ZonaUsuarioDTO>(0);
		if (opcion == 1) {
			this.setMarcarZonaMxSv("Desmarcar");
			for (ZonaUsuarioDTO z : listaZonaMxSv) {
				z.setStatus(true);
				zonaNueva.add(z);
			}
		} else {
			this.setMarcarZonaMxSv("Marcar");
			for (ZonaUsuarioDTO z : listaZonaMxSv) {
				z.setStatus(false);
				zonaNueva.add(z);
			}
		}
		this.setListaZonaMxSv(zonaNueva);
	}

	public void verificarTodosSucursalMxGs(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalMxGs("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxGs) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalMxGs("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxGs) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalMxGs(sucursalNueva);
	}

	public void verificarTodosSucursalMxEn(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalMxEn("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxEn) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalMxEn("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxEn) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalMxEn(sucursalNueva);
	}

	public void verificarTodosSucursalMxAr(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalMxAr("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxAr) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalMxAr("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxAr) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalMxAr(sucursalNueva);
	}

	public void verificarTodosSucursalMxAf(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalMxAf("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxAf) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalMxAf("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxAf) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalMxAf(sucursalNueva);
	}

	public void verificarTodosSucursalMxAc(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalMxAc("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxAc) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalMxAc("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxAc) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalMxAc(sucursalNueva);
	}

	public void verificarTodosSucursalMxSv(int opcion) {
		List<SucursalesUsuarioDTO> sucursalNueva = new ArrayList<SucursalesUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarSucursalMxSv("Desmarcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxSv) {
				s.setStatus(true);
				sucursalNueva.add(s);
			}
		} else {
			this.setMarcarSucursalMxSv("Marcar");
			for (SucursalesUsuarioDTO s : listaSucursalMxSv) {
				s.setStatus(false);
				sucursalNueva.add(s);
			}
		}
		this.setListaSucursalMxSv(sucursalNueva);
	}

	public void verificarTodosNegocioMxGs(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioMxGs("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioMxGs) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioMxGs("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioMxGs) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioMxGs(negocioNueva);
	}

	public void verificarTodosNegocioMxEn(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioMxEn("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioMxEn) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioMxEn("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioMxEn) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioMxEn(negocioNueva);
	}

	public void verificarTodosNegocioMxAr(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioMxAr("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioMxAr) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioMxAr("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioMxAr) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioMxAr(negocioNueva);
	}

	public void verificarTodosNegocioMxAf(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioMxAf("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioMxAf) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioMxAf("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioMxAf) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioMxAf(negocioNueva);
	}

	public void verificarTodosNegocioMxAc(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioMxAc("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioMxAc) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioMxAc("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioMxAc) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioMxAc(negocioNueva);
	}

	public void verificarTodosNegocioMxSv(int opcion) {
		List<NegocioUsuarioDTO> negocioNueva = new ArrayList<NegocioUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarNegocioMxSv("Desmarcar");
			for (NegocioUsuarioDTO n : listaNegocioMxSv) {
				n.setStatus(true);
				negocioNueva.add(n);
			}
		} else {
			this.setMarcarNegocioMxSv("Marcar");
			for (NegocioUsuarioDTO n : listaNegocioMxSv) {
				n.setStatus(false);
				negocioNueva.add(n);
			}
		}
		this.setListaNegocioMxSv(negocioNueva);
	}

	public void verificarTodosConceptoMxGs(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoMxGs("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxGs) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoMxGs("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxGs) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoMxGs(conceptoNueva);
	}

	public void verificarTodosConceptoMxEn(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoMxEn("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxEn) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoMxEn("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxEn) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoMxEn(conceptoNueva);
	}

	public void verificarTodosConceptoMxAr(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoMxAr("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxAr) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoMxAr("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxAr) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoMxAr(conceptoNueva);
	}

	public void verificarTodosConceptoMxAf(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoMxAf("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxAf) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoMxAf("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxAf) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoMxAf(conceptoNueva);
	}

	public void verificarTodosConceptoMxAc(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoMxAc("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxAc) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoMxAc("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxAc) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoMxAc(conceptoNueva);
	}

	public void verificarTodosConceptoMxSv(int opcion) {
		List<ConceptoUsuarioDTO> conceptoNueva = new ArrayList<ConceptoUsuarioDTO>(
				0);
		if (opcion == 1) {
			this.setMarcarConceptoMxSv("Desmarcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxSv) {
				n.setStatus(true);
				conceptoNueva.add(n);
			}
		} else {
			this.setMarcarConceptoMxSv("Marcar");
			for (ConceptoUsuarioDTO n : listaConceptoMxSv) {
				n.setStatus(false);
				conceptoNueva.add(n);
			}
		}
		this.setListaConceptoMxSv(conceptoNueva);
	}

	public void insertarInformacionUsuario() {
		boolean existe = false;
		if (validarInformacion()) {
			try {
				if (this.eliminado == false) {
					this.usuario.setEliminado(false);
				} else {
					this.usuario.setEliminado(true);
				}
				this.usuario.setPassword(this.password);
				entityManager.merge(this.usuario);
				entityManager.flush();
				entityManager.refresh(this.usuario);

				UsuarioCargo usuarioAux = scoringService.getUsuarioCargo(usuario.getIdPersonal());
				if (usuarioAux == null) {
					usuarioAux = new UsuarioCargo();
					usuarioAux.setUsuario(usuario);
					existe = false;
				} else {
					existe = true;
				}

				usuarioAux.setCargo(cargo);
				if (this.zona) {
					usuarioAux.setZona(ConceptosType.ZONA);
				}
				if (this.sucursal) {
					usuarioAux.setSucursal(ConceptosType.SUCURSAL);
				}
				if (this.negocio) {
					usuarioAux.setNegocio(ConceptosType.NEGOCIO);
				}
				if (this.concepto) {
					usuarioAux.setConcepto(ConceptosType.CONCEPTO);
				}

				if (this.administrador) {
					usuarioAux.setAdministrador(this.administrador);
				} else {
					usuarioAux.setAdministrador(false);
				}

				if (this.otrasSolictudes) {
					usuarioAux.setEnvioAutomatico(this.otrasSolictudes);
				} else {
					usuarioAux.setEnvioAutomatico(false);
				}

				if (this.noNotificaInicial) {
					usuarioAux.setnoNotificaInicial(this.noNotificaInicial);
				} else {
					usuarioAux.setnoNotificaInicial(false);
				}

				if (existe) {
					entityManager.merge(usuarioAux);
				} else {
					entityManager.persist(usuarioAux);
				}

				if (usuarioAux != null) {
					for (String canal : this.canales) {
						if (canal.equals(TipoCuentasKupferType.KX.name())) {/*
																			 * seleccion
																			 * del
																			 * perfil
																			 * de
																			 * kupfer
																			 * express
																			 */
							CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
							canalUsuarioCargo.setUsuarioCargo(usuarioAux);
							canalUsuarioCargo
									.setTipoCuenta(TipoCuentasKupferType.KX);
							entityManager.persist(canalUsuarioCargo);
							if (perfiles != null) {
								if (canalUsuarioCargo != null) {
									/* ingreso especial de generar solicitud KX */
									try {
										PerfilFuncionCanal perfilFuncionCanalKX = new PerfilFuncionCanal();
										perfilFuncionCanalKX
												.setCanalUsuarioCargo(canalUsuarioCargo);
										perfilFuncionCanalKX
												.setFuncion(FuncionesType.GENERARSOLICITUD);
										perfilFuncionCanalKX
												.setMontoMinimo(this.montoDesdeKX);
										perfilFuncionCanalKX
												.setMontoMaximo(this.montoHastaKX);
										perfilFuncionCanalKX
												.setMontoMinimoCompartido(this.montoDesdeCompKX);
										perfilFuncionCanalKX
												.setMontoMaximoCompartido(this.montoHastaCompKX);
										entityManager
												.persist(perfilFuncionCanalKX);
										if (perfilFuncionCanalKX != null) {
											for (ZonaUsuarioDTO z : this.listaZonaKxGs) {
												if (z.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
													zsnc.setTipoConcepto(ConceptosType.ZONA);
													zsnc.setCodigo(z
															.getCodigo());
													zsnc.setDescripcion(z
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
											for (SucursalesUsuarioDTO s : this.listaSucursalKxGs) {
												if (s.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
													zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
													zsnc.setCodigo(s
															.getCodigo());
													zsnc.setDescripcion(s
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
											for (NegocioUsuarioDTO n : this.listaNegocioKxGs) {
												if (n.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
													zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
													zsnc.setCodigo(n
															.getCodigo());
													zsnc.setDescripcion(n
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
											for (ConceptoUsuarioDTO c : this.listaConceptoKxGs) {
												if (c.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
													zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
													zsnc.setCodigo(c
															.getCodigo());
													zsnc.setDescripcion(c
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
										}
									} catch (Exception e) {
										log.error(
												"Error, al ingresar de la información generar solicitudes ",
												e.getMessage());
									}
									/* fin del ingreso es */
									for (String perfil : perfiles) {
										if (perfil.equals("1")) {// perfil para
																	// aprobar
																	// y/o
																	// rechazar
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.APRUEBARECHAZO);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeKX);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaKX);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompKX);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompKX);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaKxAr) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalKxAr) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioKxAr) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoKxAr) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}

										} else if (perfil.equals("2")) {// perfil
																		// para
																		// Agregar
																		// Comenatrios
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.AGREGARCOMENTARIO);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeKX);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaKX);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompKX);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompKX);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaKxAc) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalKxAc) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioKxAc) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoKxAc) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}
										}
									}
									// fin de recorrido de perfiles

									if (perfiles2 != null) {
										// comienzo de recorrido de perfiles2
										for (String perfil2 : perfiles2) {
											if (perfil2.equals("3")) {// perfil
																		// de
																		// analista
																		// de
																		// negocio.
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.ANALISTANEGOCIO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeKX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaKX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompKX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompKX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaKxEn) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalKxEn) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioKxEn) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoKxEn) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil2.equals("4")) {// perfil
																				// para
																				// analisis
																				// financiero
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.ANALISTAFINANCIERO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeKX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaKX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompKX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompKX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaKxAf) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalKxAf) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioKxAf) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoKxAf) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil2.equals("5")) {// perfil
																				// para
																				// solo
																				// visualizar
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.SOLOVISUALIZAR);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeKX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaKX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompKX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompKX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaKxSv) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalKxSv) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioKxSv) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoKxSv) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}
											}
										}// fin de recorrido de perfiles2
									}
								}
							}
						} else if (canal
								.equals(TipoCuentasKupferType.GC.name())) {/*
																			 * seleccion
																			 * del
																			 * perfil
																			 * de
																			 * Grandes
																			 * Cuentas
																			 */
							CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
							canalUsuarioCargo.setUsuarioCargo(usuarioAux);
							canalUsuarioCargo
									.setTipoCuenta(TipoCuentasKupferType.GC);
							entityManager.persist(canalUsuarioCargo);
							if (canalUsuarioCargo != null) {

								/* ingreso especial de generar solicitud de GC */
								try {
									PerfilFuncionCanal perfilFuncionCanalGC = new PerfilFuncionCanal();
									perfilFuncionCanalGC
											.setCanalUsuarioCargo(canalUsuarioCargo);
									perfilFuncionCanalGC
											.setFuncion(FuncionesType.GENERARSOLICITUD);
									perfilFuncionCanalGC
											.setMontoMinimo(this.montoDesdeGC);
									perfilFuncionCanalGC
											.setMontoMaximo(this.montoHastaGC);
									perfilFuncionCanalGC
											.setMontoMinimoCompartido(this.montoDesdeCompGC);
									perfilFuncionCanalGC
											.setMontoMaximoCompartido(this.montoHastaCompGC);
									entityManager.persist(perfilFuncionCanalGC);
									if (perfilFuncionCanalGC != null) {

										for (ZonaUsuarioDTO z : this.listaZonaGcGs) {
											if (z.isStatus() == true) {
												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
												zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
												zsnc.setTipoConcepto(ConceptosType.ZONA);
												zsnc.setCodigo(z.getCodigo());
												zsnc.setDescripcion(z
														.getDescripcion());
												entityManager.persist(zsnc);
											}
										}

										for (SucursalesUsuarioDTO s : this.listaSucursalGcGs) {
											if (s.isStatus() == true) {
												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
												zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
												zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
												zsnc.setCodigo(s.getCodigo());
												zsnc.setDescripcion(s
														.getDescripcion());
												entityManager.persist(zsnc);
											}
										}

										for (NegocioUsuarioDTO n : this.listaNegocioGcGs) {
											if (n.isStatus() == true) {
												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
												zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
												zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
												zsnc.setCodigo(n.getCodigo());
												zsnc.setDescripcion(n
														.getDescripcion());
												entityManager.persist(zsnc);
											}
										}

										for (ConceptoUsuarioDTO c : this.listaConceptoGcGs) {
											if (c.isStatus() == true) {
												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
												zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
												zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
												zsnc.setCodigo(c.getCodigo());
												zsnc.setDescripcion(c
														.getDescripcion());
												entityManager.persist(zsnc);
											}
										}
									}
								} catch (Exception e) {
									log.error(
											"Error, al ingresar la información de generar solicitudes de GC",
											e.getMessage());
								}
								/*
								 * fin ingreso especial de generar solicitud de
								 * GC
								 */

								if (perfilesGC != null) {
									for (String perfil : perfilesGC) {
										if (perfil.equals("1")) {// perfil para
																	// aprobar
																	// y/o
																	// rechazar
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.APRUEBARECHAZO);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeGC);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaGC);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompGC);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompGC);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaGcAr) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalGcAr) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioGcAr) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoGcAr) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}

										} else if (perfil.equals("2")) {// perfil
																		// para
																		// Agregar
																		// Comenatrios
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.AGREGARCOMENTARIO);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeGC);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaGC);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompGC);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompGC);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaGcAc) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalGcAc) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioGcAc) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoGcAc) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}
										}
									}
									// fin de recorrido de perfilesGC
								}

								if (perfiles2GC != null) {
									// comienzo de recorrido de perfiles2GC
									for (String perfil2 : perfiles2GC) {
										if (perfil2.equals("3")) {// perfil de
																	// analista
																	// de
																	// negocio.
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.ANALISTANEGOCIO);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeGC);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaGC);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompGC);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompGC);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaGcEn) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalGcEn) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioGcEn) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoGcEn) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}

										} else if (perfil2.equals("4")) {// perfil
																			// para
																			// analisis
																			// financiero
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.ANALISTAFINANCIERO);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeGC);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaGC);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompGC);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompGC);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaGcAf) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalGcAf) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioGcAf) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoGcAf) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}

										} else if (perfil2.equals("5")) {// perfil
																			// para
																			// solo
																			// visualizar
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.SOLOVISUALIZAR);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeGC);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaGC);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompGC);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompGC);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaGcSv) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalGcSv) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioGcSv) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoGcSv) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}
										}
									}// fin de recorrido de perfiles2GC
								}
							}
						} else if (canal
								.equals(TipoCuentasKupferType.MX.name())) {/*
																			 * seleccion
																			 * del
																			 * perfil
																			 * de
																			 * Mixto
																			 */
							CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
							canalUsuarioCargo.setUsuarioCargo(usuarioAux);
							canalUsuarioCargo
									.setTipoCuenta(TipoCuentasKupferType.MX);
							entityManager.persist(canalUsuarioCargo);
							if (canalUsuarioCargo != null) {
								/* ingreso especial de generar solicitud de MX */
								try {

									PerfilFuncionCanal perfilFuncionCanalMX = new PerfilFuncionCanal();
									perfilFuncionCanalMX
											.setCanalUsuarioCargo(canalUsuarioCargo);
									perfilFuncionCanalMX
											.setFuncion(FuncionesType.GENERARSOLICITUD);
									perfilFuncionCanalMX
											.setMontoMinimo(this.montoDesdeMX);
									perfilFuncionCanalMX
											.setMontoMaximo(this.montoHastaMX);
									perfilFuncionCanalMX
											.setMontoMinimoCompartido(this.montoDesdeCompMX);
									perfilFuncionCanalMX
											.setMontoMaximoCompartido(this.montoHastaCompMX);
									entityManager.persist(perfilFuncionCanalMX);
									if (perfilFuncionCanalMX != null) {
										for (ZonaUsuarioDTO z : this.listaZonaMxGs) {
											if (z.isStatus() == true) {
												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
												zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
												zsnc.setTipoConcepto(ConceptosType.ZONA);
												zsnc.setCodigo(z.getCodigo());
												zsnc.setDescripcion(z
														.getDescripcion());
												entityManager.persist(zsnc);
											}
										}
										for (SucursalesUsuarioDTO s : this.listaSucursalMxGs) {
											if (s.isStatus() == true) {
												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
												zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
												zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
												zsnc.setCodigo(s.getCodigo());
												zsnc.setDescripcion(s
														.getDescripcion());
												entityManager.persist(zsnc);
											}
										}
										for (NegocioUsuarioDTO n : this.listaNegocioMxGs) {
											if (n.isStatus() == true) {
												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
												zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
												zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
												zsnc.setCodigo(n.getCodigo());
												zsnc.setDescripcion(n
														.getDescripcion());
												entityManager.persist(zsnc);
											}
										}
										for (ConceptoUsuarioDTO c : this.listaConceptoMxGs) {
											if (c.isStatus() == true) {
												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
												zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
												zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
												zsnc.setCodigo(c.getCodigo());
												zsnc.setDescripcion(c
														.getDescripcion());
												entityManager.persist(zsnc);
											}
										}
									}
								} catch (Exception e) {
									log.error(
											"Error, al ingresar la información de generar solicitudes de MX",
											e.getMessage());
								}
								/*
								 * fin ingreso especial de generar solicitud de
								 * MX
								 */

								if (perfilesMX != null) {
									for (String perfil : perfilesMX) {
										if (perfil.equals("1")) {// perfil para
																	// aprobar
																	// y/o
																	// rechazar
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.APRUEBARECHAZO);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeMX);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaMX);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompMX);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompMX);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaMxAr) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalMxAr) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioMxAr) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoMxAr) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}

										} else if (perfil.equals("2")) {// perfil
																		// para
																		// Agregar
																		// Comenatrios
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.AGREGARCOMENTARIO);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeMX);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaMX);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompMX);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompMX);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaMxAc) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalMxAc) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioMxAc) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoMxAc) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}
										}
									}
									// fin de recorrido de perfilesMX
								}

								if (perfiles2MX != null) {
									// comienzo de recorrido de perfiles2MX
									for (String perfil2 : perfiles2MX) {
										if (perfil2.equals("3")) {// perfil de
																	// analista
																	// de
																	// negocio.
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.ANALISTANEGOCIO);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeMX);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaMX);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompMX);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompMX);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaMxEn) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalMxEn) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioMxEn) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoMxEn) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}

										} else if (perfil2.equals("4")) {// perfil
																			// para
																			// analisis
																			// financiero
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.ANALISTAFINANCIERO);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeMX);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaMX);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompMX);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompMX);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaMxAf) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalMxAf) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioMxAf) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoMxAf) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}

										} else if (perfil2.equals("5")) {// perfil
																			// para
																			// solo
																			// visualizar
											PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
											perfilFuncionCanal
													.setCanalUsuarioCargo(canalUsuarioCargo);
											perfilFuncionCanal
													.setFuncion(FuncionesType.SOLOVISUALIZAR);
											perfilFuncionCanal
													.setMontoMinimo(this.montoDesdeMX);
											perfilFuncionCanal
													.setMontoMaximo(this.montoHastaMX);
											perfilFuncionCanal
													.setMontoMinimoCompartido(this.montoDesdeCompMX);
											perfilFuncionCanal
													.setMontoMaximoCompartido(this.montoHastaCompMX);
											entityManager
													.persist(perfilFuncionCanal);
											if (perfilFuncionCanal != null) {
												if (this.zona) {
													for (ZonaUsuarioDTO z : this.listaZonaMxSv) {
														if (z.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.ZONA);
															zsnc.setCodigo(z
																	.getCodigo());
															zsnc.setDescripcion(z
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.sucursal) {
													for (SucursalesUsuarioDTO s : this.listaSucursalMxSv) {
														if (s.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
															zsnc.setCodigo(s
																	.getCodigo());
															zsnc.setDescripcion(s
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.negocio) {
													for (NegocioUsuarioDTO n : this.listaNegocioMxSv) {
														if (n.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
															zsnc.setCodigo(n
																	.getCodigo());
															zsnc.setDescripcion(n
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
												if (this.concepto) {
													for (ConceptoUsuarioDTO c : this.listaConceptoMxSv) {
														if (c.isStatus() == true) {
															ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
															zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
															zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
															zsnc.setCodigo(c
																	.getCodigo());
															zsnc.setDescripcion(c
																	.getDescripcion());
															entityManager
																	.persist(zsnc);
														}
													}
												}
											}
										}
									}// fin de recorrido de perfiles2MX
								}
							}
						}
					}
				}
				entityManager.flush();
				limpiarDatos();
				FacesMessages
						.instance()
						.add(Severity.INFO,
								"Los datos fueron asociado al usuario exitosamente...!!");
			} catch (Exception e) {
				log.error("Error, al insertar los datos ", e.getMessage());
			}

		}
	}

	public boolean validarInformacion() {

		if (this.usuario == null) {
			FacesMessages.instance().add(Severity.ERROR,
					"Debe seleccionar el usuario .");
			return false;
		}

		if (this.cargo == null) {
			FacesMessages.instance().add(Severity.ERROR,
					"Debe seleccionar el cargo del usuario .");
			return false;

		}
		// if(this.password == null ){
		// FacesMessages.instance().add(Severity.INFO,"Debe ingresar una password...!!!!");
		// return false;
		// }else{
		// if("".equals(this.password)){
		// FacesMessages.instance().add(Severity.INFO,"La password no debe ser en blanco...!!!!");
		// return false;
		// }
		// }

		if (this.conceptos == null) {
			FacesMessages.instance().add(Severity.ERROR,
					"Seleccione un cargo que tenga algun concepto asociado.");
			return false;
		} else {
			boolean existen = false;
			if (this.conceptos.length == 0) {
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"Seleccione un cargo que tenga algun concepto asociado.");
				return false;
			} else {
				for (String dato : this.conceptos) {
					if (dato != null && !"".equals(dato)) {
						existen = true;
						break;
					}
				}
				if (existen == false) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Seleccione un cargo que tenga algun concepto asociado.");
					return false;
				}
			}
		}

		if (this.canales == null) {
			FacesMessages.instance().add(Severity.ERROR,
					"Debe seleccionar algun canal para asociar los datos.");
			return false;
		} else {
			boolean existen = false;
			if (this.canales.length == 0) {
				FacesMessages.instance().add(Severity.ERROR,
						"Debe seleccionar algun canal para asociar los datos.");
				return false;
			} else {
				for (String dato : this.canales) {
					if (dato != null && !"".equals(dato)) {
						existen = validarCanal(dato);
						if (existen == false)
							break;
					}
				}
				if (existen == false) {
					return false;
				}

			}
		}

		return true;
	}

	public boolean validarCanal(String canal) {
		boolean validar = false;
		if (canal.equals(TipoCuentasKupferType.KX.name())) {
			validar = validarKx();
		} else if (canal.equals(TipoCuentasKupferType.GC.name())) {
			validar = validarGc();
		} else if (canal.equals(TipoCuentasKupferType.MX.name())) {
			validar = validarMx();
		}

		return validar;
	}

	public boolean validarKx() {
		if (this.perfiles == null && this.perfiles2 == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe seleccionar alguna función para el usuario en kupfer express.");
			return false;
		}

		if (this.perfiles != null) {
			boolean existen = false;
			int cant = this.perfiles.length;
			int contNulo = 0;
			for (String dato : this.perfiles) {
				if (dato != null && !"".equals(dato)) {
					existen = validarListasKx(dato);
					if (existen == false)
						break;
				} else {
					contNulo++;
				}
			}
			if (cant == contNulo) {
				existen = true;
			}
			if (existen == false) {
				return false;
			}
		}
		// else{
		// FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario en kupfer express.");
		// return false;
		// }

		if (this.perfiles2 != null) {
			boolean existen = false;
			if (this.perfiles2.length == 0) {
				FacesMessages.instance().add(Severity.ERROR,
						"Debe seleccionar alguna función para el usuario.");
				return false;
			} else {
				int cant = this.perfiles2.length;
				int contNulo = 0;
				for (String dato : this.perfiles2) {
					if (dato != null && !"".equals(dato)) {
						existen = validarListasKx(dato);
						if (existen == false)
							break;
					} else {
						contNulo++;
					}
				}
				if (cant == contNulo) {
					existen = true;
				}
				if (existen == false) {
					return false;
				}

			}
		}

		if (this.montoDesdeKX == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto minimo de aprobación del canal kupfer express.");
			return false;
		}
		if (this.montoHastaKX == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto maximo de aprobación del canal kupfer express.");
			return false;
		}

		if (this.montoHastaKX.longValue() < this.montoDesdeKX.longValue()) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"El monto maximo de aprobación del canal kupfer express debe ser mayor.");
			return false;
		}

		if (this.montoDesdeCompKX == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto minimo de aprobación compartido del canal kupfer express.");
			return false;
		}
		if (this.montoHastaCompKX == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto maximo de aprobación compartido del canal kupfer express.");
			return false;
		}

		if (this.montoHastaKX.longValue() < this.montoDesdeKX.longValue()) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"El monto maximo de aprobación compartido del canal kupfer express debe ser mayor.");
			return false;
		}

		return true;
	}

	public boolean validarGc() {
		if (this.perfilesGC == null && this.perfiles2GC == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe seleccionar alguna función para el usuario en Grande Cuentas.");
			return false;
		}

		if (this.perfilesGC != null) {
			boolean existen = false;
			int cant = this.perfilesGC.length;
			int contNulo = 0;
			for (String dato : this.perfilesGC) {
				if (dato != null && !"".equals(dato)) {
					existen = validarListasGc(dato);
					if (existen == false)
						break;
				} else {
					contNulo++;
				}
			}
			if (cant == contNulo) {
				existen = true;
			}
			if (existen == false) {
				return false;
			}
		}
		// else{
		// FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario en Grande Cuentas.");
		// return false;
		// }

		if (this.perfiles2GC != null) {
			boolean existen = false;
			if (this.perfiles2GC.length == 0) {
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"Debe seleccionar alguna función para el usuario en Grande Cuentas.");

			} else {
				int cant = this.perfiles2GC.length;
				int contNulo = 0;
				for (String dato : this.perfiles2GC) {
					if (dato != null && !"".equals(dato)) {
						existen = validarListasGc(dato);
						if (existen == false)
							break;
					} else {
						contNulo++;
					}
				}
				if (cant == contNulo) {
					existen = true;
				}
				if (existen == false) {
					return false;
				}

			}
		}

		if (this.montoDesdeGC == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto minimo de aprobación del canal Grande Cuentas.");
			return false;
		}
		if (this.montoHastaGC == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto maximo de aprobación del canal Grande Cuentas.");
			return false;
		}

		if (this.montoHastaGC.longValue() < this.montoDesdeGC.longValue()) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"El monto maximo de aprobación del canal Grande Cuentas debe ser mayor.");
			return false;
		}

		if (this.montoDesdeCompGC == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto minimo de aprobación compartido del canal Grande Cuentas.");
			return false;
		}
		if (this.montoHastaCompGC == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto maximo de aprobación compartido del canal Grande Cuentas.");
			return false;
		}

		if (this.montoHastaGC.longValue() < this.montoDesdeGC.longValue()) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"El monto maximo de aprobación compartido del canal Grande Cuentas debe ser mayor.");
			return false;
		}

		return true;
	}

	public boolean validarMx() {
		if (this.perfilesMX == null && this.perfiles2MX == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe seleccionar alguna función para el usuario en Grande Cuentas.");
			return false;
		}

		if (this.perfilesMX != null) {
			boolean existen = false;
			if (this.perfilesMX.length == 0) {
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"Debe seleccionar alguna función para el usuario en Grande Cuentas.");
				return false;
			} else {
				int cant = this.perfilesMX.length;
				int contNulo = 0;
				for (String dato : this.perfilesMX) {
					if (dato != null && !"".equals(dato)) {
						existen = validarListasMx(dato);
						if (existen == false)
							break;

					} else {
						contNulo++;
					}
				}
				if (cant == contNulo) {
					existen = true;
				}

				if (existen == false) {
					return false;
				}

			}
		}

		if (this.perfiles2MX != null) {
			boolean existen = false;
			if (this.perfiles2MX.length == 0) {
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"Debe seleccionar alguna función para el usuario en Grande Cuentas.");

			} else {
				int cant = this.perfiles2MX.length;
				int contNulo = 0;
				for (String dato : this.perfiles2MX) {
					if (dato != null && !"".equals(dato)) {
						existen = validarListasMx(dato);
						if (existen == false)
							break;
					} else {
						contNulo++;
					}
				}
				if (cant == contNulo) {
					existen = true;
				}

				if (existen == false) {
					return false;
				}

			}
		}

		if (this.montoDesdeMX == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto minimo de aprobación del canal Mixto.");
			return false;
		}
		if (this.montoHastaMX == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto maximo de aprobación del canal Mixto.");
			return false;
		}

		if (this.montoHastaMX.longValue() < this.montoDesdeMX.longValue()) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"El monto maximo de aprobación del canal Mixto debe ser mayor.");
			return false;
		}

		if (this.montoDesdeCompMX == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto minimo de aprobación compartido del canal Mixto.");
			return false;
		}
		if (this.montoHastaCompMX == null) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"Debe ingresar el monto maximo de aprobación compartido del canal Mixto.");
			return false;
		}

		if (this.montoHastaMX.longValue() < this.montoDesdeMX.longValue()) {
			FacesMessages
					.instance()
					.add(Severity.ERROR,
							"El monto maximo de aprobación compartido del canal Mixto debe ser mayor.");
			return false;
		}

		return true;
	}

	public boolean validarListasKx(String codigo) {
		int zona = 0;
		int sucursal = 0;
		int negocio = 0;
		int concepto = 0;
		if (codigo.equals("1")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaKxAr);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Kupfer Express de función Aprueba/Rechaza.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalKxAr);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Kupfer Express de función Aprueba/Rechaza.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioKxAr);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Kupfer Express de función Aprueba/Rechaza.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoKxAr);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Kupfer Express de función Aprueba/Rechaza.");
					return false;
				}

			}

		} else if (codigo.equals("2")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaKxAc);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Kupfer Express de función Agregar Cometarios.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalKxAc);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Kupfer Express de función Agregar Cometarios.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioKxAc);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Kupfer Express de función Agregar Cometarios.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoKxAc);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Kupfer Express de función Agregar Cometarios.");
					return false;
				}

			}

		} else if (codigo.equals("3")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaKxEn);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Kupfer Express de función Ejecutivo Negocio.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalKxEn);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Kupfer Express de función Ejecutivo Negocio.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioKxEn);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Kupfer Express de función Ejecutivo Negocio.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoKxEn);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Kupfer Express de función Ejecutivo Negocio.");
					return false;
				}

			}
		} else if (codigo.equals("4")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaKxAf);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Kupfer Express de función Analista Financiero.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalKxAf);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Kupfer Express de función Analista Financiero.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioKxAf);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Kupfer Express de función Analista Financiero.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoKxAf);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Kupfer Express de función Analista Financiero.");
					return false;
				}

			}

		} else if (codigo.equals("5")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaKxSv);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Kupfer Express de función Solo Visualizar.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalKxSv);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Kupfer Express de función Solo Visualizar.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioKxSv);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Kupfer Express de función Solo Visualizar.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoKxSv);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Kupfer Express de función Solo Visualizar.");
					return false;
				}

			}
		}
		return true;
	}

	public boolean validarListasGc(String codigo) {
		int zona = 0;
		int sucursal = 0;
		int negocio = 0;
		int concepto = 0;
		if (codigo.equals("1")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaGcAr);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Grande Cuentas de función Aprueba/Rechaza.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalGcAr);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Grande Cuentas de función Aprueba/Rechaza.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioGcAr);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Grande Cuentas de función Aprueba/Rechaza.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoGcAr);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Grande Cuentas de función Aprueba/Rechaza.");
					return false;
				}

			}

		} else if (codigo.equals("2")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaGcAc);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Grande Cuentas de función Agregar Cometarios.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalGcAc);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Grande Cuentas de función Agregar Cometarios.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioGcAc);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Grande Cuentas de función Agregar Cometarios.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoGcAc);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Grande Cuentas de función Agregar Cometarios.");
					return false;
				}

			}

		} else if (codigo.equals("3")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaGcEn);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Grande Cuentas de función Ejecutivo Negocio.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalGcEn);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Grande Cuentas de función Ejecutivo Negocio.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioGcEn);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Grande Cuentas de función Ejecutivo Negocio.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoGcEn);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Grande Cuentas de función Ejecutivo Negocio.");
					return false;
				}

			}
		} else if (codigo.equals("4")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaGcAf);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Grande Cuentas de función Analista Financiero.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalGcAf);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Grande Cuentas de función Analista Financiero.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioGcAf);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Grande Cuentas de función Analista Financiero.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoGcAf);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Grande Cuentas de función Analista Financiero.");
					return false;
				}

			}

		} else if (codigo.equals("5")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaGcSv);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Grande Cuentas de función Solo Visualizar.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalGcSv);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Grande Cuentas de función Solo Visualizar.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioGcSv);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Grande Cuentas de función Solo Visualizar.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoGcSv);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Grande Cuentas de función Solo Visualizar.");
					return false;
				}

			}
		}
		return true;
	}

	public boolean validarListasMx(String codigo) {
		int zona = 0;
		int sucursal = 0;
		int negocio = 0;
		int concepto = 0;
		if (codigo.equals("1")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaMxAr);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Mixto de función Aprueba/Rechaza.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalMxAr);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Mixto de función Aprueba/Rechaza.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioMxAr);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Mixto de función Aprueba/Rechaza.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoMxAr);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Mixto de función Aprueba/Rechaza.");
					return false;
				}

			}

		} else if (codigo.equals("2")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaMxAc);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Mixto de función Agregar Cometarios.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalMxAc);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Mixto de función Agregar Cometarios.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioMxAc);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Mixto de función Agregar Cometarios.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoMxAc);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Mixto de función Agregar Cometarios.");
					return false;
				}

			}

		} else if (codigo.equals("3")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaMxEn);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Mixto de función Ejecutivo Negocio.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalMxEn);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Mixto de función Ejecutivo Negocio.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioMxEn);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Mixto de función Ejecutivo Negocio.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoMxEn);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Mixto de función Ejecutivo Negocio.");
					return false;
				}

			}
		} else if (codigo.equals("4")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaMxAf);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Mixto de función Analista Financiero.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalMxAf);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Mixto de función Analista Financiero.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioMxAf);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Mixto de función Analista Financiero.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoMxAf);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Mixto de función Analista Financiero.");
					return false;
				}

			}

		} else if (codigo.equals("5")) {
			if (this.zona) {
				zona = validarZona(this.listaZonaMxSv);
				if (zona == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna zona en el canal Mixto de función Solo Visualizar.");
					return false;
				}
			}
			if (this.sucursal) {
				sucursal = validarSucursal(this.listaSucursalMxSv);
				if (sucursal == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar alguna sucursal en el canal Mixto de función Solo Visualizar.");
					return false;
				}
			}
			if (this.negocio) {
				negocio = validarNegocio(this.listaNegocioMxSv);
				if (negocio == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun negocio en el canal Mixto de función Solo Visualizar.");
					return false;
				}
			}
			if (this.concepto) {
				concepto = validarConcepto(this.listaConceptoMxSv);
				if (concepto == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Debe seleccionar algun concepto en el canal Mixto de función Solo Visualizar.");
					return false;
				}

			}
		}
		return true;
	}

	public int validarZona(List<ZonaUsuarioDTO> listaZona) {
		int cantidad = 0;
		for (ZonaUsuarioDTO z : listaZona) {
			if (z.isStatus() == true)
				cantidad++;
		}
		return cantidad;
	}

	public int validarSucursal(List<SucursalesUsuarioDTO> listaSucursal) {
		int cantidad = 0;
		for (SucursalesUsuarioDTO s : listaSucursal) {
			if (s.isStatus() == true)
				cantidad++;
		}
		return cantidad;
	}

	public int validarNegocio(List<NegocioUsuarioDTO> listaNegocio) {
		int cantidad = 0;
		for (NegocioUsuarioDTO n : listaNegocio) {
			if (n.isStatus() == true)
				cantidad++;
		}
		return cantidad;
	}

	public int validarConcepto(List<ConceptoUsuarioDTO> listaConcepto) {
		int cantidad = 0;
		for (ConceptoUsuarioDTO c : listaConcepto) {
			if (c.isStatus() == true)
				cantidad++;
		}
		return cantidad;
	}

	public void eliminarRelaciones() {
		try {
			if (this.usuario != null) {
				this.usuario.setEliminado(true);
				entityManager.merge(this.usuario);
				entityManager.flush();
				FacesMessages.instance().add(Severity.INFO,
						"Se eliminado el usuario " + this.usuario.getNombre());
				sacarUsuarios();
				sacarUsuariosNuevosDefinidos();
				limpiarDatos();
			} else {
				FacesMessages.instance().add(Severity.INFO,
						"Debe seleccionar un usuario para eliminar...!!!.");
			}
		} catch (Exception e) {
			log.error("Error, al eliminar los datos de usuario.",
					e.getMessage());
		}

	}

	public void activarRelaciones() {
		try {
			if (this.usuario != null) {
				this.usuario.setEliminado(false);
				entityManager.merge(this.usuario);
				entityManager.flush();
				FacesMessages.instance().add(Severity.INFO,
						"Se activo el usuario " + this.usuario.getNombre());
				sacarUsuarios();
				sacarUsuariosNuevosDefinidos();
				limpiarDatos();
			} else {
				FacesMessages.instance().add(Severity.INFO,
						"Debe seleccionar un usuario para eliminar...!!!.");
			}
		} catch (Exception e) {
			log.error("Error, al eliminar los datos de usuario.",
					e.getMessage());
		}

	}

	public void cambiarPassword() {
		try {
			if (this.password == null) {
				FacesMessages.instance().add(Severity.INFO,
						"Debe ingresar una password...!!!!");
				return;
			} else {
				if ("".equals(this.password)) {
					FacesMessages.instance().add(Severity.INFO,
							"La password no debe ser en blanco...!!!!");
					return;
				}
			}

			if (this.usuario == null) {
				FacesMessages.instance().add(Severity.INFO,
						"Debe seleccionar un usuario para eliminar...!!!.");
				return;
			}

			if (this.usuario != null) {
				this.usuario.setPassword(this.password);
				entityManager.merge(this.usuario);
				entityManager.flush();
				FacesMessages.instance().add(
						Severity.INFO,
						"Se modificado la password del usuario "
								+ this.usuario.getNombre());
				sacarUsuarios();
				sacarUsuariosNuevosDefinidos();
				limpiarDatos();

			}
		} catch (Exception e) {
			log.error("Error, al cambiar las claves los datos de usuario.",
					e.getMessage());
		}

	}

	public void asignarRelacionesMasivo() {
		try {
			// if(listaUsuariosDefinidos != null){
			// for(UsuarioIngresoDTO usuarioIngresoDTO :
			// listaUsuariosDefinidos){
			// UsuarioCargo uc =
			// scoringService.getUsuarioCargoForUsername(usuarioIngresoDTO.getUsuario());
			// if(uc == null){
			// Usuariosegur usuarioAux =
			// scoringService.getUsuarioUsuarioSegurForUsername(usuarioIngresoDTO.getUsuario(),usuarioIngresoDTO.getCorreo());
			// if(usuarioAux != null){
			// UsuarioCargo usuarioCargo = new UsuarioCargo();
			// LcredCargos cargo = entityManager.find(LcredCargos.class,
			// usuarioIngresoDTO.getCargo());
			// usuarioCargo.setCargo(cargo);
			// usuarioCargo.setUsuario(usuarioAux);
			// if(cargo.getDetalleZona() != null &&
			// cargo.getDetalleZona().equals("S")){
			// usuarioCargo.setZona(ConceptosType.ZONA);
			// }else{
			// usuarioCargo.setZona(null);
			// }
			//
			// if(cargo.getDetalleSucursal() != null &&
			// cargo.getDetalleSucursal().equals("S")){
			// usuarioCargo.setSucursal(ConceptosType.SUCURSAL);
			// }else{
			// usuarioCargo.setSucursal(null);
			// }
			//
			// if(cargo.getDetalleNegocio() != null &&
			// cargo.getDetalleNegocio().equals("S")){
			// usuarioCargo.setNegocio(ConceptosType.NEGOCIO);
			// }else{
			// usuarioCargo.setNegocio(null);
			// }
			//
			// if(cargo.getDetalleNegocio() != null &&
			// cargo.getDetalleNegocio().equals("S")){
			// usuarioCargo.setConcepto(ConceptosType.CONCEPTO);
			// }else{
			// usuarioCargo.setConcepto(null);
			// }
			//
			// usuarioCargo.setAdministrador(false);
			// usuarioCargo.setEnvioAutomatico(false);
			//
			// usuarioCargo = scoringService.persistUsuarioCargo(usuarioCargo);
			// }
			// }
			// }
			// }
			//

			// if(listaUsuarios != null){
			// for(Usuariosegur usuario : listaUsuarios){
			// UsuarioCargo uc =
			// scoringService.getUsuarioCargo(usuario.getIdPersonal());
			// if(uc == null){
			// UsuarioIngresoDTO usuarioIngresoDTO =
			// getUsuarioIngresoDTO(usuario.getAlias());
			// if(usuarioIngresoDTO != null){
			// UsuarioCargo usuarioCargo = new UsuarioCargo();
			// LcredCargos cargo = entityManager.find(LcredCargos.class,
			// usuarioIngresoDTO.getCargo());
			// usuarioCargo.setCargo(cargo);
			// usuarioCargo.setUsuario(usuario);
			// if(usuarioIngresoDTO.getZona() != null &&
			// usuarioIngresoDTO.getZona().equals("S")){
			// usuarioCargo.setZona(ConceptosType.ZONA);
			// }else{
			// usuarioCargo.setZona(null);
			// }
			//
			// if(usuarioIngresoDTO.getSucursal() != null &&
			// usuarioIngresoDTO.getSucursal().equals("S")){
			// usuarioCargo.setSucursal(ConceptosType.SUCURSAL);
			// }else{
			// usuarioCargo.setSucursal(null);
			// }
			//
			// if(usuarioIngresoDTO.getNegocio() != null &&
			// usuarioIngresoDTO.getNegocio().equals("S")){
			// usuarioCargo.setNegocio(ConceptosType.NEGOCIO);
			// }else{
			// usuarioCargo.setNegocio(null);
			// }
			//
			// if(usuarioIngresoDTO.getConcepto() != null &&
			// usuarioIngresoDTO.getConcepto().equals("S")){
			// usuarioCargo.setConcepto(ConceptosType.CONCEPTO);
			// }else{
			// usuarioCargo.setConcepto(null);
			// }
			//
			// if(usuarioIngresoDTO.getAdminstrador() != null &&
			// usuarioIngresoDTO.getAdminstrador().equals("S")){
			// usuarioCargo.setAdministrador(true);
			// }else{
			// usuarioCargo.setAdministrador(false);
			// }
			//
			// if(usuarioIngresoDTO.getEnvio() != null &&
			// usuarioIngresoDTO.getEnvio().equals("S")){
			// usuarioCargo.setEnvioAutomatico(true);
			// }else{
			// usuarioCargo.setEnvioAutomatico(false);
			// }
			//
			// usuarioCargo = scoringService.persistUsuarioCargo(usuarioCargo);
			// if(usuarioCargo != null){
			// for(String canal: canalesAux ){
			// if(canal.equals(TipoCuentasKupferType.KX.name())){
			// CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
			// canalUsuarioCargo.setUsuarioCargo(usuarioCargo);
			// canalUsuarioCargo.setTipoCuenta(TipoCuentasKupferType.KX);
			// entityManager.persist(canalUsuarioCargo);
			// /*ingreso especial de generar solicitud KX*/
			// try{
			// PerfilFuncionCanal perfilFuncionCanalKX = new
			// PerfilFuncionCanal();
			// perfilFuncionCanalKX.setCanalUsuarioCargo(canalUsuarioCargo);
			// perfilFuncionCanalKX.setFuncion(FuncionesType.GENERARSOLICITUD);
			// perfilFuncionCanalKX.setMontoMinimo((long)0 );
			// perfilFuncionCanalKX.setMontoMaximo((long)10000000);
			// perfilFuncionCanalKX.setMontoMinimoCompartido((long)10000001);
			// perfilFuncionCanalKX.setMontoMaximoCompartido((long)1000000000);
			// entityManager.persist(perfilFuncionCanalKX);
			// if(perfilFuncionCanalKX != null){
			// if(usuarioCargo.getZona() != null){
			// for(ZonaUsuarioDTO z : this.listaZonaKxGs){
			// if(z.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
			// zsnc.setTipoConcepto(ConceptosType.ZONA);
			// zsnc.setCodigo(z.getCodigo());
			// zsnc.setDescripcion(z.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// if(usuarioCargo.getSucursal() != null){
			// for(SucursalesUsuarioDTO s : this.listaSucursalKxGs){
			// if(s.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
			// zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
			// zsnc.setCodigo(s.getCodigo());
			// zsnc.setDescripcion(s.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// if(usuarioCargo.getNegocio() != null){
			// for(NegocioUsuarioDTO n : this.listaNegocioKxGs){
			// if(n.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
			// zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
			// zsnc.setCodigo(n.getCodigo());
			// zsnc.setDescripcion(n.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// if(usuarioCargo.getConcepto() != null){
			// for(ConceptoUsuarioDTO c : this.listaConceptoKxGs){
			// if(c.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
			// zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
			// zsnc.setCodigo(c.getCodigo());
			// zsnc.setDescripcion(c.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// }
			// }catch (Exception e) {
			// log.error("Error, al ingresar de la información generar solicitudes ",
			// e.getMessage());
			// }
			//
			// }else if(canal.equals(TipoCuentasKupferType.GC.name())){
			// CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
			// canalUsuarioCargo.setUsuarioCargo(usuarioCargo);
			// canalUsuarioCargo.setTipoCuenta(TipoCuentasKupferType.GC);
			// entityManager.persist(canalUsuarioCargo);
			//
			// /*ingreso especial de generar solicitud GC*/
			// try{
			// PerfilFuncionCanal perfilFuncionCanalGC = new
			// PerfilFuncionCanal();
			// perfilFuncionCanalGC.setCanalUsuarioCargo(canalUsuarioCargo);
			// perfilFuncionCanalGC.setFuncion(FuncionesType.GENERARSOLICITUD);
			// perfilFuncionCanalGC.setMontoMinimo((long)0 );
			// perfilFuncionCanalGC.setMontoMaximo((long)10000000);
			// perfilFuncionCanalGC.setMontoMinimoCompartido((long)10000001);
			// perfilFuncionCanalGC.setMontoMaximoCompartido((long)1000000000);
			// entityManager.persist(perfilFuncionCanalGC);
			// if(perfilFuncionCanalGC != null){
			// if(usuarioCargo.getZona() != null){
			// for(ZonaUsuarioDTO z : this.listaZonaGcGs){
			// if(z.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
			// zsnc.setTipoConcepto(ConceptosType.ZONA);
			// zsnc.setCodigo(z.getCodigo());
			// zsnc.setDescripcion(z.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// if(usuarioCargo.getSucursal() != null){
			// for(SucursalesUsuarioDTO s : this.listaSucursalGcGs){
			// if(s.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
			// zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
			// zsnc.setCodigo(s.getCodigo());
			// zsnc.setDescripcion(s.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// if(usuarioCargo.getNegocio() != null){
			// for(NegocioUsuarioDTO n : this.listaNegocioGcGs){
			// if(n.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
			// zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
			// zsnc.setCodigo(n.getCodigo());
			// zsnc.setDescripcion(n.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// if(usuarioCargo.getConcepto() != null){
			// for(ConceptoUsuarioDTO c : this.listaConceptoGcGs){
			// if(c.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
			// zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
			// zsnc.setCodigo(c.getCodigo());
			// zsnc.setDescripcion(c.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// }
			// }catch (Exception e) {
			// log.error("Error, al ingresar de la información generar solicitudes ",
			// e.getMessage());
			// }
			// }else if(canal.equals(TipoCuentasKupferType.MX.name())){
			// CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
			// canalUsuarioCargo.setUsuarioCargo(usuarioCargo);
			// canalUsuarioCargo.setTipoCuenta(TipoCuentasKupferType.MX);
			// entityManager.persist(canalUsuarioCargo);
			// /*ingreso especial de generar solicitud GC*/
			// try{
			// PerfilFuncionCanal perfilFuncionCanalMX = new
			// PerfilFuncionCanal();
			// perfilFuncionCanalMX.setCanalUsuarioCargo(canalUsuarioCargo);
			// perfilFuncionCanalMX.setFuncion(FuncionesType.GENERARSOLICITUD);
			// perfilFuncionCanalMX.setMontoMinimo((long)0 );
			// perfilFuncionCanalMX.setMontoMaximo((long)10000000);
			// perfilFuncionCanalMX.setMontoMinimoCompartido((long)10000001);
			// perfilFuncionCanalMX.setMontoMaximoCompartido((long)1000000000);
			// entityManager.persist(perfilFuncionCanalMX);
			// if(perfilFuncionCanalMX != null){
			// if(usuarioCargo.getZona() != null){
			// for(ZonaUsuarioDTO z : this.listaZonaMxGs){
			// if(z.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
			// zsnc.setTipoConcepto(ConceptosType.ZONA);
			// zsnc.setCodigo(z.getCodigo());
			// zsnc.setDescripcion(z.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// if(usuarioCargo.getSucursal() != null){
			// for(SucursalesUsuarioDTO s : this.listaSucursalMxGs){
			// if(s.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
			// zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
			// zsnc.setCodigo(s.getCodigo());
			// zsnc.setDescripcion(s.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// if(usuarioCargo.getNegocio() != null){
			// for(NegocioUsuarioDTO n : this.listaNegocioMxGs){
			// if(n.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
			// zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
			// zsnc.setCodigo(n.getCodigo());
			// zsnc.setDescripcion(n.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// if(usuarioCargo.getConcepto() != null){
			// for(ConceptoUsuarioDTO c : this.listaConceptoMxGs){
			// if(c.isStatus()==true){
			// ZonaSucursalNegocioConcepto zsnc = new
			// ZonaSucursalNegocioConcepto();
			// zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
			// zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
			// zsnc.setCodigo(c.getCodigo());
			// zsnc.setDescripcion(c.getDescripcion());
			// entityManager.persist(zsnc);
			// }
			// }
			// }
			// }
			// }catch (Exception e) {
			// log.error("Error, al ingresar de la información generar solicitudes ",
			// e.getMessage());
			// }
			// }
			// }
			// }
			// entityManager.flush();
			// entityManager.clear();
			// contador++;
			// if(contador == cantidad){
			// break;
			// }
			// }
			// }
			// }
			// }
		} catch (Exception e) {
			log.error("Error, al asignar masivamente . #0", e.getMessage());
		}

	}

	public UsuarioIngresoDTO getUsuarioIngresoDTO(String username) {
		UsuarioIngresoDTO usuario = null;
		try {
			if (listaUsuariosDefinidos != null) {
				for (UsuarioIngresoDTO objeto : listaUsuariosDefinidos) {
					log.debug("username: #0 <--> objeto.getUsuario(): #1",
							username, objeto.getUsuario());
					if (objeto != null
							&& (objeto.getUsuario().trim()).equals(username
									.trim())) {
						usuario = objeto;
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error(
					"Error, sacar los datos de la lista  usuarios definidos #0",
					e.getMessage());
		}
		return usuario;
	}

	public void modificarRelaciones() {
		if (validarInformacion()) {
			try {
				if (listaZonaSucursalNegocioConceptos != null) {
					for (ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos) {
						entityManager.remove(zsnc);
					}
					if (listaPerfilFuncionCanals != null) {
						for (PerfilFuncionCanal pfc : listaPerfilFuncionCanals) {
							entityManager.remove(pfc);
						}

						if (listaCanalUsuarioCargos != null) {
							for (CanalUsuarioCargo cuc : listaCanalUsuarioCargos) {
								entityManager.remove(cuc);

							}
						}
					}
					entityManager.flush();
				}

				try {
					if (this.eliminado == false) {
						this.usuario.setEliminado(false);
					} else {
						this.usuario.setEliminado(true);
					}
					this.usuario.setPassword(this.password);
					entityManager.merge(this.usuario);
					entityManager.flush();
					entityManager.refresh(this.usuario);
					
					UsuarioCargo usuarioAux = scoringService.getUsuarioCargo(usuario.getIdPersonal());
					usuarioAux.setCargo(cargo);
					if (this.zona) {
						usuarioAux.setZona(ConceptosType.ZONA);
					} else {
						usuarioAux.setZona(null);
					}
					if (this.sucursal) {
						usuarioAux.setSucursal(ConceptosType.SUCURSAL);
					} else {
						usuarioAux.setSucursal(null);
					}
					if (this.negocio) {
						usuarioAux.setNegocio(ConceptosType.NEGOCIO);
					} else {
						usuarioAux.setNegocio(null);
					}
					if (this.concepto) {
						usuarioAux.setConcepto(ConceptosType.CONCEPTO);
					} else {
						usuarioAux.setConcepto(null);
					}

					if (this.administrador) {
						usuarioAux.setAdministrador(this.administrador);
					} else {
						usuarioAux.setAdministrador(false);
					}

					if (this.otrasSolictudes) {
						usuarioAux.setEnvioAutomatico(this.otrasSolictudes);
					} else {
						usuarioAux.setEnvioAutomatico(false);
					}

					if (this.noNotificaInicial) {
						usuarioAux.setnoNotificaInicial(this.noNotificaInicial);
					} else {
						usuarioAux.setnoNotificaInicial(false);
					}

					entityManager.merge(usuarioAux);

					if (usuarioAux != null) {
						for (String canal : this.canales) {
							if (canal != null
									&& canal.equals(TipoCuentasKupferType.KX
											.name())) {/*
														 * seleccion del perfil
														 * de kupfer express
														 */
								CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
								canalUsuarioCargo.setUsuarioCargo(usuarioAux);
								canalUsuarioCargo
										.setTipoCuenta(TipoCuentasKupferType.KX);
								entityManager.persist(canalUsuarioCargo);
								if (canalUsuarioCargo != null) {
									/* ingreso especial de generar solicitud KX */
									try {
										PerfilFuncionCanal perfilFuncionCanalKX = new PerfilFuncionCanal();
										perfilFuncionCanalKX
												.setCanalUsuarioCargo(canalUsuarioCargo);
										perfilFuncionCanalKX
												.setFuncion(FuncionesType.GENERARSOLICITUD);
										perfilFuncionCanalKX
												.setMontoMinimo(this.montoDesdeKX);
										perfilFuncionCanalKX
												.setMontoMaximo(this.montoHastaKX);
										perfilFuncionCanalKX
												.setMontoMinimoCompartido(this.montoDesdeCompKX);
										perfilFuncionCanalKX
												.setMontoMaximoCompartido(this.montoHastaCompKX);
										entityManager
												.persist(perfilFuncionCanalKX);
										if (perfilFuncionCanalKX != null) {
											for (ZonaUsuarioDTO z : this.listaZonaKxGs) {
												if (z.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
													zsnc.setTipoConcepto(ConceptosType.ZONA);
													zsnc.setCodigo(z
															.getCodigo());
													zsnc.setDescripcion(z
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
											for (SucursalesUsuarioDTO s : this.listaSucursalKxGs) {
												if (s.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
													zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
													zsnc.setCodigo(s
															.getCodigo());
													zsnc.setDescripcion(s
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
											for (NegocioUsuarioDTO n : this.listaNegocioKxGs) {
												if (n.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
													zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
													zsnc.setCodigo(n
															.getCodigo());
													zsnc.setDescripcion(n
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
											for (ConceptoUsuarioDTO c : this.listaConceptoKxGs) {
												if (c.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
													zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
													zsnc.setCodigo(c
															.getCodigo());
													zsnc.setDescripcion(c
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
										}
									} catch (Exception e) {
										log.error(
												"Error, al ingresar de la información generar solicitudes ",
												e.getMessage());
									}

									/* fin del ingreso es */
									if (perfiles != null) {
										for (String perfil : perfiles) {
											if (perfil != null
													&& perfil.equals("1")) {// perfil
																			// para
																			// aprobar
																			// y/o
																			// rechazar
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.APRUEBARECHAZO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeKX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaKX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompKX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompKX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaKxAr) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalKxAr) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioKxAr) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoKxAr) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil != null
													&& perfil.equals("2")) {// perfil
																			// para
																			// Agregar
																			// Comenatrios
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.AGREGARCOMENTARIO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeKX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaKX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompKX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompKX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaKxAc) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalKxAc) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioKxAc) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoKxAc) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}
											}
										}
									}
									// fin de recorrido de perfiles

									if (perfiles2 != null) {
										// comienzo de recorrido de perfiles2
										for (String perfil2 : perfiles2) {
											if (perfil2 != null
													&& perfil2.equals("3")) {// perfil
																				// de
																				// analista
																				// de
																				// negocio.
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.ANALISTANEGOCIO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeKX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaKX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompKX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompKX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaKxEn) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalKxEn) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioKxEn) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoKxEn) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil2 != null
													&& perfil2.equals("4")) {// perfil
																				// para
																				// analisis
																				// financiero
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.ANALISTAFINANCIERO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeKX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaKX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompKX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompKX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaKxAf) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalKxAf) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioKxAf) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoKxAf) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil2 != null
													&& perfil2.equals("5")) {// perfil
																				// para
																				// solo
																				// visualizar
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.SOLOVISUALIZAR);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeKX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaKX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompKX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompKX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaKxSv) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalKxSv) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioKxSv) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoKxSv) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}
											}
										}// fin de recorrido de perfiles2
									}
								}

							} else if (canal != null
									&& canal.equals(TipoCuentasKupferType.GC
											.name())) {/*
														 * seleccion del perfil
														 * de Grandes Cuentas
														 */
								CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
								canalUsuarioCargo.setUsuarioCargo(usuarioAux);
								canalUsuarioCargo
										.setTipoCuenta(TipoCuentasKupferType.GC);
								entityManager.persist(canalUsuarioCargo);
								if (canalUsuarioCargo != null) {

									/*
									 * ingreso especial de generar solicitud de
									 * GC
									 */
									try {
										PerfilFuncionCanal perfilFuncionCanalGC = new PerfilFuncionCanal();
										perfilFuncionCanalGC
												.setCanalUsuarioCargo(canalUsuarioCargo);
										perfilFuncionCanalGC
												.setFuncion(FuncionesType.GENERARSOLICITUD);
										perfilFuncionCanalGC
												.setMontoMinimo(this.montoDesdeGC);
										perfilFuncionCanalGC
												.setMontoMaximo(this.montoHastaGC);
										perfilFuncionCanalGC
												.setMontoMinimoCompartido(this.montoDesdeCompGC);
										perfilFuncionCanalGC
												.setMontoMaximoCompartido(this.montoHastaCompGC);
										entityManager
												.persist(perfilFuncionCanalGC);
										if (perfilFuncionCanalGC != null) {

											for (ZonaUsuarioDTO z : this.listaZonaGcGs) {
												if (z.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
													zsnc.setTipoConcepto(ConceptosType.ZONA);
													zsnc.setCodigo(z
															.getCodigo());
													zsnc.setDescripcion(z
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}

											for (SucursalesUsuarioDTO s : this.listaSucursalGcGs) {
												if (s.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
													zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
													zsnc.setCodigo(s
															.getCodigo());
													zsnc.setDescripcion(s
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}

											for (NegocioUsuarioDTO n : this.listaNegocioGcGs) {
												if (n.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
													zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
													zsnc.setCodigo(n
															.getCodigo());
													zsnc.setDescripcion(n
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}

											for (ConceptoUsuarioDTO c : this.listaConceptoGcGs) {
												if (c.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
													zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
													zsnc.setCodigo(c
															.getCodigo());
													zsnc.setDescripcion(c
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
										}
									} catch (Exception e) {
										log.error(
												"Error, al ingresar la información de generar solicitudes de GC",
												e.getMessage());
									}
									/*
									 * fin ingreso especial de generar solicitud
									 * de GC
									 */

									if (perfilesGC != null) {
										for (String perfil : perfilesGC) {
											if (perfil != null
													&& perfil.equals("1")) {// perfil
																			// para
																			// aprobar
																			// y/o
																			// rechazar
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.APRUEBARECHAZO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeGC);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaGC);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompGC);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompGC);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaGcAr) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalGcAr) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioGcAr) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoGcAr) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil != null
													&& perfil.equals("2")) {// perfil
																			// para
																			// Agregar
																			// Comenatrios
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.AGREGARCOMENTARIO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeGC);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaGC);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompGC);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompGC);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaGcAc) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalGcAc) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioGcAc) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoGcAc) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}
											}
										}
										// fin de recorrido de perfilesGC
									}

									if (perfiles2GC != null) {
										// comienzo de recorrido de perfiles2GC
										for (String perfil2 : perfiles2GC) {
											if (perfil2 != null
													&& perfil2.equals("3")) {// perfil
																				// de
																				// analista
																				// de
																				// negocio.
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.ANALISTANEGOCIO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeGC);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaGC);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompGC);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompGC);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaGcEn) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalGcEn) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioGcEn) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoGcEn) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil2 != null
													&& perfil2.equals("4")) {// perfil
																				// para
																				// analisis
																				// financiero
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.ANALISTAFINANCIERO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeGC);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaGC);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompGC);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompGC);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaGcAf) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalGcAf) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioGcAf) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoGcAf) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil2 != null
													&& perfil2.equals("5")) {// perfil
																				// para
																				// solo
																				// visualizar
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.SOLOVISUALIZAR);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeGC);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaGC);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompGC);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompGC);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaGcSv) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalGcSv) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioGcSv) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoGcSv) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}
											}
										}// fin de recorrido de perfiles2GC
									}
								}
							} else if (canal != null
									&& canal.equals(TipoCuentasKupferType.MX
											.name())) {/*
														 * seleccion del perfil
														 * de Mixto
														 */
								CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
								canalUsuarioCargo.setUsuarioCargo(usuarioAux);
								canalUsuarioCargo
										.setTipoCuenta(TipoCuentasKupferType.MX);
								entityManager.persist(canalUsuarioCargo);
								if (canalUsuarioCargo != null) {
									/*
									 * ingreso especial de generar solicitud de
									 * MX
									 */
									try {

										PerfilFuncionCanal perfilFuncionCanalMX = new PerfilFuncionCanal();
										perfilFuncionCanalMX
												.setCanalUsuarioCargo(canalUsuarioCargo);
										perfilFuncionCanalMX
												.setFuncion(FuncionesType.GENERARSOLICITUD);
										perfilFuncionCanalMX
												.setMontoMinimo(this.montoDesdeMX);
										perfilFuncionCanalMX
												.setMontoMaximo(this.montoHastaMX);
										perfilFuncionCanalMX
												.setMontoMinimoCompartido(this.montoDesdeCompMX);
										perfilFuncionCanalMX
												.setMontoMaximoCompartido(this.montoHastaCompMX);
										entityManager
												.persist(perfilFuncionCanalMX);
										if (perfilFuncionCanalMX != null) {
											for (ZonaUsuarioDTO z : this.listaZonaMxGs) {
												if (z.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
													zsnc.setTipoConcepto(ConceptosType.ZONA);
													zsnc.setCodigo(z
															.getCodigo());
													zsnc.setDescripcion(z
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
											for (SucursalesUsuarioDTO s : this.listaSucursalMxGs) {
												if (s.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
													zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
													zsnc.setCodigo(s
															.getCodigo());
													zsnc.setDescripcion(s
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
											for (NegocioUsuarioDTO n : this.listaNegocioMxGs) {
												if (n.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
													zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
													zsnc.setCodigo(n
															.getCodigo());
													zsnc.setDescripcion(n
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
											for (ConceptoUsuarioDTO c : this.listaConceptoMxGs) {
												if (c.isStatus() == true) {
													ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
													zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
													zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
													zsnc.setCodigo(c
															.getCodigo());
													zsnc.setDescripcion(c
															.getDescripcion());
													entityManager.persist(zsnc);
												}
											}
										}
									} catch (Exception e) {
										log.error(
												"Error, al ingresar la información de generar solicitudes de MX",
												e.getMessage());
									}
									/*
									 * fin ingreso especial de generar solicitud
									 * de MX
									 */

									if (perfilesMX != null) {
										for (String perfil : perfilesMX) {
											if (perfil != null
													&& perfil.equals("1")) {// perfil
																			// para
																			// aprobar
																			// y/o
																			// rechazar
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.APRUEBARECHAZO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeMX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaMX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompMX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompMX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaMxAr) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalMxAr) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioMxAr) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoMxAr) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil != null
													&& perfil.equals("2")) {// perfil
																			// para
																			// Agregar
																			// Comenatrios
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.AGREGARCOMENTARIO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeMX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaMX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompMX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompMX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaMxAc) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalMxAc) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioMxAc) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoMxAc) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}
											}
										}
										// fin de recorrido de perfilesMX
									}

									if (perfiles2MX != null) {
										// comienzo de recorrido de perfiles2MX
										for (String perfil2 : perfiles2MX) {
											if (perfil2 != null
													&& perfil2.equals("3")) {// perfil
																				// de
																				// analista
																				// de
																				// negocio.
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.ANALISTANEGOCIO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeMX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaMX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompMX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompMX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaMxEn) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalMxEn) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioMxEn) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoMxEn) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil2 != null
													&& perfil2.equals("4")) {// perfil
																				// para
																				// analisis
																				// financiero
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.ANALISTAFINANCIERO);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeMX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaMX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompMX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompMX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaMxAf) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalMxAf) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioMxAf) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoMxAf) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}

											} else if (perfil2 != null
													&& perfil2.equals("5")) {// perfil
																				// para
																				// solo
																				// visualizar
												PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
												perfilFuncionCanal
														.setCanalUsuarioCargo(canalUsuarioCargo);
												perfilFuncionCanal
														.setFuncion(FuncionesType.SOLOVISUALIZAR);
												perfilFuncionCanal
														.setMontoMinimo(this.montoDesdeMX);
												perfilFuncionCanal
														.setMontoMaximo(this.montoHastaMX);
												perfilFuncionCanal
														.setMontoMinimoCompartido(this.montoDesdeCompMX);
												perfilFuncionCanal
														.setMontoMaximoCompartido(this.montoHastaCompMX);
												entityManager
														.persist(perfilFuncionCanal);
												if (perfilFuncionCanal != null) {
													if (this.zona) {
														for (ZonaUsuarioDTO z : this.listaZonaMxSv) {
															if (z.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.ZONA);
																zsnc.setCodigo(z
																		.getCodigo());
																zsnc.setDescripcion(z
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.sucursal) {
														for (SucursalesUsuarioDTO s : this.listaSucursalMxSv) {
															if (s.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
																zsnc.setCodigo(s
																		.getCodigo());
																zsnc.setDescripcion(s
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.negocio) {
														for (NegocioUsuarioDTO n : this.listaNegocioMxSv) {
															if (n.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
																zsnc.setCodigo(n
																		.getCodigo());
																zsnc.setDescripcion(n
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
													if (this.concepto) {
														for (ConceptoUsuarioDTO c : this.listaConceptoMxSv) {
															if (c.isStatus() == true) {
																ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
																zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
																zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
																zsnc.setCodigo(c
																		.getCodigo());
																zsnc.setDescripcion(c
																		.getDescripcion());
																entityManager
																		.persist(zsnc);
															}
														}
													}
												}
											}
										}// fin de recorrido de perfiles2MX
									}
								}
							}
						}
					}
					entityManager.flush();
					limpiarDatos();
					FacesMessages
							.instance()
							.add(Severity.INFO,
									"Los datos fueron asociado al usuario exitosamente...!!");
				} catch (Exception e) {
					log.error("Error, al insertar los datos #0", e.getMessage());
				}

			} catch (Exception e) {
				log.error("Error, al eliminar los datos de usuario. #0",
						e.getMessage());
			}
		}

	}

	public void limparPantalla() {
		try {
			limpiarDatos();
		} catch (Exception e) {
			log.error("Error, al eliminar los datos de usuario.",
					e.getMessage());
		}

	}

	public void limpiarDatos() {
		this.setUsuario(null);
		this.setCargo(null);
		this.setPassword(null);
		this.setEliminado(false);
		this.conceptos = null;
		this.administrador = false;
		this.otrasSolictudes = false;
		this.canales = null;
		this.montoDesdeKX = (long) 0;
		this.montoHastaKX = (long) 0;
		this.montoDesdeCompKX = (long) 0;
		this.montoHastaCompKX = (long) 0;
		this.montoDesdeGC = (long) 0;
		this.montoHastaGC = (long) 0;
		this.montoDesdeCompGC = (long) 0;
		this.montoHastaCompGC = (long) 0;
		this.montoDesdeMX = (long) 0;
		this.montoHastaMX = (long) 0;
		this.montoDesdeCompMX = (long) 0;
		this.montoHastaCompMX = (long) 0;
		this.setAccion("");
		this.perfiles = null;
		this.perfiles2 = null;
		this.perfilesGC = null;
		this.perfiles2GC = null;
		this.perfiles2MX = null;
		this.perfilesMX = null;

		/* limpia lista de kepfer express */
		verificarTodosZonaKxGs(2);
		verificarTodosZonaKxEn(2);
		verificarTodosZonaKxAr(2);
		verificarTodosZonaKxAf(2);
		verificarTodosZonaKxAc(2);
		verificarTodosZonaKxSv(2);

		verificarTodosSucursalKxGs(2);
		verificarTodosSucursalKxEn(2);
		verificarTodosSucursalKxAr(2);
		verificarTodosSucursalKxAf(2);
		verificarTodosSucursalKxAc(2);
		verificarTodosSucursalKxSv(2);

		verificarTodosNegocioKxGs(2);
		verificarTodosNegocioKxEn(2);
		verificarTodosNegocioKxAr(2);
		verificarTodosNegocioKxAf(2);
		verificarTodosNegocioKxAc(2);
		verificarTodosNegocioKxSv(2);

		verificarTodosConceptoGcGs(2);
		verificarTodosConceptoGcEn(2);
		verificarTodosConceptoGcAr(2);
		verificarTodosConceptoGcAf(2);
		verificarTodosConceptoGcAc(2);
		verificarTodosConceptoGcSv(2);

		/* limpia lista de grandes cuentas */
		verificarTodosZonaGcGs(2);
		verificarTodosZonaGcEn(2);
		verificarTodosZonaGcAr(2);
		verificarTodosZonaGcAf(2);
		verificarTodosZonaGcAc(2);
		verificarTodosZonaGcSv(2);

		verificarTodosSucursalGcGs(2);
		verificarTodosSucursalGcEn(2);
		verificarTodosSucursalGcAr(2);
		verificarTodosSucursalGcAf(2);
		verificarTodosSucursalGcAc(2);
		verificarTodosSucursalGcSv(2);

		verificarTodosNegocioGcGs(2);
		verificarTodosNegocioGcEn(2);
		verificarTodosNegocioGcAr(2);
		verificarTodosNegocioGcAf(2);
		verificarTodosNegocioGcAc(2);
		verificarTodosNegocioGcSv(2);

		verificarTodosConceptoGcGs(2);
		verificarTodosConceptoGcEn(2);
		verificarTodosConceptoGcAr(2);
		verificarTodosConceptoGcAf(2);
		verificarTodosConceptoGcAc(2);
		verificarTodosConceptoGcSv(2);

		/* mixto */
		verificarTodosZonaMxGs(2);
		verificarTodosZonaMxEn(2);
		verificarTodosZonaMxAr(2);
		verificarTodosZonaMxAf(2);
		verificarTodosZonaMxAc(2);
		verificarTodosZonaMxSv(2);

		verificarTodosSucursalMxGs(2);
		verificarTodosSucursalMxEn(2);
		verificarTodosSucursalMxAr(2);
		verificarTodosSucursalMxAf(2);
		verificarTodosSucursalMxAc(2);
		verificarTodosSucursalMxSv(2);

		verificarTodosNegocioMxGs(2);
		verificarTodosNegocioMxEn(2);
		verificarTodosNegocioMxAr(2);
		verificarTodosNegocioMxAf(2);
		verificarTodosNegocioMxAc(2);
		verificarTodosNegocioMxSv(2);

		verificarTodosConceptoMxGs(2);
		verificarTodosConceptoMxEn(2);
		verificarTodosConceptoMxAr(2);
		verificarTodosConceptoMxAf(2);
		verificarTodosConceptoMxAc(2);
		verificarTodosConceptoMxSv(2);

		/* desahabiltacion de los canales */
		this.setPestanakupferExpres(true);
		this.setPestanaGrandesCuentas(true);
		this.setPestanaMixto(true);

		/* desahabilitar pestañas de los perfiles KX */
		this.setGenerarSolicitudes(true);
		this.setAqpruebaRechaza(true);
		this.setAgregarComentarios(true);
		this.setEjecutivoNegocio(true);
		this.setAnalistaFinanciero(true);
		this.setSoloVisualizar(true);

		/* desahabilitar pestañas de los perfiles GC */
		this.setGenerarSolicitudesGC(true);
		this.setAqpruebaRechazaGC(true);
		this.setAgregarComentariosGC(true);
		this.setEjecutivoNegocioGC(true);
		this.setAnalistaFinancieroGC(true);
		this.setSoloVisualizarGC(true);

		/* desahabilitar pestañas de los perfiles MX */
		this.setGenerarSolicitudesMX(true);
		this.setAqpruebaRechazaMX(true);
		this.setAgregarComentariosMX(true);
		this.setEjecutivoNegocioMX(true);
		this.setAnalistaFinancieroMX(true);
		this.setSoloVisualizarMX(true);

		sacarUsuariosNuevosDefinidos();

	}

	@SuppressWarnings("unchecked")
	public void sacarUsuariosNuevosDefinidos() {
		try {
			listaUsuarioCargos = (List<UsuarioCargo>) entityManager
					.createQuery(
							"select u from UsuarioCargo u "
									+ " where u.usuario.idPersonal in ("
									+ " select u.idPersonal from Usuariosegur u "
									+ " where u.alias <> null and u.nombre <> null  and u.nombre != 'ADMINISTRADOR' and u.eliminado=0 "
									+ ")" + "order by u.usuario.nombre")
					.getResultList();

		} catch (Exception e) {
			log.error("Error, al sacar los usuarios nuevos definidos #0",
					e.getMessage());
		}
	}

	/* gets y sets */
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<Usuariosegur> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuariosegur> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	@BypassInterceptors
	public List<LcredPerfiles> getListaPerfiles() {
		return listaPerfiles;
	}

	@BypassInterceptors
	public void setListaPerfiles(List<LcredPerfiles> listaPerfiles) {
		this.listaPerfiles = listaPerfiles;
	}

	public Usuariosegur getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuariosegur usuario) {
		this.usuario = usuario;
	}

	@BypassInterceptors
	public List<LcredPerfiles> getListaUsuariosPerfilesSeleccionado() {
		return listaUsuariosPerfilesSeleccionado;
	}

	@BypassInterceptors
	public void setListaUsuariosPerfilesSeleccionado(
			List<LcredPerfiles> listaUsuariosPerfilesSeleccionado) {
		this.listaUsuariosPerfilesSeleccionado = listaUsuariosPerfilesSeleccionado;
	}

	@BypassInterceptors
	public List<UsuarioPerfil> getListaUsuarioPerfiles() {
		return listaUsuarioPerfiles;
	}

	@BypassInterceptors
	public void setListaUsuarioPerfiles(List<UsuarioPerfil> listaUsuarioPerfiles) {
		this.listaUsuarioPerfiles = listaUsuarioPerfiles;
	}

	public List<LcredCargos> getListaCargos() {
		return listaCargos;
	}

	public void setListaCargos(List<LcredCargos> listaCargos) {
		this.listaCargos = listaCargos;
	}

	public LcredCargos getCargo() {
		return cargo;
	}

	public void setCargo(LcredCargos cargo) {
		this.cargo = cargo;
	}

	public String[] getConceptos() {
		return conceptos;
	}

	public void setConceptos(String[] conceptos) {
		this.conceptos = conceptos;
	}

	public String[] getCanales() {
		return canales;
	}

	public void setCanales(String[] canales) {
		this.canales = canales;
	}

	public boolean isPestanakupferExpres() {
		return pestanakupferExpres;
	}

	public void setPestanakupferExpres(boolean pestanakupferExpres) {
		this.pestanakupferExpres = pestanakupferExpres;
	}

	public boolean isPestanaGrandesCuentas() {
		return pestanaGrandesCuentas;
	}

	public void setPestanaGrandesCuentas(boolean pestanaGrandesCuentas) {
		this.pestanaGrandesCuentas = pestanaGrandesCuentas;
	}

	public boolean isPestanaMixto() {
		return pestanaMixto;
	}

	public void setPestanaMixto(boolean pestanaMixto) {
		this.pestanaMixto = pestanaMixto;
	}

	public int getNumeropestana() {
		return numeropestana;
	}

	public void setNumeropestana(int numeropestana) {
		this.numeropestana = numeropestana;
	}

	public int getPestanaUno() {
		return pestanaUno;
	}

	public void setPestanaUno(int pestanaUno) {
		this.pestanaUno = pestanaUno;
	}

	public int getPestanaDos() {
		return pestanaDos;
	}

	public void setPestanaDos(int pestanaDos) {
		this.pestanaDos = pestanaDos;
	}

	public int getPestanaTres() {
		return pestanaTres;
	}

	public void setPestanaTres(int pestanaTres) {
		this.pestanaTres = pestanaTres;
	}

	public int getPestanacuarto() {
		return pestanacuarto;
	}

	public void setPestanacuarto(int pestanacuarto) {
		this.pestanacuarto = pestanacuarto;
	}

	public int getPestanaCinco() {
		return pestanaCinco;
	}

	public void setPestanaCinco(int pestanaCinco) {
		this.pestanaCinco = pestanaCinco;
	}

	public String[] getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(String[] perfiles) {
		this.perfiles = perfiles;
	}

	public Long getMontoDesdeKX() {
		return montoDesdeKX;
	}

	public void setMontoDesdeKX(Long montoDesdeKX) {
		this.montoDesdeKX = montoDesdeKX;
	}

	public Long getMontoHastaKX() {
		return montoHastaKX;
	}

	public void setMontoHastaKX(Long montoHastaKX) {
		this.montoHastaKX = montoHastaKX;
	}

	public Long getMontoDesdeCompKX() {
		return montoDesdeCompKX;
	}

	public void setMontoDesdeCompKX(Long montoDesdeCompKX) {
		this.montoDesdeCompKX = montoDesdeCompKX;
	}

	public Long getMontoHastaCompKX() {
		return montoHastaCompKX;
	}

	public void setMontoHastaCompKX(Long montoHastaCompKX) {
		this.montoHastaCompKX = montoHastaCompKX;
	}

	public Long getMontoDesdeGC() {
		return montoDesdeGC;
	}

	public void setMontoDesdeGC(Long montoDesdeGC) {
		this.montoDesdeGC = montoDesdeGC;
	}

	public Long getMontoHastaGC() {
		return montoHastaGC;
	}

	public void setMontoHastaGC(Long montoHastaGC) {
		this.montoHastaGC = montoHastaGC;
	}

	public Long getMontoDesdeCompGC() {
		return montoDesdeCompGC;
	}

	public void setMontoDesdeCompGC(Long montoDesdeCompGC) {
		this.montoDesdeCompGC = montoDesdeCompGC;
	}

	public Long getMontoHastaCompGC() {
		return montoHastaCompGC;
	}

	public void setMontoHastaCompGC(Long montoHastaCompGC) {
		this.montoHastaCompGC = montoHastaCompGC;
	}

	public Long getMontoDesdeMX() {
		return montoDesdeMX;
	}

	public void setMontoDesdeMX(Long montoDesdeMX) {
		this.montoDesdeMX = montoDesdeMX;
	}

	public Long getMontoHastaMX() {
		return montoHastaMX;
	}

	public void setMontoHastaMX(Long montoHastaMX) {
		this.montoHastaMX = montoHastaMX;
	}

	public Long getMontoDesdeCompMX() {
		return montoDesdeCompMX;
	}

	public void setMontoDesdeCompMX(Long montoDesdeCompMX) {
		this.montoDesdeCompMX = montoDesdeCompMX;
	}

	public Long getMontoHastaCompMX() {
		return montoHastaCompMX;
	}

	public void setMontoHastaCompMX(Long montoHastaCompMX) {
		this.montoHastaCompMX = montoHastaCompMX;
	}

	public boolean isGenerarSolicitudes() {
		return generarSolicitudes;
	}

	public void setGenerarSolicitudes(boolean generarSolicitudes) {
		this.generarSolicitudes = generarSolicitudes;
	}

	public boolean isAqpruebaRechaza() {
		return aqpruebaRechaza;
	}

	public void setAqpruebaRechaza(boolean aqpruebaRechaza) {
		this.aqpruebaRechaza = aqpruebaRechaza;
	}

	public boolean isEjecutivoNegocio() {
		return ejecutivoNegocio;
	}

	public void setEjecutivoNegocio(boolean ejecutivoNegocio) {
		this.ejecutivoNegocio = ejecutivoNegocio;
	}

	public boolean isAnalistaFinanciero() {
		return analistaFinanciero;
	}

	public void setAnalistaFinanciero(boolean analistaFinanciero) {
		this.analistaFinanciero = analistaFinanciero;
	}

	public boolean isAgregarComentarios() {
		return agregarComentarios;
	}

	public void setAgregarComentarios(boolean agregarComentarios) {
		this.agregarComentarios = agregarComentarios;
	}

	public boolean isSoloVisualizar() {
		return soloVisualizar;
	}

	public void setSoloVisualizar(boolean soloVisualizar) {
		this.soloVisualizar = soloVisualizar;
	}

	public String[] getPerfiles2() {
		return perfiles2;
	}

	public void setPerfiles2(String[] perfiles2) {
		this.perfiles2 = perfiles2;
	}

	public String[] getPerfilesGC() {
		return perfilesGC;
	}

	public void setPerfilesGC(String[] perfilesGC) {
		this.perfilesGC = perfilesGC;
	}

	public String[] getPerfiles2GC() {
		return perfiles2GC;
	}

	public void setPerfiles2GC(String[] perfiles2gc) {
		perfiles2GC = perfiles2gc;
	}

	public String[] getPerfilesMX() {
		return perfilesMX;
	}

	public void setPerfilesMX(String[] perfilesMX) {
		this.perfilesMX = perfilesMX;
	}

	public String[] getPerfiles2MX() {
		return perfiles2MX;
	}

	public void setPerfiles2MX(String[] perfiles2mx) {
		perfiles2MX = perfiles2mx;
	}

	public boolean isGenerarSolicitudesGC() {
		return generarSolicitudesGC;
	}

	public void setGenerarSolicitudesGC(boolean generarSolicitudesGC) {
		this.generarSolicitudesGC = generarSolicitudesGC;
	}

	public boolean isAqpruebaRechazaGC() {
		return aqpruebaRechazaGC;
	}

	public void setAqpruebaRechazaGC(boolean aqpruebaRechazaGC) {
		this.aqpruebaRechazaGC = aqpruebaRechazaGC;
	}

	public boolean isEjecutivoNegocioGC() {
		return ejecutivoNegocioGC;
	}

	public void setEjecutivoNegocioGC(boolean ejecutivoNegocioGC) {
		this.ejecutivoNegocioGC = ejecutivoNegocioGC;
	}

	public boolean isAnalistaFinancieroGC() {
		return analistaFinancieroGC;
	}

	public void setAnalistaFinancieroGC(boolean analistaFinancieroGC) {
		this.analistaFinancieroGC = analistaFinancieroGC;
	}

	public boolean isAgregarComentariosGC() {
		return agregarComentariosGC;
	}

	public void setAgregarComentariosGC(boolean agregarComentariosGC) {
		this.agregarComentariosGC = agregarComentariosGC;
	}

	public boolean isSoloVisualizarGC() {
		return soloVisualizarGC;
	}

	public void setSoloVisualizarGC(boolean soloVisualizarGC) {
		this.soloVisualizarGC = soloVisualizarGC;
	}

	public boolean isGenerarSolicitudesMX() {
		return generarSolicitudesMX;
	}

	public void setGenerarSolicitudesMX(boolean generarSolicitudesMX) {
		this.generarSolicitudesMX = generarSolicitudesMX;
	}

	public boolean isAqpruebaRechazaMX() {
		return aqpruebaRechazaMX;
	}

	public void setAqpruebaRechazaMX(boolean aqpruebaRechazaMX) {
		this.aqpruebaRechazaMX = aqpruebaRechazaMX;
	}

	public boolean isEjecutivoNegocioMX() {
		return ejecutivoNegocioMX;
	}

	public void setEjecutivoNegocioMX(boolean ejecutivoNegocioMX) {
		this.ejecutivoNegocioMX = ejecutivoNegocioMX;
	}

	public boolean isAnalistaFinancieroMX() {
		return analistaFinancieroMX;
	}

	public void setAnalistaFinancieroMX(boolean analistaFinancieroMX) {
		this.analistaFinancieroMX = analistaFinancieroMX;
	}

	public boolean isAgregarComentariosMX() {
		return agregarComentariosMX;
	}

	public void setAgregarComentariosMX(boolean agregarComentariosMX) {
		this.agregarComentariosMX = agregarComentariosMX;
	}

	public boolean isSoloVisualizarMX() {
		return soloVisualizarMX;
	}

	public void setSoloVisualizarMX(boolean soloVisualizarMX) {
		this.soloVisualizarMX = soloVisualizarMX;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxGs() {
		return listaZonaKxGs;
	}

	public void setListaZonaKxGs(List<ZonaUsuarioDTO> listaZonaKxGs) {
		this.listaZonaKxGs = listaZonaKxGs;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxEn() {
		return listaZonaKxEn;
	}

	public void setListaZonaKxEn(List<ZonaUsuarioDTO> listaZonaKxEn) {
		this.listaZonaKxEn = listaZonaKxEn;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxAr() {
		return listaZonaKxAr;
	}

	public void setListaZonaKxAr(List<ZonaUsuarioDTO> listaZonaKxAr) {
		this.listaZonaKxAr = listaZonaKxAr;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxAf() {
		return listaZonaKxAf;
	}

	public void setListaZonaKxAf(List<ZonaUsuarioDTO> listaZonaKxAf) {
		this.listaZonaKxAf = listaZonaKxAf;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxAc() {
		return listaZonaKxAc;
	}

	public void setListaZonaKxAc(List<ZonaUsuarioDTO> listaZonaKxAc) {
		this.listaZonaKxAc = listaZonaKxAc;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxSv() {
		return listaZonaKxSv;
	}

	public void setListaZonaKxSv(List<ZonaUsuarioDTO> listaZonaKxSv) {
		this.listaZonaKxSv = listaZonaKxSv;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxGs() {
		return listaSucursalKxGs;
	}

	public void setListaSucursalKxGs(
			List<SucursalesUsuarioDTO> listaSucursalKxGs) {
		this.listaSucursalKxGs = listaSucursalKxGs;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxEn() {
		return listaSucursalKxEn;
	}

	public void setListaSucursalKxEn(
			List<SucursalesUsuarioDTO> listaSucursalKxEn) {
		this.listaSucursalKxEn = listaSucursalKxEn;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxAr() {
		return listaSucursalKxAr;
	}

	public void setListaSucursalKxAr(
			List<SucursalesUsuarioDTO> listaSucursalKxAr) {
		this.listaSucursalKxAr = listaSucursalKxAr;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxAf() {
		return listaSucursalKxAf;
	}

	public void setListaSucursalKxAf(
			List<SucursalesUsuarioDTO> listaSucursalKxAf) {
		this.listaSucursalKxAf = listaSucursalKxAf;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxAc() {
		return listaSucursalKxAc;
	}

	public void setListaSucursalKxAc(
			List<SucursalesUsuarioDTO> listaSucursalKxAc) {
		this.listaSucursalKxAc = listaSucursalKxAc;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxSv() {
		return listaSucursalKxSv;
	}

	public void setListaSucursalKxSv(
			List<SucursalesUsuarioDTO> listaSucursalKxSv) {
		this.listaSucursalKxSv = listaSucursalKxSv;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxGs() {
		return listaNegocioKxGs;
	}

	public void setListaNegocioKxGs(List<NegocioUsuarioDTO> listaNegocioKxGs) {
		this.listaNegocioKxGs = listaNegocioKxGs;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxEn() {
		return listaNegocioKxEn;
	}

	public void setListaNegocioKxEn(List<NegocioUsuarioDTO> listaNegocioKxEn) {
		this.listaNegocioKxEn = listaNegocioKxEn;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxAr() {
		return listaNegocioKxAr;
	}

	public void setListaNegocioKxAr(List<NegocioUsuarioDTO> listaNegocioKxAr) {
		this.listaNegocioKxAr = listaNegocioKxAr;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxAf() {
		return listaNegocioKxAf;
	}

	public void setListaNegocioKxAf(List<NegocioUsuarioDTO> listaNegocioKxAf) {
		this.listaNegocioKxAf = listaNegocioKxAf;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxAc() {
		return listaNegocioKxAc;
	}

	public void setListaNegocioKxAc(List<NegocioUsuarioDTO> listaNegocioKxAc) {
		this.listaNegocioKxAc = listaNegocioKxAc;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxSv() {
		return listaNegocioKxSv;
	}

	public void setListaNegocioKxSv(List<NegocioUsuarioDTO> listaNegocioKxSv) {
		this.listaNegocioKxSv = listaNegocioKxSv;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxGs() {
		return listaConceptoKxGs;
	}

	public void setListaConceptoKxGs(List<ConceptoUsuarioDTO> listaConceptoKxGs) {
		this.listaConceptoKxGs = listaConceptoKxGs;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxEn() {
		return listaConceptoKxEn;
	}

	public void setListaConceptoKxEn(List<ConceptoUsuarioDTO> listaConceptoKxEn) {
		this.listaConceptoKxEn = listaConceptoKxEn;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxAr() {
		return listaConceptoKxAr;
	}

	public void setListaConceptoKxAr(List<ConceptoUsuarioDTO> listaConceptoKxAr) {
		this.listaConceptoKxAr = listaConceptoKxAr;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxAf() {
		return listaConceptoKxAf;
	}

	public void setListaConceptoKxAf(List<ConceptoUsuarioDTO> listaConceptoKxAf) {
		this.listaConceptoKxAf = listaConceptoKxAf;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxAc() {
		return listaConceptoKxAc;
	}

	public void setListaConceptoKxAc(List<ConceptoUsuarioDTO> listaConceptoKxAc) {
		this.listaConceptoKxAc = listaConceptoKxAc;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxSv() {
		return listaConceptoKxSv;
	}

	public void setListaConceptoKxSv(List<ConceptoUsuarioDTO> listaConceptoKxSv) {
		this.listaConceptoKxSv = listaConceptoKxSv;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcGs() {
		return listaZonaGcGs;
	}

	public void setListaZonaGcGs(List<ZonaUsuarioDTO> listaZonaGcGs) {
		this.listaZonaGcGs = listaZonaGcGs;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcEn() {
		return listaZonaGcEn;
	}

	public void setListaZonaGcEn(List<ZonaUsuarioDTO> listaZonaGcEn) {
		this.listaZonaGcEn = listaZonaGcEn;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcAr() {
		return listaZonaGcAr;
	}

	public void setListaZonaGcAr(List<ZonaUsuarioDTO> listaZonaGcAr) {
		this.listaZonaGcAr = listaZonaGcAr;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcAf() {
		return listaZonaGcAf;
	}

	public void setListaZonaGcAf(List<ZonaUsuarioDTO> listaZonaGcAf) {
		this.listaZonaGcAf = listaZonaGcAf;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcAc() {
		return listaZonaGcAc;
	}

	public void setListaZonaGcAc(List<ZonaUsuarioDTO> listaZonaGcAc) {
		this.listaZonaGcAc = listaZonaGcAc;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcSv() {
		return listaZonaGcSv;
	}

	public void setListaZonaGcSv(List<ZonaUsuarioDTO> listaZonaGcSv) {
		this.listaZonaGcSv = listaZonaGcSv;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcGs() {
		return listaSucursalGcGs;
	}

	public void setListaSucursalGcGs(
			List<SucursalesUsuarioDTO> listaSucursalGcGs) {
		this.listaSucursalGcGs = listaSucursalGcGs;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcEn() {
		return listaSucursalGcEn;
	}

	public void setListaSucursalGcEn(
			List<SucursalesUsuarioDTO> listaSucursalGcEn) {
		this.listaSucursalGcEn = listaSucursalGcEn;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcAr() {
		return listaSucursalGcAr;
	}

	public void setListaSucursalGcAr(
			List<SucursalesUsuarioDTO> listaSucursalGcAr) {
		this.listaSucursalGcAr = listaSucursalGcAr;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcAf() {
		return listaSucursalGcAf;
	}

	public void setListaSucursalGcAf(
			List<SucursalesUsuarioDTO> listaSucursalGcAf) {
		this.listaSucursalGcAf = listaSucursalGcAf;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcAc() {
		return listaSucursalGcAc;
	}

	public void setListaSucursalGcAc(
			List<SucursalesUsuarioDTO> listaSucursalGcAc) {
		this.listaSucursalGcAc = listaSucursalGcAc;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcSv() {
		return listaSucursalGcSv;
	}

	public void setListaSucursalGcSv(
			List<SucursalesUsuarioDTO> listaSucursalGcSv) {
		this.listaSucursalGcSv = listaSucursalGcSv;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcGs() {
		return listaNegocioGcGs;
	}

	public void setListaNegocioGcGs(List<NegocioUsuarioDTO> listaNegocioGcGs) {
		this.listaNegocioGcGs = listaNegocioGcGs;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcEn() {
		return listaNegocioGcEn;
	}

	public void setListaNegocioGcEn(List<NegocioUsuarioDTO> listaNegocioGcEn) {
		this.listaNegocioGcEn = listaNegocioGcEn;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcAr() {
		return listaNegocioGcAr;
	}

	public void setListaNegocioGcAr(List<NegocioUsuarioDTO> listaNegocioGcAr) {
		this.listaNegocioGcAr = listaNegocioGcAr;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcAf() {
		return listaNegocioGcAf;
	}

	public void setListaNegocioGcAf(List<NegocioUsuarioDTO> listaNegocioGcAf) {
		this.listaNegocioGcAf = listaNegocioGcAf;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcAc() {
		return listaNegocioGcAc;
	}

	public void setListaNegocioGcAc(List<NegocioUsuarioDTO> listaNegocioGcAc) {
		this.listaNegocioGcAc = listaNegocioGcAc;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcSv() {
		return listaNegocioGcSv;
	}

	public void setListaNegocioGcSv(List<NegocioUsuarioDTO> listaNegocioGcSv) {
		this.listaNegocioGcSv = listaNegocioGcSv;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcGs() {
		return listaConceptoGcGs;
	}

	public void setListaConceptoGcGs(List<ConceptoUsuarioDTO> listaConceptoGcGs) {
		this.listaConceptoGcGs = listaConceptoGcGs;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcEn() {
		return listaConceptoGcEn;
	}

	public void setListaConceptoGcEn(List<ConceptoUsuarioDTO> listaConceptoGcEn) {
		this.listaConceptoGcEn = listaConceptoGcEn;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcAr() {
		return listaConceptoGcAr;
	}

	public void setListaConceptoGcAr(List<ConceptoUsuarioDTO> listaConceptoGcAr) {
		this.listaConceptoGcAr = listaConceptoGcAr;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcAf() {
		return listaConceptoGcAf;
	}

	public void setListaConceptoGcAf(List<ConceptoUsuarioDTO> listaConceptoGcAf) {
		this.listaConceptoGcAf = listaConceptoGcAf;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcAc() {
		return listaConceptoGcAc;
	}

	public void setListaConceptoGcAc(List<ConceptoUsuarioDTO> listaConceptoGcAc) {
		this.listaConceptoGcAc = listaConceptoGcAc;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcSv() {
		return listaConceptoGcSv;
	}

	public void setListaConceptoGcSv(List<ConceptoUsuarioDTO> listaConceptoGcSv) {
		this.listaConceptoGcSv = listaConceptoGcSv;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxGs() {
		return listaZonaMxGs;
	}

	public void setListaZonaMxGs(List<ZonaUsuarioDTO> listaZonaMxGs) {
		this.listaZonaMxGs = listaZonaMxGs;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxEn() {
		return listaZonaMxEn;
	}

	public void setListaZonaMxEn(List<ZonaUsuarioDTO> listaZonaMxEn) {
		this.listaZonaMxEn = listaZonaMxEn;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxAr() {
		return listaZonaMxAr;
	}

	public void setListaZonaMxAr(List<ZonaUsuarioDTO> listaZonaMxAr) {
		this.listaZonaMxAr = listaZonaMxAr;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxAf() {
		return listaZonaMxAf;
	}

	public void setListaZonaMxAf(List<ZonaUsuarioDTO> listaZonaMxAf) {
		this.listaZonaMxAf = listaZonaMxAf;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxAc() {
		return listaZonaMxAc;
	}

	public void setListaZonaMxAc(List<ZonaUsuarioDTO> listaZonaMxAc) {
		this.listaZonaMxAc = listaZonaMxAc;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxSv() {
		return listaZonaMxSv;
	}

	public void setListaZonaMxSv(List<ZonaUsuarioDTO> listaZonaMxSv) {
		this.listaZonaMxSv = listaZonaMxSv;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxGs() {
		return listaSucursalMxGs;
	}

	public void setListaSucursalMxGs(
			List<SucursalesUsuarioDTO> listaSucursalMxGs) {
		this.listaSucursalMxGs = listaSucursalMxGs;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxEn() {
		return listaSucursalMxEn;
	}

	public void setListaSucursalMxEn(
			List<SucursalesUsuarioDTO> listaSucursalMxEn) {
		this.listaSucursalMxEn = listaSucursalMxEn;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxAr() {
		return listaSucursalMxAr;
	}

	public void setListaSucursalMxAr(
			List<SucursalesUsuarioDTO> listaSucursalMxAr) {
		this.listaSucursalMxAr = listaSucursalMxAr;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxAf() {
		return listaSucursalMxAf;
	}

	public void setListaSucursalMxAf(
			List<SucursalesUsuarioDTO> listaSucursalMxAf) {
		this.listaSucursalMxAf = listaSucursalMxAf;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxAc() {
		return listaSucursalMxAc;
	}

	public void setListaSucursalMxAc(
			List<SucursalesUsuarioDTO> listaSucursalMxAc) {
		this.listaSucursalMxAc = listaSucursalMxAc;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxSv() {
		return listaSucursalMxSv;
	}

	public void setListaSucursalMxSv(
			List<SucursalesUsuarioDTO> listaSucursalMxSv) {
		this.listaSucursalMxSv = listaSucursalMxSv;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxGs() {
		return listaNegocioMxGs;
	}

	public void setListaNegocioMxGs(List<NegocioUsuarioDTO> listaNegocioMxGs) {
		this.listaNegocioMxGs = listaNegocioMxGs;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxEn() {
		return listaNegocioMxEn;
	}

	public void setListaNegocioMxEn(List<NegocioUsuarioDTO> listaNegocioMxEn) {
		this.listaNegocioMxEn = listaNegocioMxEn;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxAr() {
		return listaNegocioMxAr;
	}

	public void setListaNegocioMxAr(List<NegocioUsuarioDTO> listaNegocioMxAr) {
		this.listaNegocioMxAr = listaNegocioMxAr;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxAf() {
		return listaNegocioMxAf;
	}

	public void setListaNegocioMxAf(List<NegocioUsuarioDTO> listaNegocioMxAf) {
		this.listaNegocioMxAf = listaNegocioMxAf;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxAc() {
		return listaNegocioMxAc;
	}

	public void setListaNegocioMxAc(List<NegocioUsuarioDTO> listaNegocioMxAc) {
		this.listaNegocioMxAc = listaNegocioMxAc;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxSv() {
		return listaNegocioMxSv;
	}

	public void setListaNegocioMxSv(List<NegocioUsuarioDTO> listaNegocioMxSv) {
		this.listaNegocioMxSv = listaNegocioMxSv;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxGs() {
		return listaConceptoMxGs;
	}

	public void setListaConceptoMxGs(List<ConceptoUsuarioDTO> listaConceptoMxGs) {
		this.listaConceptoMxGs = listaConceptoMxGs;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxEn() {
		return listaConceptoMxEn;
	}

	public void setListaConceptoMxEn(List<ConceptoUsuarioDTO> listaConceptoMxEn) {
		this.listaConceptoMxEn = listaConceptoMxEn;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxAr() {
		return listaConceptoMxAr;
	}

	public void setListaConceptoMxAr(List<ConceptoUsuarioDTO> listaConceptoMxAr) {
		this.listaConceptoMxAr = listaConceptoMxAr;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxAf() {
		return listaConceptoMxAf;
	}

	public void setListaConceptoMxAf(List<ConceptoUsuarioDTO> listaConceptoMxAf) {
		this.listaConceptoMxAf = listaConceptoMxAf;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxAc() {
		return listaConceptoMxAc;
	}

	public void setListaConceptoMxAc(List<ConceptoUsuarioDTO> listaConceptoMxAc) {
		this.listaConceptoMxAc = listaConceptoMxAc;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxSv() {
		return listaConceptoMxSv;
	}

	public void setListaConceptoMxSv(List<ConceptoUsuarioDTO> listaConceptoMxSv) {
		this.listaConceptoMxSv = listaConceptoMxSv;
	}

	public String getMarcarZonaKxGs() {
		return marcarZonaKxGs;
	}

	public void setMarcarZonaKxGs(String marcarZonaKxGs) {
		this.marcarZonaKxGs = marcarZonaKxGs;
	}

	public String getMarcarSucursalKxGs() {
		return marcarSucursalKxGs;
	}

	public void setMarcarSucursalKxGs(String marcarSucursalKxGs) {
		this.marcarSucursalKxGs = marcarSucursalKxGs;
	}

	public String getMarcarNegocioKxGs() {
		return marcarNegocioKxGs;
	}

	public void setMarcarNegocioKxGs(String marcarNegocioKxGs) {
		this.marcarNegocioKxGs = marcarNegocioKxGs;
	}

	public String getMarcarConceptoKxGs() {
		return marcarConceptoKxGs;
	}

	public void setMarcarConceptoKxGs(String marcarConceptoKxGs) {
		this.marcarConceptoKxGs = marcarConceptoKxGs;
	}

	public boolean isZona() {
		return zona;
	}

	public void setZona(boolean zona) {
		this.zona = zona;
	}

	public boolean isSucursal() {
		return sucursal;
	}

	public void setSucursal(boolean sucursal) {
		this.sucursal = sucursal;
	}

	public boolean isNegocio() {
		return negocio;
	}

	public void setNegocio(boolean negocio) {
		this.negocio = negocio;
	}

	public boolean isConcepto() {
		return concepto;
	}

	public void setConcepto(boolean concepto) {
		this.concepto = concepto;
	}

	public String getMarcarZonaKxEn() {
		return marcarZonaKxEn;
	}

	public void setMarcarZonaKxEn(String marcarZonaKxEn) {
		this.marcarZonaKxEn = marcarZonaKxEn;
	}

	public String getMarcarSucursalKxEn() {
		return marcarSucursalKxEn;
	}

	public void setMarcarSucursalKxEn(String marcarSucursalKxEn) {
		this.marcarSucursalKxEn = marcarSucursalKxEn;
	}

	public String getMarcarNegocioKxEn() {
		return marcarNegocioKxEn;
	}

	public void setMarcarNegocioKxEn(String marcarNegocioKxEn) {
		this.marcarNegocioKxEn = marcarNegocioKxEn;
	}

	public String getMarcarConceptoKxEn() {
		return marcarConceptoKxEn;
	}

	public void setMarcarConceptoKxEn(String marcarConceptoKxEn) {
		this.marcarConceptoKxEn = marcarConceptoKxEn;
	}

	public String getMarcarZonaKxAr() {
		return marcarZonaKxAr;
	}

	public void setMarcarZonaKxAr(String marcarZonaKxAr) {
		this.marcarZonaKxAr = marcarZonaKxAr;
	}

	public String getMarcarSucursalKxAr() {
		return marcarSucursalKxAr;
	}

	public void setMarcarSucursalKxAr(String marcarSucursalKxAr) {
		this.marcarSucursalKxAr = marcarSucursalKxAr;
	}

	public String getMarcarNegocioKxAr() {
		return marcarNegocioKxAr;
	}

	public void setMarcarNegocioKxAr(String marcarNegocioKxAr) {
		this.marcarNegocioKxAr = marcarNegocioKxAr;
	}

	public String getMarcarConceptoKxAr() {
		return marcarConceptoKxAr;
	}

	public void setMarcarConceptoKxAr(String marcarConceptoKxAr) {
		this.marcarConceptoKxAr = marcarConceptoKxAr;
	}

	public String getMarcarZonaKxAf() {
		return marcarZonaKxAf;
	}

	public void setMarcarZonaKxAf(String marcarZonaKxAf) {
		this.marcarZonaKxAf = marcarZonaKxAf;
	}

	public String getMarcarSucursalKxAf() {
		return marcarSucursalKxAf;
	}

	public void setMarcarSucursalKxAf(String marcarSucursalKxAf) {
		this.marcarSucursalKxAf = marcarSucursalKxAf;
	}

	public String getMarcarNegocioKxAf() {
		return marcarNegocioKxAf;
	}

	public void setMarcarNegocioKxAf(String marcarNegocioKxAf) {
		this.marcarNegocioKxAf = marcarNegocioKxAf;
	}

	public String getMarcarConceptoKxAf() {
		return marcarConceptoKxAf;
	}

	public void setMarcarConceptoKxAf(String marcarConceptoKxAf) {
		this.marcarConceptoKxAf = marcarConceptoKxAf;
	}

	public String getMarcarZonaKxAc() {
		return marcarZonaKxAc;
	}

	public void setMarcarZonaKxAc(String marcarZonaKxAc) {
		this.marcarZonaKxAc = marcarZonaKxAc;
	}

	public String getMarcarSucursalKxAc() {
		return marcarSucursalKxAc;
	}

	public void setMarcarSucursalKxAc(String marcarSucursalKxAc) {
		this.marcarSucursalKxAc = marcarSucursalKxAc;
	}

	public String getMarcarNegocioKxAc() {
		return marcarNegocioKxAc;
	}

	public void setMarcarNegocioKxAc(String marcarNegocioKxAc) {
		this.marcarNegocioKxAc = marcarNegocioKxAc;
	}

	public String getMarcarConceptoKxAc() {
		return marcarConceptoKxAc;
	}

	public void setMarcarConceptoKxAc(String marcarConceptoKxAc) {
		this.marcarConceptoKxAc = marcarConceptoKxAc;
	}

	public String getMarcarZonaKxSv() {
		return marcarZonaKxSv;
	}

	public void setMarcarZonaKxSv(String marcarZonaKxSv) {
		this.marcarZonaKxSv = marcarZonaKxSv;
	}

	public String getMarcarSucursalKxSv() {
		return marcarSucursalKxSv;
	}

	public void setMarcarSucursalKxSv(String marcarSucursalKxSv) {
		this.marcarSucursalKxSv = marcarSucursalKxSv;
	}

	public String getMarcarNegocioKxSv() {
		return marcarNegocioKxSv;
	}

	public void setMarcarNegocioKxSv(String marcarNegocioKxSv) {
		this.marcarNegocioKxSv = marcarNegocioKxSv;
	}

	public String getMarcarConceptoKxSv() {
		return marcarConceptoKxSv;
	}

	public void setMarcarConceptoKxSv(String marcarConceptoKxSv) {
		this.marcarConceptoKxSv = marcarConceptoKxSv;
	}

	public String getMarcarZonaGcGs() {
		return marcarZonaGcGs;
	}

	public void setMarcarZonaGcGs(String marcarZonaGcGs) {
		this.marcarZonaGcGs = marcarZonaGcGs;
	}

	public String getMarcarSucursalGcGs() {
		return marcarSucursalGcGs;
	}

	public void setMarcarSucursalGcGs(String marcarSucursalGcGs) {
		this.marcarSucursalGcGs = marcarSucursalGcGs;
	}

	public String getMarcarNegocioGcGs() {
		return marcarNegocioGcGs;
	}

	public void setMarcarNegocioGcGs(String marcarNegocioGcGs) {
		this.marcarNegocioGcGs = marcarNegocioGcGs;
	}

	public String getMarcarConceptoGcGs() {
		return marcarConceptoGcGs;
	}

	public void setMarcarConceptoGcGs(String marcarConceptoGcGs) {
		this.marcarConceptoGcGs = marcarConceptoGcGs;
	}

	public String getMarcarZonaGcEn() {
		return marcarZonaGcEn;
	}

	public void setMarcarZonaGcEn(String marcarZonaGcEn) {
		this.marcarZonaGcEn = marcarZonaGcEn;
	}

	public String getMarcarSucursalGcEn() {
		return marcarSucursalGcEn;
	}

	public void setMarcarSucursalGcEn(String marcarSucursalGcEn) {
		this.marcarSucursalGcEn = marcarSucursalGcEn;
	}

	public String getMarcarNegocioGcEn() {
		return marcarNegocioGcEn;
	}

	public void setMarcarNegocioGcEn(String marcarNegocioGcEn) {
		this.marcarNegocioGcEn = marcarNegocioGcEn;
	}

	public String getMarcarConceptoGcEn() {
		return marcarConceptoGcEn;
	}

	public void setMarcarConceptoGcEn(String marcarConceptoGcEn) {
		this.marcarConceptoGcEn = marcarConceptoGcEn;
	}

	public String getMarcarZonaGcAr() {
		return marcarZonaGcAr;
	}

	public void setMarcarZonaGcAr(String marcarZonaGcAr) {
		this.marcarZonaGcAr = marcarZonaGcAr;
	}

	public String getMarcarSucursalGcAr() {
		return marcarSucursalGcAr;
	}

	public void setMarcarSucursalGcAr(String marcarSucursalGcAr) {
		this.marcarSucursalGcAr = marcarSucursalGcAr;
	}

	public String getMarcarNegocioGcAr() {
		return marcarNegocioGcAr;
	}

	public void setMarcarNegocioGcAr(String marcarNegocioGcAr) {
		this.marcarNegocioGcAr = marcarNegocioGcAr;
	}

	public String getMarcarConceptoGcAr() {
		return marcarConceptoGcAr;
	}

	public void setMarcarConceptoGcAr(String marcarConceptoGcAr) {
		this.marcarConceptoGcAr = marcarConceptoGcAr;
	}

	public String getMarcarZonaGcAf() {
		return marcarZonaGcAf;
	}

	public void setMarcarZonaGcAf(String marcarZonaGcAf) {
		this.marcarZonaGcAf = marcarZonaGcAf;
	}

	public String getMarcarSucursalGcAf() {
		return marcarSucursalGcAf;
	}

	public void setMarcarSucursalGcAf(String marcarSucursalGcAf) {
		this.marcarSucursalGcAf = marcarSucursalGcAf;
	}

	public String getMarcarNegocioGcAf() {
		return marcarNegocioGcAf;
	}

	public void setMarcarNegocioGcAf(String marcarNegocioGcAf) {
		this.marcarNegocioGcAf = marcarNegocioGcAf;
	}

	public String getMarcarConceptoGcAf() {
		return marcarConceptoGcAf;
	}

	public void setMarcarConceptoGcAf(String marcarConceptoGcAf) {
		this.marcarConceptoGcAf = marcarConceptoGcAf;
	}

	public String getMarcarZonaGcAc() {
		return marcarZonaGcAc;
	}

	public void setMarcarZonaGcAc(String marcarZonaGcAc) {
		this.marcarZonaGcAc = marcarZonaGcAc;
	}

	public String getMarcarSucursalGcAc() {
		return marcarSucursalGcAc;
	}

	public void setMarcarSucursalGcAc(String marcarSucursalGcAc) {
		this.marcarSucursalGcAc = marcarSucursalGcAc;
	}

	public String getMarcarNegocioGcAc() {
		return marcarNegocioGcAc;
	}

	public void setMarcarNegocioGcAc(String marcarNegocioGcAc) {
		this.marcarNegocioGcAc = marcarNegocioGcAc;
	}

	public String getMarcarConceptoGcAc() {
		return marcarConceptoGcAc;
	}

	public void setMarcarConceptoGcAc(String marcarConceptoGcAc) {
		this.marcarConceptoGcAc = marcarConceptoGcAc;
	}

	public String getMarcarZonaGcSv() {
		return marcarZonaGcSv;
	}

	public void setMarcarZonaGcSv(String marcarZonaGcSv) {
		this.marcarZonaGcSv = marcarZonaGcSv;
	}

	public String getMarcarSucursalGcSv() {
		return marcarSucursalGcSv;
	}

	public void setMarcarSucursalGcSv(String marcarSucursalGcSv) {
		this.marcarSucursalGcSv = marcarSucursalGcSv;
	}

	public String getMarcarNegocioGcSv() {
		return marcarNegocioGcSv;
	}

	public void setMarcarNegocioGcSv(String marcarNegocioGcSv) {
		this.marcarNegocioGcSv = marcarNegocioGcSv;
	}

	public String getMarcarConceptoGcSv() {
		return marcarConceptoGcSv;
	}

	public void setMarcarConceptoGcSv(String marcarConceptoGcSv) {
		this.marcarConceptoGcSv = marcarConceptoGcSv;
	}

	public String getMarcarZonaMxGs() {
		return marcarZonaMxGs;
	}

	public void setMarcarZonaMxGs(String marcarZonaMxGs) {
		this.marcarZonaMxGs = marcarZonaMxGs;
	}

	public String getMarcarSucursalMxGs() {
		return marcarSucursalMxGs;
	}

	public void setMarcarSucursalMxGs(String marcarSucursalMxGs) {
		this.marcarSucursalMxGs = marcarSucursalMxGs;
	}

	public String getMarcarNegocioMxGs() {
		return marcarNegocioMxGs;
	}

	public void setMarcarNegocioMxGs(String marcarNegocioMxGs) {
		this.marcarNegocioMxGs = marcarNegocioMxGs;
	}

	public String getMarcarConceptoMxGs() {
		return marcarConceptoMxGs;
	}

	public void setMarcarConceptoMxGs(String marcarConceptoMxGs) {
		this.marcarConceptoMxGs = marcarConceptoMxGs;
	}

	public String getMarcarZonaMxEn() {
		return marcarZonaMxEn;
	}

	public void setMarcarZonaMxEn(String marcarZonaMxEn) {
		this.marcarZonaMxEn = marcarZonaMxEn;
	}

	public String getMarcarSucursalMxEn() {
		return marcarSucursalMxEn;
	}

	public void setMarcarSucursalMxEn(String marcarSucursalMxEn) {
		this.marcarSucursalMxEn = marcarSucursalMxEn;
	}

	public String getMarcarNegocioMxEn() {
		return marcarNegocioMxEn;
	}

	public void setMarcarNegocioMxEn(String marcarNegocioMxEn) {
		this.marcarNegocioMxEn = marcarNegocioMxEn;
	}

	public String getMarcarConceptoMxEn() {
		return marcarConceptoMxEn;
	}

	public void setMarcarConceptoMxEn(String marcarConceptoMxEn) {
		this.marcarConceptoMxEn = marcarConceptoMxEn;
	}

	public String getMarcarZonaMxAr() {
		return marcarZonaMxAr;
	}

	public void setMarcarZonaMxAr(String marcarZonaMxAr) {
		this.marcarZonaMxAr = marcarZonaMxAr;
	}

	public String getMarcarSucursalMxAr() {
		return marcarSucursalMxAr;
	}

	public void setMarcarSucursalMxAr(String marcarSucursalMxAr) {
		this.marcarSucursalMxAr = marcarSucursalMxAr;
	}

	public String getMarcarNegocioMxAr() {
		return marcarNegocioMxAr;
	}

	public void setMarcarNegocioMxAr(String marcarNegocioMxAr) {
		this.marcarNegocioMxAr = marcarNegocioMxAr;
	}

	public String getMarcarConceptoMxAr() {
		return marcarConceptoMxAr;
	}

	public void setMarcarConceptoMxAr(String marcarConceptoMxAr) {
		this.marcarConceptoMxAr = marcarConceptoMxAr;
	}

	public String getMarcarZonaMxAf() {
		return marcarZonaMxAf;
	}

	public void setMarcarZonaMxAf(String marcarZonaMxAf) {
		this.marcarZonaMxAf = marcarZonaMxAf;
	}

	public String getMarcarSucursalMxAf() {
		return marcarSucursalMxAf;
	}

	public void setMarcarSucursalMxAf(String marcarSucursalMxAf) {
		this.marcarSucursalMxAf = marcarSucursalMxAf;
	}

	public String getMarcarNegocioMxAf() {
		return marcarNegocioMxAf;
	}

	public void setMarcarNegocioMxAf(String marcarNegocioMxAf) {
		this.marcarNegocioMxAf = marcarNegocioMxAf;
	}

	public String getMarcarConceptoMxAf() {
		return marcarConceptoMxAf;
	}

	public void setMarcarConceptoMxAf(String marcarConceptoMxAf) {
		this.marcarConceptoMxAf = marcarConceptoMxAf;
	}

	public String getMarcarZonaMxAc() {
		return marcarZonaMxAc;
	}

	public void setMarcarZonaMxAc(String marcarZonaMxAc) {
		this.marcarZonaMxAc = marcarZonaMxAc;
	}

	public String getMarcarSucursalMxAc() {
		return marcarSucursalMxAc;
	}

	public void setMarcarSucursalMxAc(String marcarSucursalMxAc) {
		this.marcarSucursalMxAc = marcarSucursalMxAc;
	}

	public String getMarcarNegocioMxAc() {
		return marcarNegocioMxAc;
	}

	public void setMarcarNegocioMxAc(String marcarNegocioMxAc) {
		this.marcarNegocioMxAc = marcarNegocioMxAc;
	}

	public String getMarcarConceptoMxAc() {
		return marcarConceptoMxAc;
	}

	public void setMarcarConceptoMxAc(String marcarConceptoMxAc) {
		this.marcarConceptoMxAc = marcarConceptoMxAc;
	}

	public String getMarcarZonaMxSv() {
		return marcarZonaMxSv;
	}

	public void setMarcarZonaMxSv(String marcarZonaMxSv) {
		this.marcarZonaMxSv = marcarZonaMxSv;
	}

	public String getMarcarSucursalMxSv() {
		return marcarSucursalMxSv;
	}

	public void setMarcarSucursalMxSv(String marcarSucursalMxSv) {
		this.marcarSucursalMxSv = marcarSucursalMxSv;
	}

	public String getMarcarNegocioMxSv() {
		return marcarNegocioMxSv;
	}

	public void setMarcarNegocioMxSv(String marcarNegocioMxSv) {
		this.marcarNegocioMxSv = marcarNegocioMxSv;
	}

	public String getMarcarConceptoMxSv() {
		return marcarConceptoMxSv;
	}

	public void setMarcarConceptoMxSv(String marcarConceptoMxSv) {
		this.marcarConceptoMxSv = marcarConceptoMxSv;
	}

	public List<UsuarioIngresoDTO> getListaUsuariosDefinidos() {
		return listaUsuariosDefinidos;
	}

	public void setListaUsuariosDefinidos(
			List<UsuarioIngresoDTO> listaUsuariosDefinidos) {
		this.listaUsuariosDefinidos = listaUsuariosDefinidos;
	}

	public boolean isAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}

	public boolean isOtrasSolictudes() {
		return otrasSolictudes;
	}

	public void setOtrasSolictudes(boolean otrasSolictudes) {
		this.otrasSolictudes = otrasSolictudes;
	}

	public boolean isnoNotificaInicial() {
		return noNotificaInicial;
	}

	public void setnoNotificaInicial(boolean noNotificaInicial) {
		this.noNotificaInicial = noNotificaInicial;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public List<UsuarioCargo> getListaUsuarioCargos() {
		return listaUsuarioCargos;
	}

	public void setListaUsuarioCargos(List<UsuarioCargo> listaUsuarioCargos) {
		this.listaUsuarioCargos = listaUsuarioCargos;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEliminado() {
		return eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		this.eliminado = eliminado;
	}

	public List<Sociedad> getListaSociedad() {
		return listaSociedad;
	}

	public void setListaSociedad(List<Sociedad> listaSociedad) {
		this.listaSociedad = listaSociedad;
	}

	public Sociedad getSociedadAux() {
		return sociedadAux;
	}

	public void setSociedadAux(Sociedad sociedadAux) {
		this.sociedadAux = sociedadAux;
	}
	
	

}
