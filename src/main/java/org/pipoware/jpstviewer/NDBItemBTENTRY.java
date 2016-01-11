package org.pipoware.jpstviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TreeItem;
import org.pipoware.pst.exp.PSTFile;
import org.pipoware.pst.exp.pages.BTENTRY;
import org.pipoware.pst.exp.pages.Page;

/**
 *
 * @author Franck Arnulfo
 */
class NDBItemBTENTRY extends NDBItem {
  
  private final BTENTRY btentry;
  private final PSTFile pstFile;
  private final List<NDBItem> ndbItems = new ArrayList<>();
  
  public NDBItemBTENTRY(BTENTRY btentry, PSTFile pstFile) throws IOException {
    this.btentry = btentry;
    this.pstFile = pstFile;
    Page page = pstFile.ndb.getPage(btentry.bref);
    ndbItems.add(new NDBItemPage(page, pstFile));
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  @Override
  public List<NDBItem> getNDBItems() {
    return ndbItems;
  }
  
  @Override
  public String toString() {
    return btentry.toString();
  }
  
}
