package org.pipoware.jpstviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.pipoware.pst.exp.PSTFile;
import org.pipoware.pst.exp.pages.BBTENTRY;

/**
 *
 * @author Franck Arnulfo
 */
class NDBItemBBTENTRY extends NDBItem {
  
  private final BBTENTRY bbtentry;
  private final PSTFile pstFile;
  private final List<NDBItem> ndbItems = new ArrayList<>();

  public NDBItemBBTENTRY(BBTENTRY bttentry, PSTFile pstFile) throws IOException {
    this.bbtentry = bttentry;
    this.pstFile = pstFile;
    
    ndbItems.add(new NDBItemBlock(pstFile.ndb.getBlockFromBID(bbtentry.bref.getBid())));
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  @Override
  public Iterable<NDBItem> getNDBItems() {
    return ndbItems;
  }
  

  
  @Override
  public String toString() {
    return bbtentry.toString();
  }
  
}
