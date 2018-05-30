/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerais;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Lucas
 */
public class Celula {

    Tarefa tarefa;
    List<CanalCom> listaCanais;
    Rectangle imagem;
    Label texto;

    public Celula() {
        listaCanais = new ArrayList();
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public List<CanalCom> getListaCanais() {
        return listaCanais;
    }

    public void addCanal(CanalCom canal) {
        this.listaCanais.add(canal);
    }
    
    public void setImagem(Rectangle imagem){
        this.imagem = imagem;
        updateCor();
    }
    
    public Rectangle getImagem(){
        return this.imagem;
    }

    public void setLabel(Label texto){
        this.texto = texto;
        updateLabel();
    }
    
    public Label getLabel(){
        return this.texto;
    }
    
    public void updateCor() {
        
        if(this.tarefa == null){
            imagem.setFill(Color.GRAY);
        }else{
            imagem.setFill(tarefa.getAplicativo().getCor());
        }
        
    }
    
    public void updateLabel(){
        
        if(this.tarefa == null){            
            texto.setFont(Font.font("Verdana",FontWeight.BOLD,20));           
            texto.toFront();
        }else{
            texto.setText("T"+String.valueOf(this.tarefa.getNumero()));
        }
        
    }

}
