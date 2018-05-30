/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heuristicas;

import gerais.Aplicativo;
import gerais.MPSoC;
import gerais.Tarefa;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lucas
 */
public class FF implements Heuristica {

    private MPSoC mpsoc;
    private ArrayList<Aplicativo> listaAplicativos;
    private List<Tarefa> listaTarefas;
    private int contadorLinha = 0;
    private int contadorColuna = 0;
    private int contadorAplicativo = 0;
    private int contadorTarefa = 0;

    public FF(MPSoC mpsoc, ArrayList<Aplicativo> listaAplicativos) {
        this.mpsoc = mpsoc;
        this.listaAplicativos = listaAplicativos;

    }

    public void executar() {

        listaTarefas = listaAplicativos.get(contadorAplicativo).getTarefas();

        Tarefa tarefa = listaTarefas.get(contadorTarefa);

        if (mpsoc.getCelulas()[contadorLinha][contadorColuna].getTarefa() == null) {
            mpsoc.getCelulas()[contadorLinha][contadorColuna].setTarefa(tarefa);
            mpsoc.getCelulas()[contadorLinha][contadorColuna].updateCor();
            mpsoc.getCelulas()[contadorLinha][contadorColuna].updateLabel();

            if (mpsoc.getCelulas()[contadorLinha][contadorColuna].getTarefa().getNumero() != 0) {
                
                int mestra = mpsoc.getCelulas()[contadorLinha][contadorColuna].getTarefa().getMestra();
                Aplicativo aplicativo = mpsoc.getCelulas()[contadorLinha][contadorColuna].getTarefa().getAplicativo();
                
                int[] posicaoMestra = mpsoc.getIndexTarefa(new Tarefa(mestra,aplicativo));
                int[] posicaoEscrava = mpsoc.getIndexTarefa(mpsoc.getCelulas()[contadorLinha][contadorColuna].getTarefa());
                
                mpsoc.preencherCanais(posicaoEscrava, posicaoMestra);
                
            }

        } else {

        }

        contadorColuna++;
        
        if (contadorColuna == mpsoc.getNColunas()) {
            contadorColuna = 0;
            contadorLinha++;
        } 

        contadorTarefa++;
        
        if (contadorTarefa == listaTarefas.size()) {
            contadorAplicativo++;
            contadorTarefa = 0;
        } 

    }

}
