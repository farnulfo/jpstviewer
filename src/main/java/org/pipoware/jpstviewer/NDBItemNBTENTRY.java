/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pipoware.jpstviewer;

import com.google.common.io.BaseEncoding;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.pipoware.pst.exp.NID;
import org.pipoware.pst.exp.PC;
import org.pipoware.pst.exp.PCItem;
import org.pipoware.pst.exp.PSTFile;
import org.pipoware.pst.exp.PropertyDataType;
import org.pipoware.pst.exp.PropertyIdentifier;
import org.pipoware.pst.exp.pages.NBTENTRY;

/**
 *
 * @author Franck Arnulfo
 */
class NDBItemNBTENTRY extends NDBItem {
  
  private final NBTENTRY nbtentry;
  private final PSTFile pstFile;

  public NDBItemNBTENTRY(NBTENTRY nbtentry, PSTFile pstFile) {
    this.nbtentry = nbtentry;
    this.pstFile = pstFile;
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
    if (nbtentry.nid.nidType == NID.NID_TYPE_NORMAL_FOLDER ||
      nbtentry.nid.nidType == NID.NID_TYPE_NORMAL_MESSAGE) {
      try {
        PC pc = pstFile.ndb.getPCFromNBTENTRY(nbtentry);
        displayPC(pc);
        System.out.println(pc);
      } catch (IOException ex) {
        Logger.getLogger(NDBItemNBTENTRY.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  @Override
  public String toString() {
    return nbtentry.toString();
  }

  private void displayPC(PC pc) {
    Stage stage = new Stage();
    stage.setTitle("PC Inspector:");
    VBox root = new VBox();
    TableView<PCItem> table = new TableView();
    TableColumn<PCItem, String> propIdCol = new TableColumn("Prop. Id");
    TableColumn<PCItem, String>  propIdNameCol = new TableColumn("Prop. Name");
    TableColumn<PCItem, String>  propData = new TableColumn("Data");
    TableColumn<PCItem, String>  propType = new TableColumn("Type");
    
    table.getColumns().addAll(propIdCol, propIdNameCol, propType, propData);
    
    ObservableList<PCItem> data = FXCollections.observableList(pc.items);

    propIdCol.setCellValueFactory(param -> {
      final PCItem pcItem = param.getValue();
      return new SimpleStringProperty(Integer.toHexString(pcItem.propertyIdentifier));
    });

    propIdNameCol.setCellValueFactory((TableColumn.CellDataFeatures<PCItem, String> param) -> {
      final PCItem pcItem = param.getValue();
      return new SimpleStringProperty(PropertyIdentifier.MAP.get(pcItem.propertyIdentifier));
    });

    propType.setCellValueFactory((TableColumn.CellDataFeatures<PCItem, String> param) -> {
      final PCItem pcItem = param.getValue();
      return new SimpleStringProperty(pcItem.getPropertyDataType().toString());
    });

    propData.setCellValueFactory((TableColumn.CellDataFeatures<PCItem, String> param) -> {
      final PCItem pcItem = param.getValue();
      String s;
      if (pcItem.getPropertyDataType() == PropertyDataType.PtypString) {
        s = pcItem.getString();
      } else if (pcItem.dataValue == null) {
        s = "";
      } else {
        final int MAX_BYTES = 128;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(BaseEncoding.base16().withSeparator(",", 2).encode(pcItem.dataValue, 0, Math.min(pcItem.dataValue.length, MAX_BYTES)));
        sb.append("]");
        s = sb.toString();
      }
      return new SimpleStringProperty(s);
    });

    table.setItems(data);
        
    VBox.setVgrow(table, Priority.ALWAYS);
    root.getChildren().add(table);
    stage.setScene(new Scene(root, 380, 250));
    stage.show();
  }
  
}
