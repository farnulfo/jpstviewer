package org.pipoware.jpstviewer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.pipoware.pst.exp.Folder;
import org.pipoware.pst.exp.Message;
import org.pipoware.pst.exp.PSTFile;

/**
 *
 * @author Franck Arnulfo
 */
public class PSTViewer extends Application {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("PST Viewer");

    Parent gui = buildGUI(primaryStage);
    primaryStage.setScene(new Scene(gui, 700, 500));
    primaryStage.show();
  }

  private Parent buildGUI(Stage stage) {

    final ListView<String> messageList = new ListView<>();

    SplitPane mainSplitPane = new SplitPane();
    VBox.setVgrow(mainSplitPane, Priority.ALWAYS);
    TreeView<Folder> folderTreeView = new TreeView<>();
    folderTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener(){
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        TreeItem<Folder> folder = (TreeItem<Folder>) newValue;
        messageList.getItems().clear();
        try {
          for (Message message : folder.getValue().getMessages()) {
            messageList.getItems().add(message.getSubject());
          }
        } catch (IOException ex) {
          Logger.getLogger(PSTViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      }
    });

    SplitPane rightSplitPane = new SplitPane();
    rightSplitPane.setOrientation(Orientation.VERTICAL);
    VBox.setVgrow(rightSplitPane, Priority.ALWAYS);

    TabPane bodyTabs = new TabPane();
    rightSplitPane.getItems().addAll(messageList, bodyTabs);

    mainSplitPane.getItems().addAll(folderTreeView, rightSplitPane);

    VBox vbox = new VBox();
    vbox.getChildren().add(buildMenuBar(stage, folderTreeView));
    vbox.getChildren().add(mainSplitPane);

    return vbox;
  }

  private MenuBar buildMenuBar(Stage stage, TreeView<Folder> folderTreeView) {
    final FileChooser fileChooser = new FileChooser();
    MenuBar menuBar = new MenuBar();
    Menu file = new Menu("File");
    MenuItem menuOpen = new MenuItem("Open...");
    menuOpen.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        fileChooser.setTitle("Open a Outlook PST file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PST file", "*.pst"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
          Path path = file.toPath();
          PSTFile pstFile;
          try {
            pstFile = new PSTFile(path);
            TreeItem<Folder> rootFolder = new TreeItem<>(pstFile.getMessageStore().getRootFolder());
            folderTreeView.setRoot(rootFolder);
            processFolder(rootFolder);
            
            
          } catch (IOException ex) {
            Logger.getLogger(NDBViewer.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }

      private void processFolder(TreeItem<Folder> treeItemFolder) throws IOException {
        Folder folder = treeItemFolder.getValue();
        if (folder.hasSubFolers()) {
          for (Folder f : folder.getFolders()) {
            TreeItem<Folder> tif = new TreeItem<>(f);
            treeItemFolder.getChildren().add(tif);
            processFolder(tif);
          }
        }
      }
    });

    file.getItems().add(menuOpen);
    menuBar.getMenus().add(file);
    return menuBar;
  }
}
