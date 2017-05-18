package com.mycompany.maven.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Manoel
 */
@Entity
@Table(name = "imovel")
public class Imovel implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "logradouro", length = 255, nullable = false)
    private String logradouro;

    @Column(name = "numero", length = 5, nullable = true)
    private String numero;

    @Column(name = "cep", length = 9, nullable = true)
    private String cep;

    @Column(name = "frente_terreno", length = 10, nullable = true)
    private String frenteTerreno;

    @Column(name = "lado_esquerdo_terreno", length = 10, nullable = true)
    private String ladoEsquerdoTerreno;

    @Column(name = "lado_direito_terreno", length = 10, nullable = true)
    private String ladoDireitoTerreno;

    @Column(name = "fundo_terreno", length = 10, nullable = true)
    private String fundoTerreno;

    @Column(name = "area_terreno", length = 10, nullable = true)
    private String areaTerreno;

    @Column(name = "quantidade_edificacoes", length = 10, nullable = true)
    private String quantidadeEdificacoes;

    @Column(name = "area_edificada", length = 10, nullable = true)
    private String area_edificada;

    @Column(name = "quantidade_pavimentos", length = 10, nullable = true)
    private String quantidadePavimentos;

    @NotNull
    @Column(name = "tem_esgoto", nullable = false)
    private Boolean temEsgoto;

    @NotNull
    @Column(name = "tem_energia_eletrica", nullable = false)
    private Boolean temEnergiaEletrica;

    @NotNull
    @Column(name = "tem_iluminacao_publica", nullable = false)
    private Boolean temIluminacaoPublica;

    @NotNull
    @Column(name = "tem_calcamento", nullable = false)
    private Boolean temCalcamento;

    @NotNull
    @Column(name = "tipo_posse", nullable = false)
    private String tipoPosse;

    @NotNull
    @Column(name = "tipo_construcao", nullable = false)
    private String tipoConstrucao;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getFrenteTerreno() {
        return frenteTerreno;
    }

    public void setFrenteTerreno(String frenteTerreno) {
        this.frenteTerreno = frenteTerreno;
    }

    public String getLadoEsquerdoTerreno() {
        return ladoEsquerdoTerreno;
    }

    public void setLadoEsquerdoTerreno(String ladoEsquerdoTerreno) {
        this.ladoEsquerdoTerreno = ladoEsquerdoTerreno;
    }

    public String getLadoDireitoTerreno() {
        return ladoDireitoTerreno;
    }

    public void setLadoDireitoTerreno(String ladoDireitoTerreno) {
        this.ladoDireitoTerreno = ladoDireitoTerreno;
    }

    public String getFundoTerreno() {
        return fundoTerreno;
    }

    public void setFundoTerreno(String fundoTerreno) {
        this.fundoTerreno = fundoTerreno;
    }

    public String getAreaTerreno() {
        return areaTerreno;
    }

    public void setAreaTerreno(String areaTerreno) {
        this.areaTerreno = areaTerreno;
    }

    public String getQuantidadeEdificacoes() {
        return quantidadeEdificacoes;
    }

    public void setQuantidadeEdificacoes(String quantidadeEdificacoes) {
        this.quantidadeEdificacoes = quantidadeEdificacoes;
    }

    public String getArea_edificada() {
        return area_edificada;
    }

    public void setArea_edificada(String area_edificada) {
        this.area_edificada = area_edificada;
    }

    public String getQuantidadePavimentos() {
        return quantidadePavimentos;
    }

    public void setQuantidadePavimentos(String quantidadePavimentos) {
        this.quantidadePavimentos = quantidadePavimentos;
    }

    public Boolean getTemEsgoto() {
        return temEsgoto;
    }

    public void setTemEsgoto(Boolean temEsgoto) {
        this.temEsgoto = temEsgoto;
    }

    public Boolean getTemEnergiaEletrica() {
        return temEnergiaEletrica;
    }

    public void setTemEnergiaEletrica(Boolean temEnergiaEletrica) {
        this.temEnergiaEletrica = temEnergiaEletrica;
    }

    public Boolean getTemIluminacaoPublica() {
        return temIluminacaoPublica;
    }

    public void setTemIluminacaoPublica(Boolean temIluminacaoPublica) {
        this.temIluminacaoPublica = temIluminacaoPublica;
    }

    public Boolean getTemCalcamento() {
        return temCalcamento;
    }

    public void setTemCalcamento(Boolean temCalcamento) {
        this.temCalcamento = temCalcamento;
    }

    public String getTipoPosse() {
        return tipoPosse;
    }

    public void setTipoPosse(String tipoPosse) {
        this.tipoPosse = tipoPosse;
    }

    public String getTipoConstrucao() {
        return tipoConstrucao;
    }

    public void setTipoConstrucao(String tipoConstrucao) {
        this.tipoConstrucao = tipoConstrucao;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Imovel other = (Imovel) obj;
        return !(!Objects.equals(this.id, other.id) && (this.id == null || !this.id.equals(other.id)));
    }

}
