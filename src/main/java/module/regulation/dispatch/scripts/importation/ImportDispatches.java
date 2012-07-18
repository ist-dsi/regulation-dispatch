/*
 * @(#)ImportDispatches.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Anil Kassamali
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Dispatch Registry Module.
 *
 *   The Dispatch Registry Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Dispatch Registry Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Dispatch Registry Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.regulation.dispatch.scripts.importation;

import jvstm.TransactionalCommand;
import module.organization.domain.Person;
import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.regulation.dispatch.domain.activities.CreateRegulationDispatchBean;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.scheduler.CustomTask;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixframework.pstm.Transaction;

/**
 * 
 * @author Anil Kassamali
 * 
 */
public class ImportDispatches extends CustomTask implements TransactionalCommand {

    @Override
    public void run() {
	Transaction.withTransaction(false, this);
	out.println("Done.");
    }

    @Override
    public void doIt() {
	RegulationDispatchQueue queue = readQueue();

	int created = 0;
	int withError = 0;
	boolean throwException = false;

	for (String entry : ENTRIES) {
	    try {
		DispatchEntry dispatchEntry = new DispatchEntry();
		if (dispatchEntry.parse(entry)) {
		    dispatchEntry.createEntry(queue);
		    created++;
		}

	    } catch (Exception e) {
		throwException = true;
		withError++;

		System.out.println(e.getMessage());
	    }
	}

	System.out.println(created + " " + withError);

	// if (throwException) {
	// throw new RuntimeException("abort");
	// }
    }

    private RegulationDispatchQueue readQueue() {
	return RegulationDispatchQueue.fromExternalId("7602092115122");
    }

    private static class DispatchEntry {

	private String reference;
	private LocalDate emissionDate;
	private String subject;
	private Person emissor;

	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd.MM.yyyy");

	public boolean parse(String line) {

	    if (StringUtils.isEmpty(line)) {
		return false;
	    }

	    String[] fields = line.split(";");

	    this.reference = fields[0].trim();
	    this.emissionDate = dateFormatter.parseDateTime(fields[1].trim()).toLocalDate();
	    this.subject = fields[3].trim();

	    this.emissor = readEmissor(fields[4].trim());

	    return true;
	}

	private Person readEmissor(String name) {
	    if (name.indexOf("Rogério Colaço") > -1) {
		return User.findByUsername("ist13267").getPerson();
	    } else if (name.indexOf("Cruz Serra") > -1) {
		return User.findByUsername("ist11791").getPerson();
	    } else if (name.indexOf("Arlindo Oliveira") > -1) {
		return User.findByUsername("ist12282").getPerson();
	    }

	    return null;
	}

	public RegulationDispatchWorkflowMetaProcess createEntry(final RegulationDispatchQueue queue) {
	    User responsible = User.findByUsername("ist149608");
	    CreateRegulationDispatchBean bean = new CreateRegulationDispatchBean(queue);
	    bean.setReference(reference + "/2011");
	    bean.setEmissionDate(emissionDate);
	    bean.setEmissor(emissor);
	    bean.setDispatchDescription(subject);

	    return RegulationDispatchWorkflowMetaProcess.createNewProcess(bean, responsible);
	}
    }

    private final static String[] ENTRIES = new String[] {
	    "1;05.01.2011;;Nomeação do Presidente do DEG - Prof. C. Bana e Costa;Prof. Cruz Serra;",
	    "2;11.01.2011;;Regulamento do Dept. de Engª Civil, Arquitectura e Georrecursos;Prof. Cruz Serra;",
	    "3;11.01.2011;;Regulamento do Dept. de Bioengenharia;Prof. Cruz Serra;",
	    "4;17.01.2011;;Eleição da Comissão Paritária;Prof. Cruz Serra;",
	    "5;17.01.2011;;Nomeação de Membros Mesa Eleitoral p/ Eleição da CP - Alameda;Prof. Cruz Serra;",
	    "6;17.01.2011;;Nomeação de Membros Mesa Eleitoral p/ Eleição da CP - Taguspark;Prof. Cruz Serra;",
	    "7;19.01.2011;;Processo Disciplinar nº 01/PDF/2010 - Aplicação de pena;Prof. Cruz Serra;",
	    "8;20.01.2011;;Nomeação do Presidente do DECivil - Prof.João Azevedo;Prof. Cruz Serra;",
	    "9;25.01.2011;;Anulação de concursos para Professor Associado (Química);Prof. Cruz Serra;",
	    "9a;25.01.2011;;Anulação de concursos para Professor Catedrático (Química);Prof. Cruz Serra;",
	    "10;26.01.2011;;Delegação de competências no Prof. Rogério Colaço;Prof. Cruz Serra;",
	    "11;26.01.2011;;Regulamento de Remunerações Adicionais no Âmbito de Contratos do IST;Prof. Cruz Serra;",
	    "12;26.01.2011;;Nomeação do Presidente do ISR - Prof. Vitor Barroso;Prof. Cruz Serra;",
	    "13;26.01.2011;;Nomeação do Presidente do CQFM - Prof. José Gaspar Martinho;Prof. Cruz Serra;",
	    "14;26.01.2011;;Nomeação do Presidente do CFIF - Prof. Vitor Vieira;Prof. Cruz Serra;",
	    "15;27.01.2011;;Nomeação do Presidente do Dept. de Física - Prof. Alfredo Barbosa Henriques;Prof. Cruz Serra;",
	    "16;27.01.2011;;Nomeação do Presidente do Dept. de Bioengenharia - Prof. J. Sampaio Cabral;Prof. Cruz Serra;",
	    "17;27.01.2011;;Regulamento de Remunerações Adicionais no Âmbito de Contratos do IST;Prof. Cruz Serra;",
	    "18;27.01.2011;;Abertura de Processo de Inquérito - funcionário José Santos;Prof. Cruz Serra;",
	    "19;27.01.2011;;Coordenadora do 1º e 2º ciclos em Engª Gestão Industrial - Profª Ana Póvoa;Prof. Cruz Serra;",
	    "20;27.01.2011;;Coordenadora do Programa de Doutoramento em Engª e Gestão - Prof. C. Bana Costa;Prof. Cruz Serra;",
	    "21;27.01.2011;;Coordenadora do Programa de Doutoramento em Mudança Tecnológica e Empreendedorismo - Prof. Rui Baptista;Prof. Cruz Serra;",
	    "22;28.01.2011;;Coordenador do MEEC - Prof. Leonel Sousa;Prof. Cruz Serra;",
	    "23;28.01.2011;;Coordenador do LEE/MEE - Prof. Carlos Ferreira Fernandes;Prof. Cruz Serra;",
	    "24;28.01.2011;;Coordenador do LERC - Prof. Rui Valadas;Prof. Cruz Serra;",
	    "25;28.01.2011;;Coordenadora do Mestrado Integrado em Engenharia Biológica - Profª Raquel Aires de Barros;Prof. Cruz Serra;",
	    "26;28.01.2011;;Coordenadora do Mestrado Europeu em Biologia de Sistemas - Profª Isabel Sá Correia;Prof. Cruz Serra;",
	    "27;28.01.2011;;Coordenadora do Mestrado em Biotecnologia - Profª Isabel Sá Correia;Prof. Cruz Serra;",
	    "28;28.01.2011;;Coordenador do Mestrado em Bioengenharia e Nanossistemas - Prof. João Pedro Conde;Prof. Cruz Serra;",
	    "29;28.01.2011;;Coordenadora do Doutoramento em Biotecnologia - Profª Isabel Sá Correia;Prof. Cruz Serra;",
	    "30;28.01.2011;;Coordenador do Doutoramento em Bioengenharia - Prof. J. Sampaio Cabral;Prof. Cruz Serra;",
	    "31;28.01.2011;;Coordenadora do Mestrado Integrado em Engenharia Física Tecnológica - Profª Ana Mourão;Prof. Cruz Serra;",
	    "32;28.01.2011;;Coordenador do Programa Doutoral em Física - Prof. Jorge Loureiro;Prof. Cruz Serra;",
	    "33;28.01.2011;;Coordenador do Programa Doutoral em Engenharia Física Tecnológica - Prof. Jorge Loureiro;Prof. Cruz Serra;",
	    "34;01.02.2011;;Nomeação do Presidente do Dept. de Engª Química - Prof. Francisco Lemos;Prof. Cruz Serra;",
	    "35;04.02.2011;;Despacho - ALO;Prof. Cruz Serra;",
	    "36;04.02.2011;;Despacho - ALO;;",
	    "37;07.02.2011;;Despacho de arquivamento de processo disciplinar- Tiago Anjos;Prof. Rogério Colaço;",
	    "38;07.02.2011;;Despacho de arquivamento de processo disciplinar - Aluno Pompeu Santos;Prof. Rogério Colaço;",
	    "39;07.02.2011;;Despacho de arquivamento de processo disciplinar - Aluno Alcínio Tavares;Prof. Rogério Colaço;",
	    "40;07.02.2011;;Despacho de arquivamento de processo disciplinar - Aluno Gabriel Nunes;Prof. Rogério Colaço;",
	    "41;07.02.2011;;Abertura de Processo de Inquérito relativo ao desaparecimento de equipamento informático;Prof. Cruz Serra;",
	    "42;07.02.2011;;Arquivamento do Processo de inquérito n.º 04/PIA/2010;Prof. Cruz Serra;",
	    "43;07.02.2011;;Nomeação do Presidente do Dept. de Engª Mecânica - Prof. Manuel Seabra Pereira;Prof. Cruz Serra;",
	    "44;07.02.2011;;Nomeação do Presidente do CEHIDRO - Prof. Francisco Nunes Correia;Prof. Cruz Serra;",
	    "45;11.02.2011;;Nomeação do Presidente do CAMGSD - Prof. Carlos Rocha;Prof. Cruz Serra;",
	    "46;11.02.2011;;Nomeação do Presidente do CERENA - Profª Maria Teresa Carvalho;Prof. Cruz Serra;",
	    "47;11.02.2011;;Nomeação do Presidente do IPFN - Prof. Carlos Varandas;Prof. Cruz Serra;",
	    "48;15.02.2011;;Abertura de Processo de Inquérito a situação de alegado furto de enunciados de exame;Prof. Cruz Serra;",
	    "49;17.02.2011;;Regulamento do Apoio ao estudante c necessidades educativas especiais;Prof. Cruz Serra;",
	    "50;25.02.2011;;Nomeação do Presidente do CESUR - Prof. João Levy;Prof. Cruz Serra;",
	    "51;25.02.2011;;Regulamento de deslocação ao estrangeiro do IST;Prof. Cruz Serra;",
	    "52;28.02.2011;;Nomeação da Comissão Paritária para o biénio 2011-2012;Prof. Cruz Serra;",
	    "53;28.02.2011;;Revogação do Despacho referente ao Processo Disciplinar nº 08/PDF/2010;Prof. Cruz Serra;",
	    "54;14.03.2011;;Regulamento de ingresso no primeiro ciclo do IST;Prof. Cruz Serra;",
	    "55;14.03.2011;;Regulamento de ingresso no segundo ciclo do IST;Prof. Cruz Serra;",
	    "56;14.03.2011;;Regulamento de Matrículas e Inscrições do IST;Prof. Cruz Serra;",
	    "57;18.03.2011;;Regulamento geral de doutoramentos do IST;Prof. Cruz Serra;",
	    "58;18.03.2011;;Regulamento geral dos diplomas do IST;Prof. Cruz Serra;",
	    "59;18.03.2011;;Nomeação do Presidente do CIE3 - Prof. José Santanta;Prof. Cruz Serra;",
	    "60;29.03.2011;;Republicação do Regulamento Geral de Doutoramentos;Prof. Cruz Serra;",
	    "61;31.03.2011;;Nomeação do Presidente do CEPGIST - Prof. José Manuel Marques;Prof. Cruz Serra;",
	    "62;05.04.2011;;Nomeação do Presidente do CEG-IST - Prof. Paulo Correia;Prof. Cruz Serra;",
	    "63;05.04.2011;;Rectificação do Despacho de nomeação do Coordenador da LERC - Prof. Rui Valadas;Prof. Cruz Serra;",
	    "64;06.04.2011;;Data limite para conclusão da Dissertação de Doutoramento;Prof. Rogério Colaço;",
	    "65;06.04.2011;;Nomeação do Coordenador do Mestrado Integrado em Engª Química;Prof. Cruz Serra;",
	    "66;06.04.2011;;Nomeação do Coordenador do Mestrado em Engenharia Farmacêutica;Prof. Cruz Serra;",
	    "67;08.04.2011;;Despacho de Arquivamento do Processo de Inquérito n.º 02/PIF/2011;Prof. Cruz Serra;",
	    "68;14.04.2011;;Aprovação da alteração plano curricular MEE;Prof. Rogério Colaço;",
	    "69;18.04.2011;;Cessação da relação contratual com o Professor Manuel Filipe Ventura;Prof. Cruz Serra;",
	    "69-0;21.04.2011;;Nomeação do Coordenador do Mestrado Integrado em Engª Aeroespacial - Prof. Luís Braga Campos;Prof. Cruz Serra;",
	    "70;27.04.2011;;Calendário de prazos académicos 2011/2012;Prof. Cruz Serra;",
	    "71;27.04.2011;;Calendário Escolar Integrado 2011/2012;Prof. Cruz Serra;",
	    "72;28.04.2011;;Nomeação do Coordenador do Mestrado Integrado em Eng. Mecânica;Prof. Cruz Serra;",
	    "73;28.04.2011;;Nomeação do Coordenador do Doutoramento em Eng. Mecânica;Prof. Cruz Serra;",
	    "74;28.04.2011;;Nomeação do Coordenador do Doutoramento em Líderes para a Industria Tecnológica (MIT-EDAM/LTI);Prof. Cruz Serra;",
	    "75;28.04.2011;;Nomeação do Coordenador do Doutoramento em Eng. Computacional (Texas/Austin);Prof. Cruz Serra;",
	    "76;28.04.2011;;Nomeação do Coordenador do Doutoramento em Sistemas Sustentáveis de Energia (MIT);Prof. Cruz Serra;",
	    "77;28.04.2011;;Nomeação do Coordenador do Doutoramento em Eng. Aerospacial;Prof. Cruz Serra;",
	    "78;28.04.2011;;Nomeação do Coordenador da Licenciatura em Eng. e Arquitectura Naval;Prof. Cruz Serra;",
	    "79;28.04.2011;;Nomeação do Coordenador do Mestrado em Eng. e Arquitectura Naval e Doutoramento em Eng. Naval;Prof. Cruz Serra;",
	    "80;28.04.2011;;Nomeação do Coordenador do Doutoramento em Eng. Naval;Prof. Cruz Serra;",
	    "81;02.05.2011;;Nomeação do Coordenador do Mestrado Integrado em Eng. Biomédica;Prof. Cruz Serra;",
	    "82;02.05.2011;;Nomeação do Coordenador do Doutoramento em Eng. Biomédica;Prof. Cruz Serra;",
	    "83;02.05.2011;;Nomeação do Coordenador do Mestrado Integrado em Eng. Civil;Prof. Cruz Serra;",
	    "84;02.05.2011;;Nomeação do Coordenador da Licenciatura em Eng. Geológica e Minas;Prof. Cruz Serra;",
	    "85;02.05.2011;;Nomeação do Coordenador do Mestrado em Eng. Geológica e de Minas;Prof. Cruz Serra;",
	    "86;02.05.2011;;Nomeação do Coordenador da Licenciatura em Eng. do Território;Prof. Cruz Serra;",
	    "87;02.05.2011;;Nomeação do Coordenador do Mestrado em Eng. do Território;Prof. Cruz Serra;",
	    "88;02.05.2011;;Nomeação do Coordenador do Doutoramento em Sistemas de Transportes;Prof. Cruz Serra;",
	    "89;02.05.2011;;Nomeação do Coordenador do Doutoramento em Arquitectura;Prof. Cruz Serra;",
	    "90;02.05.2011;;Nomeação do Coordenador do Doutoramento em Georrecursos;Prof. Cruz Serra;",
	    "91;02.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Bruno M. Simões Carvalho;Prof. Cruz Serra;",
	    "92;03.05.2011;;Nomeação do Coordenador do Centro Multidisciplinar de Astrofísica (CENTRA) - Prof. José Sande Lemos;Prof. Cruz Serra;",
	    "93;04.04.2011;;Plataforma de Ciências e Engenharia do Ambiente e a Iniciativa em Energia;Prof. Cruz Serra;",
	    "94;05.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Luís Cardoso;Prof. Cruz Serra;",
	    "95;05.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Paulo Duarte;Prof. Cruz Serra;",
	    "96;06.05.2011;;Nomeação Coordenador do Centro de Ciências e Tecnologias Aeronáuticas e Espaciais;Prof. Cruz Serra;",
	    "97;06.05.2011;;Relatório Final - Aplicação de pena à funcionária Susana Martins;Prof. Cruz Serra;",
	    "98;06.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Valentino Anok;Prof. Cruz Serra;",
	    "99;06.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Manuel João Rodrigues;Prof. Cruz Serra;",
	    "100;06.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Camilo Christo;Prof. Cruz Serra;",
	    "101;09.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Isabel Nogueira;Prof. Cruz Serra;",
	    "102;10.05.2011;;Avaliação do período experimental - Alteração de júri - Carlos Gonçalves;Prof. Cruz Serra;",
	    "103;10.05.2011;;Nomeação do Coordenador do Doutoramento em Engenharia do Ambiente;Prof. Cruz Serra;",
	    "104;10.05.2011;;Nomeação do Coordenador da LicEngMateriais;Prof. Cruz Serra;",
	    "105;10.05.2011;;Nomeação do Coordenador do MestEngMateriais;Prof. Cruz Serra;",
	    "106;10.05.2011;;Nomeação do Coordenador do Doutoramento em Engenharia de Materiais;Prof. Cruz Serra;",
	    "107;12.05.2011;;Renovação da Comissão - Dr. José Manuel Riscado;Prof. Cruz Serra;",
	    "108;12.05.2011;;Renovação da Comissão - Ana Paula Silva;Prof. Cruz Serra;",
	    "109;12.05.2011;;Despacho de aplicação de uma pena de suspensão - Processo Disciplinar UTL-SAJ71/2010;Prof. Cruz Serra;",
	    "110;16.05.2011;;Despacho sobre pedido de equivalências, aluno Daniel de Almeida;Prof. Rogério Colaço;",
	    "111;18.05.2011;;Correcção - Coordenação do Mestrado em Engenharia Farmaceutica;Prof. Cruz Serra;",
	    "112;20.05.2011;;Cessação de Licença sem vencimento - Prof. José Epifânio da Franca;Prof. Cruz Serra;",
	    "113;20.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Ana Lúcia Barbosa;Prof. Cruz Serra;",
	    "114;24.05.2011;;Avaliação do período experimental - Contrato de Trabalho - João Algarvio Costa;Prof. Cruz Serra;",
	    "115;24.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Ana Rosa;Prof. Cruz Serra;",
	    "116;24.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Alda Freitas;Prof. Cruz Serra;",
	    "117;24.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Alexandra Cardoso;Prof. Cruz Serra;",
	    "118;24.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Luís Silveiro;Prof. Cruz Serra;",
	    "119;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Tânia Beleza;Prof. Cruz Serra;",
	    "120;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Elsa Silva;Prof. Cruz Serra;",
	    "121;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - João Alfaiate;Prof. Cruz Serra;",
	    "122;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - João Fernandes;Prof. Cruz Serra;",
	    "123;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Jorge Ferreira;Prof. Cruz Serra;",
	    "124;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - João Patrício;Prof. Cruz Serra;",
	    "125;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Alexandre Júlio;Prof. Cruz Serra;",
	    "126;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - José Cheng;Prof. Cruz Serra;",
	    "127;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Ana Silva;Prof. Cruz Serra;",
	    "128;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Sónia Sousa;Prof. Cruz Serra;",
	    "129;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Vera Gouveia;Prof. Cruz Serra;",
	    "130;30.05.2011;;Avaliação do período experimental - Contrato de Trabalho - Ricardo;Prof. Cruz Serra;",
	    "131;31.05.2011;;Despacho sobre o curso de Especialização  \"Materiais em Engenharia-  Gestão Integrada da Qualidade, Ambiente e Segurança\";Prof. Rogério Colaço;",
	    "132;31.05.2011;;Nomeação do Coordenador do Mestrado em Engª Farmacêutica p/ 2011 - Prof. João Carlos Bordado;Prof. Cruz Serra;",
	    "133;01.06.2011;;Nomeação de Coordenadora do Núcleo de Pós-Graducação - Júlia Oliveira;Prof. Cruz Serra;",
	    "134;03.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Rita Wahl;Prof. Cruz Serra;",
	    "135;03.06.2011;;Abertura de Processo de Inquérito - Situação ocorrida no Pavilhão Física;Prof. Cruz Serra;",
	    "136;08.06.2011;;Avaliação do período experimental - Contrato de Trabalho - João Pargana;Prof. Cruz Serra;",
	    "137;08.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Ana Pipio;Prof. Cruz Serra;",
	    "138;08.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Daniel Almeida;Prof. Cruz Serra;",
	    "139;08.06.2011;;Nomeação do Coordenador do Doutoramento em Química;Prof. Cruz Serra;",
	    "140;08.06.2011;;Nomeação do Coordenador do Mestrado em Química;Prof. Cruz Serra;",
	    "141;08.06.2011;;Nomeação do Coordenador do Doutoramento em Engenharia Química;Prof. Cruz Serra;",
	    "142;08.06.2011;;Nomeação do Coordenador do Prog. Doutoral em Sistemas Sustentáveis de Energia (KIC);Prof. Cruz Serra;",
	    "143;08.06.2011;;Nomeação do Coordenador do Prog. de Mestrado de Energia (KIC);Prof. Cruz Serra;",
	    "144;08.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Ana Fernandes;Prof. Cruz Serra;",
	    "145;08.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Margarida Coimbra;Prof. Cruz Serra;",
	    "146;14.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Maria João Piñeiro ;Prof. Cruz Serra;",
	    "147;14.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Patrícia Lima;Prof. Cruz Serra;",
	    "148;14.06.2011;;Nomeação de Coordenadora do Núcleo de Microinformática - Eng. Jorge Prates;Prof. Cruz Serra;",
	    "149;15.06.2011;;Abertura de Processo de Inquérito - Situação de agressão ocorrida no Instituto Superior Técnico/Taguspark;Prof. Cruz Serra;",
	    "150;16.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Ana Agante Lucas;Prof. Cruz Serra;",
	    "151;22.06.2011;;Avaliação do período experimental - Contrato de Trabalho - José Calhariz;Prof. Cruz Serra;",
	    "152;22.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Teresa Afonso;Prof. Cruz Serra;",
	    "153;22.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Joanne Laranjeiro;Prof. Cruz Serra;",
	    "154;28.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Susana Fernandes;Prof. Cruz Serra;ANULADO (pq é igual ao 151 já assinado)",
	    "155;28.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Diogo Simões;Prof. Cruz Serra;",
	    "156;28.06.2011;;Avaliação do período experimental - Contrato de Trabalho - Pedro Santos;Prof. Cruz Serra;",
	    "115-6;30.06.2011;;Cessação de funções - Dr. Nuno Rolo;Prof. Cruz Serra;",
	    "157;01.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Maria Conceição Nogueira;Prof. Cruz Serra;",
	    "158;01.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Maria Vieira Raposo;Prof. Cruz Serra;",
	    "159;01.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Elisabete Pino;Prof. Cruz Serra;",
	    "160;01.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Afonso Franca;Prof. Cruz Serra;",
	    "161;01.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Onésimo Silva;Prof. Cruz Serra;",
	    "162;01.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Rita Costa;Prof. Cruz Serra;",
	    "163;01.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Marta Graça;Prof. Cruz Serra;",
	    "164;01.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Rui Mendes;Prof. Cruz Serra;",
	    "165;01.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Ana Torres;Prof. Cruz Serra;",
	    "166;01.07.2011;;Nomeação do Coordenador da Direcção de Recursos Humanos - Dr. Miguel Coimbra;Prof. Cruz Serra;",
	    "167;07.07.2011;;Nomeação do Presidente do Dep Eng. Mecânica;Prof. Cruz Serra;",
	    "168;07.07.2011;;Nomeação do Coordenador da Plataforma IST-Ambiente - Prof. José Saldanha Matos;Prof. Cruz Serra;",
	    "169;13.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Claudio Martins;Prof. Cruz Serra;",
	    "170;13.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Anil ;Prof. Cruz Serra;",
	    "171;18.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Carlos Gonçalves;Prof. Cruz Serra;",
	    "172;18.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Magda Varanda;Prof. Cruz Serra;",
	    "173;18.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Claudia ;Prof. Cruz Serra;",
	    "174;19.07.2011;;Nomeação do Presidente do Dep Eng. Mecânica - Prof. Helder Rodrigues;Prof. Cruz Serra;",
	    "175;20.07.2011;;Plataforma de Nanotecnologias e Engenharia de Materiais;Prof. Cruz Serra;",
	    "176;20.07.2011;;Avaliação do período experimental - Contrato de Trabalho - José H. Castello Branco;Prof. Cruz Serra;",
	    "177;26.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Carla Tristão;Prof. Cruz Serra;",
	    "178;26.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Ana Sofia Caetano;Prof. Cruz Serra;",
	    "179;27.07.2011;;Alteração ao DESPACHO Nº 122/CG/2010;Prof. Rogério Colaço;",
	    "180;27.07.2011;;Avaliação do período experimental - Contrato de Trabalho - Dulce Cunha;Prof. Cruz Serra;",
	    "181;27.07.2011;;Relatório final do processo de inquérito n.º 01/PIF/2011;Prof. Cruz Serra;",
	    "182;01.08.2011;;Delegação de competências - Revogação do Despacho n.º 10286/2010;Prof. Cruz Serra;",
	    "183;02.08.2011;;Avaliação do período experimental - Contrato de Trabalho - Manuela Morais;Prof. Arlindo Oliveira;",
	    "184;05.08.2011;;Despacho de abertura de processo disciplinar - Margarida Gregório;Prof. Arlindo Oliveira;",
	    "185;09.08.2011;;Despacho - Junta Médica - Margarida Gregório;Prof. Arlindo Oliveira;",
	    "186;11.08.2011;;Alteração ao Regulamento de Compras do IST;Prof. Cruz Serra;",
	    "187;07.09.2011;;Avaliação do período experimental - Contrato de Trabalho - Joana Cruz;Prof. Cruz Serra;",
	    "188;07.09.2011;;Avaliação do período experimental - Contrato de Trabalho - Ana Isabel Rapado;Prof. Cruz Serra;",
	    "189;08.09.2011;;Declaração de rectificação - Presidente do CEG;Prof. Cruz Serra;",
	    "190;19.09.2011;;Declaração de rectificação - diversos;Prof. Cruz Serra;",
	    "191;21.09.2011;;Suspensão de prémios escolares aos alunos que exercem cargos de gestão;Prof. Cruz Serra;",
	    "192;21.09.2011;;Nomeação do Coordenador do Mestrado em Construção e Reabilitação - Prof. João Ramôa Ribeiro Correia;Prof. Cruz Serra;",
	    "193;21.09.2011;;Nomeação do Coordenador do Mestrado Integrado em Engenharia Mecânica - Prof. Mário Costa;Prof. Cruz Serra;",
	    "194;21.09.2011;;Nomeação do Coordenador da Licenciatura em Engª e Arquitectura Naval - Prof. Yordan Garbatov;Prof. Cruz Serra;",
	    "195;21.09.2011;;Nomeação do Coordenador do Prog. Doutoral em Segurança de Informação - Prof. Diogo Gomes;Prof. Cruz Serra;",
	    "196;30.09.2011;;Relatório final do Processo de Inquérito n.º 01/PIA/2011;Prof. Cruz Serra;",
	    "197;11.10.2011;;Nomeação do Coordenador da Plataforma em Nanotecnologias - Prof. Paulo Freitas;Prof. Cruz Serra;",
	    "198;17.10.2011;;Despacho aluno Tote da Silva;Prof. Rogério Colaço;",
	    "199;24.10.2011;;Despacho Conjunto C/ CC;Prof. Rogério Colaço Profª Teresa Duarte;",
	    "200;25.10.2011;;Despacho de renovação da Comissão de Serviço da Drª Salomé Louro;Prof. Cruz Serra;",
	    "201;31.10.2011;;Despacho aluno Tiago Marcelino;Prof.Rogério Colaço;",
	    "202;31.10.2011;;Alteração do DEBQ para Dept. de Engenharia Química;Prof. Cruz Serra;",
	    "203;07.11.2011;;Avaliação do período experimental - Contrato de Trabalho - Susana Varela;Prof. Cruz Serra;",
	    "204;07.11.2011;;Avaliação do período experimental - Contrato de Trabalho - Sara Neves;Prof. Cruz Serra;",
	    "205;07.11.2011;;Avaliação do período experimental - Contrato de Trabalho - Paula Antunes;Prof. Cruz Serra;",
	    "206;07.11.2011;;Avaliação do período experimental - Contrato de Trabalho - Mauro Henriques;Prof. Cruz Serra;",
	    "207;07.11.2011;;Avaliação do período experimental - Contrato de Trabalho - Carla Martins Pereira;Prof. Cruz Serra;",
	    "208;07.11.2011;;Avaliação do período experimental - Contrato de Trabalho - Maria do Carmo Santana;Prof. Cruz Serra;",
	    "209;07.11.2011;;Despacho nomeação de Instrutor Pro. Inquérito - Agressão a aluno;Prof. Cruz Serra;",
	    "210;07.11.2011;;Despacho Nomeação do Presidente do CEG-IST - Prof. Rui Baptista;Prof. Cruz Serra;",
	    "211;10.11.2011;;Despacho nomeação de Instrutor Pro. Inquérito - Agressão a aluno;Prof. Cruz Serra;",
	    "212;17.11.2011;;Despacho nomeação de Instrutor Pro. Inquérito - Sequência a uma auditoria a processos de aquisição;Prof. Cruz Serra;" };

}
