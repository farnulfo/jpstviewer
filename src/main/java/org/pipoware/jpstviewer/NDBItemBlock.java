/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pipoware.jpstviewer;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    Preconditions.checkArgument(block.blockType == Block.BlockType.DATA_BLOCK || block.blockType == Block.BlockType.XBLOCK);
    if (block.blockType == Block.BlockType.DATA_BLOCK) {
      displayDataBlock();
    } else if (block.blockType == Block.BlockType.XBLOCK) {
      displayXBlock();
    }
  }

  private void displayDataBlock() {
    Stage stage = new Stage();
    stage.setTitle("Block Inspector: " + block.getBREF());
    VBox root = new VBox();
    TextArea blockData = new TextArea();
    blockData.setFont(Font.font(java.awt.Font.MONOSPACED));
    blockData.setWrapText(true);
    blockData.setEditable(false);
    blockData.setText(getStringData(block.getBREF().getIb(), block.data, 12));
    VBox.setVgrow(blockData, Priority.ALWAYS);
    root.getChildren().add(blockData);
    stage.setScene(new Scene(root, 380, 250));
    stage.show();
  }

  private void displayXBlock() {
    Stage stage = new Stage();
    stage.setTitle("XBlock Inspector: " + block.getBREF());
    VBox root = new VBox();
    ListView<String> bids = new ListView<>();
    for (int i = 0; i < block.rgbid.length; i++) {
      bids.getItems().add("rgbid[" + i + "] = 0x" +Long.toHexString(block.rgbid[i]));
    }
    VBox.setVgrow(bids, Priority.ALWAYS);
    root.getChildren().add(bids);
    stage.setScene(new Scene(root, 380, 250));
    stage.show();
  }

  @Override
  public String toString() {
    return block.toString();
  }

  private String getStringData(long startAddress, byte[] bytes, int nbBytesPerLine) {
    StringBuilder sb = new StringBuilder();
    for (int offset = 0; offset < bytes.length; offset += nbBytesPerLine, startAddress += nbBytesPerLine) {
      sb.append(Long.toHexString(startAddress).toUpperCase());
      sb.append(": ");
      int len = (offset + nbBytesPerLine > bytes.length) ? bytes.length - offset : nbBytesPerLine;
      sb.append(BaseEncoding.base16().withSeparator(" ", 2).encode(bytes, offset, len));
      sb.append(System.lineSeparator());
    }
    return sb.toString();
  }
}
