package org.pipoware.jpstviewer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pipoware.pst.exp.PSTFile;
import org.pipoware.pst.exp.pages.BBTENTRY;
import org.pipoware.pst.exp.pages.BTENTRY;
import org.pipoware.pst.exp.pages.NBTENTRY;
import org.pipoware.pst.exp.pages.Page;

/**
 *
 * @author Franck Arnulfo
 */
public class NDBItemPage extends NDBItem {

  private final Page page;
  private final List<NDBItem> ndbItems = new ArrayList<>();
  private boolean isFirstTimeChildren = true;
  private final PSTFile pstFile;

  public NDBItemPage(Page page, PSTFile pstFile) throws IOException {
    this.page = page;
    this.pstFile = pstFile;
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  @Override
  public List<NDBItem> getNDBItems() {
    if (isFirstTimeChildren) {
      isFirstTimeChildren = false;
      if (page.getDepthLevel() > 0) {
        page.getcEnt();
        page.getType();
        Preconditions.checkArgument(page.getType() == Page.PageType.ptypeNBT || page.getType() == Page.PageType.ptypeBBT);
        for (BTENTRY btentry : page.btentries) {
          try {
            ndbItems.add(new NDBItemBTENTRY(btentry, pstFile));
          } catch (IOException ex) {
            Logger.getLogger(NDBItemPage.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      } else if (page.getType() == Page.PageType.ptypeBBT) {
        for (BBTENTRY bttentry : page.bbtentries) {
          try {
            ndbItems.add(new NDBItemBBTENTRY(bttentry, pstFile));
          } catch (IOException ex) {
            Logger.getLogger(NDBItemPage.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      } else if (page.getType() == Page.PageType.ptypeNBT) {
        for (NBTENTRY nbtentry : page.nbtentries) {
            ndbItems.add(new NDBItemNBTENTRY(nbtentry, pstFile));
        }
      }
    }

    return ndbItems;
  }

  @Override
  public String toString() {
    String s = MoreObjects.toStringHelper("Page")
      .add("type", page.getType())
      .add("cEnt", page.getcEnt())
      .add("cEntMax", page.getcEntMax())
      .add("cbEnt", page.getcbEnt())
      .add("cLevel", page.getcLevel()).toString();
    return s;
  }
}
