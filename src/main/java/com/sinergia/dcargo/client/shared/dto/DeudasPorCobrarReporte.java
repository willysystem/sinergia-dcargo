package com.sinergia.dcargo.client.shared.dto;

import java.io.Serializable;
import java.util.List;

public class DeudasPorCobrarReporte implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<DeudasReporte> deudasReporte;
	
	private String montoTotalDeudas;
	
	private String montoTotalAcuenta;

	public List<DeudasReporte> getDeudasReporte() {
		return deudasReporte;
	}

	public void setDeudasReporte(List<DeudasReporte> deudasReporte) {
		this.deudasReporte = deudasReporte;
	}

	public String getMontoTotalDeudas() {
		return montoTotalDeudas;
	}

	public void setMontoTotalDeudas(String montoTotalDeudas) {
		this.montoTotalDeudas = montoTotalDeudas;
	}

	public String getMontoTotalAcuenta() {
		return montoTotalAcuenta;
	}

	public void setMontoTotalAcuenta(String montoTotalAcuenta) {
		this.montoTotalAcuenta = montoTotalAcuenta;
	}

	
	
}
