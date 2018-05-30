/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerais;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Lucas
 */
public class CanalCom {

    int cargaIda = 0;
    int cargaVolta = 0;
    int[] posicaoInicial;
    int[] posicaoFinal;
    Rectangle imagem;
    Label texto;

    public CanalCom(int xInicial, int yInicial, int xFinal, int yFinal) {

        posicaoInicial = new int[2];
        posicaoInicial[0] = xInicial;
        posicaoInicial[1] = yInicial;

        posicaoFinal = new int[2];
        posicaoFinal[0] = xFinal;
        posicaoFinal[1] = yFinal;

    }

    public int getCargaIda() {
        return cargaIda;
    }

    public void addCargaIda(int cargaIda) {
        this.cargaIda += cargaIda;
    }

    public int getCargaVolta() {
        return cargaVolta;
    }

    public void addCargaVolta(int cargaVolta) {
        this.cargaVolta += cargaVolta;
    }

    public int[] getPosicaoInicial() {
        return posicaoInicial;
    }

    public int[] getPosicaoFinal() {
        return posicaoFinal;
    }

    public void setImagem(Rectangle imagem) {

        this.imagem = imagem;
        updateCor();

    }

    public void setLabel(Label texto) {

        this.texto = texto;
        texto.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        texto.toFront();
        updateLabel();
    }

    public void updateLabel() {
        texto.setText("[" + String.valueOf(this.cargaIda) + "|" + String.valueOf(this.cargaVolta) + "]");
    }

    public void updateCor() {
        if (this.cargaIda < 50 || this.cargaVolta < 50) {
            this.imagem.setFill(Color.GREEN);
        }
        if (this.cargaIda >= 50 || this.cargaVolta >= 50) {
            this.imagem.setFill(Color.YELLOW);
        }
        if (this.cargaIda > 90 || this.cargaVolta > 90) {
            this.imagem.setFill(Color.RED);
        }
    }

    @Override
    public boolean equals(Object o) {

        CanalCom outroCanal = (CanalCom) o;

        return (this.getPosicaoInicial()[0] == outroCanal.getPosicaoInicial()[0] && this.getPosicaoInicial()[1] == outroCanal.getPosicaoInicial()[1]
                && this.getPosicaoFinal()[0] == outroCanal.getPosicaoFinal()[0] && this.getPosicaoFinal()[1] == outroCanal.getPosicaoFinal()[1]);

    }

}
