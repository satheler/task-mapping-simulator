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
public class NN implements Heuristica {

    private MPSoC mpsoc;
    private ArrayList<Aplicativo> listaAplicativos;
    private List<Tarefa> listaTarefas;
    private int contadorAplicativo = 0;
    private int contadorTarefa = 0;

    public NN(MPSoC mpsoc, ArrayList<Aplicativo> listaAplicativos) {
        this.mpsoc = mpsoc;
        this.listaAplicativos = listaAplicativos;

    }

    @Override
    public void executar() {

        int incremento = 1;

        listaTarefas = listaAplicativos.get(contadorAplicativo).getTarefas();

        Tarefa tarefa = listaTarefas.get(contadorTarefa);

        if (contadorAplicativo == 0 && contadorTarefa == 0) {
            mpsoc.getCelulas()[0][0].setTarefa(tarefa);
            mpsoc.getCelulas()[0][0].updateCor();
            mpsoc.getCelulas()[0][0].updateLabel();
            contadorTarefa++;
        } else {

            if (tarefa.getNumero() != 0) {

                int[] posicaoMestra = mpsoc.getIndexTarefa(new Tarefa(tarefa.getMestra(), tarefa.getAplicativo()));

                if (procurarVizinhoMaisProximo(posicaoMestra, tarefa, incremento)) {
                    contadorTarefa++;
                } else {
                    incremento++;
                    while (!procurarVizinhoMaisProximo(posicaoMestra, tarefa, incremento)) {
                        incremento++;                        
                    }
                    contadorTarefa++;

                }

            } else {
                //primeira tarefa de um novo aplicativo
                if (procuraPrimeiroLivre(tarefa)) {
                    contadorTarefa++;
                }

            }

        }

        if (contadorTarefa == listaTarefas.size()) {
            contadorAplicativo++;
            contadorTarefa = 0;
        }

    }

    private boolean procurarVizinhoMaisProximo(int[] posicaoMestra, Tarefa tarefa, int incremento) {

        int[] posicaoEscrava;

        //verifica primeira posição a direita
        if (posicaoMestra[1] + incremento < mpsoc.getNColunas()) {
            if (mpsoc.getCelulas()[posicaoMestra[0]][posicaoMestra[1] + incremento].getTarefa() == null) {
                mpsoc.getCelulas()[posicaoMestra[0]][posicaoMestra[1] + incremento].setTarefa(tarefa);
                mpsoc.getCelulas()[posicaoMestra[0]][posicaoMestra[1] + incremento].updateCor();
                mpsoc.getCelulas()[posicaoMestra[0]][posicaoMestra[1] + incremento].updateLabel();
                posicaoEscrava = new int[]{posicaoMestra[0], posicaoMestra[1] + incremento};
                mpsoc.preencherCanais(posicaoEscrava, posicaoMestra);
                return true;
            }
        }

        //verifica primeira posição a esquerda
        if (posicaoMestra[1] - incremento >= 0) {
            if (mpsoc.getCelulas()[posicaoMestra[0]][posicaoMestra[1] - incremento].getTarefa() == null) {
                mpsoc.getCelulas()[posicaoMestra[0]][posicaoMestra[1] - incremento].setTarefa(tarefa);
                mpsoc.getCelulas()[posicaoMestra[0]][posicaoMestra[1] - incremento].updateCor();
                mpsoc.getCelulas()[posicaoMestra[0]][posicaoMestra[1] - incremento].updateLabel();
                posicaoEscrava = new int[]{posicaoMestra[0], posicaoMestra[1] - incremento};
                mpsoc.preencherCanais(posicaoEscrava, posicaoMestra);
                return true;
            }
        }
        
        //verifica primeira posicao abaixo 
        if (posicaoMestra[0] + incremento < mpsoc.getNLinhas()) {
            if (mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1]].getTarefa() == null) {
                mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1]].setTarefa(tarefa);
                mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1]].updateCor();
                mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1]].updateLabel();
                posicaoEscrava = new int[]{posicaoMestra[0] + incremento, posicaoMestra[1]};
                mpsoc.preencherCanais(posicaoEscrava, posicaoMestra);
                return true;
            }
        }

        //Primeira posicao acima
        if (posicaoMestra[0] - incremento >= 0) {
            if (mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1]].getTarefa() == null) {
                mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1]].setTarefa(tarefa);
                mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1]].updateCor();
                mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1]].updateLabel();
                posicaoEscrava = new int[]{posicaoMestra[0] - incremento, posicaoMestra[1]};
                mpsoc.preencherCanais(posicaoEscrava, posicaoMestra);
                return true;
            }
        }
        
        //verifica diagonal a direita abaixo
        if (posicaoMestra[0] + incremento < mpsoc.getNLinhas() && posicaoMestra[1] + incremento < mpsoc.getNColunas()) {
            if (mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1] + incremento].getTarefa() == null) {
                mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1] + incremento].setTarefa(tarefa);
                mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1] + incremento].updateCor();
                mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1] + incremento].updateLabel();
                posicaoEscrava = new int[]{posicaoMestra[0] + incremento, posicaoMestra[1] + incremento};
                mpsoc.preencherCanais(posicaoEscrava, posicaoMestra);
                return true;
            }
        }
        
        //verifica diagonal a direita acima
        if (posicaoMestra[0] - incremento >= 0 && posicaoMestra[1] + incremento < mpsoc.getNColunas()) {
            if (mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1] + incremento].getTarefa() == null) {
                mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1] + incremento].setTarefa(tarefa);
                mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1] + incremento].updateCor();
                mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1] + incremento].updateLabel();
                posicaoEscrava = new int[]{posicaoMestra[0] - incremento, posicaoMestra[1] + incremento};
                mpsoc.preencherCanais(posicaoEscrava, posicaoMestra);
                return true;
            }
        }
        
        //verifica diagonal a esquerda acima
        if (posicaoMestra[0] - incremento >= 0 && posicaoMestra[1] - incremento >=0) {
            if (mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1] - incremento].getTarefa() == null) {
                mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1] - incremento].setTarefa(tarefa);
                mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1] - incremento].updateCor();
                mpsoc.getCelulas()[posicaoMestra[0] - incremento][posicaoMestra[1] - incremento].updateLabel();
                posicaoEscrava = new int[]{posicaoMestra[0] - incremento, posicaoMestra[1] - incremento};
                mpsoc.preencherCanais(posicaoEscrava, posicaoMestra);
                return true;
            }
        }
        
         //verifica diagonal a esquerda abaixo
        if (posicaoMestra[0] + incremento < mpsoc.getNLinhas() && posicaoMestra[1] - incremento >=0) {
            if (mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1] - incremento].getTarefa() == null) {
                mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1] - incremento].setTarefa(tarefa);
                mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1] - incremento].updateCor();
                mpsoc.getCelulas()[posicaoMestra[0] + incremento][posicaoMestra[1] - incremento].updateLabel();
                posicaoEscrava = new int[]{posicaoMestra[0] + incremento, posicaoMestra[1] - incremento};
                mpsoc.preencherCanais(posicaoEscrava, posicaoMestra);
                return true;
            }
        }

        return false;

    }

    private boolean procuraPrimeiroLivre(Tarefa tarefa) {

        int[] posicaoEscrava;

        for (int i = 0; i < mpsoc.getNLinhas(); i++) {
            for (int j = 0; j < mpsoc.getNColunas(); j++) {
                if (mpsoc.getCelulas()[i][j].getTarefa() == null) {
                    mpsoc.getCelulas()[i][j].setTarefa(tarefa);
                    mpsoc.getCelulas()[i][j].updateCor();
                    mpsoc.getCelulas()[i][j].updateLabel();
                    return true;
                }
            }
        }

        return false;

    }

}
