/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pipoware.jpstviewer;

import com.google.common.io.BaseEncoding;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
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
    stage.setTitle("Block Inspector");
    VBox root = new VBox();
    TextArea blockData = new TextArea();
    blockData.setWrapText(true);
    blockData.setText("[" + BaseEncoding.base16().withSeparator(",", 2).encode(block.data) + "]");
    VBox.setVgrow(blockData, Priority.ALWAYS);
    root.getChildren().add(blockData);
    stage.setScene(new Scene(root, 300, 250));
    stage.show();
  }

  @Override
  public String toString() {
    return block.toString();
  }

}
