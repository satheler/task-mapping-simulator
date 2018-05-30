/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerais;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Lucas
 */
public class LeitorArquivo {

    private static File arquivo;
    private static int quantidadeTarefas;

    public static File carregarArquivo() {

        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("CSV file", "csv");
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        int retorno = chooser.showOpenDialog(null);

        if (retorno == JFileChooser.APPROVE_OPTION) {

            arquivo = chooser.getSelectedFile();

            JOptionPane.showMessageDialog(null, "Arquivo importado com sucesso");
            return arquivo;

        }

        if (arquivo == null) {
            JOptionPane.showMessageDialog(null, "Arquivo Não Selecionado");
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum arquivo foi selecionado será mantido o anterior");
        }

        return arquivo;
    }

    public static ArrayList montarLista(File arquivo) {

        FileReader reader;
        BufferedReader br;

        ArrayList<Aplicativo> listaAplicativos = new ArrayList();

        try {
            reader = new FileReader(arquivo);
            br = new BufferedReader(reader);

            String[] entradaArray;
            String entradaString;
            String nomeApp;

            int numeroTarefa;
            int tarefaEscrava;
            int larguraIda;
            int larguraVolta;
            Aplicativo aplicativo = null;
            Tarefa tarefa = null;
            Tarefa escrava = null;

            while ((entradaString = br.readLine()) != null) {

                entradaArray = entradaString.split(",");
                nomeApp = entradaArray[0];
                numeroTarefa = Integer.parseInt(entradaArray[1]);
                tarefaEscrava = Integer.parseInt(entradaArray[2]);
                larguraIda = Integer.parseInt(entradaArray[3]);
                larguraVolta = Integer.parseInt(entradaArray[4]);

                if (aplicativo == null) {
                    //caso ainda não exista uma aplicativo
                    aplicativo = new Aplicativo(nomeApp);
                    tarefa = new Tarefa(numeroTarefa, aplicativo);
                    escrava = new Tarefa(tarefaEscrava, aplicativo, larguraIda, larguraVolta, numeroTarefa);
                    aplicativo.addTarefa(tarefa);
                    aplicativo.addTarefa(escrava);
                    listaAplicativos.add(aplicativo);

                } else if (aplicativo.getNome().equals(nomeApp)) {
                    
                    tarefa = new Tarefa(numeroTarefa, aplicativo);
                    escrava = new Tarefa(tarefaEscrava, aplicativo, larguraIda, larguraVolta, numeroTarefa);
                    
                    if (!aplicativo.getTarefas().contains(tarefa)) {
                        aplicativo.addTarefa(tarefa);
                    }
                    aplicativo.addTarefa(escrava);

                } else {
                    //ja existe um aplicativo mas é encontrado um novo na lista    
                    aplicativo = new Aplicativo(nomeApp);
                    
                    tarefa = new Tarefa(numeroTarefa, aplicativo);
                    escrava = new Tarefa(tarefaEscrava, aplicativo, larguraIda, larguraVolta, numeroTarefa);
                    
                    aplicativo.addTarefa(tarefa);
                    aplicativo.addTarefa(escrava);
                    listaAplicativos.add(aplicativo);
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(LeitorArquivo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaAplicativos;
    }

}
