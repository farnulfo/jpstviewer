/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pipoware.jpstviewer;

import org.pipoware.pst.exp.PSTFile;
import org.pipoware.pst.exp.pages.NBTENTRY;

/**
 *
 * @author Franck Arnulfo
 */
class NDBItemNBTENTRY extends NDBItem {
  
  private final NBTENTRY nbtentry;

  public NDBItemNBTENTRY(NBTENTRY nbtentry, PSTFile pstFile) {
    this.nbtentry = nbtentry;
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
  public String toString() {
    return nbtentry.toString();
  }
  
}
