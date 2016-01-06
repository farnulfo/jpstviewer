package org.pipoware.jpstviewer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import org.pipoware.pst.exp.PSTFile;
import org.pipoware.pst.exp.pages.BTENTRY;
import org.pipoware.pst.exp.pages.NBTENTRY;
import org.pipoware.pst.exp.pages.Page;
import static javafx.application.Application.launch;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.pipoware.pst.exp.pages.BBTENTRY;

/**
 *
 * @author Franck Arnulfo
 */
public class PSTViewer extends Application {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("need a filename!");
      System.exit(0);
    }
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    primaryStage.setTitle("PST Structure Viewer");

    String filename = getParameters().getRaw().get(0);
    Path path = Paths.get(filename);
    PSTFile pstFile = new PSTFile(path);
    Page pageNBT = pstFile.ndb.getPage(pstFile.getHeader().getRoot().bRefNBT);
    Page pageBBT = pstFile.ndb.getPage(pstFile.getHeader().getRoot().bRefBBT);

    TreeItem<String> rootNBTItem = new TreeItem<>("NBT");
    rootNBTItem.setExpanded(true);
    addNBTPage(rootNBTItem, pstFile, pageNBT);
    TreeView<String> nbtTree = new TreeView<>(rootNBTItem);

    TreeItem<String> rootBBTItem = new TreeItem<>("BBT");
    rootBBTItem.setExpanded(true);
    addBBTPage(rootBBTItem, pstFile, pageBBT);
    TreeView<String> bbtTree = new TreeView<>(rootBBTItem);

    VBox root = new VBox();
    SplitPane splitPane = new SplitPane();
    VBox.setVgrow(splitPane, Priority.ALWAYS);

    splitPane.getItems().addAll(nbtTree, bbtTree);
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
