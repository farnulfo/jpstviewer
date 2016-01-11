/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pipoware.jpstviewer;

import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.pipoware.pst.exp.Block;

/**
 *
 * @author Franck Arnulfo
 */
class NDBItemBlock extends NDBItem {

  private final Block block;

  public NDBItemBlock(Block block) {
    this.block = block;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public Iterable<NDBItem> getNDBItems() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void display() {
    System.out.println("Block:" + block);
    System.out.println("Data: " + block.data);
    Stage stage = new Stage();
    stage.setTitle("Display Block");
    VBox root = new VBox();
    SplitPane splitPane = new SplitPane();
    VBox.setVgrow(splitPane, Priority.ALWAYS);
    stage.setScene(new Scene(root, 300, 250));
    stage.show();
  }

  @Override
  public String toString() {
    return block.toString();
  }

}
