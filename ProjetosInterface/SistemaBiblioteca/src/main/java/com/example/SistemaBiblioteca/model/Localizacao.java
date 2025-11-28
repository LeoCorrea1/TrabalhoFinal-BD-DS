package com.example.SistemaBiblioteca.model;

public class Localizacao {
    private Integer idLocalizacao;
    private String setor;
    private String prateleira;
    private String caixa;

    public Integer getIdLocalizacao() { return idLocalizacao; }
    public void setIdLocalizacao(Integer idLocalizacao) { this.idLocalizacao = idLocalizacao; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public String getPrateleira() { return prateleira; }
    public void setPrateleira(String prateleira) { this.prateleira = prateleira; }

    public String getCaixa() { return caixa; }
    public void setCaixa(String caixa) { this.caixa = caixa; }

    @Override
    public String toString() {
        // texto curto útil para ComboBox
        String s = (setor == null ? "" : setor);
        String p = (prateleira == null ? "" : prateleira);
        String c = (caixa == null ? "" : caixa);
        String combined = (s + " / " + p + " / " + c).replaceAll("^\\s*/\\s*|\\s*/\\s*$", "").trim();
        return combined.isBlank() ? "Localização " + (idLocalizacao == null ? "" : idLocalizacao) : combined;
    }
}
