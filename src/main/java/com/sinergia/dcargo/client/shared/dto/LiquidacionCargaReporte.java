package com.sinergia.dcargo.client.shared.dto;

import java.io.Serializable;
import java.util.List;

public class LiquidacionCargaReporte implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<LiquidacionReporte> liquidacionesReporte;
	
	private String nroTotalConocimientos;
	
	private String nroTotalGuia;
	
	private String totalCobroOrigen;
	
	private String totalCobroDestino;
	
	private String totalFleteDestino;
	
	private String deduccionFletesDestino;
	
	private String deduccionTotal;

	public List<LiquidacionReporte> getLiquidacionesReporte() {
		return liquidacionesReporte;
	}

	public void setLiquidacionesReporte(List<LiquidacionReporte> liquidacionesReporte) {
		this.liquidacionesReporte = liquidacionesReporte;
	}

	public String getNroTotalConocimientos() {
		return nroTotalConocimientos;
	}

	public void setNroTotalConocimientos(String nroTotalConocimientos) {
		this.nroTotalConocimientos = nroTotalConocimientos;
	}

	public String getNroTotalGuia() {
		return nroTotalGuia;
	}

	public void setNroTotalGuia(String nroTotalGuia) {
		this.nroTotalGuia = nroTotalGuia;
	}

	public String getTotalCobroOrigen() {
		return totalCobroOrigen;
	}

	public void setTotalCobroOrigen(String totalCobroOrigen) {
		this.totalCobroOrigen = totalCobroOrigen;
	}

	public String getTotalCobroDestino() {
		return totalCobroDestino;
	}

	public void setTotalCobroDestino(String totalCobroDestino) {
		this.totalCobroDestino = totalCobroDestino;
	}

	public String getTotalFleteDestino() {
		return totalFleteDestino;
	}

	public void setTotalFleteDestino(String totalFleteDestino) {
		this.totalFleteDestino = totalFleteDestino;
	}

	public String getDeduccionFletesDestino() {
		return deduccionFletesDestino;
	}

	public void setDeduccionFletesDestino(String deduccionFletesDestino) {
		this.deduccionFletesDestino = deduccionFletesDestino;
	}

	public String getDeduccionTotal() {
		return deduccionTotal;
	}

	public void setDeduccionTotal(String deduccionTotal) {
		this.deduccionTotal = deduccionTotal;
	}

	@Override
	public String toString() {
		return "LiquidacionCargaReporte [liquidacionesReporte=" + liquidacionesReporte + ", nroTotalConocimientos="
				+ nroTotalConocimientos + ", nroTotalGuia=" + nroTotalGuia + ", totalCobroOrigen=" + totalCobroOrigen
				+ ", totalCobroDestino=" + totalCobroDestino + ", totalFleteDestino=" + totalFleteDestino
				+ ", deduccionFletesDestino=" + deduccionFletesDestino + ", deduccionTotal=" + deduccionTotal + "]";
	}
	
}
