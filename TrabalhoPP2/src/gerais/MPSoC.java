/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerais;

/**
 *
 * @author Lucas
 */
public class MPSoC {

    private Celula[][] celulas;
    private int linhas;
    private int colunas;

    public MPSoC(int nLinhas, int nColunas) {
        celulas = new Celula[nLinhas][nColunas];
        this.linhas = nLinhas;
        this.colunas = nColunas;

        for (int i = 0; i < nLinhas; i++) {
            for (int j = 0; j < nColunas; j++) {
                celulas[i][j] = new Celula();
            }
        }

    }

    public void addCelula(Celula celula, int linha, int coluna) {
        this.celulas[linha][coluna] = celula;
    }

    public Celula getCelula(int linha, int coluna) {
        return this.celulas[linha][coluna];
    }

    public Celula[][] getCelulas() {
        return this.celulas;
    }

    public int getNLinhas() {
        return this.linhas;
    }

    public int getNColunas() {
        return this.colunas;
    }

    public int[] getIndexTarefa(Tarefa tarefa) {

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {

                if (celulas[i][j].getTarefa() != null) {
                    if (celulas[i][j].getTarefa().equals(tarefa)) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        return null;
    }

    public void preencherCanais(int[] posicaoEscrava, int[] posicaoMestra) {

        if (posicaoMestra[0] == posicaoEscrava[0]) {
            andarEmX(posicaoEscrava, posicaoMestra);
        } else {
            if (posicaoMestra[0] < posicaoEscrava[0]) {
                andarEmX(posicaoEscrava, posicaoMestra);
                andarYNegativamente(posicaoEscrava, posicaoMestra);
            } else {
                andarEmX(posicaoEscrava, posicaoMestra);
                andarYPositivamente(posicaoEscrava, posicaoMestra);
            }
        }

    }

    private void andarEmX(int[] posicaoEscrava, int[] posicaoMestra) {
        if (posicaoMestra[1] < posicaoEscrava[1]) {
            andarXNegativamente(posicaoEscrava, posicaoMestra);
        } else {
            andarXPositivamente(posicaoEscrava, posicaoMestra);
        }
    }

    private void andarXNegativamente(int[] posicaoEscrava, int[] posicaoMestra) {
        for (int i = posicaoEscrava[1]; i > posicaoMestra[1]; i--) {
            for (CanalCom canal : celulas[posicaoMestra[0]][i].getListaCanais()) {
                if (canal.equals(new CanalCom(posicaoMestra[0], i - 1, posicaoMestra[0], i))) {
                    canal.addCargaIda(celulas[posicaoEscrava[0]][posicaoEscrava[1]].getTarefa().getLarguraIda());
                    canal.addCargaVolta(celulas[posicaoEscrava[0]][posicaoEscrava[1]].getTarefa().getLarguraVolta());
                    canal.updateCor();
                    canal.updateLabel();
                }
            }
        }
    }

    private void andarXPositivamente(int[] posicaoEscrava, int[] posicaoMestra) {
        for (int i = posicaoEscrava[1]; i < posicaoMestra[1]; i++) {
            for (CanalCom canal : celulas[posicaoEscrava[0]][i].getListaCanais()) {
                if (canal.equals(new CanalCom(posicaoEscrava[0], i, posicaoEscrava[0], i + 1))) {
                    canal.addCargaIda(celulas[posicaoEscrava[0]][posicaoEscrava[1]].getTarefa().getLarguraIda());
                    canal.addCargaVolta(celulas[posicaoEscrava[0]][posicaoEscrava[1]].getTarefa().getLarguraVolta());
                    canal.updateCor();
                    canal.updateLabel();

                }
            }
        }
    }

    private void andarYNegativamente(int[] posicaoEscrava, int[] posicaoMestra) {
        for (int i = posicaoEscrava[0]; i > posicaoMestra[0]; i--) {
            for (CanalCom canal : celulas[i][posicaoMestra[1]].getListaCanais()) {
                if (canal.equals(new CanalCom(i - 1, posicaoMestra[1], i, posicaoMestra[1]))) {
                    canal.addCargaIda(celulas[posicaoEscrava[0]][posicaoEscrava[1]].getTarefa().getLarguraIda());
                    canal.addCargaVolta(celulas[posicaoEscrava[0]][posicaoEscrava[1]].getTarefa().getLarguraVolta());
                    canal.updateCor();
                    canal.updateLabel();
                }
            }
        }
    }

    private void andarYPositivamente(int[] posicaoEscrava, int[] posicaoMestra) {
        for (int i = posicaoEscrava[0]; i < posicaoMestra[0]; i++) {
            for (CanalCom canal : celulas[i][posicaoMestra[1]].getListaCanais()) {
                if (canal.equals(new CanalCom(i, posicaoMestra[1], i + 1, posicaoMestra[1]))) {
                    canal.addCargaIda(celulas[posicaoEscrava[0]][posicaoEscrava[1]].getTarefa().getLarguraIda());
                    canal.addCargaVolta(celulas[posicaoEscrava[0]][posicaoEscrava[1]].getTarefa().getLarguraVolta());
                    canal.updateCor();
                    canal.updateLabel();
                }
            }
        }
    }

}
