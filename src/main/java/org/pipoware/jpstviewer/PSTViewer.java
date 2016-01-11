package org.pipoware.jpstviewer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import org.pipoware.pst.exp.PSTFile;
import org.pipoware.pst.exp.pages.BTENTRY;
import org.pipoware.pst.exp.pages.NBTENTRY;
import org.pipoware.pst.exp.pages.Page;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.pipoware.pst.exp.pages.BBTENTRY;

/**
 *
 * @author Franck Arnulfo
 */
public class PSTViewer extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(final Stage primaryStage) throws IOException {
    primaryStage.setTitle("PST Structure Viewer");

   TreeItem<NDBItem> rootNBTItem = new TreeItem<>();
    final TreeView<NDBItem> nbtTree = new TreeView<>(rootNBTItem);
    nbtTree.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
          TreeItem<NDBItem> item = nbtTree.getSelectionModel().getSelectedItem();
          System.out.println("Selected Text : " + item.getValue());
        }
      }
    });

    TreeItem<NDBItem> rootBBTItem = new TreeItem<>();
    final TreeView<NDBItem> bbtTree = new TreeView<>(rootBBTItem);
    bbtTree.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
          TreeItem<NDBItem> item = bbtTree.getSelectionModel().getSelectedItem();
          System.out.println("Selected Text : " + item.getValue());
          item.getValue().display();
        }
      }
    });

    VBox root = new VBox();
    final FileChooser fileChooser = new FileChooser();
    MenuBar menuBar = new MenuBar();
    Menu file = new Menu("File");
    MenuItem menuOpen = new MenuItem("Open...");
    menuOpen.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        fileChooser.setTitle("Open a Outlook PST file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PST file", "*.pst"));
        File file = fileChooser.showOpenDialog(primaryStage);
        System.out.println("File:" + file);
        Path path = file.toPath();
        PSTFile pstFile;
        try {
          pstFile = new PSTFile(path);
          Page pageNBT = pstFile.ndb.getPage(pstFile.getHeader().getRoot().bRefNBT);
          Page pageBBT = pstFile.ndb.getPage(pstFile.getHeader().getRoot().bRefBBT);
          nbtTree.setRoot(NDBTreeItem.createNode(new NDBItemPage(pageNBT, pstFile)));
          bbtTree.setRoot(NDBTreeItem.createNode(new NDBItemPage(pageBBT, pstFile)));

        } catch (IOException ex) {
          Logger.getLogger(PSTViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    file.getItems().add(menuOpen);
    menuBar.getMenus().add(file);

    root.getChildren().add(menuBar);

    VBox vboxNodes = new VBox();
    Label nodeLabel = new Label("Nodes");
    VBox.setVgrow(nbtTree, Priority.ALWAYS);
    vboxNodes.getChildren().addAll(nodeLabel, nbtTree);

    VBox vboxBlocks = new VBox();
    Label blocksLabel = new Label("Blocks");
    VBox.setVgrow(bbtTree, Priority.ALWAYS);
    vboxBlocks.getChildren().addAll(blocksLabel, bbtTree);

    SplitPane splitPane = new SplitPane();
    VBox.setVgrow(splitPane, Priority.ALWAYS);
    splitPane.getItems().addAll(vboxNodes, vboxBlocks);
    root.getChildren().add(splitPane);

    primaryStage.setScene(new Scene(root, 300, 250));
    primaryStage.show();
  }
  private void addNBTPage(TreeItem<String> rootItem, PSTFile pstFile, Page page) throws IOException {
    TreeItem<String> p = new TreeItem<>(page.toString());
    if (page.getDepthLevel() > 0) {
      for (BTENTRY btentry : page.btentries) {
        String s = String.format("btKey=%1s bref=%2s", btentry.btKey, btentry.bref);
        p.getChildren().add(new TreeItem<>(s));
        Page p1 = pstFile.ndb.getPage(btentry.bref);
        addNBTPage(p, pstFile, p1);
      }
    } else {
      for (NBTENTRY nbtentry : page.nbtentries) {
        TreeItem<String> nid = new TreeItem<>(nbtentry.nid.toString());
        nid.getChildren().add(new TreeItem<>("nidParent: " + nbtentry.nidParent));
        nid.getChildren().add(new TreeItem<>("bidData: " + nbtentry.bidData));
        nid.getChildren().add(new TreeItem<>("bidSub: " + nbtentry.bidSub));
        p.getChildren().add(nid);
      }
    }
    rootItem.getChildren().add(p);
  }

  private void addBBTPage(TreeItem<String> item, PSTFile pstFile, Page page) throws IOException {
    TreeItem<String> p = new TreeItem<>(page.toString());
    if (page.getDepthLevel() > 0) {
      for (BTENTRY btentry : page.btentries) {
        String s = String.format("btKey=%1s bref=%2s", btentry.btKey, btentry.bref);
        p.getChildren().add(new TreeItem<>(s));
        Page p1 = pstFile.ndb.getPage(btentry.bref);
        addBBTPage(p, pstFile, p1);
      }
    } else {
      for (BBTENTRY bbtentry : page.bbtentries) {
        TreeItem<String> nid = new TreeItem<>(bbtentry.toString());
        p.getChildren().add(nid);
      }
    }
    item.getChildren().add(p);
  }
}
