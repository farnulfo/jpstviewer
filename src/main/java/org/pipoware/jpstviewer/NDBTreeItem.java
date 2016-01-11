/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pipoware.jpstviewer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Franck Arnulfo
 */
public class NDBTreeItem extends TreeItem<NDBItem> {

  private boolean isFirstTimeChildren = true;

  private NDBTreeItem(NDBItem ndbItem) {
    super(ndbItem);
  }

  public static TreeItem<NDBItem> createNode(NDBItem ndbItem) {
    return new NDBTreeItem(ndbItem);
  }

  @Override
  public ObservableList<TreeItem<NDBItem>> getChildren() {
    if (isFirstTimeChildren) {
      isFirstTimeChildren = false;
      super.getChildren().setAll(buildChildren(this));
    }
    return super.getChildren();
  }

  @Override
  public boolean isLeaf() {
    return getValue().isLeaf();
  }

  private ObservableList<TreeItem<NDBItem>> buildChildren(TreeItem<NDBItem> treeItem) {
    NDBItem anNDBItem = treeItem.getValue();
    if (!anNDBItem.isLeaf()) {
      ObservableList<TreeItem<NDBItem>> children = FXCollections.observableArrayList();
      for (NDBItem i : anNDBItem.getNDBItems()) {
        children.add(createNode(i));
      }
      return children;
    }

    return FXCollections.emptyObservableList();
  }
  
//  @Override
//  public String toString() {
//    return ndbItem.toString();
//  }
}
