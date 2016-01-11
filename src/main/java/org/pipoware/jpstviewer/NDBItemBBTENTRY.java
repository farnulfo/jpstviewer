/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pipoware.jpstviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pipoware.pst.exp.Block;
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
